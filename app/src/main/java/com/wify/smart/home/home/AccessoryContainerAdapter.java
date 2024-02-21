package com.wify.smart.home.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.AccessoriesObject;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.List;

public class AccessoryContainerAdapter extends RecyclerView.Adapter<AccessoryContainerAdapter.RecyclerViewHolder> {

    private final Context context;

    private List<AccessoriesObject> accessoriesObjects;

    public static int AccCnt = 0;

    public AccessoryContainerAdapter(Context context, List<AccessoriesObject> accessoriesObjects) {

        this.context = context;

        this.accessoriesObjects = accessoriesObjects;
    }

    @Override
    public AccessoryContainerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.accessories_container_adapter, viewGroup, false);

        return new AccessoryContainerAdapter.RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AccessoryContainerAdapter.RecyclerViewHolder holder, final int i) {

        try {

            AccCnt++;

            AccessoriesObject accessoriesObject = accessoriesObjects.get(i);

            holder.accessory_name.setText(holder.accessory_name.getText().toString().concat(accessoriesObject.getAccessory()).concat(" -" + AccCnt));

            holder.acc_date.setText(holder.acc_date.getText().toString().concat(accessoriesObject.getDate()));

            holder.accessory_mac.setText(holder.accessory_mac.getText().toString().concat(accessoriesObject.getMac()));

            holder.accessory_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, EditAccessorySettingActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    intent.putExtra(UtilityConstants.ACC_OBJ, accessoriesObject);

                    context.startActivity(intent);

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != accessoriesObjects ? accessoriesObjects.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView accessory_name, acc_date, accessory_mac;

        ImageView accessory_setting;

        RecyclerViewHolder(View view) {
            super(view);

            accessory_name = view.findViewById(R.id.accessory_name);

            acc_date = view.findViewById(R.id.acc_date);

            accessory_mac = view.findViewById(R.id.accessory_mac);

            accessory_setting = view.findViewById(R.id.accessory_setting);

        }
    }

}
