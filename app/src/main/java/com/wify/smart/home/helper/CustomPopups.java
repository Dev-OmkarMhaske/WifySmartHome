package com.wify.smart.home.helper;

import static com.wify.smart.home.helper.HomePopUp.applyDim;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.activities.ConnectAccessoriesActivity;
import com.wify.smart.home.activities.ConnectAccessoriesWithRoomActivity;
import com.wify.smart.home.activities.EditMotionActivity;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.DimmerObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.ParentObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.scene.AddSceneActivity;
import com.wify.smart.home.schedule.AddScheduleActivity;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import abak.tr.com.boxedverticalseekbar.BoxedVertical;
import pl.droidsonroids.gif.GifImageView;

public class CustomPopups {

    public static boolean WriteFlag = false;

    public void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }

    public void showPopupForController(final View view) {

        try {

            RadioButton miniserver_radio, router_radio;

            RadioGroup installation_radio_grp;

            Button save_btn;

            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);

            View popupView = inflater.inflate(R.layout.popup_for_installation, null);

            int width = LinearLayout.LayoutParams.MATCH_PARENT;

            int height = LinearLayout.LayoutParams.WRAP_CONTENT;

            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

//        popupWindow.setWidth(width-20);
//        popupWindow.setFocusable(true);

            ViewGroup root = (ViewGroup) ((Activity) popupView.getContext()).getWindow().getDecorView().getRootView();

            applyDim(root, 0.7f);

            installation_radio_grp = popupView.findViewById(R.id.installation_radio_grp);

            miniserver_radio = popupView.findViewById(R.id.miniserver_radio);

            router_radio = popupView.findViewById(R.id.router_radio);

            save_btn = popupView.findViewById(R.id.save_btn);

            save_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        if (miniserver_radio.isChecked()) {

                            ConnectAccessoriesActivity.progressBar.setVisibility(View.VISIBLE);

                            ConnectAccessoriesActivity.onModule();

                            new Timer().scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {

                                    if (!ConnectAccessoriesActivity.isNext) {

                                        ConnectAccessoriesActivity.parentRange(view.getContext());

                                    }

                                }
                            }, 0, 2000);

                            popupWindow.dismiss();

                        } else if (router_radio.isChecked()) {

                            if (Utility.STASSID.trim().length() == 0 || Utility.STAPWD.trim().length() == 0) {

                                Toast.makeText(popupView.getContext(), "Please set WiFi credentials to Miniserver.", Toast.LENGTH_LONG).show();
                                return;
                            }

                            ConnectAccessoriesActivity.onModule();

                            Intent intent = new Intent(view.getContext(), ConnectAccessoriesWithRoomActivity.class);

                            intent.putExtra(UtilityConstants.MODULE_TXT, UtilityConstants.CONTROLLER_MODULE);

                            intent.putExtra("isHmodule", "true");

                            intent.putExtra("parentObjects", new ArrayList<ParentObject>());

                            view.getContext().startActivity(intent);

                            popupWindow.dismiss();

                        } else {

                            popupWindow.dismiss();
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            });

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {

                    clearDim(root);
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void showPopupWindowForFan(final View view, FanObject fanObject, String from) {

        try {

            WriteFlag = false;

            Switch include_in_favourite, fan_freeze, alexa_switch;

            LinearLayout fav_layout, freeze_layout, alexa_layout;

            TextView fan_name, done, fan_speed;

            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);

            View popupView = inflater.inflate(R.layout.fan_speed_popup_layout, null);

            int width = LinearLayout.LayoutParams.MATCH_PARENT;

            int height = LinearLayout.LayoutParams.MATCH_PARENT;

            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            done = popupView.findViewById(R.id.done);

            freeze_layout = popupView.findViewById(R.id.freeze_layout);

            fav_layout = popupView.findViewById(R.id.fav_layout);

            fan_name = popupView.findViewById(R.id.fan_name);

            alexa_layout = popupView.findViewById(R.id.alexa_layout);

            alexa_switch = popupView.findViewById(R.id.alexa_switch);

            fan_name.setText(fanObject.getName());

            alexa_switch.setChecked(fanObject.getEnableAlexa().equalsIgnoreCase(UtilityConstants.STATE_TRUE) ? true : false);

            fan_speed = popupView.findViewById(R.id.fan_speed);

            include_in_favourite = popupView.findViewById(R.id.include_in_favourite);

            fan_freeze = popupView.findViewById(R.id.fan_freeze);

            BoxedVertical bv = popupView.findViewById(R.id.boxed_vertical);

            fan_speed.setText("Speed Level : " + fanObject.getSpeed());

            if (!from.equalsIgnoreCase(UtilityConstants.ROOM)) {

                freeze_layout.setVisibility(View.GONE);

                fav_layout.setVisibility(View.GONE);

                alexa_layout.setVisibility(View.GONE);
            }

            include_in_favourite.setChecked(fanObject.getFav().equalsIgnoreCase(UtilityConstants.STATE_TRUE));

            fan_freeze.setChecked(fanObject.getFreeze().equalsIgnoreCase(UtilityConstants.STATE_TRUE));

            bv.setValue(Integer.parseInt(fanObject.getSpeed()));

            include_in_favourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    WriteFlag = true;

                    if (include_in_favourite.isChecked()) {

                        if (fan_freeze.isChecked()) {

                            include_in_favourite.setChecked(false);

                            Toast.makeText(popupView.getContext(), R.string.disable_fan_freeze_txt, Toast.LENGTH_SHORT).show();

                        } else {

                            fanObject.setFav(UtilityConstants.STATE_TRUE);

                        }
                    } else {

                        fanObject.setFav(UtilityConstants.STATE_FALSE);

                    }

                }
            });

            alexa_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    WriteFlag = true;
                }
            });

            fan_freeze.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    WriteFlag = true;

                    if (fan_freeze.isChecked()) {

                        if (include_in_favourite.isChecked()) {

                            fan_freeze.setChecked(false);

                            Toast.makeText(popupView.getContext(), R.string.disable_fan_favourite_txt, Toast.LENGTH_SHORT).show();

                        } else {

                            fanObject.setFreeze(UtilityConstants.STATE_TRUE);

                        }
                    } else {

                        fanObject.setFreeze(UtilityConstants.STATE_FALSE);

                    }

                }
            });

            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    return true;

                }
            });

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (from.equalsIgnoreCase(UtilityConstants.SCENE)) {

                        fanObject.setSpeed("" + bv.getValue());

                        AddSceneActivity.setRoomViseAccessories(view.getContext());

                        AddSceneActivity.updateSceneData(fanObject.getAutomationData(), UtilityConstants.ADD);

                    } else if (from.equalsIgnoreCase(UtilityConstants.SCHEDULE)) {

                        fanObject.setSpeed("" + bv.getValue());

                        AddScheduleActivity.SetAdapterData(view.getContext());

                        AddScheduleActivity.updateScheduleData(fanObject.getAutomationData(), UtilityConstants.ADD);

                    } else if (from.equalsIgnoreCase(UtilityConstants.MOTION)) {

                        fanObject.setSpeed("" + bv.getValue());

                        EditMotionActivity.updateMotionData(fanObject.getAutomationData(), UtilityConstants.ADD);

                    } else {

                        fanObject.setFav(include_in_favourite.isChecked() ? UtilityConstants.STATE_TRUE : UtilityConstants.STATE_FALSE);

                        fanObject.setFreeze(fan_freeze.isChecked() ? UtilityConstants.STATE_TRUE : UtilityConstants.STATE_FALSE);

                        fanObject.setEnableAlexa(alexa_switch.isChecked() ? UtilityConstants.STATE_TRUE : UtilityConstants.STATE_FALSE);

                        if (WriteFlag) {

                            MqttOperation.writePOINT(fanObject.getFilename(), new Gson().toJson(fanObject));

                        }
                    }

                    popupWindow.dismiss();
                }
            });

            bv.setOnBoxedPointsChangeListener(new BoxedVertical.OnValuesChangeListener() {
                @Override
                public void onPointsChanged(BoxedVertical boxedPoints, int value1) {

                }

                @Override
                public void onStartTrackingTouch(BoxedVertical boxedPoints) {

                }

                @Override
                public void onStopTrackingTouch(BoxedVertical boxedPoints) {

                    fan_speed.setText("Speed Level : " + boxedPoints.getValue());

                    bv.setValue(boxedPoints.getValue());

                    if (from.equalsIgnoreCase(UtilityConstants.ROOM) || from.equalsIgnoreCase(UtilityConstants.FAV)) {

                        fanObject.setSpeed("" + boxedPoints.getValue());

                        if (fanObject.getFreeze().equalsIgnoreCase(UtilityConstants.STATE_FALSE)) {

                            fanObject.setState(UtilityConstants.STATE_TRUE);

                            MqttOperation.setAutomationData(fanObject.getAutomationData());

                            // MqttOperation.writePOINT(fanObject.getFilename(), new Gson().toJson(fanObject));

                        }

                    }
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void showPopupWindowForCurtain(final View view, CurtainObject curtainObject, String from) {

        try {

            WriteFlag = false;

            ImageView curtain_open, curtain_close, curtain_pause;

            Switch include_in_favourite, alexa_switch;

            LinearLayout fav_layout, alexa_layout;

            TextView curtain_name, done, curtain_state;

            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);

            View popupView = inflater.inflate(R.layout.custom_curtain_popup, null);

            int width = LinearLayout.LayoutParams.MATCH_PARENT;

            int height = LinearLayout.LayoutParams.MATCH_PARENT;

            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            done = popupView.findViewById(R.id.done);

            curtain_name = popupView.findViewById(R.id.curtain_name);

            alexa_switch = popupView.findViewById(R.id.alexa_switch);

            alexa_layout = popupView.findViewById(R.id.alexa_layout);

            curtain_name.setText(curtainObject.getName());

            curtain_open = popupView.findViewById(R.id.curtain_open);

            curtain_pause = popupView.findViewById(R.id.curtain_pause);

            curtain_close = popupView.findViewById(R.id.curtain_close);

            curtain_state = popupView.findViewById(R.id.curtain_state);

            fav_layout = popupView.findViewById(R.id.fav_layout);

            include_in_favourite = popupView.findViewById(R.id.include_in_favourite);

            if (!from.equalsIgnoreCase(UtilityConstants.ROOM)) {

                fav_layout.setVisibility(View.GONE);
                alexa_layout.setVisibility(View.GONE);

            }

            alexa_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    WriteFlag = true;
                }
            });

            if (curtainObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                curtain_state.setText(UtilityConstants.STATE_OPEN_STR);

            } else if (curtainObject.getState().equalsIgnoreCase(UtilityConstants.STATE_FALSE)) {

                curtain_state.setText(UtilityConstants.STATE_CLOSE_STR);

            } else {

                curtain_state.setText(UtilityConstants.STATE_PAUSE_STR);
            }

            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    return true;

                }
            });

            include_in_favourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    WriteFlag = true;
                }
            });

            include_in_favourite.setChecked(curtainObject.getFav().equalsIgnoreCase(UtilityConstants.STATE_TRUE));

            alexa_switch.setChecked(curtainObject.getEnableAlexa().equalsIgnoreCase(UtilityConstants.STATE_TRUE) ? true : false);

            curtain_open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    curtain_state.setText(UtilityConstants.STATE_OPEN_STR);

                    Toast.makeText(popupView.getContext(), R.string.curtain_open_str, Toast.LENGTH_SHORT).show();

                    curtainObject.setState(UtilityConstants.STATE_TRUE);

                    if (from.equalsIgnoreCase(UtilityConstants.ROOM) || from.equalsIgnoreCase(UtilityConstants.FAV)) {

                        MqttOperation.setAutomationData(curtainObject.getAutomationData());

                    }
                }
            });

            curtain_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    curtain_state.setText(UtilityConstants.STATE_CLOSE_STR);

                    curtainObject.setState(UtilityConstants.STATE_FALSE);

                    Toast.makeText(popupView.getContext(), R.string.curtain_close_str, Toast.LENGTH_SHORT).show();

                    if (from.equalsIgnoreCase(UtilityConstants.ROOM) || from.equalsIgnoreCase(UtilityConstants.FAV)) {

                        MqttOperation.setAutomationData(curtainObject.getAutomationData());

                    }
                }
            });

            curtain_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    curtainObject.setState(UtilityConstants.STATE_PAUSE);

                    curtain_state.setText(UtilityConstants.STATE_PAUSE_STR);

                    Toast.makeText(popupView.getContext(), R.string.curtain_paused_str, Toast.LENGTH_SHORT).show();

                    if (from.equalsIgnoreCase(UtilityConstants.ROOM) || from.equalsIgnoreCase(UtilityConstants.FAV)) {

                        MqttOperation.setAutomationData(curtainObject.getAutomationData());
                    }
                }
            });

            done.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    curtainObject.setFav(include_in_favourite.isChecked() ? UtilityConstants.STATE_TRUE : UtilityConstants.STATE_FALSE);

                    curtainObject.setEnableAlexa(alexa_switch.isChecked() ? UtilityConstants.STATE_TRUE : UtilityConstants.STATE_FALSE);

                    if (from.equalsIgnoreCase(UtilityConstants.SCENE)) {

                        AddSceneActivity.setRoomViseAccessories(view.getContext());

                        AddSceneActivity.updateSceneData(curtainObject.getAutomationData(), UtilityConstants.ADD);

                    } else if (from.equalsIgnoreCase(UtilityConstants.SCHEDULE)) {

                        AddScheduleActivity.SetAdapterData(view.getContext());

                        AddScheduleActivity.updateScheduleData(curtainObject.getAutomationData(), UtilityConstants.ADD);

                    } else if (from.equalsIgnoreCase(UtilityConstants.MOTION)) {

                        EditMotionActivity.updateMotionData(curtainObject.getAutomationData(), UtilityConstants.ADD);

                    } else {

                        if (WriteFlag) {

                            MqttOperation.writePOINT(curtainObject.getFilename(), new Gson().toJson(curtainObject));

                        }
                    }

                    popupWindow.dismiss();

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void showPopupWindowForGeneric(final View view, GenericObject genericObject, String from) {

        try {
            WriteFlag = false;


            Switch include_in_favourite, generic_freeze, dimmer_switch, alexa_switch;

            LinearLayout alexa_layout, dimmer_layout, dimmer_intensity_layout, dimmer_color_layout, freeze_layout, fav_layout;

            TextView dimmer_count, generic_name;

            BoxedVertical dimmer_intensity;

            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);

            View popupView = inflater.inflate(R.layout.generic_setting_popup, null);

            int width = LinearLayout.LayoutParams.MATCH_PARENT;

            int height = LinearLayout.LayoutParams.MATCH_PARENT;

            generic_name = popupView.findViewById(R.id.generic_name);
            generic_name.setText(genericObject.getName());

            alexa_layout = popupView.findViewById(R.id.alexa_layout);

            alexa_switch = popupView.findViewById(R.id.alexa_switch);

            include_in_favourite = popupView.findViewById(R.id.include_in_favourite);

            generic_freeze = popupView.findViewById(R.id.generic_freeze);

            dimmer_layout = popupView.findViewById(R.id.dimmer_layout);

            dimmer_count = popupView.findViewById(R.id.dimmer_count);

            dimmer_intensity_layout = popupView.findViewById(R.id.dimmer_intensity_layout);

            dimmer_intensity = popupView.findViewById(R.id.dimmer_intensity);

            dimmer_switch = popupView.findViewById(R.id.dimmer_switch);

            dimmer_color_layout = popupView.findViewById(R.id.dimmer_color_layout);

            freeze_layout = popupView.findViewById(R.id.freeze_layout);

            fav_layout = popupView.findViewById(R.id.fav_layout);

            if (!from.equalsIgnoreCase(UtilityConstants.ROOM)) {

                freeze_layout.setVisibility(View.GONE);

                fav_layout.setVisibility(View.GONE);

                alexa_layout.setVisibility(View.GONE);
            }

            generic_freeze.setChecked(genericObject.getFreeze().equalsIgnoreCase(UtilityConstants.STATE_TRUE));

            include_in_favourite.setChecked(genericObject.getFav().equalsIgnoreCase(UtilityConstants.STATE_TRUE));

            alexa_switch.setChecked(genericObject.getEnableAlexa().equalsIgnoreCase(UtilityConstants.STATE_TRUE) ? true : false);

            alexa_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    WriteFlag = true;
                }
            });

            ArrayList<DimmerObject> dimmerObjects = new ArrayList<>();

            String dimmerData = genericObject.getDmData();

            int intensity = 0;

            if (from.equalsIgnoreCase(UtilityConstants.ROOM)) {

                for (Map.Entry<String, DimmerObject> entry : Utility.dimmerObjectHashMap.entrySet()) {

                    DimmerObject dimmerObject = entry.getValue();

                    if (dimmerObject != null && dimmerObject.getParent_mac().equalsIgnoreCase(genericObject.getMac())
                            && dimmerObject.getParent_point().equalsIgnoreCase(genericObject.getPoint())) {

                        dimmerObjects.add(dimmerObject);

                        if (dimmerObject.getColor().equalsIgnoreCase("ww")) {

                            dimmer_switch.setChecked(true);

                        }

                        intensity = Integer.parseInt(dimmerObject.getIntensity());

                        dimmer_intensity.setValue(intensity);

                        if (dimmerData.equalsIgnoreCase("-")) {

                            dimmerData = MqttOperation.getDimmerDataForAutomation(dimmerObject);

                        } else if (!dimmerData.contains(dimmerObject.getMac())) {

                            dimmerData = dimmerData + "~" + MqttOperation.getDimmerDataForAutomation(dimmerObject);

                        }

                    }

                }

                if (!genericObject.getDmData().equalsIgnoreCase(dimmerData)) {

                    genericObject.setDmData(dimmerData);

                    MqttOperation.setAutomationData(genericObject.getAutomationData());

                }

            }

            if (!dimmerData.equalsIgnoreCase("-") && dimmerData.length() > 1) {

                String dimmerDataSplit[] = dimmerData.split("~");

                if (dimmerDataSplit.length > 0 && !from.equalsIgnoreCase(UtilityConstants.ROOM)) {

                    DimmerObject dimmerObject = new DimmerObject(Utility.getDimmerDataMap(dimmerDataSplit[0]));

                    dimmer_intensity.setValue(Integer.parseInt(dimmerObject.getIntensity()));

                    if (dimmerObject.getColor().equalsIgnoreCase("ww")) {

                        dimmer_switch.setChecked(true);

                    }

                    dimmerObjects.add(dimmerObject);

                }

                dimmer_count.setText("" + dimmerDataSplit.length);

                dimmer_layout.setVisibility(View.VISIBLE);

                dimmer_intensity_layout.setVisibility(View.VISIBLE);

                dimmer_color_layout.setVisibility(View.VISIBLE);

                dimmer_intensity.setOnBoxedPointsChangeListener(new BoxedVertical.OnValuesChangeListener() {
                    @Override
                    public void onPointsChanged(BoxedVertical boxedPoints, int value1) {

                    }

                    @Override
                    public void onStartTrackingTouch(BoxedVertical boxedPoints) {

                    }

                    @Override
                    public void onStopTrackingTouch(BoxedVertical boxedPoints) {

                        dimmer_intensity.setValue(boxedPoints.getValue());

                    }
                });

            }

            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            TextView done = popupView.findViewById(R.id.done);

            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    return true;

                }
            });

            include_in_favourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    WriteFlag = true;

                    if (include_in_favourite.isChecked()) {

                        if (generic_freeze.isChecked()) {

                            include_in_favourite.setChecked(false);

                            Toast.makeText(popupView.getContext(), R.string.disable_generic_freeze_txt, Toast.LENGTH_SHORT).show();

                        } else {

                            genericObject.setFav(UtilityConstants.STATE_TRUE);

                        }

                    } else {

                        genericObject.setFav(UtilityConstants.STATE_FALSE);

                    }

                }
            });

            generic_freeze.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    WriteFlag = true;

                    if (generic_freeze.isChecked()) {

                        if (include_in_favourite.isChecked()) {

                            generic_freeze.setChecked(false);

                            Toast.makeText(popupView.getContext(), R.string.disable_generic_favourite_txt, Toast.LENGTH_SHORT).show();

                        } else {

                            genericObject.setFreeze(UtilityConstants.STATE_TRUE);

                        }

                    } else {

                        genericObject.setFreeze(UtilityConstants.STATE_FALSE);

                    }

                }
            });

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String dimmerData = null;

                    if (dimmerObjects.size() > 0) {

                        for (DimmerObject dimmerObject : dimmerObjects) {

                            dimmerObject.setIntensity("" + dimmer_intensity.getValue());

                            if (dimmer_switch.isChecked()) {

                                dimmerObject.setColor("ww");

                            } else {

                                dimmerObject.setColor("w");

                            }
                            if (dimmerData == null) {

                                dimmerData = MqttOperation.getDimmerDataForAutomation(dimmerObject);

                            } else {

                                dimmerData = dimmerData + "~" + MqttOperation.getDimmerDataForAutomation(dimmerObject);

                            }

                            if (from.equalsIgnoreCase(UtilityConstants.ROOM)) {
//                                MqttOperation.setAutomationData(dimmerObject);
                            }

                        }

                        genericObject.setDmData(dimmerData);

                    }

                    if (from.equalsIgnoreCase(UtilityConstants.SCENE)) {

                        AddSceneActivity.updateSceneData(genericObject.getAutomationData(), UtilityConstants.ADD);

                    } else if (from.equalsIgnoreCase(UtilityConstants.SCHEDULE)) {

                        AddScheduleActivity.updateScheduleData(genericObject.getAutomationData(), UtilityConstants.ADD);

                    } else if (from.equalsIgnoreCase(UtilityConstants.MOTION)) {

                        EditMotionActivity.updateMotionData(genericObject.getAutomationData(), UtilityConstants.ADD);

                    } else {

                        genericObject.setFav(include_in_favourite.isChecked() ? UtilityConstants.STATE_TRUE : UtilityConstants.STATE_FALSE);

                        genericObject.setFreeze(generic_freeze.isChecked() ? UtilityConstants.STATE_TRUE : UtilityConstants.STATE_FALSE);

                        genericObject.setEnableAlexa(alexa_switch.isChecked() ? UtilityConstants.STATE_TRUE : UtilityConstants.STATE_FALSE);

                        if (WriteFlag) {

                            MqttOperation.writePOINT(genericObject.getFilename(), new Gson().toJson(genericObject));

                        }
                    }

                    popupWindow.dismiss();
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void showPopupWindowForPower(final View view, PowerObject powerObject) {

        try {

            WriteFlag = false;

            Switch include_in_favourite, power_freeze, alexa_switch;

            TextView power_name, done;

            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);

            View popupView = inflater.inflate(R.layout.power_setting_popup, null);

            int width = LinearLayout.LayoutParams.MATCH_PARENT;

            int height = LinearLayout.LayoutParams.MATCH_PARENT;

            include_in_favourite = popupView.findViewById(R.id.include_in_favourite);

            power_freeze = popupView.findViewById(R.id.power_freeze);

            alexa_switch = popupView.findViewById(R.id.alexa_switch);

            power_name = popupView.findViewById(R.id.power_name);

            power_name.setText(powerObject.getName());

            power_freeze.setChecked(powerObject.getFreeze().equalsIgnoreCase(UtilityConstants.STATE_TRUE));

            include_in_favourite.setChecked(powerObject.getFav().equalsIgnoreCase(UtilityConstants.STATE_TRUE));

            alexa_switch.setChecked(powerObject.getEnableAlexa().equalsIgnoreCase(UtilityConstants.STATE_TRUE) ? true : false);

            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            done = popupView.findViewById(R.id.done);

            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    return true;

                }
            });

            alexa_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    WriteFlag = true;
                }
            });

            include_in_favourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    WriteFlag = true;

                    if (include_in_favourite.isChecked()) {

                        if (power_freeze.isChecked()) {

                            include_in_favourite.setChecked(false);

                            Toast.makeText(popupView.getContext(), R.string.disable_power_freeze_txt, Toast.LENGTH_SHORT).show();

                        } else {

                            powerObject.setFav(UtilityConstants.STATE_TRUE);

                        }

                    } else {

                        powerObject.setFav(UtilityConstants.STATE_TRUE);

                    }
                }
            });

            power_freeze.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    WriteFlag = true;

                    if (power_freeze.isChecked()) {

                        if (include_in_favourite.isChecked()) {

                            power_freeze.setChecked(false);

                            Toast.makeText(popupView.getContext(), R.string.disable_power_favourite_txt, Toast.LENGTH_SHORT).show();

                        } else {

                            powerObject.setFreeze(UtilityConstants.STATE_TRUE);

                        }
                    } else {

                        powerObject.setFreeze(UtilityConstants.STATE_FALSE);

                    }
                }
            });

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    powerObject.setFav(include_in_favourite.isChecked() ? UtilityConstants.STATE_TRUE : UtilityConstants.STATE_FALSE);

                    powerObject.setFreeze(power_freeze.isChecked() ? UtilityConstants.STATE_TRUE : UtilityConstants.STATE_FALSE);

                    powerObject.setEnableAlexa(alexa_switch.isChecked() ? UtilityConstants.STATE_TRUE : UtilityConstants.STATE_FALSE);

                    if (WriteFlag) {

                        MqttOperation.writePOINT(powerObject.getFilename(), new Gson().toJson(powerObject));

                        popupWindow.dismiss();

                    }
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void showPopupForGesture(final View view, String gName) {

        try {

            GifImageView gifImageView;

            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);

            View popupView = inflater.inflate(R.layout.gesture_gif_layout, null);

            int width = LinearLayout.LayoutParams.MATCH_PARENT;

            int height = LinearLayout.LayoutParams.MATCH_PARENT;

            Button OK = popupView.findViewById(R.id.ok);

            gifImageView = popupView.findViewById(R.id.gifImageView);

            switch (gName) {

                case "Right":

                    gifImageView.setImageResource(R.drawable.right_gif);

                    break;

                case "Left":

                    gifImageView.setImageResource(R.drawable.left_gif);

                    break;

                case "Right-Left":

                    gifImageView.setImageResource(R.drawable.right_left_gif);

                    break;

                case "Left-Right":

                    gifImageView.setImageResource(R.drawable.left_right_gif);

                    break;

                case "Up":

                    gifImageView.setImageResource(R.drawable.up_gif);

                    break;

                case "Down":

                    gifImageView.setImageResource(R.drawable.down_gif);

                    break;

                case "Up-Down":

                    gifImageView.setImageResource(R.drawable.up_down_gif);

                    break;

                case "Down-Up":

                    gifImageView.setImageResource(R.drawable.down_up_gif);

                    break;

                case "Forward":

                    gifImageView.setImageResource(R.drawable.forward_gif);

                    break;

                case "Backward":

                    gifImageView.setImageResource(R.drawable.backward_gif);

                    break;

                case "Forward-Backward":

                    gifImageView.setImageResource(R.drawable.forward_backward_gif);

                    break;

                case "Backward-Forward":

                    gifImageView.setImageResource(R.drawable.backward_forward_gif);

                    break;

                case "Clockwise":

                    gifImageView.setImageResource(R.drawable.clockwise_gif);

                    break;

                case "Anti-clockwise":

                    gifImageView.setImageResource(R.drawable.anticlockwise_gif);

                    break;

                case "Wave":

                    gifImageView.setImageResource(R.drawable.wave_gif);

                    break;

                default:

                    gifImageView.setImageResource(R.drawable.left_gif);

                    break;

            }

            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

            popupWindow.showAtLocation(view, Gravity.TOP, 0, 200);

            ViewGroup root = (ViewGroup) ((Activity) popupView.getContext()).getWindow().getDecorView().getRootView();

            applyDim(root, 0.8f);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {

                    clearDim(root);
                }
            });

            OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    popupWindow.dismiss();
                    clearDim(root);
                }
            });

            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    return true;

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
