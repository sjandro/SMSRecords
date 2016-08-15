package com.asalazar.alex.smsrecords;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;


public class SendToDatabaseBackgroundService extends Service {

//    private final IBinder mIBinder = new LocalBinder();
//
//    private Handler mHandler = null;

    @Override
    public void onCreate() {

        Utilities utilities = new Utilities();

        utilities.SetAlarm(getApplicationContext());

        //utilities.checkForMessageService(getApplicationContext());

        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if(mHandler != null)
//        {
//            mHandler = null;
//        }
    }


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
        //return mIBinder;
    }
//
//    public class LocalBinder extends Binder
//    {
//        public SendToDatabaseBackgroundService getInstance()
//        {
//            return SendToDatabaseBackgroundService.this;
//        }
//    }
//
//    public void setHandler(Handler handler)
//    {
//        mHandler = handler;
//    }


}
