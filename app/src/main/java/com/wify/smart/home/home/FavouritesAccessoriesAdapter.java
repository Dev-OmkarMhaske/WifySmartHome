package com.wify.smart.home.home;

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

import com.wify.smart.home.R;
import com.wify.smart.home.activities.RGBSettingContainerActivity;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.helper.CustomPopups;
import com.wify.smart.home.mqtt.MqttClient;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.List;

public class FavouritesAccessoriesAdapter extends RecyclerView.Adapter<FavouritesAccessoriesAdapter.RecyclerViewHolder> {

    private final Context context;

    private List<Object> accessories;

    public FavouritesAccessoriesAdapter(Context context, List<Object> accessories) {

        this.context = context;

        this.accessories = accessories;
    }

    @Override
    public FavouritesAccessoriesAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favourites_accessories_adapter, viewGroup, false);

        return new FavouritesAccessoriesAdapter.RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            Object object = accessories.get(i);

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

    public void setFanSpeedLevel(String fanSpeedLevel, FavouritesAccessoriesAdapter.RecyclerViewHolder holder) {

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

    public void updateFanView(FanObject fanObject, final RecyclerViewHolder holder) {

        try {

            holder.fan_tile_background.setVisibility(View.VISIBLE);

            holder.fan_title.setText(fanObject.getName());

            if (!fanObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                holder.fan_tile_background.setAlpha((float) 1);

                if (!fanObject.getState().equalsIgnoreCase(UtilityConstants.STATE_FALSE)) {

                    holder.fan_subtitle.setText(UtilityConstants.ACTIVE_STATE_STR);

                } else {

                    holder.fan_subtitle.setText(UtilityConstants.INACTIVE_STATE_STR);
                }

                if (holder.fan_subtitle.getText().toString().equalsIgnoreCase(UtilityConstants.ACTIVE_STATE_STR)) {

                    holder.fan_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_white_background));

                    holder.fan_subtitle.setVisibility(View.GONE);

                    holder.fan_speed_layout.setVisibility(View.VISIBLE);

                    setFanSpeedLevel(fanObject.getSpeed(), holder);

                    holder.fan_title.setTextColor(context.getResources().getColor(R.color.text_black));

                    holder.fan_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_fan_a));

                    holder.fan_subtitle.setTextColor(context.getResources().getColor(R.color.sub_text_color));

                } else {

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

                        if (holder.fan_subtitle.getText().toString().equalsIgnoreCase(UtilityConstants.ACTIVE_STATE_STR)) {

                            fanObject.setState(UtilityConstants.STATE_FALSE);

                        } else {

                            fanObject.setState(UtilityConstants.STATE_TRUE);

                        }

                        MqttOperation.setAutomationData(fanObject.getAutomationData());

                        Utility.vibrate();

                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                    }
                });

                holder.fan_tile_background.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        Utility.vibrate();
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                        new CustomPopups().showPopupWindowForFan(view, fanObject, UtilityConstants.FAV);

                        return true;
                    }
                });

            } else {

                holder.fan_subtitle.setText(UtilityConstants.OFFLINE_STR);

                holder.fan_tile_background.setAlpha((float) 0.5);

                holder.fan_subtitle.setTextColor(context.getResources().getColor(R.color.bold_text_color));

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void updatePowerView(PowerObject powerObject, final RecyclerViewHolder holder) {

        try {

            holder.power_tile_background.setVisibility(View.VISIBLE);

            holder.power_title.setText(powerObject.getName());

            if (!powerObject.getLogo().equalsIgnoreCase("-")) {

                holder.power_logo.setImageResource(Utility.getAccessoryLogo(powerObject.getLogo()).getIcon());

            }

            if (!powerObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                holder.power_logo.setColorFilter(null);

                holder.power_tile_background.setAlpha((float) 1);

                if (powerObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.power_subtitle.setText(UtilityConstants.ACTIVE_STATE_STR);

                } else {

                    holder.power_subtitle.setText(UtilityConstants.INACTIVE_STATE_STR);

                }

                if (holder.power_subtitle.getText().toString().equalsIgnoreCase(UtilityConstants.ACTIVE_STATE_STR)) {

                    holder.power_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_white_background));

                    holder.power_title.setTextColor(context.getResources().getColor(R.color.text_black));

                    if (!powerObject.getLogo().equalsIgnoreCase("-")) {

                        holder.power_logo.setImageResource(Utility.getAccessoryLogo(powerObject.getLogo()).getIcon());

                    } else {

                        holder.power_logo.setImageResource(R.drawable.icon_ac_a);
                    }

                    holder.power_subtitle.setTextColor(context.getResources().getColor(R.color.sub_text_color));

                } else {

                    holder.power_tile_background.setBackground(context.getResources().getDrawable(R.drawable.room_tiles_shape));

                    holder.power_title.setTextColor(context.getResources().getColor(R.color.text_white));

                    if (!powerObject.getLogo().equalsIgnoreCase("-")) {

                        holder.power_logo.setImageResource(Utility.getAccessoryLogo(powerObject.getLogo()).getDisable_icon());

                    } else {

                        holder.power_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_ac_d));

                    }

                    holder.power_subtitle.setTextColor(context.getResources().getColor(R.color.text_white));
                }

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

                        if (holder.power_subtitle.getText().toString().equalsIgnoreCase(UtilityConstants.ACTIVE_STATE_STR)) {

                            powerObject.setState(UtilityConstants.STATE_FALSE);

                        } else {

                            powerObject.setState(UtilityConstants.STATE_TRUE);

                        }

                        MqttOperation.setAutomationData(powerObject.getAutomationData());

                        Utility.vibrate();

                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                    }
                });

            } else {

                holder.power_subtitle.setText(UtilityConstants.OFFLINE_STR);

                holder.power_tile_background.setAlpha((float) 0.5);

                holder.power_logo.setColorFilter(context.getResources().getColor(R.color.text_white));

                holder.power_subtitle.setTextColor(context.getResources().getColor(R.color.bold_text_color));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void updateCurtainView(CurtainObject curtainObject, final RecyclerViewHolder holder) {

        try {

            holder.curtain_tile_background.setVisibility(View.VISIBLE);

            holder.curtain_title.setText(curtainObject.getName());

            if (!curtainObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

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

                holder.curtain_tile_background.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        Utility.vibrate();

                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                        CustomPopups curtain_popup = new CustomPopups();

                        curtain_popup.showPopupWindowForCurtain(view, curtainObject, UtilityConstants.FAV);

                        return true;
                    }
                });

            } else {

                holder.curtain_subtitle.setText(UtilityConstants.OFFLINE_STR);

                holder.curtain_tile_background.setAlpha((float) 0.5);

                holder.curtain_subtitle.setTextColor(context.getResources().getColor(R.color.bold_text_color));

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void updateRGBView(RGBObject rgbObject1, final RecyclerViewHolder holder) {

        try {

            RGBObject rgbObject = (RGBObject) rgbObject1.clone();

            holder.rgb_tile_background.setVisibility(View.VISIBLE);

            holder.rgb_title.setText(rgbObject.getName());

            if (!rgbObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                holder.rgb_tile_background.setAlpha((float) 1);

                if (rgbObject.getType().equalsIgnoreCase(UtilityConstants.RGB_TYPE_SIMPLE)) {

                    holder.rgb_subtitle.setText(UtilityConstants.RGB_SIMPLE_TXT);

                } else if (rgbObject.getType().equalsIgnoreCase(UtilityConstants.RGB_TYPE_SINGLE)) {

                    holder.rgb_subtitle.setText(UtilityConstants.RGB_SINGLE_TXT);

                } else {

                    holder.rgb_subtitle.setText(rgbObject.getMode().equalsIgnoreCase(UtilityConstants.RGB_MODE_MASTER) ? "Master" : "Function");
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

                holder.rgb_tile_background.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (rgbObject.getFreeze().equalsIgnoreCase(UtilityConstants.STATE_FALSE)) {

                            if (rgbObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                                rgbObject.setState(UtilityConstants.STATE_FALSE);

                                  

                            } else {

                                rgbObject.setState(UtilityConstants.STATE_TRUE);

                            }

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

                        i.putExtra(UtilityConstants.RGB_OBJ, rgbObject);

                        i.putExtra(UtilityConstants.FROM, UtilityConstants.FAV);

                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(i);

                        return true;
                    }
                });

            } else {

                holder.rgb_subtitle.setText(UtilityConstants.OFFLINE_STR);

                holder.rgb_tile_background.setAlpha((float) 0.5);

                holder.rgb_subtitle.setTextColor(context.getResources().getColor(R.color.bold_text_color));

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void updateGenericView(GenericObject genericObject, final RecyclerViewHolder holder) {

        try {

            holder.generic_tile_background.setVisibility(View.VISIBLE);

            holder.generic_title.setText(genericObject.getName());

            if (!genericObject.getLogo().equalsIgnoreCase("-")) {

                holder.generic_logo.setImageResource(Utility.getAccessoryLogo(genericObject.getLogo()).getIcon());

            }

            if (!genericObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                holder.generic_logo.setColorFilter(null);

                holder.generic_tile_background.setAlpha((float) 1);

                holder.generic_subtitle.setText(genericObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE) ? "Active" : "InActive");

                if (holder.generic_subtitle.getText().toString().equalsIgnoreCase(UtilityConstants.ACTIVE_STATE_STR)) {

                    holder.generic_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_white_background));

                    holder.generic_title.setTextColor(context.getResources().getColor(R.color.text_black));

                    if (!genericObject.getLogo().equalsIgnoreCase("-")) {

                        holder.generic_logo.setImageResource(Utility.getAccessoryLogo(genericObject.getLogo()).getIcon());

                    } else {

                        holder.generic_logo.setImageResource(R.drawable.icon_light_fill_a);
                    }

                    holder.generic_subtitle.setTextColor(context.getResources().getColor(R.color.sub_text_color));

                } else {

                    holder.generic_tile_background.setBackground(context.getResources().getDrawable(R.drawable.room_tiles_shape));

                    holder.generic_title.setTextColor(context.getResources().getColor(R.color.text_white));

                    if (!genericObject.getLogo().equalsIgnoreCase("-")) {

                        holder.generic_logo.setImageResource(Utility.getAccessoryLogo(genericObject.getLogo()).getDisable_icon());

                    } else {

                        holder.generic_logo.setImageResource(R.drawable.icon_light_fill_d);
                    }

                    holder.generic_subtitle.setTextColor(context.getResources().getColor(R.color.text_white));

                }

                holder.generic_tile_background.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (holder.generic_subtitle.getText().toString().equalsIgnoreCase(UtilityConstants.ACTIVE_STATE_STR)) {

                            genericObject.setState(UtilityConstants.STATE_FALSE);

                        } else {

                            genericObject.setState(UtilityConstants.STATE_TRUE);

                        }

                        MqttOperation.setAutomationData(genericObject.getAutomationData());

                        Utility.vibrate();
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                    }
                });

            } else {

                holder.generic_logo.setColorFilter(context.getResources().getColor(R.color.text_white));

                holder.generic_subtitle.setText(UtilityConstants.OFFLINE_STR);

                holder.generic_tile_background.setAlpha((float) 0.5);

                holder.generic_subtitle.setTextColor(context.getResources().getColor(R.color.bold_text_color));

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != accessories ? accessories.size() : 0);
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView generic_title, motion_title, rgb_title, fan_title, curtain_title, power_title, curtain_subtitle;

        TextView generic_subtitle, motion_subtitle, fan_subtitle, power_subtitle, rgb_subtitle;

        ImageView curtain_logo, fan_speed_level_1, fan_speed_level_2, fan_speed_level_3, fan_speed_level_4, fan_speed_level_5;

        LinearLayout power_tile_background, generic_tile_background, motion_tile_background, rgb_tile_background, fan_tile_background, fan_speed_layout, curtain_tile_background;

        ImageView generic_logo, fan_logo, power_logo, rgb_logo;

        RecyclerViewHolder(View view) {
            super(view);

//            title
            motion_title = view.findViewById(R.id.motion_title);
            generic_title = view.findViewById(R.id.generic_title);
            rgb_title = view.findViewById(R.id.rgb_title);
            curtain_title = view.findViewById(R.id.curtain_title);
            fan_title = view.findViewById(R.id.fan_title);
            power_title = view.findViewById(R.id.power_title);

//            subtitle
            motion_subtitle = view.findViewById(R.id.motion_subtitle);
            generic_subtitle = view.findViewById(R.id.generic_subtitle);
            fan_subtitle = view.findViewById(R.id.fan_subtitle);
            power_subtitle = view.findViewById(R.id.power_subtitle);
            curtain_subtitle = view.findViewById(R.id.curtain_subtitle);
            rgb_subtitle = view.findViewById(R.id.rgb_subtitle);
//            tile
            generic_tile_background = view.findViewById(R.id.generic_tile_background);
            rgb_tile_background = view.findViewById(R.id.rgb_tile_background);
            motion_tile_background = view.findViewById(R.id.motion_tile_background);
            fan_tile_background = view.findViewById(R.id.fan_tile_background);
            curtain_tile_background = view.findViewById(R.id.curtain_tile_background);
            power_tile_background = view.findViewById(R.id.power_tile_background);

//            logo
            generic_logo = view.findViewById(R.id.generic_logo);
            fan_logo = view.findViewById(R.id.fan_logo);
            power_logo = view.findViewById(R.id.power_logo);
            rgb_logo = view.findViewById(R.id.rgb_logo);
            curtain_logo = view.findViewById(R.id.curtain_logo);

            fan_speed_layout = view.findViewById(R.id.fan_speed_layout);
            fan_speed_level_1 = view.findViewById(R.id.fan_speed_level_1);
            fan_speed_level_2 = view.findViewById(R.id.fan_speed_level_2);
            fan_speed_level_3 = view.findViewById(R.id.fan_speed_level_3);
            fan_speed_level_4 = view.findViewById(R.id.fan_speed_level_4);
            fan_speed_level_5 = view.findViewById(R.id.fan_speed_level_5);
        }

    }

}
