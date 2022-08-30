package com.vms.antplay.activity;


import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;
import com.vms.antplay.R;
import com.vms.antplay.adapter.PagerAdapter;
import com.vms.antplay.fragments.ComputerFragment;
import com.vms.antplay.fragments.FaqFargment;
import com.vms.antplay.fragments.HomeFragment;
import com.vms.antplay.fragments.SettingsFragment;
import com.vms.antplay.model.ImageModel;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    ViewPager simpleViewPager;
    FrameLayout simpleFrameLayout;
    TabLayout tabLayout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccentDark_light, this.getTheme()));
        simpleFrameLayout = (FrameLayout) findViewById(R.id.simpleFrameLayout);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        //  simpleViewPager = (ViewPager) findViewById(R.id.viewPager);
        loadFragment(new HomeFragment());


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // get the current selected tab's position and replace the fragment accordingly
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new HomeFragment();
                        break;
                    case 1:
                        fragment = new ComputerFragment();
                        break;
                    case 2:
                        fragment = new SettingsFragment();
                        break;
                    case  3:
                        fragment = new FaqFargment();
                        break;
                    case 4:
                        fragment = new FaqFargment();
                        break;
                    case 5:
                        logout();

                    default:
                        fragment = new HomeFragment();
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void logout() {
        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.simpleFrameLayout, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}