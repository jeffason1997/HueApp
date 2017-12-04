package com.jldev.hueapp;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jeffrey on 4-12-2017.
 */

public class ConnectionHandler {

    String url = "http://192.168.0.103:81/api/6b6f0a4393acc74c5f00613e8c86fbf/lights";
    MainActivity main;

    public void setUrl(String Url) {
        url = Url;
    }

    public void setRef(MainActivity main) {
        this.main = main;
    }


    protected JsonObjectRequest GetMethod(String toDo) {

        String usingString = url + toDo;

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

        return jsonRequest;
    }


    protected JsonObjectRequest PutMethod(String toDo, JSONObject request) {

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

        return jsonRequest;
    }
}
