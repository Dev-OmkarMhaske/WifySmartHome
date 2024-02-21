package com.wify.smart.home.mqtt;

import com.google.gson.Gson;
import com.wify.smart.home.dto.DimmerObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.mqtt.dto.TransactionObject;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.HashMap;

public class MqttOperation {

    public static void preferenceValueAction(String action, String type, String data) {

        TransactionObject transactionObject = null;

        try {

            transactionObject = new TransactionObject();

            transactionObject.setType(type);

            transactionObject.setAction(action);

            transactionObject.setData(data);

            MqttClient.publishMessage(MqttClient.HOMEUID, new Gson().toJson(transactionObject));

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void spiffsValueAction(String action, String type, String file, String data) {

        TransactionObject transactionObject = null;

        try {

            transactionObject = new TransactionObject();

            transactionObject.setType(type);

            transactionObject.setAction(action);

            transactionObject.setData(data);

            transactionObject.setFile(file);

            MqttClient.publishMessage(MqttClient.HOMEUID, new Gson().toJson(transactionObject));

            if (Utility.KEYMAP == null) {

                Utility.KEYMAP = new HashMap<>();

            }

            if (!file.equalsIgnoreCase(UtilityConstants.KEYMAP) && (action.equalsIgnoreCase(UtilityConstants.WRITE) ||
                    action.equalsIgnoreCase(UtilityConstants.DELETE))) {

                if (action.equalsIgnoreCase(UtilityConstants.WRITE)) {

                    Utility.KEYMAP.put(file, Utility.getMinuteAndSecond());

                }

                if (action.equalsIgnoreCase(UtilityConstants.DELETE)) {

                    Utility.KEYMAP.remove(file);

                }

                writeKeyMap();

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void writeControllerPoints(String filename, String data) {

        TransactionObject transactionObject = new TransactionObject();

        try {

            transactionObject.setType(UtilityConstants.POINT);

            transactionObject.setAction(UtilityConstants.WRITE);

            transactionObject.setData(data);

            transactionObject.setFile(filename);

            MqttClient.publishMessage(MqttClient.HOMEUID, new Gson().toJson(transactionObject));

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void writeMotionPOINT(String filename, String data) {

        try {

            MqttOperation.spiffsValueAction(UtilityConstants.WRITE, UtilityConstants.MOTION_POINT, filename, data);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void writePOINT(String filename, String data) {

        try {

            MqttOperation.spiffsValueAction(UtilityConstants.WRITE, UtilityConstants.POINT, filename, data);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void writeKeyMap() {

        if (Utility.KEYMAP != null) {

            TransactionObject transactionObject = null;

            try {

                transactionObject = new TransactionObject();

                transactionObject.setType(UtilityConstants.KEYMAP);

                transactionObject.setAction(UtilityConstants.WRITE);

                transactionObject.setData(new Gson().toJson(Utility.KEYMAP));

                transactionObject.setFile(UtilityConstants.KEYMAP);

                MqttClient.publishMessage(MqttClient.HOMEUID, new Gson().toJson(transactionObject));

            } catch (Exception e) {

                e.printStackTrace();

            }
        }

    }

    public static void setAutomationDataForMotion(String type, String data) {

        TransactionObject transactionObject = null;

        try {

            transactionObject = new TransactionObject();

            transactionObject.setType(type);

            transactionObject.setData(data);

            MqttClient.publishMessage(MqttClient.HOMEUID, new Gson().toJson(transactionObject));

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void setAutomationData(String data) {

        TransactionObject transactionObject = null;

        try {

            transactionObject = new TransactionObject();

            transactionObject.setType(UtilityConstants.AUTOMATION_DATA);

            transactionObject.setData(data);

            MqttClient.publishMessage(MqttClient.HOMEUID, new Gson().toJson(transactionObject));

            TransactionPool.startTransaction(transactionObject);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static String getTransactionObject(String type, String data) {

        TransactionObject transactionObject = null;

        try {

            transactionObject = new TransactionObject();

            transactionObject.setType(type);

            transactionObject.setData(data);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return new Gson().toJson(transactionObject);

    }

    public static String getDimmerDataForAutomation(DimmerObject dimmerObject) {

        StringBuilder builder = new StringBuilder();

        try {

//     mac@intensity@parent_mac@parent_point@color

            builder.append(dimmerObject.getMac());
            builder.append("$").append(dimmerObject.getIntensity());
            builder.append("$").append(dimmerObject.getParent_mac());
            builder.append("$").append(dimmerObject.getParent_point());
            builder.append("$").append(dimmerObject.getColor());

        } catch (Exception e) {

            e.printStackTrace();

        }

        return builder.toString();
    }

}
