package com.wify.smart.home.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.ScheduleObject;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;
import java.util.List;

public class ScheduleContainerFragment extends Fragment {

    View view;

    Context mCtx;

    List<ScheduleObject> schedules = new ArrayList<>();

    RecyclerView schedule_list_recycler;

    LinearLayoutManager linearLayoutManager = null;

    ScheduleContainerAdapter scheduleContainerAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {

            view = inflater.inflate(R.layout.schedule_container_fragment, null);

            mCtx = view.getContext();

            schedule_list_recycler = view.findViewById(R.id.schedule_list_recycler);

            schedule_list_recycler.setHasFixedSize(true);

            linearLayoutManager = new LinearLayoutManager(mCtx);

            schedule_list_recycler.setLayoutManager(linearLayoutManager);

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

        try {

            schedules = new ArrayList<>();

            scheduleContainerAdapter = new ScheduleContainerAdapter(mCtx, schedules);

            schedule_list_recycler.setAdapter(scheduleContainerAdapter);

            for (ScheduleObject scheduleObject : Utility.SCHEDULEMap.values()) {

                if (scheduleObject != null) {

                    schedules.add(scheduleObject);

                    scheduleContainerAdapter.notifyDataSetChanged();

                }

            }

            view.findViewById(R.id.add_schedule).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ScheduleObject scheduleObject = null;

                    Intent i = new Intent(mCtx, AddScheduleActivity.class);

                    i.putExtra(UtilityConstants.SCHEDULE, scheduleObject);

                    startActivity(i);
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}