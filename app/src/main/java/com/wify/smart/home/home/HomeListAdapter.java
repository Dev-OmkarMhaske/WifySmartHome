package com.wify.smart.home.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.activities.LoadingActivity;
import com.wify.smart.home.dto.HomeObject;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.List;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.RecyclerViewHolder> {

    private final Context context;

    View view;

    PopupWindow popupWindow;

    List<HomeObject> homeObjectList;

    public HomeListAdapter(View view, List<HomeObject> homeObjectList, PopupWindow popupWindow) {

        this.view = view;

        this.popupWindow = popupWindow;

        this.context = view.getContext();

        this.homeObjectList = homeObjectList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_homes_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, @SuppressLint("RecyclerView") final int i) {

        try {

            HomeObject homeObject = homeObjectList.get(i);

            holder.home_name.setText(homeObject.getHome());

            if (SharedPreference.getConnectedHome(context) != null && SharedPreference.getConnectedHome(context).getHome().equalsIgnoreCase(homeObject.getHome())) {

                holder.is_home_selected.setVisibility(View.VISIBLE);

            }

            holder.home_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    popupWindow.dismiss();

                    ConfirmToConnectHome(homeObject);

                }
            });


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void ConfirmToConnectHome(HomeObject homeObject) {

        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);

            builder.setMessage(context.getString(R.string.confirm_home_connect_str) + " " + homeObject.getHome() + " ?")
                    .setTitle(context.getString(R.string.connect_home_str))
                    .setPositiveButton(UtilityConstants.CONFIRM_STR, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            if (Utility.connectedHome != null && !Utility.connectedHome.getHome().equalsIgnoreCase(homeObject.getHome())) {

                                if (SharedPreference.getAccessorySyncFlag(context)) {

                                    Toast.makeText(context, R.string.sync_home_err, Toast.LENGTH_SHORT).show();

                                    return;

                                }

                                SharedPreference.setRooms(context, null);

                                SharedPreference.setIP(context, null);

                                SharedPreference.setAccessories(context, null);

                                SharedPreference.setKeyMap(context, null);

                                SharedPreference.setUsers(context, null);

                                SharedPreference.setFMs(context, null);

                                SharedPreference.setCMs(context, null);

                                SharedPreference.setGMs(context, null);

                                SharedPreference.setRGBs(context, null);

                                SharedPreference.setMMs(context, null);

                                SharedPreference.setPMs(context, null);

                                SharedPreference.setScenes(context, null);

                                SharedPreference.setSchedules(context, null);

                            }

                            Intent i = new Intent(context, LoadingActivity.class);

                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            i.putExtra(UtilityConstants.HOME_OBJ, homeObject);

                            context.startActivity(i);

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

    @Override
    public int getItemCount() {
        return (null != homeObjectList ? homeObjectList.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView is_home_selected;

        TextView home_name;

        RecyclerViewHolder(View view) {
            super(view);

            home_name = view.findViewById(R.id.home_name);

            is_home_selected = view.findViewById(R.id.is_home_selected);

        }

    }

}
