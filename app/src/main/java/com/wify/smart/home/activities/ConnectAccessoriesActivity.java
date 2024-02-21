package com.wify.smart.home.activities;

import android.Manifest;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.dto.AccessoriesObject;
import com.wify.smart.home.dto.ParentObject;
import com.wify.smart.home.helper.CustomPopups;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectAccessoriesActivity extends AppCompatActivity {

    public static final int MULTIPLE_PERMISSIONS = 10;

    String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static String module = null;

    ImageView imageLayout;

    LinearLayout connect_to_wifi, passwordLayoutId;

    Button nextButton;

    public static ProgressBar progressBar;

    WifiInfo wifiInfo = null;

    WifiManager wifiManager = null;

    List<String> listPermissionsNeeded = null;

    public static boolean isNext = false;

    public WifiInfo getWifiInfo() {

        try {

            if (checkPermissions()) {

                wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            module = (String) getIntent().getSerializableExtra(UtilityConstants.MODULE_TXT);

            System.out.println(">>>>> module >>>>>" + module);

            setContentView(R.layout.connect_accessories_activity);

            isNext = false;

            imageLayout = findViewById(R.id.imageLayout);

            progressBar = findViewById(R.id.progressBar);

            connect_to_wifi = findViewById(R.id.connect_to_wifi);

            passwordLayoutId = findViewById(R.id.passwordLayoutId);

            nextButton = findViewById(R.id.nextButton);

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    nextButton.setVisibility(View.GONE);

                    progressBar.setVisibility(View.VISIBLE);

                    wifiInfo = getWifiInfo();

                    if (wifiInfo != null) {

                        if (module.equalsIgnoreCase(UtilityConstants.CONTROLLER_MODULE)) {

                            System.out.println(">>>>> opening popup>>>>>>");
                            new CustomPopups().showPopupForController(v);

                        } else {

                            onModule();

                            new Timer().scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {

                                    if (!isNext) {

                                        parentRange(getApplicationContext());

                                    }

                                }
                            }, 0, 2000);

                        }

                    } else {

                        imageLayout.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.connect_netwok_accessories_success));

                        connect_to_wifi.setVisibility(View.VISIBLE);

                        passwordLayoutId.setVisibility(View.GONE);

                        Toast.makeText(ConnectAccessoriesActivity.this, getString(R.string.wifi_connect_txt) + " \"" + module + "\"", Toast.LENGTH_SHORT);

                    }

                }
            });

            connect_to_wifi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

                    imageLayout.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.connect_netwok_accessories_success));

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

    public static String[] getStrings(String s) {
        return s.split("=");
    }

    public static void parentRange(Context context) {

        HttpURLConnection con = null;

        try {

            HashMap<String, AccessoriesObject> accessoriesObjects = SharedPreference.getAccessories(context);

            UtilityConstants.URL = "http://192.168.4.1:91/parentRange";

            System.out.println(">>>> URL <<<" + UtilityConstants.URL);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            java.net.URL obj = new URL(UtilityConstants.URL);

            con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod(UtilityConstants.GET_TXT);

            con.setConnectTimeout(60000);

            con.setReadTimeout(120000);

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

                    String data = response.toString().replace("OK#", "");

                    System.out.println("data >>>>>>>>>>>>>>>>>" + data);

                    HashMap<String, ParentObject> parentObjectHashMap = new HashMap<>();

                    String dataSplit[] = data.split(",");

                    if (dataSplit.length > 0) {

                        for (int i = 0; i < dataSplit.length; i++) {

                            if (dataSplit[i].contains(UtilityConstants.MINISERVER) || dataSplit[i].contains(UtilityConstants.CONTROLLER_TXT)) {

                                String[] pSplit = getStrings(dataSplit[i]);

                                String first[] = pSplit[0].split("_", 2);

                                System.out.println(">>>>> pSplit >>>>" + new Gson().toJson(pSplit));

                                for (AccessoriesObject accessoriesObject : accessoriesObjects.values()) {

                                    if (first[0].contains(UtilityConstants.MINISERVER) && first[1].contains(Utility.connectedHome.getHome()) && accessoriesObject.getAccessory().equalsIgnoreCase(UtilityConstants.MINISERVER_ACCESORY)) {

                                        if (accessoriesObject.getChildMAC() != null && accessoriesObject.getChildMAC().split(",").length < 20) {

                                            ParentObject parentObject = new ParentObject(pSplit[0], pSplit[1], accessoriesObject.getEsp_now());

                                            parentObjectHashMap.put(parentObject.getName(), parentObject);

                                        }

                                    }

                                    if (first[1].contains(accessoriesObject.getMac())) {

                                        ParentObject parentObject = new ParentObject(pSplit[0], pSplit[1], accessoriesObject.getReal_mac());

                                        parentObjectHashMap.put(parentObject.getName(), parentObject);

                                    }

                                }

                            }

                        }

                        ArrayList<ParentObject> parentObjects = new ArrayList<>();

                        for (Map.Entry<String, ParentObject> entry : parentObjectHashMap.entrySet()) {

                            parentObjects.add(entry.getValue());

                        }

                        System.out.println(">>>> parentObjects >>>>" + new Gson().toJson(parentObjects));

                        if (parentObjects.size() > 0 && !isNext) {

                            Intent intent = new Intent(context, ConnectAccessoriesWithRoomActivity.class);

                            intent.putExtra(UtilityConstants.MODULE_TXT, module);

                            intent.putExtra("isHmodule", "false");

                            intent.putExtra("parentObjects", parentObjects);

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            context.startActivity(intent);

                            isNext = true;

                        }

                    }

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            con.disconnect();

        }

    }

    private boolean checkPermissions() {

        try {

            int result;

            listPermissionsNeeded = new ArrayList<>();

            for (String p : permissions) {

                result = ContextCompat.checkSelfPermission(ConnectAccessoriesActivity.this, p);

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

    public static boolean onModule() {

        System.out.println(">>>>>>>>>>>>>>>> in onModule >>>>>>>>>>>>>>");

        HttpURLConnection con = null;

        try {

            UtilityConstants.URL = "http://192.168.4.1:91/on";

            System.out.println(" UtilityConstants.URL >>>>>" + UtilityConstants.URL);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            java.net.URL obj = new URL(UtilityConstants.URL);

            con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod(UtilityConstants.GET_TXT);

            con.setConnectTimeout(20000);

            if (con.getResponseCode() == 200) {

            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            con.disconnect();

        }

        return false;
    }
}


