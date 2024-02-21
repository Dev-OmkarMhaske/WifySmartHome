package com.wify.smart.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.activities.AccessoriesSettingActivity;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.MotionObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.room.ChangeModuleLogoActivity;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.io.Serializable;

public class ModuleSettingAdapter extends RecyclerView.Adapter<ModuleSettingAdapter.RecyclerViewHolder> {

    public static String AccessoryName;

    private final Context context;

    private Object accessories;

    public ModuleSettingAdapter(Context context, Object accessories) {

        this.context = context;
        this.accessories = accessories;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.module_setting_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            Object object = accessories;

            if (object instanceof GenericObject) {

                GenericObject genericObject = (GenericObject) object;

                if (genericObject.getLogo().equalsIgnoreCase("-")) {

                    holder.module_logo.setImageResource(R.drawable.icon_light_fill_a);

                } else {

                    holder.module_logo.setImageResource(Utility.getAccessoryLogo(genericObject.getLogo()).getIcon());

                }

                holder.accessories_name.setText(genericObject.getName());

                holder.accessories_check.setChecked(genericObject.getUsed().equalsIgnoreCase(UtilityConstants.STATE_TRUE) ? true : false);

                holder.accessories_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (holder.accessories_check.isChecked()) {

                            genericObject.setUsed(UtilityConstants.STATE_TRUE);

                        } else {

                            genericObject.setUsed(UtilityConstants.STATE_FALSE);

                        }

                        AccessoriesSettingActivity.object = genericObject;
                    }
                });

                holder.module_logo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = new Intent(context, ChangeModuleLogoActivity.class);

                        i.putExtra(UtilityConstants.OBJECT_STR, (Serializable) object);

                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(i);
                    }
                });

            } else if (object instanceof PowerObject) {

                PowerObject powerObject = (PowerObject) object;

                holder.accessories_name.setText(powerObject.getName());

                if (powerObject.getLogo().equalsIgnoreCase("-")) {

                    holder.module_logo.setImageResource(R.drawable.icon_ac_a);

                } else {

                    holder.module_logo.setImageResource(Utility.getAccessoryLogo(powerObject.getLogo()).getIcon());

                }

                holder.accessories_name.setText(powerObject.getName());

                holder.accessories_check.setVisibility(View.GONE);

                holder.module_logo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = new Intent(context, ChangeModuleLogoActivity.class);

                        i.putExtra(UtilityConstants.OBJECT_STR, (Serializable) object);

                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(i);
                    }
                });

            } else if (object instanceof MotionObject) {

                MotionObject motionObject = (MotionObject) object;

                holder.module_logo.setImageResource(R.drawable.icon_motion_a);

                holder.accessories_name.setText(motionObject.getName());

                holder.accessories_check.setVisibility(View.GONE);

            } else if (object instanceof RGBObject) {

                RGBObject rgbObject = (RGBObject) object;

                holder.module_logo.setImageResource(R.drawable.icon_rgb_a);

                holder.accessories_name.setText(rgbObject.getName());

                holder.accessories_check.setVisibility(View.GONE);

            } else if (object instanceof CurtainObject) {

                CurtainObject curtainObject = (CurtainObject) object;

                holder.module_logo.setImageResource(R.drawable.icon_curtain_a);

                holder.accessories_name.setText(curtainObject.getName());

                holder.accessories_check.setVisibility(View.GONE);

            } else if (object instanceof FanObject) {

                FanObject fanObject = (FanObject) object;

                holder.module_logo.setImageResource(R.drawable.icon_fan_a);

                holder.accessories_name.setText(fanObject.getName());

                holder.accessories_check.setVisibility(View.GONE);

            }

            AccessoryName = holder.accessories_name.getText().toString();

            holder.accessories_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (object instanceof GenericObject) {

                        GenericObject genericObject = (GenericObject) object;

                        genericObject.setState(UtilityConstants.STATE_TRUE);

                        MqttOperation.setAutomationData(genericObject.getAutomationData());

                    } else if (object instanceof PowerObject) {

                        PowerObject powerObject = (PowerObject) object;

                        powerObject.setState(UtilityConstants.STATE_TRUE);

                        MqttOperation.setAutomationData(powerObject.getAutomationData());

                    } else if (object instanceof RGBObject) {

                        RGBObject rgbObject = (RGBObject) object;

                        rgbObject.setState(UtilityConstants.STATE_TRUE);

                        MqttOperation.setAutomationData(rgbObject.getAutomationData());

                    } else if (object instanceof CurtainObject) {

                        CurtainObject curtainObject = (CurtainObject) object;

                        curtainObject.setState(UtilityConstants.STATE_TRUE);

                        MqttOperation.setAutomationData(curtainObject.getAutomationData());

                    } else if (object instanceof FanObject) {

                        FanObject fanObject = (FanObject) object;

                        fanObject.setState(UtilityConstants.STATE_TRUE);

                        fanObject.setSpeed(UtilityConstants.STATE_TRUE);

                        MqttOperation.setAutomationData(fanObject.getAutomationData());

                    }
                }
            });

            holder.accessories_name.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    // AccessoriesSettingActivity.moduleSettingAdapter.notifyDataSetChanged();
                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {

                    AccessoryName = holder.accessories_name.getText().toString();

                    if (object instanceof GenericObject) {

                        GenericObject genericObject = (GenericObject) object;

                        genericObject.setName(AccessoryName);

                        AccessoriesSettingActivity.object = genericObject;

                    } else if (object instanceof PowerObject) {

                        PowerObject powerObject = (PowerObject) object;

                        powerObject.setName(AccessoryName);

                        AccessoriesSettingActivity.object = powerObject;

                    } else if (object instanceof RGBObject) {

                        RGBObject rgbObject = (RGBObject) object;

                        rgbObject.setName(AccessoryName);

                        AccessoriesSettingActivity.object = rgbObject;

                    } else if (object instanceof CurtainObject) {

                        CurtainObject curtainObject = (CurtainObject) object;

                        curtainObject.setName(AccessoryName);

                        AccessoriesSettingActivity.object = curtainObject;

                    } else if (object instanceof FanObject) {

                        FanObject fanObject = (FanObject) object;

                        fanObject.setName(AccessoryName);

                        AccessoriesSettingActivity.object = fanObject;

                    }

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        EditText accessories_name;

        ImageView module_logo;

        CheckBox accessories_check;

        RecyclerViewHolder(View view) {
            super(view);

            accessories_name = view.findViewById(R.id.accessories_name);

            module_logo = view.findViewById(R.id.module_logo);

            accessories_check = view.findViewById(R.id.accessories_check);
        }

    }
}
