package com.wify.smart.home.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.mqtt.MqttClient;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.List;

public class FavouritesScenesAdapter extends RecyclerView.Adapter<FavouritesScenesAdapter.RecyclerViewHolder> {

    private final Context context;

    private List<SceneObject> favourites_scenes;

    public FavouritesScenesAdapter(Context context, List<SceneObject> favourites_scenes) {

        this.context = context;

        this.favourites_scenes = favourites_scenes;
    }

    @Override
    public FavouritesScenesAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favourites_scenes_adapter, viewGroup, false);

        return new FavouritesScenesAdapter.RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FavouritesScenesAdapter.RecyclerViewHolder holder, final int i) {

        try {

            SceneObject sceneObject = favourites_scenes.get(i);

            holder.scene_name.setText(sceneObject.getName());

            holder.scene_logo.setImageDrawable(context.getResources().getDrawable(Utility.getSceneIcon(sceneObject.getLogo()).getDisable_icon()));

            holder.scene_linear_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    playScene(sceneObject.getFile());

                    Utility.vibrate();

                    view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void playScene(String filename) {

        try {

            MqttClient.publishMessage(MqttClient.HOMEUID, MqttOperation.getTransactionObject(UtilityConstants.PLAY_SCENE, filename));

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    @Override
    public int getItemCount() {
        return (null != favourites_scenes ? favourites_scenes.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView scene_name;

        LinearLayout scene_linear_layout;

        ImageView scene_logo;

        RecyclerViewHolder(View view) {
            super(view);

            scene_name = view.findViewById(R.id.scene_name);

            scene_logo = view.findViewById(R.id.scene_logo);

            scene_linear_layout = view.findViewById(R.id.scene_linear_layout);

        }
    }

}
