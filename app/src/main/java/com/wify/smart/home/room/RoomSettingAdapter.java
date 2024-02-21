package com.wify.smart.home.room;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.List;


public class RoomSettingAdapter extends RecyclerView.Adapter<RoomSettingAdapter.RecyclerViewHolder> {

    private final Context context;

    private List<RoomObject> rooms;

    public RoomSettingAdapter(Context context, List<RoomObject> rooms) {
        this.context = context;
        this.rooms = rooms;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rooms_setting_adapter, viewGroup, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            RoomObject roomObject  = rooms.get(i);

            holder.room_label.setText(roomObject.getName());

            holder.view_room.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, AddRoomActivity.class);

                    i.putExtra(UtilityConstants.ROOM_OBJ, roomObject);

                    i.putExtra(UtilityConstants.ACTION, UtilityConstants.EDIT_STR);

                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(i);

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != rooms ? rooms.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView room_label;

        ImageView view_room;

        RecyclerViewHolder(View view) {
            super(view);

            room_label = view.findViewById(R.id.room_label_setting);

            view_room = view.findViewById(R.id.view_room);

        }

    }

}