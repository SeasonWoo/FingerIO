package com.example.demo.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.demo.screens.HorizontalPagerFragment;
import com.example.demo.screens.TwoWayPagerFragment;

/**
 * Created by GIGAMOLE on 8/18/16.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "MainPagerAdapter";
    private final static int COUNT = 2;

    private final static int HORIZONTAL = 0;
    private final static int TWO_WAY = 1;

    public TwoWayPagerFragment twoWayPagerFragment;
    static public HorizontalPagerFragment horizontalPagerFragment;

    public MainPagerAdapter(final FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case TWO_WAY:
                twoWayPagerFragment = new TwoWayPagerFragment();
                return twoWayPagerFragment;
            case HORIZONTAL:
            default:
                horizontalPagerFragment = new HorizontalPagerFragment();
                return horizontalPagerFragment;
        }
    }

    public Fragment getFragment(int position){
        Log.d(TAG, "getFragment: "+ horizontalPagerFragment);
        return horizontalPagerFragment;
    }
    public MainPagerAdapter getAdapter(){
        return this;
    }

    @Override
    public int getCount() {
        return COUNT;
    }
}
