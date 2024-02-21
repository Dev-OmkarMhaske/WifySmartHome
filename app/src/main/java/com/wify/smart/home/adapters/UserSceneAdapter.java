package com.wify.smart.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.dto.UserObject;
import com.wify.smart.home.home.SettingUserViewActivity;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.List;

public class UserSceneAdapter extends RecyclerView.Adapter<UserSceneAdapter.RecyclerViewHolder> {

    private final Context context;

    private List<SceneObject> scenes;

    UserObject current_user;

    public UserSceneAdapter(Context context, List<SceneObject> scenes, UserObject userObject) {

        this.context = context;

        this.scenes = scenes;

        this.current_user = userObject;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_vise_appliances_adapter_access, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            SceneObject sceneObject = scenes.get(i);

            holder.access_name.setText(sceneObject.getName());

            holder.accessories_check.setChecked(!current_user.getMac().contains(sceneObject.getFile()));

            holder.accessories_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.accessories_check.isChecked()) {

                        SettingUserViewActivity.updateUserMacData(sceneObject.getFile(), UtilityConstants.DELETE);

                    } else {

                        SettingUserViewActivity.updateUserMacData(sceneObject.getFile(), UtilityConstants.ADD);
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

        TextView access_name;

        CheckBox accessories_check;

        RecyclerViewHolder(View view) {
            super(view);

            access_name = view.findViewById(R.id.access_name);

            accessories_check = view.findViewById(R.id.accessories_check);

        }

    }
}
