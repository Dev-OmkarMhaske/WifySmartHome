package com.wify.smart.home.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.dto.UserObject;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.List;

public class HomeSettingUserAdapter extends RecyclerView.Adapter<HomeSettingUserAdapter.RecyclerViewHolder> {

    private final Context context;

    private List<UserObject> users;

    public HomeSettingUserAdapter(Context context, List<UserObject> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_setting_user_adapter, viewGroup, false);

        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {

        try {

            UserObject userObject = users.get(i);

            if (SharedPreference.getPhone_number(context).equalsIgnoreCase(userObject.getPhn()) ||
                    Utility.CLIENT_IDs != null && (Utility.CLIENT_IDs.contains(userObject.getPhn()))) {

                holder.user_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_user_owner));

            }

            holder.user_type.setText(userObject.getType());

            if (userObject.getName().length() > 0) {

                holder.user_name.setText(userObject.getName());

            } else {

                holder.user_name.setText(userObject.getPhn());
            }

            holder.view_user_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!userObject.getType().equalsIgnoreCase(UtilityConstants.USER_HOST) && Utility.CurrentUserObj.getType().equalsIgnoreCase(UtilityConstants.USER_HOST)) {

                        Intent i = new Intent(context, SettingUserViewActivity.class);

                        i.putExtra(UtilityConstants.USER, userObject);

                        i.putExtra("type", holder.user_type.getText().toString());

                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(i);

                    }
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != users ? users.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {


        TextView user_type, user_name;

        ImageView view_user_profile, user_logo;

        RecyclerViewHolder(View view) {
            super(view);

            user_type = view.findViewById(R.id.user_type);

            user_name = view.findViewById(R.id.user_name);

            user_logo = view.findViewById(R.id.user_logo);

            view_user_profile = view.findViewById(R.id.view_user_profile);

        }

    }

}
