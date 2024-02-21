package com.wify.smart.home.room;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.activities.EditMotionActivity;
import com.wify.smart.home.activities.RGBSettingContainerActivity;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.MotionObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.helper.CustomPopups;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.List;

public class RoomViewAccessoriesAdapter extends RecyclerView.Adapter<RoomViewAccessoriesAdapter.RecyclerViewHolder> {

    private final Context context;

    List<Object> objectList;

    public RoomViewAccessoriesAdapter(Context context, List<Object> accessories) {
        this.context = context;
        this.objectList = accessories;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_view_accessories_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            Object object = objectList.get(i);

            holder.motion_tile_background.setVisibility(View.GONE);

            holder.generic_tile_background.setVisibility(View.GONE);

            holder.rgb_tile_background.setVisibility(View.GONE);

            holder.fan_tile_background.setVisibility(View.GONE);

            holder.curtain_tile_background.setVisibility(View.GONE);

            holder.power_tile_background.setVisibility(View.GONE);

            if (object instanceof GenericObject) {

                GenericObject genericObject = (GenericObject) object;

                updateGenericView(genericObject, holder);

                for (ObservableField<String> observableField : Utility.runtimeObservable) {

                    if (observableField.get().equalsIgnoreCase(genericObject.getMac() + ":" + genericObject.getPoint())) {

                        observableField.addOnPropertyChangedCallback(new androidx.databinding.Observable.OnPropertyChangedCallback() {
                            @Override
                            public void onPropertyChanged(Observable sender, int propertyId) {

                                updateGenericView(Utility.genericObjectHashMap.get(genericObject.getMac() + ":" + genericObject.getPoint()), holder);

                            }
                        });

                    }

                }

            } else if (object instanceof MotionObject) {

                MotionObject motionObject = (MotionObject) object;

                updateMotionView(motionObject, holder);

                for (ObservableField<String> observableField : Utility.runtimeObservable) {

                    if (observableField.get().equalsIgnoreCase(motionObject.getMac())) {

                        observableField.addOnPropertyChangedCallback(new androidx.databinding.Observable.OnPropertyChangedCallback() {
                            @Override
                            public void onPropertyChanged(Observable sender, int propertyId) {

                                updateMotionView(Utility.motionObjectHashMap.get(motionObject.getMac()), holder);

                            }
                        });

                    }

                }

            } else if (object instanceof RGBObject) {

                RGBObject rgbObject = (RGBObject) object;

                updateRGBView(rgbObject, holder);

                for (ObservableField<String> observableField : Utility.runtimeObservable) {

                    if (observableField.get().equalsIgnoreCase(rgbObject.getMac())) {

                        observableField.addOnPropertyChangedCallback(new androidx.databinding.Observable.OnPropertyChangedCallback() {
                            @Override
                            public void onPropertyChanged(Observable sender, int propertyId) {

                                updateRGBView(Utility.rgbObjectHashMap.get(rgbObject.getMac()), holder);

                            }
                        });

                    }

                }

            } else if (object instanceof FanObject) {

                FanObject fanObject = (FanObject) object;

                updateFanView(fanObject, holder);

                for (ObservableField<String> observableField : Utility.runtimeObservable) {

                    if (observableField.get().equalsIgnoreCase(fanObject.getMac())) {

                        observableField.addOnPropertyChangedCallback(new androidx.databinding.Observable.OnPropertyChangedCallback() {
                            @Override
                            public void onPropertyChanged(Observable sender, int propertyId) {

                                updateFanView(Utility.fanObjectHashMap.get(fanObject.getMac()), holder);

                            }
                        });

                    }

                }

            } else if (object instanceof CurtainObject) {

                CurtainObject curtainObject = (CurtainObject) object;

                updateCurtainView(curtainObject, holder);

                for (ObservableField<String> observableField : Utility.runtimeObservable) {

                    if (observableField.get().equalsIgnoreCase(curtainObject.getMac())) {

                        observableField.addOnPropertyChangedCallback(new androidx.databinding.Observable.OnPropertyChangedCallback() {
                            @Override
                            public void onPropertyChanged(Observable sender, int propertyId) {

                                updateCurtainView(Utility.curtainObjectHashMap.get(curtainObject.getMac()), holder);

                            }
                        });

                    }

                }

            } else if (object instanceof PowerObject) {

                PowerObject powerObject = (PowerObject) object;

                updatePowerView(powerObject, holder);

                for (ObservableField<String> observableField : Utility.runtimeObservable) {

                    if (observableField.get().equalsIgnoreCase(powerObject.getMac())) {

                        observableField.addOnPropertyChangedCallback(new androidx.databinding.Observable.OnPropertyChangedCallback() {
                            @Override
                            public void onPropertyChanged(Observable sender, int propertyId) {

                                updatePowerView(Utility.powerObjectHashMap.get(powerObject.getMac()), holder);

                            }
                        });
                    }
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void setFanSpeedLevel(String fanSpeedLevel, RecyclerViewHolder holder) {

        try {

            if (fanSpeedLevel.equalsIgnoreCase("1")) {

                holder.fan_speed_level_1.setColorFilter(context.getResources().getColor(R.color.bold_text_color));
                holder.fan_speed_level_2.setColorFilter(null);
                holder.fan_speed_level_3.setColorFilter(null);
                holder.fan_speed_level_4.setColorFilter(null);
                holder.fan_speed_level_5.setColorFilter(null);

            } else if (fanSpeedLevel.equalsIgnoreCase("2")) {

                holder.fan_speed_level_1.setColorFilter(context.getResources().getColor(R.color.bold_text_color));
                holder.fan_speed_level_2.setColorFilter(context.getResources().getColor(R.color.bold_text_color));
                holder.fan_speed_level_3.setColorFilter(null);
                holder.fan_speed_level_4.setColorFilter(null);
                holder.fan_speed_level_5.setColorFilter(null);

            } else if (fanSpeedLevel.equalsIgnoreCase("3")) {

                holder.fan_speed_level_1.setColorFilter(context.getResources().getColor(R.color.bold_text_color));
                holder.fan_speed_level_2.setColorFilter(context.getResources().getColor(R.color.bold_text_color));
                holder.fan_speed_level_3.setColorFilter(context.getResources().getColor(R.color.bold_text_color));
                holder.fan_speed_level_4.setColorFilter(null);
                holder.fan_speed_level_5.setColorFilter(null);
            } else if (fanSpeedLevel.equalsIgnoreCase("4")) {

                holder.fan_speed_level_1.setColorFilter(context.getResources().getColor(R.color.bold_text_color));
                holder.fan_speed_level_2.setColorFilter(context.getResources().getColor(R.color.bold_text_color));
                holder.fan_speed_level_3.setColorFilter(context.getResources().getColor(R.color.bold_text_color));
                holder.fan_speed_level_4.setColorFilter(context.getResources().getColor(R.color.bold_text_color));
                holder.fan_speed_level_5.setColorFilter(null);
            } else if (fanSpeedLevel.equalsIgnoreCase("5")) {

                holder.fan_speed_level_1.setColorFilter(context.getResources().getColor(R.color.bold_text_color));
                holder.fan_speed_level_2.setColorFilter(context.getResources().getColor(R.color.bold_text_color));
                holder.fan_speed_level_3.setColorFilter(context.getResources().getColor(R.color.bold_text_color));
                holder.fan_speed_level_4.setColorFilter(context.getResources().getColor(R.color.bold_text_color));
                holder.fan_speed_level_5.setColorFilter(context.getResources().getColor(R.color.bold_text_color));

            } else {
                holder.fan_speed_level_1.setColorFilter(null);
                holder.fan_speed_level_2.setColorFilter(null);
                holder.fan_speed_level_3.setColorFilter(null);
                holder.fan_speed_level_4.setColorFilter(null);
                holder.fan_speed_level_5.setColorFilter(null);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void updateGenericView(GenericObject genericObject, RecyclerViewHolder holder) {

        try {

            holder.generic_tile_background.setVisibility(View.VISIBLE);

            holder.generic_freeze.setVisibility(View.GONE);

            holder.generic_title.setText(genericObject.getName());

            if (!genericObject.getLogo().equalsIgnoreCase("-")) {

                holder.generic_logo.setImageResource(Utility.getAccessoryLogo(genericObject.getLogo()).getIcon());

            }

            if (!genericObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                holder.generic_logo.setColorFilter(null);

                holder.generic_tile_background.setAlpha((float) 1);

                holder.generic_setting.setVisibility(View.VISIBLE);

                if (genericObject.getFav().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.generic_freeze.setVisibility(View.VISIBLE);

                    holder.generic_freeze.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_favourite));

                } else if (genericObject.getFreeze().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.generic_freeze.setVisibility(View.VISIBLE);
                    holder.generic_freeze.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_lock));
                }

                if (genericObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.generic_subtitle.setText(UtilityConstants.ACTIVE_STATE_STR);

                    holder.generic_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_white_background));

                    holder.generic_title.setTextColor(context.getResources().getColor(R.color.text_black));

                    if (!genericObject.getLogo().equalsIgnoreCase("-")) {

                        holder.generic_logo.setImageResource(Utility.getAccessoryLogo(genericObject.getLogo()).getIcon());

                    } else {

                        holder.generic_logo.setImageResource(R.drawable.icon_light_fill_a);
                    }

                    holder.generic_subtitle.setTextColor(context.getResources().getColor(R.color.sub_text_color));

                } else {

                    holder.generic_subtitle.setText(UtilityConstants.INACTIVE_STATE_STR);

                    holder.generic_tile_background.setBackground(context.getResources().getDrawable(R.drawable.room_tiles_shape));

                    holder.generic_title.setTextColor(context.getResources().getColor(R.color.text_white));

                    if (!genericObject.getLogo().equalsIgnoreCase("-")) {

                        holder.generic_logo.setImageResource(Utility.getAccessoryLogo(genericObject.getLogo()).getDisable_icon());

                    } else {

                        holder.generic_logo.setImageResource(R.drawable.icon_light_fill_d);
                    }

                    holder.generic_subtitle.setTextColor(context.getResources().getColor(R.color.text_white));

                }

                holder.generic_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Utility.vibrate();
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                        CustomPopups geneCustomPopups = new CustomPopups();
                        geneCustomPopups.showPopupWindowForGeneric(view, genericObject, UtilityConstants.ROOM);

                    }
                });

                holder.generic_tile_background.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        Utility.vibrate();
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                        CustomPopups geneCustomPopups = new CustomPopups();
                        geneCustomPopups.showPopupWindowForGeneric(view, genericObject, UtilityConstants.ROOM);
                        return true;
                    }
                });

                holder.generic_tile_background.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (genericObject.getFreeze().equalsIgnoreCase(UtilityConstants.STATE_FALSE)) {

                            if (holder.generic_subtitle.getText().toString().equalsIgnoreCase(UtilityConstants.ACTIVE_STATE_STR)) {

                                genericObject.setState(UtilityConstants.STATE_FALSE);

                            } else {

                                genericObject.setState(UtilityConstants.STATE_TRUE);

                            }
                            MqttOperation.setAutomationData(genericObject.getAutomationData());
                        }

                        Utility.vibrate();

                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));
                    }
                });

            } else {

                holder.generic_setting.setVisibility(View.INVISIBLE);

                holder.generic_subtitle.setText(UtilityConstants.OFFLINE_STR);

                holder.generic_tile_background.setAlpha((float) 0.5);

                holder.generic_logo.setColorFilter(context.getResources().getColor(R.color.text_white));

                holder.generic_subtitle.setTextColor(context.getResources().getColor(R.color.bold_text_color));

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void updateFanView(FanObject fanObject, RecyclerViewHolder holder) {

        try {

            holder.fan_tile_background.setVisibility(View.VISIBLE);

            holder.fan_title.setText(fanObject.getName());

            holder.fan_freeze.setVisibility(View.GONE);

            if (!fanObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                holder.fan_setting.setVisibility(View.VISIBLE);

                holder.fan_tile_background.setAlpha((float) 1);

                if (fanObject.getFav().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.fan_freeze.setVisibility(View.VISIBLE);
                    holder.fan_freeze.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_favourite));

                } else if (fanObject.getFreeze().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.fan_freeze.setVisibility(View.VISIBLE);
                    holder.fan_freeze.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_lock));

                }

                if (fanObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.fan_subtitle.setText(UtilityConstants.ACTIVE_STATE_STR);

                    holder.fan_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_white_background));

                    holder.fan_subtitle.setVisibility(View.GONE);

                    holder.fan_speed_layout.setVisibility(View.VISIBLE);

                    setFanSpeedLevel(fanObject.getSpeed(), holder);

                    holder.fan_title.setTextColor(context.getResources().getColor(R.color.text_black));

                    holder.fan_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_fan_a));

                    holder.fan_subtitle.setTextColor(context.getResources().getColor(R.color.sub_text_color));

                } else {

                    holder.fan_subtitle.setText(UtilityConstants.INACTIVE_STATE_STR);

                    holder.fan_tile_background.setBackground(context.getResources().getDrawable(R.drawable.room_tiles_shape));

                    holder.fan_title.setTextColor(context.getResources().getColor(R.color.text_white));

                    holder.fan_subtitle.setVisibility(View.VISIBLE);

                    holder.fan_speed_layout.setVisibility(View.GONE);

                    holder.fan_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_fan_d));

                    holder.fan_subtitle.setTextColor(context.getResources().getColor(R.color.text_white));

                }

                holder.fan_tile_background.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (fanObject.getFreeze().equalsIgnoreCase(UtilityConstants.STATE_FALSE)) {

                            if (holder.fan_subtitle.getText().toString().equalsIgnoreCase(UtilityConstants.ACTIVE_STATE_STR)) {

                                fanObject.setState(UtilityConstants.STATE_FALSE);

                            } else {

                                fanObject.setState(UtilityConstants.STATE_TRUE);

                            }

                            System.out.println("write fanObject >>>" + new Gson().toJson(fanObject));

                            MqttOperation.setAutomationData(fanObject.getAutomationData());

                        }

                        Utility.vibrate();

                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                    }
                });

                holder.fan_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Utility.vibrate();
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                        CustomPopups fanSpeedPopup = new CustomPopups();
                        fanSpeedPopup.showPopupWindowForFan(view, fanObject, UtilityConstants.ROOM);

                    }
                });

                holder.fan_tile_background.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        Utility.vibrate();
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                        CustomPopups fanSpeedPopup = new CustomPopups();
                        fanSpeedPopup.showPopupWindowForFan(view, fanObject, UtilityConstants.ROOM);

                        return true;
                    }
                });

            } else {

                holder.fan_setting.setVisibility(View.INVISIBLE);

                holder.fan_subtitle.setText(UtilityConstants.OFFLINE_STR);

                holder.fan_tile_background.setAlpha((float) 0.5);

                holder.fan_subtitle.setTextColor(context.getResources().getColor(R.color.bold_text_color));

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void updateRGBView(RGBObject rgbObject1, RecyclerViewHolder holder) {

        try {

            RGBObject rgbObject = (RGBObject) rgbObject1.clone();

            holder.rgb_tile_background.setVisibility(View.VISIBLE);

            holder.rgb_title.setText(rgbObject.getName());

            holder.rgb_freeze.setVisibility(View.GONE);

            if (!rgbObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                holder.rgb_tile_background.setAlpha((float) 1);

                holder.rgb_setting.setVisibility(View.VISIBLE);

                if (rgbObject.getType().equalsIgnoreCase("0")) {

                    holder.rgb_subtitle.setText(UtilityConstants.RGB_SIMPLE_TXT);

                } else if (rgbObject.getType().equalsIgnoreCase("2")) {

                    holder.rgb_subtitle.setText("Single Color");

                } else {

                    holder.rgb_subtitle.setText(rgbObject.getMode().equalsIgnoreCase("1") ? "Master" : "Function");
                }

                if (rgbObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.rgb_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_white_background));

                    holder.rgb_title.setTextColor(context.getResources().getColor(R.color.text_black));

                    holder.rgb_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_rgb_a));

                    holder.rgb_subtitle.setTextColor(context.getResources().getColor(R.color.sub_text_color));

                } else {

                    holder.rgb_tile_background.setBackground(context.getResources().getDrawable(R.drawable.room_tiles_shape));

                    holder.rgb_title.setTextColor(context.getResources().getColor(R.color.text_white));

                    holder.rgb_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_rgb_d));

                    holder.rgb_subtitle.setTextColor(context.getResources().getColor(R.color.text_white));

                }

                if (rgbObject.getFav().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.rgb_freeze.setVisibility(View.VISIBLE);

                    holder.rgb_freeze.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_favourite));

                } else if (rgbObject.getFreeze().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.rgb_freeze.setVisibility(View.VISIBLE);

                }

                holder.rgb_tile_background.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (rgbObject.getFreeze().equalsIgnoreCase(UtilityConstants.STATE_FALSE)) {

                            if (rgbObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                                rgbObject.setState(UtilityConstants.STATE_FALSE);

                            } else {

                                rgbObject.setState(UtilityConstants.STATE_TRUE);

                            }

                            System.out.println("write rgb object >>>" + new Gson().toJson(rgbObject.getAutomationData()));

                            MqttOperation.setAutomationData(rgbObject.getAutomationData());

                        }

                        Utility.vibrate();

                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                    }
                });

                holder.rgb_tile_background.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        Utility.vibrate();
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                        Intent i = new Intent(context, RGBSettingContainerActivity.class);

                        i.putExtra(UtilityConstants.FROM, UtilityConstants.ROOM);

                        i.putExtra(UtilityConstants.RGB_OBJ, rgbObject);

                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(i);

                        return true;
                    }
                });

                holder.rgb_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Utility.vibrate();
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                        Intent i = new Intent(context, RGBSettingContainerActivity.class);

                        i.putExtra(UtilityConstants.FROM, UtilityConstants.ROOM);

                        i.putExtra(UtilityConstants.RGB_OBJ, rgbObject);

                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(i);

                    }
                });

            } else {

                holder.rgb_setting.setVisibility(View.INVISIBLE);

                holder.rgb_subtitle.setText(UtilityConstants.OFFLINE_STR);

                holder.rgb_tile_background.setAlpha((float) 0.5);

                holder.rgb_subtitle.setTextColor(context.getResources().getColor(R.color.bold_text_color));

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void updateMotionView(MotionObject motionObject, RecyclerViewHolder holder) {

        try {

            holder.motion_tile_background.setVisibility(View.VISIBLE);

            holder.motion_title.setText(motionObject.getName());

            holder.motion_freeze.setVisibility(View.GONE);

            if (!motionObject.getActive().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                holder.motion_setting.setVisibility(View.VISIBLE);

                holder.motion_tile_background.setAlpha((float) 1);

                if (motionObject.getFav().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.motion_freeze.setVisibility(View.VISIBLE);
                    holder.motion_freeze.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_favourite));

                }
                if (motionObject.getActive().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.motion_subtitle.setText(UtilityConstants.ACTIVE_STATE_STR);

                    holder.motion_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_white_background));

                    holder.motion_title.setTextColor(context.getResources().getColor(R.color.text_black));

                    holder.motion_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_motion_a));

                    holder.motion_subtitle.setTextColor(context.getResources().getColor(R.color.sub_text_color));

                } else {

                    holder.motion_subtitle.setText(UtilityConstants.INACTIVE_STATE_STR);

                    holder.motion_tile_background.setBackground(context.getResources().getDrawable(R.drawable.room_tiles_shape));

                    holder.motion_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_motion_d));

                    holder.motion_title.setTextColor(context.getResources().getColor(R.color.text_white));

                    holder.motion_subtitle.setTextColor(context.getResources().getColor(R.color.text_white));

                }

                holder.motion_tile_background.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        Utility.vibrate();

                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                        Intent intent = new Intent(context, EditMotionActivity.class);

                        intent.putExtra("motion_obj", motionObject);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);

                        return true;
                    }
                });

                holder.motion_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Utility.vibrate();

                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                        Intent i = new Intent(context, EditMotionActivity.class);

                        i.putExtra(UtilityConstants.MOTION_OBJ, motionObject);

                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(i);

                    }
                });

            } else {

                holder.motion_setting.setVisibility(View.INVISIBLE);

                holder.motion_subtitle.setText(UtilityConstants.OFFLINE_STR);

                holder.motion_tile_background.setAlpha((float) 0.5);

                holder.motion_subtitle.setTextColor(context.getResources().getColor(R.color.bold_text_color));

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void updateCurtainView(CurtainObject curtainObject, RecyclerViewHolder holder) {

        try {

            holder.curtain_tile_background.setVisibility(View.VISIBLE);

            holder.curtain_title.setText(curtainObject.getName());

            holder.curtain_freeze.setVisibility(View.GONE);

            if (!curtainObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                holder.curtain_setting.setVisibility(View.VISIBLE);

                holder.curtain_tile_background.setAlpha((float) 1);

                if (curtainObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.curtain_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_white_background));
                    holder.curtain_logo.setColorFilter(context.getResources().getColor(R.color.bold_text_color));
                    holder.curtain_title.setTextColor(context.getResources().getColor(R.color.text_black));
                    holder.curtain_subtitle.setTextColor(context.getResources().getColor(R.color.sub_text_color));
                    holder.curtain_subtitle.setText(UtilityConstants.STATE_OPEN_STR);

                } else if (curtainObject.getState().equalsIgnoreCase(UtilityConstants.STATE_PAUSE)) {

                    holder.curtain_tile_background.setBackground(context.getResources().getDrawable(R.drawable.room_tiles_shape));
                    holder.curtain_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_curtain_d));
                    holder.curtain_title.setTextColor(context.getResources().getColor(R.color.text_white));
                    holder.curtain_subtitle.setTextColor(context.getResources().getColor(R.color.text_white));
                    holder.curtain_logo.setColorFilter(context.getResources().getColor(R.color.text_white));
                    holder.curtain_subtitle.setText(UtilityConstants.STATE_PAUSE_STR);

                } else {

                    holder.curtain_tile_background.setBackground(context.getResources().getDrawable(R.drawable.room_tiles_shape));
                    holder.curtain_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_curtain_d));
                    holder.curtain_title.setTextColor(context.getResources().getColor(R.color.text_white));
                    holder.curtain_subtitle.setTextColor(context.getResources().getColor(R.color.text_white));
                    holder.curtain_logo.setColorFilter(context.getResources().getColor(R.color.text_white));

                    holder.curtain_subtitle.setText(curtainObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE) ? UtilityConstants.STATE_OPEN_STR : UtilityConstants.STATE_CLOSE_STR);
                }

                if (curtainObject.getFav().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.curtain_freeze.setVisibility(View.VISIBLE);

                    holder.curtain_freeze.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_favourite));

                }

                holder.curtain_tile_background.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        Utility.vibrate();
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                        CustomPopups curtain_popup = new CustomPopups();

                        curtain_popup.showPopupWindowForCurtain(view, curtainObject, UtilityConstants.ROOM);

                        return true;
                    }
                });

                holder.curtain_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Utility.vibrate();
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                        CustomPopups curtain_popup = new CustomPopups();

                        curtain_popup.showPopupWindowForCurtain(view, curtainObject, UtilityConstants.ROOM);

                    }
                });

            } else {

                holder.curtain_setting.setVisibility(View.INVISIBLE);

                holder.curtain_subtitle.setText(UtilityConstants.OFFLINE_STR);

                holder.curtain_tile_background.setAlpha((float) 0.5);

                holder.curtain_subtitle.setTextColor(context.getResources().getColor(R.color.bold_text_color));

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void updatePowerView(PowerObject powerObject, RecyclerViewHolder holder) {

        try {

            holder.power_tile_background.setVisibility(View.VISIBLE);

            holder.power_title.setText(powerObject.getName());

            holder.power_freeze.setVisibility(View.GONE);

            if (!powerObject.getLogo().equalsIgnoreCase("-")) {

                holder.power_logo.setImageResource(Utility.getAccessoryLogo(powerObject.getLogo()).getIcon());

            }

            if (!powerObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                holder.power_logo.setColorFilter(null);

                holder.power_setting.setVisibility(View.VISIBLE);

                holder.power_tile_background.setAlpha((float) 1);

                if (powerObject.getFav().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.power_freeze.setVisibility(View.VISIBLE);

                    holder.power_freeze.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_favourite));

                } else if (powerObject.getFreeze().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.power_freeze.setVisibility(View.VISIBLE);

                    holder.power_freeze.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_lock));

                }

                if (powerObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.power_subtitle.setText(UtilityConstants.ACTIVE_STATE_STR);

                    holder.power_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_white_background));

                    holder.power_title.setTextColor(context.getResources().getColor(R.color.text_black));

                    if (!powerObject.getLogo().equalsIgnoreCase("-")) {

                        holder.power_logo.setImageResource(Utility.getAccessoryLogo(powerObject.getLogo()).getIcon());

                    } else {

                        holder.power_logo.setImageResource(R.drawable.icon_ac_a);
                    }

                    holder.power_subtitle.setTextColor(context.getResources().getColor(R.color.sub_text_color));

                } else {

                    holder.power_subtitle.setText(UtilityConstants.INACTIVE_STATE_STR);

                    holder.power_tile_background.setBackground(context.getResources().getDrawable(R.drawable.room_tiles_shape));

                    holder.power_title.setTextColor(context.getResources().getColor(R.color.text_white));

                    if (!powerObject.getLogo().equalsIgnoreCase("-")) {

                        holder.power_logo.setImageResource(Utility.getAccessoryLogo(powerObject.getLogo()).getDisable_icon());

                    } else {

                        holder.power_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_ac_d));

                    }

                    holder.power_subtitle.setTextColor(context.getResources().getColor(R.color.text_white));

                }

                holder.power_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Utility.vibrate();

                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                        CustomPopups powerPopup = new CustomPopups();

                        powerPopup.showPopupWindowForPower(view, powerObject);

                    }
                });

                holder.power_tile_background.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        Utility.vibrate();

                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                        CustomPopups powerPopup = new CustomPopups();

                        powerPopup.showPopupWindowForPower(view, powerObject);

                        return true;
                    }
                });

                holder.power_tile_background.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (powerObject.getFreeze().equalsIgnoreCase(UtilityConstants.STATE_FALSE)) {

                            if (holder.power_subtitle.getText().toString().equalsIgnoreCase(UtilityConstants.ACTIVE_STATE_STR)) {

                                powerObject.setState(UtilityConstants.STATE_FALSE);

                            } else {

                                powerObject.setState(UtilityConstants.STATE_TRUE);

                            }

                            MqttOperation.setAutomationData(powerObject.getAutomationData());

                        }

                        Utility.vibrate();

                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                    }
                });

            } else {

                holder.power_setting.setVisibility(View.INVISIBLE);

                holder.power_subtitle.setText(UtilityConstants.OFFLINE_STR);

                holder.power_tile_background.setAlpha((float) 0.5);

                holder.power_logo.setColorFilter(context.getResources().getColor(R.color.text_white));

                holder.power_subtitle.setTextColor(context.getResources().getColor(R.color.bold_text_color));

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != objectList ? objectList.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView generic_subtitle, rgb_subtitle, motion_subtitle, fan_subtitle, power_subtitle, curtain_subtitle;

        TextView generic_title, motion_title, rgb_title, fan_title, curtain_title, power_title;

        ImageView curtain_logo, motion_setting, fan_setting, rgb_setting, curtain_setting, generic_setting, power_setting;

        ImageView fan_speed_level_1, fan_speed_level_2, fan_speed_level_3, fan_speed_level_4, fan_speed_level_5;

        LinearLayout power_tile_background, generic_tile_background, motion_tile_background, rgb_tile_background, fan_tile_background, fan_speed_layout, curtain_tile_background;

        ImageView generic_freeze, fan_freeze, power_freeze, motion_freeze, curtain_freeze, rgb_freeze;

        ImageView generic_logo, fan_logo, power_logo, motion_logo, rgb_logo;

        RecyclerViewHolder(View view) {
            super(view);

//            title
            motion_title = view.findViewById(R.id.motion_title);

            generic_title = view.findViewById(R.id.generic_title);

            rgb_title = view.findViewById(R.id.rgb_title);

            curtain_title = view.findViewById(R.id.curtain_title);

            fan_title = view.findViewById(R.id.fan_title);

            power_title = view.findViewById(R.id.power_title);

//            freeze
            generic_freeze = view.findViewById(R.id.generic_freeze);
            fan_freeze = view.findViewById(R.id.fan_freeze);
            power_freeze = view.findViewById(R.id.power_freeze);
            motion_freeze = view.findViewById(R.id.motion_freeze);
            curtain_freeze = view.findViewById(R.id.curtain_freeze);
            rgb_freeze = view.findViewById(R.id.rgb_freeze);

//            logo
            generic_logo = view.findViewById(R.id.generic_logo);
            fan_logo = view.findViewById(R.id.fan_logo);
            power_logo = view.findViewById(R.id.power_logo);
            motion_logo = view.findViewById(R.id.motion_logo);
            rgb_logo = view.findViewById(R.id.rgb_logo);
            curtain_logo = view.findViewById(R.id.curtain_logo);

//            subtitle

            motion_subtitle = view.findViewById(R.id.motion_subtitle);

            generic_subtitle = view.findViewById(R.id.generic_subtitle);

            fan_subtitle = view.findViewById(R.id.fan_subtitle);
            rgb_subtitle = view.findViewById(R.id.rgb_subtitle);
            power_subtitle = view.findViewById(R.id.power_subtitle);
            curtain_subtitle = view.findViewById(R.id.curtain_subtitle);
//            background

            generic_tile_background = view.findViewById(R.id.generic_tile_background);

            rgb_tile_background = view.findViewById(R.id.rgb_tile_background);

            motion_tile_background = view.findViewById(R.id.motion_tile_background);

            fan_tile_background = view.findViewById(R.id.fan_tile_background);
            fan_speed_layout = view.findViewById(R.id.fan_speed_layout);

            curtain_tile_background = view.findViewById(R.id.curtain_tile_background);

            power_tile_background = view.findViewById(R.id.power_tile_background);

//            imageview setting

            rgb_setting = view.findViewById(R.id.rgb_setting);
            motion_setting = view.findViewById(R.id.motion_setting);
            fan_setting = view.findViewById(R.id.fan_setting);
            curtain_setting = view.findViewById(R.id.curtain_setting);
            generic_setting = view.findViewById(R.id.generic_setting);
            power_setting = view.findViewById(R.id.power_setting);

//            fan speed level
            fan_speed_level_1 = view.findViewById(R.id.fan_speed_level_1);
            fan_speed_level_2 = view.findViewById(R.id.fan_speed_level_2);
            fan_speed_level_3 = view.findViewById(R.id.fan_speed_level_3);
            fan_speed_level_4 = view.findViewById(R.id.fan_speed_level_4);
            fan_speed_level_5 = view.findViewById(R.id.fan_speed_level_5);

        }

    }

}
