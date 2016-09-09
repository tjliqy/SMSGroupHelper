package com.example.tjliqy.smsgrouphelper.ui;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tjliqy.smsgrouphelper.R;
import com.example.tjliqy.smsgrouphelper.bean.AddList;
import com.example.tjliqy.smsgrouphelper.bean.Address;
import com.example.tjliqy.smsgrouphelper.sms.SmsHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tjliqy on 2016/9/8.
 */
public class GotFragment extends Fragment {

    @BindView(R.id.rv_got)
    RecyclerView recyclerView;

    private RvGotAdapter adapter;

    private SmsHelper smsHelper;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_got, container, false);
        ButterKnife.bind(this, view);

        smsHelper = new SmsHelper(getActivity());
        adapter = new RvGotAdapter(getActivity());
        new GotAsync().execute();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        return view;

    }

    class GotAsync extends AsyncTask<Void,Integer,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            for (Address a : AddList.getInstance().getList()){
                a.setResponse(smsHelper.getSmsDetailByNum(a.getPhone()));
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean){
                adapter.notifyDataSetChanged();
            }
        }
    }
}
