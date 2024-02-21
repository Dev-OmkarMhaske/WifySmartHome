package com.wify.smart.home.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wify.smart.home.R;
import com.wify.smart.home.activities.RGBSettingContainerActivity;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.UtilityConstants;

import top.defaults.colorpicker.ColorPickerView;

public class RGBMasterFragment extends Fragment {

    public static int MasterCnt = 0;

    View view;

    Context mCtx;

    ColorPickerView colorPickerView;

    LinearLayout color_line, title_layout;

    SeekBar brightnessMaster;

    TextView percentage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {

            view = inflater.inflate(R.layout.rgb_master_fragment, null);

            mCtx = view.getContext();

            MasterCnt = 0;

            brightnessMaster = view.findViewById(R.id.sb);

            colorPickerView = view.findViewById(R.id.colorPicker);

            color_line = view.findViewById(R.id.color_line);

            title_layout = view.findViewById(R.id.title_layout);

            percentage = view.findViewById(R.id.percentage);

            if (Integer.parseInt(RGBSettingContainerActivity.rgbObject.getBrightness_master()) == 0) {

                brightnessMaster.setProgress(100);

                RGBSettingContainerActivity.rgbObject.setBrightness_master("100");

                percentage.setText("100%");

            } else {

                brightnessMaster.setProgress(Integer.parseInt(RGBSettingContainerActivity.rgbObject.getBrightness_master()));

                percentage.setText(RGBSettingContainerActivity.rgbObject.getBrightness_master() + "%");

            }

            brightnessMaster.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

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

                    RGBSettingContainerActivity.rgbObject.setBrightness_master("" + progress);

                }
            });

            ((ColorPickerView) view.findViewById(R.id.colorPicker)).subscribe((color, fromUser, shouldPropagate) -> {

                color_line.setBackgroundColor(color);

                MasterCnt++;

                String[] rgbArr = colorRGB(color).split(",");

                if (RGBSettingContainerActivity.rgbObject.getType() != null && RGBSettingContainerActivity.rgbObject.getType().equalsIgnoreCase(UtilityConstants.RGB_TYPE_PRO)) {

                    RGBSettingContainerActivity.rgbObject.setRgb(convertToHex(Integer.parseInt(rgbArr[0]), Integer.parseInt(rgbArr[1]), Integer.parseInt(rgbArr[2])));

                }
                if ((RGBSettingContainerActivity.from.equalsIgnoreCase(UtilityConstants.ROOM) || RGBSettingContainerActivity.from.equalsIgnoreCase(UtilityConstants.FAV))
                        && MasterCnt > 2 && RGBSettingContainerActivity.tabLayout.getSelectedTabPosition() == 0) {

                    RGBSettingContainerActivity.rgbObject.setMode(UtilityConstants.RGB_MODE_MASTER);

                    RGBSettingContainerActivity.WriteFlag = true;

                    RGBSettingContainerActivity.rgbObject.setState(UtilityConstants.STATE_TRUE);

                    MqttOperation.setAutomationData(RGBSettingContainerActivity.rgbObject.getAutomationData());

                }
            });

            if (RGBSettingContainerActivity.rgbObject.getRgb() != null && RGBSettingContainerActivity.rgbObject.getType() != null && RGBSettingContainerActivity.rgbObject.getType().equalsIgnoreCase("1")) {

                String[] rgbArr = colorRGB(ConvertToRGB(RGBSettingContainerActivity.rgbObject.getRgb())).split(",");

                colorPickerView.setInitialColor(Color.rgb(Integer.parseInt(rgbArr[0]), Integer.parseInt(rgbArr[1]), Integer.parseInt(rgbArr[2])));

                color_line.setBackgroundColor(Color.rgb(Integer.parseInt(rgbArr[0]), Integer.parseInt(rgbArr[1]), Integer.parseInt(rgbArr[2])));

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return view;
    }

    private String colorRGB(int color) {

        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return r + "," + g + "," + b;
    }

    public String convertToHex(int R, int G, int B) {

        String hex = String.format("%02x%02x%02x", R, G, B);

        return hex;
    }

    public int ConvertToRGB(String hex) {

        return Color.parseColor("#" + hex);
    }

}
