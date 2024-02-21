package com.wify.smart.home.helper;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.wify.smart.home.activities.RGBSettingContainerActivity;
import com.wify.smart.home.fragments.RGBFunctionFragment;
import com.wify.smart.home.fragments.RGBMasterFragment;
import com.wify.smart.home.fragments.SimpleRGBFragment;
import com.wify.smart.home.fragments.SingleRGBFragment;
import com.wify.smart.home.utils.UtilityConstants;

public class RGBTabContainer extends FragmentPagerAdapter {

    int totalTabs;
    private Context myContext;

    public RGBTabContainer(Context context, FragmentManager fm, int totalTabs) {
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

                    if (RGBSettingContainerActivity.rgbObject != null && RGBSettingContainerActivity.rgbObject.getType().equalsIgnoreCase(UtilityConstants.RGB_TYPE_PRO)) {

                        return new RGBMasterFragment();

                    } else if (RGBSettingContainerActivity.rgbObject != null && RGBSettingContainerActivity.rgbObject.getType().equalsIgnoreCase(UtilityConstants.RGB_TYPE_SIMPLE)) {

                        return new SimpleRGBFragment();

                    } else if (RGBSettingContainerActivity.rgbObject != null && RGBSettingContainerActivity.rgbObject.getType().equalsIgnoreCase(UtilityConstants.RGB_TYPE_SINGLE)) {

                        return new SingleRGBFragment();
                    }

                case 1:

                    return new RGBFunctionFragment();

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
