package com.example.tjliqy.smsgrouphelper.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tjliqy.smsgrouphelper.R;
import com.example.tjliqy.smsgrouphelper.SMSGroupHelperApp;
import com.example.tjliqy.smsgrouphelper.bean.Address;
import com.example.tjliqy.smsgrouphelper.sms.SmsHelper;
import com.example.tjliqy.smsgrouphelper.support.PreferenceHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tjliqy on 2016/9/8.
 */
public class GotFragment extends Fragment {

    @BindView(R.id.rv_got)
    RecyclerView recyclerView;

    private RvGotAdapter adapter;

    private List<Address> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_got, container, false);
        ButterKnife.bind(this, view);

        SmsHelper smsHelper = new SmsHelper(getActivity());
        adapter = new RvGotAdapter(getActivity());
        list = PreferenceHelper.getPreAddList();
        for (Address a: list) {
            a.setDetail(smsHelper.getSmsDetailByNum(a.getPhone()));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.add(list);
        adapter.notifyDataSetChanged();

        return view;

    }
}
