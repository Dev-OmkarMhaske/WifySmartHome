package com.wify.smart.home.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.wify.smart.home.R;
import com.wify.smart.home.fragments.WiFiGestureTabAdapter;
import com.wify.smart.home.home.MainActivity;
import com.wify.smart.home.utils.UtilityConstants;

public class WiFiGestureTabContainerActivity extends AppCompatActivity {

    TabLayout tabLayout;

    ViewPager viewPager;

    TextView home_txt;

    WiFiGestureTabAdapter wiFiGestureTabAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.wifi_gesture_tab_container_fragment);

            tabLayout = findViewById(R.id.tabLayout);

            viewPager = findViewById(R.id.viewPager);

            home_txt = findViewById(R.id.home_txt);

            //  tabLayout.addTab(tabLayout.newTab().setText("Gesture"));

            tabLayout.addTab(tabLayout.newTab().setText("Wi-Fi setting"));

            setView();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void setView() {

        wiFiGestureTabAdapter = new WiFiGestureTabAdapter(getApplicationContext(), getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(wiFiGestureTabAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        home_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                intent.putExtra(UtilityConstants.FROM, UtilityConstants.HOME);

                startActivity(intent);
            }
        });
    }
}
