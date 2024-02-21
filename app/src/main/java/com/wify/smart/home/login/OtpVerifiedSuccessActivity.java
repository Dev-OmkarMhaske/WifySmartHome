package com.wify.smart.home.login;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.WifyThemes;
import com.wify.smart.home.utils.SharedPreference;

public class OtpVerifiedSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.otp_varified_success_splash_activity);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    LoginActivity.loginActivity.applicationStartUp();

                    finish();

                }

            }, 1 * 1200); // wait for 2 seconds

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
