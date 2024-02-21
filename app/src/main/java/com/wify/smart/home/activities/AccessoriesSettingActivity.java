package com.wify.smart.home.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.adapters.ModuleSettingAdapter;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.MotionObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.home.MainActivity;
import com.wify.smart.home.mqtt.MqttClient;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;

public class AccessoriesSettingActivity extends AppCompatActivity {

    public static String RoomName;

    public static Object object;

    public static RecyclerView module_vise_accessories_recycler;

    public static ModuleSettingAdapter moduleSettingAdapter;

    ArrayAdapter roomDataAdapter;

    Spinner room_drop_down_spinner;

    TextView room_text, module_text, led_cnt_txt;

    EditText led_cnt;

    ArrayList<String> roomList = null;

    LinearLayoutManager linearLayoutManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.room_accessories_setting_activity);

            object = (Object) getIntent().getSerializableExtra(UtilityConstants.ACCESSORY);

            RoomName = getIntent().getStringExtra(UtilityConstants.ROOM_NAME);

            led_cnt = findViewById(R.id.led_cnt);

            led_cnt_txt = findViewById(R.id.led_cnt_txt);

            room_text = findViewById(R.id.room_text);

            module_text = findViewById(R.id.module_text);

            room_text.setText(RoomName);

            room_drop_down_spinner = findViewById(R.id.room_drop_down);

            module_vise_accessories_recycler = findViewById(R.id.module_vise_accessories_recycler);

            linearLayoutManager = new LinearLayoutManager(getApplicationContext());

            module_vise_accessories_recycler.setLayoutManager(linearLayoutManager);

            if (object instanceof GenericObject) {

                module_text.setText(UtilityConstants.GENERIC_MODULE);

            } else if (object instanceof PowerObject) {

                module_text.setText(UtilityConstants.POWER_MODULE);

            } else if (object instanceof MotionObject) {

                module_text.setText(UtilityConstants.MOTION_MODULE);

            } else if (object instanceof RGBObject) {

                module_text.setText(UtilityConstants.RGB_MODULE);

                RGBObject rgbObject = (RGBObject) object;

                if (rgbObject.getType().equalsIgnoreCase("1")) {

                    led_cnt.setVisibility(View.VISIBLE);

                    led_cnt_txt.setVisibility(View.VISIBLE);

                    led_cnt.setText(rgbObject.getLed_count());

                }

            } else if (object instanceof FanObject) {

                module_text.setText(UtilityConstants.FAN_MODULE);

            } else if (object instanceof CurtainObject) {

                module_text.setText(UtilityConstants.CURTAIN_MODULE);

            }

            roomList = new ArrayList<>();

            roomList.add(getString(R.string.select_room_txt));

            for (RoomObject roomObject : Utility.ROOMMap.values()) {

                if (roomObject != null) {

                    roomList.add(roomObject.getName());

                }

            }

            roomDataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spinner_item, roomList);

            roomDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            room_drop_down_spinner.setAdapter(roomDataAdapter);

            room_drop_down_spinner.setSelection(roomDataAdapter.getPosition(RoomName));

            moduleSettingAdapter = new ModuleSettingAdapter(getApplicationContext(), object);

            module_vise_accessories_recycler.setAdapter(moduleSettingAdapter);

            findViewById(R.id.back_text).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onBackPressed();

                }
            });

            findViewById(R.id.save_accessories).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!room_drop_down_spinner.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.select_room_txt))) {

                        String room_name = room_drop_down_spinner.getSelectedItem().toString();

                        if (ModuleSettingAdapter.AccessoryName != null && ModuleSettingAdapter.AccessoryName.length() > 0) {

                            if (object instanceof GenericObject) {

                                GenericObject genericObject = (GenericObject) object;

                                genericObject.setName(ModuleSettingAdapter.AccessoryName);

                                genericObject.setState(UtilityConstants.STATE_FALSE);

                                // MqttOperation.setAutomationData(genericObject.getAutomationData());

                                MqttOperation.writePOINT(genericObject.getFilename(), new Gson().toJson(genericObject));

                                updateRooms(genericObject, room_name);

                            } else if (object instanceof RGBObject) {

                                RGBObject rgbObject = (RGBObject) object;

                                if (led_cnt.getText().toString().length() == 0) {

                                    Toast.makeText(AccessoriesSettingActivity.this, R.string.validate_led_cnt_txt, Toast.LENGTH_SHORT).show();

                                    return;
                                }

                                rgbObject.setName(ModuleSettingAdapter.AccessoryName);

                                rgbObject.setState(UtilityConstants.STATE_FALSE);

                                if (!rgbObject.getLed_count().equalsIgnoreCase(led_cnt.getText().toString().trim())) {

                                    rgbObject.setLed_count(led_cnt.getText().toString().trim());

                                    MqttClient.publishMessage(MqttClient.HOMEUID, MqttOperation.getTransactionObject(UtilityConstants.LED_COUNT_TXT, rgbObject.getMac() + "@" + led_cnt.getText().toString()));

                                }

//                            MqttOperation.setAutomationData(rgbObject.getAutomationData());

                                MqttOperation.writePOINT(rgbObject.getFilename(), new Gson().toJson(rgbObject));

                                Utility.rgbObjectHashMap.put(rgbObject.getMac(), rgbObject);

                                updateRooms(rgbObject, room_name);

                            } else if (object instanceof PowerObject) {

                                PowerObject powerObject = (PowerObject) object;

                                powerObject.setName(ModuleSettingAdapter.AccessoryName);

                                powerObject.setState(UtilityConstants.STATE_FALSE);

                                // MqttOperation.setAutomationData(powerObject.getAutomationData());

                                MqttOperation.writePOINT(powerObject.getFilename(), new Gson().toJson(powerObject));

                                updateRooms(powerObject, room_name);

                            } else if (object instanceof MotionObject) {

                                MotionObject motionObject = (MotionObject) object;

                                motionObject.setName(ModuleSettingAdapter.AccessoryName);

                                MqttOperation.writeMotionPOINT(motionObject.getFilename(), new Gson().toJson(motionObject));

                                updateRooms(motionObject, room_name);

                            } else if (object instanceof CurtainObject) {

                                CurtainObject curtainObject = (CurtainObject) object;

                                curtainObject.setName(ModuleSettingAdapter.AccessoryName);

                                curtainObject.setState(UtilityConstants.STATE_FALSE);

                                //   MqttOperation.setAutomationData(curtainObject.getAutomationData());

                                MqttOperation.writePOINT(curtainObject.getFilename(), new Gson().toJson(curtainObject));

                                updateRooms(curtainObject, room_name);

                            } else if (object instanceof FanObject) {

                                FanObject fanObject = (FanObject) object;

                                fanObject.setName(ModuleSettingAdapter.AccessoryName);

                                fanObject.setState(UtilityConstants.STATE_FALSE);

                                fanObject.setSpeed(UtilityConstants.STATE_FALSE);

                                //  MqttOperation.setAutomationData(fanObject.getAutomationData());

                                MqttOperation.writePOINT(fanObject.getFilename(), new Gson().toJson(fanObject));

                                updateRooms(fanObject, room_name);

                            }

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            intent.putExtra(UtilityConstants.FROM, UtilityConstants.ROOM);

                            startActivity(intent);

                        } else {

                            Toast.makeText(AccessoriesSettingActivity.this, R.string.validate_acc_name_txt, Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        Toast.makeText(AccessoriesSettingActivity.this, R.string.validate_room_txt, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void updateRooms(Object UpdatedObject, String Room_name) {

        String Mac = null;

        if (UpdatedObject instanceof GenericObject) {

            GenericObject genericObject = (GenericObject) UpdatedObject;

            Mac = genericObject.getMac();

        } else if (UpdatedObject instanceof PowerObject) {

            PowerObject powerObject = (PowerObject) UpdatedObject;

            Mac = powerObject.getMac();

        } else if (UpdatedObject instanceof RGBObject) {

            RGBObject rgbObject = (RGBObject) UpdatedObject;

            Mac = rgbObject.getMac();

        } else if (UpdatedObject instanceof CurtainObject) {

            CurtainObject curtainObject = (CurtainObject) UpdatedObject;

            Mac = curtainObject.getMac();

        } else if (UpdatedObject instanceof FanObject) {

            FanObject fanObject = (FanObject) UpdatedObject;

            if(fanObject.getType().equalsIgnoreCase(UtilityConstants.CFM)){

                Mac = fanObject.getMac().replaceAll("F1","");

            }else {

                Mac = fanObject.getMac();
            }

        } else if (UpdatedObject instanceof MotionObject) {

            MotionObject motionObject = (MotionObject) UpdatedObject;

            Mac = motionObject.getMac();

        }

        if (Mac != null) {

            for (RoomObject roomObject : Utility.ROOMMap.values()) {

                if (roomObject.getMac().contains(Mac)) {

                    roomObject.setMac(roomObject.getMac().replaceAll(Mac, ""));

                    MqttOperation.spiffsValueAction(UtilityConstants.WRITE, UtilityConstants.ROOM, roomObject.getFile(), new Gson().toJson(roomObject));

                }

                if (roomObject.getName().equalsIgnoreCase(Room_name)) {

                    if (roomObject.getMac().length() > 0) {

                        roomObject.setMac(roomObject.getMac() + "," + Mac);

                    } else {

                        roomObject.setMac(Mac);

                    }

                    MqttOperation.spiffsValueAction(UtilityConstants.WRITE, UtilityConstants.ROOM, roomObject.getFile(), new Gson().toJson(roomObject));
                }

            }
        }
    }

}
