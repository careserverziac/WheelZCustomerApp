package com.ziac.wheelzcustomer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import Fragments.ScheduledFragment;
import Fragments.ChooseDealerFragment;
import Fragments.SubmitRequest;

public class TestDriveActivity extends AppCompatActivity {

    Context context;
    TabLayout tabLayout;
    ViewPager viewPager;
//    ViewpagerAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_drive);
    /*    context = this;

        tabLayout = findViewById(R.id.expenses);
        viewPager = findViewById(R.id.viewPager);

        adapter = new ViewpagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public class ViewpagerAdapter extends FragmentPagerAdapter {
        Context context;

        public ViewpagerAdapter(@NonNull Context context, FragmentManager fm) {
            super(fm);
            this.context = context;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new ChooseDealerFragment(viewPager);
                    break;
                case 1:
                    fragment = new ScheduledFragment(viewPager);
                    break;
                case 2:
                    fragment = new SubmitRequest();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            SpannableString spannableString;
            switch (position) {
                case 0:
                    spannableString = new SpannableString("Choose Dealer");
                    spannableString.setSpan(new AbsoluteSizeSpan(10, true), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return spannableString;
                case 1:
                    spannableString = new SpannableString("Scheduled");
                    spannableString.setSpan(new AbsoluteSizeSpan(10, true), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return spannableString;
                case 2:
                    spannableString = new SpannableString("Submit Request");
                    spannableString.setSpan(new AbsoluteSizeSpan(10, true), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return spannableString;
                default:
                    return null;
            }
        }*/
    }
}
