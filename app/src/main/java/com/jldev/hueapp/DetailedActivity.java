package com.jldev.hueapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Jeffrey on 8-12-2017.
 */

public class DetailedActivity extends AppCompatActivity {

    ArrayList<Light> lights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView cardsLV = (ListView) findViewById(R.id.lv_lights);
        ArrayAdapter mAdapter = new DetailedAdapter((this.getApplicationContext()),lights);
    }
}