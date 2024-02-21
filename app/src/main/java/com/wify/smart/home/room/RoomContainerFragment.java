package com.wify.smart.home.room;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.RoomObject;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.util.ArrayList;

public class RoomContainerFragment extends Fragment {

    View view;

    Context mCtx;

    RecyclerView rooms_container_recycler;

    RoomsContainerAdapter roomsContainerAdapter = null;

    ArrayList<RoomObject> rooms;

    GridLayoutManager mGridLayoutManager = null;

    LinearLayout child_container, parent_container;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {

            view = inflater.inflate(R.layout.room_container_fragment, null);

            mCtx = view.getContext();

            child_container = (LinearLayout) view.findViewById(R.id.child_container);

            parent_container = (LinearLayout) view.findViewById(R.id.parent_container);

            child_container.setBackgroundResource(0);

            parent_container.setBackgroundResource(0);

            rooms_container_recycler = view.findViewById(R.id.rooms_container_recycler);

            rooms_container_recycler.setHasFixedSize(true);

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

        rooms = new ArrayList<>();

        mGridLayoutManager = new GridLayoutManager(getActivity(), Utility.TILE_SPAN);

        rooms_container_recycler.setLayoutManager(mGridLayoutManager);

        roomsContainerAdapter = new RoomsContainerAdapter(mCtx, rooms);

        rooms_container_recycler.setAdapter(roomsContainerAdapter);

        if (Utility.isMiniserverConnected) {

            for (RoomObject roomObject : Utility.ROOMMap.values()) {

                if (roomObject != null) {

                    rooms.add(roomObject);

                }
                roomsContainerAdapter.notifyDataSetChanged();
            }
        }
    }

}

