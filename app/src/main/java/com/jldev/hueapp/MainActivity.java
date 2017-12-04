package com.jldev.hueapp;

import android.app.Application;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
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


    ListView lvLights;
    ArrayAdapter adapter;
    boolean done = false;
    ConnectionHandler handler;
    ArrayList<String> lights;
    JSONObject response = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lights = new ArrayList<String>();

        handler = (ConnectionHandler) getIntent().getSerializableExtra("handler");
        String ip = getIntent().getExtras().getString("IP");
        String user = getIntent().getExtras().getString("user");
        handler.setUrl(ip,user);


        handler.setRef(this);
        handler.GetMethod();
        lvLights = (ListView) findViewById(R.id.lv_available_lights);


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

                    handler.PutMethod(command,jsonObject);
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

                    handler.PutMethod(command,jsonObject);
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

                    handler.PutMethod(command,jsonObject);
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
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Something went wrong, sorry man");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                            System.exit(0);
                        }
                    });
            alertDialog.show();
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
