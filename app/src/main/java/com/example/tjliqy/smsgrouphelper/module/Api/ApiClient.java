package com.example.tjliqy.smsgrouphelper.module.api;

import com.example.tjliqy.smsgrouphelper.bean.Address;
import com.example.tjliqy.smsgrouphelper.bean.Bean;
import com.example.tjliqy.smsgrouphelper.bean.EBean;
import com.example.tjliqy.smsgrouphelper.module.BaseEnity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tjliqy on 2016/9/2.
 */
public class ApiClient {

    private volatile static ApiClient INSTANCE;

    private static Api api;

    private ApiClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        String BASE_URL = "http://recruit.twtstudio.com/api.php/api/";
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }

    public static ApiClient getInstance(){
        if(INSTANCE == null){
            synchronized (ApiClient.class){
                if(INSTANCE == null){
                    INSTANCE = new ApiClient();
                }
            }
        }
        return INSTANCE;
    }
    public void getExMsg(BaseEnity basebar){
        Observable observable = basebar.getObservable(api)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(basebar);

        observable.subscribe(basebar.getSubscrber());
    }

    public void send(Subscriber<EBean> subscriber, int id){
        Observable<EBean> observable = api.send("pEu7AWDahyPZkWXCSQe2Bkua9MxE6Ev5PZsnybxRba-gowIR34rrZQZiyO0R1VqRh3MEt1AdXddctlr-AGUs7w",id);
        observable.subscribeOn(Schedulers.io())//指定事件产生线程io 用于读写文件、读写数据库、网络信息交互
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
