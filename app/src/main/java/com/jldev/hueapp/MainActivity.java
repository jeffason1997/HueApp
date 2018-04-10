package com.jldev.hueapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jldev.hueapp.DetailedLight.DetailedActivity;
import com.jldev.hueapp.Internet.ConnectionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    static String TAG = "MainActivity";
    private ListView lvLights;
    private ArrayAdapter adapter;
    private ImageView colorEx;
    private ConnectionHandler handler;
    private ArrayList<String> lights = new ArrayList<String>();
    private JSONObject response = null;
    private int hue,sat,bri,counter;
    public static DocumentReference basic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = (ConnectionHandler) getIntent().getSerializableExtra("handler");
        String ip = getIntent().getExtras().getString("IP");
        String user = getIntent().getExtras().getString("user");
        handler.setUrl(ip,user);
        handler.setRef(this);
        handler.GetMethod();
        updateDatabase();


        connectDatabase(ip,user);

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
                updateDatabase();
                ArrayList<String> tempList = new ArrayList<>();
                SparseBooleanArray checked = lvLights.getCheckedItemPositions();
                for (int i = 0; i < checked.size(); i++) {
                    int position = checked.keyAt(i);
                    if(checked.get(position)) {
                        System.out.println(adapter.getItem(position).toString());
                        tempList.add(adapter.getItem(position).toString());
                    }
                }

                Intent intent = new Intent(getApplicationContext(), DetailedActivity.class);
                intent.putStringArrayListExtra("LIGHTS",tempList);
                startActivity(intent);
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
                    jsonObject.put("hue", (int)((float)hueBar.getProgress()*182));
                    jsonObject.put("sat", (int)(((float)satBar.getProgress()*254)/100));
                    jsonObject.put("bri", (int)(((float)briBar.getProgress()*254)/100));


                } catch (JSONException e) {
                    // handle exception
                }

                SparseBooleanArray checked = lvLights.getCheckedItemPositions();
                for (int i = 0; i < checked.size(); i++) {
                    int position = checked.keyAt(i);
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
        updateDatabase();
    }

    public void setDataResponse(JSONObject obj){
        Light current = new Light();
        try {
            JSONObject state = obj.getJSONObject("state");
            current.On = state.getBoolean("on");
            current.Bri = state.getInt("bri");
            current.Hue = state.getInt("hue");
            current.Sat = state.getInt("sat");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String,Object> light = new HashMap<>();
        light.put("ID",lights.get(counter));
        light.put("Hue",current.Hue);
        light.put("Bri",current.Bri);
        light.put("Sat",current.Sat);
        light.put("On",current.On);

        basic.collection("Lights").document(lights.get(counter)).set(light)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });


        counter++;
    }

    private void updateDatabase() {
        counter = 0;
        for(String s : lights){
            handler.GetLightMethod("/"+s);
        }
    }

    public void checkError(VolleyError error){
        if (error.toString().contains("success")) {


        } else {
            System.out.println(error.toString());
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
                lights.add(names.getString(i));
            }

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

    private void connectDatabase(String ip, String user){
        Map<String,Object> config = new HashMap<>();
        config.put("IP",ip);
        config.put("User",user);

        db.collection("Users").document(user).set(config)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"Succes");
                    }
                }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG,"Succes");
            }
        });

        MainActivity.basic = db.collection("Users").document(user);
    }


}
