package com.asalazar.alex.smsrecords;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import com.google.gson.Gson;

/**
 * Created by sjand on 5/6/2016.
 */
public class AddRecordThread implements Runnable {

    PrivateData privateData;
    PrivateData privateData1;
    String sr;
    String date;
    String text;

    ArrayList<SMSRecordObj> mySMSRecordObj;


    public AddRecordThread(String num, String sendOrReceive, String theDate, String textLength, String mynum){

        privateData = new PrivateData(num);
        privateData1 = new PrivateData(mynum);
        sr = sendOrReceive;
        date = theDate;
        text = textLength;

    }

    public AddRecordThread(ArrayList<SMSRecordObj> mySMSRecordObj){
        this.mySMSRecordObj = mySMSRecordObj;
    }

    @Override
    public void run() {

        System.out.println("In new Thread!");

        try {
            sendJsonToDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public String sendToDatabase(String number, String myNumber) throws IOException {
//        BufferedReader in = null;
//        String data = null;
//
//        try{
//            System.out.println("In downloadUrl!");
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpGet request = new HttpGet();
//            URI website = new URI("http://107.170.82.86/SMS?key=alex_8138431896&action=insert_entry&hash="+number+"&sr="+sr+"&date="+date+"&text="+text+"&users_id="+myNumber);
//            request.setURI(website);
//            HttpResponse response = httpclient.execute(request);
//            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//            System.out.println("(AddRecord)HTTP output: " + in.readLine());
//
//            return in.readLine();
//        }
//        catch(Exception e){
//            System.out.println("Error in http connection " + e.toString());
//        }
//
//        return "unsuccessful";
//    }

    public void sendJsonToDatabase() throws IOException{
        BufferedReader in = null;
        try{

            ArrayList<NameValuePair> nameValuePairsList = new ArrayList<>();

            String postUrl = "http://107.170.82.86/post";// put in your url

            Gson gson = new Gson();
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
            HttpPost post = new HttpPost(postUrl);
            System.out.println(gson.toJson(mySMSRecordObj));
            nameValuePairsList.add(new BasicNameValuePair("data", gson.toJson(mySMSRecordObj)));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairsList, "UTF-8"));
            HttpResponse  response = httpClient.execute(post);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            System.out.println("(AddRecord)HTTP output: " + in.readLine());

        }
        catch (Exception e){}
    }
}
