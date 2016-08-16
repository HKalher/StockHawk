package com.sam_chordas.android.stockhawk.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.sam_chordas.android.stockhawk.ui.DetailFragment.DetailFragmentOne;
import com.sam_chordas.android.stockhawk.ui.DetailFragment.DetailFragmentThree;
import com.sam_chordas.android.stockhawk.ui.DetailFragment.DetailFragmentTwo;

import java.util.ArrayList;

/**
 * Created by Henu on 06/06/16.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private DetailFragmentOne tabOne;
    private DetailFragmentTwo tabTwo;
    private DetailFragmentThree tabThree;

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    //This method return the fragment for the every position in the View Pager, This method is called only when we slide or change the tabs and not called upon rotating the screen
    @Override
    public Fragment getItem(int position) {
        if(position == 0) // if the position is 0 we are returning the First tab
        {
            tabOne = new DetailFragmentOne();
            return tabOne;
        }
        else if(position == 1)            // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            tabTwo = new DetailFragmentTwo();
            return tabTwo;
        }
        else if(position == 2)
        {
            tabThree = new DetailFragmentThree();
            return tabThree;
        }
        else {
            return null;
        }


    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

    public void sendLineGraphDetail(ArrayList<String> stockCloseValueList){
        tabOne.setDetailGraphView(stockCloseValueList);
    }
    public void sendPointGraphDetail(ArrayList<String> stockCloseValueList){
        tabTwo.setDetailGraphView(stockCloseValueList);
    }
    public void sendBarGraphDetail(ArrayList<String> stockCloseValueList){
        tabThree.setDetailGraphView(stockCloseValueList);
    }
}

