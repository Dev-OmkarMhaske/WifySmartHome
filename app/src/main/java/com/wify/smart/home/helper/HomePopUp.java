package com.wify.smart.home.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.wify.smart.home.R;
import com.wify.smart.home.activities.ConnectMiniserverActivity;
import com.wify.smart.home.activities.LoadingActivity;
import com.wify.smart.home.dto.HomeObject;
import com.wify.smart.home.home.AccessorySettingContainerActivity;
import com.wify.smart.home.home.AddUserActivity;
import com.wify.smart.home.home.HomeListAdapter;
import com.wify.smart.home.home.HomeSettingActivity;
import com.wify.smart.home.home.MainActivity;
import com.wify.smart.home.login.LoginActivity;
import com.wify.smart.home.mqtt.MqttClient;
import com.wify.smart.home.room.AddAccessoriesBottomSheetAdapter;
import com.wify.smart.home.room.RoomListActivity;
import com.wify.smart.home.room.RoomSettingActivity;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomePopUp {

    public static void applyDim(@NonNull ViewGroup parent, float dimAmount) {
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    public static void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }

    public void showPopupHomeSetting(final View view) {

        try {

            LinearLayout home_setting_layout, room_setting_layout, accessory_setting_layout;

            LinearLayoutManager linearLayoutManager = null;

            ImageView home_setting, room_setting, accessory_setting, logout;

            RecyclerView homes_recycler;

            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);

            View popupView = inflater.inflate(R.layout.home_setting_popup, null);

            accessory_setting = popupView.findViewById(R.id.accessory_setting);

            home_setting = popupView.findViewById(R.id.home_setting);

            room_setting = popupView.findViewById(R.id.room_setting);

            home_setting_layout = popupView.findViewById(R.id.home_setting_layout);

            accessory_setting_layout = popupView.findViewById(R.id.accessory_setting_layout);

            room_setting_layout = popupView.findViewById(R.id.room_setting_layout);

            if (Utility.connectedHome == null) {

                home_setting_layout.setVisibility(View.GONE);

                room_setting_layout.setVisibility(View.GONE);

                accessory_setting_layout.setVisibility(View.GONE);
            }

            logout = popupView.findViewById(R.id.logout);

            homes_recycler = popupView.findViewById(R.id.homes_recycler);

            if (!Utility.isHost) {
                accessory_setting_layout.setVisibility(View.GONE);
            }

            homes_recycler.setHasFixedSize(true);

            linearLayoutManager = new LinearLayoutManager(popupView.getContext());

            homes_recycler.setLayoutManager(linearLayoutManager);

            int width = 600;

            int height = LinearLayout.LayoutParams.WRAP_CONTENT;

            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 50, 100);

            ViewGroup root = (ViewGroup) ((Activity) popupView.getContext()).getWindow().getDecorView().getRootView();

            applyDim(root, 0.9f);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {

                    clearDim(root);
                }
            });

            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    return true;

                }
            });

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreference.clearSharedPreference(popupView.getContext());

                    if (MqttClient.mqttAndroidClient != null && MqttClient.mqttAndroidClient.isConnected()) {

                        MqttClient.mqttAndroidClient.disconnect();
                    }

                    if (LoadingActivity.UIHandler != null) {

                        System.out.println(">>>>>> remove UI handler >>>>>");
                        LoadingActivity.UIHandler.removeCallbacksAndMessages(null);
                    }

                    Utility.ResetAttribute();

                    popupWindow.dismiss();

                    Intent intent = new Intent(popupView.getContext(), LoginActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    popupView.getContext().startActivity(intent);

                    Toast.makeText(popupView.getContext(), R.string.logout_success_str, Toast.LENGTH_SHORT).show();

                }
            });

            home_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    popupWindow.dismiss();

                    Intent intent = new Intent(new Intent(view.getContext(), HomeSettingActivity.class));

                    intent.putExtra(UtilityConstants.FROM, UtilityConstants.EDIT_STR);

                    view.getContext().startActivity(intent);

                }
            });

            accessory_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    popupWindow.dismiss();

                    view.getContext().startActivity(new Intent(view.getContext(), AccessorySettingContainerActivity.class));
                }
            });

            room_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    popupWindow.dismiss();

                    view.getContext().startActivity(new Intent(view.getContext(), RoomSettingActivity.class));

                }
            });

            getUserHomes(popupView, homes_recycler, popupWindow);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void showPopupHome_Add(final View view) {

        try {

            LinearLayout add_room, add_people, add_accessories, add_new_home;

            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);

            View popupView = inflater.inflate(R.layout.home_add_popup, null);

            add_room = popupView.findViewById(R.id.add_room);

            add_people = popupView.findViewById(R.id.add_people);

            add_accessories = popupView.findViewById(R.id.add_accessories);

            add_new_home = popupView.findViewById(R.id.add_new_home);

            add_room.setVisibility(View.GONE);

            add_people.setVisibility(View.GONE);

            add_accessories.setVisibility(View.GONE);

            if (Utility.isHost) {

                if (Utility.COMMUNICATION_MODE != 0) {

                    add_room.setVisibility(View.VISIBLE);

                    add_people.setVisibility(View.VISIBLE);

                }

                add_accessories.setVisibility(View.VISIBLE);

            }

            int width = 600;

            int height = LinearLayout.LayoutParams.WRAP_CONTENT;

            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, MainActivity.plusIconX, MainActivity.plusIconY);

            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    return true;

                }
            });

            ViewGroup root = (ViewGroup) ((Activity) popupView.getContext()).getWindow().getDecorView().getRootView();

            applyDim(root, 0.9f);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {

                    clearDim(root);
                }
            });

            add_new_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (LoginActivity.isInternetAvailable(popupView.getContext())) {

                        Intent intent = new Intent(new Intent(popupView.getContext(), HomeSettingActivity.class));

                        intent.putExtra(UtilityConstants.FROM, UtilityConstants.ADD);

                        popupView.getContext().startActivity(intent);

                    } else {
                        Toast.makeText(view.getContext(), R.string.no_internet_txt, Toast.LENGTH_SHORT).show();
                    }

                }
            });

            add_accessories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showBottomSheetDialog(popupView.getContext());

                }
            });

            add_people.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (Utility.COMMUNICATION_MODE == 2 || Utility.COMMUNICATION_MODE == 1) {

                        popupView.getContext().startActivity(new Intent(popupView.getContext(), AddUserActivity.class));

                    } else {

                        Toast.makeText(view.getContext(), R.string.connect_through_online_str, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            add_room.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    popupView.getContext().startActivity(new Intent(popupView.getContext(), RoomListActivity.class));

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void showBottomSheetDialog(Context mCtx) {

        try {

            TextView miniserver_text;

            ArrayList<String> accessories = new ArrayList<>();

            LinearLayoutManager linearLayoutManager = null;

            RecyclerView room_add_accessories_recycler;

            AddAccessoriesBottomSheetAdapter addAccessoriesBottomSheetAdapter = null;

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mCtx, R.style.MyBottomSheetDialogTheme);

            if (Utility.isHomeConnected && Utility.isMiniserverInstall) {

                //  MqttClient.publishMessage(MqttClient.HOMEUID, MqttOperation.getTransactionObject(UtilityConstants.INSTALLATION_START_STR, ""));

                accessories = new ArrayList<>();

                accessories.add(UtilityConstants.GENERIC_MODULE);

                accessories.add(UtilityConstants.FAN_MODULE);

                accessories.add(UtilityConstants.POWER_MODULE);

                accessories.add(UtilityConstants.CURTAIN_MODULE);

                accessories.add(UtilityConstants.RGB_MODULE);

                accessories.add(UtilityConstants.MOTION_MODULE);

                accessories.add(UtilityConstants.DIMMER_MODULE);

                accessories.add(UtilityConstants.CONTROLLER_MODULE);

                bottomSheetDialog.setContentView(R.layout.room_add_accessories_bottom_sheet);

                room_add_accessories_recycler = bottomSheetDialog.findViewById(R.id.room_add_accessories_recycler);

                room_add_accessories_recycler.setHasFixedSize(true);

                linearLayoutManager = new LinearLayoutManager(mCtx);

                room_add_accessories_recycler.setLayoutManager(linearLayoutManager);

                addAccessoriesBottomSheetAdapter = new AddAccessoriesBottomSheetAdapter(mCtx, accessories);

                room_add_accessories_recycler.setAdapter(addAccessoriesBottomSheetAdapter);

                bottomSheetDialog.show();

            } else {

                bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);

                miniserver_text = bottomSheetDialog.findViewById(R.id.miniserver_text);

                miniserver_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mCtx.startActivity(new Intent(mCtx, ConnectMiniserverActivity.class));

                    }
                });

                bottomSheetDialog.show();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public static void getUserHomes(View view, RecyclerView homes_recycler, PopupWindow popupWindow) {

        try {

            HashMap<String, HomeObject> homeObjectHashMap = new HashMap<>();

            if (SharedPreference.getUserHostedHomes(view.getContext()) != null) {

                homeObjectHashMap.putAll(SharedPreference.getUserHostedHomes(view.getContext()));

            }

            if (SharedPreference.getUserSharedHomes(view.getContext()) != null) {

                homeObjectHashMap.putAll(SharedPreference.getUserSharedHomes(view.getContext()));
            }

            if (Utility.dbObject != null) {

                if (Utility.dbObject.getHost() != null) {

                    for (String home_id : Utility.dbObject.getHost()) {

                        Utility.myRef_homes.child(home_id).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    System.out.println(">>>>> onDataChange >>>> getHost >>>");

                                    HomeObject homeObject = dataSnapshot.getValue(HomeObject.class);

                                    homeObjectHashMap.put(homeObject.getHome_uid(), homeObject);

                                    homes_recycler.setAdapter(new HomeListAdapter(view, new ArrayList<HomeObject>(homeObjectHashMap.values()), popupWindow));

                                    SharedPreference.setUserHostedHomes(view.getContext(), homeObjectHashMap);

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                    }

                }

                if (Utility.dbObject.getShared() != null) {

                    for (String home_shared_id : Utility.dbObject.getShared()) {

                        Utility.myRef_homes.child(home_shared_id).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    System.out.println(">>>>> onDataChange >>>> getShared >>>");

                                    HomeObject homeObject = dataSnapshot.getValue(HomeObject.class);

                                    homeObjectHashMap.put(homeObject.getHome_uid(), homeObject);

                                    homes_recycler.setAdapter(new HomeListAdapter(view, new ArrayList<HomeObject>(homeObjectHashMap.values()), popupWindow));

                                    SharedPreference.setUserSharedHomes(view.getContext(), homeObjectHashMap);

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    }
                }

            }

            System.out.println(">>>>>> setting values to adapter >>>>>");

            List<HomeObject> homeObjectList = new ArrayList<HomeObject>(homeObjectHashMap.values());

            HomeListAdapter homeListAdapter = new HomeListAdapter(view, homeObjectList, popupWindow);

            homes_recycler.setAdapter(homeListAdapter);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
