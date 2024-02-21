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
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;

public class RoomsContainerAdapter extends RecyclerView.Adapter<RoomsContainerAdapter.RecyclerViewHolder> {

    private final Context context;

    private ArrayList<RoomObject> rooms;

    public RoomsContainerAdapter(Context context, ArrayList<RoomObject> rooms) {
        this.context = context;
        this.rooms = rooms;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rooms_container_adapter, viewGroup, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            RoomObject roomObject = rooms.get(i);

            if (roomObject != null) {

                holder.room_title.setText(roomObject.getName());

                holder.room_subtitle.setText(getRoomDeviceActiveCnt(roomObject) + " Active");

                holder.room_logo.setImageDrawable(context.getResources().getDrawable(Utility.getRoomIcon(roomObject.getLogo()).getDisable_icon()));

                holder.linear_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, RoomAccessoriesActivity.class);

                        intent.putExtra(UtilityConstants.ROOM, roomObject);

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
        return (null != rooms ? rooms.size() : 0);
    }

    public int getRoomDeviceActiveCnt(RoomObject roomObject) {

        int cnt = 0;

        try {

            if (roomObject.getMac().trim().length() > 0) {

                for (FanObject fanObject : Utility.fanObjectHashMap.values()) {

                    if ((roomObject.getMac().contains(fanObject.getMac()) || roomObject.getMac().contains(fanObject.getMac().substring(2, fanObject.getMac().length()))) && !fanObject.getSpeed().equalsIgnoreCase(UtilityConstants.STATE_FALSE) && fanObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                        cnt++;
                    }
                }
                for (PowerObject powerObject : Utility.powerObjectHashMap.values()) {

                    if (roomObject.getMac().contains(powerObject.getMac()) && powerObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                        cnt++;
                    }
                }
                for (RGBObject rgbObject : Utility.rgbObjectHashMap.values()) {

                    if (roomObject.getMac().contains(rgbObject.getMac()) && rgbObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                        cnt++;
                    }
                }
                for (GenericObject genericObject : Utility.genericObjectHashMap.values()) {

                    if (roomObject.getMac().contains(genericObject.getMac()) && genericObject.getUsed().equalsIgnoreCase(UtilityConstants.STATE_TRUE) && genericObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                        cnt++;

                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return cnt;
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView room_logo;

        LinearLayout linear_layout;

        TextView room_subtitle, room_title;

        RecyclerViewHolder(View view) {
            super(view);

            room_subtitle = view.findViewById(R.id.room_subtitle);

            room_logo = view.findViewById(R.id.room_logo);

            room_title = view.findViewById(R.id.room_title);

            linear_layout = itemView.findViewById(R.id.linear_layout);

        }

    }

}
