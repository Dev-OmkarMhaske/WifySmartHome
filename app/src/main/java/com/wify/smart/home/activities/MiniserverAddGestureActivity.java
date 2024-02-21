package com.wify.smart.home.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.adapters.MiniserverAddGestureAdapter;
import com.wify.smart.home.dto.GestureObject;
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.fragments.GestureSettingFragment;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class MiniserverAddGestureActivity extends AppCompatActivity {

    Spinner scenes_spinner;

    ArrayAdapter sceneDataAdapter;

    List<String> scene_data = new ArrayList<>();

    RecyclerView gesture_list_recycler;

    TextView back_text, Save;

    public static int selectedPosition = -1;

    public static String GestureName = null;

    List<GestureObject> gestureObjectArrayList = null;

    ListIterator<GestureObject> listIterator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.miniserver_add_gesture_activity);

            scenes_spinner = findViewById(R.id.scenes_spinner);

            selectedPosition = -1;

            GestureName = "";

            gesture_list_recycler = findViewById(R.id.gesture_list_recycler);

            back_text = findViewById(R.id.back_text);

            Save = findViewById(R.id.save);

            setView();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void setView() {

        try {

            scene_data = new ArrayList<>();

            Save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (scenes_spinner.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.select_scene_txt))) {

                        Toast.makeText(MiniserverAddGestureActivity.this, getString(R.string.select_scene_to_add_txt), Toast.LENGTH_SHORT).show();

                    } else if (selectedPosition != -1) {

                        GestureObject gestureObject = new GestureObject();

                        gestureObject.setName(GestureName);

                        gestureObject.setIcon(Utility.GestureList.get(selectedPosition).getIcon());

                        for (SceneObject sceneObject : Utility.SCENEMap.values()) {

                            if (sceneObject != null && sceneObject.getName().equalsIgnoreCase(scenes_spinner.getSelectedItem().toString())) {

                                gestureObject.setScene(sceneObject.getFile());

                            }
                        }

                        if (Utility.GestureMap.size() == 0) {

                            MqttOperation.preferenceValueAction(UtilityConstants.WRITE, UtilityConstants.GESTURE, Utility.PrePareGestureObject(gestureObject));

                        } else {

                            String Gdata = GestureSettingFragment.updateGestureData(Utility.PrePareGestureObject(gestureObject), UtilityConstants.ADD);

                            MqttOperation.preferenceValueAction(UtilityConstants.WRITE, UtilityConstants.GESTURE, Gdata.trim());

                        }

                        startActivity(new Intent(getApplicationContext(), WiFiGestureTabContainerActivity.class));

                    } else {

                        Toast.makeText(MiniserverAddGestureActivity.this, R.string.select_gesture_txt, Toast.LENGTH_SHORT).show();
                    }

                }
            });

            back_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onBackPressed();
                }
            });

            scene_data.add(getString(R.string.select_scene_txt));

            for (SceneObject sceneObject : Utility.SCENEMap.values()) {

                scene_data.add(sceneObject.getName());

                for (Map.Entry<String, GestureObject> entry : Utility.GestureMap.entrySet()) {

                    if (entry.getKey().contains(sceneObject.getFile())) {

                        scene_data.remove(sceneObject.getName());
                    }
                }

            }

            sceneDataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spinner_item, scene_data);

            sceneDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            scenes_spinner.setAdapter(sceneDataAdapter);

            gesture_list_recycler.setHasFixedSize(true);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

            gesture_list_recycler.setLayoutManager(linearLayoutManager);

            MiniserverAddGestureAdapter miniserverAddGestureAdapter = new MiniserverAddGestureAdapter(getApplicationContext(), getGestureList());

            gesture_list_recycler.setAdapter(miniserverAddGestureAdapter);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public List<GestureObject> getGestureList() {

        gestureObjectArrayList = new ArrayList<>();

        try {

            gestureObjectArrayList.addAll(Utility.GestureList);

            for (GestureObject gestureObject : Utility.GestureMap.values()) {

                listIterator = gestureObjectArrayList.listIterator();

                while (listIterator.hasNext()) {

                    if (listIterator.next().getName().equalsIgnoreCase(gestureObject.getName())) {

                        listIterator.remove();
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return gestureObjectArrayList;
    }
}