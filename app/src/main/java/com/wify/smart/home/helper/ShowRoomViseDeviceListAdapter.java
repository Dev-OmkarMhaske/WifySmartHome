package com.wify.smart.home.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.activities.EditMotionActivity;
import com.wify.smart.home.adapters.AddAccessoriesAdapter;
import com.wify.smart.home.adapters.MotionAddAccessoriesAdapter;
import com.wify.smart.home.adapters.MotionViewAccessoriesAdapter;
import com.wify.smart.home.dto.AccessoriesObject;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.MotionObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.dto.ScheduleObject;
import com.wify.smart.home.home.AccessoryContainerAdapter;
import com.wify.smart.home.home.AccessorySettingContainerActivity;
import com.wify.smart.home.scene.AddSceneActivity;
import com.wify.smart.home.scene.ViewAccessoriesAdapter;
import com.wify.smart.home.schedule.AddAccessoriesInScheduleAdapter;
import com.wify.smart.home.schedule.AddScheduleActivity;
import com.wify.smart.home.schedule.ScheduleAccessoriesAdapter;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ShowRoomViseDeviceListAdapter extends RecyclerView.Adapter<ShowRoomViseDeviceListAdapter.RecyclerViewHolder> {

    private final Context context;

    private List<RoomObject> roomObjects;

    private Object object;

    public ShowRoomViseDeviceListAdapter(Context context, List<RoomObject> roomObjects, Object object) {

        this.context = context;

        this.roomObjects = roomObjects;

        this.object = object;
    }

    @Override
    public ShowRoomViseDeviceListAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_vise_appliance_list_adapter, viewGroup, false);

        return new ShowRoomViseDeviceListAdapter.RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ShowRoomViseDeviceListAdapter.RecyclerViewHolder holder, final int i) {

        try {

            RoomObject roomObject = roomObjects.get(i);

            if (roomObject != null) {

                if (object instanceof ScheduleObject) {

                    if (getScheduleAccessories(roomObject, holder).size() > 0) {

                        holder.room_name.setVisibility(View.VISIBLE);

                        holder.room_name.setText(roomObject.getName());

                    } else {

                        holder.room_name.setVisibility(View.GONE);
                    }

                } else if (object instanceof SceneObject) {

                    if (getSceneAccessories(roomObject, holder).size() > 0) {

                        holder.room_name.setVisibility(View.VISIBLE);

                        holder.room_name.setText(roomObject.getName());

                    } else {
                        holder.room_name.setVisibility(View.GONE);
                    }

                } else if (object instanceof String) {

                    String from = (String) object;

                    holder.room_text.setText(roomObject.getName());

                    if (from.equalsIgnoreCase(UtilityConstants.ADD_ACC_IN_SCENE)) {

                        AddRoomViseAppliancesInScene(roomObject, holder);

                    } else if (from.equalsIgnoreCase(UtilityConstants.ADD_ACC_IN_SCHEDULE)) {

                        AddRoomViseAppliancesInSchedule(roomObject, holder);

                    } else if (from.equalsIgnoreCase(UtilityConstants.ADD_ACC_IN_MOTION)) {

                        AddRoomViseAppliancesInMotion(roomObject, holder);

                    } else if (from.equalsIgnoreCase(UtilityConstants.SHOW_ROOMVISE_ACC)) {

                        ShowRoomViseAccessory(roomObject, holder);
                    }

                } else if (object instanceof MotionObject) {

                    MotionObject motionObject = (MotionObject) object;

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, Utility.TILE_SPAN);

                    holder.appliances_recycler.setLayoutManager(gridLayoutManager);

                    holder.add_all_linear_layout.setVisibility(View.GONE);

                    if (EditMotionActivity.getMotionAccessories(motionObject, roomObject).size() > 0) {

                        holder.room_name.setVisibility(View.VISIBLE);

                        holder.room_name.setText(roomObject.getName());

                        MotionViewAccessoriesAdapter motionViewAccessoriesAdapter = new MotionViewAccessoriesAdapter(context, EditMotionActivity.getMotionAccessories(motionObject, roomObject));

                        holder.appliances_recycler.setAdapter(motionViewAccessoriesAdapter);

                    }

                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != roomObjects ? roomObjects.size() : 0);
    }

    public void AddRoomViseAppliancesInSchedule(RoomObject roomObject, ShowRoomViseDeviceListAdapter.RecyclerViewHolder holder) {

        try {

            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, Utility.TILE_SPAN);

            holder.appliances_recycler.setLayoutManager(gridLayoutManager);

            AddAccessoriesInScheduleAdapter addAccessoriesAdapter = null;

            LinkedList<Object> objects = Utility.getRoomViseAccessories(roomObject);

            if (objects.size() > 0) {

                holder.add_all_linear_layout.setVisibility(View.VISIBLE);

                addAccessoriesAdapter = new AddAccessoriesInScheduleAdapter(context, objects);

                holder.appliances_recycler.setAdapter(addAccessoriesAdapter);

            }

            holder.add_all_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AddAccessoriesInScheduleAdapter addAccessoriesAdapter = new AddAccessoriesInScheduleAdapter(context, Utility.getAllRoomAccessories(roomObject, UtilityConstants.SCHEDULE));

                    holder.appliances_recycler.setAdapter(addAccessoriesAdapter);
                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void AddRoomViseAppliancesInScene(RoomObject roomObject, ShowRoomViseDeviceListAdapter.RecyclerViewHolder holder) {

        try {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, Utility.TILE_SPAN);

            holder.appliances_recycler.setLayoutManager(gridLayoutManager);

            holder.room_name.setVisibility(View.GONE);

            AddAccessoriesAdapter addAccessoriesAdapter = null;

            LinkedList<Object> objects = Utility.getRoomViseAccessories(roomObject);

            if (objects.size() > 0) {

                holder.add_all_linear_layout.setVisibility(View.VISIBLE);

                addAccessoriesAdapter = new AddAccessoriesAdapter(context, objects);

                holder.appliances_recycler.setAdapter(addAccessoriesAdapter);

            }

            holder.add_all_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AddAccessoriesAdapter addAccessoriesAdapter = new AddAccessoriesAdapter(context, Utility.getAllRoomAccessories(roomObject, UtilityConstants.SCENE));

                    holder.appliances_recycler.setAdapter(addAccessoriesAdapter);
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void AddRoomViseAppliancesInMotion(RoomObject roomObject, ShowRoomViseDeviceListAdapter.RecyclerViewHolder holder) {

        try {

            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, Utility.TILE_SPAN);

            holder.appliances_recycler.setLayoutManager(gridLayoutManager);

            holder.room_name.setVisibility(View.GONE);

            LinkedList<Object> objects = Utility.getRoomViseAccessoriesInMotion(roomObject);

            if (objects.size() > 0) {

                holder.add_all_linear_layout.setVisibility(View.VISIBLE);

                MotionAddAccessoriesAdapter motionAddAccessoriesAdapter = new MotionAddAccessoriesAdapter(context, objects);

                holder.appliances_recycler.setAdapter(motionAddAccessoriesAdapter);

            }

            holder.add_all_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MotionAddAccessoriesAdapter motionAddAccessoriesAdapter = new MotionAddAccessoriesAdapter(context, Utility.getAllRoomAccessories(roomObject, UtilityConstants.MOTION));

                    holder.appliances_recycler.setAdapter(motionAddAccessoriesAdapter);
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public LinkedList<Object> getScheduleAccessories(RoomObject roomObject, ShowRoomViseDeviceListAdapter.RecyclerViewHolder holder) {

        LinkedList<Object> objectList = new LinkedList<>();

        String[] ControllerData = null;

        String[] SCHEDULE_DATA = null;

        GridLayoutManager gridLayoutManager = null;

        ScheduleAccessoriesAdapter scheduleAccessoriesAdapter = null;

        String dataSplit[] = null;

        String Mac, GPs, FPs, gp;

        try {

            ScheduleObject scheduleObject = (ScheduleObject) object;

            if (scheduleObject.getController_data().trim().length() > 0) {

                //  4033@1111111111@101
                ControllerData = scheduleObject.getController_data().split(",");

                for (int i = 0; i < ControllerData.length; i++) {

                    String split[] = ControllerData[i].split("@");

                    Mac = split[0];

                    GPs = split[1];

                    FPs = split[2];

                    for (GenericObject genericObject1 : Utility.genericObjectHashMap.values()) {

                        GenericObject genericObject = (GenericObject) genericObject1.clone();

                        if (GPs.length() == 7 && genericObject.getMac().equalsIgnoreCase(Mac) && roomObject.getMac().contains(genericObject.getMac())) {

                            gp = "" + GPs.charAt(Integer.parseInt(genericObject.getPoint()) - 1);

                            if (!gp.equalsIgnoreCase("-")) {

                                genericObject.setState("" + GPs.charAt(Integer.parseInt(genericObject.getPoint()) - 1));

                                objectList.add(genericObject);

                                AddScheduleActivity.scheduleAccessoriesSet.add(genericObject.getMac() + ":" + genericObject.getPoint());

                            }
                        }

                    }

//            freeze@state@speed

                    for (FanObject fanObject1 : Utility.fanObjectHashMap.values()) {

                        FanObject fanObject = (FanObject) fanObject1.clone();

                        if (FPs.length() == 2 && fanObject.getMac().contains(Mac) && roomObject.getMac().contains(fanObject.getMac().replace("F1", ""))) {

                            if (fanObject.getPoint().equalsIgnoreCase("F1")) {

                                String fp = "" + FPs.charAt(0);

                                if (!fp.equalsIgnoreCase("-")) {

                                    fanObject.setState("" + FPs.charAt(0));

                                    fanObject.setSpeed("" + FPs.charAt(1));

                                    objectList.add(fanObject);

                                    AddScheduleActivity.scheduleAccessoriesSet.add(fanObject.getMac());

                                }

                            }

                        }
                    }

                }
            }

            if (scheduleObject.getData() != null && roomObject.getMac().trim().length() > 0) {

                SCHEDULE_DATA = scheduleObject.getData().trim().split(",");

                for (int i = 0; i < SCHEDULE_DATA.length; i++) {

                    try {

                        String moduleData = SCHEDULE_DATA[i].trim();

                        String mac = moduleData.split("@")[0];

                        String type = Utility.getModuleTypeByFakeMAC(mac);

                        if (type.equalsIgnoreCase(UtilityConstants.FAN_MODULE) && moduleData != null) {

                            dataSplit = moduleData.split("@");

                            FanObject fanObject = (FanObject) Utility.fanObjectHashMap.get(dataSplit[0]).clone();

                            if (roomObject.getMac().contains(fanObject.getMac()) || (fanObject.getType().equalsIgnoreCase(UtilityConstants.CFM) && roomObject.getMac().contains(fanObject.getMac().replace("F1", "")))) {

                                fanObject.setAutomationData(moduleData);

                                objectList.add(fanObject);

                                AddScheduleActivity.scheduleAccessoriesSet.add(fanObject.getMac());

                            }

                        } else if (type.equalsIgnoreCase(UtilityConstants.RGB_MODULE)) {

                            dataSplit = moduleData.split("@");

                            RGBObject rgbObject = (RGBObject) Utility.rgbObjectHashMap.get(dataSplit[0]).clone();

                            if (roomObject.getMac().contains(rgbObject.getMac())) {

                                rgbObject.setAutomationDataByType(moduleData, rgbObject);

                                objectList.add(rgbObject);

                                AddScheduleActivity.scheduleAccessoriesSet.add(rgbObject.getMac());

                            }
                        } else if (type.equalsIgnoreCase(UtilityConstants.CURTAIN_MODULE)) {

                            dataSplit = moduleData.split("@");

                            CurtainObject curtainObject = (CurtainObject) Utility.curtainObjectHashMap.get(dataSplit[0]).clone();

                            if (roomObject.getMac().contains(curtainObject.getMac())) {

                                curtainObject.setAutomationData(moduleData);

                                objectList.add(curtainObject);

                                AddScheduleActivity.scheduleAccessoriesSet.add(curtainObject.getMac());

                            }

                        } else if (type.equalsIgnoreCase(UtilityConstants.POWER_MODULE)) {

                            dataSplit = moduleData.split("@");

                            PowerObject powerObject = (PowerObject) Utility.powerObjectHashMap.get(dataSplit[0]).clone();

                            if (roomObject.getMac().contains(powerObject.getMac())) {

                                powerObject.setAutomationData(moduleData);

                                objectList.add(powerObject);

                                AddScheduleActivity.scheduleAccessoriesSet.add(powerObject.getMac());

                            }
                        } else if (type.equalsIgnoreCase(UtilityConstants.GENERIC_MODULE)) {

                            dataSplit = moduleData.split("@");

                            GenericObject genericObject = (GenericObject) Utility.genericObjectHashMap.get(dataSplit[0] + ":" + dataSplit[1]).clone();

                            if (roomObject.getMac().contains(genericObject.getMac())) {

                                genericObject.setAutomationData(moduleData);

                                AddScheduleActivity.scheduleAccessoriesSet.add(genericObject.getMac() + ":" + genericObject.getPoint());

                                objectList.add(genericObject);
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }

            gridLayoutManager = new GridLayoutManager(context, Utility.TILE_SPAN);

            holder.appliances_recycler.setLayoutManager(gridLayoutManager);

            scheduleAccessoriesAdapter = new ScheduleAccessoriesAdapter(context, objectList);

            holder.appliances_recycler.setAdapter(scheduleAccessoriesAdapter);

        } catch (Exception e) {

            e.printStackTrace();
        }
        return objectList;
    }

    public List<Object> getSceneAccessories(RoomObject roomObject, ShowRoomViseDeviceListAdapter.RecyclerViewHolder holder) {

        List<Object> objectList = new ArrayList<>();

        String[] SCENE_DATA = null;

        GridLayoutManager gridLayoutManager = null;

        ViewAccessoriesAdapter viewAccessoriesAdapter = null;

        String[] ControllerData = null;

        String Mac, GPs, FPs, gp, moduleData, mac, type;

        try {

            SceneObject sceneObject = (SceneObject) object;

            if (sceneObject.getController_data().trim().length() > 0) {

                //              4033@1111111111@101
                ControllerData = sceneObject.getController_data().split(",");

                for (int i = 0; i < ControllerData.length; i++) {

                    String split[] = ControllerData[i].split("@");

                    Mac = split[0];

                    GPs = split[1];

                    FPs = split[2];

                    for (GenericObject genericObject1 : Utility.genericObjectHashMap.values()) {

                        GenericObject genericObject = (GenericObject) genericObject1.clone();

                        if (GPs.length() == 7 && genericObject.getMac().equalsIgnoreCase(Mac) && roomObject.getMac().contains(genericObject.getMac())) {

                            gp = "" + GPs.charAt(Integer.parseInt(genericObject.getPoint()) - 1);

                            if (!gp.equalsIgnoreCase("-")) {

                                genericObject.setState("" + GPs.charAt(Integer.parseInt(genericObject.getPoint()) - 1));

                                objectList.add(genericObject);

                                AddSceneActivity.addSceneAccessories.add(genericObject.getMac() + ":" + genericObject.getPoint());

                            }
                        }

                    }

//            freeze@state@speed

                    for (FanObject fanObject1 : Utility.fanObjectHashMap.values()) {

                        FanObject fanObject = (FanObject) fanObject1.clone();

                        if (fanObject.getType().equalsIgnoreCase(UtilityConstants.CFM)) {

                            if (FPs.length() == 2 && fanObject.getMac().contains(Mac) && roomObject.getMac().contains(fanObject.getMac().replace("F1", ""))) {

                                if (fanObject.getPoint().equalsIgnoreCase("F1")) {

                                    String fp = "" + FPs.charAt(0);

                                    if (!fp.equalsIgnoreCase("-")) {

                                        fanObject.setState("" + FPs.charAt(0));

                                        fanObject.setSpeed("" + FPs.charAt(1));

                                        AddSceneActivity.addSceneAccessories.add(fanObject.getMac());

                                        objectList.add(fanObject);

                                    }

                                }

                             }
                        }

                    }

                }
            }

            if (sceneObject.getData() != null && roomObject.getMac().trim().length() > 0) {

                SCENE_DATA = sceneObject.getData().split(",");

                for (int i = 0; i < SCENE_DATA.length; i++) {

                    try {

                        moduleData = SCENE_DATA[i].trim();

                        mac = moduleData.split("@")[0];

                        type = Utility.getModuleTypeByFakeMAC(mac);

                        if (type.equalsIgnoreCase(UtilityConstants.FAN_MODULE) && moduleData != null) {

                            String dataSplit[] = moduleData.split("@");

                            FanObject fanObject = (FanObject) Utility.fanObjectHashMap.get(dataSplit[0]).clone();

                            if (roomObject.getMac().contains(fanObject.getMac()) || (fanObject.getType().equalsIgnoreCase(UtilityConstants.CFM) && roomObject.getMac().contains(fanObject.getMac().replace("F1", "")))) {

                                fanObject.setAutomationData(moduleData);

                                objectList.add(fanObject);

                                AddSceneActivity.addSceneAccessories.add(fanObject.getMac());

                            }

                        } else if (type.equalsIgnoreCase(UtilityConstants.RGB_MODULE)) {

                            String dataSplit[] = moduleData.split("@");

                            RGBObject rgbObject = (RGBObject) Utility.rgbObjectHashMap.get(dataSplit[0]).clone();

                            if (roomObject.getMac().contains(rgbObject.getMac())) {

                                rgbObject.setAutomationDataByType(moduleData, rgbObject);

                                objectList.add(rgbObject);

                                AddSceneActivity.addSceneAccessories.add(rgbObject.getMac());
                            }

                        } else if (type.equalsIgnoreCase(UtilityConstants.CURTAIN_MODULE)) {

                            String dataSplit[] = moduleData.split("@");

                            CurtainObject curtainObject = (CurtainObject) Utility.curtainObjectHashMap.get(dataSplit[0]).clone();

                            if (roomObject.getMac().contains(curtainObject.getMac())) {

                                curtainObject.setAutomationData(moduleData);

                                objectList.add(curtainObject);

                                AddSceneActivity.addSceneAccessories.add(curtainObject.getMac());
                            }

                        } else if (type.equalsIgnoreCase(UtilityConstants.POWER_MODULE)) {

                            String dataSplit[] = moduleData.split("@");

                            PowerObject powerObject = (PowerObject) Utility.powerObjectHashMap.get(dataSplit[0]).clone();

                            if (roomObject.getMac().contains(powerObject.getMac())) {

                                powerObject.setAutomationData(moduleData);

                                objectList.add(powerObject);

                                AddSceneActivity.addSceneAccessories.add(powerObject.getMac());
                            }
                        } else if (type.equalsIgnoreCase(UtilityConstants.GENERIC_MODULE)) {

                            String dataSplit[] = moduleData.split("@");

                            GenericObject genericObject = (GenericObject) Utility.genericObjectHashMap.get(dataSplit[0] + ":" + dataSplit[1]).clone();

                            if (roomObject.getMac().contains(genericObject.getMac())) {

                                genericObject.setAutomationData(moduleData);

                                AddSceneActivity.addSceneAccessories.add(genericObject.getMac() + ":" + genericObject.getPoint());

                                objectList.add(genericObject);

                            }
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                }

            }

            gridLayoutManager = new GridLayoutManager(context, Utility.TILE_SPAN);

            holder.appliances_recycler.setLayoutManager(gridLayoutManager);

            viewAccessoriesAdapter = new ViewAccessoriesAdapter(context, objectList);

            holder.appliances_recycler.setAdapter(viewAccessoriesAdapter);

        } catch (Exception e) {

            e.printStackTrace();
        }
        return objectList;
    }

    public void ShowRoomViseAccessory(RoomObject roomObject, ShowRoomViseDeviceListAdapter.RecyclerViewHolder holder) {

        List<AccessoriesObject> accessoriesObjects = AccessorySettingContainerActivity.getAccessoryList(roomObject);

        ViewGroup.MarginLayoutParams mp = (ViewGroup.MarginLayoutParams) holder.appliances_recycler.getLayoutParams();

        mp.setMargins(0, 0, 0, 20);

        try {
            if (accessoriesObjects.size() > 0) {

                holder.room_name.setVisibility(View.VISIBLE);

                holder.room_name.setText(roomObject.getName());

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

                holder.appliances_recycler.setLayoutManager(linearLayoutManager);

                holder.appliances_recycler.setBackground(context.getResources().getDrawable(R.drawable.rectangle_shape_background));

                AccessoryContainerAdapter accessoryContainerAdapter = new AccessoryContainerAdapter(context, accessoriesObjects);

                holder.appliances_recycler.setAdapter(accessoryContainerAdapter);

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        RecyclerView appliances_recycler;

        TextView room_name, room_text, add_all_txt;

        LinearLayout add_all_linear_layout;

        RecyclerViewHolder(View view) {
            super(view);

            appliances_recycler = view.findViewById(R.id.room_vise_appliances_recycler);

            room_name = view.findViewById(R.id.room_name);

            add_all_linear_layout = view.findViewById(R.id.add_all_linear_layout);

            room_text = view.findViewById(R.id.room_text);

            add_all_txt = view.findViewById(R.id.add_all_txt);

        }

    }

}
