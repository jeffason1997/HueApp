package com.jldev.hueapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements HueLampListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.getButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg){
                fetchHueLamps();
            }
        });
    }

    public void fetchHueLamps(){
        HueLampListenerTask task = new HueLampListenerTask(this);
        String[] url = new String[]{
                "http://145.48.205.33/api/iYrmsQq1wu5FxF9CPqpJCnm1GpPVylKBWDUsNDhB"
        };
        task.execute(url);
    }

    public void fetchHueLamps(String s){
       // HueLampListenerTask task = new HueLampListenerTask(this);
        //TODO: make a new fucking listener class for individual lamps
        String[] url = new String[]{
                "http://145.48.205.33/api/iYrmsQq1wu5FxF9CPqpJCnm1GpPVylKBWDUsNDhB"
        };
        task.execute(url);
    }

    @Override
    public void onLampsAvailable(String s) {
        System.out.println("Main line 24"+s);
    }
}
