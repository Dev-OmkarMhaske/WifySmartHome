package com.wify.smart.home.room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.RoomIcons;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

public class RoomListActivity extends AppCompatActivity {

    RecyclerView room_list_recycler;

    public static int selectedPosition = -1;

    LinearLayoutManager linearLayoutManager = null;

    RoomListAdapter roomListAdapter = null;

    RoomIcons roomIcons = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

            setContentView(R.layout.room_list_activity);

            room_list_recycler = findViewById(R.id.room_list_recycler);

            selectedPosition = -1;

            linearLayoutManager = new LinearLayoutManager(getApplicationContext());

            room_list_recycler.setLayoutManager(linearLayoutManager);

            room_list_recycler.setHasFixedSize(true);

            roomListAdapter = new RoomListAdapter(getApplicationContext(), Utility.Defined_room_List);

            room_list_recycler.setAdapter(roomListAdapter);

            findViewById(R.id.save_text).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (selectedPosition != -1) {

                        roomIcons = Utility.Defined_room_List.get(selectedPosition);

                        RoomObject roomObject = new RoomObject();

                        roomObject.setName(roomIcons.getName());

                        roomObject.setLogo(roomIcons.getName());

                        roomObject.setMac("");

                        roomObject.setFile("");

                        Intent i = new Intent(getApplicationContext(), AddRoomActivity.class);

                        i.putExtra(UtilityConstants.ACTION, UtilityConstants.ADD);

                        i.putExtra(UtilityConstants.ROOM_OBJ, roomObject);

                        finish();

                        startActivity(i);
                    }


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
