package com.wify.smart.home.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.activities.RGBSettingContainerActivity;
import com.wify.smart.home.adapters.RoomViewRGBFunctionAdapter;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.LinkedList;

public class RGBFunctionFragment extends Fragment {

    public static int selectedPosition = -1;

    public LinkedList<String> functionItems = new LinkedList<>();

    View view;

    Context mCtx;

    RecyclerView rgb_function_recycler_view;

    SeekBar percentageSB, speedSB;

    TextView speed, percentage;

    LinearLayoutManager linearLayoutManager = null;

    RoomViewRGBFunctionAdapter adapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {

            view = inflater.inflate(R.layout.rgb_function_fragment, null);

            mCtx = view.getContext();

            functionItems = new LinkedList<>();

            selectedPosition = -1;

            percentageSB = view.findViewById(R.id.sb);

            speedSB = view.findViewById(R.id.speedSB);

            speed = view.findViewById(R.id.speed);

            percentage = view.findViewById(R.id.percentage);

            rgb_function_recycler_view = view.findViewById(R.id.rgb_function_recycler_view);

            rgb_function_recycler_view.setHasFixedSize(true);

            if (Integer.parseInt(RGBSettingContainerActivity.rgbObject.getBrightness_function()) == 0) {

                percentageSB.setProgress(100);

                RGBSettingContainerActivity.rgbObject.setBrightness_function("100");

                percentage.setText("100%");

            } else {

                percentageSB.setProgress(Integer.parseInt(RGBSettingContainerActivity.rgbObject.getBrightness_function()));

                percentage.setText(RGBSettingContainerActivity.rgbObject.getBrightness_function() + "%");

            }

            speedSB.setProgress(Integer.parseInt(RGBSettingContainerActivity.rgbObject.getSpeed()));

            speed.setText(RGBSettingContainerActivity.rgbObject.getSpeed() + "%");

            percentageSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub

                    if (RGBSettingContainerActivity.from.equalsIgnoreCase(UtilityConstants.ROOM) || RGBSettingContainerActivity.from.equalsIgnoreCase(UtilityConstants.FAV)) {

                        RGBSettingContainerActivity.WriteFlag = true;

                        MqttOperation.setAutomationData(RGBSettingContainerActivity.rgbObject.getAutomationData());

                    }

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {

                    percentage.setText(progress + "%");

                    RGBSettingContainerActivity.rgbObject.setBrightness_function("" + progress);

                }
            });

            speedSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub

                    if (RGBSettingContainerActivity.from.equalsIgnoreCase(UtilityConstants.ROOM) || RGBSettingContainerActivity.from.equalsIgnoreCase(UtilityConstants.FAV)) {

                        RGBSettingContainerActivity.WriteFlag = true;

                        MqttOperation.setAutomationData(RGBSettingContainerActivity.rgbObject.getAutomationData());

                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {

                    speed.setText(progress + "%");

                    RGBSettingContainerActivity.rgbObject.setSpeed("" + progress);

                }
            });

            linearLayoutManager = new LinearLayoutManager(getContext());

            rgb_function_recycler_view.setLayoutManager(linearLayoutManager);

            functionItems.add("Static");
            functionItems.add("Blink");
            functionItems.add("Breath");
            functionItems.add("Color Wipe");
            functionItems.add("Color Wipe Inverse");
            functionItems.add("Color Wipe Reverse");
            functionItems.add("Color Wipe Reverse Inverse");
            functionItems.add("Color Wipe Random");
            functionItems.add("Random Color");
            functionItems.add("Single Dynamic");
            functionItems.add("Multi Dynamic");
            functionItems.add("Rainbow");
            functionItems.add("Rainbow Cycle");
            functionItems.add("Scan");
            functionItems.add("Dual Scan");
            functionItems.add("Fade");
            functionItems.add("Theater Chase");
            functionItems.add("Theater Chase Rainbow");
            functionItems.add("Running Lights");
            functionItems.add("Twinkle");
            functionItems.add("Twinkle Random");
            functionItems.add("Twinkle Fade");
            functionItems.add("Twinkle Fade Random");
            functionItems.add("Sparkle");
            functionItems.add("Flash Sparkle");
            functionItems.add("Hyper Sparkle");
            functionItems.add("Strobe");
            functionItems.add("Strobe Rainbow");
            functionItems.add("Multi Strobe");
            functionItems.add("Blink Rainbow");
            functionItems.add("Chase White");
            functionItems.add("Chase Color");
            functionItems.add("Chase Random");
            functionItems.add("Chase Rainbow");
            functionItems.add("Chase Flash");
            functionItems.add("Chase Flash Random");
            functionItems.add("Chase Rainbow White");
            functionItems.add("Chase Blackout");
            functionItems.add("Chase Blackout Rainbow");
            functionItems.add("Color Sweep Random");
            functionItems.add("Running Color");
            functionItems.add("Running Red Blue");
            functionItems.add("Running Random");
            functionItems.add("Larson Scanner");
            functionItems.add("Comet");
            functionItems.add("Fireworks");
            functionItems.add("Fireworks Random");
            functionItems.add("Merry Christmas");
            functionItems.add("Fire Flicker");
            functionItems.add("Fire Flicker(soft)");
            functionItems.add("Fire Flicker(intense)");
            functionItems.add("Circus Combustus");
            functionItems.add("Halloween");
            functionItems.add("Bicolor Chase");
            functionItems.add("Tricolor Chase");
            functionItems.add("TwinkleFOX");
            functionItems.add("Rain");
            functionItems.add("Block Dissolve");
            functionItems.add("ICU");
            functionItems.add("Dual Larson");
            functionItems.add("Running Random2");
            functionItems.add("Filler Up");
            functionItems.add("Rainbow Larson");
            functionItems.add("Rainbow Fireworks");
            functionItems.add("Trifade");
            functionItems.add("VU Meter");
            functionItems.add("Heartbeat");
            functionItems.add("Bits");
            functionItems.add("Multi Comet");
            functionItems.add("Flipbook");
            functionItems.add("Popcorn");
            functionItems.add("Oscillator");

            adapter = new RoomViewRGBFunctionAdapter(getContext(), functionItems);

            rgb_function_recycler_view.setAdapter(adapter);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return view;
    }
}
