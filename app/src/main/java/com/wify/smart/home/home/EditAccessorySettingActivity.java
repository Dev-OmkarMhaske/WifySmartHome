package com.wify.smart.home.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.Observable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.adapters.AccessoryListAdapter;
import com.wify.smart.home.dto.AccessoriesObject;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.MotionObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.mqtt.MqttClient;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.LinkedList;
import java.util.TreeSet;

public class EditAccessorySettingActivity extends AppCompatActivity {

    AccessoriesObject accessoriesObject = null;

    Button restart_module, reset_module;

    TextView accessories_name, accessory_state, back_text, save;

    RecyclerView accessory_recycler;

    AccessoryListAdapter accessoryListAdapter = null;

    LinearLayoutManager linearLayoutManager = null;

    LinkedList<Object> objectLinkedList = null;

    public static EditAccessorySettingActivity editAccessorySettingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_accessory_setting_activity);

        editAccessorySettingActivity = this;

        accessoriesObject = (AccessoriesObject) getIntent().getSerializableExtra(UtilityConstants.ACC_OBJ);

        reset_module = findViewById(R.id.reset_module);

        accessory_recycler = findViewById(R.id.accessory_recycler);

        restart_module = findViewById(R.id.restart_module);

        accessories_name = findViewById(R.id.accessories_name);

        accessory_state = findViewById(R.id.accessory_state);

        save = findViewById(R.id.save);

        back_text = findViewById(R.id.back_text);

        accessories_name.setText(accessories_name.getText().toString().concat(accessoriesObject.getAccessory()));

        accessory_state.setText(accessory_state.getText().toString().concat(accessoriesObject.getState()));

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        accessory_recycler.setLayoutManager(linearLayoutManager);

        accessoryListAdapter = new AccessoryListAdapter(getApplicationContext(), getAccessoryList());

        accessory_recycler.setAdapter(accessoryListAdapter);

        Utility.reload.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {

                accessoryListAdapter = new AccessoryListAdapter(getApplicationContext(), getAccessoryList());

                accessory_recycler.setAdapter(accessoryListAdapter);
            }
        });

        back_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        reset_module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText input = new EditText(EditAccessorySettingActivity.this);

                input.setHint(getString(R.string.confirm_password_txt));

                input.setHintTextColor(getApplicationContext().getResources().getColor(R.color.black_transparent));

                input.setTextColor(getApplicationContext().getResources().getColor(R.color.text_white));

                AlertDialog.Builder builder = new AlertDialog.Builder(EditAccessorySettingActivity.this, R.style.AlertDialogCustom);

                builder.setMessage("Are you sure you want to reset module ?")
                        .setTitle("Reset module")
                        .setView(input)
                        .setPositiveButton(UtilityConstants.CONFIRM_STR, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                try {

                                    if (UtilityConstants.ADMIN_PASSWORD.equalsIgnoreCase(input.getText().toString())) {

                                        MqttClient.publishMessage(MqttClient.HOMEUID, MqttOperation.getTransactionObject(UtilityConstants.RESET, accessoriesObject.getMac()));

                                        updateRoom(accessoriesObject.getMac());

                                        Thread.sleep(100);

                                        Utility.AccessoriesMap.remove(accessoriesObject.getReal_mac());

                                        MqttOperation.spiffsValueAction(UtilityConstants.WRITE, UtilityConstants.ACCESSORY, UtilityConstants.ACCESSORY, new Gson().toJson(Utility.AccessoriesMap));

                                        TreeSet<String> FileSet = new TreeSet<>();

                                        for (GenericObject genericObject : Utility.genericObjectHashMap.values()) {

                                            if (genericObject.getMac().equalsIgnoreCase(accessoriesObject.getMac())) {

                                                FileSet.add(genericObject.getFilename());
                                            }
                                        }

                                        for (FanObject fanObject : Utility.fanObjectHashMap.values()) {

                                            if (fanObject.getMac().equalsIgnoreCase(accessoriesObject.getMac()) || fanObject.getMac().contains(accessoriesObject.getMac())) {

                                                FileSet.add(fanObject.getFilename());
                                            }
                                        }

                                        for (MotionObject motionObject : Utility.motionObjectHashMap.values()) {

                                            if (motionObject.getMac().equalsIgnoreCase(accessoriesObject.getMac())) {

                                                FileSet.add(motionObject.getFilename());
                                            }
                                        }

                                        for (RGBObject rgbObject : Utility.rgbObjectHashMap.values()) {

                                            if (rgbObject.getMac().equalsIgnoreCase(accessoriesObject.getMac())) {

                                                FileSet.add(rgbObject.getFilename());
                                            }
                                        }

                                        for (PowerObject powerObject : Utility.powerObjectHashMap.values()) {

                                            if (powerObject.getMac().equalsIgnoreCase(accessoriesObject.getMac())) {

                                                FileSet.add(powerObject.getFilename());
                                            }
                                        }

                                        for (CurtainObject curtainObject : Utility.curtainObjectHashMap.values()) {

                                            if (curtainObject.getMac().equalsIgnoreCase(accessoriesObject.getMac())) {

                                                FileSet.add(curtainObject.getFilename());
                                            }
                                        }

                                        for (String s : FileSet) {

                                            if (s.contains(UtilityConstants.MOTION)) {

                                                MqttOperation.spiffsValueAction(UtilityConstants.DELETE, UtilityConstants.MOTION_POINT, s, "");

                                            } else {

                                                MqttOperation.spiffsValueAction(UtilityConstants.DELETE, UtilityConstants.POINT, s, "");

                                            }

                                            Thread.sleep(100);

                                        }

                                    } else {

                                        Toast.makeText(EditAccessorySettingActivity.this, R.string.valid_password_err_txt, Toast.LENGTH_SHORT).show();

                                    }

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                    intent.putExtra(UtilityConstants.FROM, UtilityConstants.HOME);

                                    startActivity(intent);

                                } catch (Exception e) {

                                    e.printStackTrace();
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

            }
        });

        restart_module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText input = new EditText(EditAccessorySettingActivity.this);
               input.setHint(getString(R.string.confirm_password_txt));
                input.setHintTextColor(getApplicationContext().getResources().getColor(R.color.black_transparent));
                input.setTextColor(getApplicationContext().getResources().getColor(R.color.text_white));

                AlertDialog.Builder builder = new AlertDialog.Builder(EditAccessorySettingActivity.this, R.style.AlertDialogCustom);
                builder.setMessage("Are you sure you want to restart module ?")
                        .setTitle("Restart module")
                        .setView(input)
                        .setPositiveButton(UtilityConstants.CONFIRM_STR, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (UtilityConstants.ADMIN_PASSWORD.equalsIgnoreCase(input.getText().toString())) {


                                } else {

                                    Toast.makeText(EditAccessorySettingActivity.this, R.string.valid_password_err_txt, Toast.LENGTH_SHORT).show();

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

            }
        });

    }

    public LinkedList<Object> getAccessoryList() {

         objectLinkedList = new LinkedList<>();

        for (GenericObject genericObject : Utility.genericObjectHashMap.values()) {

            if (genericObject.getMac().equalsIgnoreCase(accessoriesObject.getMac())) {

                objectLinkedList.add(genericObject);
            }
        }

        for (FanObject fanObject : Utility.fanObjectHashMap.values()) {

            if (fanObject.getMac().contains(accessoriesObject.getMac()) || fanObject.getMac().equalsIgnoreCase(accessoriesObject.getMac())) {

                objectLinkedList.add(fanObject);
            }
        }

        for (MotionObject motionObject : Utility.motionObjectHashMap.values()) {

            if (motionObject.getMac().equalsIgnoreCase(accessoriesObject.getMac())) {

                objectLinkedList.add(motionObject);
            }
        }

        for (PowerObject powerObject : Utility.powerObjectHashMap.values()) {

            if (powerObject.getMac().equalsIgnoreCase(accessoriesObject.getMac())) {

                objectLinkedList.add(powerObject);
            }
        }

        for (RGBObject rgbObject : Utility.rgbObjectHashMap.values()) {

            if (rgbObject.getMac().equalsIgnoreCase(accessoriesObject.getMac())) {

                objectLinkedList.add(rgbObject);
            }
        }

        for (CurtainObject curtainObject : Utility.curtainObjectHashMap.values()) {

            if (curtainObject.getMac().equalsIgnoreCase(accessoriesObject.getMac())) {

                objectLinkedList.add(curtainObject);
            }
        }

        return objectLinkedList;
    }

    public static void updateRoom(String Mac) {

        for (RoomObject roomObject : Utility.ROOMMap.values()) {

            if (roomObject.getMac().contains(Mac)) {

                TreeSet<String> treeSet = new TreeSet<>();

                for (String s : roomObject.getMac().trim().split(",")) {

                    if (!s.equalsIgnoreCase(Mac)) {

                        if (treeSet.size() == 0) {

                            treeSet.add(s);
                        } else {

                            treeSet.add("," + s);
                        }
                    }
                }

                if (treeSet.size() > 0) {

                    roomObject.setMac(treeSet.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", ""));

                    MqttOperation.spiffsValueAction(UtilityConstants.WRITE, UtilityConstants.ROOM, roomObject.getFile(), new Gson().toJson(roomObject));

                    break;
                }

            }

        }
    }

}
