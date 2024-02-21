package com.wify.smart.home.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.List;

public class AddSceneInScheduleAdapter extends RecyclerView.Adapter<AddSceneInScheduleAdapter.RecyclerViewHolder> {

    private final Context context;

    private List<SceneObject> scenes;

    public AddSceneInScheduleAdapter(Context context, List<SceneObject> scenes) {
        this.context = context;
        this.scenes = scenes;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_scene_in_schedule_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            SceneObject sceneObject = scenes.get(i);

            holder.scene_name.setText(sceneObject.getName());

            holder.scene_logo.setImageDrawable(context.getResources().getDrawable(Utility.getSceneIcon(sceneObject.getLogo()).getDisable_icon()));

            holder.scene_check.setChecked(AddScheduleActivity.scheduleObject.getScene_ids().contains(sceneObject.getFile()));

            holder.scene_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (holder.scene_check.isChecked()) {

                        AddScheduleActivity.updateScheduleSceneData(sceneObject.getFile(), UtilityConstants.ADD);

                    } else {

                        AddScheduleActivity.updateScheduleSceneData(sceneObject.getFile(), UtilityConstants.DELETE);

                    }
                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != scenes ? scenes.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {


        TextView scene_name;

        ImageView scene_logo;

        CheckBox scene_check;

        RecyclerViewHolder(View view) {
            super(view);

            scene_check = view.findViewById(R.id.scene_check);

            scene_name = view.findViewById(R.id.scene_name);

            scene_logo = view.findViewById(R.id.scene_logo);

        }

    }

}