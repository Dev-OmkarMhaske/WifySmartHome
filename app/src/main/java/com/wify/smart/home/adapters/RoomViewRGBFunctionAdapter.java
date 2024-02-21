package com.wify.smart.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.activities.RGBSettingContainerActivity;
import com.wify.smart.home.fragments.RGBFunctionFragment;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.LinkedList;

public class RoomViewRGBFunctionAdapter extends RecyclerView.Adapter<RoomViewRGBFunctionAdapter.RecyclerViewHolder> {

    private final LinkedList<String> arrayList;
    private final Context context;

    public RoomViewRGBFunctionAdapter(Context context, LinkedList<String> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_view_rgb_function_adapter, viewGroup, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            holder.label.setText(arrayList.get(i).toUpperCase());

            holder.radioButton.setChecked(i == Integer.parseInt(RGBSettingContainerActivity.rgbObject.getFunction_name()));

            holder.radioButton.setTag(i);

            holder.label.setTag(i);

            holder.radioButton.setOnClickListener(new View.OnClickListener() {
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

        RGBFunctionFragment.selectedPosition = (Integer) v.getTag();

        RGBSettingContainerActivity.rgbObject.setFunction_name("" + RGBFunctionFragment.selectedPosition);

        if (RGBSettingContainerActivity.from.equalsIgnoreCase(UtilityConstants.ROOM) || RGBSettingContainerActivity.from.equalsIgnoreCase(UtilityConstants.FAV)) {

            RGBSettingContainerActivity.rgbObject.setMode(UtilityConstants.RGB_MODE_FUNCTION);

            RGBSettingContainerActivity.rgbObject.setState(UtilityConstants.STATE_TRUE);

            RGBSettingContainerActivity.WriteFlag = true;

            MqttOperation.setAutomationData(RGBSettingContainerActivity.rgbObject.getAutomationData());

            //MqttOperation.writePOINT(RGBSettingContainerActivity.rgbObject.getFilename(), new Gson().toJson(RGBSettingContainerActivity.rgbObject));

        }

        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private final TextView label;

        private final RadioButton radioButton;

        RecyclerViewHolder(View view) {

            super(view);

            label = view.findViewById(R.id.label);

            radioButton = view.findViewById(R.id.radio_button);

        }

    }

}
