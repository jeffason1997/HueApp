package com.jldev.hueapp.DetailedLight;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jldev.hueapp.Internet.ConnectionHandler;
import com.jldev.hueapp.Light;
import com.jldev.hueapp.MainActivity;
import com.jldev.hueapp.R;

import java.util.ArrayList;

/**
 * Created by Jeffrey on 8-12-2017.
 */

public class DetailedActivity extends AppCompatActivity {

    ArrayList<Light> lights = new ArrayList<>();
    ArrayList<String> aLights;
    ArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        aLights = getIntent().getStringArrayListExtra("LIGHTS");

        buildLights(aLights);

        ListView lightsLV = (ListView) findViewById(R.id.lv_lights);
        mAdapter = new DetailedAdapter(getApplicationContext(),lights);
        lightsLV.setAdapter(mAdapter);

    }

    public void buildLights(ArrayList<String> aLights){
        for(String s : aLights){
            MainActivity.basic.collection("Lights").document(s).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        double hue,sat,bri;
                        DocumentSnapshot doc = task.getResult();
                        Light temp = new Light();
                        temp.id = doc.getId();
                        hue = doc.getDouble("Hue");
                        temp.Hue = (int) hue;
                        bri = doc.getDouble("Bri");
                        temp.Bri = (int) bri ;
                        sat = doc.getDouble("Sat");
                        temp.Sat = (int) sat ;
                        temp.On = doc.getBoolean("On");

                        lights.add(temp);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

    }
}