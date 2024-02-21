package com.wify.smart.home.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.wify.smart.home.R;
import com.wify.smart.home.activities.LoadingActivity;
import com.wify.smart.home.dto.DBObject;
import com.wify.smart.home.home.MainActivity;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    public static final int MULTIPLE_PERMISSIONS = 10;

    public static LoginActivity loginActivity;

    String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
    };

    private EditText username, phoneNumber;

    private Button button_get_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

            loginActivity = this;

            setContentView(R.layout.login_activity);

            Utility.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            getScreenSize(getApplicationContext());

            if (checkPermissions()) {

                applicationStartUp();

            }

            username = findViewById(R.id.username);

            phoneNumber = findViewById(R.id.phoneNumber);

            button_get_otp = findViewById(R.id.button_get_otp);

            button_get_otp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (TextUtils.isEmpty(username.getText().toString())) {

                        Toast.makeText(LoginActivity.this, R.string.valid_name_err, Toast.LENGTH_SHORT).show();

                    } else if (TextUtils.isEmpty(phoneNumber.getText().toString()) || phoneNumber.getText().toString().length() != 10) {

                        Toast.makeText(LoginActivity.this, R.string.valid_number_err, Toast.LENGTH_SHORT).show();

                    } else if (checkPermissions()) {

                        if (isInternetAvailable(getApplicationContext())) {

                            Intent intent = new Intent(getApplicationContext(), ValidateOTPActivity.class);

                            intent.putExtra(UtilityConstants.USER_NAME_STR, username.getText().toString());

                            intent.putExtra(UtilityConstants.USER_NUMBER_STR, phoneNumber.getText().toString());

                            startActivity(intent);

                        } else {

                            Toast.makeText(LoginActivity.this, R.string.network_err_txt, Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public static boolean isMobileOrWifiConnectivityAvailable(Context ctx) {

        boolean haveConnectedWifi = false;

        boolean haveConnectedMobile = false;

        ConnectivityManager cm = null;

        NetworkInfo[] netInfo;

        try {

            cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

            netInfo = cm.getAllNetworkInfo();

            for (NetworkInfo ni : netInfo) {

                if (ni.getTypeName().equalsIgnoreCase(UtilityConstants.NETWORK_WiFi)) {

                    if (ni.isConnected()) {

                        haveConnectedWifi = true;

                    }
                }

                if (ni.getTypeName().equalsIgnoreCase(UtilityConstants.NETWORK_MOBILE)) {

                    if (ni.isConnected()) {

                        haveConnectedMobile = true;

                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return haveConnectedWifi || haveConnectedMobile;
    }

    public static boolean isInternetAvailable(Context context) {

        if (isMobileOrWifiConnectivityAvailable(context)) {

            try {

                if (getWifiName(context) != null && getWifiName(context).contains(UtilityConstants.MINISERVER)) {

                    return false;
                }

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);

                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());

                urlc.setRequestProperty("User-Agent", "Test");

                urlc.setRequestProperty("Connection", UtilityConstants.STATE_CLOSE_STR);

                urlc.setConnectTimeout(1100);

                urlc.connect();

                return (urlc.getResponseCode() == 200);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        return false;
    }

    public static String getWifiName(Context context) {

        String Wifi_name = null;

        WifiManager wifiManager = null;

        WifiInfo wifiInfo;

        try {

            wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);

            wifiInfo = wifiManager.getConnectionInfo();

            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {

                Wifi_name = wifiInfo.getSSID();

                return Wifi_name;

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return Wifi_name;
    }

    private boolean checkPermissions() {

        int result;

        List<String> listPermissionsNeeded = new ArrayList<>();

        try {

            for (String p : permissions) {

                result = ContextCompat.checkSelfPermission(LoginActivity.this, p);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissionsList, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissionsList, grantResults);

        try {

            switch (requestCode) {

                case MULTIPLE_PERMISSIONS: {

                    if (grantResults.length > 0) {

                        String permissionsDenied = "";

                        for (String per : permissionsList) {

                            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                                permissionsDenied += "\n" + per;
                            }

                        }
                    }

                    return;

                }

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    public void applicationStartUp() {

        try {

            if (SharedPreference.getPhone_number(getApplicationContext()) != null) {

                if (SharedPreference.getConnectedHome(getApplicationContext()) == null) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);


                } else {

                    Intent i = new Intent(getApplicationContext(), LoadingActivity.class);

                    i.putExtra(UtilityConstants.HOME_OBJ, SharedPreference.getConnectedHome(getApplicationContext()));

                    startActivity(i);
                }

                getDBData();

                LoadingActivity.getOnlineVersions();

                finish();

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void getDBData() {

        try {

            Utility.myRef_db.child(SharedPreference.getPhone_number(getApplicationContext())).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        Utility.dbObject = dataSnapshot.getValue(DBObject.class);
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

    private int getScreenSizeInMM() {

        DisplayMetrics metrics = null;

        float widthDpi;

        double screenMM = 0;

        try {

            metrics = new DisplayMetrics();

            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            widthDpi = metrics.xdpi;

            screenMM = (metrics.widthPixels / widthDpi) * 25.4;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return (int) screenMM;

    }

    public void getScreenSize(Context context) {

        try {

            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

            float dpHeight = displayMetrics.heightPixels / displayMetrics.density;

            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

            int widthMM = getScreenSizeInMM();

            Utility.TILE_SPAN = widthMM / 20;

            float tileSize = ((dpWidth - (Utility.TILE_SPAN)) / Utility.TILE_SPAN);

            Utility.SCREEN_HEIGHT = dpHeight;

            Utility.SCREEN_WIDTH = dpWidth;

            UtilityConstants.FAV_SCENE_TILE_SIZE = (int) (dpWidth * 1.27);

            Utility.TILE_SIZE = Math.round(tileSize * displayMetrics.density);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}
