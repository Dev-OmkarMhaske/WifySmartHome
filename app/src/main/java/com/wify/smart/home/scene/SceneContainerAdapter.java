package com.wify.smart.home.scene;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;
import java.util.List;

public class SceneContainerAdapter extends RecyclerView.Adapter<SceneContainerAdapter.RecyclerViewHolder> {

    private final Context context;
    private List<SceneObject> scenes;

    public SceneContainerAdapter(Context context, List<SceneObject> scenes) {
        this.context = context;
        this.scenes = scenes;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scene_list_container_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            SceneObject sceneObject = scenes.get(i);

            holder.scene_name.setText(sceneObject.getName());

            holder.view_scene.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, AddSceneActivity.class);

                    i.putExtra(UtilityConstants.SCENE,sceneObject);

                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(i);
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

        ImageView view_scene;

        RecyclerViewHolder(View view) {
            super(view);

            scene_name = view.findViewById(R.id.scene_name);

            view_scene = view.findViewById(R.id.view_scene);

        }

    }

}
