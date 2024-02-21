package com.wify.smart.home.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.AccessoriesObject;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.helper.ShowRoomViseDeviceListAdapter;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;
import java.util.List;

public class AccessorySettingContainerActivity extends AppCompatActivity {

    RecyclerView accessories_recycler;

    LinearLayoutManager linearLayoutManager = null;

    ShowRoomViseDeviceListAdapter accessories = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.accessories_setting_activity);

            AccessoryContainerAdapter.AccCnt = 0;

            accessories_recycler = findViewById(R.id.accessories_recycler);

            linearLayoutManager = new LinearLayoutManager(getApplicationContext());

            accessories_recycler.setLayoutManager(linearLayoutManager);

            accessories = new ShowRoomViseDeviceListAdapter(getApplicationContext(), Utility.getRoomList(), UtilityConstants.SHOW_ROOMVISE_ACC);

            accessories_recycler.setAdapter(accessories);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static List<AccessoriesObject> getAccessoryList(RoomObject roomObject) {

        List<AccessoriesObject> accessoriesObjects = new ArrayList<>();

        for (AccessoriesObject accessoriesObject : Utility.AccessoriesMap.values()) {

            if (!accessoriesObject.getAccessory().equalsIgnoreCase(UtilityConstants.MINISERVER_ACCESORY) && roomObject.getMac().contains(accessoriesObject.getMac())) {

                accessoriesObjects.add(accessoriesObject);
            }

        }
        return accessoriesObjects;
    }

}
