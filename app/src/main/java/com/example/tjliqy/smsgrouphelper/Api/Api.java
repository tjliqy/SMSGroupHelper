package com.example.tjliqy.smsgrouphelper.Api;

import com.example.tjliqy.smsgrouphelper.bean.Bean;
import com.example.tjliqy.smsgrouphelper.bean.EBean;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by tjliqy on 2016/9/2.
 */
public interface Api {
    @GET("message")
    Observable<Bean> getMsg(@Query("token") String token);

    @POST("message")
    Observable<EBean>send(@Query("token")String token, @Field("id") int id);
}
