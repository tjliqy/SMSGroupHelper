package com.example.tjliqy.smsgrouphelper.support;

import com.example.tjliqy.smsgrouphelper.bean.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tjliqy on 2016/9/8.
 */
public class Str2ListHelper {
    /*
    * 将储存的Address化为如下字符串
    * realname1,13312345678|realname2,18912345678|
    */
    public static List<Address> String2List(String s){
        List<Address> list = new ArrayList<>();
        String[] temp = s.split("\\|");
        for (String t : temp) {
            if (!"".equals(t)) {
                Address add = new Address();
                String[] addS = t.split(",");
                add.setRealname(addS[0]);
                add.setPhone(addS[1]);
                list.add(add);
            }
        }
        return list;
    }

    public static String List2String(List<Address> list){
        String s = "";
        for (Address add : list) {
            s += add.getRealname() + "," + add.getPhone() + "|";
        }
        return s;
    }
}
