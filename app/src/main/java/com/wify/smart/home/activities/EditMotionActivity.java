package com.wify.smart.home.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.adapters.MotionViewSceneAdapter;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.MotionObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.helper.ShowRoomViseDeviceListAdapter;
import com.wify.smart.home.home.MainActivity;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class EditMotionActivity extends AppCompatActivity {

    public static EditMotionActivity editMotionActivity;

    public static MotionObject motionObject = null;

    public static TreeSet<String> motion_accessories = null;

    public EditText start_time, end_time, time_period, motion_name;

    RelativeLayout startTimeLayout, endTimeLayout;

    RadioGroup r_group;

    RadioButton always, periodic;

    RecyclerView motion_accessories_recycler, motion_scenes_recycler;

    Switch motion_enable;

    private int mHourStart, mMinuteStart, mHourEnd, mMinuteEnd;

    public static List<Object> getMotionAccessories(MotionObject motionObject, RoomObject roomObject) {

        List<Object> objectList = new ArrayList<>();

        try {

            if (motionObject.getController_data().trim().length() > 0) {

                //  4033@1111111111@101
                String[] ControllerData = motionObject.getController_data().split(",");

                for (int i = 0; i < ControllerData.length; i++) {

                    String split[] = ControllerData[i].split("@");

                    String Mac = split[0];

                    String GPs = split[1];

                    String FPs = split[2];

                    for (GenericObject genericObject1 : Utility.genericObjectHashMap.values()) {

                        GenericObject genericObject = (GenericObject) genericObject1.clone();

                        if (GPs.length() == 7 && genericObject.getMac().equalsIgnoreCase(Mac) && roomObject.getMac().contains(genericObject.getMac())) {

                            String gp = "" + GPs.charAt(Integer.parseInt(genericObject.getPoint()) - 1);

                            if (!gp.equalsIgnoreCase("-")) {

                                genericObject.setState("" + GPs.charAt(Integer.parseInt(genericObject.getPoint()) - 1));

                                objectList.add(genericObject);

                                EditMotionActivity.motion_accessories.add(genericObject.getMac() + ":" + genericObject.getPoint());

                            }
                        }

                    }

//            freeze@state@speed

                    for (FanObject fanObject1 : Utility.fanObjectHashMap.values()) {

                        FanObject fanObject = (FanObject) fanObject1.clone();

                        if (FPs.length() == 2 && fanObject.getMac().contains(Mac) && roomObject.getMac().contains(fanObject.getMac().substring(2, fanObject.getMac().length()))) {

                            if (fanObject.getPoint().equalsIgnoreCase("F1")) {

                                String fp = "" + FPs.charAt(0);

                                if (!fp.equalsIgnoreCase("-")) {

                                    fanObject.setState("" + FPs.charAt(0));

                                    fanObject.setSpeed("" + FPs.charAt(1));

                                    objectList.add(fanObject);

                                    EditMotionActivity.motion_accessories.add(fanObject.getMac());

                                }

                            }

                        }
                    }

                }
            }

            if (motionObject.getRoom_data().trim() != null && roomObject.getMac().trim().length() > 0) {

                String[] MOTION_DATA = motionObject.getRoom_data().trim().split(",");

                for (int i = 0; i < MOTION_DATA.length; i++) {

                    String moduleData = MOTION_DATA[i].trim();

                    String mac = moduleData.split("@")[0];

                    String type = Utility.getModuleTypeByFakeMAC(mac);

                    if (type.equalsIgnoreCase(UtilityConstants.FAN_MODULE)) {

                        String dataSplit[] = moduleData.split("@");

                        FanObject fanObject = (FanObject) Utility.fanObjectHashMap.get(dataSplit[0]).clone();

                        String M = "";

                        if (fanObject.getMac().contains("F")) {

                            M = fanObject.getMac().substring(2, fanObject.getMac().length() - 1);
                        }

                        if (roomObject.getMac().contains(fanObject.getMac()) || ((fanObject.getMac().contains("F") && roomObject.getMac().contains(M)))) {

                            fanObject.setAutomationData(moduleData);

                            objectList.add(fanObject);

                            EditMotionActivity.motion_accessories.add(fanObject.getMac());

                        }
                    } else if (type.equalsIgnoreCase(UtilityConstants.RGB_MODULE)) {

                        String dataSplit[] = moduleData.split("@");

                        RGBObject rgbObject = (RGBObject) Utility.rgbObjectHashMap.get(dataSplit[0]).clone();

                        if (roomObject.getMac().contains(rgbObject.getMac())) {

                            rgbObject.setAutomationDataByType(moduleData, rgbObject);

                            objectList.add(rgbObject);

                            EditMotionActivity.motion_accessories.add(rgbObject.getMac());

                        }

                    } else if (type.equalsIgnoreCase(UtilityConstants.CURTAIN_MODULE)) {

                        String dataSplit[] = moduleData.split("@");

                        CurtainObject curtainObject = (CurtainObject) Utility.curtainObjectHashMap.get(dataSplit[0]).clone();

                        if (roomObject.getMac().contains(curtainObject.getMac())) {

                            curtainObject.setAutomationData(moduleData);

                            curtainObject.setAutomationData(moduleData);

                            objectList.add(curtainObject);

                            EditMotionActivity.motion_accessories.add(curtainObject.getMac());

                        }

                    } else if (type.equalsIgnoreCase(UtilityConstants.POWER_MODULE)) {

                        String dataSplit[] = moduleData.split("@");

                        PowerObject powerObject = (PowerObject) Utility.powerObjectHashMap.get(dataSplit[0]).clone();

                        if (roomObject.getMac().contains(powerObject.getMac())) {

                            powerObject.setAutomationData(moduleData);

                            objectList.add(powerObject);

                            EditMotionActivity.motion_accessories.add(powerObject.getMac());

                        }

                    } else if (type.equalsIgnoreCase(UtilityConstants.GENERIC_MODULE)) {

                        String dataSplit[] = moduleData.split("@");

                        GenericObject genericObject = (GenericObject) Utility.genericObjectHashMap.get(dataSplit[0] + ":" + dataSplit[1]).clone();

                        if (roomObject.getMac().contains(genericObject.getMac())) {

                            genericObject.setAutomationData(moduleData);

                            objectList.add(genericObject);

                            EditMotionActivity.motion_accessories.add(genericObject.getMac() + ":" + genericObject.getPoint());

                        }

                    }

                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return objectList;
    }

    public static void updateMotionData(String updatedata, String action) {

        try {

            LinkedHashMap<String, String> map = new LinkedHashMap<>();

            if (updatedata.contains(UtilityConstants.CGM) || updatedata.contains(UtilityConstants.CFM)) {

                prepareControllerString(updatedata, action);

            } else {

                if (motionObject != null && motionObject.getRoom_data().length() > 0) {

                    for (String s : motionObject.getRoom_data().trim().split(",")) {

                        String split[] = s.split("@");

                        if (Utility.curtainObjectHashMap.containsKey(split[0])) {

                            map.put(new StringBuilder(split[0]).toString(), s.trim());

                        } else {
                            map.put(new StringBuilder(split[0]).append("@").append(split[1]).toString().trim(), s.trim());
                        }
                    }

                }

                if (action.equals(UtilityConstants.ADD)) {

                    String update[] = updatedata.split("@");

                    if (Utility.curtainObjectHashMap.containsKey(update[0])) {

                        map.put(new StringBuilder(update[0]).toString(), updatedata);

                    } else {
                        map.put(new StringBuilder(update[0]).append("@").append(update[1]).toString(), updatedata);
                    }
                }

                if (action.equals(UtilityConstants.DELETE)) {

                    String update[] = updatedata.split("@");

                    if (Utility.curtainObjectHashMap.containsKey(update[0])) {
                        map.remove(new StringBuilder(update[0]).toString());

                    } else {
                        map.remove(new StringBuilder(update[0]).append("@").append(update[1]).toString());
                    }
                }

                motionObject.setRoom_data(map.values().toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "").trim());

                System.out.println(">>>>> motion room data >>>" + motionObject.getRoom_data());
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public static void updateMotionSceneData(String updatedata, String action) {

        try {

            LinkedHashMap<String, String> map = new LinkedHashMap<>();

            if (motionObject != null && motionObject.getScene_ids().length() > 0) {

                for (String s : motionObject.getScene_ids().trim().split(",")) {

                    map.put(s.trim(), s.trim());

                }

            }

            if (action.equals(UtilityConstants.ADD)) {

                String update[] = updatedata.split(",");

                map.put(new StringBuilder(update[0]).append(",").toString(), updatedata);

            }

            if (action.equals(UtilityConstants.DELETE)) {

                map.remove(new StringBuilder(updatedata).toString());

            }

            motionObject.setScene_ids(map.values().toString().trim().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "").trim());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void prepareControllerString(String data, String action) {

        try {

            // 4033@1111111@10

            HashMap<String, String> map = new HashMap<>();

            String TempData[] = motionObject.getController_data().split(",");

            for (String s : TempData) {

                if (s.trim().length() > 0)
                    map.put(s.split("@")[0], s.substring(s.indexOf("@") + 1, s.length()));

            }

            String dataSplit[] = data.split("@");

            if (data.contains(UtilityConstants.CGM)) {

                GenericObject genericObject = (GenericObject) Utility.genericObjectHashMap.get(dataSplit[0] + ":" + dataSplit[1]).clone();

                genericObject.setAutomationData(data);

                StringBuilder temp = new StringBuilder("-------@--");

                if (map.containsKey(genericObject.getMac())) {

                    temp = new StringBuilder(map.get(genericObject.getMac()));
                }

                if (action.equalsIgnoreCase(UtilityConstants.ADD)) {

                    temp.setCharAt(Integer.parseInt(genericObject.getPoint()) - 1, genericObject.getState().charAt(0));

                } else {

                    temp.setCharAt(Integer.parseInt(genericObject.getPoint()) - 1, '-');

                }

                map.put(genericObject.getMac(), temp.toString());

            } else if (data.contains(UtilityConstants.CFM)) {

                FanObject fanObject = (FanObject) Utility.fanObjectHashMap.get(dataSplit[1] + dataSplit[0]).clone();

                fanObject.setAutomationData(data);

                String FakeMac = fanObject.getMac().replaceAll("F1", "");

                StringBuilder temp = new StringBuilder("-------@--");

                if (map.containsKey(FakeMac)) {

                    temp = new StringBuilder(map.get(FakeMac));

                }

                if (fanObject.getPoint().equalsIgnoreCase("F1")) {

                    if (action.equalsIgnoreCase(UtilityConstants.ADD)) {

                        temp.setCharAt(8, fanObject.getState().charAt(0));

                        temp.setCharAt(9, fanObject.getSpeed().charAt(0));

                    } else {

                        temp.setCharAt(8, '-');

                        temp.setCharAt(9, '-');

                    }

                }

                map.put(FakeMac, temp.toString());
            }

            StringBuilder stringBuilder = new StringBuilder("");

            for (Map.Entry<String, String> map2 : map.entrySet()) {

                if (stringBuilder.toString().length() == 0) {

                    stringBuilder.append(map2.getKey() + "@" + map2.getValue());

                } else {

                    stringBuilder.append("," + map2.getKey() + "@" + map2.getValue());

                }

            }

            motionObject.setController_data(stringBuilder.toString());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.edit_motion_activity);

            motion_accessories = new TreeSet<>();

            motionObject = (MotionObject) getIntent().getSerializableExtra(UtilityConstants.MOTION_OBJ);

            editMotionActivity = this;

            r_group = findViewById(R.id.r_group);

            time_period = findViewById(R.id.time_period);

            always = findViewById(R.id.always);

            motion_enable = findViewById(R.id.motion_enable);

            start_time = findViewById(R.id.start_time);

            end_time = findViewById(R.id.end_time);

            motion_name = findViewById(R.id.motion_name);

            periodic = findViewById(R.id.periodic);

            startTimeLayout = findViewById(R.id.startTimeLayout);

            endTimeLayout = findViewById(R.id.endTimeLayout);

            end_time.setRawInputType(InputType.TYPE_NULL);

            end_time.setFocusable(true);

            start_time.setRawInputType(InputType.TYPE_NULL);

            start_time.setFocusable(true);

            motion_scenes_recycler = findViewById(R.id.motion_scenes_recycler);

            motion_accessories_recycler = findViewById(R.id.motion_accessories);

            motion_accessories_recycler.setHasFixedSize(true);

            motion_scenes_recycler.setHasFixedSize(true);

            always.setChecked(motionObject.getActive_time().equalsIgnoreCase(UtilityConstants.MOTION_ALWAYS));

            periodic.setChecked(motionObject.getActive_time().equalsIgnoreCase(UtilityConstants.MOTION_PERIODIC));

            r_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    if (always.isChecked()) {

                        startTimeLayout.setVisibility(View.GONE);

                        endTimeLayout.setVisibility(View.GONE);

                        motionObject.setActive_time(UtilityConstants.MOTION_ALWAYS);

                    } else {

                        startTimeLayout.setVisibility(View.VISIBLE);

                        endTimeLayout.setVisibility(View.VISIBLE);

                        motionObject.setActive_time(UtilityConstants.MOTION_PERIODIC);

                    }
                }
            });

            motion_name.setText(motionObject.getName());

            motion_enable.setChecked(motionObject.getActive().equalsIgnoreCase(UtilityConstants.STATE_TRUE));

            if (always.isChecked()) {

                startTimeLayout.setVisibility(View.GONE);

                endTimeLayout.setVisibility(View.GONE);
            }

            start_time.setText(motionObject.getStart_time());

            end_time.setText(motionObject.getEnd_time());

            time_period.setText(motionObject.getPeriod());

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

            motion_accessories_recycler.setLayoutManager(linearLayoutManager);

            GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getApplicationContext(), Utility.TILE_SPAN);

            motion_scenes_recycler.setLayoutManager(gridLayoutManager2);

            List<SceneObject> sceneObjectList = new ArrayList<>();

            if (motionObject.getStart_time().length() > 0) {

                String TimeSplit[] = motionObject.getStart_time().split(":");

                boolean isPM = (Integer.parseInt(TimeSplit[0]) >= 12);

                start_time.setText(String.format("%02d:%02d %s", (Integer.parseInt(TimeSplit[0]) == 12 || Integer.parseInt(TimeSplit[0]) == 0) ? 12 : Integer.parseInt(TimeSplit[0]) % 12, Integer.parseInt(TimeSplit[1]), isPM ? "PM" : "AM"));

            }
            if (motionObject.getEnd_time().length() > 0) {

                String TimeSplit[] = motionObject.getEnd_time().split(":");

                boolean isPM = (Integer.parseInt(TimeSplit[0]) >= 12);

                end_time.setText(String.format("%02d:%02d %s", (Integer.parseInt(TimeSplit[0]) == 12 || Integer.parseInt(TimeSplit[0]) == 0) ? 12 : Integer.parseInt(TimeSplit[0]) % 12, Integer.parseInt(TimeSplit[1]), isPM ? "PM" : "AM"));
            }

            String[] MOTION_SCENES = motionObject.getScene_ids().split(",");

            for (int i = 0; i < MOTION_SCENES.length; i++) {

                for (SceneObject sceneObject : Utility.SCENEMap.values()) {

                    if (sceneObject != null) {

                        if (MOTION_SCENES[i].trim().equalsIgnoreCase(sceneObject.getFile())) {

                            sceneObjectList.add(sceneObject);
                        }
                    }
                }
            }

            MotionViewSceneAdapter motionViewSceneAdapter = new MotionViewSceneAdapter(getApplicationContext(), sceneObjectList);

            motion_scenes_recycler.setAdapter(motionViewSceneAdapter);

            ShowRoomViseDeviceListAdapter roomviseApplianceListAdapter = new ShowRoomViseDeviceListAdapter(getApplicationContext(), Utility.getRoomList(), motionObject);

            motion_accessories_recycler.setAdapter(roomviseApplianceListAdapter);

            start_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Calendar c = Calendar.getInstance();

                    mHourStart = c.get(Calendar.HOUR_OF_DAY);

                    mMinuteStart = c.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(EditMotionActivity.this, R.style.TimePickerTheme,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view1, int hourOfDay,
                                                      int minute) {

                                    boolean isPM = (hourOfDay >= 12);

                                    motionObject.setStart_time(String.format("%02d:%02d", hourOfDay, minute));

                                    start_time.setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));

                                }

                            }, mHourStart, mMinuteStart, false);

                    timePickerDialog.show();

                }
            });

            end_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Calendar c = Calendar.getInstance();

                    mHourEnd = c.get(Calendar.HOUR_OF_DAY);

                    mMinuteEnd = c.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(EditMotionActivity.this, R.style.TimePickerTheme,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view1, int hourOfDay,
                                                      int minute) {

                                    boolean isPM = (hourOfDay >= 12);

                                    motionObject.setEnd_time(String.format("%02d:%02d", hourOfDay, minute));

                                    end_time.setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));

                                }

                            }, mHourEnd, mMinuteEnd, false);

                    timePickerDialog.show();

                }
            });

            findViewById(R.id.add_motion_accessories).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    motionObject.setActive(motion_enable.isChecked() ? UtilityConstants.STATE_TRUE : UtilityConstants.STATE_FALSE);

                    motionObject.setName(motion_name.getText().toString());

                    startActivity(new Intent(getApplicationContext(), MotionAddAccessoriesActivity.class));
                }
            });

            findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onBackPressed();
                }
            });

            findViewById(R.id.save_motion).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (motionObject.getName().trim().length() > 0) {

                        motionObject.setName(motion_name.getText().toString());

                        motionObject.setActive(motion_enable.isChecked() ? UtilityConstants.STATE_TRUE : UtilityConstants.STATE_FALSE);

                        if (always.isChecked()) {

                            motionObject.setActive_time(UtilityConstants.MOTION_ALWAYS);

                        } else if (periodic.isChecked()) {

                            motionObject.setActive_time(UtilityConstants.MOTION_PERIODIC);
                        }

                        motionObject.setPeriod(time_period.getText().toString());

                        MqttOperation.setAutomationDataForMotion(UtilityConstants.MOTION_DATA, motionObject.getMac() + "@" + GetMData());

                        MqttOperation.setAutomationData(motionObject.getAutomationData());

                        MqttOperation.writeMotionPOINT(motionObject.getFilename(), new Gson().toJson(motionObject));

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        intent.putExtra(UtilityConstants.FROM, UtilityConstants.ROOM);

                        startActivity(intent);

                    } else {

                        Toast.makeText(EditMotionActivity.this, R.string.valid_motion_txt, Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public String GetMData() {

        StringBuilder builder = new StringBuilder();

        try {

            HashMap<String, String> map = new HashMap<>();

            if (motionObject.getRoom_data().trim().length() > 0) {

                String data[] = motionObject.getRoom_data().split(",");

                for (int i = 0; i < data.length; i++) {

                    String SubData[] = data[i].split("@");

                    if (map.containsKey(SubData[0])) {

                        map.put(SubData[0], map.get(SubData[0]) + "#" + SubData[1]);

                    } else {
                        map.put(SubData[0], SubData[1]);

                    }

                }

            }

            if (motionObject.getController_data().trim().length() > 0) {

                String controllerData[] = motionObject.getController_data().split(",");

                for (int i = 0; i < controllerData.length; i++) {

                    String SubcontrollerData[] = controllerData[i].split("@");

                    String Mac = SubcontrollerData[0];

                    String GPs = SubcontrollerData[1];

                    for (int j = 1; j <= 10; j++) {

                        if (GPs.charAt(j - 1) == '1') {

                            if (map.containsKey(Mac)) {

                                map.put(Mac, map.get(Mac) + "#" + j);

                            } else {

                                map.put(Mac, "" + j);

                            }

                        }

                    }

                }

            }

            for (Map.Entry<String, String> entry : map.entrySet()) {

                if (builder.toString().length() == 0) {

                    builder.append(entry.getKey() + "#" + entry.getValue());

                } else {

                    builder.append("," + entry.getKey() + "#" + entry.getValue());

                }

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return builder.toString();
    }
}
