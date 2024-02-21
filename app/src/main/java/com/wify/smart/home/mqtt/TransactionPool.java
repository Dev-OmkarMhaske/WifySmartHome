package com.wify.smart.home.mqtt;

import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.MotionObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.mqtt.dto.TransactionObject;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

public class TransactionPool {

    public static HashMap<Long, TransactionObject> transactionMap = new HashMap<>();

    public static long transactionTimeout = 2000;

    public static int TimeoutRequest = 0;

    public static void transactionPoolStart() {

        try {

            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    checkTransaction();

                }
            }, 0, transactionTimeout);

            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    try {

/*
                        if (Utility.COMMUNICATION_MODE != 1 && Utility.isMiniserverInstall) {

                            if (MqttClient.mqttAndroidClient != null && !MqttClient.mqttAndroidClient.isConnected() && Utility.isMiniserverConnected) {

                                Utility.isMiniserverConnected = false;

                                Utility.isMiniserverConnectionLost = true;

                                MqttClient.gotoMainActivity();

                            } else if (!Utility.isMiniserverConnected || Utility.isMiniserverConnectionLost) {

                                Intent intent = new Intent(MqttClient.globalContext, LoadingActivity.class);

                                intent.putExtra(UtilityConstants.HOME_OBJ, Utility.connectedHome);

                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                MqttClient.globalContext.startActivity(intent);

                            }
                        }
*/

                        SendClientIdPulse();

                    } catch (Exception e) {

                        e.printStackTrace();

                    }
                }
            }, 0, 30000);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void SendClientIdPulse() {

        try {

            if (Utility.COMMUNICATION_MODE != 0) {

                MqttClient.publishMessage(MqttClient.HOMEUID, MqttOperation.getTransactionObject(UtilityConstants.CLIENT_ID_ADD, SharedPreference.getPhone_number(MqttClient.globalContext)));

            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public static void pushModuleRequest() {

        try {

            TreeSet<String> offlineSet = new TreeSet<>();

            TimeoutRequest = 0;

            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    TimeoutRequest++;

                    for (GenericObject genericObject : Utility.genericObjectHashMap.values()) {

                        if (genericObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                            offlineSet.add(genericObject.getMac());

                        }
                    }

                    for (PowerObject powerObject : Utility.powerObjectHashMap.values()) {

                        if (powerObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                            offlineSet.add(powerObject.getMac());

                        }
                    }

                    for (MotionObject motionObject : Utility.motionObjectHashMap.values()) {

                        if (motionObject.getActive().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                            offlineSet.add(motionObject.getMac());
                        }

                    }

                    for (RGBObject rgbObject : Utility.rgbObjectHashMap.values()) {

                        if (rgbObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                            offlineSet.add(rgbObject.getMac());

                        }
                    }

                    for (FanObject fanObject : Utility.fanObjectHashMap.values()) {

                        if (fanObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                            offlineSet.add(fanObject.getMac());
                        }

                    }

                    for (CurtainObject curtainObject : Utility.curtainObjectHashMap.values()) {

                        if (curtainObject.getState().equalsIgnoreCase(UtilityConstants.STATE_OFFLINE)) {

                            offlineSet.add(curtainObject.getMac());
                        }

                    }

                    for (String s : offlineSet) {

                        MqttClient.publishMessage(MqttClient.HOMEUID, MqttOperation.getTransactionObject(UtilityConstants.MODULE_DATA, s));

                    }

                    if (TimeoutRequest > 1) {
                        TimeoutRequest = 1;
                    }

                    if (TimeoutRequest == 1) {

                        cancel();

                        return;
                    }
                }

            }, 5000, 2000);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public static void checkTransaction() {

        try {

            for (Map.Entry<Long, TransactionObject> entry : transactionMap.entrySet()) {

                if (System.currentTimeMillis() >= entry.getKey()) {

//                    System.out.println(">>>>>>>>>>> transaction timeout <<<<<<<<<<<<<");

//                    Toast.makeText( MqttClient.globalContext, "Transaction Timeout", Toast.LENGTH_SHORT).show();

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    public static void completeTransaction(String transID) {

        long currentMillis;

        try {

            currentMillis = Long.parseLong(transID) + transactionTimeout;

            if (transactionMap.containsKey(currentMillis)) {

                System.out.println(">>>>>>>>>>> transaction completed <<<<<<<<<<<<<");

                transactionMap.remove(currentMillis);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void startTransaction(TransactionObject transactionObject) {

        long currentMillis;

        try {

            currentMillis = Long.parseLong(transactionObject.getTransID().split("\\.")[1]) + transactionTimeout;

            transactionMap.put(currentMillis, transactionObject);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
