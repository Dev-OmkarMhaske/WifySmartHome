package com.wify.smart.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.activities.ConnectAccessoriesWithRoomActivity;
import com.wify.smart.home.dto.RoomObject;

import java.util.ArrayList;

public class ConnectAccessoriesWithRoomAdapter extends RecyclerView.Adapter<ConnectAccessoriesWithRoomAdapter.RecyclerViewHolder> {

    private final Context context;

    private ArrayList<RoomObject> rooms;


    public ConnectAccessoriesWithRoomAdapter(Context context, ArrayList<RoomObject> rooms) {

        this.context = context;

        this.rooms = rooms;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.connect_accessories_with_room_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            RoomObject roomObject = rooms.get(i);

            holder.accessories_name.setText(roomObject.getName());

            holder.select_room_radio.setTag(i);

            holder.select_room_radio.setChecked(i == ConnectAccessoriesWithRoomActivity.selectedPosition);

            holder.select_room_radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemCheckChanged(v);

                    ConnectAccessoriesWithRoomActivity.SetSpinner(v.getContext(), roomObject);
                }

            });


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void itemCheckChanged(View v) {

        ConnectAccessoriesWithRoomActivity.selectedPosition = (Integer) v.getTag();

        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return (null != rooms ? rooms.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        RadioButton select_room_radio;

        TextView accessories_name;

        RecyclerViewHolder(View view) {
            super(view);

            accessories_name = view.findViewById(R.id.accessories_name);

            select_room_radio = view.findViewById(R.id.select_room_radio);

        }

    }
}
