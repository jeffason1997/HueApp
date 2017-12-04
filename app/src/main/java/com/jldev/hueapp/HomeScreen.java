package com.jldev.hueapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                i.putExtra("user","6b6f0a4393acc74c5f00613e8c86fbf");
                startActivity(i);
            }
        });

        Button LAButton = (Button) findViewById(R.id.laButton);
        LAButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg){
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("IP","192.168.0.103:81");
                i.putExtra("user","6b6f0a4393acc74c5f00613e8c86fbf");
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

                i.putExtra("IP",sIp);
                i.putExtra("user",sUser);
                startActivity(i);
            }
        });
    }
}
