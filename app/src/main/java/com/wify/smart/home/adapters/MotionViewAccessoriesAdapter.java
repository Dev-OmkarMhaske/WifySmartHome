package com.wify.smart.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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

public class MotionViewAccessoriesAdapter extends RecyclerView.Adapter<MotionViewAccessoriesAdapter.RecyclerViewHolder> {

    private final Context context;

    private List<Object> motion_accessories;

    public MotionViewAccessoriesAdapter(Context context, List<Object> motion_accessories) {
        this.context = context;
        this.motion_accessories = motion_accessories;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.motion_view_accessories_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            holder.generic_tile.setVisibility(View.GONE);

            holder.fan_tile.setVisibility(View.GONE);

            holder.curtain_tile.setVisibility(View.GONE);

            holder.rgb_tile.setVisibility(View.GONE);

            holder.power_tile.setVisibility(View.GONE);

            Object object = motion_accessories.get(i);

            if (object instanceof GenericObject) {

                GenericObject genericObject = (GenericObject) object;

                holder.generic_tile.setVisibility(View.VISIBLE);

                if (!genericObject.getLogo().equalsIgnoreCase("-")) {

                    holder.generic_logo.setImageResource(Utility.getAccessoryLogo(genericObject.getLogo()).getDisable_icon());

                }

                holder.generic_title.setText(genericObject.getName());

            } else if (object instanceof FanObject) {

                FanObject fanObject = (FanObject) object;

                holder.fan_tile.setVisibility(View.VISIBLE);

                holder.fan_title.setText(fanObject.getName());

                holder.fan_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new CustomPopups().showPopupWindowForFan(view, fanObject, UtilityConstants.MOTION);
                    }
                });

            } else if (object instanceof CurtainObject) {

                CurtainObject curtainObject = (CurtainObject) object;

                holder.curtain_tile.setVisibility(View.VISIBLE);

                holder.curtain_title.setText(curtainObject.getName());

                holder.curtain_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new CustomPopups().showPopupWindowForCurtain(view, curtainObject, UtilityConstants.MOTION);
                    }
                });

            } else if (object instanceof PowerObject) {

                PowerObject powerObject = (PowerObject) object;

                holder.power_tile.setVisibility(View.VISIBLE);

                if (!powerObject.getLogo().equalsIgnoreCase("-")) {

                    holder.power_logo.setImageResource(Utility.getAccessoryLogo(powerObject.getLogo()).getDisable_icon());

                }

                holder.power_title.setText(powerObject.getName());

            } else if (object instanceof RGBObject) {

                RGBObject rgbObject = (RGBObject) object;

                holder.rgb_tile.setVisibility(View.VISIBLE);

                holder.rgb_title.setText(rgbObject.getName());

                holder.rgb_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, RGBSettingContainerActivity.class);

                        intent.putExtra(UtilityConstants.FROM, UtilityConstants.MOTION);

                        intent.putExtra(UtilityConstants.RGB_OBJ, rgbObject);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);
                    }
                });

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return (null != motion_accessories ? motion_accessories.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        LinearLayout generic_tile, rgb_tile, fan_tile, curtain_tile, power_tile;

        TextView generic_title, rgb_title, fan_title, curtain_title, power_title;

        ImageView curtain_setting, fan_setting, rgb_setting, generic_setting, generic_logo, power_logo;

        RecyclerViewHolder(View view) {
            super(view);

            curtain_setting = view.findViewById(R.id.curtain_setting);

            fan_setting = view.findViewById(R.id.fan_setting);

            rgb_setting = view.findViewById(R.id.rgb_setting);

            generic_setting = view.findViewById(R.id.generic_setting);

            generic_tile = view.findViewById(R.id.generic_tile_background);

            fan_tile = view.findViewById(R.id.fan_tile_background);

            curtain_tile = view.findViewById(R.id.curtain_tile_background);

            rgb_tile = view.findViewById(R.id.rgb_tile_background);

            power_tile = view.findViewById(R.id.power_tile_background);

            generic_logo = view.findViewById(R.id.generic_logo);

            power_logo = view.findViewById(R.id.power_logo);

            generic_title = view.findViewById(R.id.generic_title);

            fan_title = view.findViewById(R.id.fan_title);

            curtain_title = view.findViewById(R.id.curtain_title);

            rgb_title = view.findViewById(R.id.rgb_title);

            power_title = view.findViewById(R.id.power_title);

        }

    }
}