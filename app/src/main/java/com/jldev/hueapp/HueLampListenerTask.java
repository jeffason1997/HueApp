package com.jldev.hueapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

/**
 * Created by jeffrey on 24-11-2017.
 */

public class HueLampListenerTask extends AsyncTask<String, Void, String> {

    private HueLampListener listener;

    HueLampListenerTask(HueLampListener listener){
        this.listener = listener;
    }


    @Override
    protected void onPostExecute(String response) {
        try {
            JSONObject jo = new JSONObject(response);
            JSONObject joo = jo.getJSONObject("lights");
            JSONArray jsonArray = joo.names();
            for(int i = 0;i<jsonArray.length();i++){
                listener.onLampsAvailable(jsonArray.getString(i));
            }
            System.out.println(jsonArray);
        } catch ( JSONException e){
            System.out.println("LTask line 34");
            System.out.println(response);
        }

    }



    @Override
    protected String doInBackground(String... params) {
        BufferedReader reader = null;
        String respone = "";

        try{
            URL url = new URL(params[0]);
            URLConnection connection = url.openConnection();


            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            respone = reader.readLine();
            String line;
            while((line = reader.readLine())!=null){
                respone+=line;
            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(reader!=null){
                try{
                    reader.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        return respone;
    }
}
