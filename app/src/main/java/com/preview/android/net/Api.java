package com.preview.android.net;


import com.preview.android.model.ApiEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit 请求需要的 API
 */

public interface Api {

    @GET("setting")
    Observable<ApiEntity> getSettings(
            @Query("versioncode") int verCode, @Query("app") String appType);
}
