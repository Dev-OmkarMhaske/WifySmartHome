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

import com.wify.smart.home.R;
import com.wify.smart.home.activities.RGBSettingContainerActivity;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.UtilityConstants;

public class SingleRGBFragment extends Fragment {

    View view;

    Context context = null;

    TextView percentage, rgb_name;

    SeekBar brightness;

    int progressCnt = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {

            view = inflater.inflate(R.layout.single_rgb_fragment, null);

            context = view.getContext();

            percentage = view.findViewById(R.id.percentage);

            brightness = view.findViewById(R.id.brightness);

            rgb_name = view.findViewById(R.id.rgb_name);

            rgb_name.setText(RGBSettingContainerActivity.rgbObject.getName());

            progressCnt = (int) Math.round(Integer.parseInt(RGBSettingContainerActivity.rgbObject.getBrightness_master()) / 2.55);

            percentage.setText(progressCnt + "%");

            brightness.setProgress(progressCnt);

            brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub

                    RGBSettingContainerActivity.WriteFlag = true;

                    percentage.setText(seekBar.getProgress() + "%");

                    int i = (int) Math.round(seekBar.getProgress() * 2.55);

                    RGBSettingContainerActivity.rgbObject.setBrightness_master("" + i);

                    if ((RGBSettingContainerActivity.from.equalsIgnoreCase(UtilityConstants.ROOM) || RGBSettingContainerActivity.from.equalsIgnoreCase(UtilityConstants.FAV))) {

                        RGBSettingContainerActivity.rgbObject.setState(UtilityConstants.STATE_TRUE);

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

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }

        return view;
    }

}