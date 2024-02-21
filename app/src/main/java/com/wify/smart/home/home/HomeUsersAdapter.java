package com.wify.smart.home.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.UserObject;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.Utility;

import java.util.LinkedList;

public class HomeUsersAdapter extends RecyclerView.Adapter<HomeUsersAdapter.RecyclerViewHolder> {

    private final Context context;

    private LinkedList<UserObject> users;

    public HomeUsersAdapter(Context context, LinkedList<UserObject> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public HomeUsersAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_user_component_adapter, viewGroup, false);

        return new HomeUsersAdapter.RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final HomeUsersAdapter.RecyclerViewHolder holder, final int i) {

        try {

            UserObject userObject = users.get(i);

            holder.user_name.setText(userObject.getName());

            if (userObject != null && Utility.CLIENT_IDs != null && (Utility.CLIENT_IDs.contains(userObject.getPhn())
                    || SharedPreference.getPhone_number(context).equalsIgnoreCase(userObject.getPhn()))) {

                holder.user_image.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_user_owner));

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != users ? users.size() : 0);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView user_name;

        ImageView user_image;

        RecyclerViewHolder(View view) {
            super(view);

            user_name = view.findViewById(R.id.user_name);

            user_image = view.findViewById(R.id.user_image);

        }

    }

}
