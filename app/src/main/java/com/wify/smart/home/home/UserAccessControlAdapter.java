package com.wify.smart.home.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.CurtainObject;
import com.wify.smart.home.dto.FanObject;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.MotionObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.dto.RGBObject;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.dto.UserObject;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.LinkedList;
import java.util.List;

public class UserAccessControlAdapter extends RecyclerView.Adapter<UserAccessControlAdapter.RecyclerViewHolder> {

    private final Context context;

    private List<RoomObject> room_access;

    RoomViseAppliancesAdapter roomViseAppliancesAdapter;

    UserObject current_user;

    public UserAccessControlAdapter(Context context, List<RoomObject> access, UserObject userObject) {

        this.context = context;

        this.room_access = access;

        this.current_user = userObject;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_access_adapter_rooms, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            RoomObject roomObject = room_access.get(i);

            holder.parent_layout.setVisibility(View.VISIBLE);

            holder.access_name.setText(roomObject.getName());

            holder.room_check.setChecked(!current_user.getMac().contains(roomObject.getFile()));

            holder.room_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.room_check.isChecked()) {

                        SettingUserViewActivity.updateUserMacData(roomObject.getFile(), UtilityConstants.DELETE);

                    } else {

                        SettingUserViewActivity.updateUserMacData(roomObject.getFile(), UtilityConstants.ADD);

                    }
                }
            });

            holder.view_accessories_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.room_vise_appliances_recycler.setHasFixedSize(true);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

                    holder.room_vise_appliances_recycler.setLayoutManager(linearLayoutManager);

                    roomViseAppliancesAdapter = new RoomViseAppliancesAdapter(context, getAccessories(roomObject), current_user);

                    holder.room_vise_appliances_recycler.setAdapter(roomViseAppliancesAdapter);

                    if (holder.room_vise_appliances_recycler.getVisibility() == View.VISIBLE) {

                        holder.room_vise_appliances_recycler.setVisibility(View.GONE);

                    } else {

                        holder.room_vise_appliances_recycler.setVisibility(View.VISIBLE);
                    }

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private LinkedList<Object> getAccessories(RoomObject roomObject) {

        LinkedList<Object> room_accessories = new LinkedList<>();

        try {

            if (roomObject != null && roomObject.getMac().trim().length() > 0) {

                for (FanObject fanObject : Utility.fanObjectHashMap.values()) {

                    if (roomObject.getMac().contains(fanObject.getMac()) || (fanObject.getType().equalsIgnoreCase(UtilityConstants.CFM) && roomObject.getMac().contains(fanObject.getMac().replace("F1", "")))) {

                        room_accessories.add(fanObject);
                    }
                }

                for (MotionObject motionObject : Utility.motionObjectHashMap.values()) {

                    if (roomObject.getMac().contains(motionObject.getMac())) {

                        room_accessories.add(motionObject);

                    }
                }

                for (PowerObject powerObject : Utility.powerObjectHashMap.values()) {

                    if (roomObject.getMac().contains(powerObject.getMac())) {

                        room_accessories.add(powerObject);

                    }
                }
                for (CurtainObject curtainObject : Utility.curtainObjectHashMap.values()) {

                    if (roomObject.getMac().contains(curtainObject.getMac())) {

                        room_accessories.add(curtainObject);

                    }
                }
                for (RGBObject rgbObject : Utility.rgbObjectHashMap.values()) {

                    if (roomObject.getMac().contains(rgbObject.getMac())) {

                        room_accessories.add(rgbObject);

                    }
                }
                for (GenericObject genericObject : Utility.genericObjectHashMap.values()) {

                    if (genericObject.getUsed().equalsIgnoreCase(UtilityConstants.STATE_TRUE) && roomObject.getMac().contains(genericObject.getMac())) {

                        room_accessories.add(genericObject);

                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return room_accessories;
    }

    @Override
    public int getItemCount() {
        return (null != room_access ? room_access.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView access_name;

        ImageView view_accessories_img;

        RecyclerView room_vise_appliances_recycler;

        LinearLayout parent_layout;

        CheckBox room_check;

        RecyclerViewHolder(View view) {
            super(view);

            access_name = view.findViewById(R.id.access_name);

            view_accessories_img = view.findViewById(R.id.view_accessories_img);

            parent_layout = view.findViewById(R.id.parent_layout);

            room_check = view.findViewById(R.id.accessories_check);

            room_vise_appliances_recycler = view.findViewById(R.id.room_vise_appliances_recycler);

        }

    }

}

