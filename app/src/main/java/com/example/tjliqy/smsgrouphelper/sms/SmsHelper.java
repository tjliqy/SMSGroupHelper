package com.example.tjliqy.smsgrouphelper.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by tjliqy on 2016/9/2.
 */
public class SmsHelper {


    String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

    private Context context;

    public SmsHelper(Context context) {
        this.context = context;
    }

    public void sendSMS(String pno, int pos, String body) {

        SmsManager smsManager = SmsManager.getDefault();
        Intent send = new Intent(SENT_SMS_ACTION);
        send.putExtra("id",pos);
        Log.d("lqy",pos+"");
        // 短信发送广播
        PendingIntent sendPI = PendingIntent.getBroadcast(context, 0, send, 0);
        Intent delive = new Intent(DELIVERED_SMS_ACTION);
        // 发送结果广播
        PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0, delive, 0);

        if (body.length()>70){
            ArrayList<String> bodyList = smsManager.divideMessage(body);
            ArrayList<PendingIntent> sendPIList = new ArrayList<>();
            sendPIList.add(sendPI);
            ArrayList<PendingIntent> deliverPIList = new ArrayList<>();
            sendPIList.add(deliverPI);
            smsManager.sendMultipartTextMessage(pno, null, bodyList,sendPIList,deliverPIList);
        }else if(body.length()>140){
            Toast.makeText(context,"短信长度过长",Toast.LENGTH_SHORT).show();
        }else {
            smsManager.sendTextMessage(pno, null, body, sendPI, deliverPI);
        }
    }
}
