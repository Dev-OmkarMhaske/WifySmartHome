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
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.MotionObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.home.EditAccessorySettingActivity;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.LinkedList;

public class AccessoryListAdapter extends RecyclerView.Adapter<AccessoryListAdapter.RecyclerViewHolder> {

    private final Context context;

    LinkedList<Object> accessories;

    public AccessoryListAdapter(Context context, LinkedList<Object> accessories) {

        this.context = context;

        this.accessories = accessories;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.accessories_list_setting_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            Object object = accessories.get(i);

            if (object instanceof GenericObject) {

                GenericObject genericObject = (GenericObject) object;

                if (!genericObject.getLogo().equalsIgnoreCase("-")) {

                    holder.acc_logo.setImageResource(Utility.getAccessoryLogo(genericObject.getLogo()).getIcon());

                }

                holder.acc_name.setText(genericObject.getName());

                holder.acc_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (genericObject.getType().equalsIgnoreCase(UtilityConstants.CGM)) {

                            RemoveAccessory(genericObject.getFilename(), "");

                        } else {

                            RemoveAccessory(genericObject.getFilename(), genericObject.getMac());
                        }

                    }
                });

            } else if (object instanceof PowerObject) {

                PowerObject powerObject = (PowerObject) object;

                if (!powerObject.getLogo().equalsIgnoreCase("-")) {

                    holder.acc_logo.setImageResource(Utility.getAccessoryLogo(powerObject.getLogo()).getIcon());

                } else {

                    holder.acc_logo.setImageResource(R.drawable.icon_ac_d);
                }

                holder.acc_name.setText(powerObject.getName());

                holder.acc_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RemoveAccessory(powerObject.getFilename(), powerObject.getMac());
                    }
                });

            } else if (object instanceof MotionObject) {

                MotionObject motionObject = (MotionObject) object;

                holder.acc_logo.setImageResource(R.drawable.icon_motion_d);

                holder.acc_name.setText(motionObject.getName());

                holder.acc_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        RemoveAccessory(motionObject.getFilename(), motionObject.getMac());

                    }
                });

            } else if (object instanceof RGBObject) {

                RGBObject rgbObject = (RGBObject) object;

                holder.acc_logo.setImageResource(R.drawable.icon_rgb_d);

                holder.acc_name.setText(rgbObject.getName());

                holder.acc_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        RemoveAccessory(rgbObject.getFilename(), rgbObject.getMac());

                    }
                });


            } else if (object instanceof FanObject) {

                FanObject fanObject = (FanObject) object;

                holder.acc_logo.setImageResource(R.drawable.icon_fan_d);

                holder.acc_name.setText(fanObject.getName());

                holder.acc_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        RemoveAccessory(fanObject.getFilename(), fanObject.getMac());

                    }
                });

            } else if (object instanceof CurtainObject) {

                CurtainObject curtainObject = (CurtainObject) object;

                holder.acc_logo.setImageResource(R.drawable.icon_curtain_d);

                holder.acc_name.setText(curtainObject.getName());

                holder.acc_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RemoveAccessory(curtainObject.getFilename(), curtainObject.getMac());
                    }
                });
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void RemoveAccessory(String Filename, String Mac) {

        try {

            final EditText input = new EditText(context.getApplicationContext());

            input.setHint(context.getString(R.string.confirm_password_txt));

            input.setHintTextColor(context.getApplicationContext().getResources().getColor(R.color.black_transparent));

            input.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_white));

            AlertDialog.Builder builder = new AlertDialog.Builder(EditAccessorySettingActivity.editAccessorySettingActivity, R.style.AlertDialogCustom);

            builder.setMessage(context.getString(R.string.confirm_delete_module))
                    .setTitle(context.getString(R.string.delete_module_txt))
                    .setView(input)
                    .setPositiveButton(UtilityConstants.CONFIRM_STR, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            if (UtilityConstants.ADMIN_PASSWORD.equalsIgnoreCase(input.getText().toString())) {

                                MqttOperation.spiffsValueAction(UtilityConstants.DELETE, UtilityConstants.POINT, Filename, "");

                                EditAccessorySettingActivity.updateRoom(Mac);

                            } else {

                                Toast.makeText(context.getApplicationContext(), R.string.valid_password_err_txt, Toast.LENGTH_SHORT).show();

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
        return (null != accessories ? accessories.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView acc_logo, acc_delete;

        TextView acc_name;

        RecyclerViewHolder(View view) {
            super(view);

            acc_name = view.findViewById(R.id.acc_name);

            acc_logo = view.findViewById(R.id.acc_logo);

            acc_delete = view.findViewById(R.id.acc_delete);

        }

    }
}
