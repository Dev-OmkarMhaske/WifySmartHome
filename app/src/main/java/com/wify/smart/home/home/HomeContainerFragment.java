package com.wify.smart.home.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.wify.smart.home.R;
import com.wify.smart.home.activities.WiFiGestureTabContainerActivity;
import com.wify.smart.home.dto.AccessoriesObject;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.HomeObject;
import com.wify.smart.home.dto.MotionObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.dto.ScheduleObject;
import com.wify.smart.home.dto.UserObject;
import com.wify.smart.home.helper.DeviceAxisValueFormatter;
import com.wify.smart.home.helper.MyAxisValueFormatter;
import com.wify.smart.home.helper.MyValueFormatter;
import com.wify.smart.home.mqtt.TransactionPool;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class HomeContainerFragment extends Fragment implements OnChartValueSelectedListener {

    public static View view;

    public static Context context;

    public static LinearLayout parent_container, miniserver_layout;

    public static FavouritesAccessoriesAdapter favouritesAccessoriesAdapter = null;

    public static FavouritesScenesAdapter favouritesScenesAdapter = null;

    public static HomeUsersAdapter homeUsersAdapter = null;

    public static ScrollView child_container;

    public static RecyclerView user_recycler, favourites_scene_recycler, favourites_accessories_recycler;

    public static ImageView miniserver_setting, miniserverImageView;

    public static TextView next_schedule, error_msg, next_schedule_txt, sceneText, FavouriteText, home_name, online_cnt_view, offline_cnt_view;

    public static ProgressBar offline_loader;

    public static int online_cnt = 0;

    public static int offline_cnt = 0;

    public static BarChart chart;

    public static int getNextTime(TreeMap<Integer, ScheduleObject> map, int index) {

        int i = 0;

        try {

            for (Map.Entry<Integer, ScheduleObject> entry : map.entrySet()) {

                if (i == index) {

                    return entry.getKey();
                }

                i++;

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return 0;
    }

    public static void setModuleCnt() {

        try {

            HashMap<String, AccessoriesObject> accessoriesObjectHashMap = SharedPreference.getAccessories(context);

            offline_cnt = 0;

            online_cnt = 0;

            if (accessoriesObjectHashMap != null && accessoriesObjectHashMap.size() > 1) {

                for (GenericObject genericObject : Utility.genericObjectHashMap.values()) {

                    if (genericObject.getUsed().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                        if (genericObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                            offline_cnt++;

                        } else {

                            online_cnt++;
                        }

                    }
                }

                for (PowerObject powerObject : Utility.powerObjectHashMap.values()) {

                    if (powerObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                        offline_cnt++;

                    } else {

                        online_cnt++;
                    }
                }

                for (MotionObject motionObject : Utility.motionObjectHashMap.values()) {

                    if (motionObject.getActive().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                        offline_cnt++;

                    } else {

                        online_cnt++;
                    }
                }

                for (RGBObject rgbObject : Utility.rgbObjectHashMap.values()) {

                    if (rgbObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                        offline_cnt++;

                    } else {

                        online_cnt++;
                    }

                }

                for (FanObject fanObject : Utility.fanObjectHashMap.values()) {

                    if (fanObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                        offline_cnt++;

                    } else {

                        online_cnt++;
                    }

                }

                for (CurtainObject curtainObject : Utility.curtainObjectHashMap.values()) {

                    if (curtainObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                        offline_cnt++;

                    } else {

                        online_cnt++;
                    }

                }

            }

            online_cnt_view.setText("Online : " + online_cnt);

            offline_cnt_view.setText("Offline : " + offline_cnt);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void ShowOfflineLoader() {

        try {

            if (offline_cnt > 0) {

                offline_loader.setVisibility(View.VISIBLE);

                TransactionPool.pushModuleRequest();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        offline_loader.setVisibility(View.INVISIBLE);

                    }

                }, (int) 5 * 1000);

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void setView() {

        try {

            if (view != null) {

                miniserverImageView = view.findViewById(R.id.miniserverImageView);

                miniserverImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ShowOfflineLoader();
                    }
                });

                ShowDeviceGraph();

                HomeObject homeObject = Utility.connectedHome;

                if (homeObject != null) {

                    home_name.setText(homeObject.getHome());

                } else {

                    home_name.setVisibility(View.GONE);
                }

                if (homeObject != null && homeObject.getAccessories() != null && homeObject.getAccessories().size() > 0) {

                    Utility.isMiniserverInstall = true;

                }

                if (!Utility.isHomeConnected) {

                    child_container.setVisibility(View.GONE);

                } else {

                    if (context != null && SharedPreference.getAccessorySyncFlag(context)) {

                        miniserver_layout.setVisibility(View.VISIBLE);

                        child_container.setVisibility(View.GONE);

                        error_msg.setText(context.getString(R.string.miniserver_sync_err));

                    } else if (!Utility.isMiniserverInstall) {

                        miniserver_layout.setVisibility(View.VISIBLE);

                        child_container.setVisibility(View.GONE);

                        error_msg.setText(context.getString(R.string.miniserver_installed_err));

                    } else if (!Utility.isMiniserverConnected) {

                        miniserver_layout.setVisibility(View.VISIBLE);

                        child_container.setVisibility(View.GONE);

                        error_msg.setText(context.getString(R.string.miniserver_not_connect_err));

                    } else if (Utility.isMiniserverInstall && Utility.isMiniserverConnected && !Utility.isMiniserverConnectionLost) {

                        miniserver_layout.setVisibility(View.GONE);

                        child_container.setVisibility(View.VISIBLE);

                        if (Utility.COMMUNICATION_MODE == 2 || Utility.COMMUNICATION_MODE == 3 || Utility.COMMUNICATION_MODE == 4 && context != null) {

                            miniserverImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.yellow_miniserver));

                        }

                        if (Utility.COMMUNICATION_MODE == 1 && context != null) {

                            miniserverImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.blue_miniserver));

                        }

                    } else if (Utility.isMiniserverConnectionLost) {

                        miniserver_layout.setVisibility(View.VISIBLE);

                        child_container.setVisibility(View.GONE);

                        error_msg.setText(context.getString(R.string.miniserver_connection_lost_err));

                    }
                }

                setNextScheduler();

                setModuleCnt();

                if (!Utility.isHost) {

                    miniserver_setting.setVisibility(View.INVISIBLE);
                }

                miniserver_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = new Intent().setClass(context, WiFiGestureTabContainerActivity.class);

                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.getApplicationContext().startActivity(i);
                    }
                });

                if (Utility.SCENEMap.size() == 1) {

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);

                    favourites_scene_recycler.setLayoutManager(gridLayoutManager);

                } else {

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);

                    favourites_scene_recycler.setLayoutManager(gridLayoutManager);

                }

                GridLayoutManager mGridLayoutManager = new GridLayoutManager(context, Utility.TILE_SPAN);

                favourites_accessories_recycler.setLayoutManager(mGridLayoutManager);

                List<SceneObject> SceneList = new ArrayList<>();

                for (SceneObject sceneObject : Utility.SCENEMap.values()) {

                    if (sceneObject != null) {

                        SceneList.add(sceneObject);
                    }

                }

                if (SceneList.size() == 0) {

                    sceneText.setVisibility(View.GONE);

                } else {

                    sceneText.setVisibility(View.VISIBLE);
                }

                LinkedList<UserObject> userObjectList = new LinkedList<>();

                if (Utility.CurrentUserObj != null && Utility.CurrentUserObj.getPhn().length() == 10) {

                    userObjectList.add(Utility.CurrentUserObj);

                }

                for (UserObject userObject : Utility.USERMap.values()) {

                    if (userObject != null && Utility.CurrentUserObj != null && userObject.getPhn().length() != 0 && !userObject.getPhn().equalsIgnoreCase(Utility.CurrentUserObj.getPhn())) {

                        userObjectList.add(userObject);

                    }

                }

                LinearLayoutManager HorizontalLayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

                user_recycler.setLayoutManager(HorizontalLayout);

                homeUsersAdapter = new HomeUsersAdapter(context, userObjectList);

                user_recycler.setAdapter(homeUsersAdapter);

                favouritesScenesAdapter = new FavouritesScenesAdapter(context, SceneList);

                favourites_scene_recycler.setAdapter(favouritesScenesAdapter);

                favouritesAccessoriesAdapter = new FavouritesAccessoriesAdapter(context, getFavAccessories());

                favourites_accessories_recycler.setAdapter(favouritesAccessoriesAdapter);

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public static List<Object> getFavAccessories() {

        List<Object> fav = new ArrayList<>();

        try {

            for (GenericObject genericObject : Utility.genericObjectHashMap.values()) {

                if (genericObject.getUsed().equalsIgnoreCase(UtilityConstants.STATE_TRUE) && genericObject.getFav().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    GenericObject genericObject1 = (GenericObject) genericObject.clone();

                    fav.add(genericObject1);

                }

            }
            for (PowerObject powerObject : Utility.powerObjectHashMap.values()) {

                if (powerObject.getFav().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    PowerObject powerObject1 = (PowerObject) powerObject.clone();

                    fav.add(powerObject1);

                }

            }
            for (RGBObject rgbObject : Utility.rgbObjectHashMap.values()) {

                if (rgbObject != null && rgbObject.getFav().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    RGBObject rgbObject1 = (RGBObject) rgbObject.clone();

                    fav.add(rgbObject1);

                }

            }
            for (FanObject fanObject : Utility.fanObjectHashMap.values()) {

                if (fanObject.getFav().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    FanObject fanObject1 = (FanObject) fanObject.clone();

                    fav.add(fanObject1);

                }

            }
            for (MotionObject motionObject : Utility.motionObjectHashMap.values()) {

                if (motionObject.getFav().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    MotionObject motionObject1 = (MotionObject) motionObject.clone();

                    fav.add(motionObject1);

                }
            }
            for (CurtainObject curtainObject : Utility.curtainObjectHashMap.values()) {

                if (curtainObject.getFav().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                    CurtainObject curtainObject1 = (CurtainObject) curtainObject.clone();

                    fav.add(curtainObject1);

                }
            }

            if (fav.size() == 0) {

                FavouriteText.setVisibility(View.GONE);

            } else {

                FavouriteText.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return fav;
    }

    public static void setNextScheduler() {

        Calendar calendar = null;

        String strday = null;

        String ScheduleDate = "";

        String ScheduleSplit[];

        boolean isPM;

        int day = -1;

        int currentday;

        try {

            next_schedule = view.findViewById(R.id.next_schedule);

            calendar = Calendar.getInstance();

            final Date date = calendar.getTime();

            strday = new SimpleDateFormat(UtilityConstants.EEEE, Locale.ENGLISH).format(date.getTime());

            if (strday.equalsIgnoreCase(UtilityConstants.SUNDAY)) {

                day = 1;

            } else if (strday.equalsIgnoreCase(UtilityConstants.MONDAY)) {

                day = 2;

            } else if (strday.equalsIgnoreCase(UtilityConstants.TUESDAY)) {

                day = 3;

            } else if (strday.equalsIgnoreCase(UtilityConstants.WEDNESDAY)) {

                day = 4;

            } else if (strday.equalsIgnoreCase(UtilityConstants.THURSDAY)) {

                day = 5;

            } else if (strday.equalsIgnoreCase(UtilityConstants.FRIDAY)) {

                day = 6;

            } else if (strday.equalsIgnoreCase(UtilityConstants.SATURDAY)) {

                day = 7;

            }

            currentday = day;

            TreeMap<Integer, ScheduleObject> map = new TreeMap<>();

            String str = "";

            int currentSec;

            int nextScheduleSecond;

            int i = 0;

            int key;

            ScheduleObject nextScheduleObject = null;

            for (ScheduleObject scheduleObject : Utility.SCHEDULEMap.values()) {

                try {

                    if (scheduleObject != null && scheduleObject.getDays().length() > 0) {

                        String days = "";

                        for (int index = 0; index < scheduleObject.getDays().length(); index++) {

                            String c = "" + (Integer.parseInt("" + scheduleObject.getDays().charAt(index)) + 1);

                            days = days + c;

                        }

                        ScheduleSplit = scheduleObject.getTime().split(":");

                        isPM = (Integer.parseInt(ScheduleSplit[0]) >= 12);

                        ScheduleDate = String.format("%02d:%02d %s", (Integer.parseInt(ScheduleSplit[0]) == 12 || Integer.parseInt(ScheduleSplit[0]) == 0) ? 12 : Integer.parseInt(ScheduleSplit[0]) % 12, Integer.parseInt(ScheduleSplit[1]), isPM ? "PM" : "AM");

                        if (scheduleObject.getActive().equalsIgnoreCase(UtilityConstants.STATE_TRUE) &&

                                currentday != -1 && days.contains(Integer.toString(currentday))) {

                            map.put(Integer.parseInt(getLongFromDate(new SimpleDateFormat(UtilityConstants.DDMMYYYY).format(date) + " " + ScheduleDate)), scheduleObject);

                        }

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

            if (date.getMinutes() < 10) {

                str = "" + date.getHours() + "0" + date.getMinutes();

            } else {

                str = "" + date.getHours() + date.getMinutes();

            }

            currentSec = Integer.parseInt(str);

            nextScheduleSecond = -1;

            for (Map.Entry<Integer, ScheduleObject> entry : map.entrySet()) {

                key = entry.getKey();

                if (key > currentSec) {

                    nextScheduleSecond = key;

                    break;

                } else if (i == map.size() - 1) {

                    break;

                } else if (key <= currentSec && getNextTime(map, i + 1) > currentSec) {

                    nextScheduleSecond = getNextTime(map, i + 1);

                    break;

                }

                i++;

            }

            if (nextScheduleSecond != -1) {

                nextScheduleObject = map.get(nextScheduleSecond);

                ScheduleSplit = nextScheduleObject.getTime().split(":");

                isPM = (Integer.parseInt(ScheduleSplit[0]) >= 12);

                next_schedule.setText(strday + "  " + String.format("%02d:%02d %s", (Integer.parseInt(ScheduleSplit[0]) == 12 || Integer.parseInt(ScheduleSplit[0]) == 0) ? 12 : Integer.parseInt(ScheduleSplit[0]) % 12, Integer.parseInt(ScheduleSplit[1]), isPM ? "PM" : "AM"));

            } else {

                next_schedule.setVisibility(View.GONE);

                next_schedule_txt.setVisibility(View.GONE);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static String getLongFromDate(String date_str) {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        DateFormat outputformat = new SimpleDateFormat("dd/MM/yyyy HH:mm a");

        Date date = null;

        String output = null;

        try {

            date = df.parse(date_str);

            output = outputformat.format(date);

            Date date2 = outputformat.parse(output);

            if (date2.getMinutes() < 10) {

                return "" + date2.getHours() + "0" + date2.getMinutes();

            } else {

                return "" + date2.getHours() + date2.getMinutes();

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {

            view = inflater.inflate(R.layout.home_container_fragment, null);

            child_container = (ScrollView) view.findViewById(R.id.child_container);

            parent_container = (LinearLayout) view.findViewById(R.id.parent_container);

            user_recycler = view.findViewById(R.id.user_recycler);

            miniserver_layout = (LinearLayout) view.findViewById(R.id.miniserver_layout);

            home_name = view.findViewById(R.id.home_name);

            chart = view.findViewById(R.id.chart);

            FavouriteText = view.findViewById(R.id.FavouriteText);

            sceneText = view.findViewById(R.id.sceneText);

            next_schedule_txt = view.findViewById(R.id.next_schedule_txt);

            child_container.setBackgroundResource(0);

            miniserver_layout.setBackgroundResource(0);

            parent_container.setBackgroundResource(0);

            next_schedule = view.findViewById(R.id.next_schedule);

            error_msg = view.findViewById(R.id.error_msg);

            miniserver_setting = view.findViewById(R.id.miniserver_setting);

            online_cnt_view = view.findViewById(R.id.online_cnt);

            offline_cnt_view = view.findViewById(R.id.offline_cnt);

            favourites_scene_recycler = view.findViewById(R.id.favourites_scene_recycler);

            favourites_accessories_recycler = view.findViewById(R.id.favourites_accessories_recycler);

            favourites_scene_recycler.setHasFixedSize(true);

            user_recycler.setHasFixedSize(true);

            favourites_accessories_recycler.setHasFixedSize(true);

            offline_loader = view.findViewById(R.id.offline_loader);

            context = getContext();

            chart.setOnChartValueSelectedListener(this);

            chart.animateXY(1300, 1300);

            setView();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return view;
    }

    private static void ShowDeviceGraph() {

        try {

            ArrayList<BarEntry> values = new ArrayList<>();

            DeviceAxisValueFormatter.Devices = new ArrayList<>();

            chart.getDescription().setEnabled(false);

            chart.setMaxVisibleValueCount(40);

            chart.setPinchZoom(false);

            chart.setDrawGridBackground(false);

            chart.setDrawBarShadow(false);

            chart.setDrawValueAboveBar(false);

            chart.setHighlightFullBarEnabled(false);

            IAxisValueFormatter xAxisFormatter = new DeviceAxisValueFormatter(chart);

            XAxis xAxis = chart.getXAxis();

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            xAxis.setDrawGridLines(false);

            xAxis.setGranularity(1f); // only intervals of 1 day

//            xAxis.setLabelCount(7);

            xAxis.setTextColor(context.getResources().getColor(R.color.text_white));

            YAxis leftAxis = chart.getAxisLeft();

            leftAxis.setValueFormatter(new MyAxisValueFormatter());

            leftAxis.setTextColor(context.getResources().getColor(R.color.text_white));

            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            chart.getAxisRight().setEnabled(false);

            Legend l = chart.getLegend();

            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);

            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

            l.setDrawInside(false);

            l.setFormSize(10f);

            l.setTextColor(context.getResources().getColor(R.color.text_white));

            l.setFormToTextSpace(4f);

            l.setXEntrySpace(8f);

            BarDataSet set1;

            int OnCnt = 0;

            int OffCnt = 0;

            int AcCntOn = 0;

            int AcCntOff = 0;

            int HeavyLoadOn = 0;

            int HeavyLoadOff = 0;

            if (Utility.genericObjectHashMap.size() > 0) {

                for (GenericObject genericObject : Utility.genericObjectHashMap.values()) {

                    if (genericObject.getUsed().equalsIgnoreCase("1")) {

                        if (genericObject.getLogo().equalsIgnoreCase("AC")) {

                            if (genericObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                                AcCntOn++;

                            } else {

                                AcCntOff++;
                            }

                        } else if (genericObject.getLogo().equalsIgnoreCase("Plug")) {

                            if (genericObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                                HeavyLoadOn++;

                            } else {

                                HeavyLoadOff++;
                            }

                        } else if (genericObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                            OnCnt++;

                        } else {

                            OffCnt++;

                        }
                    }
                }

                if (OnCnt > 0 || OffCnt > 0) {

                    DeviceAxisValueFormatter.Devices.add("Generic");

                    values.add(new BarEntry(
                            DeviceAxisValueFormatter.Devices.lastIndexOf("Generic"),
                            new float[]{OnCnt, OffCnt},
                            context.getResources().getDrawable(R.drawable.icon_favourite)));

                    xAxis.setValueFormatter(xAxisFormatter);
                }

            }

            if (Utility.fanObjectHashMap.size() > 0) {

                OnCnt = 0;
                OffCnt = 0;

                for (FanObject fanObject : Utility.fanObjectHashMap.values()) {

                    if (fanObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                        OnCnt++;

                    } else {

                        OffCnt++;

                    }
                }

                if (OnCnt > 0 || OffCnt > 0) {

                    DeviceAxisValueFormatter.Devices.add("Fan");

                    values.add(new BarEntry(
                            DeviceAxisValueFormatter.Devices.lastIndexOf("Fan"),
                            new float[]{OnCnt, OffCnt},
                            context.getResources().getDrawable(R.drawable.icon_favourite)));

                    xAxis.setValueFormatter(xAxisFormatter);

                }

            }

            if (Utility.powerObjectHashMap.size() > 0) {

                OnCnt = 0;
                OffCnt = 0;

                for (PowerObject powerObject : Utility.powerObjectHashMap.values()) {

                    if (powerObject.getLogo().equalsIgnoreCase("AC")) {

                        if (powerObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                            AcCntOn++;

                        } else {

                            AcCntOff++;
                        }

                    } else if (powerObject.getLogo().equalsIgnoreCase("Plug")) {

                        if (powerObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                            HeavyLoadOn++;

                        } else {

                            HeavyLoadOff++;
                        }

                    } else if (powerObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                        OnCnt++;

                    } else {

                        OffCnt++;

                    }
                }

                if (OnCnt > 0 || OffCnt > 0) {

                    DeviceAxisValueFormatter.Devices.add("Power");

                    values.add(new BarEntry(
                            DeviceAxisValueFormatter.Devices.lastIndexOf("Power"),
                            new float[]{OnCnt, OffCnt},
                            context.getResources().getDrawable(R.drawable.icon_favourite)));

                    xAxis.setValueFormatter(xAxisFormatter);

                }
            }

            if (Utility.rgbObjectHashMap.size() > 0) {

                OnCnt = 0;
                OffCnt = 0;

                for (RGBObject rgbObject : Utility.rgbObjectHashMap.values()) {

                    if (rgbObject.getState().equalsIgnoreCase(UtilityConstants.STATE_TRUE)) {

                        OnCnt++;

                    } else {

                        OffCnt++;

                    }
                }

                if (OnCnt > 0 || OffCnt > 0) {

                    DeviceAxisValueFormatter.Devices.add("RGB");

                    values.add(new BarEntry(
                            DeviceAxisValueFormatter.Devices.lastIndexOf("RGB"),
                            new float[]{OnCnt, OffCnt},
                            context.getResources().getDrawable(R.drawable.icon_favourite)));

                    xAxis.setValueFormatter(xAxisFormatter);

                }
            }

            if (HeavyLoadOn > 0 || HeavyLoadOff > 0) {

                DeviceAxisValueFormatter.Devices.add("Heavy Load");

                values.add(new BarEntry(
                        DeviceAxisValueFormatter.Devices.lastIndexOf("Heavy Load"),
                        new float[]{HeavyLoadOn, HeavyLoadOff},
                        context.getResources().getDrawable(R.drawable.icon_favourite)));

                xAxis.setValueFormatter(xAxisFormatter);
            }

            if (AcCntOn > 0 || AcCntOff > 0) {

                DeviceAxisValueFormatter.Devices.add("AC");

                values.add(new BarEntry(
                        DeviceAxisValueFormatter.Devices.lastIndexOf("AC"),
                        new float[]{AcCntOn, AcCntOff},
                        context.getResources().getDrawable(R.drawable.icon_favourite)));

                xAxis.setValueFormatter(xAxisFormatter);
            }

            xAxis.setLabelCount(values.size());

            if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {

                set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);

                set1.setValues(values);

                chart.getData().notifyDataChanged();

                chart.notifyDataSetChanged();

            } else {

                ArrayList<IBarDataSet> dataSets = new ArrayList<>();

                set1 = new BarDataSet(values, "");

                set1.setDrawIcons(false);

                set1.setColors(getColors());

                set1.setStackLabels(new String[]{"ON", "OFF"});

                dataSets.add(set1);

                BarData data = new BarData(dataSets);

                data.setValueFormatter(new MyValueFormatter());

                data.setValueTextSize(10);

                data.setValueTextColor(context.getResources().getColor(R.color.sub_text_color));

                chart.setData(data);
            }

            chart.setFitBars(true);

            chart.invalidate();

            chart.getBarData().setBarWidth(0.2f);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private static int[] getColors() {

        // have as many colors as stack-values per entry
        int[] colors = new int[2];

        int[] MATERIAL_COLORS = {
                rgb("#F7A31E"), rgb("#ffffff")
        };

        System.arraycopy(MATERIAL_COLORS, 0, colors, 0, 2);

        return colors;
    }

    private static int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }

}
