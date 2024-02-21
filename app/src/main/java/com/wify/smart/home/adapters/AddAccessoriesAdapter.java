package com.wify.smart.home.adapters;

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
import com.wify.smart.home.scene.AddSceneActivity;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.LinkedList;

public class AddAccessoriesAdapter extends RecyclerView.Adapter<AddAccessoriesAdapter.RecyclerViewHolder> {

    private final Context context;

    LinkedList<Object> room_accessories;

    public AddAccessoriesAdapter(Context context, LinkedList<Object> accessories) {

        this.context = context;
        this.room_accessories = accessories;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_accessories_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            Object object = room_accessories.get(i);

            if (object instanceof GenericObject) {

                GenericObject genericObject = (GenericObject) object;

                holder.accessories_title.setText(genericObject.getName());

                if (!genericObject.getLogo().equalsIgnoreCase("-")) {

                    holder.accessories_logo.setImageResource(Utility.getAccessoryLogo(genericObject.getLogo()).getDisable_icon());

                }

                holder.accessories_check.setChecked(AddSceneActivity.addSceneAccessories.contains(genericObject.getMac() + ":" + genericObject.getPoint()));

                holder.accessories_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (AddSceneActivity.addSceneAccessories.contains(genericObject.getMac() + ":" + genericObject.getPoint())) {

                            AddSceneActivity.addSceneAccessories.remove(genericObject.getMac() + ":" + genericObject.getPoint());

                            AddSceneActivity.updateSceneData(genericObject.getAutomationData(), UtilityConstants.DELETE);

                        } else {

                            AddSceneActivity.addSceneAccessories.add(genericObject.getMac() + ":" + genericObject.getPoint());

                            AddSceneActivity.updateSceneData(genericObject.getAutomationData(), UtilityConstants.ADD);

                        }

                    }
                });

            } else if (object instanceof RGBObject) {

                RGBObject rgbObject = (RGBObject) object;

                holder.accessories_title.setText(rgbObject.getName());

                holder.accessories_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_rgb_d));

                holder.accessories_check.setChecked(AddSceneActivity.addSceneAccessories.contains(rgbObject.getMac()));

                holder.accessories_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (AddSceneActivity.addSceneAccessories.contains(rgbObject.getMac())) {

                            AddSceneActivity.addSceneAccessories.remove(rgbObject.getMac());

                            AddSceneActivity.updateSceneData(rgbObject.getAutomationData(), UtilityConstants.DELETE);

                        } else {

                              

                            AddSceneActivity.addSceneAccessories.add(rgbObject.getMac());

                            AddSceneActivity.updateSceneData(rgbObject.getAutomationData(), UtilityConstants.ADD);

                        }
                    }
                });


            } else if (object instanceof CurtainObject) {

                CurtainObject curtainObject = (CurtainObject) object;

                holder.accessories_title.setText(curtainObject.getName());

                holder.accessories_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_curtain_d));

                holder.accessories_check.setChecked(AddSceneActivity.addSceneAccessories.contains(curtainObject.getMac()));

                holder.accessories_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (AddSceneActivity.addSceneAccessories.contains(curtainObject.getMac())) {

                            AddSceneActivity.addSceneAccessories.remove(curtainObject.getMac());

                            AddSceneActivity.updateSceneData(curtainObject.getAutomationData(), UtilityConstants.DELETE);

                        } else {

                            AddSceneActivity.addSceneAccessories.add(curtainObject.getMac());

                            AddSceneActivity.updateSceneData(curtainObject.getAutomationData(), UtilityConstants.ADD);
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
                holder.accessories_check.setChecked(AddSceneActivity.addSceneAccessories.contains(powerObject.getMac()));

                holder.accessories_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (AddSceneActivity.addSceneAccessories.contains(powerObject.getMac())) {

                            AddSceneActivity.addSceneAccessories.remove(powerObject.getMac());

                            AddSceneActivity.updateSceneData(powerObject.getAutomationData(), UtilityConstants.DELETE);

                        } else {

                            AddSceneActivity.addSceneAccessories.add(powerObject.getMac());

                            AddSceneActivity.updateSceneData(powerObject.getAutomationData(), UtilityConstants.ADD);
                        }
                    }
                });

            } else if (object instanceof FanObject) {

                FanObject fanObject = (FanObject) object;

                holder.accessories_title.setText(fanObject.getName());

                holder.accessories_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_fan_d));

                holder.accessories_check.setChecked(AddSceneActivity.addSceneAccessories.contains(fanObject.getMac()));

                holder.accessories_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (AddSceneActivity.addSceneAccessories.contains(fanObject.getMac())) {

                            AddSceneActivity.addSceneAccessories.remove(fanObject.getMac());

                            AddSceneActivity.updateSceneData(fanObject.getAutomationData(), UtilityConstants.DELETE);

                        } else {

                            AddSceneActivity.addSceneAccessories.add(fanObject.getMac());

                            AddSceneActivity.updateSceneData(fanObject.getAutomationData(), UtilityConstants.ADD);
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

        CheckBox accessories_check;

        ImageView accessories_logo;

        RecyclerViewHolder(View view) {
            super(view);

            accessories_title = view.findViewById(R.id.accessories_title);

            room_name = view.findViewById(R.id.room_name);

            accessories_check = view.findViewById(R.id.accessories_check);

            accessories_logo = view.findViewById(R.id.accessories_logo);
        }

    }
}
