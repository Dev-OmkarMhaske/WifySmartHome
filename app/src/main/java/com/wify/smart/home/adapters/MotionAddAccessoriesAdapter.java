package com.wify.smart.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.activities.EditMotionActivity;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.LinkedList;

public class MotionAddAccessoriesAdapter extends RecyclerView.Adapter<MotionAddAccessoriesAdapter.RecyclerViewHolder> {

    private final Context context;
  
    private LinkedList<Object> accessories;

    public MotionAddAccessoriesAdapter(Context context, LinkedList<Object> accessories) {
        this.context = context;
        this.accessories = accessories;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_accessories_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            Object object = accessories.get(i);

            holder.accessories_layout.setVisibility(View.GONE);

            if (object instanceof GenericObject) {

                holder.accessories_layout.setVisibility(View.VISIBLE);

                GenericObject genericObject = (GenericObject) object;

                holder.accessories_title.setText(genericObject.getName());

                holder.motion_check.setChecked(EditMotionActivity.motion_accessories.contains(genericObject.getMac() + ":" + genericObject.getPoint()));

                if (!genericObject.getLogo().equalsIgnoreCase("-")) {

                    holder.accessories_logo.setImageResource(Utility.getAccessoryLogo(genericObject.getLogo()).getDisable_icon());

                }

                holder.motion_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (EditMotionActivity.motion_accessories.contains(genericObject.getMac() + ":" + genericObject.getPoint())) {

                            EditMotionActivity.motion_accessories.remove(genericObject.getMac() + ":" + genericObject.getPoint());

                            EditMotionActivity.updateMotionData(genericObject.getAutomationData(), UtilityConstants.DELETE);

                        } else {

                            EditMotionActivity.motion_accessories.add(genericObject.getMac() + ":" + genericObject.getPoint());

                            EditMotionActivity.updateMotionData(genericObject.getAutomationData(), UtilityConstants.ADD);

                        }
                    }
                });


            } else if (object instanceof RGBObject) {

                holder.accessories_layout.setVisibility(View.VISIBLE);

                RGBObject rgbObject = (RGBObject) object;

                holder.accessories_title.setText(rgbObject.getName());

                holder.accessories_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_rgb_d));

                holder.motion_check.setChecked(EditMotionActivity.motion_accessories.contains(rgbObject.getMac()));

                holder.motion_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (EditMotionActivity.motion_accessories.contains(rgbObject.getMac())) {

                            EditMotionActivity.motion_accessories.remove(rgbObject.getMac());

                            EditMotionActivity.updateMotionData(rgbObject.getAutomationData(), UtilityConstants.DELETE);

                        } else {

                            EditMotionActivity.motion_accessories.add(rgbObject.getMac());

                            EditMotionActivity.updateMotionData(rgbObject.getAutomationData(),  UtilityConstants.ADD);

                        }

                    }
                });

            } else if (object instanceof CurtainObject) {

                holder.accessories_layout.setVisibility(View.VISIBLE);

                CurtainObject curtainObject = (CurtainObject) object;

                holder.accessories_title.setText(curtainObject.getName());

                holder.motion_check.setChecked(EditMotionActivity.motion_accessories.contains(curtainObject.getMac()));

                holder.accessories_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_curtain_d));

                holder.motion_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (EditMotionActivity.motion_accessories.contains(curtainObject.getMac())) {

                            EditMotionActivity.motion_accessories.remove(curtainObject.getMac());

                            EditMotionActivity.updateMotionData(curtainObject.getAutomationData(), UtilityConstants.DELETE);

                        } else {

                            EditMotionActivity.motion_accessories.add(curtainObject.getMac());

                            EditMotionActivity.updateMotionData(curtainObject.getAutomationData(),  UtilityConstants.ADD);

                        }

                    }
                });

            } else if (object instanceof FanObject) {

                holder.accessories_layout.setVisibility(View.VISIBLE);

                FanObject fanObject = (FanObject) object;

                holder.accessories_title.setText(fanObject.getName());

                holder.motion_check.setChecked(EditMotionActivity.motion_accessories.contains(fanObject.getMac()));

                holder.accessories_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_fan_d));

                holder.motion_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (EditMotionActivity.motion_accessories.contains(fanObject.getMac())) {

                            EditMotionActivity.motion_accessories.remove(fanObject.getMac());

                            EditMotionActivity.updateMotionData(fanObject.getAutomationData(), UtilityConstants.DELETE);

                        } else {

                            EditMotionActivity.motion_accessories.add(fanObject.getMac());

                            EditMotionActivity.updateMotionData(fanObject.getAutomationData(),  UtilityConstants.ADD);

                        }

                    }
                });

            } else if (object instanceof PowerObject) {

                holder.accessories_layout.setVisibility(View.VISIBLE);

                PowerObject powerObject = (PowerObject) object;

                holder.accessories_title.setText(powerObject.getName());

                holder.accessories_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_ac_d));

                holder.motion_check.setChecked(EditMotionActivity.motion_accessories.contains(powerObject.getMac()));

                if (!powerObject.getLogo().equalsIgnoreCase("-")) {

                    holder.accessories_logo.setImageResource(Utility.getAccessoryLogo(powerObject.getLogo()).getDisable_icon());

                }
                holder.motion_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (EditMotionActivity.motion_accessories.contains(powerObject.getMac())) {

                            EditMotionActivity.motion_accessories.remove(powerObject.getMac());

                            EditMotionActivity.updateMotionData(powerObject.getAutomationData(), UtilityConstants.DELETE);

                        } else {

                            EditMotionActivity.motion_accessories.add(powerObject.getMac());

                            EditMotionActivity.updateMotionData(powerObject.getAutomationData(),  UtilityConstants.ADD);

                        }
                    }
                });

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

        TextView accessories_title;
        ImageView accessories_logo;
        LinearLayout accessories_layout;
        CheckBox motion_check;

        RecyclerViewHolder(View view) {
            super(view);

            accessories_title = view.findViewById(R.id.accessories_title);

            accessories_logo = view.findViewById(R.id.accessories_logo);
            accessories_layout = view.findViewById(R.id.linear_layout);
            motion_check = view.findViewById(R.id.accessories_check);

        }

    }
}
