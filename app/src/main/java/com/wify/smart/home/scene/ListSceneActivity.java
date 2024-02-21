package com.wify.smart.home.scene;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.dto.SceneObject;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

public class ListSceneActivity extends AppCompatActivity {

    RecyclerView predefined_scene_list;

    public static int selectedPosition = -1;

    LinearLayoutManager linearLayoutManager = null;

    SceneListAdapter sceneListAdapter = null;

    SceneObject sceneObject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.list_scene_activity);

            selectedPosition = -1;

            predefined_scene_list = findViewById(R.id.predefined_scene_list);

            predefined_scene_list.setHasFixedSize(true);

            linearLayoutManager = new LinearLayoutManager(getApplicationContext());

            predefined_scene_list.setLayoutManager(linearLayoutManager);

            sceneListAdapter = new SceneListAdapter(getApplicationContext(), Utility.SceneIconList);

            predefined_scene_list.setAdapter(sceneListAdapter);

            findViewById(R.id.cancel_text).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onBackPressed();

                }
            });

            findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (selectedPosition != -1) {

                        sceneObject = new SceneObject();

                        sceneObject.setName(Utility.SceneIconList.get(selectedPosition).getName());

                        sceneObject.setLogo(Utility.SceneIconList.get(selectedPosition).getName());

                        sceneObject.setData("");

                        sceneObject.setFile("");

                        sceneObject.setFav("");

                        Intent i = new Intent(getApplicationContext(), AddSceneActivity.class);

                        i.putExtra(UtilityConstants.SCENE, sceneObject);

                        startActivity(i);

                    }

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
