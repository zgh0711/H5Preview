package com.preview.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.preview.android.App;
import com.preview.android.R;
import com.preview.android.constant.Urls;
import com.preview.android.model.ApiEntity;
import com.preview.android.net.Api;
import com.preview.android.net.RetrofitManager;
import com.preview.android.util.ImageManager;
import com.preview.android.util.JsonTools;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ZGH
 */
public class SplashActivity extends AppCompatActivity {
    private static boolean isAdClicked = false;
    private Disposable disposable;
    private Handler handler = new Handler();
    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        initView();
        getUrls();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }, 2000);
    }

    private void getUrls() {
        Api api = RetrofitManager.getInstance(Urls.API_URL).create(Api.class);
        api.getSettings(AppUtils.getAppVersionCode(), "android")
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe(new Observer<ApiEntity>() {
               @Override
               public void onSubscribe(@NonNull Disposable d) {
                   disposable = d;
               }

               @Override
               public void onNext(@NonNull ApiEntity apiEntity) {
                   try {
                       LogUtils.json(JsonTools.buildJsonStr(apiEntity));
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   ApiEntity.DataEntity dataEntity = apiEntity.getData();
                   if (dataEntity != null) {
                       if (!TextUtils.isEmpty(dataEntity.getBaseurl())) {
                           Urls.BASE_URL = dataEntity.getBaseurl();
                       }
                       if (!TextUtils.isEmpty(dataEntity.getAdimg())) {
                           Urls.AD_IMG = dataEntity.getAdimg();
                           ImageManager.getInstance()
                                       .withNoDefault(App.getAppContext(), Urls.AD_IMG, mImageView);
                       }
                       if (!TextUtils.isEmpty(dataEntity.getAdurl())) {
                           Urls.AD_URL = dataEntity.getAdurl();
                       }
                       if (!TextUtils.isEmpty(dataEntity.getAndroidurl())) {
                           Urls.VER_URL = dataEntity.getAndroidurl();
                       }
                       if (!TextUtils.isEmpty(dataEntity.getAndroidcode())) {
                           Urls.VER_Code = Integer.parseInt(dataEntity.getAndroidcode());
                       }
                   }

               }

               @Override
               public void onError(@NonNull Throwable e) {
                   LogUtils.d(e.toString());
               }

               @Override
               public void onComplete() {
               }
           });
    }

    private void start() {
        //如果点击了广告那么停止启动应用，没点广告的话就按正常情况启动
        if (isAdClicked) {
            return;
        } else {
            isFirst();
        }
        finish();
    }

    private void isFirst() {
        //判断是否是第一次进入应用
//        if (!TextUtils.isEmpty(SPUtils.getInstance().getString(SPKeys.IS_FIRST))) {
//            startActivity(new Intent(this, WebActivity.class));
//        } else {
//            String deviceId = DeviceUtils.getAndroidID();
//            SPUtils.getInstance().put(SPKeys.IS_FIRST, deviceId);
//            startActivity(new Intent(this, GuideActivity.class));
//        }

        //先不要引导页，后期需要的话再加上
        startActivity(new Intent(this, WebActivity.class));
    }

    private void initView() {
        mImageView = findViewById(R.id.img_ad);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAdClicked = true;
                if (TextUtils.isEmpty(Urls.AD_URL)) {
                    isFirst();
                    finish();
                } else {
                    //点击广告图片设置标记并跳转页面
                    Intent intent = new Intent();
                    intent.putExtra("url", Urls.AD_URL);
                    intent.setClass(SplashActivity.this, ADActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //从广告页回退时再延迟一点执行启动程序，并改变标记状态
        if (isAdClicked) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            }, 800);
        }
        isAdClicked = false;
    }

    @Override
    protected void onDestroy() {
        //当 Activity 退出时，切断事件传递，以避免继续发送事件到主线程导致程序崩溃
        if (disposable != null) {
            disposable.dispose();
        }
        super.onDestroy();
    }
}
