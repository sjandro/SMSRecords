package com.asalazar.alex.smsrecords;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by sjand on 5/16/2016.
 */


public class ReplyToMessage implements Runnable {//extends AsyncTask<String, Void, String>{//implements Runnable {

    String information;
    String hash;
    int id;
    PostObj postObj;

    public ReplyToMessage(int id, String hash,String reply){
        this.information = reply;
        this.id = id;
        this.hash = hash;
        postObj = new PostObj();
        postObj.setId(this.id);
        postObj.setHash(this.hash);
        postObj.setMessage(this.information);
    }

    @Override
    public void run() {

        try {
            sendToServer();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendToServer() throws IOException {
        BufferedReader in = null;

        try{
            System.out.println("Sending Message!");

            String       postUrl       = "http://107.170.82.86/reply";// put in your url
            Gson         gson          = new Gson();
            HttpClient   httpClient    = new DefaultHttpClient();
            HttpPost     post          = new HttpPost(postUrl);
            StringEntity postingString = new StringEntity(gson.toJson(postObj));//gson.tojson() converts your pojo to json
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse  response = httpClient.execute(post);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            System.out.println(in.readLine());
        }
        catch(Exception e){
            System.out.println("Error in http connection " + e.toString());
        }
    }

    class PostObj{
        int id;
        String hash;
        String message;

        public void setId(int id){
            this.id = id;
        }

        public int getId(){
            return id;
        }

        public void setHash(String hash){
            this.hash = hash;
        }

        public String getHash(){
            return hash;
        }

        public void setMessage(String message){
            this.message = message;
        }

        public String getMessage(){
            return message;
        }
    }

}

