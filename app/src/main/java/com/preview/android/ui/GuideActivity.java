package com.preview.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.preview.android.R;

import java.util.ArrayList;

/**
 * @author ZGH
 */
public class GuideActivity extends AppCompatActivity {
    private ArrayList<ImageView> mGuides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
    }

    private void initView() {
        final int[] pictures =
                new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3, R.mipmap.guide_4};

        mGuides = new ArrayList<>();
        for (int picture : pictures) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(picture);
            mGuides.add(imageView);
        }

        final ViewPager viewPager = findViewById(R.id.vp_guide);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mGuides.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                // 从Viewpager中移除
                container.removeView((View) object);
            }

            //viewPager的预加载功能。默认情况 预加载前一个页面和后一个页面 ，
            // 如果前后没有pager 不加载，默认下最多有3个页面缓存加载，加载到第四个的时候会把第一个页面销毁
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // 获取View
                View child = mGuides.get(position);
                // 添加View
                container.addView(child);
                return child;
            }
        });

        Button button = findViewById(R.id.btn_guide);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();
                if (position == mGuides.size() -1) {
                    startActivity(new Intent(GuideActivity.this, WebActivity.class));
                    finish();
                } else {
                    viewPager.setCurrentItem(position + 1);
                }
            }
        });
    }
}
