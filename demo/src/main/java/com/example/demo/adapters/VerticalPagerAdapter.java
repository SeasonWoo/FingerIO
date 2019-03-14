package com.example.demo.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo.R;
import com.example.demo.utils.Utils;

import static com.example.demo.utils.Utils.setupItem;

/**
 * Created by GIGAMOLE on 7/27/16.
 */
public class VerticalPagerAdapter extends PagerAdapter {

    private final Utils.LibraryObject[] TWO_WAY_LIBRARIES = new Utils.LibraryObject[]{
            new Utils.LibraryObject(
                    R.drawable.greendragon,
                    "Fintech"
            ),
            new Utils.LibraryObject(
                    R.drawable.greendragon,
                    "Delivery"
            ),
            new Utils.LibraryObject(
                    R.drawable.greendragon,
                    "Social network"
            ),
            new Utils.LibraryObject(
                    R.drawable.greendragon,
                    "E-commerce"
            ),
            new Utils.LibraryObject(
                    R.drawable.greendragon,
                    "Wearable"
            ),
            new Utils.LibraryObject(
                    R.drawable.greendragon,
                    "Internet of things"
            )
    };

    private LayoutInflater mLayoutInflater;

    public VerticalPagerAdapter(final Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return TWO_WAY_LIBRARIES.length;
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view = mLayoutInflater.inflate(R.layout.item, container, false);

        setupItem(view, TWO_WAY_LIBRARIES[position]);

        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }
}