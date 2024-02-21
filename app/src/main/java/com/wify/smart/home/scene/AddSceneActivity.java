package com.wify.smart.home.scene;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.MotionObject;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.dto.ScheduleObject;
import com.wify.smart.home.helper.ShowRoomViseDeviceListAdapter;
import com.wify.smart.home.home.MainActivity;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class AddSceneActivity extends AppCompatActivity {

    public static RecyclerView scene_accessories_recycler;

    public static SceneObject sceneObject;

    public static TreeSet<String> addSceneAccessories;

    Switch scene_fav;

    TextView scene_name;

    ImageView scene_logo;

    LinearLayoutManager linearLayoutManager = null;

    public static void updateSceneData(String updatedata, String action) {

        LinkedHashMap<String, String> map = null;

        try {

            if (updatedata.contains(UtilityConstants.CGM) || updatedata.contains(UtilityConstants.CFM)) {

                prepareControllerString(updatedata, action);

            } else {

                map = new LinkedHashMap<>();

                if (sceneObject != null && sceneObject.getData().length() > 0) {

                    for (String s : sceneObject.getData().trim().split(",")) {

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

                sceneObject.setData(map.values().toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", ""));
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void setRoomViseAccessories(Context context) {

        ShowRoomViseDeviceListAdapter roomviseApplianceListAdapter = null;

        List<RoomObject> roomObjects = new ArrayList<>();

        try {

            for (RoomObject roomObject : Utility.ROOMMap.values()) {

                roomObjects.add(roomObject);

            }

            roomviseApplianceListAdapter = new ShowRoomViseDeviceListAdapter(context, roomObjects, sceneObject);

            scene_accessories_recycler.setAdapter(roomviseApplianceListAdapter);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void prepareControllerString(String data, String action) {

        HashMap<String, String> map = null;

        String TempData[] = null;

        GenericObject genericObject = null;

        FanObject fanObject = null;

        StringBuilder temp = null;

        try {

            map = new HashMap<>();

            TempData = sceneObject.getController_data().split(",");

            for (String s : TempData) {

                if (s.trim().length() > 0)

                    map.put(s.split("@")[0], s.substring(s.indexOf("@") + 1, s.length()));

            }

            String dataSplit[] = data.split("@");

            if (data.contains(UtilityConstants.CGM)) {

                genericObject = (GenericObject) Utility.genericObjectHashMap.get(dataSplit[0] + ":" + dataSplit[1]).clone();

                genericObject.setAutomationData(data);

                temp = new StringBuilder("-------@--");

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

                fanObject = (FanObject) Utility.fanObjectHashMap.get(dataSplit[1] + dataSplit[0]).clone();

                fanObject.setAutomationData(data);

                String FakeMac = fanObject.getMac().replaceAll("F1", "");

                temp = new StringBuilder("-------@--");

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

            sceneObject.setController_data(stringBuilder.toString());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.add_scene_activity);

            sceneObject = (SceneObject) getIntent().getSerializableExtra(UtilityConstants.SCENE);

            addSceneAccessories = new TreeSet<>();

            scene_fav = findViewById(R.id.scene_fav);

            scene_name = findViewById(R.id.scene_name);

            scene_name.setText(sceneObject.getName());

            scene_logo = findViewById(R.id.scene_logo);

            scene_logo.setImageDrawable(getApplicationContext().getResources().getDrawable(Utility.getSceneIcon(sceneObject.getLogo()).getDisable_icon()));

            if (sceneObject != null && sceneObject.getFav() != null) {

                scene_fav.setChecked(sceneObject.getFav().equalsIgnoreCase(UtilityConstants.STATE_TRUE));

            }

            scene_fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (scene_fav.isChecked()) {

                        sceneObject.setFav(UtilityConstants.STATE_TRUE);

                    } else {

                        sceneObject.setFav(UtilityConstants.STATE_FALSE);
                    }
                }
            });

            scene_accessories_recycler = findViewById(R.id.scene_accessories_recycler);

            linearLayoutManager = new LinearLayoutManager(getApplicationContext());

            scene_accessories_recycler.setLayoutManager(linearLayoutManager);

            setRoomViseAccessories(getApplicationContext());

            findViewById(R.id.remove_scene).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    StringBuilder stringBuilder = new StringBuilder();

                    String Msg = getString(R.string.confirm_delete_scene);

                    if (sceneObject.getFile().length() > 0) {

                        List<String> scheduleNames = getScheduleName(sceneObject.getFile());

                        List<String> MotionNames = getMotionNames(sceneObject.getFile());

                        if (scheduleNames.size() > 0) {

                            for (int i = 0; i < scheduleNames.size(); i++) {

                                if (i == 0) {

                                    stringBuilder = new StringBuilder("\"" + scheduleNames.get(i) + "\"");

                                } else {

                                    stringBuilder.append(",\"" + scheduleNames.get(i) + "\"");

                                }
                            }
                            Msg = Msg + getString(R.string.delete_scene_schedule) + stringBuilder.toString();
                        }

                        if (MotionNames.size() > 0) {

                            for (int i = 0; i < MotionNames.size(); i++) {

                                if (i == 0) {

                                    stringBuilder = new StringBuilder("\"" + MotionNames.get(i) + "\"");

                                } else {

                                    stringBuilder.append(",\"" + MotionNames.get(i) + "\"");

                                }

                            }
                            Msg = Msg + getString(R.string.delete_scene_motion) + stringBuilder.toString();
                        }
                    }

                    final EditText input = new EditText(AddSceneActivity.this);

                    input.setHint(getString(R.string.confirm_password_txt));

                    input.setHintTextColor(getApplicationContext().getResources().getColor(R.color.black_transparent));

                    input.setTextColor(getApplicationContext().getResources().getColor(R.color.text_white));

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddSceneActivity.this, R.style.AlertDialogCustom);

                    builder.setMessage(Msg)
                            .setTitle(getString(R.string.delete_scene))
                            .setView(input)
                            .setPositiveButton(UtilityConstants.CONFIRM_STR, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {

                                    if (UtilityConstants.ADMIN_PASSWORD.equalsIgnoreCase(input.getText().toString())) {

                                        if (sceneObject.getFile().length() > 0) {

                                            MqttOperation.spiffsValueAction(UtilityConstants.DELETE, UtilityConstants.SCENE, sceneObject.getFile(), "");

                                            Toast.makeText(AddSceneActivity.this, "Remove scene " + sceneObject.getName(), Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                            intent.putExtra(UtilityConstants.FROM, UtilityConstants.SCENE);

                                            startActivity(intent);
                                        }

                                    } else {

                                        Toast.makeText(AddSceneActivity.this, R.string.valid_password_err_txt, Toast.LENGTH_SHORT).show();
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

            findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        if (scene_name.getText().toString().length() > 0) {

                            sceneObject.setName(scene_name.getText().toString());

                            if (sceneObject.getFile() == null || sceneObject.getFile() != null && sceneObject.getFile().trim().length() == 0) {

                                sceneObject.setFile(UtilityConstants.SCENE + System.currentTimeMillis());

                            }
                            if (scene_fav.isChecked()) {

                                sceneObject.setFav(UtilityConstants.STATE_TRUE);

                            } else {

                                sceneObject.setFav(UtilityConstants.STATE_FALSE);
                            }

                            sceneObject.getData().trim();

                            sceneObject.setLast("" + System.currentTimeMillis());

                            MqttOperation.spiffsValueAction(UtilityConstants.WRITE, UtilityConstants.SCENE, sceneObject.getFile(), new Gson().toJson(sceneObject));

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            intent.putExtra(UtilityConstants.FROM, UtilityConstants.SCENE);

                            startActivity(intent);

                        } else {

                            Toast.makeText(AddSceneActivity.this, R.string.valid_scene_name_err, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.add_scene_accessories).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    sceneObject.setName(scene_name.getText().toString());

                    startActivity(new Intent(getApplicationContext(), AddSceneAccessoriesActivity.class));

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public List<String> getScheduleName(String scene_name) {

        List<String> schedulesNames = new ArrayList<>();

        try {

            for (ScheduleObject scheduleObject : Utility.SCHEDULEMap.values()) {

                if (scheduleObject != null) {

                    if (scheduleObject.getScene_ids().contains(scene_name)) {

                        schedulesNames.add(scheduleObject.getName());

                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return schedulesNames;
    }

    public List<String> getMotionNames(String sceneNames) {

        List<String> motionNames = new ArrayList<>();

        try {

            for (MotionObject motionObject : Utility.motionObjectHashMap.values()) {

                if (motionObject != null) {

                    if (motionObject.getScene_ids().contains(sceneNames)) {

                        motionNames.add(motionObject.getName());

                    }
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return motionNames;
    }
}
