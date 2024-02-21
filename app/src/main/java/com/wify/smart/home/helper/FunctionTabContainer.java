package com.wify.smart.home.helper;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.wify.smart.home.scene.SceneContainerFragment;
import com.wify.smart.home.schedule.ScheduleContainerFragment;


public class FunctionTabContainer extends FragmentPagerAdapter {

    int totalTabs;

    private Context myContext;

    public FunctionTabContainer(Context context, FragmentManager fm, int totalTabs) {
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

                    return new SceneContainerFragment();

                case 1:

                    return new ScheduleContainerFragment();

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
