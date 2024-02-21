package com.wify.smart.home.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.wify.smart.home.mqtt.MqttClient;
import com.wify.smart.home.mqtt.MqttOperation;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.UtilityConstants;

public class service extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("------------------------- Service started by user.");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        try {

            super.onTaskRemoved(rootIntent);

            System.out.println("------------------------- onTaskRemoved Service destroyed by user.");

            if (MqttClient.HOMEUID != null) {

                MqttClient.publishMessage(MqttClient.HOMEUID, MqttOperation.getTransactionObject(UtilityConstants.CLIENT_ID_REMOVE, SharedPreference.getPhone_number(MqttClient.globalContext)));

                Thread.sleep(1000);

                if (MqttClient.mqttAndroidClient != null && MqttClient.mqttAndroidClient.isConnected()) {

                    MqttClient.mqttAndroidClient.disconnect();

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}