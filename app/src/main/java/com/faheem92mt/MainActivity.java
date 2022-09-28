package com.faheem92mt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.faheem92mt.profile.ProfileActivity;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    // for back press
    private boolean doubleBackPressed = false;

    // method 1 - default
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabMain);
        viewPager = findViewById(R.id.vpMain);

        setViewPager();
    }

    // class
    class Adapter extends FragmentPagerAdapter {

        public Adapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    ChatFragment chatFragment = new ChatFragment();
                    return chatFragment;
                case 1:
                    RequestsFragment requestsFragment = new RequestsFragment();
                    return requestsFragment;
                case 2:
                    FindFriendsFragment findFriendsFragment = new FindFriendsFragment();
                    return findFriendsFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return tabLayout.getTabCount();
        }
    }

    //
    private void setViewPager() {

        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_chat));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_requests));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_findfriends));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Adapter adapter = new Adapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);

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

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    // method 2
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // custom
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // default
        return super.onCreateOptionsMenu(menu);
    }

    // method 3
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //custom
        int id = item.getItemId();

        if (id==R.id.mnuProfile) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        }

        //default
        return super.onOptionsItemSelected(item);
    }

    //method 4



    @Override
    public void onBackPressed() {
        // if we don't comment out this super
        // then the remaining code is useless
//        super.onBackPressed();

        // we want to return the user to chat tab which is the default tab

        // to check if the tab index is > 0 i.e "Chat Tab"
        if (tabLayout.getSelectedTabPosition()>0) {
            // the tab at index 0 i.e. the chat tab will be loaded
            tabLayout.selectTab(tabLayout.getTabAt(0));
        }
        // if user is already at chat tab, then user can exit the app
        // with 2 double back presses
        else {
            // initially its false so it'll go directly to else part
            if (doubleBackPressed) {
                // if user presses back within 2 seconds,
                // it comes here and
                // closes all activities of the app
                finishAffinity();
            }
            // it comes here and turns the value true
            else {
                // value becomes true
                doubleBackPressed = true;
                // and gives a toast message
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                // however, if the user doesn't press back within 2 seconds,,
                // the value will become false again

                // 2 seconds delay, after which it'll become false
                android.os.Handler handler = new android.os.Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackPressed = false;
                    }
                }, 2000);
            }


        }

    }



}