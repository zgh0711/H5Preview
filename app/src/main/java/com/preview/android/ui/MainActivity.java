package com.preview.android.ui;

import android.os.Bundle;

import com.preview.android.R;
import com.preview.android.base.BaseActivity;
import com.preview.android.event.EventCenter;

/**
 * @author ZGH
 */
public class MainActivity extends BaseActivity {
//    private ArrayList<Fragment>        fragments       = new ArrayList<>();
//    private ArrayList<CustomTabEntity> tabEntities     = new ArrayList<>();
//    private String[]                   titles          = {"首页", "商城", "竞猜", "发现", "我的"};
//    private int[]                      unSelectedIcons = {
//            R.mipmap.ic_home, R.mipmap.ic_mall, R.mipmap.ic_quiz, R.mipmap.ic_discover,
//            R.mipmap.ic_mine};
//    private int[]                      selectedIcons   = {
//            R.mipmap.ic_home_blue, R.mipmap.ic_mall_blue, R.mipmap.ic_quiz_blue,
//            R.mipmap.ic_discover_blue, R.mipmap.ic_mine_blue};


    @Override
    protected void initContentView(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_main);
        setStatusBarColor(this, getResources().getColor(R.color.colorPrimary), 0);
//        viewPagerWithFragment();
    }

//    private void viewPagerWithFragment() {
//        for (int i = 0; i < titles.length; i++) {
//            tabEntities.add(new TabEntity(titles[i], selectedIcons[i], unSelectedIcons[i]));
////            fragments.add(BlankFragment.newInstance());
//            fragments.add(WebViewFragment.newInstance(titles[i]));
//        }
//
//        final CommonTabLayout tabLayout = findViewById(R.id.tabLayout_main);
//        tabLayout.setTabData(tabEntities);
//
//        final ViewPager viewPager = findViewById(R.id.vp_main);
//        viewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager(), fragments, titles));
//
//        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelect(int position) {
//                viewPager.setCurrentItem(position);
//            }
//
//            @Override
//            public void onTabReselect(int position) {
//
//            }
//        });
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                tabLayout.setCurrentTab(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//    }

    @Override
    protected void initView() {

    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void onEventComing(EventCenter event) {

    }

}
