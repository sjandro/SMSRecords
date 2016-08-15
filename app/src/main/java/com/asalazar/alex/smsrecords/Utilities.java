package com.asalazar.alex.smsrecords;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.csvreader.CsvWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sjand on 2/19/2016.
 */
public class Utilities {

    public String chooseColor(int digit) {
        switch (digit) {
            case 0:
                return "#009688";
            case 1:
                return "#FFC107";
            case 2:
                return "#607D8B";
            case 3:
                return "#3F51B5";
            case 4:
                return "#2196F3";
            case 5:
                return "#8BC34A";
            case 6:
                return "#F44336";
            case 7:
                return "#9E9E9E";
            case 8:
                return "#FF9800";
            case 9:
                return "#9C27B0";
            default:
                return "#03A9F4";
        }
    }

    public String chooseDarkColor(int digit) {
        switch (digit) {
            case 0:
                return "#00796B";
            case 1:
                return "#FFA000";
            case 2:
                return "#455A64";
            case 3:
                return "#303F9F";
            case 4:
                return "#1976D2";
            case 5:
                return "#689F38";
            case 6:
                return "#D32F2F";
            case 7:
                return "#616161";
            case 8:
                return "#F57C00";
            case 9:
                return "#7B1FA2";
            default:
                return "#03A9F4";
        }
    }

    public int getOval(int digit, Context context) {
        switch (digit) {
            case 0:
                //return context.getDrawable(R.drawable.oval);
            return R.drawable.oval;
            case 1:
                //return context.getDrawable(R.drawable.oval1);
            return R.drawable.oval1;
            case 2:
                //return context.getDrawable(R.drawable.oval2);
                return R.drawable.oval2;
            case 3:
                //return context.getDrawable(R.drawable.oval3);
                return R.drawable.oval3;
            case 4:
                //return context.getDrawable(R.drawable.oval4);
                return R.drawable.oval4;
            case 5:
                //return context.getDrawable(R.drawable.oval5);
                return R.drawable.oval5;
            case 6:
                //return context.getDrawable(R.drawable.oval6);
                return R.drawable.oval6;
            case 7:
                //return context.getDrawable(R.drawable.oval7);
                return R.drawable.oval7;
            case 8:
                //return context.getDrawable(R.drawable.oval8);
                return R.drawable.oval8;
            case 9:
                //return context.getDrawable(R.drawable.oval9);
                return R.drawable.oval9;
            default:
                //return context.getDrawable(R.drawable.oval4);
                return R.drawable.oval4;
        }
    }

    public void toastMsg(CharSequence str, Context context) {
        CharSequence text = str;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public String returnDate(String s) {
        Long timestamp = Long.parseLong(s);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        Date finaldate = calendar.getTime();
        String date = finaldate.toString();

        return date;
    }

    public void writeCSV(SMSDictionary records) {
        CsvWriter writer = null;
        List<String> writeList = records.getTextStats();
        DateSort sort = new DateSort(writeList, 2);
        writeList = sort.getSortedDates();
        try {
            String newFile = newFile("SMSData");
            FileWriter file = new FileWriter(newFile, true);
            writer = new CsvWriter(file, ',');
            writer.write("Number");
            writer.write("Received");
            writer.write("Sent");
            writer.endRecord();
            for (int i = 0; i < writeList.size(); ++i) {
                String[] temp = writeList.get(i).split(",");
                System.out.println(temp[0] + " " + temp[1] + " " + temp[2]);
                writer.write(temp[0]);
                writer.write(temp[2]);
                writer.write(temp[1]);
                writer.endRecord();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public String newFile(String fileN) {
        String fileName;
        File f;
        int count = 1;
        while (true) {
            fileName = "sdcard/SMSRecords/" + fileN + count + ".csv";
            f = new File(fileName);
            if (f.isFile() && !f.isDirectory()) {
                count += 1;
                continue;
            } else
                break;
        }
        return fileName;
    }

    public void writeTimeCVS(String number, SMSDictionary records) {
        for (String temp : records.getDateList()) {
            if (temp.contains(number))
                System.out.println(temp);

        }
        CsvWriter writer = null;

        try {
            String newFile = newFile(number + "_Logs");
            FileWriter file = new FileWriter(newFile, true);
            writer = new CsvWriter(file, ',');
            writer.write("Number");
            writer.write("Date");
            writer.write("S/R");
            writer.endRecord();
            for (String temp : records.getDateList()) {
                String[] data = temp.split(",");
                if (number == "all") {
                    writer.write(data[0]);
                    writer.write(returnDate(data[1]));
                    writer.write(data[2]);
                    writer.endRecord();
                } else if (temp.contains(number)) {
                    writer.write(data[0]);
                    writer.write(returnDate(data[1]));
                    writer.write(data[2]);
                    writer.endRecord();
                }

            }
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void writeRecordEntryCSV(String entry) {
        CsvWriter writer = null;

        try {
            String newFile = newFile("Initial_Entry");
            FileWriter file = new FileWriter(newFile, true);
            writer = new CsvWriter(file, ',');
            writer.write(entry);
            writer.endRecord();
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    public HashMap<String, String> ReadPhoneContacts(Context cntx) //This Context parameter is nothing but your Activity class's Context
    {
        HashMap<String, String> contacts = new HashMap<>();
        Cursor cursor = cntx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        Integer contactsCount = cursor.getCount(); // get how many contacts you have in your contacts list
        if (contactsCount > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    //the below cursor will give you details for multiple contacts
                    Cursor pCursor = cntx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    // continue till this cursor reaches to all phone numbers which are associated with a contact in the contact list
                    while (pCursor.moveToNext()) {
                        int phoneType = pCursor.getInt(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        String phoneNo = pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        String address = phoneNo.replaceAll("[+()\\s-]+", "");
                        if(address.startsWith("1"))
                            address = address.substring(1);

                        contacts.put(address, contactName);
                    }
                    pCursor.close();
                }
            }
            cursor.close();
        }
        return contacts;
    }

    public String getMyNumber(Context context){
        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber;
        mPhoneNumber = tMgr.getLine1Number();
        if(mPhoneNumber != null) {
            mPhoneNumber = mPhoneNumber.replaceAll("[+()\\s-]+", "");
            if (mPhoneNumber.startsWith("1"))
                mPhoneNumber = mPhoneNumber.substring(1);

        }
        else{
            mPhoneNumber = "no_phone_number";
        }

        //System.out.println("NUMBER: " + mPhoneNumber);
        return mPhoneNumber;
    }


    public void fetchInbox(Context context)
    {
        Uri inbox = Uri.parse("content://sms/inbox");
        Uri sim = Uri.parse("content://sms/sent");
        String[] reqCols = new String[]{"_id","address","body","read","date"};
        Cursor cursor = context.getContentResolver().query(inbox, reqCols,null,null,null);
        Cursor simCur = context.getContentResolver().query(sim,reqCols,null,null,null);
        boolean inboxCountZero = cursor.getCount() == 0;
        boolean outboxCountZero = simCur.getCount() == 0;
        if(inboxCountZero && outboxCountZero){
            cursor.close();
            simCur.close();
            return;
        }
        else{
            try {
                storeMessageCount(cursor, "received", context);
                storeMessageCount(simCur, "sent", context);
            }
            catch (Exception ex){
                System.out.println("fetchInbox(Utilities): " +ex);
            }

        }
    }

    public void storeMessageCount(Cursor cursor, String type, Context context){
        String dateType;
        ArrayList<SMSRecordObj> mySMSRecordObjs = new ArrayList<>();
        PrivateData myNumberHash = new PrivateData(getMyNumber(context).substring(3,getMyNumber(context).length()));
        if(type.equals("received")) {
            dateType = "RECEIVED";
        }
        else{
            dateType = "SENT";
        }

        String address;
        cursor.moveToFirst();
        while(true) {
            String date = returnDate(cursor.getString(cursor.getColumnIndex("date")));

            address = cursor.getString(cursor.getColumnIndex("address"));
            address = address.replaceAll("[+()\\s-]+", "");
            if(address.startsWith("1"))
                address = address.substring(1);



            //Data that is sent to the database
            PrivateData privateData = new PrivateData(address.substring(3, address.length()));

            SMSRecordObj mySMSRecordObj = new SMSRecordObj();

            mySMSRecordObj.setNum(privateData.getNumber());
            mySMSRecordObj.setSr(dateType);
            mySMSRecordObj.setDate(date);
            //System.out.println(date);
            mySMSRecordObj.setTextLength(cursor.getString(cursor.getColumnIndex("body")).length());
            mySMSRecordObj.setMyNum(myNumberHash.getNumber());
            mySMSRecordObj.setMessage("");
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String dateSort = sdf.format(cursor.getColumnIndex("date"));
            mySMSRecordObj.setDateMilliseconds(cursor.getString(cursor.getColumnIndex("date")));

            String dateParts = returnDate(cursor.getString(cursor.getColumnIndex("date")));
            PrivateData idHash = new PrivateData(privateData.getNumber() + dateParts);
            mySMSRecordObj.setId(idHash.getNumber());
            mySMSRecordObjs.add(mySMSRecordObj);
            //Data that is sent to the database

            if(!cursor.moveToNext())
                break;
        }
        cursor.close();

        Runnable r = new AddRecordThread(mySMSRecordObjs);
        new Thread(r).start();
    }

    public void SetAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SendDataToDatabaseBackgroundBroadcast.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 215215, intent, 0);
        intent.putExtra("send_records", true);
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60000 , pi);
    }

    public void checkForMessageService(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SendDataToDatabaseBackgroundBroadcast.class);
        intent.putExtra("checking_for_message", true);
        PendingIntent pi = PendingIntent.getBroadcast(context, 432215, intent, 0);
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60000 , pi);
    }
}
