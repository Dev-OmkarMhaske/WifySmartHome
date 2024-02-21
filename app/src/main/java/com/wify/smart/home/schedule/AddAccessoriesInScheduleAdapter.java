package com.wify.smart.home.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.LinkedList;

public class AddAccessoriesInScheduleAdapter extends RecyclerView.Adapter<AddAccessoriesInScheduleAdapter.RecyclerViewHolder> {

    private final Context context;
    LinkedList<Object> room_accessories;

    Object object = null;

    public AddAccessoriesInScheduleAdapter(Context context, LinkedList<Object> accessories) {
        this.context = context;
        this.room_accessories = accessories;
    }

    @Override
    public AddAccessoriesInScheduleAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_accessories_adapter, viewGroup, false);

        return new AddAccessoriesInScheduleAdapter.RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AddAccessoriesInScheduleAdapter.RecyclerViewHolder holder, final int i) {

        try {

            object = room_accessories.get(i);

            if (object instanceof GenericObject) {

                GenericObject genericObject = (GenericObject) object;

                holder.accessories_title.setText(genericObject.getName());

                holder.accessories_check.setChecked(AddScheduleActivity.scheduleAccessoriesSet.contains(genericObject.getMac() + ":" + genericObject.getPoint()));

                if (!genericObject.getLogo().equalsIgnoreCase("-")) {

                    holder.accessories_logo.setImageResource(Utility.getAccessoryLogo(genericObject.getLogo()).getDisable_icon());

                }

                holder.accessories_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (AddScheduleActivity.scheduleAccessoriesSet.contains(genericObject.getMac() + ":" + genericObject.getPoint())) {

                            AddScheduleActivity.scheduleAccessoriesSet.remove(genericObject.getMac() + ":" + genericObject.getPoint());

                            AddScheduleActivity.updateScheduleData(genericObject.getAutomationData(), UtilityConstants.DELETE);

                        } else {

                            AddScheduleActivity.scheduleAccessoriesSet.add(genericObject.getMac() + ":" + genericObject.getPoint());

                            AddScheduleActivity.updateScheduleData(genericObject.getAutomationData(), UtilityConstants.ADD);

                        }

                    }
                });

            } else if (object instanceof RGBObject) {

                RGBObject rgbObject = (RGBObject) object;

                holder.accessories_title.setText(rgbObject.getName());

                holder.accessories_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_rgb_d));

                holder.accessories_check.setChecked(AddScheduleActivity.scheduleAccessoriesSet.contains(rgbObject.getMac()));

                holder.accessories_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (AddScheduleActivity.scheduleAccessoriesSet.contains(rgbObject.getMac())) {

                            AddScheduleActivity.scheduleAccessoriesSet.remove(rgbObject.getMac());

                            AddScheduleActivity.updateScheduleData(rgbObject.getAutomationData(), UtilityConstants.DELETE);

                        } else {

                            AddScheduleActivity.scheduleAccessoriesSet.add(rgbObject.getMac());

                            AddScheduleActivity.updateScheduleData(rgbObject.getAutomationData(), UtilityConstants.ADD);

                        }

                    }
                });

            } else if (object instanceof CurtainObject) {

                CurtainObject curtainObject = (CurtainObject) object;

                holder.accessories_title.setText(curtainObject.getName());

                holder.accessories_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_curtain_d));

                holder.accessories_check.setChecked(AddScheduleActivity.scheduleAccessoriesSet.contains(curtainObject.getMac()));

                holder.accessories_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (AddScheduleActivity.scheduleAccessoriesSet.contains(curtainObject.getMac())) {

                            AddScheduleActivity.scheduleAccessoriesSet.remove(curtainObject.getMac());

                            AddScheduleActivity.updateScheduleData(curtainObject.getAutomationData(), UtilityConstants.DELETE);

                        } else {

                            AddScheduleActivity.scheduleAccessoriesSet.add(curtainObject.getMac());

                            AddScheduleActivity.updateScheduleData(curtainObject.getAutomationData(), UtilityConstants.ADD);

                        }

                    }
                });

            } else if (object instanceof PowerObject) {

                PowerObject powerObject = (PowerObject) object;

                holder.accessories_title.setText(powerObject.getName());

                holder.accessories_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_ac_d));

                if (!powerObject.getLogo().equalsIgnoreCase("-")) {

                    holder.accessories_logo.setImageResource(Utility.getAccessoryLogo(powerObject.getLogo()).getDisable_icon());

                }
                holder.accessories_check.setChecked(AddScheduleActivity.scheduleAccessoriesSet.contains(powerObject.getMac()));

                holder.accessories_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (AddScheduleActivity.scheduleAccessoriesSet.contains(powerObject.getMac())) {

                            AddScheduleActivity.scheduleAccessoriesSet.remove(powerObject.getMac());

                            AddScheduleActivity.updateScheduleData(powerObject.getAutomationData(), UtilityConstants.DELETE);

                        } else {

                            AddScheduleActivity.scheduleAccessoriesSet.add(powerObject.getMac());

                            AddScheduleActivity.updateScheduleData(powerObject.getAutomationData(), UtilityConstants.ADD);

                        }
                    }
                });

            } else if (object instanceof FanObject) {

                FanObject fanObject = (FanObject) object;

                holder.accessories_title.setText(fanObject.getName());

                holder.accessories_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_fan_d));

                holder.accessories_check.setChecked(AddScheduleActivity.scheduleAccessoriesSet.contains(fanObject.getMac()));

                holder.accessories_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (AddScheduleActivity.scheduleAccessoriesSet.contains(fanObject.getMac())) {

                            AddScheduleActivity.scheduleAccessoriesSet.remove(fanObject.getMac());

                            AddScheduleActivity.updateScheduleData(fanObject.getAutomationData(), UtilityConstants.DELETE);

                        } else {

                            AddScheduleActivity.scheduleAccessoriesSet.add(fanObject.getMac());

                            AddScheduleActivity.updateScheduleData(fanObject.getAutomationData(), UtilityConstants.ADD);

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
        return (null != room_accessories ? room_accessories.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView accessories_title, room_name;

        ImageView accessories_logo;

        CheckBox accessories_check;

        RecyclerViewHolder(View view) {
            super(view);

            accessories_title = view.findViewById(R.id.accessories_title);

            room_name = view.findViewById(R.id.room_name);

            accessories_logo = view.findViewById(R.id.accessories_logo);

            accessories_check = view.findViewById(R.id.accessories_check);
        }

    }
}

