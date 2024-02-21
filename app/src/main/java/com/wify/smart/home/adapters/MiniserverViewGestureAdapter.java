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
import com.wify.smart.home.dto.GestureObject;
import com.wify.smart.home.fragments.GestureSettingFragment;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;
import java.util.List;

public class MiniserverViewGestureAdapter extends RecyclerView.Adapter<MiniserverViewGestureAdapter.RecyclerViewHolder> {

    private final Context context;
    private List<GestureObject> gestureObjectList;

    public MiniserverViewGestureAdapter(Context context, List<GestureObject> gestureObjectList) {
        this.context = context;
        this.gestureObjectList = gestureObjectList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.miniserver_gesture_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            GestureObject gestureObject = gestureObjectList.get(i);

            holder.gesture_name.setText(gestureObject.getName());

            int logo = GetLogo(gestureObject.getName());

            if (Utility.SCENEMap.size() > 0 && Utility.SCENEMap.containsKey(gestureObject.getScene())) {

                holder.gesture_scene.setText("Scene : " + Utility.SCENEMap.get(gestureObject.getScene()).getName());

            }

            if (logo != 0) {

                holder.gesture_logo.setImageResource(logo);

            }

            holder.gesture_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ConfirmDelete(gestureObject);

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != gestureObjectList ? gestureObjectList.size() : 0);
    }

    public static int GetLogo(String name) {

        try {

            List<GestureObject> gestureObjectList = new ArrayList<GestureObject>() {
                {

                    add(new GestureObject("Right", "", R.drawable.icon_right_gesture));
                    add(new GestureObject("Left", "", R.drawable.icon_left_gesture));
                    add(new GestureObject("Right-Left", "", R.drawable.icon_right_left_gesture));
                    add(new GestureObject("Left-Right", "", R.drawable.icon_left_right_gesture));
                    add(new GestureObject("Up", "", R.drawable.icon_up_gesture));
                    add(new GestureObject("Down", "", R.drawable.icon_down_gesture));
                    add(new GestureObject("Up-Down", "", R.drawable.icon_up_down_gesture));
                    add(new GestureObject("Down-Up", "", R.drawable.icon_down_up_gesture));
                    add(new GestureObject("Forward", "", R.drawable.icon_forward_gesture));
                    add(new GestureObject("Backward", "", R.drawable.icon_backward_gesture));
                    add(new GestureObject("Forward-Backward", "", R.drawable.icon_forward_backward_gesture));
                    add(new GestureObject("Backward-Forward", "", R.drawable.icon_backward_forward_gesture));
                    add(new GestureObject("Clockwise", "", R.drawable.icon_clockwise_gesture));
                    add(new GestureObject("Anti-clockwise", "", R.drawable.icon_anticlockwise_gesture));
                    add(new GestureObject("Wave", "", R.drawable.icon_wave_gesture));

                }
            };

            for (GestureObject gestureObject : gestureObjectList) {

                if (gestureObject.getName().equalsIgnoreCase(name)) {

                    return gestureObject.getIcon();
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }

    private void ConfirmDelete(GestureObject gestureObject) {

        try {

            final EditText input = new EditText(context);

            input.setHint(context.getString(R.string.confirm_password_txt));

            input.setHintTextColor(context.getResources().getColor(R.color.black_transparent));

            input.setTextColor(context.getResources().getColor(R.color.text_white));

            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);

            builder.setMessage(context.getString(R.string.confirm_gesture_delete))
                    .setTitle(context.getString(R.string.delete_gesture_txt))
                    .setView(input)
                    .setPositiveButton(UtilityConstants.CONFIRM_STR, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            if (UtilityConstants.ADMIN_PASSWORD.equalsIgnoreCase(input.getText().toString())) {

                                String gesture_data = GestureSettingFragment.updateGestureData(Utility.PrePareGestureObject(gestureObject), UtilityConstants.DELETE);

                                Toast.makeText(context, "Remove Gesture " + gestureObject.getName(), Toast.LENGTH_SHORT).show();

                                MqttOperation.preferenceValueAction(UtilityConstants.WRITE, UtilityConstants.GESTURE, gesture_data.trim());

                            } else {

                                Toast.makeText(context, R.string.valid_password_err_txt, Toast.LENGTH_SHORT).show();
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

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView gesture_logo, gesture_delete;

        TextView gesture_name, gesture_scene;

        RecyclerViewHolder(View view) {

            super(view);

            gesture_logo = view.findViewById(R.id.gesture_logo);

            gesture_delete = view.findViewById(R.id.gesture_delete);

            gesture_name = view.findViewById(R.id.gesture_name);

            gesture_scene = view.findViewById(R.id.gesture_scene);
        }

    }
}
