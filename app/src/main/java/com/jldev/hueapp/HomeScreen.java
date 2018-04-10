package com.jldev.hueapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.jldev.hueapp.Internet.ConnectionHandler;

public class HomeScreen extends AppCompatActivity {


    ConnectionHandler handler = new ConnectionHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);



        Button SimJeff = (Button) findViewById(R.id.simButton);
        SimJeff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg){
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("handler",handler);
                i.putExtra("IP","192.168.0.103:81");
                i.putExtra("user","4f4c464515fe6024e7702c8ee9e0c1f"); //refresh every time you use the simulator again
                startActivity(i);
            }
        });

        Button LAButton = (Button) findViewById(R.id.laButton);
        LAButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg){
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("handler",handler);
                i.putExtra("IP","145.48.205.33");
                i.putExtra("user","iYrmsQq1wu5FxF9CPqpJCnm1GpPVylKBWDUsNDhB");
                startActivity(i);
            }
        });

        final EditText ip = (EditText) findViewById(R.id.ipTextField);
        final EditText user = (EditText) findViewById(R.id.userTextField);

        Button newButton = (Button) findViewById(R.id.newButton);
        newButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg){
                Intent i = new Intent(getApplicationContext(), MainActivity.class);

                String sIp = ip.getText().toString();
                String sUser = user.getText().toString();

                i.putExtra("handler",handler);
                i.putExtra("IP",sIp);
                i.putExtra("user",sUser);
                startActivity(i);
            }
        });
    }
}
