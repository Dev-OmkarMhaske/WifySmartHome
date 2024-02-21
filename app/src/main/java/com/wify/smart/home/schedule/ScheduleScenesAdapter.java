package com.wify.smart.home.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.utils.Utility;

import java.util.List;

public class ScheduleScenesAdapter extends RecyclerView.Adapter<ScheduleScenesAdapter.RecyclerViewHolder> {

    private final Context context;
    private List<SceneObject> scenes;

    public ScheduleScenesAdapter(Context context, List<SceneObject> scenes) {
        this.context = context;
        this.scenes = scenes;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.motion_view_scene_adapter, viewGroup, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            SceneObject sceneObject = scenes.get(i);

            holder.scene_title.setText(sceneObject.getName());

            holder.scene_logo.setImageDrawable(context.getResources().getDrawable(Utility.getSceneIcon(sceneObject.getLogo()).getDisable_icon()));

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != scenes ? scenes.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView scene_title;

        ImageView scene_delete, scene_logo;

        RecyclerViewHolder(View view) {
            super(view);

            scene_title = view.findViewById(R.id.scene_title);

            scene_delete = view.findViewById(R.id.scene_delete);

            scene_logo = view.findViewById(R.id.scene_logo);

        }

    }

}