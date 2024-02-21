package com.wify.smart.home.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.MotionObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.dto.UserObject;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.LinkedList;

public class RoomViseAppliancesAdapter extends RecyclerView.Adapter<RoomViseAppliancesAdapter.RecyclerViewHolder> {

    private final Context context;

    private LinkedList<Object> room_appliances;

    UserObject current_user;

    public RoomViseAppliancesAdapter(Context context, LinkedList<Object> room_appliances, UserObject userObject) {

        this.context = context;

        this.room_appliances = room_appliances;

        this.current_user = userObject;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_vise_appliances_adapter_access, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            Object object = room_appliances.get(i);

            if (object instanceof GenericObject) {

                GenericObject genericObject = (GenericObject) object;

                holder.access_name.setText(genericObject.getName());

                holder.accessories_check.setChecked(!current_user.getMac().contains(genericObject.getMac() + ":" + genericObject.getPoint()));

                holder.accessories_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (holder.accessories_check.isChecked()) {

                            SettingUserViewActivity.updateUserMacData(genericObject.getMac() + ":" + genericObject.getPoint(), UtilityConstants.DELETE);

                        } else {

                            SettingUserViewActivity.updateUserMacData(genericObject.getMac() + ":" + genericObject.getPoint(), UtilityConstants.ADD);

                        }

                    }
                });

            } else if (object instanceof FanObject) {

                FanObject fanObject = (FanObject) object;

                holder.access_name.setText(fanObject.getName());

                holder.accessories_check.setChecked(!current_user.getMac().contains(fanObject.getMac()));

                holder.accessories_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (holder.accessories_check.isChecked()) {

                            SettingUserViewActivity.updateUserMacData(fanObject.getMac(), UtilityConstants.DELETE);

                        } else {

                            SettingUserViewActivity.updateUserMacData(fanObject.getMac(), UtilityConstants.ADD);

                        }

                    }
                });

            } else if (object instanceof CurtainObject) {

                CurtainObject curtainObject = (CurtainObject) object;

                holder.access_name.setText(curtainObject.getName());

                holder.accessories_check.setChecked(!current_user.getMac().contains(curtainObject.getMac()));

                holder.accessories_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (holder.accessories_check.isChecked()) {

                            SettingUserViewActivity.updateUserMacData(curtainObject.getMac(), UtilityConstants.DELETE);


                        } else {

                            SettingUserViewActivity.updateUserMacData(curtainObject.getMac(), UtilityConstants.ADD);
                        }
                    }
                });

            } else if (object instanceof MotionObject) {

                MotionObject motionObject = (MotionObject) object;

                holder.access_name.setText(motionObject.getName());

                holder.accessories_check.setChecked(!current_user.getMac().contains(motionObject.getMac()));

                holder.accessories_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (holder.accessories_check.isChecked()) {

                            SettingUserViewActivity.updateUserMacData(motionObject.getMac(), UtilityConstants.DELETE);

                        } else {

                            SettingUserViewActivity.updateUserMacData(motionObject.getMac(), UtilityConstants.ADD);
                        }

                    }
                });
            } else if (object instanceof RGBObject) {

                RGBObject rgbObject = (RGBObject) object;

                holder.access_name.setText(rgbObject.getName());

                holder.accessories_check.setChecked(!current_user.getMac().contains(rgbObject.getMac()));

                holder.accessories_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (holder.accessories_check.isChecked()) {

                            SettingUserViewActivity.updateUserMacData(rgbObject.getMac(), UtilityConstants.DELETE);

                        } else {

                            SettingUserViewActivity.updateUserMacData(rgbObject.getMac(), UtilityConstants.ADD);

                        }

                    }
                });

            } else if (object instanceof PowerObject) {

                PowerObject powerObject = (PowerObject) object;

                holder.access_name.setText(powerObject.getName());

                holder.accessories_check.setChecked(!current_user.getMac().contains(powerObject.getMac()));

                holder.accessories_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (holder.accessories_check.isChecked()) {

                            SettingUserViewActivity.updateUserMacData(powerObject.getMac(), UtilityConstants.DELETE);

                        } else {

                            SettingUserViewActivity.updateUserMacData(powerObject.getMac(), UtilityConstants.ADD);

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
        return (null != room_appliances ? room_appliances.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView access_name;

        CheckBox accessories_check;

        RecyclerViewHolder(View view) {
            super(view);

            access_name = view.findViewById(R.id.access_name);

            accessories_check = view.findViewById(R.id.accessories_check);
        }

    }

}
