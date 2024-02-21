package com.wify.smart.home.fragments;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class WiFiGestureTabAdapter extends FragmentPagerAdapter {

    int totalTabs;

    private Context myContext;

    public WiFiGestureTabAdapter(Context context, FragmentManager fm, int totalTabs) {

        super(fm);
        try {

            myContext = context;

            this.totalTabs = totalTabs;

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public Fragment getItem(int position) {

        try {

            switch (position) {

                case 0:

                    return new WiFiSettingFragment();

                case 1:

                    return new GestureSettingFragment();


                default:

                    return null;

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int getCount() {

        return totalTabs;
    }
}
