package com.wify.smart.home.room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wify.smart.home.R;
import com.wify.smart.home.activities.AccessoriesSettingActivity;
import com.wify.smart.home.dto.AccessoriesIconsList;
import com.wify.smart.home.dto.GenericObject;
import com.wify.smart.home.dto.PowerObject;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

import java.io.Serializable;

public class ChangeModuleLogoActivity extends AppCompatActivity {

    public static int selectedPosition = -1;

    public static Object object;

    RecyclerView modules_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

            selectedPosition = -1;

            setContentView(R.layout.change_module_logo_activity);

            modules_recycler = findViewById(R.id.modules_recycler);

            object = (Object) getIntent().getSerializableExtra(UtilityConstants.OBJECT_STR);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

            modules_recycler.setLayoutManager(linearLayoutManager);

            modules_recycler.setHasFixedSize(true);

            int index = 0;

            for (AccessoriesIconsList accessoriesIconsList : Utility.accessoriesIconsLists) {

                if (object instanceof GenericObject) {

                    GenericObject genericObject = (GenericObject) object;

                    if (genericObject.getLogo().equalsIgnoreCase(accessoriesIconsList.getName())) {

                        selectedPosition = index;

                        break;
                    }

                } else if (object instanceof PowerObject) {

                    PowerObject powerObject = (PowerObject) object;

                    if (powerObject.getLogo().equalsIgnoreCase(accessoriesIconsList.getName())) {

                        selectedPosition = index;

                        break;
                    }
                }

                index++;
            }

            ModuleLogoListAdapter moduleLogoListAdapter = new ModuleLogoListAdapter(getApplicationContext(), Utility.accessoriesIconsLists);

            modules_recycler.setAdapter(moduleLogoListAdapter);

            findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onBackPressed();
                }
            });

            findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (selectedPosition != -1) {

                        if (object instanceof GenericObject) {

                            GenericObject genericObject = (GenericObject) object;

                            genericObject.setLogo(Utility.accessoriesIconsLists.get(selectedPosition).getName());

                            object = genericObject;

                        } else if (object instanceof PowerObject) {

                            PowerObject powerObject = (PowerObject) object;

                            powerObject.setLogo(Utility.accessoriesIconsLists.get(selectedPosition).getName());

                            object = powerObject;
                        }

                        Intent i = new Intent(getApplicationContext(), AccessoriesSettingActivity.class);

                        i.putExtra(UtilityConstants.ACCESSORY, (Serializable) object);

                        i.putExtra(UtilityConstants.ROOM_NAME, AccessoriesSettingActivity.RoomName);

                        startActivity(i);
                    }

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
