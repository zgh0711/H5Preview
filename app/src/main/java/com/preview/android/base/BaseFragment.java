package com.preview.android.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.preview.android.event.EventCenter;
import com.preview.android.view.TitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by ZGH on 2017/4/18.
 */

public abstract class BaseFragment extends SupportFragment {
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME  = 2000L;
    private              long TOUCH_TIME = 0;

    public View      mView;
    public TitleView mTitleView;

    public BaseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (getArguments() != null) {
            getBundleExtras(getArguments());
        }
        EventBus.getDefault().register(this);
        mView = initContentView(inflater, container, savedInstanceState);
        return mView;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initView(mView);
    }

    /**
     * 初始化 UI
     */
    protected abstract View initContentView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * 处理传入的数据
     */
    protected abstract void getBundleExtras(Bundle arguments);

    /**
     * 初始化控件
     */
    protected abstract void initView(View view);

    /**
     * EventBus 接收消息
     */
    @Subscribe
    public void onEventAsync(EventCenter event) {
        if (event != null) {
            onEventComing(event);
        }
    }

    /**
     * 接收消息后的处理逻辑
     */
    protected abstract void onEventComing(EventCenter event);

    /**
     * 处理回退事件
     */
    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            Toast.makeText(_mActivity, "再按一次退出", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
