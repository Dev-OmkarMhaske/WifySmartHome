package com.wify.smart.home.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wify.smart.home.R;
import com.wify.smart.home.dto.WifyThemes;
import com.wify.smart.home.fragments.FunctionContainerFragment;
import com.wify.smart.home.helper.CustomPopups;
import com.wify.smart.home.helper.HomePopUp;
import com.wify.smart.home.room.RoomContainerFragment;
import com.wify.smart.home.utils.SharedPreference;
import com.wify.smart.home.utils.Utility;
import com.wify.smart.home.utils.UtilityConstants;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static int plusIconX, plusIconY;

    private ImageView home_setting;

    private TextView plusIcon;

    BottomNavigationView navigation = null;

    String from = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);

            if (SharedPreference.getTheme(getApplicationContext()) != null) {

                WifyThemes wifyThemes = SharedPreference.getTheme(getApplicationContext());

                setTheme(wifyThemes.getTheme());

            }

            setContentView(R.layout.activity_main);

            home_setting = findViewById(R.id.home_setting);

            plusIcon = findViewById(R.id.plusIcon);

            from = getIntent().getStringExtra(UtilityConstants.FROM);

            navigation = findViewById(R.id.navigation);

            navigation.setOnNavigationItemSelectedListener(this);

            if (from == null || from.equalsIgnoreCase(UtilityConstants.HOME)) {

                loadFragment(new HomeContainerFragment());

                navigation.setSelectedItemId(R.id.navigation_home);

            } else if (from.equalsIgnoreCase(UtilityConstants.SCHEDULE) || from.equalsIgnoreCase(UtilityConstants.SCENE)) {

                loadFragment(new FunctionContainerFragment());

                navigation.setSelectedItemId(R.id.navigation_function);

            } else if (from.equalsIgnoreCase(UtilityConstants.ROOM)) {

                loadFragment(new RoomContainerFragment());

                navigation.setSelectedItemId(R.id.navigation_room);
            }

            navigation.setVisibility(View.GONE);

            if (SharedPreference.getAccessorySyncFlag(getApplicationContext()) || Utility.isMiniserverConnectionLost || Utility.connectedHome == null || !Utility.isMiniserverConnected || !Utility.isMiniserverInstall) {

                navigation.setVisibility(View.GONE);

            } else {

                navigation.setVisibility(View.VISIBLE);

            }

            Utility.isReady.addOnPropertyChangedCallback(new androidx.databinding.Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {

                    if (Utility.isMiniserverConnectionLost || Utility.connectedHome == null || !Utility.isMiniserverConnected || !Utility.isMiniserverInstall) {

                        navigation.setVisibility(View.GONE);

                    } else {

                        navigation.setVisibility(View.VISIBLE);

                    }
                }
            });

            plusIcon.setVisibility(View.VISIBLE);

            home_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new HomePopUp().showPopupHomeSetting(view);

                }
            });

            plusIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new HomePopUp().showPopupHome_Add(view);

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        int[] posXY = new int[2];

        plusIcon.getLocationOnScreen(posXY);

        plusIconX = posXY[0];

        plusIconY = posXY[1];

    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {

            case R.id.navigation_home:

                plusIcon.setVisibility(View.VISIBLE);

                fragment = new HomeContainerFragment();

                Utility.vibrate();

                break;

            case R.id.navigation_room:

                plusIcon.setVisibility(View.INVISIBLE);

                fragment = new RoomContainerFragment();

                Utility.vibrate();

                break;

            case R.id.navigation_function:

                plusIcon.setVisibility(View.INVISIBLE);

                fragment = new FunctionContainerFragment();

                Utility.vibrate();

                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, fragment);

            transaction.addToBackStack(null);

            transaction.commit();

            return true;

        }
        return false;
    }

}