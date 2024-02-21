package com.wify.smart.home.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.helper.RGBTabContainer;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.scene.AddSceneActivity;
import com.wify.smart.home.schedule.AddScheduleActivity;
import com.wify.smart.home.utils.UtilityConstants;

public class RGBSettingContainerActivity extends AppCompatActivity {

    public static ViewPager viewPager;

    public static RGBObject rgbObject = null;

    public static String from = null;

    public static boolean WriteFlag = false;

    public static TabLayout tabLayout;

    public static int CurrentActiveTab = -1;

    LinearLayout rgb_layouts;

    TextView device_title;

    Switch include_in_favourite = null, alexa_switch;

    RGBTabContainer rgbTabContainer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.rgb_setting_container_activity);

            WriteFlag = false;

            rgbObject = (RGBObject) getIntent().getSerializableExtra(UtilityConstants.RGB_OBJ);

            from = (String) getIntent().getSerializableExtra(UtilityConstants.FROM);

            tabLayout = findViewById(R.id.tabLayout);

            rgb_layouts = findViewById(R.id.rgb_layouts);

            alexa_switch = findViewById(R.id.alexa_switch);

            device_title = findViewById(R.id.device_title);

            viewPager = findViewById(R.id.viewPager);

            include_in_favourite = findViewById(R.id.include_in_favourite);

            include_in_favourite.setChecked(rgbObject.getFav().equalsIgnoreCase(UtilityConstants.STATE_TRUE) ? true : false);

            alexa_switch.setChecked(rgbObject.getEnableAlexa().equalsIgnoreCase(UtilityConstants.STATE_TRUE) ? true : false);

            if (!from.equalsIgnoreCase(UtilityConstants.ROOM)) {

                rgb_layouts.setVisibility(View.GONE);
            }

            include_in_favourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    WriteFlag = true;
                }
            });

            alexa_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    WriteFlag = true;
                }
            });

            device_title.setText(rgbObject.getName());

            if (rgbObject.getType().equalsIgnoreCase(UtilityConstants.RGB_TYPE_PRO)) {

                tabLayout.addTab(tabLayout.newTab().setText(UtilityConstants.RGB_MASTER_TXT));

                tabLayout.addTab(tabLayout.newTab().setText(UtilityConstants.RGB_FUNCTION_TXT));

            } else if (rgbObject.getType().equalsIgnoreCase(UtilityConstants.RGB_TYPE_SIMPLE)) {

                tabLayout.addTab(tabLayout.newTab().setText(UtilityConstants.RGB_SIMPLE_TXT));

            } else if (rgbObject.getType().equalsIgnoreCase(UtilityConstants.RGB_TYPE_SINGLE)) {

                tabLayout.addTab(tabLayout.newTab().setText(UtilityConstants.RGB_SINGLE_TXT));
            }

            rgbTabContainer = new RGBTabContainer(getApplicationContext(), getSupportFragmentManager(), tabLayout.getTabCount());

            viewPager.setAdapter(rgbTabContainer);

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            if (rgbObject.getType().equalsIgnoreCase(UtilityConstants.RGB_TYPE_PRO)) {

                viewPager.setCurrentItem(Integer.parseInt(rgbObject.getMode()) - 1);
            }

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    viewPager.setCurrentItem(tab.getPosition());

                    CurrentActiveTab = tab.getPosition();

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            findViewById(R.id.save_text).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    rgbObject.setMode("" + (tabLayout.getSelectedTabPosition() + 1));

                    if (from.equalsIgnoreCase(UtilityConstants.SCENE)) {

                        AddSceneActivity.updateSceneData(rgbObject.getAutomationData(), UtilityConstants.ADD);

                        Intent intent = new Intent(getApplicationContext(), AddSceneActivity.class);

                        intent.putExtra(UtilityConstants.SCENE, AddSceneActivity.sceneObject);

                        startActivity(intent);

                    } else if (from.equalsIgnoreCase(UtilityConstants.SCHEDULE)) {

                        AddScheduleActivity.updateScheduleData(rgbObject.getAutomationData(), UtilityConstants.ADD);

                        Intent intent = new Intent(getApplicationContext(), AddScheduleActivity.class);

                        intent.putExtra(UtilityConstants.SCHEDULE, AddScheduleActivity.scheduleObject);

                        startActivity(intent);

                    } else if (from.equalsIgnoreCase(UtilityConstants.MOTION)) {

                        EditMotionActivity.updateMotionData(rgbObject.getAutomationData(), UtilityConstants.ADD);

                        Intent intent = new Intent(getApplicationContext(), EditMotionActivity.class);

                        intent.putExtra(UtilityConstants.MOTION, EditMotionActivity.motionObject);

                        startActivity(intent);

                    } else {

                        rgbObject.setFav(include_in_favourite.isChecked() ? UtilityConstants.STATE_TRUE : UtilityConstants.STATE_FALSE);

                        rgbObject.setEnableAlexa(alexa_switch.isChecked() ? UtilityConstants.STATE_TRUE : UtilityConstants.STATE_FALSE);

                        //  MqttOperation.setAutomationData(rgbObject.getAutomationData());

                        onBackPressed();

                    }
                }
            });

            viewPager.beginFakeDrag();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        try {

            super.onBackPressed();

            if (WriteFlag && (from.equalsIgnoreCase(UtilityConstants.FAV) || from.equalsIgnoreCase(UtilityConstants.ROOM))) {

                WriteFlag = false;

                MqttOperation.writePOINT(rgbObject.getFilename(), new Gson().toJson(rgbObject));

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }
}