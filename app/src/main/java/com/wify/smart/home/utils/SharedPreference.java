package com.wify.smart.home.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wify.smart.home.dto.AccessoriesObject;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.HomeObject;
import com.wify.smart.home.dto.MotionObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.dto.ScheduleObject;
import com.wify.smart.home.dto.UserObject;
import com.wify.smart.home.dto.WifyThemes;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class SharedPreference {

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void clearSharedPreference(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.apply();
    }

    public static void setMQTTConnect(Context ctx, String value) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("MQTTConnect", value);

        editor.commit();

    }

    public static String getMQTTConnect(Context ctx) {

        return getSharedPreferences(ctx).getString("MQTTConnect", "");

    }

    public static void setALEXA(Context ctx, String value) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("ALEXA", value);

        editor.commit();

    }

    public static String getALEXA(Context ctx) {

        return getSharedPreferences(ctx).getString("ALEXA", "");

    }

    public static void setIP(Context ctx, String IP) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("IP", IP);

        editor.commit();

    }

    public static String getIP(Context ctx) {

        return getSharedPreferences(ctx).getString("IP", "");

    }

    public static void setKeyMap(Context ctx, HashMap<String, String> map) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("keyMap", new Gson().toJson(map));

        editor.commit();
    }

    public static HashMap<String, String> getKeyMap(Context ctx) {

        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();

        HashMap<String, String> map = new Gson().fromJson(getSharedPreferences(ctx).getString("keyMap", null), type);

        if (map == null) {
            map = new HashMap<String, String>();
        }

        return map;

    }

    public static void setHostUser(Context ctx, UserObject host) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("USER_HOST", new Gson().toJson(host));

        editor.commit();

    }

    public static UserObject getHostUser(Context ctx) {

        return new Gson().fromJson(getSharedPreferences(ctx).getString("USER_HOST", null), UserObject.class);

    }

    public static void setPhone_number(Context ctx, String number) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("USER_NUMBER", number);

        editor.commit();

    }

    public static String getPhone_number(Context ctx) {

        return getSharedPreferences(ctx).getString("USER_NUMBER", null);

    }

    public static void setConnectedHome(Context ctx, HomeObject homeObject) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("homeObject", new Gson().toJson(homeObject));

        editor.commit();

    }

    public static HomeObject getConnectedHome(Context ctx) {

        return new Gson().fromJson(getSharedPreferences(ctx).getString("homeObject", null), HomeObject.class);

    }

    public static void setUserHostedHomes(Context ctx, HashMap<String, HomeObject> homes) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("UserHostedHomes", new Gson().toJson(homes));

        editor.commit();

    }

    public static HashMap<String, HomeObject> getUserHostedHomes(Context ctx) {

        Type listType = new TypeToken<HashMap<String, HomeObject>>() {
        }.getType();

        HashMap<String, HomeObject> homes = new Gson().fromJson(getSharedPreferences(ctx).getString("UserHostedHomes", null), listType);

        if (homes == null) {
            homes = new HashMap<String, HomeObject>();
        }

        return homes;
    }

    public static void setRoomImages(Context ctx, ConcurrentHashMap<String, String> images) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("RoomImages", new Gson().toJson(images));

        editor.commit();
    }

    public static ConcurrentHashMap<String, String> getRoomImages(Context ctx) {

        Type listType = new TypeToken<ConcurrentHashMap<String, String>>() {
        }.getType();

        ConcurrentHashMap<String, String> images = new Gson().fromJson(getSharedPreferences(ctx).getString("RoomImages", null), listType);

        if (images == null) {
            images = new ConcurrentHashMap<String, String>();
        }

        return images;
    }

    public static void setUserSharedHomes(Context ctx, HashMap<String, HomeObject> homes) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("UserSharedHomes", new Gson().toJson(homes));

        editor.commit();

    }

    public static HashMap<String, HomeObject> getUserSharedHomes(Context ctx) {

        Type listType = new TypeToken<HashMap<String, HomeObject>>() {
        }.getType();

        HashMap<String, HomeObject> homes = new Gson().fromJson(getSharedPreferences(ctx).getString("UserSharedHomes", null), listType);

        if (homes == null) {
            homes = new HashMap<String, HomeObject>();
        }

        return homes;
    }

    public static void setAccessories(Context ctx, HashMap<String, AccessoriesObject> accessoriesObjectList) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("accessories", new Gson().toJson(accessoriesObjectList));

        editor.commit();
    }

    public static HashMap<String, AccessoriesObject> getAccessories(Context ctx) {

        Type type = new TypeToken<HashMap<String, AccessoriesObject>>() {
        }.getType();

        HashMap<String, AccessoriesObject> accessoriesObjects = new Gson().fromJson(getSharedPreferences(ctx).getString("accessories", null), type);

        if (accessoriesObjects == null) {
            accessoriesObjects = new HashMap<>();
        }

        return accessoriesObjects;

    }

    public static void setRooms(Context context, ConcurrentHashMap<String, RoomObject> roomObjectHashMap) {

        SharedPreferences.Editor editor = getSharedPreferences(context).edit();

        editor.putString("rooms", new Gson().toJson(roomObjectHashMap));

        editor.commit();
    }

    public static ConcurrentHashMap<String, RoomObject> getRooms(Context ctx) {

        Type type = new TypeToken<ConcurrentHashMap<String, RoomObject>>() {
        }.getType();

        ConcurrentHashMap<String, RoomObject> roomObjectHashMap = new Gson().fromJson(getSharedPreferences(ctx).getString("rooms", null), type);

        if (roomObjectHashMap == null) {
            roomObjectHashMap = new ConcurrentHashMap<>();
        }

        return roomObjectHashMap;
    }

    public static void setTheme(Context ctx, WifyThemes themes) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("THEME", new Gson().toJson(themes));

        editor.commit();
    }

    public static WifyThemes getTheme(Context ctx) {

        return new Gson().fromJson(getSharedPreferences(ctx).getString("THEME", null), WifyThemes.class);

    }

    public static void setAccessorySyncFlag(Context ctx, boolean flag) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("SyncFlag", new Gson().toJson(flag));

        editor.commit();
    }

    public static boolean getAccessorySyncFlag(Context ctx) {

        String json = getSharedPreferences(ctx).getString("SyncFlag", null);

        if (json == null) {

            return false;
        }

        return new Gson().fromJson(json, boolean.class);
    }

    public static void setUsers(Context ctx, ConcurrentHashMap<String, UserObject> map) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("users", new Gson().toJson(map));

        editor.commit();

    }

    public static ConcurrentHashMap<String, UserObject> getUsers(Context ctx) {

        Type type = new TypeToken<ConcurrentHashMap<String, UserObject>>() {
        }.getType();

        ConcurrentHashMap<String, UserObject> map = new Gson().fromJson(getSharedPreferences(ctx).getString("users", null), type);

        if (map == null) {

            map = new ConcurrentHashMap<>();

        }

        return map;

    }

    public static void setScenes(Context ctx, ConcurrentHashMap<String, SceneObject> map) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("scenes", new Gson().toJson(map));

        editor.commit();

    }

    public static ConcurrentHashMap<String, SceneObject> getScenes(Context ctx) {

        Type type = new TypeToken<ConcurrentHashMap<String, SceneObject>>() {
        }.getType();

        ConcurrentHashMap<String, SceneObject> map = new Gson().fromJson(getSharedPreferences(ctx).getString("scenes", null), type);

        if (map == null) {

            map = new ConcurrentHashMap<>();

        }

        return map;

    }

    public static void setSchedules(Context ctx, ConcurrentHashMap<String, ScheduleObject> map) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("schedules", new Gson().toJson(map));

        editor.commit();

    }

    public static ConcurrentHashMap<String, ScheduleObject> getSchedules(Context ctx) {

        Type type = new TypeToken<ConcurrentHashMap<String, ScheduleObject>>() {
        }.getType();

        ConcurrentHashMap<String, ScheduleObject> map = new Gson().fromJson(getSharedPreferences(ctx).getString("schedules", null), type);

        if (map == null) {

            map = new ConcurrentHashMap<>();

        }

        return map;

    }

    public static void setGMs(Context ctx, ConcurrentHashMap<String, GenericObject> map) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("GMs", new Gson().toJson(map));

        editor.commit();

    }

    public static ConcurrentHashMap<String, GenericObject> getGMs(Context ctx) {

        Type type = new TypeToken<ConcurrentHashMap<String, GenericObject>>() {
        }.getType();

        ConcurrentHashMap<String, GenericObject> map = new Gson().fromJson(getSharedPreferences(ctx).getString("GMs", null), type);

        if (map == null) {

            map = new ConcurrentHashMap<>();

        }

        return map;

    }

    public static void setFMs(Context ctx, ConcurrentHashMap<String, FanObject> map) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("FMs", new Gson().toJson(map));

        editor.commit();

    }

    public static ConcurrentHashMap<String, FanObject> getFMs(Context ctx) {

        Type type = new TypeToken<ConcurrentHashMap<String, FanObject>>() {
        }.getType();

        ConcurrentHashMap<String, FanObject> map = new Gson().fromJson(getSharedPreferences(ctx).getString("FMs", null), type);

        if (map == null) {

            map = new ConcurrentHashMap<>();

        }

        return map;

    }

    public static void setCMs(Context ctx, ConcurrentHashMap<String, CurtainObject> map) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("CMs", new Gson().toJson(map));

        editor.commit();

    }

    public static ConcurrentHashMap<String, CurtainObject> getCMs(Context ctx) {

        Type type = new TypeToken<ConcurrentHashMap<String, CurtainObject>>() {
        }.getType();

        ConcurrentHashMap<String, CurtainObject> map = new Gson().fromJson(getSharedPreferences(ctx).getString("CMs", null), type);

        if (map == null) {

            map = new ConcurrentHashMap<>();

        }

        return map;

    }

    public static void setRGBs(Context ctx, ConcurrentHashMap<String, RGBObject> map) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("RGBs", new Gson().toJson(map));

        editor.commit();

    }

    public static ConcurrentHashMap<String, RGBObject> getRGBs(Context ctx) {

        Type type = new TypeToken<ConcurrentHashMap<String, RGBObject>>() {
        }.getType();

        ConcurrentHashMap<String, RGBObject> map = new Gson().fromJson(getSharedPreferences(ctx).getString("RGBs", null), type);

        if (map == null) {

            map = new ConcurrentHashMap<>();

        }

        return map;

    }

    public static void setMMs(Context ctx, ConcurrentHashMap<String, MotionObject> map) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("MMs", new Gson().toJson(map));

        editor.commit();

    }

    public static ConcurrentHashMap<String, MotionObject> getMMs(Context ctx) {

        Type type = new TypeToken<ConcurrentHashMap<String, MotionObject>>() {
        }.getType();

        ConcurrentHashMap<String, MotionObject> map = new Gson().fromJson(getSharedPreferences(ctx).getString("MMs", null), type);

        if (map == null) {

            map = new ConcurrentHashMap<>();

        }

        return map;

    }

    public static void setPMs(Context ctx, ConcurrentHashMap<String, PowerObject> map) {

        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();

        editor.putString("PMs", new Gson().toJson(map));

        editor.commit();

    }

    public static ConcurrentHashMap<String, PowerObject> getPMs(Context ctx) {

        Type type = new TypeToken<ConcurrentHashMap<String, PowerObject>>() {
        }.getType();

        ConcurrentHashMap<String, PowerObject> map = new Gson().fromJson(getSharedPreferences(ctx).getString("PMs", null), type);

        if (map == null) {

            map = new ConcurrentHashMap<>();

        }

        return map;

    }

}