package com.hostapp.parminder.hostapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AddMenuItem extends ActionBarActivity {
    Button submit;
    EditText name;
    String dishName;
    Intent intent;
    String TAG="checkingP";
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        name= (EditText) findViewById(R.id.editText);
        submit= (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dishName=name.getText().toString();
                dishName=dishName.replace(' ','_');
                String tag_json_obj = "json_obj_req";
                String url = "http://10.10.20.169:82/newTrend/hostApp/addMenuItem.php?name="+dishName;
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                        url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray dataSet = (JSONArray) response.get("data");
                                    if (dataSet != null) {
                                        int len = dataSet.length();
                                        for (int i = 0; i < len; i++) {
                                            JSONObject json = dataSet.getJSONObject(i);
                                            if(json.get("result").equals("yes")) {
                                                Toast.makeText(getApplicationContext(), "Item Added", Toast.LENGTH_LONG).show();
                                                intent =new Intent(AddMenuItem.this,MenuActivity.class);
                                                startActivity(intent);
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(),"Something went wrong Try again",Toast.LENGTH_LONG).show();
                                                intent=new Intent(AddMenuItem.this,AddMenuItem.class);
                                                startActivity(intent);
                                            }




                                        }
                                    }

//                          sl
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error: " + error);
                    }
                });
                AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {

            Intent home = new Intent(getApplicationContext(),MenuActivity.class);
            startActivity(home);

            /*Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            // NavUtils.navigateUpFromSameTask(this);
            // finish();*/
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
