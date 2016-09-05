package com.example.tjliqy.smsgrouphelper;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tjliqy.smsgrouphelper.Api.ApiClient;
import com.example.tjliqy.smsgrouphelper.bean.Bean;
import com.example.tjliqy.smsgrouphelper.bean.EBean;
import com.example.tjliqy.smsgrouphelper.sms.SmsHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rl_add)
    RecyclerView rlAdd;
    @BindView(R.id.bt_send)
    Button btSend;
    @BindView(R.id.bt_get)
    Button btGet;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.et_f)
    EditText etF;
    @BindView(R.id.et_t)
    EditText etT;

    private RvAdapter adapter;

    private List<Bean.DataBean> beanList;

    private SmsHelper helper;

    private ApiClient apiClient;

    String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

    int num = 0;
    int endNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        adapter = new RvAdapter(this);
        beanList = new ArrayList<>();

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);


//        init();
        rlAdd.setLayoutManager(new LinearLayoutManager(this));
        apiClient = ApiClient.getInstance();

        btGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                apiClient.getExMsg(new Subscriber<Bean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Bean bean) {
                        beanList.clear();
                        beanList.addAll(bean.getData());
                        adapter.add(beanList);
                        tvAll.setText("总数："+ beanList.size());
                        endNum = beanList.size();
                    }
                });
            }
        });

        rlAdd.setAdapter(adapter);
//        adapter.add(beanList);
        helper = new SmsHelper(this);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("是否开始批量发送？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!etF.getText().toString().isEmpty()){
                            num = Integer.parseInt(etF.getText().toString());
                        }else {
                            num = 0;
                        }
                        if (!etT.getText().toString().isEmpty()){
                            endNum = Integer.parseInt(etT.getText().toString());
                        }
                        new SendTask().execute();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    private void init(){
        for (int i = 0; i < 200; i++) {
            Bean.DataBean addBean = new Bean.DataBean();
            addBean.setRealname("冀辰阳"+i);
            addBean.setPhone("13302083639");
            addBean.setDetail("ceshi");
            addBean.setSend(false);
            beanList.add(addBean);
        }
    }

    public void send() {

    }

    class SendTask extends AsyncTask<Void, Integer, Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            while (num < beanList.size() && num < endNum && beanList.get(num).isSend()) {
                num++;
            }
            if (num < beanList.size() && num < endNum) {
                if (beanList.get(num).getPhone() == null || "".equals(beanList.get(num).getPhone())){
                    num++;
                }else {
                    helper.sendSMS(beanList.get(num).getPhone(), num, beanList.get(num).getDetail());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (!aBoolean){
                Toast.makeText(MainActivity.this, "全部发送完成!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void onResume() {
        super.onResume();
        //注册监听
        registerReceiver(sendMessage, new IntentFilter(SENT_SMS_ACTION));
    }

    BroadcastReceiver sendMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            // 判断短信是否成功
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(MainActivity.this, num + "发送成功！", Toast.LENGTH_SHORT)
                            .show();
                    beanList.get(num).setSend(true);
                    adapter.changeStatus(num);
                    apiClient.send(new Subscriber<EBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(EBean eBean) {
                            if ("0".equals(eBean.getErrno())){
                                new SendTask().execute();
                            }else {
                                Toast.makeText(MainActivity.this,num+"上传发送记录失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    },num);
//                    new SendTask().execute();
                    break;
                default:
                    Toast.makeText(MainActivity.this, "发送失败！", Toast.LENGTH_SHORT)
                            .show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("在发送给" + beanList.get(num).getRealname() + "时出错，发送中断");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                    break;
            }
        }
    };
}
