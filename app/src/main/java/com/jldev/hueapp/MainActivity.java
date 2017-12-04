package com.jldev.hueapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    RequestQueue queue;
    ListView lvLights;
    ArrayAdapter adapter;
    boolean done = false;
    ConnectionHandler handler = new ConnectionHandler();
    ArrayList<String> lights;
    JSONObject response = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lights = new ArrayList<String>();

        queue = Volley.newRequestQueue(this);
        handler.setRef(this);
        lvLights = (ListView) findViewById(R.id.lv_available_lights);
        queue.add(handler.GetMethod("/lights"));


        Button getButton = (Button) findViewById(R.id.getButton);
        getButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg){

            }
        });

        Button onButton = (Button) findViewById(R.id.onButton);
        onButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg){
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("on", true);
                } catch (JSONException e) {
                    // handle exception
                }

                SparseBooleanArray checked = lvLights.getCheckedItemPositions();
                ArrayList<String> selectedItems = new ArrayList<String>();
                for (int i = 0; i < checked.size(); i++) {
                    int position = checked.keyAt(i);
                    String light = adapter.getItem(position).toString();
                    String command = "/"+light+"/state";

                    queue.add(handler.PutMethod(command,jsonObject));
                }
            }
        });

        Button offButton = (Button) findViewById(R.id.offButton);
        offButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg){
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("on", false);
                } catch (JSONException e) {
                    // handle exception
                }

                SparseBooleanArray checked = lvLights.getCheckedItemPositions();
                for (int i = 0; i < checked.size(); i++) {
                    int position = checked.keyAt(i);
                    String light = adapter.getItem(position).toString();
                    String command = "/"+light+"/state";

                    queue.add(handler.PutMethod(command,jsonObject));
                }
            }
        });


        Button putButton = (Button) findViewById(R.id.putButton);
        putButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg){
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("hue", 12234);
                } catch (JSONException e) {
                    // handle exception
                }

                SparseBooleanArray checked = lvLights.getCheckedItemPositions();
                for (int i = 0; i < checked.size(); i++) {
                    int position = checked.keyAt(i);
                    String light = adapter.getItem(position).toString();
                    String command = "/"+light+"/state";

                    queue.add(handler.PutMethod(command,jsonObject));
                }
            }
        });
    }

    public void setResponse(JSONObject obj){
        response = obj;
        setListview();

    }

    public void checkError(VolleyError error){
        if (error.toString().contains("success")){

        } else {
            System.out.println("fuck");
        }
    }

    public void setListview(){
        try {
            JSONArray names = response.names();

            for (int i = 0; i < names.length(); i++) {
                System.out.println(names.getString(i));
                lights.add(names.getString(i));
            }
            done = true;

        }catch(JSONException e){
            e.printStackTrace();
        }

        if (lvLights.getAdapter() == null) {
            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, lights);
            lvLights.setAdapter(adapter);
            lvLights.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            lvLights.setItemsCanFocus(false);
        }
    }


}
