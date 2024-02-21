package com.wify.smart.home.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.activities.EditMotionActivity;
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.List;

public class MotionViewSceneAdapter extends RecyclerView.Adapter<MotionViewSceneAdapter.RecyclerViewHolder> {

    private final Context context;

    private List<SceneObject> scenes;

    public MotionViewSceneAdapter(Context context, List<SceneObject> scenes) {
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

            holder.scene_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ConfirmDelete();
                }
            });


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void ConfirmDelete() {

        try {

            final EditText input = new EditText(EditMotionActivity.editMotionActivity);
            input.setHint(context.getString(R.string.confirm_password_txt));
            input.setHintTextColor(EditMotionActivity.editMotionActivity.getResources().getColor(R.color.black_transparent));
            input.setTextColor(EditMotionActivity.editMotionActivity.getResources().getColor(R.color.text_white));


            AlertDialog.Builder builder = new AlertDialog.Builder(EditMotionActivity.editMotionActivity, R.style.AlertDialogCustom);
            builder.setMessage(context.getString(R.string.confirm_delete_scene))
                    .setTitle(context.getString(R.string.delete_scene))
                    .setView(input)
                    .setPositiveButton(UtilityConstants.CONFIRM_STR, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (UtilityConstants.ADMIN_PASSWORD.equalsIgnoreCase(input.getText().toString())) {

                                Toast.makeText(EditMotionActivity.editMotionActivity, R.string.remove_scene, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(EditMotionActivity.editMotionActivity, R.string.valid_password_err_txt, Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .setNegativeButton(R.string.cancel_string, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // CANCEL
                        }
                    });

            builder.create();

            builder.show();

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
