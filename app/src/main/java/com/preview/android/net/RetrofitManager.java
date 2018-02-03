package com.preview.android.net;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 描述: 网络请求管理器
 */
public class RetrofitManager {
    private static final int TIMEOUT = 10;
    private static RetrofitManager httpManager;
    private        Retrofit        retrofit;

    /**
     * 私有构造，完成 client 的创建
     */
    private RetrofitManager() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(genericHeader());
        builder.retryOnConnectionFailure(true);

        retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.192:2661/")
                                         .client(builder.build())
                                         .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                         .addConverterFactory(GsonConverterFactory.create())
                                         .build();
    }

    /**
     * 私有构造，完成 client 的创建
     */
    private RetrofitManager(String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
//        builder.addInterceptor(genericHeader());
        builder.retryOnConnectionFailure(true);

        retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                                         .client(builder.build())
                                         .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                         .addConverterFactory(GsonConverterFactory.create())
                                         .build();
    }

    /**
     * 获取单例
     */
    public static Retrofit getInstance() {
        if (httpManager == null) {
            httpManager = new RetrofitManager();
        }
        return httpManager.retrofit;
    }

    /**
     * 获取单例
     */
    public static Retrofit getInstance(String baseUrl) {
        if (httpManager == null) {
            httpManager = new RetrofitManager(baseUrl);
        }
        return httpManager.retrofit;
    }


    /**
     * 添加统一的请求头
     */
    private Interceptor genericHeader() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                                       .newBuilder()
                                       .addHeader("androidapp", "true")
                                       .build();
                return chain.proceed(request);
            }
        };
    }


}
