package com.example.tjliqy.smsgrouphelper.module;

import com.example.tjliqy.smsgrouphelper.bean.Bean;
import com.example.tjliqy.smsgrouphelper.module.api.Api;
import com.example.tjliqy.smsgrouphelper.module.api.ApiClient;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by tjliqy on 2016/9/5.
 */
public abstract class BaseEnity<T> implements Func1<Bean<T>, T> {

    public abstract Observable getObservable(Api apiClient);

    public abstract Subscriber getSubscrber();

    @Override
    public T call(Bean<T> tBean) {
        if (tBean.getErrno() == 1){
            throw new HttpTimeException(tBean.getErrmsg());
        }
        return tBean.getData();
    }
}
