package com.wify.smart.home.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.helper.ShowRoomViseDeviceListAdapter;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;
import java.util.List;

public class AddScheduleAccessoriesActivity extends AppCompatActivity {

    RecyclerView schedule_add_scene_recycler, schedule_accessories_recycler;

    List<SceneObject> scenes;

    LinearLayoutManager linearLayoutManager = null;

    GridLayoutManager mGridLayoutManager2 = null;

    AddSceneInScheduleAdapter addSceneInScheduleAdapter = null;

    ShowRoomViseDeviceListAdapter roomviseApplianceListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            scenes = new ArrayList<>();

            setContentView(R.layout.add_schedule_accessories_activity);

            schedule_add_scene_recycler = findViewById(R.id.schedule_add_scene_recycler);

            schedule_accessories_recycler = findViewById(R.id.schedule_accessories_recycler);

            schedule_add_scene_recycler.setHasFixedSize(true);

            schedule_accessories_recycler.setHasFixedSize(true);

            linearLayoutManager = new LinearLayoutManager(getApplicationContext());

            schedule_accessories_recycler.setLayoutManager(linearLayoutManager);

            mGridLayoutManager2 = new GridLayoutManager(getApplicationContext(), Utility.TILE_SPAN);

            schedule_add_scene_recycler.setLayoutManager(mGridLayoutManager2);

            for (SceneObject sceneObject : Utility.SCENEMap.values()) {

                if (sceneObject != null) {

                    scenes.add(sceneObject);
                }

            }

            addSceneInScheduleAdapter = new AddSceneInScheduleAdapter(getApplicationContext(), scenes);

            schedule_add_scene_recycler.setAdapter(addSceneInScheduleAdapter);

            roomviseApplianceListAdapter = new ShowRoomViseDeviceListAdapter(getApplicationContext(), Utility.getRoomList(), "add_accessories_in_schedule");

            schedule_accessories_recycler.setAdapter(roomviseApplianceListAdapter);

            findViewById(R.id.save_text).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getApplicationContext(), AddScheduleActivity.class);

                    intent.putExtra(UtilityConstants.SCHEDULE, AddScheduleActivity.scheduleObject);

                    finish();

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
