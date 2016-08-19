package com.asalazar.alex.smsrecords;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by sjand on 5/5/2016.
 */
public class AddUserThread implements Runnable {

    String information;

    public AddUserThread(String num){
        information = num;
    }

    @Override
    public void run() {
        System.out.println("In new Thread!");

        try {
            sendToDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToDatabase() throws IOException {
        BufferedReader in = null;
        String data = null;

        try{
            System.out.println("In downloadUrl!");
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet request = new HttpGet();
            URI website = new URI("http://107.170.82.86/SMS?key=alex_8138431896&action=insert_user&hash="+information);
            request.setURI(website);
            HttpResponse response = httpclient.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            System.out.println("(AddUser)HTTP output: "+in.readLine());
        }
        catch(Exception e){
            System.out.println("Error in http connection " + e.toString());
        }
    }
}

//Test Soheil