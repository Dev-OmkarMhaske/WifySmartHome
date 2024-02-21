package com.wify.smart.home.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.adapters.MotionSceneAdapter;
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.helper.ShowRoomViseDeviceListAdapter;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;
import java.util.List;

public class MotionAddAccessoriesActivity extends AppCompatActivity {

    RecyclerView scene_motion_recycler, accessories_motion_recycler;

    LinearLayoutManager linearLayoutManager = null;

    GridLayoutManager gridLayoutManager2 = null;

    ShowRoomViseDeviceListAdapter roomviseApplianceListAdapter = null;

    MotionSceneAdapter motionSceneAdapter = null;

    List<SceneObject> sceneObjectList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.motion_add_accessories_activity);

            scene_motion_recycler = findViewById(R.id.scene_motion_recycler);

            accessories_motion_recycler = findViewById(R.id.accessories_motion_recycler);

            scene_motion_recycler.setHasFixedSize(true);

            accessories_motion_recycler.setHasFixedSize(true);

            linearLayoutManager = new LinearLayoutManager(getApplicationContext());

            gridLayoutManager2 = new GridLayoutManager(getApplicationContext(), Utility.TILE_SPAN);

            accessories_motion_recycler.setLayoutManager(linearLayoutManager);

            scene_motion_recycler.setLayoutManager(gridLayoutManager2);

            roomviseApplianceListAdapter = new ShowRoomViseDeviceListAdapter(getApplicationContext(), Utility.getRoomList(), UtilityConstants.ADD_ACC_IN_MOTION);

            accessories_motion_recycler.setAdapter(roomviseApplianceListAdapter);

            sceneObjectList = new ArrayList<>();

            for (SceneObject sceneObject : Utility.SCENEMap.values()) {

                if (sceneObject != null) {

                    sceneObjectList.add(sceneObject);
                }
            }

            motionSceneAdapter = new MotionSceneAdapter(getApplicationContext(), sceneObjectList);

            scene_motion_recycler.setAdapter(motionSceneAdapter);

            findViewById(R.id.save_accessories).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getApplicationContext(), EditMotionActivity.class);

                    intent.putExtra(UtilityConstants.MOTION_OBJ, EditMotionActivity.motionObject);

                    startActivity(intent);
                }
            });

            findViewById(R.id.cancel_text).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onBackPressed();
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}