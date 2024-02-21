package com.wify.smart.home.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.adapters.UserSceneAdapter;
import com.wify.smart.home.dto.DBObject;
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.dto.UserObject;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SettingUserViewActivity extends AppCompatActivity {

    public static UserObject userObject = null;

    RecyclerView user_room_access_recycler, user_scene_access;

    TextView user_txt, user_name, user_type_txt, save_user;

    public static LinkedHashMap<String, String> map = null;

    UserSceneAdapter userSceneAdapter = null;

    List<SceneObject> sceneObjectList = null;

    LinearLayoutManager linearLayoutManager, linearLayoutManager2;

    UserAccessControlAdapter userAccessControlAdapter = null;

    public static void updateUserMacData(String updatedata, String action) {

        try {

            map = new LinkedHashMap<>();

            if (userObject != null && userObject.getMac().length() > 0) {

                for (String s : userObject.getMac().trim().split(",")) {

                    map.put(s.trim(), s.trim());

                }

            }

            if (action.equals(UtilityConstants.ADD)) {

                String update[] = updatedata.split(",");

                map.put(new StringBuilder(update[0]).append(",").toString(), updatedata);

            }

            if (action.equals(UtilityConstants.DELETE)) {

                map.remove(new StringBuilder(updatedata).toString());

            }

            userObject.setMac(map.values().toString().trim().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "").trim());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.setting_user_view_activity);

            sceneObjectList = new ArrayList<>();

            user_txt = findViewById(R.id.user_phoneNo);

            user_name = findViewById(R.id.user_name);

            user_type_txt = findViewById(R.id.user_type);

            user_scene_access = findViewById(R.id.user_scene_access);

            save_user = findViewById(R.id.save_user);

            userObject = (UserObject) getIntent().getSerializableExtra(UtilityConstants.USER);

            String user_type = getIntent().getStringExtra("type");

            if (userObject != null) {

                user_name.setText(userObject.getPhn());

                user_type_txt.setText(user_type);

                user_txt.setText(userObject.getName());

            }

            user_room_access_recycler = findViewById(R.id.user_room_access);

            user_room_access_recycler.setHasFixedSize(true);

            user_scene_access.setHasFixedSize(true);

            linearLayoutManager = new LinearLayoutManager(getApplicationContext());

            user_room_access_recycler.setLayoutManager(linearLayoutManager);

            linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());

            user_scene_access.setLayoutManager(linearLayoutManager2);

            for (SceneObject sceneObject : Utility.SCENEMap.values()) {

                if (sceneObject != null) {

                    sceneObjectList.add(sceneObject);

                }

            }

            userSceneAdapter = new UserSceneAdapter(getApplicationContext(), sceneObjectList, userObject);

            user_scene_access.setAdapter(userSceneAdapter);

            userAccessControlAdapter = new UserAccessControlAdapter(getApplicationContext(), Utility.getRoomList(), userObject);

            user_room_access_recycler.setAdapter(userAccessControlAdapter);

            findViewById(R.id.back_text).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onBackPressed();

                }
            });

            save_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    userObject.setLast("" + System.currentTimeMillis());

                    MqttOperation.spiffsValueAction(UtilityConstants.WRITE, UtilityConstants.USER, userObject.getFile(), new Gson().toJson(userObject));

                    finish();

                    onBackPressed();

                }
            });

            findViewById(R.id.remove_person).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        if (Utility.COMMUNICATION_MODE == 2 || Utility.COMMUNICATION_MODE == 1) {

                            final EditText input = new EditText(SettingUserViewActivity.this);

                            input.setHint(getString(R.string.confirm_password_txt));

                            input.setHintTextColor(getApplicationContext().getResources().getColor(R.color.black_transparent));

                            input.setTextColor(getApplicationContext().getResources().getColor(R.color.text_white));

                            AlertDialog.Builder builder = new AlertDialog.Builder(SettingUserViewActivity.this, R.style.AlertDialogCustom);
                            builder.setMessage(getString(R.string.remove_person_str))
                                    .setTitle(getString(R.string.delete_person_str))
                                    .setView(input)
                                    .setPositiveButton(UtilityConstants.CONFIRM_STR, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int id) {

                                            if (UtilityConstants.ADMIN_PASSWORD.equalsIgnoreCase(input.getText().toString())) {

                                                RemovePerson(userObject.getPhn().trim());

                                                MqttOperation.spiffsValueAction(UtilityConstants.DELETE, UtilityConstants.USER, userObject.getFile(), "");

                                                finish();

                                                Intent intent = new Intent(getApplicationContext(), HomeSettingActivity.class);

                                                intent.putExtra(UtilityConstants.FROM, UtilityConstants.EDIT_STR);

                                                startActivity(intent);

                                                Toast.makeText(SettingUserViewActivity.this, "Remove person " + userObject.getPhn(), Toast.LENGTH_SHORT).show();

                                            } else {

                                                Toast.makeText(SettingUserViewActivity.this, R.string.valid_password_err_txt, Toast.LENGTH_SHORT).show();

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

                        } else {

                            Toast.makeText(SettingUserViewActivity.this, R.string.connect_through_online_str, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void RemovePerson(String uid) {

        try {

            if (uid.length() > 0) {

                Utility.myRef_db.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        DBObject dbObject = new DBObject();

                        if (dataSnapshot.exists()) {

                            dbObject = dataSnapshot.getValue(DBObject.class);

                            if (dbObject.getShared() != null) {

                                dbObject.getShared().remove(Utility.connectedHome.getHome_uid());

                            }

                            Utility.myRef_db.child(uid).setValue(dbObject);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

                if (Utility.connectedHome != null && Utility.connectedHome.getUsers() != null) {

                    Utility.connectedHome.getUsers().remove(uid);

                    Utility.UpdateHome(Utility.connectedHome);

                }

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
