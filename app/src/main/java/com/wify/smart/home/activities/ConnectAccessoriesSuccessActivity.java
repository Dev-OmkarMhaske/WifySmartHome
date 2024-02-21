package com.wify.smart.home.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.wify.smart.home.R;
import com.wify.smart.home.home.MainActivity;

public class ConnectAccessoriesSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.connect_accessories_success_activity);

            findViewById(R.id.complete_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
