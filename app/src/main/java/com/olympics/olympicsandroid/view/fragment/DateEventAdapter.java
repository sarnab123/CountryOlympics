package com.olympics.olympicsandroid.view.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by sarnab.poddar on 7/7/16.
 */
public class DateEventAdapter extends FragmentPagerAdapter
{
    public DateEventAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return EventListFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 19;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "3rd August";
            case 1:
                return "4th August";
            case 2:
                return "5th August";
            case 3:
                return "6th August";
            case 4:
                return "7th August";
            case 5:
                return "8th August";
            case 6:
                return "9th August";
            case 7:
                return "10th August";
            case 8:
                return "11th August";
            case 9:
                return "12th August";
            case 10:
                return "13th August";
            case 11:
                return "14th August";
            case 12:
                return "15th August";
            case 13:
                return "16th August";
            case 14:
                return "17th August";
            case 15:
                return "18th August";
            case 16:
                return "19th August";
            case 17:
                return "20th August";
            case 18:
                return "21st August";
            default:
                return "X";
        }
    }
}
