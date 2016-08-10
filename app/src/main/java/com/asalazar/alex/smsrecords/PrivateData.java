package com.asalazar.alex.smsrecords;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.*;
import java.util.List;

/**
 * Created by sjand on 2/10/2016.
 */
public class PrivateData{

    String number;

    PrivateData(String numString){
        number = hashMD5(numString);
    }




    public String getNumber(){
        return number;
    }

    public String hashMD5(String numString){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(numString.getBytes());
            byte[] byteData = md.digest();


            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            //number = sb.toString();

            //System.out.println("Digest(in hex format):: " + sb.toString());
            return sb.toString();

//            //convert the byte to hex format method 2
//            StringBuffer hexString = new StringBuffer();
//            for (int i=0;i<byteData.length;i++) {
//                String hex=Integer.toHexString(0xff & byteData[i]);
//                if(hex.length()==1) hexString.append('0');
//                hexString.append(hex);
//            }
//            System.out.println("Digest(in hex format):: " + hexString.toString());
        }
        catch(NoSuchAlgorithmException ex){
            System.out.println("Error");
            return "Error";
        }
    }

//    public void createfile(){
//        try {
//            File directory = new File(Environment.getExternalStorageDirectory()+ File.separator+"SMSRecords");
//            directory.mkdirs();
//            File file = new File("sdcard/SMSRecords/database.txt");
//            FileWriter fw = new FileWriter(file.getAbsoluteFile());
//            BufferedWriter bw = new BufferedWriter(fw);
//            // if file doesnt exists, then create it
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//            for(int i = 0; i < records.size(); ++i){
//                String[] info = records.get(i).split(",");
//                info[0] = hashMD5(info[0]);
//                records.set(i,number+","+info[0]+","+info[1]+","+info[2]);
//                bw.write(records.get(i)+"\n");
//            }
//            bw.close();
//        }
//        catch(IOException ex){
//            System.out.println("Can't create file...");
//        }
//    }


//    public void downloadUrl() throws IOException {
//       BufferedReader in = null;
//       String data = null;
//
//       try{
//           System.out.println("In downloadUrl!");
//           HttpClient httpclient = new DefaultHttpClient();
//
//           HttpGet request = new HttpGet();
//           URI website = new URI("http://162.243.81.9/SMS?key=alex_8138431896&action=insert_user&hash="+number);
//           request.setURI(website);
//           HttpResponse response = httpclient.execute(request);
//           in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//           System.out.println("HTTP output: "+in.readLine());
//       }
//       catch(Exception e){
//              System.out.println("Error in http connection " + e.toString());
//       }
//    }
}
