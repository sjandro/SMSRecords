package com.asalazar.alex.smsrecords;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SyncAdapterType;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by sjand on 5/12/2016.
 */
public class CheckForMessage implements Runnable {

    String information;
    Handler handler;

    public CheckForMessage(String num, Handler handler){
        this.information = num;
        this.handler = handler;
    }

    @Override
    public void run() {

        String message = "";
        try {
            message = checkServer(information);

            if(!message.equals("no_message")) {
                Message msg = new Message();
                Bundle msgBundle = new Bundle();
                msgBundle.putString("msg", message);
                msg.setData(msgBundle);
                handler.sendMessage(msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String checkServer(String number) throws IOException {
        BufferedReader in = null;

        try{
            System.out.println("Fetching Message!");
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet request = new HttpGet();
            URI website = new URI("http://107.170.82.86/SMS?key=alex_8138431896&action=check_messages&hash="+number);
            request.setURI(website);
            HttpResponse response = httpclient.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String message = in.readLine();
            System.out.println("(CheckForMessage)HTTP output: "+message);
            return message;

        }
        catch(Exception e){
            System.out.println("Error in http connection " + e.toString());
        }

        return "Error...";
    }
}
