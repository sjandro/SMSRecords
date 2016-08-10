package com.asalazar.alex.smsrecords;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by sjand on 5/16/2016.
 */
public class SendTextMessageToServer implements Runnable {//extends AsyncTask<String, Void, String>{//implements Runnable {

    String information;
    String hash;
    String myHash;
    String date;
    PostObj postObj;
    String idHash;

    public SendTextMessageToServer(String idHash, String hash, String date, String text, String myHash) {
        this.information = text;
        this.idHash = idHash;
        this.hash = hash;
        this.date = date;
        this.myHash = myHash;
        postObj = new PostObj();
        postObj.setIdHash(this.idHash);
        postObj.setHash(this.hash);
        postObj.setMessage(this.information);
        postObj.setDate(this.date);
        postObj.setMyhash(this.myHash);
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

            String       postUrl       = "http://107.170.82.86/insertTextMessage";// put in your url
            Gson gson          = new Gson();
            HttpClient httpClient    = new DefaultHttpClient();
            HttpPost post          = new HttpPost(postUrl);
            StringEntity postingString = new StringEntity(gson.toJson(postObj));//gson.tojson() converts your pojo to json
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(post);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            System.out.println("(SendTextMessageToServer)HTTP output: "+in.readLine());
        }
        catch(Exception e){
            System.out.println("Error in http connection " + e.toString());
        }
    }

    class PostObj{
        String idHash;
        String hash;
        String message;
        private String date;
        private String myhash;

        public void setIdHash(String hash){
            this.idHash = hash;
        }

        public String getIdHash(){
            return idHash;
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

        public void setDate(String date) {
            this.date = date;
        }

        public String getDate() {
            return date;
        }

        public void setMyhash(String myhash) {
            this.myhash = myhash;
        }

        public String getMyhash(){
            return myhash;
        }
    }

}
