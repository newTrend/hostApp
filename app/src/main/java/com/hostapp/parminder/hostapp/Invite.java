package com.hostapp.parminder.hostapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Invite extends ActionBarActivity {
    EditText number;
    EditText name;
    Button phoneBook;
    Button submit;
    String TAG="LogCheck";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        number = (EditText) findViewById(R.id.phoneNumber);
        name = (EditText) findViewById(R.id.friendName);
        submit = (Button) findViewById(R.id.invite);
        phoneBook= (Button) findViewById(R.id.phone);
        phoneBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Invite.this,InviteByPhoneBook.class);
                startActivity(intent);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNumber = number.getText().toString();
                String userName = name.getText().toString();
                if ((userNumber.isEmpty())||(userName.isEmpty())) {
                    Toast.makeText(getApplicationContext(),"Fill the Fields",Toast.LENGTH_LONG).show();
                }
                else {

                // Tag used to cancel the request
                String tag_json_obj = "json_obj_req";

                String url = "http://10.10.20.169:82/newTrend/hostApp/sendSms.php?number=" + userNumber + "&name=" + userName;

                final ProgressDialog pDialog = new ProgressDialog(Invite.this);
                pDialog.setMessage("Loading...");
                pDialog.show();

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                        url, null,
                        new Response.Listener<JSONObject>() {
                            JSONArray dataSet = null;

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    dataSet = (JSONArray) response.get("data");
                                    Log.d("checkingLog", "DataSet" + dataSet);
                                    JSONObject json = dataSet.getJSONObject(0);
                                    String pass = json.get("message").toString();
                                    pDialog.hide();
                                    Log.d("checkingP", pass);
                                    Toast.makeText(getApplicationContext(), pass + "", Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error: " + error + " ");
                        // hide the progress dialog
                        pDialog.hide();
                    }
                });

// Adding request to request queue
                AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
            }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {

            Intent home = new Intent(getApplicationContext(), RSVP.class);
            startActivity(home);

            /*Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            // NavUtils.navigateUpFromSameTask(this);
            // finish();*/
            return true;
        }
    }


}
