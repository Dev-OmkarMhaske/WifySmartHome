package com.wify.smart.home.mqtt;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wify.smart.home.activities.SplashScreenActivity;
import com.wify.smart.home.dto.AccessoriesObject;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.DimmerObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.GestureObject;
import com.wify.smart.home.dto.MotionObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.dto.ScheduleObject;
import com.wify.smart.home.dto.UserObject;
import com.wify.smart.home.home.HomeContainerFragment;
import com.wify.smart.home.home.MainActivity;
import com.wify.smart.home.mqtt.dto.TransactionObject;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;

public class MqttClient {

    public static String HOMEUID;

    public static Context globalContext = null;

    public static MqttAndroidClient mqttAndroidClient = null;

    public static boolean gotoMainActivityFlag = false;

    public static boolean isHOMEUISubScribe;

    private static String Miniserver_response = null;

    private static Thread pulseThread = null;

    private static MqttConnectOptions mqttConnectOptions = null;

    public static void pulseStart() {

        try {


            System.out.println("........................... pulseStart ........................");

            pulseThread = new Thread(new Runnable() {

                @Override
                public void run() {

                    try {

                        while (!Utility.isMiniserverConnectionLost) {

                            if (Utility.isPulsePublish) {

                                System.out.println("........................... connection lost ........................");

                                Utility.isMiniserverConnectionLost = true;

                                pulseThread.interrupt();

                                gotoMainActivity();

                            }

                            if (!Utility.isMiniserverConnectionLost) {

                                Utility.isPulsePublish = true;

                                publishMessage(HOMEUID, MqttOperation.getTransactionObject(UtilityConstants.PULSE_TRANSACTION, ""));

                                if (!pulseThread.isInterrupted()) {

                                    pulseThread.sleep(5000);

                                }

                            }

                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                }

            });

            pulseThread.start();


        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void publishMessage(String PUB_TOPIC, String payload) {

        try {

            String OriginalPayload = payload;

            System.out.println(">>> PUB_TOPIC >>" + PUB_TOPIC + "  payload >>" + payload);

            payload = CustomEncryption.encode(payload);

            if (mqttAndroidClient != null) {

                if (!mqttAndroidClient.isConnected()) {

                    mqttAndroidClient.connect();

                } else {

                    MqttMessage message = new MqttMessage();

                    message.setPayload(payload.getBytes());

                    message.setQos(0);

                    mqttAndroidClient.publish(PUB_TOPIC, message, null, new IMqttActionListener() {
                        @Override

                        public void onSuccess(IMqttToken asyncActionToken) {

                            Log.i(TAG, "publish succeed!");

                            if (OriginalPayload.contains(UtilityConstants.CLIENT_ID_ADD)) {

                            }

                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                            Log.i(TAG, "publish failed! Restart Application");

                            if (OriginalPayload.contains(UtilityConstants.CLIENT_ID_ADD)) {

                            }

                        }
                    });

                }
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    /**
     * Subscribe to a specific topic
     *
     * @param topic mqtt topic
     */
    public static void subscribeTopic(String topic, String type) {

        try {

            mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    Log.i(TAG, "subscribed succeed");

                    if (type.equalsIgnoreCase(UtilityConstants.Miniserver_response)) {

                        isHOMEUISubScribe = true;

                    } else if (isHOMEUISubScribe) {

                        if (SharedPreference.getMQTTConnect(globalContext).equalsIgnoreCase(UtilityConstants.TRUE_TXT)) {

                            publishReadyData();

                        }
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    Log.i(TAG, "subscribed failed");

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void publishReadyData() {

        Context context = globalContext;

        long start = System.currentTimeMillis();

        Log.i(TAG, "connect succeed >> " + (System.currentTimeMillis() - start));

        System.out.println(">>>>publish success >>>");

        System.out.println("-------------------------- Loading Stored Data ---------------------------");

        ConcurrentHashMap<String, UserObject> users = SharedPreference.getUsers(context);

        if (users != null && SharedPreference.getPhone_number(context) != null
                && users.containsKey(UtilityConstants.USER + SharedPreference.getPhone_number(context))) {

            Utility.CurrentUserObj = users.get(UtilityConstants.USER + SharedPreference.getPhone_number(context));

        }

        if (SplashScreenActivity.allowReadyCall) {

            Utility.SCENEMap = SharedPreference.getScenes(context);

            Utility.SCHEDULEMap = SharedPreference.getSchedules(context);

            Utility.ROOMMap = SharedPreference.getRooms(context);

            Utility.USERMap = SharedPreference.getUsers(context);

            Utility.AccessoriesMap = SharedPreference.getAccessories(context);

            Utility.genericObjectHashMap = SharedPreference.getGMs(context);

            Utility.fanObjectHashMap = SharedPreference.getFMs(context);

            Utility.curtainObjectHashMap = SharedPreference.getCMs(context);

            Utility.rgbObjectHashMap = SharedPreference.getRGBs(context);

            Utility.motionObjectHashMap = SharedPreference.getMMs(context);

            Utility.powerObjectHashMap = SharedPreference.getPMs(context);

            HashMap<String, String> storedKeyMap = SharedPreference.getKeyMap(MqttClient.globalContext);

            System.out.println("Offline storedKeyMap >>>" + storedKeyMap);

            Utility.setOfflineToState();

            publishMessage(HOMEUID, MqttOperation.getTransactionObject(UtilityConstants.READY_TRANSACTION, SharedPreference.getPhone_number(context.getApplicationContext())));

        }
    }

    public void executeTransaction(TransactionObject transactionObject) {

        if (transactionObject.getTransID() != null && transactionObject.getTransID().length() > 0) {

            //TransactionPool.completeTransaction(transactionObject.getTransID());

        }

        if (transactionObject.getKey().equalsIgnoreCase(UtilityConstants.READY_TRANSACTION)) {

            SplashScreenActivity.allowReadyCall = false;

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    SharedPreference.setMQTTConnect(globalContext, UtilityConstants.FALSE_TXT);

                    if (SharedPreference.getAccessorySyncFlag(globalContext)) {

                        Utility.checkForAccessoryUpdate(globalContext);

                    }

                    Utility.isMiniserverConnectionLost = false;

                    Utility.isMiniserverConnected = true;

                    Utility.isMiniserverInstall = true;

                    if (Utility.isHost) {

                        SetTimeToMiniserver();
                    }

                    if (!gotoMainActivityFlag) {

                        System.out.println(">>>>>> MQTT CLIENT gotoMainActivity >>>>>>>");

                        gotoMainActivityFlag = true;

                        gotoMainActivity();
                    }

                    TransactionPool.transactionPoolStart();

                    //pulseStart();
                }

            }, 0);

        }

        if (transactionObject.getData().contains(UtilityConstants.PULSE_TRANSACTION)) {

            Utility.isMiniserverConnected = true;

            Utility.isMiniserverInstall = true;

        }

        if (transactionObject.getKey().contains(UtilityConstants.KEYMAP)) {

            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();

            Utility.KEYMAP = new Gson().fromJson(transactionObject.getData(), type);

            if (Utility.KEYMAP == null) {

                Utility.KEYMAP = SharedPreference.getKeyMap(globalContext);

            }

            Utility.synchKeyMap(globalContext);

            SharedPreference.setKeyMap(globalContext, Utility.KEYMAP);

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().contains(UtilityConstants.MINISERVER_VERSION)) {

            Utility.MINISERVER_VERSION = transactionObject.getData();

        } else if (transactionObject.getKey().contains(UtilityConstants.ESPNOW)) {

            Utility.ESPNOW = transactionObject.getData();

        } else if (transactionObject.getKey().contains(UtilityConstants.IP)) {

            Utility.IP = transactionObject.getData();

            SharedPreference.setIP(globalContext, transactionObject.getData());

        } else if (transactionObject.getKey().equalsIgnoreCase(UtilityConstants.GATEWAY)) {

            Utility.GATEWAY = transactionObject.getData();

        } else if (transactionObject.getKey().equalsIgnoreCase(UtilityConstants.STASSID)) {

            Utility.STASSID = transactionObject.getData();

        } else if (transactionObject.getKey().contains(UtilityConstants.STAPWD)) {

            Utility.STAPWD = transactionObject.getData();

        } else if (transactionObject.getKey().contains(UtilityConstants.GESTURE)) {

            Utility.GestureMap = new HashMap<>();

            for (String s : transactionObject.getData().split(",")) {

                GestureObject gestureObject = new GestureObject();

                gestureObject.setScene(s.split("#")[1]);

                gestureObject.setName(s.split("#")[0]);

                if (Utility.GetGestureLogo(gestureObject.getName()) != 0) {

                    gestureObject.setIcon(Utility.GetGestureLogo(gestureObject.getName()));

                }

                Utility.GestureMap.put(s, gestureObject);

            }

        } else if (transactionObject.getKey().contains(UtilityConstants.MQTT)) {

            Utility.MQTT = transactionObject.getData();

            if (Utility.connectedHome != null) {
                Utility.connectedHome.setMqtt(Utility.MQTT);
            }

            Utility.CheckForMQTTVersion();

        } else if (transactionObject.getKey().contains(UtilityConstants.HOMEUID)) {

            Utility.HOMEUID = transactionObject.getData();

        } else if (transactionObject.getKey().contains(UtilityConstants.HOME)) {

            Utility.HOME = transactionObject.getData();

        } else if (transactionObject.getKey().contains(UtilityConstants.DATE)) {

            Utility.DATE = transactionObject.getData();

        } else if (transactionObject.getKey().contains(UtilityConstants.CLIENT_IDs)) {

            Utility.CLIENT_IDs = transactionObject.getData();

        } else if (transactionObject.getKey().contains(UtilityConstants.ACCESSORY)) {

            if (transactionObject.getData().trim().length() != 0 && !transactionObject.getData().equalsIgnoreCase(UtilityConstants.NOT_FOUND)) {

                try {

                    Type type1 = new TypeToken<HashMap<String, AccessoriesObject>>() {
                    }.getType();

                    Utility.AccessoriesMap = new Gson().fromJson(transactionObject.getData(), type1);

                    Utility.checkForAccessoryUpdate(globalContext.getApplicationContext());

                    SharedPreference.setAccessories(globalContext, Utility.AccessoriesMap);

                } catch (Exception e) {

                    e.printStackTrace();

                }

            } else {

                Utility.checkForAccessoryUpdate(globalContext.getApplicationContext());

            }

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().contains(UtilityConstants.SCENE) && transactionObject.getData().contains(UtilityConstants.SCENE)) {

            if (transactionObject.getData().equalsIgnoreCase(UtilityConstants.NOT_FOUND) && Utility.SCENEMap.containsKey(transactionObject.getKey())) {

                Utility.SCENEMap.remove(transactionObject.getKey());

            } else {

                SceneObject sceneObject = new Gson().fromJson(transactionObject.getData(), SceneObject.class);

                Utility.SCENEMap.put(sceneObject.getFile(), sceneObject);

                Utility.SyncUserAccessControlData();

            }

            SharedPreference.setScenes(globalContext, Utility.SCENEMap);

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().contains(UtilityConstants.ROOM) && transactionObject.getData().contains(UtilityConstants.ROOM)) {

            if (transactionObject.getData().contains(UtilityConstants.NOT_FOUND) && Utility.ROOMMap.containsKey(transactionObject.getKey())) {

                Utility.ROOMMap.remove(transactionObject.getKey());

            } else {

                RoomObject roomObject = new Gson().fromJson(transactionObject.getData(), RoomObject.class);

                Utility.ROOMMap.put(roomObject.getFile(), roomObject);

                Utility.SyncUserAccessControlData();

            }

            Utility.SynchRoom();

            SharedPreference.setRooms(globalContext, Utility.ROOMMap);

        } else if (transactionObject.getKey().contains(UtilityConstants.PLAY_SCENE)) {

            if (transactionObject.getData().contains(UtilityConstants.OK_TXT) && Utility.SCENEMap.containsKey(transactionObject.getData().split(":")[0])) {

                SceneObject sceneObject = Utility.SCENEMap.get(transactionObject.getData().split(":")[0]);

                for (String s : sceneObject.getData().split(",")) {

                    if (Utility.rgbObjectHashMap.containsKey(s.split("@")[0]) && !Utility.rgbObjectHashMap.get(s.split("@")[0]).getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                        RGBObject rgbObject = Utility.rgbObjectHashMap.get(s.split("@")[0]);

                        rgbObject.setAutomationDataByType(s, rgbObject);

                        Utility.actionOnObserverField(rgbObject.getMac());

                        break;
                    }
                }

            }

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().equalsIgnoreCase(UtilityConstants.PLAY_SCHEDULE)) {

            if (transactionObject.getData().contains(UtilityConstants.OK_TXT) && Utility.SCHEDULEMap.containsKey(transactionObject.getData().split(":")[0])) {

                ScheduleObject scheduleObject = Utility.SCHEDULEMap.get(transactionObject.getData().split(":")[0]);

                for (String s : scheduleObject.getData().split(",")) {

                    if (Utility.rgbObjectHashMap.containsKey(s.split("@")[0]) && !Utility.rgbObjectHashMap.get(s.split("@")[0]).getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                        RGBObject rgbObject = Utility.rgbObjectHashMap.get(s.split("@")[0]);

                        rgbObject.setAutomationDataByType(s, rgbObject);

                        Utility.actionOnObserverField(rgbObject.getMac());

                        break;
                    }
                }

            }

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().contains(UtilityConstants.SCHEDULE) && transactionObject.getData().contains(UtilityConstants.SCHEDULE)) {

            if (transactionObject.getData().contains(UtilityConstants.NOT_FOUND) && Utility.SCHEDULEMap.containsKey(transactionObject.getKey())) {

                Utility.SCHEDULEMap.remove(transactionObject.getKey());

            } else {

                ScheduleObject scheduleObject = new Gson().fromJson(transactionObject.getData(), ScheduleObject.class);

                Utility.SCHEDULEMap.put(scheduleObject.getFile(), scheduleObject);

            }

            SharedPreference.setSchedules(globalContext, Utility.SCHEDULEMap);

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().contains(UtilityConstants.USER) && transactionObject.getData().contains(UtilityConstants.USER)) {

            if (transactionObject.getData().contains(UtilityConstants.NOT_FOUND) && Utility.USERMap.containsKey(transactionObject.getKey())) {

                Utility.USERMap.remove(transactionObject.getKey());

            } else {

                UserObject userObject = new Gson().fromJson(transactionObject.getData(), UserObject.class);

                if (userObject.getPhn().length() != 0) {

                    if (userObject.getPhn().equalsIgnoreCase(SharedPreference.getPhone_number(globalContext))) {

                        Utility.CurrentUserObj = userObject;

                    }

                    Utility.SyncUserAccessControlData();

                    Utility.USERMap.put(userObject.getFile(), userObject);

                }

            }

            SharedPreference.setUsers(globalContext, Utility.USERMap);

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().contains("GM-")) {

            if (transactionObject.getData().contains(UtilityConstants.NOT_FOUND)) {

                for (GenericObject genericObject : Utility.genericObjectHashMap.values()) {

                    if (genericObject.getFilename().equalsIgnoreCase(transactionObject.getKey())) {

                        Utility.genericObjectHashMap.remove(genericObject.getMac() + ":" + genericObject.getPoint());

                        break;
                    }
                }

            } else {

                GenericObject genericObject = new Gson().fromJson(transactionObject.getData(), GenericObject.class);

                if (Utility.genericObjectHashMap.containsKey(genericObject.getMac() + ":" + genericObject.getPoint())) {

                    genericObject.setState(Utility.genericObjectHashMap.get(genericObject.getMac() + ":" + genericObject.getPoint()).getState());

                }

                Utility.genericObjectHashMap.put(genericObject.getMac() + ":" + genericObject.getPoint(), genericObject);

                Utility.actionOnObserverField(genericObject.getMac() + ":" + genericObject.getPoint());

                Utility.SyncUserAccessControlData();

            }

            SharedPreference.setGMs(globalContext, Utility.genericObjectHashMap);

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().contains("CM-")) {

            if (transactionObject.getData().contains(UtilityConstants.NOT_FOUND)) {

                for (CurtainObject curtainObject : Utility.curtainObjectHashMap.values()) {

                    if (curtainObject.getFilename().equalsIgnoreCase(transactionObject.getKey())) {

                        Utility.curtainObjectHashMap.remove(curtainObject.getMac());

                        break;
                    }
                }

            } else {

                CurtainObject curtainObject = new Gson().fromJson(transactionObject.getData(), CurtainObject.class);

                if (Utility.curtainObjectHashMap.containsKey(curtainObject.getMac())) {

                    curtainObject.setState(Utility.curtainObjectHashMap.get(curtainObject.getMac()).getState());

                }

                Utility.curtainObjectHashMap.put(curtainObject.getMac(), curtainObject);

                Utility.SyncUserAccessControlData();

                Utility.actionOnObserverField(curtainObject.getMac());

            }

            SharedPreference.setCMs(globalContext, Utility.curtainObjectHashMap);

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().contains("FM-")) {

            if (transactionObject.getData().contains(UtilityConstants.NOT_FOUND)) {

                for (FanObject fanObject : Utility.fanObjectHashMap.values()) {

                    if (fanObject.getFilename().equalsIgnoreCase(transactionObject.getKey())) {

                        Utility.fanObjectHashMap.remove(fanObject.getMac());

                        break;
                    }
                }

            } else {

                FanObject fanObject = new Gson().fromJson(transactionObject.getData(), FanObject.class);

                if (Utility.fanObjectHashMap.containsKey(fanObject.getMac())) {

                    fanObject.setState(Utility.fanObjectHashMap.get(fanObject.getMac()).getState());

                }

                Utility.fanObjectHashMap.put(fanObject.getMac(), fanObject);

                Utility.SyncUserAccessControlData();

                Utility.actionOnObserverField(fanObject.getMac());

            }

            SharedPreference.setFMs(globalContext, Utility.fanObjectHashMap);

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().contains("PM-")) {

            if (transactionObject.getData().contains(UtilityConstants.NOT_FOUND)) {

                for (PowerObject powerObject : Utility.powerObjectHashMap.values()) {

                    if (powerObject.getFilename().equalsIgnoreCase(transactionObject.getKey())) {

                        Utility.powerObjectHashMap.remove(powerObject.getMac());

                        break;
                    }
                }

            } else {

                PowerObject powerObject = new Gson().fromJson(transactionObject.getData(), PowerObject.class);

                if (Utility.powerObjectHashMap.containsKey(powerObject.getMac())) {

                    powerObject.setState(Utility.powerObjectHashMap.get(powerObject.getMac()).getState());

                }

                Utility.powerObjectHashMap.put(powerObject.getMac(), powerObject);

                Utility.SyncUserAccessControlData();

                Utility.actionOnObserverField(powerObject.getMac());

            }

            SharedPreference.setPMs(globalContext, Utility.powerObjectHashMap);

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().contains("RGB-")) {

            if (transactionObject.getData().contains(UtilityConstants.NOT_FOUND)) {

                for (RGBObject rgbObject : Utility.rgbObjectHashMap.values()) {

                    if (rgbObject.getFilename().equalsIgnoreCase(transactionObject.getKey())) {

                        Utility.rgbObjectHashMap.remove(rgbObject.getMac());
                    }
                }

            } else {

                RGBObject rgbObject = new Gson().fromJson(transactionObject.getData(), RGBObject.class);

                if (Utility.rgbObjectHashMap.containsKey(rgbObject.getMac())) {

                    rgbObject.setState(Utility.rgbObjectHashMap.get(rgbObject.getMac()).getState());

                }

                Utility.rgbObjectHashMap.put(rgbObject.getMac(), rgbObject);

                Utility.SyncUserAccessControlData();

                Utility.actionOnObserverField(rgbObject.getMac());

            }

            SharedPreference.setRGBs(globalContext, Utility.rgbObjectHashMap);

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().contains("MOTION-")) {

            if (transactionObject.getData().contains(UtilityConstants.NOT_FOUND)) {

                for (MotionObject motionObject : Utility.motionObjectHashMap.values()) {

                    if (motionObject.getFilename().equalsIgnoreCase(transactionObject.getKey())) {

                        Utility.motionObjectHashMap.remove(motionObject.getMac());

                        break;
                    }
                }

            } else {

                MotionObject motionObject = new Gson().fromJson(transactionObject.getData(), MotionObject.class);

                if (Utility.motionObjectHashMap.containsKey(motionObject.getMac())) {

                    motionObject.setActive(Utility.motionObjectHashMap.get(motionObject.getMac()).getActive());

                }

                Utility.motionObjectHashMap.put(motionObject.getMac(), motionObject);

                Utility.SyncUserAccessControlData();

                Utility.actionOnObserverField(motionObject.getMac());

            }

            SharedPreference.setMMs(globalContext, Utility.motionObjectHashMap);

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().equalsIgnoreCase(UtilityConstants.GENERIC_EVENT_DATA) || transactionObject.getKey().equalsIgnoreCase(UtilityConstants.GENERIC_MODULE_DATA)) {

            if (transactionObject.getKey().contains(UtilityConstants.GENERIC_EVENT_DATA)) {
                System.out.println("Manual event detected on GENERIC");

            }

            String split[] = transactionObject.getData().split("@");

            //195@0@0@@0@00@0

            if (Utility.genericObjectHashMap.containsKey(split[0] + ":1")) {

                GenericObject genericObject1 = Utility.genericObjectHashMap.get(split[0] + ":1");

                genericObject1.setFreeze(split[1]);

                genericObject1.setState(split[2]);

                Utility.genericObjectHashMap.put(split[0] + ":1", genericObject1);

                Utility.actionOnObserverField(split[0] + ":1");

            }

            if (Utility.genericObjectHashMap.containsKey(split[0] + ":2")) {

                GenericObject genericObject2 = Utility.genericObjectHashMap.get(split[0] + ":2");

                genericObject2.setFreeze(split[3]);

                genericObject2.setState(split[4]);

                Utility.genericObjectHashMap.put(split[0] + ":2", genericObject2);

                Utility.actionOnObserverField(split[0] + ":2");

            }

            if (Utility.genericObjectHashMap.containsKey(split[0] + ":3")) {

                GenericObject genericObject3 = Utility.genericObjectHashMap.get(split[0] + ":3");

                genericObject3.setFreeze(split[5]);

                genericObject3.setState(split[6]);

                Utility.genericObjectHashMap.put(split[0] + ":3", genericObject3);

                Utility.actionOnObserverField(split[0] + ":3");

            }

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().equalsIgnoreCase(UtilityConstants.CONTROLLER_GENERIC_EVENT_DATA)) {

            if (transactionObject.getKey().contains(UtilityConstants.GENERIC_EVENT_DATA)) {
                System.out.println("Manual event detected on GENERIC");

            }

            String split[] = transactionObject.getData().split("@");

            //fakeMAC@point@freeze@state

            if (Utility.genericObjectHashMap.containsKey(split[0] + ":" + split[1])) {

                GenericObject genericObject1 = Utility.genericObjectHashMap.get(split[0] + ":" + split[1]);

                genericObject1.setFreeze(split[2]);

                genericObject1.setState(split[3]);

                Utility.genericObjectHashMap.put(split[0] + ":" + split[1], genericObject1);

                Utility.actionOnObserverField(split[0] + ":" + split[1]);

                HomeContainerFragment.setView();

            }

        } else if (transactionObject.getKey().equalsIgnoreCase(UtilityConstants.CONTROLLER_MODULE_DATA)) {

            try {

                String split[] = transactionObject.getData().split("@");

                String Mac = split[0];

                String GPs = split[1];

                String FPs = split[2];

//              4033@1111111@10

                for (GenericObject genericObject : Utility.genericObjectHashMap.values()) {

                    if (GPs.length() == 7 && genericObject.getMac().equalsIgnoreCase(Mac)) {

                        genericObject.setState("" + GPs.charAt(Integer.parseInt(genericObject.getPoint()) - 1));

                        Utility.actionOnObserverField(Mac + ":" + genericObject.getPoint());

                    }

                }

//            freeze@state@speed

                for (FanObject fanObject : Utility.fanObjectHashMap.values()) {

                    if (FPs.length() == 2 && fanObject.getMac().contains(Mac)) {

                        if (fanObject.getPoint().equalsIgnoreCase("F1")) {

                            fanObject.setState("" + FPs.charAt(0));

                            fanObject.setSpeed("" + FPs.charAt(1));

                        }

                        Utility.actionOnObserverField(fanObject.getMac());

                    }
                }

                HomeContainerFragment.setView();

            } catch (Exception e) {

                e.printStackTrace();
            }

        } else if (transactionObject.getKey().equalsIgnoreCase(UtilityConstants.POWER_EVENT_DATA) || transactionObject.getKey().equalsIgnoreCase(UtilityConstants.POWER_MODULE_DATA)) {

            if (transactionObject.getKey().contains(UtilityConstants.POWER_EVENT_DATA)) {
                System.out.println("Manual event detected on POWER");
            }
            //mac@freeze@state

            String split[] = transactionObject.getData().split("@");

            PowerObject powerObject = Utility.powerObjectHashMap.get(split[0]);

            powerObject.setAutomationData(transactionObject.getData());

            Utility.powerObjectHashMap.put(split[0], powerObject);

            Utility.actionOnObserverField(split[0]);

            System.out.println("update Power Object>>>" + powerObject.toString());

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().equalsIgnoreCase(UtilityConstants.RGB_EVENT_DATA) || transactionObject.getKey().equalsIgnoreCase(UtilityConstants.RGB_MODULE_DATA)) {

            if (transactionObject.getKey().contains(UtilityConstants.RGB_EVENT_DATA)) {
                System.out.println("Manual event detected on RGB");
            }

            //mac@freeze@state@brightness_function@brightness_master@function_name@mode@rgb@Led_count

            String split[] = transactionObject.getData().split("@");

            RGBObject rgbObject = Utility.rgbObjectHashMap.get(split[0]);

            rgbObject.setState(split[1]);

           /* if (split.length < 11) {

                rgbObject.setAutomationData(transactionObject.getData(), rgbObject);

            }*/

            Utility.rgbObjectHashMap.put(split[0], rgbObject);

            Utility.actionOnObserverField(split[0]);

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().equalsIgnoreCase(UtilityConstants.CURTAIN_EVENT_DATA) || transactionObject.getKey().equalsIgnoreCase(UtilityConstants.CURTAIN_MODULE_DATA)) {

            if (transactionObject.getKey().contains(UtilityConstants.CURTAIN_EVENT_DATA)) {
                System.out.println("Manual event detected on CURTAIN");
            }

            //mac@state

            String split[] = transactionObject.getData().split("@");

            CurtainObject curtainObject = Utility.curtainObjectHashMap.get(split[0]);

            curtainObject.setAutomationData(transactionObject.getData());

            Utility.curtainObjectHashMap.put(split[0], curtainObject);

            Utility.actionOnObserverField(split[0]);

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().equalsIgnoreCase(UtilityConstants.MOTION_EVENT_DATA) || transactionObject.getKey().equalsIgnoreCase(UtilityConstants.MOTION_MODULE_DATA)) {

            if (transactionObject.getKey().contains(UtilityConstants.MOTION_EVENT_DATA)) {
                System.out.println("Manual event detected on MOTION");
            }

            //mac@active@period

            String split[] = transactionObject.getData().split("@");

            MotionObject motionObject = Utility.motionObjectHashMap.get(split[0]);

            motionObject.setAutomationData(transactionObject.getData());

            Utility.motionObjectHashMap.put(split[0], motionObject);

            Utility.actionOnObserverField(split[0]);

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().equalsIgnoreCase(UtilityConstants.FAN_EVENT_DATA) || transactionObject.getKey().equalsIgnoreCase(UtilityConstants.FAN_MODULE_DATA)) {

            if (transactionObject.getKey().contains(UtilityConstants.FAN_EVENT_DATA)) {
                System.out.println("Manual event detected on FAN");
            }

            //mac@point@freeze@state@speed

            String split[] = transactionObject.getData().split("@");

            FanObject fanObject = Utility.fanObjectHashMap.get(split[0]);

            fanObject.setAutomationData(transactionObject.getData());

            Utility.fanObjectHashMap.put(split[0], fanObject);

            Utility.actionOnObserverField(split[0]);

            HomeContainerFragment.setView();

        } else if (transactionObject.getKey().equalsIgnoreCase(UtilityConstants.DIMMER_EVENT_DATA) || transactionObject.getKey().equalsIgnoreCase(UtilityConstants.DIMMER_MODULE_DATA)) {

            DimmerObject dimmerObject = new DimmerObject(Utility.getDimmerDataMap(transactionObject.getData()));

            Utility.dimmerObjectHashMap.put(dimmerObject.getMac(), dimmerObject);

        }

        Utility.reload.set("" + System.currentTimeMillis());

        Utility.reload.notifyChange();

        System.out.println("--------- transaction END ----------");

    }

    public void connect(Context context, String FinalHOMEUID, String host) {

        try {

            globalContext = context;

            HOMEUID = FinalHOMEUID;

            Miniserver_response = HOMEUID + "_response";

            mqttConnectOptions = new MqttConnectOptions();

            mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);

            mqttConnectOptions.setAutomaticReconnect(true);

            mqttConnectOptions.setCleanSession(true);

            mqttAndroidClient = new MqttAndroidClient(context, host, SharedPreference.getPhone_number(context.getApplicationContext()), Ack.AUTO_ACK);

            mqttAndroidClient.setCallback(new MqttCallbackExtended() {

                @Override
                public void connectComplete(boolean reconnect, String serverURI) {

                    System.out.println(">>> connectComplete >>>>");

                    /* Mqtt build connection */
                    mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {

                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {

                            subscribeTopic(Miniserver_response, UtilityConstants.Miniserver_response);

                            subscribeTopic(SharedPreference.getPhone_number(context.getApplicationContext()), "Phone_number");

                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                            Log.i(TAG, "connect failed");

                        }

                    });

                }

                @Override
                public void connectionLost(Throwable cause) {

                    //connect(context, HOMEUID, host);

                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    System.out.println("----------- messageArrived START ----------");

                    Log.i(TAG, "topic: " + topic + ", data: " + new String(message.getPayload()));

                    if (Utility.isMiniserverConnectionLost) {

                        Utility.isMiniserverConnectionLost = false;

                    }

                    Type type = new TypeToken<HashMap<String, String>>() {
                    }.getType();

                    HashMap<String, String> map = new Gson().fromJson(new String(message.getPayload()), type);

                    if (map.containsKey("3")) {

                        map.put("3", CustomEncryption.decode(map.get("3")));

                    }

                    System.out.println(">>>>> map >>>>" + new Gson().toJson(map));

                    if (map.get("5").equalsIgnoreCase(UtilityConstants.BULKMMD) && map.get("3").contains("#")) {

                        String data[] = map.get("3").split("#");

                        for (int i = 0; i < data.length; i++) {

                            if (data[i].length() > 0 && data[i].contains("!")) {

                                String subData[] = data[i].split("!");

                                TransactionObject transactionObject = new TransactionObject(subData[0], subData[1], subData[1], subData[2]);

                                System.out.println(">>>>> transactionObject  obj >>>" + transactionObject.toString());

                                executeTransaction(transactionObject);

                            }

                        }

                    } else if (map.get("5").equalsIgnoreCase(UtilityConstants.READY_DATA) || map.containsKey("5") && map.get("5").equalsIgnoreCase(UtilityConstants.DATA)) {

                        HashMap<String, String> hashMap = new Gson().fromJson(map.get("3"), type);

                        for (Map.Entry<String, String> entry : hashMap.entrySet()) {

                            if (entry.getKey() != null && entry.getValue() != null && entry.getKey().length() > 0 && entry.getValue().length() > 0 && !entry.getValue().contains("#@#@#")) {

                                TransactionObject transactionObject = new TransactionObject(map.get("0"), map.get("1"), entry.getKey(), entry.getValue());

                                executeTransaction(transactionObject);

                            }

                        }

                    } else {

                        TransactionObject transactionObject = new TransactionObject(map.get("0"), map.get("1"), map.get("5"), map.get("3"));

                        executeTransaction(transactionObject);

                    }

                    System.out.println("----------- messageArrived END ----------");

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                    Log.i(TAG, "msg delivered");

                }
            });

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void initializeVariable() {

        mqttAndroidClient = null;

        HOMEUID = null;

        Miniserver_response = null;

        globalContext = null;

        pulseThread = null;

        mqttConnectOptions = null;

    }

    public MqttAndroidClient getInstance(Context context, String host) {

        try {

            if (mqttAndroidClient == null || !mqttAndroidClient.getServerURI().equalsIgnoreCase(host)) {

                mqttAndroidClient = new MqttAndroidClient(context, host, SharedPreference.getPhone_number(context.getApplicationContext()), Ack.AUTO_ACK);

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return mqttAndroidClient;
    }

    public static void gotoMainActivity() {

        try {

            System.out.println(">>>>> gotoMainActivity >>>>>>");

            Intent intent = new Intent(globalContext, MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            globalContext.startActivity(intent);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void SetTimeToMiniserver() {

        try {

            String currentDate[] = Utility.getCurrentDate().split("#");

            String dateParam = currentDate[0] + "," + currentDate[1] + "," + currentDate[2] + "," + currentDate[3] + "," + currentDate[4] + "," + currentDate[5];

            publishMessage(HOMEUID, MqttOperation.getTransactionObject(UtilityConstants.SET_TIME, dateParam));

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}
