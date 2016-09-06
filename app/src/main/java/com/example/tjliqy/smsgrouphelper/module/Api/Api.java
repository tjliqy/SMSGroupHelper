package com.example.tjliqy.smsgrouphelper.module.api;

import com.example.tjliqy.smsgrouphelper.bean.Address;
import com.example.tjliqy.smsgrouphelper.bean.Bean;
import com.example.tjliqy.smsgrouphelper.bean.EBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by tjliqy on 2016/9/2.
 */
public interface Api {
    @GET("message")
    Observable<Bean<List<Address>>> getAdd(@Query("token") String token);

    @FormUrlEncoded
    @POST("message")
    Observable<EBean>send(@Query("token")String token, @Field("id") int id);
}
