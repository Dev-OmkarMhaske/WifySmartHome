package com.wify.smart.home.room;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.activities.ConnectAccessoriesActivity;
import com.wify.smart.home.mqtt.MqttClient;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;

public class AddAccessoriesBottomSheetAdapter extends RecyclerView.Adapter<AddAccessoriesBottomSheetAdapter.RecyclerViewHolder> {

    private final Context context;
    private ArrayList<String> accessories;


    public AddAccessoriesBottomSheetAdapter(Context context, ArrayList<String> accessories) {
        this.context = context;
        this.accessories = accessories;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_add_accessories_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            holder.accessories_text.setText(accessories.get(i));

            holder.accessories_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if ((SharedPreference.getRooms(context.getApplicationContext()) != null && SharedPreference.getRooms(context.getApplicationContext()).size() > 0) ||
                            Utility.ROOMMap.size() > 0) {

                       // MqttClient.publishMessage(MqttClient.HOMEUID, MqttOperation.getTransactionObject(UtilityConstants.INSTALLATION_START_STR, ""));

                        Intent i = new Intent(context, ConnectAccessoriesActivity.class);

                        i.putExtra(UtilityConstants.MODULE_TXT, holder.accessories_text.getText());

                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(i);

                    } else {

                        Toast.makeText(context, R.string.add_room_txt, Toast.LENGTH_SHORT).show();

                    }
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != accessories ? accessories.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView accessories_text;

        RecyclerViewHolder(View view) {
            super(view);

            accessories_text = view.findViewById(R.id.accessories_text);

        }

    }

}

