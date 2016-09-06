package com.example.tjliqy.smsgrouphelper.module;

import com.example.tjliqy.smsgrouphelper.bean.Address;
import com.example.tjliqy.smsgrouphelper.bean.Bean;
import com.example.tjliqy.smsgrouphelper.bean.EBean;
import com.example.tjliqy.smsgrouphelper.module.api.Api;
import com.example.tjliqy.smsgrouphelper.module.api.ApiClient;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by tjliqy on 2016/9/5.
 */
public class SubjectPost extends BaseEnity<Address>{

    private Subscriber mSubscriber;

    private String token;

    public SubjectPost(Subscriber getAddress, String token){
        this.mSubscriber = getAddress;
        this.token = token;
    }

    @Override
    public Observable getObservable(Api apiClient) {
        return apiClient.getAdd(token);
    }

    @Override
    public Subscriber getSubscrber() {
        return  mSubscriber;
    }
}
