package com.wify.smart.home.scene;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.activities.RGBSettingContainerActivity;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.helper.CustomPopups;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.List;

public class ViewAccessoriesAdapter extends RecyclerView.Adapter<ViewAccessoriesAdapter.RecyclerViewHolder> {

    private final Context context;

    private List<Object> accessories;

    public ViewAccessoriesAdapter(Context context, List<Object> accessories) {
        this.context = context;
        this.accessories = accessories;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_accessories_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            Object object = accessories.get(i);

            holder.generic_tile_background.setVisibility(View.GONE);

            holder.rgb_tile_background.setVisibility(View.GONE);

            holder.fan_tile_background.setVisibility(View.GONE);

            holder.curtain_tile_background.setVisibility(View.GONE);

            holder.power_tile_background.setVisibility(View.GONE);

            if (object instanceof GenericObject) {

                GenericObject genericObject = (GenericObject) object;

                holder.generic_tile_background.setVisibility(View.VISIBLE);

                holder.generic_title.setText(genericObject.getName());

                if (genericObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.generic_subtitle.setText(UtilityConstants.ACTIVE_STATE_STR);

                    holder.generic_subtitle.setTextColor(context.getResources().getColor(R.color.text_black));

                    holder.generic_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_white_background));

                    holder.generic_logo.setColorFilter(context.getResources().getColor(R.color.bold_text_color));

                    holder.generic_title.setTextColor(context.getResources().getColor(R.color.text_black));

                } else {

                    holder.generic_logo.setColorFilter(null);

                    holder.generic_subtitle.setText(UtilityConstants.INACTIVE_STATE_STR);

                    holder.generic_subtitle.setTextColor(context.getResources().getColor(R.color.text_white));

                    holder.generic_title.setTextColor(context.getResources().getColor(R.color.text_white));

                    holder.generic_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_background));

                }

                if (!genericObject.getLogo().equalsIgnoreCase("-")) {

                    holder.generic_logo.setImageResource(Utility.getAccessoryLogo(genericObject.getLogo()).getDisable_icon());

                }

                holder.generic_tile_background.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (genericObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                            genericObject.setState(UtilityConstants.STATE_FALSE);

                            holder.generic_subtitle.setText(UtilityConstants.INACTIVE_STATE_STR);

                            holder.generic_subtitle.setTextColor(context.getResources().getColor(R.color.text_white));

                            holder.generic_logo.setColorFilter(null);

                            holder.generic_title.setTextColor(context.getResources().getColor(R.color.text_white));

                            holder.generic_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_background));

                        } else {

                            genericObject.setState(UtilityConstants.STATE_TRUE);

                            holder.generic_subtitle.setText(UtilityConstants.ACTIVE_STATE_STR);

                            holder.generic_subtitle.setTextColor(context.getResources().getColor(R.color.text_black));

                            holder.generic_title.setTextColor(context.getResources().getColor(R.color.text_black));

                            holder.generic_logo.setColorFilter(context.getResources().getColor(R.color.bold_text_color));

                            holder.generic_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_white_background));

                        }

                        AddSceneActivity.updateSceneData(genericObject.getAutomationData(), UtilityConstants.ADD);
                    }
                });


                if (genericObject.getDmData().length() > 1 && !genericObject.getDmData().equalsIgnoreCase("-")) {

                    holder.generic_setting.setVisibility(View.VISIBLE);

                    holder.generic_setting.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new CustomPopups().showPopupWindowForGeneric(view, genericObject, UtilityConstants.SCENE);
                        }

                    });
                }

            } else if (object instanceof FanObject) {

                FanObject fanObject = (FanObject) object;

                holder.fan_tile_background.setVisibility(View.VISIBLE);

                holder.fan_title.setText(fanObject.getName());

                if (fanObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.fan_subtitle.setText(UtilityConstants.ACTIVE_STATE_STR);

                    holder.fan_subtitle.setTextColor(context.getResources().getColor(R.color.text_black));

                    holder.fan_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_white_background));

                    holder.fan_speed_layout.setVisibility(View.VISIBLE);

                    setFanSpeedLevel(fanObject.getSpeed(), holder);

                    holder.fan_logo.setColorFilter(context.getResources().getColor(R.color.bold_text_color));

                    holder.fan_title.setTextColor(context.getResources().getColor(R.color.text_black));

                } else {

                    holder.fan_subtitle.setVisibility(View.VISIBLE);

                    holder.fan_subtitle.setText(UtilityConstants.INACTIVE_STATE_STR);

                    holder.fan_subtitle.setTextColor(context.getResources().getColor(R.color.text_white));

                    holder.fan_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_background));

                    holder.fan_logo.setColorFilter(null);

                    holder.fan_speed_layout.setVisibility(View.GONE);

                    holder.fan_title.setTextColor(context.getResources().getColor(R.color.text_white));

                }

                holder.fan_tile_background.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (fanObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                            fanObject.setState(UtilityConstants.STATE_FALSE);

                            holder.fan_logo.setColorFilter(null);

                            holder.fan_speed_layout.setVisibility(View.GONE);

                            holder.fan_subtitle.setVisibility(View.VISIBLE);

                            holder.fan_title.setTextColor(context.getResources().getColor(R.color.text_white));

                            holder.fan_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_background));

                            holder.fan_subtitle.setText(UtilityConstants.INACTIVE_STATE_STR);

                            holder.fan_subtitle.setTextColor(context.getResources().getColor(R.color.text_white));

                        } else {

                            fanObject.setState(UtilityConstants.STATE_TRUE);

                            holder.fan_speed_layout.setVisibility(View.VISIBLE);

                            holder.fan_subtitle.setVisibility(View.GONE);

                            setFanSpeedLevel(fanObject.getSpeed(), holder);

                            holder.fan_title.setTextColor(context.getResources().getColor(R.color.text_black));

                            holder.fan_logo.setColorFilter(context.getResources().getColor(R.color.bold_text_color));

                            holder.fan_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_white_background));

                            holder.fan_subtitle.setText(UtilityConstants.ACTIVE_STATE_STR);

                            holder.fan_subtitle.setTextColor(context.getResources().getColor(R.color.text_black));

                        }

                        AddSceneActivity.updateSceneData(fanObject.getAutomationData(), UtilityConstants.ADD);
                    }
                });

                holder.fan_tile_background.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        new CustomPopups().showPopupWindowForFan(view, fanObject, UtilityConstants.SCENE);

                        return false;
                    }
                });

                holder.fan_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new CustomPopups().showPopupWindowForFan(view, fanObject, UtilityConstants.SCENE);
                    }
                });

            } else if (object instanceof CurtainObject) {

                CurtainObject curtainObject = (CurtainObject) object;

                holder.curtain_tile_background.setVisibility(View.VISIBLE);

                holder.curtain_title.setText(curtainObject.getName());

                if (curtainObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.curtain_subtitle.setText(UtilityConstants.STATE_OPEN_STR);

                    holder.curtain_subtitle.setTextColor(context.getResources().getColor(R.color.text_black));

                    holder.curtain_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_white_background));

                    holder.curtain_logo.setColorFilter(context.getResources().getColor(R.color.bold_text_color));

                    holder.curtain_title.setTextColor(context.getResources().getColor(R.color.text_black));

                } else {

                    holder.curtain_subtitle.setText(curtainObject.getState().equalsIgnoreCase(UtilityConstants.STATE_PAUSE) ? UtilityConstants.STATE_PAUSE_STR : UtilityConstants.STATE_CLOSE_STR);

                    holder.curtain_subtitle.setTextColor(context.getResources().getColor(R.color.text_white));

                    holder.curtain_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_background));

                    holder.curtain_logo.setColorFilter(null);

                    holder.curtain_title.setTextColor(context.getResources().getColor(R.color.text_white));

                }

                holder.curtain_tile_background.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        new CustomPopups().showPopupWindowForCurtain(view, curtainObject, UtilityConstants.SCENE);

                        return false;
                    }
                });

                holder.curtain_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new CustomPopups().showPopupWindowForCurtain(view, curtainObject, UtilityConstants.SCENE);
                    }
                });

            } else if (object instanceof RGBObject) {

                RGBObject rgbObject = (RGBObject) object;

                holder.rgb_tile_background.setVisibility(View.VISIBLE);

                holder.rgb_title.setText(rgbObject.getName());

                if (rgbObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.rgb_subtitle.setText(UtilityConstants.ACTIVE_STATE_STR);

                    holder.rgb_subtitle.setTextColor(context.getResources().getColor(R.color.text_black));

                    holder.rgb_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_white_background));

                    holder.rgb_logo.setColorFilter(context.getResources().getColor(R.color.bold_text_color));

                    holder.rgb_title.setTextColor(context.getResources().getColor(R.color.text_black));

                } else {

                    holder.rgb_subtitle.setText(UtilityConstants.INACTIVE_STATE_STR);

                    holder.rgb_subtitle.setTextColor(context.getResources().getColor(R.color.text_white));

                    holder.rgb_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_background));

                    holder.rgb_logo.setColorFilter(null);

                    holder.rgb_title.setTextColor(context.getResources().getColor(R.color.text_white));

                }

                holder.rgb_tile_background.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (rgbObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                            rgbObject.setState(UtilityConstants.STATE_FALSE);

                              

                            holder.rgb_logo.setColorFilter(null);

                            holder.rgb_title.setTextColor(context.getResources().getColor(R.color.text_white));

                            holder.rgb_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_background));

                            holder.rgb_subtitle.setText(UtilityConstants.INACTIVE_STATE_STR);

                            holder.rgb_subtitle.setTextColor(context.getResources().getColor(R.color.text_white));

                        } else {

                            rgbObject.setState(UtilityConstants.STATE_TRUE);

                            holder.rgb_title.setTextColor(context.getResources().getColor(R.color.text_black));

                            holder.rgb_logo.setColorFilter(context.getResources().getColor(R.color.bold_text_color));

                            holder.rgb_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_white_background));

                            holder.rgb_subtitle.setText(UtilityConstants.ACTIVE_STATE_STR);

                            holder.rgb_subtitle.setTextColor(context.getResources().getColor(R.color.text_black));

                        }

                        AddSceneActivity.updateSceneData(rgbObject.getAutomationData(), UtilityConstants.ADD);
                    }
                });

                holder.rgb_tile_background.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        Intent i = new Intent(context, RGBSettingContainerActivity.class);

                        i.putExtra(UtilityConstants.FROM, UtilityConstants.SCENE);

                        i.putExtra(UtilityConstants.RGB_OBJ, rgbObject);

                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(i);

                        return false;
                    }
                });

                holder.rgb_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = new Intent(context, RGBSettingContainerActivity.class);

                        i.putExtra(UtilityConstants.FROM, UtilityConstants.SCENE);

                        i.putExtra(UtilityConstants.RGB_OBJ, rgbObject);

                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(i);
                    }
                });

            } else if (object instanceof PowerObject) {

                PowerObject powerObject = (PowerObject) object;

                holder.power_tile_background.setVisibility(View.VISIBLE);

                holder.power_title.setText(powerObject.getName());

                if (powerObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    holder.power_subtitle.setText(UtilityConstants.ACTIVE_STATE_STR);

                    holder.power_subtitle.setTextColor(context.getResources().getColor(R.color.text_black));

                    holder.power_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_white_background));

                    holder.power_logo.setColorFilter(context.getResources().getColor(R.color.bold_text_color));

                    holder.power_title.setTextColor(context.getResources().getColor(R.color.text_black));

                } else {

                    holder.power_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_background));

                    holder.power_logo.setColorFilter(null);

                    holder.power_title.setTextColor(context.getResources().getColor(R.color.text_white));

                    holder.power_subtitle.setText(UtilityConstants.INACTIVE_STATE_STR);

                    holder.power_subtitle.setTextColor(context.getResources().getColor(R.color.text_white));

                }

                holder.power_tile_background.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (powerObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                            powerObject.setState(UtilityConstants.STATE_FALSE);

                            holder.power_logo.setColorFilter(null);

                            holder.power_title.setTextColor(context.getResources().getColor(R.color.text_white));

                            holder.power_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_background));

                            holder.power_subtitle.setText(UtilityConstants.INACTIVE_STATE_STR);

                            holder.power_subtitle.setTextColor(context.getResources().getColor(R.color.text_white));

                        } else {

                            powerObject.setState(UtilityConstants.STATE_TRUE);

                            holder.power_title.setTextColor(context.getResources().getColor(R.color.text_black));

                            holder.power_logo.setColorFilter(context.getResources().getColor(R.color.bold_text_color));

                            holder.power_tile_background.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_white_background));

                            holder.power_subtitle.setText(UtilityConstants.ACTIVE_STATE_STR);

                            holder.power_subtitle.setTextColor(context.getResources().getColor(R.color.text_black));
                        }

                        AddSceneActivity.updateSceneData(powerObject.getAutomationData(), UtilityConstants.ADD);
                    }
                });


                if (!powerObject.getLogo().equalsIgnoreCase("-")) {

                    holder.power_logo.setImageResource(Utility.getAccessoryLogo(powerObject.getLogo()).getDisable_icon());

                }

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void setFanSpeedLevel(String fanSpeedLevel, ViewAccessoriesAdapter.RecyclerViewHolder holder) {

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

    @Override
    public int getItemCount() {
        return (null != accessories ? accessories.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView fan_speed_level_1, fan_speed_level_2, fan_speed_level_3, fan_speed_level_4, fan_speed_level_5;

        TextView generic_subtitle, rgb_subtitle, fan_subtitle, power_subtitle, curtain_subtitle;

        TextView generic_title, rgb_title, fan_title, curtain_title, power_title;

        LinearLayout fan_speed_layout, power_tile_background, generic_tile_background, rgb_tile_background, fan_tile_background, curtain_tile_background;

        ImageView curtain_logo, power_logo, rgb_logo, fan_setting, fan_logo, rgb_setting, curtain_setting, generic_setting, generic_logo;

        RecyclerViewHolder(View view) {
            super(view);
//            title
            generic_title = view.findViewById(R.id.generic_title);
            rgb_title = view.findViewById(R.id.rgb_title);
            curtain_title = view.findViewById(R.id.curtain_title);
            fan_title = view.findViewById(R.id.fan_title);
            power_title = view.findViewById(R.id.power_title);

            generic_subtitle = view.findViewById(R.id.generic_subtitle);
            fan_subtitle = view.findViewById(R.id.fan_subtitle);
            rgb_subtitle = view.findViewById(R.id.rgb_subtitle);
            power_subtitle = view.findViewById(R.id.power_subtitle);
            curtain_subtitle = view.findViewById(R.id.curtain_subtitle);

            fan_speed_level_1 = view.findViewById(R.id.fan_speed_level_1);
            fan_speed_level_2 = view.findViewById(R.id.fan_speed_level_2);
            fan_speed_level_3 = view.findViewById(R.id.fan_speed_level_3);
            fan_speed_level_4 = view.findViewById(R.id.fan_speed_level_4);
            fan_speed_level_5 = view.findViewById(R.id.fan_speed_level_5);

            generic_logo = view.findViewById(R.id.generic_logo);
            power_logo = view.findViewById(R.id.power_logo);
            fan_logo = view.findViewById(R.id.fan_logo);
            curtain_logo = view.findViewById(R.id.curtain_logo);
            rgb_logo = view.findViewById(R.id.rgb_logo);
            power_logo = view.findViewById(R.id.power_logo);

//            setting
            fan_setting = view.findViewById(R.id.fan_setting);
            rgb_setting = view.findViewById(R.id.rgb_setting);
            curtain_setting = view.findViewById(R.id.curtain_setting);
            generic_setting = view.findViewById(R.id.generic_setting);

//            backgrounds
            generic_tile_background = view.findViewById(R.id.generic_tile_background);
            rgb_tile_background = view.findViewById(R.id.rgb_tile_background);
            fan_tile_background = view.findViewById(R.id.fan_tile_background);
            curtain_tile_background = view.findViewById(R.id.curtain_tile_background);
            power_tile_background = view.findViewById(R.id.power_tile_background);

            fan_speed_layout = view.findViewById(R.id.fan_speed_layout);

        }

    }
}
