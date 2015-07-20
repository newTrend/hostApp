package com.hostapp.parminder.hostapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {
    Intent intent;
    private static final int SPLASH_DURATION=2000;
    private Handler myHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences=getSharedPreferences("userD", MODE_PRIVATE);
        final String check=sharedPreferences.getString("loginDetails", "null");
        myHandler=new Handler();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(check.equals("null")) {
                    //means app is installed first time
                    intent = new Intent(MainActivity.this, Login.class);

                }
                else {
                    //means app is not installed first time checking for login or not
                    if (check.equals("yes")) {
                        //already login
                        intent = new Intent(MainActivity.this, RSVP.class);

                    } else {
                        //person need to login
                        intent = new Intent(MainActivity.this, Login.class);
                    }
                }
                 startActivity(intent);
            }
        }, SPLASH_DURATION);


    }




}
