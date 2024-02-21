package com.wify.smart.home.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.AccessoriesObject;
import com.wify.smart.home.dto.UserObject;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConnectMiniserverActivity extends AppCompatActivity {

    public static final int MULTIPLE_PERMISSIONS = 10;

    ImageView imageLayout;

    LinearLayout connect_to_wifi, passwordLayoutId;

    Button nextButton;

    EditText passwordEditText;

    String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
    };

    WifiInfo wifiInfo = null;

    String MQTT = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.connect_miniserver_activity);

            imageLayout = findViewById(R.id.imageLayout);

            connect_to_wifi = findViewById(R.id.connect_to_wifi);

            passwordLayoutId = findViewById(R.id.passwordLayoutId);

            nextButton = findViewById(R.id.nextButton);

            passwordEditText = findViewById(R.id.passwordEditText);

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    wifiInfo = getWifiInfo();

                    MQTT = Utility.OnlineVersions.getMQTT();

                    if (MQTT == null) {

                        Toast.makeText(ConnectMiniserverActivity.this, R.string.connect_app_online_txt, Toast.LENGTH_SHORT).show();

                        return;

                    }

                    if (wifiInfo != null) {

                        if (registerMiniserver(Utility.connectedHome.getHome_uid(), MQTT, "MINISERVER_" + SharedPreference.getConnectedHome(getApplicationContext()).getHome(), passwordEditText.getText().toString(), SharedPreference.getConnectedHome(getApplicationContext()).getHome(), Utility.getDate_inDDMMYY())) {

                            Intent intent = new Intent(getApplicationContext(), ConnectMiniserverActivitySuccess.class);

                            startActivity(intent);

                        }

                    } else {

                        imageLayout.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.connect_miniserver));

                        connect_to_wifi.setVisibility(View.VISIBLE);

                        passwordLayoutId.setVisibility(View.GONE);

                        Toast.makeText(ConnectMiniserverActivity.this, getString(R.string.wifi_connect_txt) + "\"" + UtilityConstants.REGISTER_MINISERVER_AP + "\"", Toast.LENGTH_SHORT);

                    }

                }
            });

            connect_to_wifi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

                    imageLayout.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.miniserver_connect_success));

                    connect_to_wifi.setVisibility(View.GONE);

                    passwordLayoutId.setVisibility(View.VISIBLE);

                }
            });

            findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onBackPressed();

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public WifiInfo getWifiInfo() {

        try {

            if (checkPermissions()) {

                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

                WifiInfo wifiInfo;

                wifiInfo = wifiManager.getConnectionInfo();

                if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {

                    return wifiInfo;

                }

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    private boolean checkPermissions() {

        try {

            int result;

            List<String> listPermissionsNeeded = new ArrayList<>();

            for (String p : permissions) {

                result = ContextCompat.checkSelfPermission(ConnectMiniserverActivity.this, p);

                if (result != PackageManager.PERMISSION_GRANTED) {

                    listPermissionsNeeded.add(p);
                }
            }
            if (!listPermissionsNeeded.isEmpty()) {

                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);

                return false;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return true;
    }


    public boolean registerMiniserver(String HOMEUID, String MQTT, String APSSID, String APPWD, String HOME, String DATE) {

        String currentDate[] = Utility.getCurrentDate().split("#");

        String dateParam = "&Y=" + currentDate[0] + "&MM=" + currentDate[1] + "&D=" + currentDate[2] + "&H=" + currentDate[3] + "&M=" + currentDate[4] + "&S=" + currentDate[5];

        UtilityConstants.URL = "http://192.168.4.1:91/register?HOMEUID=" + HOMEUID + "&MQTT=" + MQTT + "&APSSID=" + APSSID + "&APPWD=" + APPWD
                + "&HOME=" + HOME + "&DATE=" + DATE + dateParam;

        HttpURLConnection con = null;

        System.out.println("URL >>>>" + UtilityConstants.URL);

        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            java.net.URL obj = new URL(UtilityConstants.URL);

            con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod(UtilityConstants.GET_TXT);

            con.setConnectTimeout(10000);

            if (con.getResponseCode() == 200) {

                InputStream inputStream = con.getInputStream();

                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder response = new StringBuilder();

                String currentLine;

                while ((currentLine = in.readLine()) != null) {

                    response.append(currentLine);

                }

                in.close();

                if (response.toString().contains(UtilityConstants.OK_TXT)) {

                    System.out.println(">>>>> response >>>>>"+response.toString());

                    String miniserver_response = response.toString().replace("OK#", "");

                    String split[] = miniserver_response.toString().split("#");

                    AccessoriesObject accessoriesObject = new AccessoriesObject();

                    accessoriesObject.setAccessory(UtilityConstants.MINISERVER_ACCESORY);

                    accessoriesObject.setDate(Utility.getDate_inDDMMYY());

                    accessoriesObject.setState(UtilityConstants.ACCESORY_WORKING_STATE);

                    accessoriesObject.setMac(split[0]);

                    accessoriesObject.setReal_mac(split[0]);

                    accessoriesObject.setEsp_now(split[1]);

                    accessoriesObject.setLevel("0");

                    HashMap<String, AccessoriesObject> accessoriesObjects = Utility.connectedHome.getAccessories();

                    accessoriesObjects.put(accessoriesObject.getMac(), accessoriesObject);

                    System.out.println(">>>>> accessoriesObjects >>>>"+accessoriesObjects.toString());

                    Utility.connectedHome.setAccessories(accessoriesObjects);

                    Utility.connectedHome.setMqtt(MQTT);

                    Utility.UpdateHome(Utility.connectedHome);

                    SetHostUserObj();

                    SharedPreference.setAccessorySyncFlag(getApplicationContext(), true);

                    SharedPreference.setAccessories(getApplicationContext(), accessoriesObjects);

                }

                System.out.println(">>>>>>>>>>>>>>>> response: " + response.toString());

                return true;
            }

        } catch (ConnectException e) {

            e.printStackTrace();

            Toast.makeText(this, R.string.connect_failed_txt, Toast.LENGTH_LONG).show();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            con.disconnect();

        }

        return false;
    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    public void SetHostUserObj() {

        if (Utility.dbObject != null && Utility.dbObject.getName() != null) {

            String phn_no = SharedPreference.getPhone_number(getApplicationContext());

            String name = Utility.dbObject.getName();

            UserObject userObject = new UserObject(UtilityConstants.USER + phn_no, phn_no, name, "", "" + System.currentTimeMillis(), "", UtilityConstants.USER_HOST);

            SharedPreference.setHostUser(getApplicationContext(), userObject);

        } else {

            Toast.makeText(getApplicationContext(), R.string.connect_internet_err, Toast.LENGTH_SHORT);

        }

    }

}
