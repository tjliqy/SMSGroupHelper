package com.example.tjliqy.smsgrouphelper.ui;

import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tjliqy.smsgrouphelper.R;
import com.example.tjliqy.smsgrouphelper.bean.AddList;
import com.example.tjliqy.smsgrouphelper.bean.Address;
import com.example.tjliqy.smsgrouphelper.module.HttpOnNextListener;
import com.example.tjliqy.smsgrouphelper.module.ProgressSubscriber;
import com.example.tjliqy.smsgrouphelper.module.SubjectPost;
import com.example.tjliqy.smsgrouphelper.module.api.ApiClient;
import com.example.tjliqy.smsgrouphelper.sms.SmsHelper;
import com.example.tjliqy.smsgrouphelper.support.PreferenceHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tjliqy on 2016/9/6.
 */
public class SendFragment extends Fragment {


    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.et_f)
    EditText etF;
    @BindView(R.id.et_t)
    EditText etT;
    @BindView(R.id.rl_add)
    RecyclerView rlAdd;
    @BindView(R.id.bt_get)
    Button btGet;
    @BindView(R.id.bt_send)
    Button btSend;


    private SmsHelper helper;

    private ApiClient apiClient;

    private SubjectPost postEntity;

    private RvSendAdapter adapter;

//    private List<Address> beanList;

    int num = 0;

    int endNum;

    String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send, container, false);
        ButterKnife.bind(this,view);


        adapter = new RvSendAdapter(getActivity());
//        beanList = new ArrayList<>();
        rlAdd.setLayoutManager(new LinearLayoutManager(getActivity()));
        postEntity = new SubjectPost(new ProgressSubscriber(getAddOnNextListener,getActivity()),this.getString(R.string.token));
        apiClient = ApiClient.getInstance();

        rlAdd.setAdapter(adapter);
        helper = new SmsHelper(getActivity());

        if (PreferenceHelper.getPreAddList() != null){
            changeList(PreferenceHelper.getPreAddList());
            adapter.notifyDataSetChanged();
        }

        btGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiClient.getExMsg(postEntity);
            }
        });


        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        getActivity().registerReceiver(sendMessage, new IntentFilter(SENT_SMS_ACTION));

        return view;
    }

    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(sendMessage);
        super.onDestroyView();
    }

    private void changeList(List<Address> list){
        AddList.getInstance().addAll(list);
        tvAll.setText("总数: " + AddList.getInstance().size() + "人");
    }

    HttpOnNextListener getAddOnNextListener = new HttpOnNextListener<List<Address>>() {
        @Override
        public void onNext(List<Address> addressList) {
            changeList(addressList);
            endNum = AddList.getInstance().size();
//            PreferenceHelper.setPrefAddList(beanList);
            PreferenceHelper.setPrefAddList(AddList.getInstance().getList());
        }
    };

    BroadcastReceiver sendMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            // 判断短信是否成功
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(getActivity(), num + "发送成功！", Toast.LENGTH_SHORT)
                            .show();
                    // TODO: 2016/9/9 设置状态
//                    beanList.get(num).setSend(true);
//                    adapter.changeStatus(num);
                    new SendTask().execute();
                    break;
                default:
                    Toast.makeText(getActivity(), "发送失败！", Toast.LENGTH_SHORT)
                            .show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("在发送给" + AddList.getInstance().getList().get(num).getRealname() + "时出错，发送中断");
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

    class SendTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            while (num < AddList.getInstance().getList().size() && num < endNum && AddList.getInstance().getList().get(num).getStatus() == 1) {
                num++;
            }
            if (num < AddList.getInstance().getList().size() && num < endNum) {
                if (AddList.getInstance().getList().get(num).getPhone() == null || "".equals(AddList.getInstance().getList().get(num).getPhone())){
                    num++;
                }else {
                    helper.sendSMS(AddList.getInstance().getList().get(num).getPhone(), num, AddList.getInstance().getList().get(num).getDetail());
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
                Toast.makeText(getActivity(), "全部发送完成!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
