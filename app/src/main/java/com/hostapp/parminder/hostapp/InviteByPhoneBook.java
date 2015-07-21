package com.hostapp.parminder.hostapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class InviteByPhoneBook extends ActionBarActivity {
    ListView l;
    ArrayList listName = new ArrayList();
    ArrayList listNumber = new ArrayList();
    String phoneNumber;
    String TAG="CheckingLog";
    ArrayList<String> aa = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_by_phone_book);
        l = (ListView) findViewById(R.id.listView);
        getName(this.getContentResolver());
    }
    public void getName(ContentResolver cr) {
        String oldName = "null";
        // Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
          //  System.out.println(".................." + name);
            if (!(oldName.equals(name))) {
                //means name is not reapeating
                aa.add(name);
                listNumber.add(phoneNumber);
                listName.add(name);
            }

            oldName = name;
        }
        phones.close();// close cursor
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, aa);
        l.setAdapter(adapter);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), listName.get(position) + " pressed", Toast.LENGTH_LONG).show();
                // Tag used to cancel the request
                String tag_json_obj = "json_obj_req";
                String url = "http://10.10.20.169:82/newTrend/hostApp/sendSms.php?number=" + listNumber.get(position) + "&name=" + listName.get(position);

                final ProgressDialog pDialog = new ProgressDialog(InviteByPhoneBook.this);
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
        });
        //display contact numbers in the list
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invite_by_phone_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
