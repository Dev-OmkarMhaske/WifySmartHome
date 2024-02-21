package com.wify.smart.home.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.adapters.ConnectAccessoriesWithParentAdapter;
import com.wify.smart.home.adapters.ConnectAccessoriesWithRoomAdapter;
import com.wify.smart.home.dto.AccessoriesObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.ParentObject;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.mqtt.MqttClient;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectAccessoriesWithRoomActivity extends AppCompatActivity {

    public static int selectedPosition = -1;

    public static int parentPosition = -1;

    public static ArrayList<ParentObject> parentObjects = new ArrayList<>();

    public static ArrayList<RoomObject> rooms = new ArrayList<>();

    public static TextView next_text, module_txt;

    public static Spinner generic_points_spinner;

    public static String module;

    public static HashMap<String, String> genericMap = new HashMap<>();

    RecyclerView room_list_recycler, parent_list_recycler;

    RadioGroup rgb_type_grp;

    RadioButton sim_rgb, pro_rgb, single_color;

    LinearLayout rgb_layout;

    String parent_mac = null;

    String parent_point = null;

    boolean nextBtnFlag = false;

    boolean HmoduleFlag = false;

    LinearLayout parent_list_layout;

    private EditText led_cnt;

    public static void SetSpinner(Context context, RoomObject roomObject) {

        try {

            if (module.equalsIgnoreCase(UtilityConstants.DIMMER_MODULE)) {

                generic_points_spinner.setVisibility(View.VISIBLE);

                module_txt.setVisibility(View.VISIBLE);

            }

            ArrayAdapter sceneDataAdapter = null;

            genericMap = new HashMap<>();

            for (GenericObject genericObject : Utility.genericObjectHashMap.values()) {

                if (roomObject.getMac().contains(genericObject.getMac())) {

                    genericMap.put(genericObject.getMac() + "@" + genericObject.getPoint(), genericObject.getName());

                }

            }

            sceneDataAdapter = new ArrayAdapter<String>(context, R.layout.custom_spinner_item, new ArrayList<>(genericMap.values()));

            sceneDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            generic_points_spinner.setAdapter(sceneDataAdapter);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            nextBtnFlag = false;

            HmoduleFlag = false;

            super.onCreate(savedInstanceState);

            setContentView(R.layout.connect_accessories_with_room_activity);

            module = getIntent().getStringExtra(UtilityConstants.MODULE_TXT);

            HmoduleFlag = getIntent().getStringExtra("isHmodule").equalsIgnoreCase("true") ? true : false;

            System.out.println(">>>>> HmoduleFlag >>>>" + HmoduleFlag);

            System.out.println(">>>>> module >>>>" + module);

            parentObjects = (ArrayList<ParentObject>) getIntent().getSerializableExtra("parentObjects");

            module_txt = findViewById(R.id.module_txt);

            led_cnt = findViewById(R.id.led_cnt);

            rgb_type_grp = findViewById(R.id.rgb_type_grp);

            sim_rgb = findViewById(R.id.sim_rgb);

            pro_rgb = findViewById(R.id.pro_rgb);

            single_color = findViewById(R.id.single_color);

            rgb_layout = findViewById(R.id.rgb_layout);

            next_text = findViewById(R.id.next_text);

            generic_points_spinner = findViewById(R.id.generic_points_spinner);

            parent_list_layout = findViewById(R.id.parent_list_layout);

            if (HmoduleFlag) {

                parent_list_layout.setVisibility(View.GONE);


            }

            if (module.contains(UtilityConstants.RGB_MODULE)) {

                rgb_layout.setVisibility(View.VISIBLE);

            }

            rgb_type_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    if (pro_rgb.isChecked()) {

                        led_cnt.setVisibility(View.VISIBLE);

                    } else {

                        led_cnt.setVisibility(View.GONE);
                    }

                }
            });

            if (Utility.ROOMMap.values().size() > 0) {

                rooms = new ArrayList<>();

                for (RoomObject roomObject : Utility.ROOMMap.values()) {

                    if (roomObject != null && !rooms.contains(roomObject.getName())) {

                        rooms.add(roomObject);

                    }

                }

            } else {

                for (RoomObject roomObject : SharedPreference.getRooms(getApplicationContext()).values()) {

                    if (roomObject != null && !rooms.contains(roomObject.getName())) {

                        rooms.add(roomObject);

                    }
                }
            }

            next_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    confirmInstallAcc(getApplicationContext());

                }
            });

            room_list_recycler = findViewById(R.id.room_list_recycler);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

            room_list_recycler.setHasFixedSize(true);

            room_list_recycler.setLayoutManager(linearLayoutManager);

            ConnectAccessoriesWithRoomAdapter connectAccessoriesWithRoomAdapter = new ConnectAccessoriesWithRoomAdapter(getApplicationContext(), rooms);

            room_list_recycler.setAdapter(connectAccessoriesWithRoomAdapter);

            parent_list_recycler = findViewById(R.id.parent_list_recycler);

            LinearLayoutManager parentListLayoutManager = new LinearLayoutManager(getApplicationContext());

            parent_list_recycler.setHasFixedSize(true);

            parent_list_recycler.setLayoutManager(parentListLayoutManager);

            ConnectAccessoriesWithParentAdapter connectAccessoriesWithParentAdapter = new ConnectAccessoriesWithParentAdapter(getApplicationContext(), parentObjects);

            parent_list_recycler.setAdapter(connectAccessoriesWithParentAdapter);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public boolean registerModule() {

        HttpURLConnection con = null;

        String parentMAC = "";

        String FinalString = "";

        try {

            String fakeMac = Utility.getMinuteAndSecond();

            if (module.equalsIgnoreCase(UtilityConstants.FAN_MODULE)) {

                fakeMac = "F1" + fakeMac;

            } else if (module.equalsIgnoreCase(UtilityConstants.CONTROLLER_MODULE) && HmoduleFlag) {

                fakeMac = "H" + fakeMac;
            }

            String ESPNOWData = "ESPNOW=";

            HashMap<String, AccessoriesObject> accessoriesObjects = SharedPreference.getAccessories(getApplicationContext());

            for (AccessoriesObject accessoriesObject : accessoriesObjects.values()) {

                if (accessoriesObject.getAccessory().equalsIgnoreCase(UtilityConstants.MINISERVER_ACCESORY)) {

                    ESPNOWData = "ESPNOW=" + accessoriesObject.getEsp_now();

                    break;

                }

            }

            if (HmoduleFlag) {

                String H_IP = "";

                if (Utility.STASSID.trim().length() > 0 && Utility.STAPWD.trim().length() > 0 && Utility.IP.trim().length() > 0 && Utility.GATEWAY.trim().length() > 0) {

                    int HCnt = 0;

                    for (AccessoriesObject accessoriesObject : Utility.AccessoriesMap.values()) {

                        if (accessoriesObject.getMac().contains("H") && accessoriesObject.getIp() != null && accessoriesObject.getIp().length() > 0) {

                            HCnt++;
                        }

                    }

                    H_IP = (Integer.parseInt(Utility.IP.substring(Utility.IP.lastIndexOf(".") + 1, Utility.IP.length())) + HCnt + 1) + "";

                } else {

                    Toast.makeText(getApplicationContext(), R.string.set_credentials_to_miniserver, Toast.LENGTH_SHORT).show();

                    return false;
                }

                FinalString = Utility.IP.substring(0, Utility.IP.lastIndexOf(".") + 1);

                FinalString = FinalString + H_IP;

                System.out.println(">>>> FinalString >>>>" + FinalString);

                UtilityConstants.URL = "http://192.168.4.1:91/register?" + ESPNOWData + "&fakeMac=" + fakeMac + "&parentMAC=" + "" + "&STASSID=" + Utility.STASSID + "&STAPWD=" + Utility.STAPWD + "&IP=" + FinalString + "&GATEWAY=" + Utility.GATEWAY + "&SERVERIP=" + Utility.IP;

                System.out.println(">>>>> H URL >>>>>> " + UtilityConstants.URL);

            } else {

                parentMAC = parentObjects.get(parentPosition).getRealMAC();

                UtilityConstants.URL = "http://192.168.4.1:91/register?" + ESPNOWData + "&fakeMac=" + fakeMac + "&parentMAC=" + parentMAC;

                if (module.contains(UtilityConstants.RGB_MODULE)) {

                    if (pro_rgb.isChecked()) {

                        UtilityConstants.URL = UtilityConstants.URL + "&type=1" + "&LED_COUNT=" + led_cnt.getText().toString();

                    } else if (sim_rgb.isChecked()) {

                        UtilityConstants.URL = UtilityConstants.URL + "&type=0" + "&LED_COUNT=" + led_cnt.getText().toString();

                    } else if (single_color.isChecked()) {

                        UtilityConstants.URL = UtilityConstants.URL + "&type=2" + "&LED_COUNT=" + led_cnt.getText().toString();
                    }

                }

            }

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            java.net.URL obj = new URL(UtilityConstants.URL);

            con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod(UtilityConstants.GET_TXT);

            con.setConnectTimeout(60000);

            con.setReadTimeout(120000);

            if (con.getResponseCode() == 200) {

                InputStream inputStream = con.getInputStream();

                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder response = new StringBuilder();

                String currentLine;

                while ((currentLine = in.readLine()) != null) {

                    response.append(currentLine);

                }

                in.close();

                if (response.toString().contains(UtilityConstants.OK_TXT)) {

                    AccessoriesObject accessoriesObject = new AccessoriesObject();

                    if (module.contains(UtilityConstants.RGB_MODULE)) {

                        if (pro_rgb.isChecked()) {

                            accessoriesObject.setAccessory(module + UtilityConstants.ProLED_TXT);

                        } else if (sim_rgb.isChecked()) {

                            accessoriesObject.setAccessory(module + UtilityConstants.SimpleLED_TXT);

                        } else if (single_color.isChecked()) {

                            accessoriesObject.setAccessory(module + UtilityConstants.SingleColor_TXT);
                        }

                    } else {

                        accessoriesObject.setAccessory(module);

                    }

                    accessoriesObject.setDate(Utility.getDate_inDDMMYY());

                    accessoriesObject.setState(UtilityConstants.ACCESORY_WORKING_STATE);

                    if (module.equalsIgnoreCase(UtilityConstants.CONTROLLER_MODULE)) {

                        accessoriesObject.setReal_mac(response.toString().split("#")[2]);

                    } else {

                        accessoriesObject.setReal_mac(response.toString().split("#")[1]);
                    }

                    accessoriesObject.setMac(fakeMac);

                    accessoriesObject.setParentMAC(parentMAC);

                    accessoriesObject.setIp(FinalString);

                    accessoriesObject.setWrite(false);

                    System.out.println(">>>>> accessoriesObject >>>>>" + new Gson().toJson(accessoriesObject));

                    AccessoriesObject parentAccessoriesObject = null;

                    for (AccessoriesObject accessoriesObject1 : accessoriesObjects.values()) {

                        if (accessoriesObject1.getAccessory().equalsIgnoreCase(UtilityConstants.MINISERVER_ACCESORY) && accessoriesObject1.getEsp_now().equalsIgnoreCase(parentMAC)) {

                            parentAccessoriesObject = accessoriesObject1;

                        }

                    }

                    if (accessoriesObjects.containsKey(parentMAC)) {

                        parentAccessoriesObject = accessoriesObjects.get(parentMAC);

                    }

                    if (parentAccessoriesObject != null) {

                        String child = parentAccessoriesObject.getChildMAC();

                        if (child == null && (child != null && child.length() == 0)) {

                            child = accessoriesObject.getReal_mac();

                        } else {

                            child += "," + accessoriesObject.getReal_mac();

                        }

                        parentAccessoriesObject.setChildMAC(child);

                        if (parentAccessoriesObject.getAccessory().equalsIgnoreCase(UtilityConstants.MINISERVER_ACCESORY)) {

                            accessoriesObjects.put(parentAccessoriesObject.getMac(), parentAccessoriesObject);

                        } else {

                            accessoriesObjects.put(parentAccessoriesObject.getReal_mac(), parentAccessoriesObject);

                        }

                    }

                    if (module.contains(UtilityConstants.CONTROLLER_MODULE)) {

                        accessoriesObject.setPoints(response.toString().split("#")[0]);

                    }

                    accessoriesObjects.put(accessoriesObject.getReal_mac(), accessoriesObject);

                    if (Utility.connectedHome != null) {

                        Utility.connectedHome.setAccessories(new HashMap<String, AccessoriesObject>(accessoriesObjects));

                    }

                    Utility.UpdateHome(Utility.connectedHome);

                    SharedPreference.setAccessories(getApplicationContext(), accessoriesObjects);

                    System.out.println(">>>>>>> Accessory Map >>>>>" + new Gson().toJson(accessoriesObjects));

                    ConcurrentHashMap<String, RoomObject> roomObjectHashMap = SharedPreference.getRooms(getApplicationContext());

                    if (!module.equalsIgnoreCase(UtilityConstants.DIMMER_MODULE)) {

                        RoomObject roomObject = null;

                        for (Map.Entry<String, RoomObject> entry : roomObjectHashMap.entrySet()) {

                            roomObject = entry.getValue();

                            if (roomObject != null && roomObject.getName().equalsIgnoreCase(rooms.get(selectedPosition).getName())) {

                                String mac = roomObject.getMac();

                                if (mac != null) {

                                    String split[] = mac.split(",");

                                    TreeSet<String> strings = new TreeSet<>();

                                    for (int i = 0; i < split.length; i++) {

                                        if (split[i].trim().length() > 0) {
                                            strings.add(split[i]);
                                        }

                                    }

                                    if (!strings.contains(accessoriesObject.getMac()) && accessoriesObject.getMac().trim().length() > 0) {

                                        strings.add(accessoriesObject.getMac());

                                    }

                                    StringBuilder builder = null;

                                    for (String s : strings) {

                                        if (builder == null) {

                                            builder = new StringBuilder();

                                            if (s.trim().length() > 0) {

                                                builder.append(s);

                                            }

                                        } else if (s.trim().length() > 0) {

                                            builder.append(",").append(s);

                                        }

                                    }

                                    if (builder != null) {

                                        roomObject.setMac(builder.toString().replaceAll(",,", ""));

                                        roomObject.setLast(UtilityConstants.TRUE_TXT);

                                    }

                                }

                            }

                        }

                        roomObjectHashMap.put(roomObject.getFile(), roomObject);

                        SharedPreference.setRooms(getApplicationContext(), roomObjectHashMap);

                    }

                    SharedPreference.setAccessorySyncFlag(getApplicationContext(), true);

                }

                System.out.println(">>>>>>>>>>>>>>>> response: " + response.toString());

                return true;
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (MqttClient.mqttAndroidClient != null && MqttClient.mqttAndroidClient.isConnected()) {

                MqttClient.mqttAndroidClient.disconnect();

            }

            con.disconnect();

        }

        return false;
    }

    private void confirmInstallAcc(Context context) {

        try {

            final EditText input = new EditText(this);
            input.setHint(context.getString(R.string.confirm_password_txt));
            input.setHintTextColor(this.getResources().getColor(R.color.black_transparent));
            input.setTextColor(this.getResources().getColor(R.color.text_white));

            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
            builder.setMessage(getString(R.string.confirm_install_acc) + " " + module + " ?")
                    .setTitle("Install Accessory")
                    .setView(input)
                    .setPositiveButton(UtilityConstants.CONFIRM_STR, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (UtilityConstants.ADMIN_PASSWORD.equalsIgnoreCase(input.getText().toString())) {

                                if (selectedPosition != -1) {

                                    nextBtnFlag = true;

                                    next_text.setVisibility(View.GONE);

                                    if (module.equalsIgnoreCase(UtilityConstants.DIMMER_MODULE) && generic_points_spinner != null && generic_points_spinner.getSelectedItem() != null) {

                                        String generic_name = generic_points_spinner.getSelectedItem().toString();

                                        for (Map.Entry<String, String> entry : genericMap.entrySet()) {

                                            if (entry.getValue() == generic_name) {

                                                parent_mac = entry.getKey().split("@")[0];

                                                parent_point = entry.getKey().split("@")[1];

                                                break;
                                            }
                                        }

                                    }

                                    if (module.equalsIgnoreCase(UtilityConstants.DIMMER_MODULE)) {

                                        //  DIMMER-MODULE

                                    } else if (Utility.connectedHome.getHome() != null && Utility.connectedHome.getHome().length() > 0 && registerModule()) {

                                        Intent intent = new Intent(getApplicationContext(), ConnectAccessoriesSuccessActivity.class);

                                        startActivity(intent);

                                    }

                                } else {

                                    Toast.makeText(ConnectAccessoriesWithRoomActivity.this, R.string.validate_room_txt, Toast.LENGTH_LONG).show();
                                }

                            } else {

                                Toast.makeText(ConnectAccessoriesWithRoomActivity.this, R.string.valid_password_err_txt, Toast.LENGTH_SHORT).show();
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

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


}
