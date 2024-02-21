package com.wify.smart.home.room;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.MotionObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.dto.WifyThemes;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;
import java.util.List;

public class RoomAccessoriesActivity extends AppCompatActivity {

    public static RoomObject roomObject;

    RecyclerView accessories_recycler, accessories_edit_recycler;

    GridLayoutManager mGridLayoutManager = null;

    LinearLayoutManager linearLayoutManager = null;

    TextView save_text, product_installed_msg, accessories_text, name;

    ImageView accessories_setting, room_logo, back_btn;

    List<Object> objectList = new ArrayList<>();

    LinearLayout parent_linear;

    ScrollView child_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

            if (SharedPreference.getTheme(getApplicationContext()) != null) {

                WifyThemes wifyThemes = SharedPreference.getTheme(getApplicationContext());

                setTheme(wifyThemes.getTheme());
            }

            setContentView(R.layout.view_room_accessories_activity);

            roomObject = (RoomObject) getIntent().getSerializableExtra(UtilityConstants.ROOM);

            save_text = findViewById(R.id.save_text);

            room_logo = findViewById(R.id.room_logo);

            back_btn = findViewById(R.id.back_btn);

            name = findViewById(R.id.name);

            product_installed_msg = findViewById(R.id.product_installed_msg);

            accessories_text = findViewById(R.id.accessories_text);

            room_logo.setImageDrawable(getApplicationContext().getResources().getDrawable(Utility.getRoomIcon(roomObject.getLogo()).getDisable_icon()));

            name.setText(roomObject.getName());

            accessories_recycler = findViewById(R.id.accessories_recycler);

            accessories_edit_recycler = findViewById(R.id.accessories_edit_recycler);

            mGridLayoutManager = new GridLayoutManager(getApplicationContext(), Utility.TILE_SPAN);

            accessories_recycler.setLayoutManager(mGridLayoutManager);

            linearLayoutManager = new LinearLayoutManager(getApplicationContext());

            accessories_edit_recycler.setLayoutManager(linearLayoutManager);

            accessories_setting = findViewById(R.id.accessories_setting);

            parent_linear = findViewById(R.id.parent_linear);

            child_layout = findViewById(R.id.child_layout);

            accessories_recycler.setHasFixedSize(true);

            if (Utility.RoomImages.containsKey(roomObject.getFile())) {

                parent_linear.setBackgroundColor(Color.BLACK);

                Drawable drawable = new BitmapDrawable(Utility.ConvertBase64TOBitmap(Utility.RoomImages.get(roomObject.getFile())));

                drawable.setAlpha(80);

                child_layout.setBackgroundDrawable(drawable);

            }

            setView();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void setView() {

        try {

            if (!Utility.isHost) {

                accessories_setting.setVisibility(View.GONE);
            }

            if (!Utility.isHomeConnected) {

                product_installed_msg.setText(roomObject.getMac().split(",").length + " products are installed. Try to connect Miniserver.");

                accessories_text.setVisibility(View.GONE);

            } else {

                product_installed_msg.setVisibility(View.GONE);

            }

            if (Utility.connectedHome != null && Utility.connectedHome.getAccessories() != null) {

                getRoomViseAccessories(UtilityConstants.ADD);

                findViewById(R.id.cancel_text).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getApplicationContext(), RoomAccessoriesActivity.class);

                        intent.putExtra(UtilityConstants.ROOM, RoomAccessoriesActivity.roomObject);

                        startActivity(intent);

                    }
                });

                findViewById(R.id.save_text).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getApplicationContext(), RoomAccessoriesActivity.class);

                        intent.putExtra(UtilityConstants.ROOM, RoomAccessoriesActivity.roomObject);

                        startActivity(intent);
                    }
                });

                if (Utility.CurrentUserObj != null && Utility.CurrentUserObj.getType().trim().length() > 0 && Utility.CurrentUserObj.getType().equalsIgnoreCase(UtilityConstants.USER_HOST)) {

                    findViewById(R.id.accessories_setting).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            child_layout.setBackground(null);

                            room_logo.setVisibility(View.GONE);

                            back_btn.setVisibility(View.VISIBLE);

                            findViewById(R.id.cancel_text).setVisibility(View.VISIBLE);

                            save_text.setVisibility(View.VISIBLE);

                            findViewById(R.id.accessories_text).setVisibility(View.GONE);

                            parent_linear.setBackground(getResources().getDrawable(R.color.App_edit_background));

                            accessories_setting.setColorFilter(Color.parseColor("#5B5B5E"));

                            accessories_edit_recycler.setVisibility(View.VISIBLE);

                            accessories_recycler.setVisibility(View.GONE);

                            RoomEditAccessoriesAdapter roomEditAccessoriesAdapter = new RoomEditAccessoriesAdapter(getApplicationContext(), getRoomViseAccessories(UtilityConstants.EDIT_STR), roomObject.getName());

                            accessories_edit_recycler.setAdapter(roomEditAccessoriesAdapter);
                        }
                    });

                }

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public List<Object> getRoomViseAccessories(String From) {

        RoomViewAccessoriesAdapter roomViewAccessoriesAdapter = null;

        try {

            objectList = new ArrayList<>();

            if (roomObject.getMac().length() > 0) {

                boolean flag = true;

                for (FanObject fanObject : Utility.fanObjectHashMap.values()) {

                    if (roomObject.getMac().contains(fanObject.getMac()) || (fanObject.getType().equalsIgnoreCase(UtilityConstants.CFM) && roomObject.getMac().contains(fanObject.getMac().replace("F1", "")))) {

                        FanObject fanObject1 = (FanObject) fanObject.clone();

                        objectList.add(fanObject1);

                        flag = false;

                    }
                }

                for (PowerObject powerObject : Utility.powerObjectHashMap.values()) {

                    if (roomObject.getMac().contains(powerObject.getMac())) {

                        PowerObject powerObject1 = (PowerObject) powerObject.clone();

                        objectList.add(powerObject1);

                        flag = false;

                    }
                }

                for (CurtainObject curtainObject : Utility.curtainObjectHashMap.values()) {

                    if (roomObject.getMac().contains(curtainObject.getMac())) {

                        CurtainObject curtainObject1 = (CurtainObject) curtainObject.clone();

                        objectList.add(curtainObject1);

                        flag = false;

                    }
                }

                for (MotionObject motionObject : Utility.motionObjectHashMap.values()) {

                    if (roomObject.getMac().contains(motionObject.getMac())) {

                        MotionObject motionObject1 = (MotionObject) motionObject.clone();

                        objectList.add(motionObject1);

                        flag = false;

                    }
                }

                for (RGBObject rgbObject : Utility.rgbObjectHashMap.values()) {

                    if (roomObject.getMac().contains(rgbObject.getMac())) {

                        RGBObject rgbObject1 = (RGBObject) rgbObject.clone();

                        objectList.add(rgbObject1);

                        flag = false;

                    }
                }

                for (GenericObject genericObject : Utility.genericObjectHashMap.values()) {

                    if (roomObject.getMac().contains(genericObject.getMac())) {

                        GenericObject genericObject1 = (GenericObject) genericObject.clone();

                        if (genericObject.getUsed().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                            objectList.add(genericObject1);

                            flag = false;

                        } else if (From.equalsIgnoreCase(UtilityConstants.EDIT_STR)) {

                            objectList.add(genericObject1);
                        }
                    }
                }

            }

            roomViewAccessoriesAdapter = new RoomViewAccessoriesAdapter(getApplicationContext(), objectList);

            accessories_recycler.setAdapter(roomViewAccessoriesAdapter);


        } catch (Exception e) {

            e.printStackTrace();
        }

        return objectList;
    }
}
