package com.wify.smart.home.room;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.RoomIcons;

import java.util.ArrayList;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RecyclerViewHolder> {

    private final Context context;

    private ArrayList<RoomIcons> roomsList;

    public RoomListAdapter(Context context, ArrayList<RoomIcons> roomsList) {

        this.context = context;

        this.roomsList = roomsList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_list_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            RoomIcons room = roomsList.get(i);

            holder.room_name.setText(room.getName());

            holder.room_logo.setImageDrawable(context.getResources().getDrawable(room.getIcon()));

            holder.room_radio.setTag(i);

            holder.room_radio.setChecked(i == RoomListActivity.selectedPosition);

            holder.room_radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemCheckChanged(v);
                }

            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != roomsList ? roomsList.size() : 0);
    }

    private void itemCheckChanged(View v) {

        RoomListActivity.selectedPosition = (Integer) v.getTag();

        notifyDataSetChanged();

    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView room_name;

        RadioButton room_radio;

        ImageView room_logo;

        RecyclerViewHolder(View view) {
            super(view);

            room_name = view.findViewById(R.id.room_name);

            room_radio = view.findViewById(R.id.room_radio);

            room_logo = view.findViewById(R.id.room_logo);

        }

    }

}
