package com.wify.smart.home.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wify.smart.home.R;
import com.wify.smart.home.activities.RGBSettingContainerActivity;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.UtilityConstants;

public class SimpleRGBFragment extends Fragment {

    View view;

    Context context = null;

    private ImageView colorImageView;

    private int redValue = 0;

    private int greenValue = 0;

    private int blueValue = 0;

    TextView red_txt, green_txt, blue_txt;

    SeekBar seekBarR, seekBarG, seekBarB;

    String rgb[] = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {

            view = inflater.inflate(R.layout.simple_rgb_fragment, null);

            context = view.getContext();

            colorImageView = view.findViewById(R.id.colorImageView);

            seekBarR = view.findViewById(R.id.seekBarR);

            seekBarG = view.findViewById(R.id.seekBarG);

            seekBarB = view.findViewById(R.id.seekBarB);

            seekBarR.setOnSeekBarChangeListener(mChangeListener);

            seekBarG.setOnSeekBarChangeListener(mChangeListener);

            seekBarB.setOnSeekBarChangeListener(mChangeListener);

            red_txt = view.findViewById(R.id.red_txt);

            green_txt = view.findViewById(R.id.green_txt);

            blue_txt = view.findViewById(R.id.blue_txt);

            rgb = RGBSettingContainerActivity.rgbObject.getRgb().split("\\|");

            redValue = Integer.parseInt(rgb[0]);

            seekBarR.setProgress(redValue);

            red_txt.setText("Red : " + redValue);

            greenValue = Integer.parseInt(rgb[1]);

            seekBarG.setProgress(greenValue);

            green_txt.setText("Green : " + greenValue);

            blueValue = Integer.parseInt(rgb[2]);

            seekBarB.setProgress(blueValue);

            blue_txt.setText("Blue : " + blueValue);

            colorImageView.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));

        } catch (Exception e) {

            e.printStackTrace();
        }

        return view;
    }

    private final SeekBar.OnSeekBarChangeListener mChangeListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar,
                                              int progress,
                                              boolean fromUser) {

                    try {

                        int viewId = seekBar.getId();

                        switch (viewId) {

                            case R.id.seekBarR:

                                redValue = seekBar.getProgress();

                                red_txt.setText("Red : " + redValue);

                                break;

                            case R.id.seekBarG:

                                greenValue = seekBar.getProgress();

                                green_txt.setText("Green : " + greenValue);

                                break;

                            case R.id.seekBarB:

                                blueValue = seekBar.getProgress();

                                blue_txt.setText("Blue : " + blueValue);

                                break;

                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    try {

                        RGBSettingContainerActivity.WriteFlag = true;

                        int viewId = seekBar.getId();

                        switch (viewId) {

                            case R.id.seekBarR:

                                redValue = seekBar.getProgress();

                                red_txt.setText("Red : " + redValue);

                                break;

                            case R.id.seekBarG:

                                greenValue = seekBar.getProgress();

                                green_txt.setText("Green : " + greenValue);

                                break;

                            case R.id.seekBarB:

                                blueValue = seekBar.getProgress();

                                blue_txt.setText("Blue : " + blueValue);

                                break;

                        }

                        int color = Color.rgb(redValue, greenValue, blueValue);

                        colorImageView.setBackgroundColor(color);

                        String rgb = Integer.toString(redValue).concat("|").concat(Integer.toString(greenValue)).concat("|").concat(Integer.toString(blueValue));

                        RGBSettingContainerActivity.rgbObject.setRgb(rgb);

                        if ((RGBSettingContainerActivity.from.equalsIgnoreCase(UtilityConstants.ROOM) || RGBSettingContainerActivity.from.equalsIgnoreCase(UtilityConstants.FAV))) {

                            RGBSettingContainerActivity.rgbObject.setState(UtilityConstants.STATE_TRUE);

                            MqttOperation.setAutomationData(RGBSettingContainerActivity.rgbObject.getAutomationData());

                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            };

}
