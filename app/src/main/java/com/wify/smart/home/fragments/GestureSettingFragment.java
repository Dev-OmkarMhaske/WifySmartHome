package com.wify.smart.home.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.wify.smart.home.R;
import com.wify.smart.home.activities.MiniserverAddGestureActivity;
import com.wify.smart.home.adapters.MiniserverViewGestureAdapter;
import com.wify.smart.home.dto.GestureObject;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.dto.UserObject;
import com.wify.smart.home.home.HomeSettingUserAdapter;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GestureSettingFragment extends Fragment {

    View view;

    Context mCtx;

    RecyclerView view_gesture_recycler;

    Button add_gesture;

    MiniserverViewGestureAdapter miniserverViewGestureAdapter = null;

    LinearLayoutManager linearLayoutManager = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {

            view = inflater.inflate(R.layout.miniserver_gesture_setting_fragment, null);

            mCtx = view.getContext();

            view_gesture_recycler = view.findViewById(R.id.view_gesture_recycler);

            add_gesture = view.findViewById(R.id.add_gesture);

            view_gesture_recycler.setHasFixedSize(true);

            setView();

            Utility.reload.addOnPropertyChangedCallback(new androidx.databinding.Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {

                    setView();
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
        return view;
    }

    public void setView() {

        List<GestureObject> gestureObjectList = new ArrayList<>();

        for (GestureObject gestureObject : Utility.GestureMap.values()) {

            if (gestureObject != null) {

                gestureObjectList.add(gestureObject);

            }

        }

        linearLayoutManager = new LinearLayoutManager(mCtx);

        view_gesture_recycler.setLayoutManager(linearLayoutManager);

        miniserverViewGestureAdapter = new MiniserverViewGestureAdapter(mCtx, gestureObjectList);

        view_gesture_recycler.setAdapter(miniserverViewGestureAdapter);

        add_gesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), MiniserverAddGestureActivity.class));

            }
        });

    }

    public static String updateGestureData(String updatedata, String action) {

        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        if (Utility.GestureMap.size() > 0) {

            for (Map.Entry<String, GestureObject> entry : Utility.GestureMap.entrySet()) {

                map.put(entry.getKey(), entry.getKey());

            }

        }

        if (action.equals(UtilityConstants.ADD)) {

            map.put(updatedata, updatedata);

        }

        if (action.equals(UtilityConstants.DELETE)) {

            map.remove(updatedata);

        }

        String data = map.values().toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "").trim();

        return data;
    }
}