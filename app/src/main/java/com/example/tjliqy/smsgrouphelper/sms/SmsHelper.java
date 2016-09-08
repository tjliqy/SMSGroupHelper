package com.example.tjliqy.smsgrouphelper.sms;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.tjliqy.smsgrouphelper.bean.Address;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by tjliqy on 2016/9/2.
 */
public class SmsHelper {


    private static final String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    private static final String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

    private static final Uri SMS_ALL = Uri.parse("content://sms/");//全部短信
    private static final Uri SMS_INBOX = Uri.parse("content://sms/inbox");//收件箱

    private static final String P_NUM = "address";//手机号码
    private static final String P_DATE = "date";//发送日期
    private static final String P_DETAIL = "body";//短信内容
    private Context context;

    public SmsHelper(Context context) {
        this.context = context;
    }

    public void sendSMS(String pno, int pos, String body) {
        SmsManager smsManager = SmsManager.getDefault();
        Intent send = new Intent(SENT_SMS_ACTION);
        send.putExtra("id", pos);
        Log.d("lqy", pos + "");
        // 短信发送广播
        PendingIntent sendPI = PendingIntent.getBroadcast(context, 0, send, 0);
        Intent delive = new Intent(DELIVERED_SMS_ACTION);
        // 发送结果广播
        PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0, delive, 0);

        if (body.length() > 70) {
            ArrayList<String> bodyList = smsManager.divideMessage(body);
            ArrayList<PendingIntent> sendPIList = new ArrayList<>();
            sendPIList.add(sendPI);
            ArrayList<PendingIntent> deliverPIList = new ArrayList<>();
            sendPIList.add(deliverPI);
            smsManager.sendMultipartTextMessage(pno, null, bodyList, sendPIList, deliverPIList);
        } else if (body.length() > 140) {
            Toast.makeText(context, "短信长度过长", Toast.LENGTH_SHORT).show();
        } else {
            smsManager.sendTextMessage(pno, null, body, sendPI, deliverPI);
        }
    }

    public List<Address> getSmsFromPhone() {
        List<Address> addressList = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[]{P_DATE, P_DETAIL, P_NUM};
        String where = P_DATE + " > " + (System.currentTimeMillis() - 10 * 60 * 1000);
        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        if (null == cur) {
            return addressList;
        }
        if (cur.moveToNext()) {
            do {
                Address address = new Address();
                String number = cur.getString(cur.getColumnIndex(P_NUM));//手机号
//                String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名
                String body = cur.getString(cur.getColumnIndex(P_DETAIL));
                address.setPhone(number);
//                address.setRealname(name);
                address.setDetail(body);
                addressList.add(address);
            }while(cur.moveToNext());
        }
        cur.close();
        return addressList;
    }

    public String getSmsDetailByNum(String num){
        String detail = "";
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[]{P_DETAIL};
        String where =P_NUM + " = " + num ;// AND" +  P_DATE + " > " + (System.currentTimeMillis() - 10 * 60 * 1000)
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        if(null == cur){
            return detail;
        }
        if(cur.moveToNext()){
            detail = cur.getString(cur.getColumnIndex(P_DETAIL));
        }
        return detail;
    }
}
