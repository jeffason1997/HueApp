package com.jldev.hueapp;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.jldev.hueapp.Internet.ConnectionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{


    ListView lvLights;
    ArrayAdapter adapter;
    ImageView colorEx;
    boolean done = false;
    ConnectionHandler handler;
    ArrayList<String> lights;
    JSONObject response = null;
    int hue,sat,bri;

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

        final TextView satValue = (TextView) findViewById(R.id.satValue);
        final TextView hueValue = (TextView) findViewById(R.id.hueValue);
        final TextView briValue = (TextView) findViewById(R.id.briValue);

        colorEx = (ImageView) findViewById(R.id.colorImage);


        final SeekBar satBar = (SeekBar) findViewById(R.id.satBar);
        satBar.setMax(100);
        satBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 satValue.setText(""+progress);
                sat=progress;
                updateColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        final SeekBar hueBar = (SeekBar) findViewById(R.id.hueBar);
        hueBar.setMax(360);
        hueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hueValue.setText(""+progress);
                hue=progress;
                updateColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        final SeekBar briBar = (SeekBar) findViewById(R.id.briBar);
        briBar.setMax(100);
        briBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                briValue.setText(""+progress);
                bri=progress;
                updateColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



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
                    jsonObject.put("hue", ((float)hueBar.getProgress()*182));
                    jsonObject.put("sat", ((float)satBar.getProgress()/100)*254);
                    jsonObject.put("bri", ((float)briBar.getProgress()/100)*254);


                } catch (JSONException e) {
                    // handle exception
                }

                SparseBooleanArray checked = lvLights.getCheckedItemPositions();
                for (int i = 0; i < checked.size(); i++) {
                    int position = checked.keyAt(i);
                    System.out.println(checked.get(position));
                    if(checked.get(position)) {
                        String light = adapter.getItem(position).toString();
                        String command = "/" + light + "/state";
                        handler.PutMethod(command,jsonObject);
                    }

                }
            }
        });
    }

    public void updateColor(){
        float[] hsvColors = new float[]{hue, sat,bri};

        //colorEx.setBackgroundColor(Color.HSVToColor(hsvColors));
        colorEx.setColorFilter(Color.HSVToColor(hsvColors));
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
