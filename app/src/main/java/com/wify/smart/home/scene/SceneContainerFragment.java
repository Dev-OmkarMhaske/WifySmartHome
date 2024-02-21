package com.wify.smart.home.scene;

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
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class SceneContainerFragment extends Fragment {

    View view;

    Context mCtx;

    RecyclerView scene_list_recycler;

    LinearLayoutManager linearLayoutManager = null;

    List<SceneObject> sceneObjectList = null;

    SceneContainerAdapter sceneContainerAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {

            view = inflater.inflate(R.layout.scene_container_fragment, null);

            mCtx = view.getContext();

            scene_list_recycler = view.findViewById(R.id.scene_list_recycler);

            scene_list_recycler.setHasFixedSize(true);

            linearLayoutManager = new LinearLayoutManager(mCtx);

            scene_list_recycler.setLayoutManager(linearLayoutManager);

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

            sceneObjectList = new ArrayList<>();

            sceneContainerAdapter = new SceneContainerAdapter(mCtx, sceneObjectList);

            scene_list_recycler.setAdapter(sceneContainerAdapter);

            for (SceneObject sceneObject : Utility.SCENEMap.values()) {

                if (sceneObject != null) {

                    sceneObjectList.add(sceneObject);

                }

                sceneContainerAdapter.notifyDataSetChanged();

            }

            view.findViewById(R.id.add_scene).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(mCtx, ListSceneActivity.class));

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
