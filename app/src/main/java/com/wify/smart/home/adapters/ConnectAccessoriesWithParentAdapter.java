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
import com.wify.smart.home.dto.ParentObject;

import java.util.ArrayList;

public class ConnectAccessoriesWithParentAdapter extends RecyclerView.Adapter<ConnectAccessoriesWithParentAdapter.RecyclerViewHolder> {

    private final Context context;

    ArrayList<ParentObject> parentObjects;

    public ConnectAccessoriesWithParentAdapter(Context context, ArrayList<ParentObject> parentObjects) {

        this.context = context;

        this.parentObjects = parentObjects;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.connect_accessories_with_parent_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            ParentObject parentObject = parentObjects.get(i);

            holder.accessories_name.setText(parentObject.getName());

            holder.range.setText(parentObject.getRange());

            holder.select_room_radio.setTag(i);

            holder.select_room_radio.setChecked(i == ConnectAccessoriesWithRoomActivity.parentPosition);

            holder.select_room_radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemCheckChanged(v);

                }

            });


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void itemCheckChanged(View v) {

        ConnectAccessoriesWithRoomActivity.parentPosition = (Integer) v.getTag();

        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return (null != parentObjects ? parentObjects.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        RadioButton select_room_radio;

        TextView accessories_name, range;

        RecyclerViewHolder(View view) {
            super(view);

            accessories_name = view.findViewById(R.id.accessories_name);

            select_room_radio = view.findViewById(R.id.select_room_radio);

            range = view.findViewById(R.id.range);

        }

    }
}
