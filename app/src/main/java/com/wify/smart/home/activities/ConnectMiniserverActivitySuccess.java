package com.wify.smart.home.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.wify.smart.home.R;
import com.wify.smart.home.home.MainActivity;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

public class ConnectMiniserverActivitySuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.connect_to_wifi_success_layout);

            Utility.isHomeConnected = true;

            findViewById(R.id.miniserver_success_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(getApplicationContext(), LoadingActivity.class);

                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    i.putExtra(UtilityConstants.HOME_OBJ, Utility.connectedHome);

                    getApplicationContext().startActivity(i);

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

}
