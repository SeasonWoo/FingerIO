package com.example.demo.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo.TRaxGame;
import com.gigamole.infinitecycleviewpager.VerticalInfiniteCycleViewPager;
import com.example.demo.R;
import com.example.demo.utils.Utils;

import static com.example.demo.utils.Utils.setupItem;

/**
 * Created by GIGAMOLE on 7/27/16.
 */
public class HorizontalPagerAdapter extends PagerAdapter {
    private static final String TAG = "HorizontalPagerAdapter";
    //定义每个Item的图片和文字
    private final Utils.LibraryObject[] LIBRARIES = new Utils.LibraryObject[]{
            new Utils.LibraryObject(
                    R.drawable.greendragon,
                    "Trax game"
            ),
            new Utils.LibraryObject(
                    R.drawable.gesturephasedemo,
                    "GesturephaseDemo"
            ),
            new Utils.LibraryObject(
                    R.drawable.greendragon,
                    "Echotype Input"
            ),
//            new Utils.LibraryObject(
//                    R.drawable.ic_development,
//                    "Development"
//            ),
//            new Utils.LibraryObject(
//                    R.drawable.ic_qa,
//                    "Quality Assurance"
//            )
    };

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private boolean mIsTwoWay;

    public HorizontalPagerAdapter(final Context context, final boolean isTwoWay) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mIsTwoWay = isTwoWay;
    }

    @Override
    public int getCount() {
        return mIsTwoWay ? 6 : LIBRARIES.length;
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }



    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view;
        if (mIsTwoWay) { //可以上下滑，且可以左右滑（本项目中没有用到）
            view = mLayoutInflater.inflate(R.layout.two_way_item, container, false);

            final VerticalInfiniteCycleViewPager verticalInfiniteCycleViewPager =
                    (VerticalInfiniteCycleViewPager) view.findViewById(R.id.vicvp);
            verticalInfiniteCycleViewPager.setAdapter(
                    new VerticalPagerAdapter(mContext)
            );
            verticalInfiniteCycleViewPager.setCurrentItem(position);
        } else { //只可以左右滑
            view = mLayoutInflater.inflate(R.layout.item, container, false);
            setupItem(view, LIBRARIES[position]);
        }

        container.addView(view);
        //这里设置每个Item的点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: 222");
                Intent intent = new Intent(mContext, TRaxGame.class);
                mContext.startActivity(intent);
            }
        });
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