package com.jldev.hueapp;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jeffrey on 4-12-2017.
 */

public class ConnectionHandler implements Serializable{

    String url =null;
    MainActivity main;
    RequestQueue queue;

    public void setUrl(String ip, String user) {
        url = "http://"+ip+"/api/"+user +"/lights";
        System.out.println(url);
    }

    public void setRef(MainActivity main) {
        this.main = main;
        queue = Volley.newRequestQueue(this.main);
    }

    protected void GetMethod() {

        String usingString = url;

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, usingString, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                main.setResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                main.checkError(error);
            }
        });

        queue.add(jsonRequest);
    }

    protected void PutMethod(String toDo, JSONObject request) {

        String usingString = url + toDo;

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, usingString, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                main.setResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                main.checkError(error);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        queue.add(jsonRequest);
    }
}
