package com.asalazar.alex.smsrecords;

/**
 * Created by sjand on 5/6/2016.
 */
public class SMSRecordObj {
    String hash;
    String sr;
    String date;
    int text_length;
    String users_id;
    String message;
    String date_milliseconds;
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setNum(String num){
        this.hash = num;
    }

    public String getNum(){
        return hash;
    }

    public void setSr(String sr){
        this.sr = sr;
    }

    public String getSR(){
        return sr;
    }
    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return date;
    }
    public void setTextLength(int text){
        this.text_length = text;
    }

    public int getTextLength(){
        return text_length;
    }

    public void setMyNum(String myNum){
        this.users_id = myNum;
    }

    public String getMyNum(){
        return users_id;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public void setDateMilliseconds(String dateMilliseconds){
        this.date_milliseconds = dateMilliseconds;
    }

    public String getDateMilliseconds(){
        return date_milliseconds;
    }

}
