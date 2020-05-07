package com.bringo.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.bringo.home.ui.OrderHistoryFragment;
import com.bringo.home.ui.PendingOrderFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MyOrdersActivity extends AppCompatActivity {

    private TabLayout tablayout;
    int position;
    private ViewPager viewPager;
    private ViewPagerAdpter viewPagerAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Other Orders");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tablayout = findViewById(R.id.tabs);

        viewPager = findViewById(R.id.viewpager);
        viewPagerAdpter = new ViewPagerAdpter(getSupportFragmentManager());
        viewPagerAdpter.addFragment(new OrderHistoryFragment(), "History");
        viewPagerAdpter.addFragment(new PendingOrderFragment(), "Pending");

        viewPager.setAdapter(viewPagerAdpter);
        tablayout.setupWithViewPager(viewPager);

    }

    public static class ViewPagerAdpter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragment;
        private String tabTitles[] = new String[]{"History", "Pending"};

        public ViewPagerAdpter(FragmentManager fm) {
            super(fm);
            this.fragment = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragment.get(position);
        }

        @Override
        public int getCount() {
            return fragment.size();
        }

        public void addFragment(Fragment fragments, String titles) {
            fragment.add(fragments);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

}