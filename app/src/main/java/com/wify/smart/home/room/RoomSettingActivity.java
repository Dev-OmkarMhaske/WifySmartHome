package com.wify.smart.home.room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.Observable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.home.MainActivity;
import com.wify.smart.home.utils.Utility;

public class RoomSettingActivity extends AppCompatActivity {

    RecyclerView rooms_recyclerview;

    TextView done, add_room;

    LinearLayoutManager linearLayoutManager = null;

    RoomSettingAdapter roomSettingAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.room_setting_activity);

            done = findViewById(R.id.done);

            add_room = findViewById(R.id.add_room_textview);

            rooms_recyclerview = findViewById(R.id.rooms_recyclerview);

            rooms_recyclerview.setHasFixedSize(true);

            setView();

            Utility.reload.addOnPropertyChangedCallback(new androidx.databinding.Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {

                    setView();

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void setView() {

        try {

            if (!Utility.isHost) {

                add_room.setVisibility(View.GONE);
            }

            linearLayoutManager = new LinearLayoutManager(getApplicationContext());

            rooms_recyclerview.setLayoutManager(linearLayoutManager);

            roomSettingAdapter = new RoomSettingAdapter(getApplicationContext(), Utility.getRoomList());

            rooms_recyclerview.setAdapter(roomSettingAdapter);

            add_room.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(getApplicationContext(), RoomListActivity.class));

                }
            });

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
