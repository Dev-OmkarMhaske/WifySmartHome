package com.wify.smart.home.schedule;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dpro.widgets.WeekdaysPicker;
import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.dto.ScheduleObject;
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

public class AddScheduleActivity extends AppCompatActivity {

    public static AddScheduleActivity addScheduleActivity;

    public static WeekdaysPicker weekdaysPicker;

    public static ScheduleObject scheduleObject = null;

    public static TreeSet<String> scheduleAccessoriesSet = null;

    public static RecyclerView schedule_scenes_recycler, schedules_accessories_recycler;

    public static List<RoomObject> roomObjects;

    GridLayoutManager gridLayoutManager = null;

    ScheduleScenesAdapter scheduleScenesAdapter = null;

    LinearLayoutManager linearLayoutManager = null;

    List<Integer> days = null;

    EditText add_time;

    EditText schedule_name;

    Switch schedule_enable;

    TextView save;

    private int mHour, mMinute;

    public static void updateScheduleData(String updatedata, String action) {

        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        try {

            if (updatedata.contains(UtilityConstants.CGM) || updatedata.contains(UtilityConstants.CFM)) {

                AddScheduleActivity.prepareControllerString(updatedata, action);

            } else {

                if (scheduleObject != null && scheduleObject.getData() != null && scheduleObject.getData().length() > 0) {

                    for (String s : scheduleObject.getData().trim().split(",")) {

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

                scheduleObject.setData(map.values().toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "").trim());

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void updateScheduleSceneData(String updatedata, String action) {

        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        try {

            if (scheduleObject != null && scheduleObject.getScene_ids().length() > 0) {

                for (String s : scheduleObject.getScene_ids().split("#")) {

                    map.put(s.trim(), s.trim());

                }

            }
            if (action.equals(UtilityConstants.ADD)) {

                String update[] = updatedata.split("#");

                map.put(new StringBuilder(update[0]).append("#").toString().trim(), updatedata.trim());

            }

            if (action.equals(UtilityConstants.DELETE)) {

                map.remove(new StringBuilder(updatedata).toString().trim());

            }

            scheduleObject.setScene_ids(map.values().toString().trim().replaceAll("\\[", "").replaceAll("\\]", "").trim().replaceAll(",", "#").replaceAll(" ", ""));

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void prepareControllerString(String data, String action) {

        HashMap<String, String> map = new HashMap<>();

        try {

            //              4033@1111111111@1010
            String TempData[] = scheduleObject.getController_data().split(",");

            for (String s : TempData) {

                if (s.trim().length() > 0) {

                    map.put(s.split("@")[0], s.substring(s.indexOf("@") + 1, s.length()));

                }

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

            scheduleObject.setController_data(stringBuilder.toString());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void SetAdapterData(Context context) {

        ShowRoomViseDeviceListAdapter roomviseApplianceListAdapter = null;

        try {

            roomObjects = new ArrayList<>();

            for (RoomObject roomObject : Utility.ROOMMap.values()) {

                if (roomObject != null) {

                    roomObjects.add(roomObject);
                }
            }

            roomviseApplianceListAdapter = new ShowRoomViseDeviceListAdapter(context, roomObjects, scheduleObject);

            schedules_accessories_recycler.setAdapter(roomviseApplianceListAdapter);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.add_schedule_activity);

            addScheduleActivity = this;

            scheduleAccessoriesSet = new TreeSet<>();

            save = findViewById(R.id.save);

            scheduleObject = (ScheduleObject) getIntent().getSerializableExtra(UtilityConstants.SCHEDULE);

            roomObjects = new ArrayList<>();

            schedule_name = findViewById(R.id.schedule_name);

            schedule_enable = findViewById(R.id.schedule_enable);

            add_time = findViewById(R.id.add_time);

            weekdaysPicker = findViewById(R.id.weekdays);

            LinkedHashMap<Integer, Boolean> map = new LinkedHashMap<>();
            map.put(1, false);
            map.put(2, false);
            map.put(3, false);
            map.put(4, false);
            map.put(5, false);
            map.put(6, false);
            map.put(7, false);

            weekdaysPicker.setSundayFirstDay(true);

            weekdaysPicker.setCustomDays(map);

            add_time.setFocusable(true);

            schedule_scenes_recycler = findViewById(R.id.schedule_scenes_recycler);

            schedule_scenes_recycler.setHasFixedSize(true);

            gridLayoutManager = new GridLayoutManager(getApplicationContext(), Utility.TILE_SPAN);

            schedule_scenes_recycler.setLayoutManager(gridLayoutManager);

            scheduleScenesAdapter = new ScheduleScenesAdapter(getApplicationContext(), getScheduleViseScenes());

            schedule_scenes_recycler.setAdapter(scheduleScenesAdapter);

            schedules_accessories_recycler = findViewById(R.id.schedules_accessories_recycler);

            schedules_accessories_recycler.setHasFixedSize(true);

            if (scheduleObject != null) {

                schedule_name.setText(scheduleObject.getName());

                schedule_enable.setChecked(scheduleObject.getActive().equalsIgnoreCase(UtilityConstants.STATE_TRUE));

                if (scheduleObject.getTime().length() > 0) {

                    String ScheduleSplit[] = scheduleObject.getTime().split(":");

                    boolean isPM = (Integer.parseInt(ScheduleSplit[0]) >= 12);

                    add_time.setText(String.format("%02d:%02d %s", (Integer.parseInt(ScheduleSplit[0]) == 12 || Integer.parseInt(ScheduleSplit[0]) == 0) ? 12 : Integer.parseInt(ScheduleSplit[0]) % 12, Integer.parseInt(ScheduleSplit[1]), isPM ? "PM" : "AM"));

                }

                days = new ArrayList<>();

                for (int i = 0; i < scheduleObject.getDays().length(); i++) {

                    String c = "" + scheduleObject.getDays().charAt(i);

                    days.add(Integer.parseInt(c) + 1);

                }

                weekdaysPicker.setSelectedDays(days);

                linearLayoutManager = new LinearLayoutManager(getApplicationContext());

                schedules_accessories_recycler.setLayoutManager(linearLayoutManager);

            } else {

                scheduleObject = new ScheduleObject();

                scheduleObject.setName("");

                scheduleObject.setDays("");

                scheduleObject.setActive("");

                scheduleObject.setFile(UtilityConstants.SCHEDULE + System.currentTimeMillis());

                scheduleObject.setScene_ids("");

                scheduleObject.setTime("");
            }

            add_time.setRawInputType(InputType.TYPE_NULL);

            SetAdapterData(getApplicationContext());

            findViewById(R.id.add_schedule_accessories).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    scheduleObject.setName(schedule_name.getText().toString());

                    scheduleObject.setActive(schedule_enable.isEnabled() == true ? UtilityConstants.STATE_TRUE : UtilityConstants.STATE_FALSE);

                    StringBuilder builder = new StringBuilder();

                    for (int i : weekdaysPicker.getSelectedDays()) {

                        builder.append(i - 1);

                    }

                    scheduleObject.setDays(builder.toString());

                    startActivity(new Intent(getApplicationContext(), AddScheduleAccessoriesActivity.class));
                }
            });

            add_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Calendar c = Calendar.getInstance();

                    mHour = c.get(Calendar.HOUR_OF_DAY);

                    mMinute = c.get(Calendar.MINUTE);

                    TimePickerDialog timePicker = new TimePickerDialog(AddScheduleActivity.this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view1, int hourOfDay,
                                              int minute) {

                            boolean isPM = (hourOfDay >= 12);

                            scheduleObject.setTime(String.format("%02d:%02d", hourOfDay, minute));

                            add_time.setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM"));

                        }

                    }, mHour, mMinute, false);

                    timePicker.show();

                }
            });

            findViewById(R.id.remove_schedule).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final EditText input = new EditText(AddScheduleActivity.this);

                    input.setHint(getString(R.string.confirm_password_txt));

                    input.setHintTextColor(getApplicationContext().getResources().getColor(R.color.black_transparent));

                    input.setTextColor(getApplicationContext().getResources().getColor(R.color.text_white));

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddScheduleActivity.this, R.style.AlertDialogCustom);
                    builder.setMessage(getString(R.string.confirm_delete_schedule))
                            .setTitle(getString(R.string.delete_schedule_txt))
                            .setView(input)
                            .setPositiveButton(UtilityConstants.CONFIRM_STR, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {

                                    if (UtilityConstants.ADMIN_PASSWORD.equalsIgnoreCase(input.getText().toString())) {

                                        MqttOperation.spiffsValueAction(UtilityConstants.DELETE, UtilityConstants.SCHEDULE, scheduleObject.getFile(), "");

                                        Toast.makeText(AddScheduleActivity.this, "Remove schedule", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                        intent.putExtra(UtilityConstants.FROM, UtilityConstants.SCHEDULE);

                                        startActivity(intent);

                                    } else {

                                        Toast.makeText(AddScheduleActivity.this, R.string.valid_password_err_txt, Toast.LENGTH_SHORT).show();

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

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (scheduleObject.getTime().trim().length() == 0) {

                        Toast.makeText(AddScheduleActivity.this, R.string.schedule_valid_time_txt, Toast.LENGTH_SHORT).show();

                        return;

                    } else if (schedule_name.getText().toString().trim().length() == 0) {

                        Toast.makeText(AddScheduleActivity.this, R.string.schedule_valid_name_txt, Toast.LENGTH_SHORT).show();

                        return;

                    } else if (scheduleObject.getTime().trim().length() > 0 && schedule_name.getText().toString().trim().length() > 0) {

                        scheduleObject.setName(schedule_name.getText().toString().trim());

                        StringBuilder builder = new StringBuilder();

                        for (int i : weekdaysPicker.getSelectedDays()) {

                            builder.append(i - 1);

                        }

                        scheduleObject.setDays(builder.toString());

                        scheduleObject.setActive(schedule_enable.isChecked() == true ? UtilityConstants.STATE_TRUE : UtilityConstants.STATE_FALSE);

                        scheduleObject.setFile(scheduleObject.getFile());

                        if (scheduleObject.getFile() == null || scheduleObject.getFile() != null && scheduleObject.getFile().trim().length() == 0) {

                            scheduleObject.setFile(UtilityConstants.SCHEDULE + System.currentTimeMillis());

                        }

                        scheduleObject.setLast("" + System.currentTimeMillis());

                        MqttOperation.spiffsValueAction(UtilityConstants.WRITE, UtilityConstants.SCHEDULE, scheduleObject.getFile(), new Gson().toJson(scheduleObject));

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        intent.putExtra(UtilityConstants.FROM, UtilityConstants.SCHEDULE);

                        startActivity(intent);

                    }
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public List<SceneObject> getScheduleViseScenes() {

        List<SceneObject> sceneObjectList = new ArrayList<>();

        try {

            if (scheduleObject != null && scheduleObject.getScene_ids().trim() != null) {

                String[] SCENE_IDS = scheduleObject.getScene_ids().replaceAll(" ", "").trim().split("#");

                for (int i = 0; i < SCENE_IDS.length; i++) {

                    if (Utility.SCENEMap.get(SCENE_IDS[i]) != null) {

                        sceneObjectList.add(Utility.SCENEMap.get(SCENE_IDS[i]));

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sceneObjectList;
    }

}
