package com.example.tjliqy.smsgrouphelper.module;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Producer;
import rx.Subscriber;

/**
 * Created by tjliqy on 2016/9/5.
 */
public class ProgressSubscriber<T> extends Subscriber<T> {

    private HttpOnNextListener mSubscriberOnNextListener;

    private WeakReference<Context> mActivity;

    private boolean cancel;

    private ProgressDialog pd;

    public ProgressSubscriber(HttpOnNextListener mSubscriberOnNextListener, Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.mActivity = new WeakReference<Context>(context);
        this.cancel = false;
        initProgressDialog();
    }

    public ProgressSubscriber(HttpOnNextListener mSubscriberOnNextListener, Context context, boolean cancel) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.mActivity = new WeakReference<Context>(context);
        this.cancel = cancel;
        initProgressDialog();
    }

    private void initProgressDialog(){
        Context context = mActivity.get();
        if(pd == null && context != null){
            pd = new ProgressDialog(context);
            pd.setCancelable(cancel);
            if (cancel){
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        onCancelProgress();
                    }
                });
            }
        }
    }

    private void showProgressDialog(){
        Context context = mActivity.get();
        if(pd == null || context == null){
            return;
        }
        if (!pd.isShowing()){
            pd.show();
        }
    }

    private void dismissProgressDialog(){
        if (pd != null && pd.isShowing()){
            pd.dismiss();
        }
    }

    @Override
    public void onStart() {
        showProgressDialog();
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        Context context = mActivity.get();
        if(context == null){
            return;
        }
        if(e instanceof SocketTimeoutException){
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        }else if(e instanceof ConnectException){
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "错误" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        dismissProgressDialog();
    }

    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null){
            mSubscriberOnNextListener.onNext(t);
        }
    }

    public void onCancelProgress(){
        if(!this.isUnsubscribed()){
            this.unsubscribe();
        }
    }
}
