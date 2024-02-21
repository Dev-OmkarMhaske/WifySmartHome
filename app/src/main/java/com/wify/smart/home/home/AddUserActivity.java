package com.wify.smart.home.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.dto.UserObject;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;
import java.util.List;

public class AddUserActivity extends AppCompatActivity {

    TextView save_user;

    EditText users_number, user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_user_activity);

        users_number = findViewById(R.id.users_number);

        user_name = findViewById(R.id.user_name);

        save_user = findViewById(R.id.save_user);

        findViewById(R.id.cancel_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        save_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (users_number.getText().toString().trim().length() != 10) {

                    Toast.makeText(AddUserActivity.this, R.string.valid_mobile_txt, Toast.LENGTH_SHORT).show();

                    return;

                } else if (user_name.getText().toString().trim().length() == 0) {

                    Toast.makeText(AddUserActivity.this, R.string.valid_username_txt, Toast.LENGTH_SHORT).show();

                    return;

                } else if (user_name.getText().toString().trim().length() > 0 && users_number.getText().toString().trim().length() == 10) {

                    UserObject userObject = new UserObject();

                    userObject.setMac("");

                    userObject.setName(user_name.getText().toString());

                    userObject.setPhn(users_number.getText().toString().trim());

                    userObject.setPhn_mac("");

                    userObject.setType(UtilityConstants.USER_RESIDENT);

                    userObject.setLast("" + System.currentTimeMillis());

                    userObject.setFile(UtilityConstants.USER + userObject.getPhn());

                    if (Utility.connectedHome.getHome_uid() != null) {

                        if (Utility.connectedHome.getUsers() == null) {

                            Utility.connectedHome.setUsers(new ArrayList<>());

                        }

                        if (Utility.connectedHome.getUsers() != null) {

                            if (!new Gson().toJson(Utility.connectedHome.getUsers()).contains(userObject.getPhn())) {

                                List<String> userObjects = Utility.connectedHome.getUsers();

                                userObjects.add(userObject.getPhn());

                                Utility.connectedHome.setUsers(userObjects);

                            }

                        }

                        Utility.setDbData(userObject.getPhn(), Utility.connectedHome.getHome_uid(), userObject.getName());

                        Utility.myRef_homes.child(Utility.connectedHome.getHome_uid()).setValue(Utility.connectedHome);

                        MqttOperation.spiffsValueAction(UtilityConstants.WRITE, UtilityConstants.USER, userObject.getFile(), new Gson().toJson(userObject));

                        Toast.makeText(AddUserActivity.this, R.string.invitation_send_success_txt, Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(AddUserActivity.this, R.string.connect_home_txt, Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(getApplicationContext(), HomeSettingActivity.class);

                    intent.putExtra(UtilityConstants.FROM, UtilityConstants.EDIT_STR);

                    startActivity(intent);

                }
            }
        });
    }

}


