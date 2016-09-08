package com.example.tjliqy.smsgrouphelper.support;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.tjliqy.smsgrouphelper.SMSGroupHelperApp;
import com.example.tjliqy.smsgrouphelper.bean.Address;

import java.util.List;

/**
 * Created by tjliqy on 2016/9/8.
 */
public class PreferenceHelper {


    private static final String PREF_ADD_LIST = "address_list";


    private static SharedPreferences getDefaultSharedPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(SMSGroupHelperApp.getContext());
    }

    public static void setPrefAddList(List<Address> addList){
        String s = Str2ListHelper.List2String(addList);
        getDefaultSharedPreferences().edit().putString(PREF_ADD_LIST,s).commit();
    }

    public static List<Address> getPreAddList(){
        String s = getDefaultSharedPreferences().getString(PREF_ADD_LIST,"");
        List<Address> list = Str2ListHelper.String2List(s);
        return list;
    }
}
