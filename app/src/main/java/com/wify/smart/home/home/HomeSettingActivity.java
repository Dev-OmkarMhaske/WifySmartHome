package com.wify.smart.home.home;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.Observable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.activities.LoadingActivity;
import com.wify.smart.home.adapters.ThemeAdapter;
import com.wify.smart.home.dto.HomeObject;
import com.wify.smart.home.dto.UserObject;
import com.wify.smart.home.dto.WifyThemes;
import com.wify.smart.home.mqtt.MqttClient;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.UpdateMiniserver;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.LinkedList;

public class HomeSettingActivity extends AppCompatActivity {

    public static int selectedPosition = 0;

    RecyclerView home_setting_users_recycler, wify_themes;

    EditText home_name, home_notes;

    HomeSettingUserAdapter homeSettingUserAdapter;

    LinearLayout invite_people_layout, theme_layout;

    String from = null;

    LinearLayoutManager linearLayoutManager = null;

    ThemeAdapter themeAdapter = null;

    GridLayoutManager GridLayoutManager = null;

    int status = 0;

    Handler handler = new Handler();

    Button factory_reset_btn, alexa_config_btn, update_miniserver_btn;

    public static LinkedList<UserObject> getUserData() {

        LinkedList<UserObject> userObjectList = new LinkedList<>();

        if (Utility.CurrentUserObj != null && Utility.CurrentUserObj.getPhn().length() == 10) {

            userObjectList.add(Utility.CurrentUserObj);

        }

        for (UserObject userObject : Utility.USERMap.values()) {

            if (userObject != null && Utility.CurrentUserObj != null && !userObject.getPhn().equalsIgnoreCase(Utility.CurrentUserObj.getPhn())) {

                userObjectList.add(userObject);

            }

        }
        return userObjectList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_setting_activity);

        from = getIntent().getStringExtra(UtilityConstants.FROM);

        if (SharedPreference.getTheme(getApplicationContext()) != null) {

            WifyThemes themes = SharedPreference.getTheme(getApplicationContext());

            for (int i = 0; i < Utility.themesList.size(); i++) {

                if (Utility.themesList.get(i).getName().equalsIgnoreCase(themes.getName())) {

                    selectedPosition = i;
                    break;
                }
            }
        }

        home_name = findViewById(R.id.home_name);

        factory_reset_btn = findViewById(R.id.factory_reset_btn);

        home_notes = findViewById(R.id.home_notes);

        theme_layout = findViewById(R.id.theme_layout);

        alexa_config_btn = findViewById(R.id.alexa_config_btn);

        update_miniserver_btn = findViewById(R.id.update_miniserver_btn);

        invite_people_layout = findViewById(R.id.invite_people_layout);

        home_setting_users_recycler = findViewById(R.id.home_setting_users_recyclerview);

        wify_themes = findViewById(R.id.wify_themes);

        home_setting_users_recycler.setHasFixedSize(true);

        wify_themes.setHasFixedSize(true);

        setView();

        Utility.reload.addOnPropertyChangedCallback(new androidx.databinding.Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {

                setView();

            }
        });

        update_miniserver_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        new UpdateMiniserver().uploadFile1();
                    }
                }).start();

                update_miniserver_btn.setVisibility(View.GONE);

                ShowMiniserverUpdateDialog();

            }
        });

        factory_reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText input = new EditText(HomeSettingActivity.this);
                input.setHint(getString(R.string.confirm_password_txt));
                input.setHintTextColor(getApplicationContext().getResources().getColor(R.color.black_transparent));
                input.setTextColor(getApplicationContext().getResources().getColor(R.color.text_white));

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeSettingActivity.this, R.style.AlertDialogCustom);
                builder.setMessage(getString(R.string.confirm_factory_rst_str))
                        .setTitle(getString(R.string.factory_rst_txt))
                        .setView(input)
                        .setPositiveButton(UtilityConstants.CONFIRM_STR, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                if (UtilityConstants.ADMIN_PASSWORD.equalsIgnoreCase(input.getText().toString())) {

                                    MqttClient.publishMessage(MqttClient.HOMEUID, MqttOperation.getTransactionObject(UtilityConstants.RESET, UtilityConstants.ALL_TXT));

                                    ClearPreferences();

                                    Intent intent = new Intent(getApplicationContext(), LoadingActivity.class);

                                    intent.putExtra(UtilityConstants.HOME_OBJ, Utility.connectedHome);

                                    startActivity(intent);

                                } else {

                                    Toast.makeText(HomeSettingActivity.this, R.string.valid_password_err_txt, Toast.LENGTH_SHORT).show();
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

            }
        });

        findViewById(R.id.remove_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText input = new EditText(HomeSettingActivity.this);
                input.setHint(getString(R.string.confirm_password_txt));
                input.setHintTextColor(getApplicationContext().getResources().getColor(R.color.black_transparent));
                input.setTextColor(getApplicationContext().getResources().getColor(R.color.text_white));

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeSettingActivity.this, R.style.AlertDialogCustom);
                builder.setMessage("Are you sure you want to delete Home ?")
                        .setTitle("Delete Home")
                        .setView(input)
                        .setPositiveButton(UtilityConstants.CONFIRM_STR, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (UtilityConstants.ADMIN_PASSWORD.equalsIgnoreCase(input.getText().toString())) {

                                    if (Utility.connectedHome != null && Utility.connectedHome.getHome_uid().trim().length() > 0) {

                                        //Utility.myRef_homes.child(Utility.connectedHome.getHome_uid()).removeValue();

                                        Toast.makeText(HomeSettingActivity.this, "Remove home", Toast.LENGTH_SHORT).show();

                                    }

                                } else {
                                    Toast.makeText(HomeSettingActivity.this, R.string.valid_password_err_txt, Toast.LENGTH_SHORT).show();
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

            }
        });

    }

    public void ClearPreferences() {

        Utility.connectedHome.setAccessories(null);

        Utility.connectedHome.setUsers(null);

        Utility.UpdateHome(Utility.connectedHome);

        SharedPreference.setIP(getApplicationContext(), null);

        SharedPreference.setKeyMap(getApplicationContext(), null);

        SharedPreference.setAccessories(getApplicationContext(), null);

        SharedPreference.setRooms(getApplicationContext(), null);

        SharedPreference.setAccessorySyncFlag(getApplicationContext(), false);

        SharedPreference.setUsers(getApplicationContext(), null);

        SharedPreference.setFMs(getApplicationContext(), null);

        SharedPreference.setCMs(getApplicationContext(), null);

        SharedPreference.setGMs(getApplicationContext(), null);

        SharedPreference.setRGBs(getApplicationContext(), null);

        SharedPreference.setMMs(getApplicationContext(), null);

        SharedPreference.setPMs(getApplicationContext(), null);

        SharedPreference.setScenes(getApplicationContext(), null);

        SharedPreference.setSchedules(getApplicationContext(), null);

    }

    public void ConfirmHome() {

        final EditText input = new EditText(HomeSettingActivity.this);

        input.setHint(getString(R.string.confirm_password_txt));

        input.setHintTextColor(getApplicationContext().getResources().getColor(R.color.black_transparent));

        input.setTextColor(getApplicationContext().getResources().getColor(R.color.text_white));

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeSettingActivity.this, R.style.AlertDialogCustom);

        builder.setMessage(R.string.confirm_home_txt)
                .setTitle(R.string.create_home_str)
                .setView(input)
                .setPositiveButton(UtilityConstants.CONFIRM_STR, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        if (UtilityConstants.ADMIN_PASSWORD.equalsIgnoreCase(input.getText().toString())) {

                            HomeObject homeObject = new HomeObject();

                            if (Utility.OnlineVersions.getMQTT() != null) {

                                homeObject.setMqtt(Utility.OnlineVersions.getMQTT());
                            }

                            homeObject.setHome(home_name.getText().toString());

                            homeObject.setNote(home_notes.getText().toString());

                            homeObject.setDate(Utility.getDate_inDDMMYY());

                            Utility.saveHome(homeObject, getApplicationContext());

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            Toast.makeText(HomeSettingActivity.this, R.string.home_create_success_str, Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(HomeSettingActivity.this, R.string.valid_password_err_txt, Toast.LENGTH_SHORT).show();
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

    }

    public void setView() {

        if (SharedPreference.getALEXA(getApplicationContext()).equalsIgnoreCase(UtilityConstants.TRUE_TXT)) {

            alexa_config_btn.setText(R.string.alexa_stop_config);

            alexa_config_btn.setTextColor(getApplicationContext().getResources().getColor(R.color.error_text_color));

        } else {

            alexa_config_btn.setText(R.string.alexa_start_config);

            alexa_config_btn.setTextColor(getApplicationContext().getResources().getColor(R.color.bold_text_color));

        }
        if (!Utility.isHost) {

            alexa_config_btn.setVisibility(View.GONE);

            factory_reset_btn.setVisibility(View.GONE);

            invite_people_layout.setVisibility(View.GONE);

            update_miniserver_btn.setVisibility(View.GONE);

            home_notes.setRawInputType(InputType.TYPE_NULL);

        } else if (Utility.COMMUNICATION_MODE == 1 || Utility.COMMUNICATION_MODE == 2) {

            invite_people_layout.setVisibility(View.VISIBLE);
        }

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        GridLayoutManager = new GridLayoutManager(getApplicationContext(), Utility.TILE_SPAN);

        home_setting_users_recycler.setLayoutManager(linearLayoutManager);

        wify_themes.setLayoutManager(GridLayoutManager);

        themeAdapter = new ThemeAdapter(getApplicationContext(), Utility.themesList);

        wify_themes.setAdapter(themeAdapter);

        if (from.equalsIgnoreCase(UtilityConstants.ADD)) {

            theme_layout.setVisibility(View.GONE);

            alexa_config_btn.setVisibility(View.GONE);

            factory_reset_btn.setVisibility(View.GONE);

            home_notes.setRawInputType(InputType.TYPE_CLASS_TEXT);

        } else {

            theme_layout.setVisibility(View.VISIBLE);

        }

        if (Utility.connectedHome != null && from.equalsIgnoreCase(UtilityConstants.EDIT_STR) && Utility.connectedHome.getHome_uid() != null) {

            home_name.setRawInputType(InputType.TYPE_NULL);

            home_name.setText(Utility.connectedHome.getHome());

            home_notes.setText(Utility.connectedHome.getNote());

            homeSettingUserAdapter = new HomeSettingUserAdapter(getApplicationContext(), getUserData());

            home_setting_users_recycler.setAdapter(homeSettingUserAdapter);

        }

        alexa_config_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce));

                if (alexa_config_btn.getText().toString().equalsIgnoreCase(getString(R.string.alexa_start_config))) {

                    SharedPreference.setALEXA(getApplicationContext(), UtilityConstants.TRUE_TXT);

                    alexa_config_btn.setText(R.string.alexa_stop_config);

                    alexa_config_btn.setTextColor(getApplicationContext().getResources().getColor(R.color.error_text_color));

                    MqttClient.publishMessage(MqttClient.HOMEUID, MqttOperation.getTransactionObject(UtilityConstants.ALEXA_START_STR, ""));

                } else {

                    SharedPreference.setALEXA(getApplicationContext(), UtilityConstants.FALSE_TXT);

                    alexa_config_btn.setText(R.string.alexa_start_config);

                    alexa_config_btn.setTextColor(getApplicationContext().getResources().getColor(R.color.bold_text_color));

                    MqttClient.publishMessage(MqttClient.HOMEUID, MqttOperation.getTransactionObject(UtilityConstants.ALEXA_STOP_STR, ""));
                }
            }
        });

        findViewById(R.id.cancel_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });

        findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setTheme();

                if (from.equalsIgnoreCase(UtilityConstants.EDIT_STR)) {

                    if (Utility.isHost && Utility.connectedHome != null && Utility.connectedHome.getHome() != null && Utility.connectedHome.getHome().length() > 0) {

                        Utility.connectedHome.setNote(home_notes.getText().toString());

                        Utility.UpdateHome(Utility.connectedHome);

                    }

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                } else if (home_name.getText().toString().length() > 0) {

                    ConfirmHome();
                }

            }
        });

        findViewById(R.id.add_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utility.COMMUNICATION_MODE == 1 || Utility.COMMUNICATION_MODE == 2) {

                    startActivity(new Intent(getApplicationContext(), AddUserActivity.class));

                } else {

                    Toast.makeText(getApplicationContext(), R.string.connect_through_online_str, Toast.LENGTH_SHORT).show();

                }

            }
        });

        findViewById(R.id.invite_people).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utility.COMMUNICATION_MODE == 1 || Utility.COMMUNICATION_MODE == 2) {

                    startActivity(new Intent(getApplicationContext(), AddUserActivity.class));

                } else {

                    Toast.makeText(getApplicationContext(), R.string.connect_through_online_str, Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    public void setTheme() {

        try {

            if (selectedPosition != -1) {

                WifyThemes wifyThemes = Utility.themesList.get(selectedPosition);

                SharedPreference.setTheme(getApplicationContext(), wifyThemes);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void ShowMiniserverUpdateDialog() {

        try {

            final Dialog dialog = new Dialog(this);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setCancelable(false);

            dialog.setContentView(R.layout.progress_dialog);

            final ProgressBar text = (ProgressBar) dialog.findViewById(R.id.progress_horizontal);

            final TextView text2 = dialog.findViewById(R.id.value123);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    while (status < 100) {

                        status += 1;

                        try {

                            Thread.sleep(200);

                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }

                        handler.post(new Runnable() {
                            @Override

                            public void run() {

                                text.setProgress(status);

                                text2.setText(String.valueOf(status));

                                if (status == 100) {

                                    dialog.dismiss();

                                    Toast.makeText(HomeSettingActivity.this, R.string.miniserver_update_success_str, Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
                    }
                }
            }).start();

            dialog.show();

            Window window = dialog.getWindow();

            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
