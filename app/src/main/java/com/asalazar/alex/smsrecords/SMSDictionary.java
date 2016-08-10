package com.asalazar.alex.smsrecords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by sjand on 12/12/2015.
 */
public class SMSDictionary implements Parcelable {

    Map<String, String> texts_received;
    Map<String, String> texts_sent;
    List<String> textInfoList;
    List<String> dateList;
    //String my_number;

    SMSDictionary() {
        texts_received = new HashMap<String,String>();
        texts_sent = new HashMap<String,String>();
        textInfoList = new ArrayList<String>();
        dateList = new ArrayList<String>();
    }

    public void storeDates(String date){
        dateList.add(date);
    }

    public void sortDates(){
        DateSort sortedDates = new DateSort(dateList, 1);
        dateList = sortedDates.getSortedDates();
    }

    public List<String> getTextStats(){return textInfoList;}

    void insertReceivedItem(String key, String value) {
        texts_received.put(key, value);
    }

    void insertSentItem(String key, String value) {
        texts_sent.put(key, value);
    }

    String getTextReceived(String key) {
        return texts_received.get(key);
    }

    String getTextSent(String key) {
        return texts_sent.get(key);
    }

    List<String> getDateList(){ return dateList; }

    boolean containThisKey(String key, String type) {
        if (type == "received")
            return texts_received.containsKey(key);
        else if (type == "sent")
            return texts_sent.containsKey(key);
        else
            return false;
    }

    void printDict(String type){
        if(type == "received") {
            for (Map.Entry<String, String> entry : texts_received.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
        else if(type == "sent"){
            for (Map.Entry<String, String> entry : texts_sent.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
        else
            System.out.println("Invalid type");
    }

    public void test(){
        if(getLongestDict().equals("sent_longer")){
            for (Map.Entry<String, String> entry : texts_sent.entrySet()) {
                if(texts_received.containsKey(entry.getKey())){
                    continue;
                }
                else{
                    texts_received.put(entry.getKey(),"0");
                }
            }
        }
        else if(getLongestDict().equals("received_longer")){
            for (Map.Entry<String, String> entry : texts_received.entrySet()) {
                if(texts_sent.containsKey(entry.getKey())){
                    continue;
                }
                else{
                    texts_sent.put(entry.getKey(),"0");
                }
            }
        }
        else{}

        printDict("received");
        System.out.println("-----");
        printDict("sent");

    }

    public String convertDictToList(){
        System.out.println("#############################");

        String longestList = getLongestDict();
//        List<String> tempList = new ArrayList<>();
//        List<String> noneReceivedMessages = new ArrayList<String>();

        for (Map.Entry<String, String> entry : texts_received.entrySet()) {
            textInfoList.add(entry.getKey() + "," + entry.getValue());
        }

        for (Map.Entry<String, String> entry : texts_sent.entrySet()) {
            for (int i = 0; i < textInfoList.size(); ++i) {
                if (textInfoList.get(i).contains(entry.getKey())) {
                    textInfoList.set(i, textInfoList.get(i) + "," + entry.getValue());
                }
            }
        }

//        if(longestList.equals("received_longer")) {
//            for (Map.Entry<String, String> entry : texts_received.entrySet()) {
//                textInfoList.add(entry.getKey() + "," + entry.getValue());
//            }
//            for (Map.Entry<String, String> entry : texts_sent.entrySet()) {
//                int count = 0;
//                for (int i = 0; i < textInfoList.size(); ++i) {
//                    if (textInfoList.get(i).contains(entry.getKey())) {
//                        textInfoList.set(i, textInfoList.get(i) + "," + entry.getValue());
//                        count++;
//                    }
//                }
//                if(count == 0)
//                    noneReceivedMessages.add(entry.getKey()+",0,"+entry.getValue());
//
//            }
//            for(int i = 0; i < noneReceivedMessages.size(); ++i)
//                textInfoList.add(noneReceivedMessages.get(i));
//
//            for(int i = 0; i < textInfoList.size(); ++i){
//                String[] temp = textInfoList.get(i).split(",");
//                if(temp.length == 3)
//                    tempList.add(temp[0]+","+temp[2]+","+temp[1]);
//                else
//                    tempList.add(temp[0]+",0,"+temp[1]);
//            }
//            textInfoList = tempList;
//        }
//        else if(longestList.equals("sent_longer")){
//            for (Map.Entry<String, String> entry : texts_sent.entrySet()) {
//                textInfoList.add(entry.getKey() + "," + entry.getValue());
//            }
//            for (Map.Entry<String, String> entry : texts_received.entrySet()) {
//                for (int i = 0; i < textInfoList.size(); ++i) {
//                    if (textInfoList.get(i).contains(entry.getKey()))
//                        textInfoList.set(i, textInfoList.get(i) + "," + entry.getValue());
//                }
//            }
//        }
//        else
//            System.out.println("Error when filling textInfoList.");

        for(int k = 0; k < textInfoList.size(); ++k)
            System.out.println(textInfoList.get(k));

        System.out.println("#############################");

        return longestList;
    }

    public List<String> getRecords(){
        convertDictToList();
//        List<String> tempList = new ArrayList<>();
//        List<String> returnList = new ArrayList<>();
//
//        for(int i = 0; i < textInfoList.size(); ++i){
//            String[] str = textInfoList.get(i).split(",");
//            if(str.length < 3) {
//                String[] temp = new String[3];
//                temp[0] = str[0];
//                temp[1] = str[1];
//                temp[2] = "0";
//                String newVal = temp[0]+","+temp[1]+","+temp[2];
//                tempList.add(newVal);
//            }
//        }
//
//        for (Iterator<String> iter = textInfoList.listIterator(); iter.hasNext(); ) {
//            String[] a = iter.next().split(",");
//            if (a.length <  3) {
//                iter.remove();
//            }
//        }
//
//        for(int i = 0; i < tempList.size(); ++i)
//            textInfoList.add(tempList.get(i));
//
//        for (Iterator<String> iter = textInfoList.listIterator(); iter.hasNext(); ) {
//            String[] a = iter.next().split(",");
//            returnList.add(a[0] + "," + a[1] + "," + a[2]);
//
//        }

        for(int k = 0; k < textInfoList.size(); ++k)
            System.out.println(textInfoList.get(k));


        return textInfoList;
    }

    public String getLongestDict(){
        if(texts_received.size() >= texts_sent.size())
            return "received_longer";
        else if(texts_received.size() <= texts_sent.size())
            return "sent_longer";
        else
            return "";
    }

    public List<String> theseDates(String number){
        List<String> theseDates = new ArrayList<>();
        System.out.println("theseDates Function"+number);
        for(String temp : dateList){
            //System.out.println("theseDates Function: "+temp);
            if(temp.contains(number)){
                theseDates.add(temp);
            }
        }
        return theseDates;
    }





    /**
     * DO NOT MODIFY ANYTHING BELLOW
     * The methods below are for allowing this object to be passed to other activities in a bundle.
     */
    protected SMSDictionary(Parcel in) {
        if (in.readByte() == 0x01) {
            textInfoList = new ArrayList<String>();
            in.readList(textInfoList, String.class.getClassLoader());
        } else {
            textInfoList = null;
        }
        if (in.readByte() == 0x01) {
            dateList = new ArrayList<String>();
            in.readList(dateList, String.class.getClassLoader());
        } else {
            dateList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (textInfoList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(textInfoList);
        }
        if (dateList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(dateList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SMSDictionary> CREATOR = new Parcelable.Creator<SMSDictionary>() {
        @Override
        public SMSDictionary createFromParcel(Parcel in) {
            return new SMSDictionary(in);
        }

        @Override
        public SMSDictionary[] newArray(int size) {
            return new SMSDictionary[size];
        }
    };
}
