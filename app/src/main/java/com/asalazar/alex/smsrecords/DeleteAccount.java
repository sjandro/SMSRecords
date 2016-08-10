package com.asalazar.alex.smsrecords;

import android.os.Bundle;
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
 * Created by sjand on 5/20/2016.
 */
public class DeleteAccount implements Runnable {
    String hash;

    public DeleteAccount(String hash) {
        this.hash = hash;
    }

    @Override
    public void run() {
        deleteUserFromDatabase();
        System.exit(0);
    }

    public String deleteUserFromDatabase(){
        BufferedReader in = null;

        try{
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet request = new HttpGet();
            URI website = new URI("http://107.170.82.86/SMS?key=alex_8138431896&action=delete_account&hash="+hash);
            request.setURI(website);
            HttpResponse response = httpclient.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String message = in.readLine();
            System.out.println("(DeleteAccount)HTTP output: "+message);
            return message;

        }
        catch(Exception e){
            System.out.println("Error in http connection " + e.toString());
        }

        return "Error...";
    }
}
