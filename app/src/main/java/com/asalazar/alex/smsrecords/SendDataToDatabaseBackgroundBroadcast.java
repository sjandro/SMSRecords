package com.asalazar.alex.smsrecords;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sjand on 6/26/2016.
 */
public class SendDataToDatabaseBackgroundBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("SMS Service Started!");
//        if(intent.getBooleanExtra("checking_for_message", false)){
//            System.out.println("checking for message");
//        }
        //else if(intent.getBooleanExtra("send_records", false)){
            Utilities utilities = new Utilities();
            utilities.fetchInbox(context);
        //}
        //some comment
        //another comment
    }
}
