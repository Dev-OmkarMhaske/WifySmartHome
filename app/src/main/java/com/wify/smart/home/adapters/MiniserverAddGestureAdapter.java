package com.wify.smart.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.activities.MiniserverAddGestureActivity;
import com.wify.smart.home.dto.GestureObject;
import com.wify.smart.home.helper.CustomPopups;

import java.util.List;

public class MiniserverAddGestureAdapter extends RecyclerView.Adapter<MiniserverAddGestureAdapter.RecyclerViewHolder> {

    private final Context context;

    private List<GestureObject> gestureObjectList;

    public MiniserverAddGestureAdapter(Context context, List<GestureObject> gestureObjectList) {

        this.context = context;

        this.gestureObjectList = gestureObjectList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.miniserver_add_gesture_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            GestureObject gestureObject = gestureObjectList.get(i);

            holder.gesture_name.setText(gestureObject.getName());

            holder.gesture_radio.setTag(i);

            holder.gesture_logo.setImageDrawable(context.getResources().getDrawable(gestureObject.getIcon()));

            holder.gesture_radio.setChecked(i == MiniserverAddGestureActivity.selectedPosition);

            holder.gesture_radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemCheckChanged(v);

                    MiniserverAddGestureActivity.GestureName = gestureObject.getName();

                    new CustomPopups().showPopupForGesture(v, gestureObject.getName());

                }

            });


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void itemCheckChanged(View v) {
        MiniserverAddGestureActivity.selectedPosition = (Integer) v.getTag();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != gestureObjectList ? gestureObjectList.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView gesture_logo;

        TextView gesture_name;

        RadioButton gesture_radio;

        RecyclerViewHolder(View view) {

            super(view);

            gesture_logo = view.findViewById(R.id.gesture_logo);

            gesture_name = view.findViewById(R.id.gesture_name);

            gesture_radio = view.findViewById(R.id.gesture_radio);

        }

    }
}
