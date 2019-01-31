package com.example.williamrudwall.smsappen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";

    //Hämtar alla värden från smset som skickades
    @Override
    public void onReceive(Context context, Intent intent) {
       Bundle intentExtras = intent.getExtras();

       if (intentExtras != null) {
           Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);

           String smsMessageStr = "";

           for(int i = 0; i < sms.length; i++){
               SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

               String smsBody = smsMessage.getMessageBody().toString();
               String adress = smsMessage.getOriginatingAddress();
               long timeMillis = smsMessage.getTimestampMillis();

               Date date = new Date(timeMillis);
               SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
               String dateText = format.format(date);

               smsMessageStr += adress + " at " + " \t" + dateText + "\n";
               smsMessageStr += smsBody + "\n";
           }

           Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();
           ReceiveSmsActivity inst = ReceiveSmsActivity.instance();
           if(inst != null) {
               inst.updateList(smsMessageStr);
           }
       }
    }
}
