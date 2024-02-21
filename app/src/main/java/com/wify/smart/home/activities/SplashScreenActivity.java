package com.wify.smart.home.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.wify.smart.home.BuildConfig;
import com.wify.smart.home.R;
import com.wify.smart.home.helper.PrefManager;
import com.wify.smart.home.login.LoginActivity;
import com.wify.smart.home.mqtt.MqttClient;
import com.wify.smart.home.service.service;
import com.wify.smart.home.utils.FirebaseConfiguration;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView miniserver_img;

    TextView miniserver_txt;

    public static MqttClient mqttClient = null;

    public static boolean allowReadyCall = false;

    public static void setDefaultFirebase(Context context) {

        try {

            boolean isDefaultAvalaible = false;

            List<FirebaseApp> firebaseApps = FirebaseApp.getApps(context);

            for (FirebaseApp firebaseApp : firebaseApps) {

                if (firebaseApp.getName().equalsIgnoreCase(UtilityConstants.DEFAULT)) {

                    isDefaultAvalaible = true;

                }

            }

            if (!isDefaultAvalaible) {

                FirebaseApp.initializeApp(context, FirebaseConfiguration.options, UtilityConstants.DEFAULT);

            }

            Utility.firebaseApp = FirebaseApp.getInstance(UtilityConstants.DEFAULT);

            Utility.firebaseDatabase = FirebaseDatabase.getInstance(Utility.firebaseApp);

            Utility.firebaseAuth = FirebaseAuth.getInstance(Utility.firebaseApp);

            Utility.myRef_homes = Utility.firebaseDatabase.getReference(UtilityConstants.ROOT_PATH_HOMES);

            Utility.myRef_db = Utility.firebaseDatabase.getReference(UtilityConstants.ROOT_PATH_DB);

            FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            Utility.firebaseDatabase.getInstance().setPersistenceEnabled(true);

            Utility.myRef_db.keepSynced(true);

            Utility.myRef_homes.keepSynced(true);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            startService(new Intent(SplashScreenActivity.this, service.class));

            setContentView(R.layout.activity_splash_screen);

            System.out.println(">>>>>>> internetConnectionAvailable >>>>" + SharedPreference.getRooms(getApplicationContext()));

            mqttClient = new MqttClient();

            miniserver_img = findViewById(R.id.miniserver_img);

            miniserver_txt = findViewById(R.id.miniserver_txt);

            if (SharedPreference.getRoomImages(getApplicationContext()) != null) {

                Utility.RoomImages = SharedPreference.getRoomImages(getApplicationContext());
            }

            Utility.APP_VERSION = BuildConfig.VERSION_NAME;

            System.out.println(">>>> APP_VERSION >>" + Utility.APP_VERSION);

            Animation animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

            miniserver_img.setVisibility(View.VISIBLE);

            animFadeIn.reset();

            miniserver_img.clearAnimation();

            miniserver_img.startAnimation(animFadeIn);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    Animation animFadeIn2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

                    miniserver_txt.setVisibility(View.VISIBLE);

                    animFadeIn2.reset();

                    miniserver_txt.clearAnimation();

                    miniserver_txt.startAnimation(animFadeIn2);
                }

            }, (int) 1.5 * 1000); // wait for 2 seconds

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    setDefaultFirebase(getApplicationContext());

                    PrefManager prefManager = new PrefManager(getApplicationContext());

                    prefManager.setFirstTimeLaunch(false);

                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));

                    finish();

                }

            }, 3 * 1000); // wait for 2 seconds

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}
