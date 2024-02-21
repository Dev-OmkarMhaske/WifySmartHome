package com.wify.smart.home.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.dto.AccessoriesObject;
import com.wify.smart.home.dto.HomeObject;
import com.wify.smart.home.dto.VersionObject;
import com.wify.smart.home.dto.WifyThemes;
import com.wify.smart.home.home.MainActivity;
import com.wify.smart.home.mqtt.MqttClient;
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

public class LoadingActivity extends AppCompatActivity {

    public static final int MULTIPLE_PERMISSIONS = 10;

    String HOMEUID = null;

    boolean flag;

    String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
    };

    WifiInfo wifiInfo = null;

    String wifi_ip = null;

    ImageView miniserver_img;

    int result;

    public static Handler UIHandler = null;

    List<String> listPermissionsNeeded = new ArrayList<>();

    public static void getOnlineVersions() {

        try {

            Utility.firebaseDatabase.getReference(UtilityConstants.VERSION).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        Utility.OnlineVersions = dataSnapshot.getValue(VersionObject.class);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void connectMQTT(Context context, String HOMEUID) {

        try {

            new BackgroundTask().execute();
/*
            String host = "tcp://" + Utility.COMMUNICATION_MODE_IP + ":20518";

            System.out.println("host >>>>>" + host);

            if (MqttClient.mqttAndroidClient != null && MqttClient.mqttAndroidClient.isConnected()) {

                MqttClient.mqttAndroidClient.disconnect();

            }

            SharedPreference.setMQTTConnect(context, UtilityConstants.TRUE_TXT);

            SplashScreenActivity.allowReadyCall = true;

            SplashScreenActivity.mqttClient.initializeVariable();

            SplashScreenActivity.mqttClient.connect(context, HOMEUID, host);

            SplashScreenActivity.mqttClient.publishMessage(HOMEUID, "");
*/

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            Utility.ResetAttribute();

            super.onCreate(savedInstanceState);

            if (SharedPreference.getTheme(getApplicationContext()) != null) {

                WifyThemes wifyThemes = SharedPreference.getTheme(getApplicationContext());

                setTheme(wifyThemes.getTheme());

            }

            wifiInfo = getWifiInfo();

            if (wifiInfo != null) {

                WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                wifi_ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

            }

            setContentView(R.layout.loading_activity);

            miniserver_img = findViewById(R.id.miniserver_img);

            HomeObject homeObject = (HomeObject) getIntent().getSerializableExtra(UtilityConstants.HOME_OBJ);

            Utility.connectedHome = homeObject;

            getHomeData(homeObject);

            String home_text = ((TextView) findViewById(R.id.home_connect)).getText().toString();

            ((TextView) findViewById(R.id.home_connect)).setText(home_text + " \"" + homeObject.getHome() + "\" Miniserver");

            Utility.isHomeConnected = true;

            Utility.isHost = SharedPreference.getUserHostedHomes(getApplicationContext()).containsKey(homeObject.getHome_uid());

            System.out.println("isHost >>>>>" + Utility.isHost);

            System.out.println(">>>> connectedHome >>" + new Gson().toJson(homeObject));

            HashMap<String, AccessoriesObject> accessoriesObjects = SharedPreference.getAccessories(getApplicationContext());

            if (accessoriesObjects != null && accessoriesObjects.size() > 0 && Utility.connectedHome.getAccessories().size() != accessoriesObjects.size() && Utility.isHost) {

                Utility.connectedHome.setAccessories(SharedPreference.getAccessories(getApplicationContext()));

                Utility.UpdateHome(Utility.connectedHome);

            }

            SharedPreference.setConnectedHome(getApplicationContext(), Utility.connectedHome);

            getOnlineVersions();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void getHomeData(HomeObject homeObject) {

        try {

            flag = false;

            if (homeObject != null && homeObject.getHome_uid() != null) {

                HOMEUID = homeObject.getHome_uid();

            }

            if (UIHandler != null) {

                LoadingActivity.UIHandler.removeCallbacksAndMessages(null);

            }

            UIHandler = new Handler();

            UIHandler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    if (!Utility.isMiniserverConnected && !Utility.MainActivityFlag) {

                        System.out.println(">>>>>> UIHandler >>>>> gotoMainActivity ()");

                        gotoMainActivity();

                        Utility.MainActivityFlag = true;

                    }

                }

            }, (int) 15 * 1000);

            IdentifyCommunicationMode(homeObject);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void gotoMainActivity() {

        try {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);

            finish();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void IdentifyCommunicationMode(HomeObject homeObject) {

        ConnectivityManager cm = null;

        String home_wifi = null;

        try {

            cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().getTypeName().equalsIgnoreCase("WIFI")) {

                home_wifi = "Miniserver_" + homeObject.getHome();

                home_wifi = home_wifi.toLowerCase().trim();

                if (wifi_ip.contains("192.168.4.")) {

                    Utility.COMMUNICATION_MODE = 3;

                    Utility.COMMUNICATION_MODE_IP = UtilityConstants.COMMUNICATION_IP;

                    connectMQTT(getApplicationContext(), HOMEUID);

                } else {

                    if (SharedPreference.getIP(getApplicationContext()) != null && SharedPreference.getIP(getApplicationContext()).length() > 0) {

                        if (SharedPreference.getIP(getApplicationContext()).contains(wifi_ip.substring(0, wifi_ip.lastIndexOf(".")))) {

                            String miniserverHOMEUID = ScanNetwork(SharedPreference.getIP(getApplicationContext()));

                            if (miniserverHOMEUID != null && miniserverHOMEUID.equalsIgnoreCase(HOMEUID)) {

                                Utility.COMMUNICATION_MODE = 2;

                                Utility.COMMUNICATION_MODE_IP = SharedPreference.getIP(getApplicationContext());

                                connectMQTT(getApplicationContext(), HOMEUID);

                            }

                        } else {

                            Utility.COMMUNICATION_MODE = 1;

                            Utility.COMMUNICATION_MODE_IP = homeObject.getMqtt();

                            connectMQTT(getApplicationContext(), HOMEUID);

                        }

                    } else {

                        for (int i = 0; i <= 255; i++) {

                            final int ipIndex = i;

                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    String miniserverHOMEUID = ScanNetwork("192.168.0." + ipIndex);

                                    if (miniserverHOMEUID != null && miniserverHOMEUID.equalsIgnoreCase(HOMEUID)) {

                                        Utility.COMMUNICATION_MODE = 2;

                                        Utility.COMMUNICATION_MODE_IP = "192.168.0." + ipIndex;

                                        connectMQTT(getApplicationContext(), HOMEUID);

                                    }

                                }
                            }).start();

                        }

                    }

                }

            } else if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().getTypeName().equalsIgnoreCase("MOBILE")) {

                Utility.COMMUNICATION_MODE = 1;

                Utility.COMMUNICATION_MODE_IP = homeObject.getMqtt();

                connectMQTT(getApplicationContext(), HOMEUID);

            } else {

                Utility.COMMUNICATION_MODE = 0;

                if (!SplashScreenActivity.mqttClient.gotoMainActivityFlag) {

                    gotoMainActivity();

                    SplashScreenActivity.mqttClient.gotoMainActivityFlag = true;

                }

            }

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

            for (String p : permissions) {

                result = ContextCompat.checkSelfPermission(LoadingActivity.this, p);

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

    public String ScanNetwork(String IP) {

        HttpURLConnection con = null;

        try {

            UtilityConstants.URL = "http://" + IP + ":91/getHOMEUID";

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            java.net.URL obj = new URL(UtilityConstants.URL);

            con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod(UtilityConstants.GET_TXT);

            con.setConnectTimeout(20000);

            if (con.getResponseCode() == 200) {

                InputStream inputStream = con.getInputStream();

                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder response = new StringBuilder();

                String currentLine;

                while ((currentLine = in.readLine()) != null) {

                    response.append(currentLine);

                }

                in.close();

                System.out.println("URL >>>>" + UtilityConstants.URL);

                return response.toString();
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            con.disconnect();

        }
        return null;
    }

    public class BackgroundTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            System.out.println(">>>>>> doInBackground  >>>>>");

            String host = "tcp://" + Utility.COMMUNICATION_MODE_IP + ":20518";

            System.out.println("host >>>>>" + host);

            if (MqttClient.mqttAndroidClient != null && MqttClient.mqttAndroidClient.isConnected()) {

                MqttClient.mqttAndroidClient.disconnect();

            }

            SharedPreference.setMQTTConnect(getApplicationContext(), UtilityConstants.TRUE_TXT);

            SplashScreenActivity.allowReadyCall = true;

            SplashScreenActivity.mqttClient.initializeVariable();

            SplashScreenActivity.mqttClient.connect(getApplicationContext(), HOMEUID, host);

            SplashScreenActivity.mqttClient.publishMessage(HOMEUID, "");

            return null;
        }
    }
}