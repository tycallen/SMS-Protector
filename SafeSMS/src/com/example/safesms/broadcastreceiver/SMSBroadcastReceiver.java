package com.example.safesms.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSBroadcastReceiver";

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String msgContent = "";
        String functiontype = "";
        Bundle bundle = intent.getExtras();
        Object messages[] = (Object[]) bundle.get("pdus");
        SmsMessage smsMessage[] = new SmsMessage[messages.length];
        for (int n = 0; n < messages.length; n++) {
            smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
            msgContent = smsMessage[n].getMessageBody();
            deleteSMS(context, msgContent);
            this.abortBroadcast();
        }
    }

    public void deleteSMS(Context context, String smscontent) {
        try {
            // 准备系统短信收信箱的uri地址
            Uri uri = Uri.parse("content://sms/inbox");// 收信箱
            // 查询收信箱里所有的短信
            Cursor isRead = context.getContentResolver().query(uri, null, "read=" + 0, null, null);
            while (isRead.moveToNext()) {
                // String phone =
                // isRead.getString(isRead.getColumnIndex("address")).trim();//获取发信人
                String body = isRead.getString(isRead.getColumnIndex("body")).trim();// 获取信息内容
                if (body.equals(smscontent)) {
                    int id = isRead.getInt(isRead.getColumnIndex("_id"));

                    context.getContentResolver().delete(Uri.parse("content://sms"), "_id=" + id, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSMS(Context context, String smscontent, String addr) {
        try {
            // 准备系统短信收信箱的uri地址
            Uri uri = Uri.parse("content://sms/inbox");// 收信箱
            // 查询收信箱里所有的短信
            Cursor isRead = context.getContentResolver().query(uri, null, "read=" + 0, null, null);
            while (isRead.moveToNext()) {
                String phone = isRead.getString(isRead.getColumnIndex("address")).trim();// 获取发信人
                String body = isRead.getString(isRead.getColumnIndex("body")).trim();// 获取信息内容
                if (body.equals(smscontent) && phone.equals(addr)) {
                    int id = isRead.getInt(isRead.getColumnIndex("_id"));

                    context.getContentResolver().delete(Uri.parse("content://sms"), "_id=" + id, null);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
