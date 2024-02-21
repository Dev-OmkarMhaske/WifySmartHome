package com.wify.smart.home.scene;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.helper.ShowRoomViseDeviceListAdapter;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AddSceneAccessoriesActivity extends AppCompatActivity {

    RecyclerView accessories_recycler;

    LinkedList<Object> room_accessories = new LinkedList<>();

    LinearLayoutManager linearLayoutManager = null;

    List<RoomObject> roomObjectList = null;

    ShowRoomViseDeviceListAdapter roomviseApplianceListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.add_scene_accessories_activity);

            accessories_recycler = findViewById(R.id.accessories_recycler);

            accessories_recycler.setHasFixedSize(true);

            for (RoomObject roomObject : Utility.ROOMMap.values()) {

                room_accessories.add(roomObject);

                getRoomViseAccessories(roomObject);

            }

            linearLayoutManager = new LinearLayoutManager(getApplicationContext());

            accessories_recycler.setLayoutManager(linearLayoutManager);

            roomObjectList = new ArrayList<>();

            for (RoomObject roomObject : Utility.ROOMMap.values()) {

                roomObjectList.add(roomObject);

            }

            roomviseApplianceListAdapter = new ShowRoomViseDeviceListAdapter(getApplicationContext(), roomObjectList, UtilityConstants.ADD_ACC_IN_SCENE);

            accessories_recycler.setAdapter(roomviseApplianceListAdapter);

            findViewById(R.id.done_text).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getApplicationContext(), AddSceneActivity.class);

                    intent.putExtra(UtilityConstants.SCENE, AddSceneActivity.sceneObject);

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

    public void getRoomViseAccessories(RoomObject roomObject) {

        try {

            if (roomObject != null && roomObject.getMac() != null) {

                for (FanObject fanObject : Utility.fanObjectHashMap.values()) {

                    if (roomObject.getMac().contains(fanObject.getMac())) {

                        room_accessories.add(fanObject);

                    }

                }

                for (PowerObject powerObject : Utility.powerObjectHashMap.values()) {

                    if (roomObject.getMac().contains(powerObject.getMac())) {

                        room_accessories.add(powerObject);

                    }

                }

                for (CurtainObject curtainObject : Utility.curtainObjectHashMap.values()) {

                    if (roomObject.getMac().contains(curtainObject.getMac())) {

                        room_accessories.add(curtainObject);

                    }

                }


                for (RGBObject rgbObject : Utility.rgbObjectHashMap.values()) {

                    if (roomObject.getMac().contains(rgbObject.getMac())) {

                        room_accessories.add(rgbObject);

                    }

                }

                for (GenericObject genericObject : Utility.genericObjectHashMap.values()) {

                    if (roomObject.getMac().contains(genericObject.getMac())) {

                        room_accessories.add(genericObject);

                    }

                }

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}
