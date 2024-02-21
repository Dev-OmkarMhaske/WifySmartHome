package com.wify.smart.home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.wify.smart.home.R;
import com.wify.smart.home.helper.FunctionTabContainer;
import com.wify.smart.home.utils.UtilityConstants;

public class FunctionContainerFragment extends Fragment {

    View view;

    TabLayout tabLayout;

    ViewPager viewPager;

    LinearLayout child_container;

    RelativeLayout parent_container;

    FunctionTabContainer functionTabContainer = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {

            view = inflater.inflate(R.layout.function_container_fragment, null);

            child_container =(LinearLayout) view.findViewById(R.id.child_container);

            parent_container = (RelativeLayout) view.findViewById(R.id.parent_container);

            child_container.setBackgroundResource(0);

            parent_container.setBackgroundResource(0);

            tabLayout = view.findViewById(R.id.tabLayout);

            viewPager = view.findViewById(R.id.viewPager);

            tabLayout.addTab(tabLayout.newTab().setText(UtilityConstants.SCENE));

            tabLayout.addTab(tabLayout.newTab().setText(UtilityConstants.SCHEDULE));

            functionTabContainer = new FunctionTabContainer(getContext(), getChildFragmentManager(), tabLayout.getTabCount());

            viewPager.setAdapter(functionTabContainer);

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    viewPager.setCurrentItem(tab.getPosition());

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

}
