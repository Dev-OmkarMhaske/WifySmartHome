package com.wify.smart.home.utils;

import com.wify.smart.home.mqtt.MqttClient;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class UpdateMiniserver {

    int serverResponseCode = 0;

    public void uploadFile1() {

        try {
            InputStream inputStream = MqttClient.globalContext.getAssets().open("MESH_MASTER.ino.esp32.bin");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);

            System.out.println("read update file buffer >>>" + buffer.length);

            System.out.println(".......................... uploadFile ..................");

            String fileName = "MESH_MASTER.ino.esp32.bin";

            URL url = new URL("http://" + Utility.COMMUNICATION_MODE_IP + ":91/upload");

            System.out.println("url >>" + url.toString());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            String boundary = UUID.randomUUID().toString();

            connection.setRequestMethod("POST");

            connection.setDoOutput(true);

            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream request = new DataOutputStream(connection.getOutputStream());

            request.writeBytes("--" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"description\"\r\n\r\n");
            String fileDescription = "test";
            request.writeBytes(fileDescription + "\r\n");

            request.writeBytes("--" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n\r\n");
            request.write(buffer);
            request.writeBytes("\r\n");

            request.writeBytes("--" + boundary + "--\r\n");
            request.flush();
            int respCode = connection.getResponseCode();

            if (respCode == 200) {

                String msg = "File Upload Completed.\n\n See uploaded file";

                System.out.println("msg >>>" + msg);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
