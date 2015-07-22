package com.hostapp.parminder.hostapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Login extends ActionBarActivity {
    EditText user;
    EditText pass;
    Button btn;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn= (Button) findViewById(R.id.login);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("checkingLog","login clicked");
                user = (EditText) findViewById(R.id.user);
                pass = (EditText) findViewById(R.id.pass);
                final String username = user.getText().toString();
                final String password = pass.getText().toString();
                Log.d("checkingLog","value "+username+" "+password);


                //sending deatils to server
                String tag_json_obj = "json_obj_req";
//                String url = "http://192.168.2.19:82/smartAdv/login.php?user="+username+"&pass="+password;
                String url = "http://10.10.20.169:82/newTrend/hostApp/login.php?username="+username+"&password="+password;
//                String url = "http://codethefuture.esy.es/smartAdv/login.php?user="+username+"&pass="+password;

                Log.d("checkingLog","url"+url);

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                        url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONArray dataSet = null;
                                try {
                                    dataSet = (JSONArray) response.get("data");
                                    Log.d("checkingLog","DataSet"+dataSet);
                                    JSONObject json = dataSet.getJSONObject(0);
                                    String pass = json.get("check").toString();
                                    if (pass.equals("yes")) {
                                        Toast.makeText(getApplicationContext(), "Login Done!!", Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor editor = getSharedPreferences("userD", MODE_PRIVATE).edit();
                                        editor.putString("number", username);
                                        editor.putString("loginDetails", "yes");
                                        editor.commit();
                                        intent = new Intent(Login.this, RSVP.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Incorrext login details !!", Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", "Error: " + error);
                        // hide the progress dialog

                    }
                });

// Adding request to request queue
                AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
            }
        });
    }


}
