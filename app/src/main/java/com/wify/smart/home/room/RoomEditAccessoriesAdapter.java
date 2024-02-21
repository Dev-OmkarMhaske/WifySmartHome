package com.wify.smart.home.room;

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
import com.wify.smart.home.activities.AccessoriesSettingActivity;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.MotionObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.utils.UtilityConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoomEditAccessoriesAdapter extends RecyclerView.Adapter<RoomEditAccessoriesAdapter.RecyclerViewHolder> {

    private final Context context;
    
    private List<Object> objectArrayList;
    
    private String roomName;

    public RoomEditAccessoriesAdapter(Context context, List<Object> objectArrayList, String roomName) {
        
        this.context = context;
        
        this.objectArrayList = objectArrayList;
        
        this.roomName = roomName;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_edit_accessories_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            Object object = objectArrayList.get(i);

            if (object instanceof GenericObject) {

                GenericObject genericObject = (GenericObject) object;

                holder.title_accessories.setText(genericObject.getName());

            } else if (object instanceof PowerObject) {

                PowerObject powerObject = (PowerObject) object;

                holder.title_accessories.setText(powerObject.getName());

            } else if (object instanceof MotionObject) {

                MotionObject motionObject = (MotionObject) object;

                holder.title_accessories.setText(motionObject.getName());

            } else if (object instanceof RGBObject) {

                RGBObject rgbObject = (RGBObject) object;

                holder.title_accessories.setText(rgbObject.getName());

            } else if (object instanceof CurtainObject) {

                CurtainObject curtainObject = (CurtainObject) object;

                holder.title_accessories.setText(curtainObject.getName());

            } else if (object instanceof FanObject) {

                FanObject fanObject = (FanObject) object;

                holder.title_accessories.setText(fanObject.getName());

            }

            holder.setting_accessories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, AccessoriesSettingActivity.class);

                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    i.putExtra(UtilityConstants.ACCESSORY, (Serializable) object);

                    i.putExtra(UtilityConstants.ROOM_NAME, roomName);

                    context.startActivity(i);
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != objectArrayList ? objectArrayList.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView title_accessories;

        ImageView setting_accessories;

        LinearLayout setting_layout;

        RecyclerViewHolder(View view) {
            super(view);

            setting_accessories = view.findViewById(R.id.setting_accessories);

            title_accessories = view.findViewById(R.id.title_accessories);

            setting_layout = view.findViewById(R.id.setting_layout);
        }

    }

}
