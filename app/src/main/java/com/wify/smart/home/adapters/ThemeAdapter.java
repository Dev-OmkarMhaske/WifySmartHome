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
import com.wify.smart.home.dto.WifyThemes;
import com.wify.smart.home.home.HomeSettingActivity;

import java.util.List;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.RecyclerViewHolder> {

    private final Context context;

    private List<WifyThemes> themes;

    public ThemeAdapter(Context context, List<WifyThemes> themes) {
        this.context = context;
        this.themes = themes;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.select_theme_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            holder.theme_name.setText(themes.get(i).getName());

            holder.theme_img.setImageResource(themes.get(i).getBackground_img());

            holder.theme_radio.setTag(i);

            holder.theme_radio.setChecked(i == HomeSettingActivity.selectedPosition);

            holder.theme_radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemCheckChanged(view);

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != themes ? themes.size() : 0);
    }

    private void itemCheckChanged(View v) {

        try {

            HomeSettingActivity.selectedPosition = (Integer) v.getTag();

            notifyDataSetChanged();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView theme_name;

        RadioButton theme_radio;

        ImageView theme_img;

        RecyclerViewHolder(View view) {
            super(view);

            theme_radio = view.findViewById(R.id.theme_radio);

            theme_name = view.findViewById(R.id.theme_name);

            theme_img = view.findViewById(R.id.theme_img);
        }

    }
}
