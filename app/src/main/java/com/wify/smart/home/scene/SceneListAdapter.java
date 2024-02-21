package com.wify.smart.home.scene;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.SceneIconList;

import java.util.ArrayList;

public class SceneListAdapter extends RecyclerView.Adapter<SceneListAdapter.RecyclerViewHolder> {

    private final Context context;
    private ArrayList<SceneIconList> scenes;

    public SceneListAdapter(Context context, ArrayList<SceneIconList> scenes) {
        this.context = context;
        this.scenes = scenes;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scene_list_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            SceneIconList sceneIconList = scenes.get(i);

            holder.scene_name.setText(sceneIconList.getName());

            holder.selected_scene.setTag(i);

            holder.selected_scene.setChecked(i == ListSceneActivity.selectedPosition);

            holder.selected_scene.setOnClickListener(new View.OnClickListener() {
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

        try {

            ListSceneActivity.selectedPosition = (Integer) v.getTag();

            notifyDataSetChanged();

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

        RadioButton selected_scene;

        RecyclerViewHolder(View view) {
            super(view);

            selected_scene = view.findViewById(R.id.scene_select);

            scene_name = view.findViewById(R.id.scene_name);

        }

    }

}
