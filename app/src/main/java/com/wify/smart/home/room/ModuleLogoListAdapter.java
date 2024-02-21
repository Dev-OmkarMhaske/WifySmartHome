package com.wify.smart.home.room;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.AccessoriesIconsList;

import java.util.ArrayList;

public class ModuleLogoListAdapter extends RecyclerView.Adapter<ModuleLogoListAdapter.RecyclerViewHolder> {

    private final Context context;

    private ArrayList<AccessoriesIconsList> accessories;

    public ModuleLogoListAdapter(Context context, ArrayList<AccessoriesIconsList> accessories) {

        this.context = context;

        this.accessories = accessories;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_list_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            AccessoriesIconsList accessoriesIconsList = accessories.get(i);

            holder.module_name.setText(accessoriesIconsList.getName());

            holder.module_logo.setImageDrawable(context.getResources().getDrawable(accessoriesIconsList.getIcon()));

            holder.module_radio.setTag(i);

            holder.module_radio.setChecked(i == ChangeModuleLogoActivity.selectedPosition);

            holder.module_radio.setOnClickListener(new View.OnClickListener() {
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
        return (null != accessories ? accessories.size() : 0);
    }

    private void itemCheckChanged(View v) {

        ChangeModuleLogoActivity.selectedPosition = (Integer) v.getTag();

        notifyDataSetChanged();

    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView module_name;

        RadioButton module_radio;

        ImageView module_logo;

        RecyclerViewHolder(View view) {
            super(view);

            module_name = view.findViewById(R.id.room_name);

            module_radio = view.findViewById(R.id.room_radio);

            module_logo = view.findViewById(R.id.room_logo);

        }

    }

}
