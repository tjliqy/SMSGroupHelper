package com.example.tjliqy.smsgrouphelper.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tjliqy on 2016/9/9.
 */
public class AddList {
    private List<Address> list;

    private volatile static AddList INSTANCE;

    public static AddList getInstance(){
        if (INSTANCE == null){
            synchronized (AddList.class){
                if (INSTANCE == null){
                    INSTANCE = new AddList();
                }
            }
        }
        return INSTANCE;
    }

    public synchronized List<Address> getList(){
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }
    public synchronized void addAll(List<Address> list){
        if (this.list == null){
            this.list = new ArrayList<>();
        }
        this.list.clear();
        this.list.addAll(list);
    }
    public int size(){
        return this.getList().size();
    }
}
