package com.hostapp.parminder.hostapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RSVP extends ActionBarActivity {
    String TAG = "checkingP";
    TextView user;
    TextView total;
    mainHappening2 adapterObj;
    ListView l;
    ArrayList listName = new ArrayList();
    ArrayList listRsvp = new ArrayList();
    String tag_json_obj = "json_obj_req";
    String url;
    ProgressDialog progress;
    JsonObjectRequest jsonObjReq;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsvp);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        user = (TextView) findViewById(R.id.textView4);
        total = (TextView) findViewById(R.id.textView3);

        url = "http://10.10.20.169:82/newTrend/hostApp/getRSVP.php";
        progress = ProgressDialog.show(this, "Please Wait",
                "Fetching Data", true);
        jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataSet = (JSONArray) response.get("data");
                            if (dataSet != null) {
                                JSONObject json = dataSet.getJSONObject(0);
                                user.setText(json.get("user").toString());
                                total.setText(json.get("total").toString());
                                //now list will be fetched
                                url = "http://10.10.20.169:82/newTrend/hostApp/getUserList.php";

                                jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                                        url, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                l = (ListView) findViewById(R.id.listView);
                                                try {

                                                    JSONArray dataSet = (JSONArray) response.get("data");

                                                    if (dataSet != null) {
                                                        int len = dataSet.length();

                                                        for (int i = 0; i < len; i++) {
                                                            JSONObject json = dataSet.getJSONObject(i);
                                                            listName.add(json.get("name"));
                                                            listRsvp.add(json.get("count"));

                                                        }
                                                    }
                                                    adapterObj = new mainHappening2(getApplicationContext(), listName, listRsvp);
                                                    progress.hide();
                                                    l.setAdapter(adapterObj);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rsv, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences.Editor editor = getSharedPreferences("userD", MODE_PRIVATE).edit();
            editor.putString("loginDetails", "no");
            editor.commit();
            intent = new Intent(RSVP.this, Login.class);
            startActivity(intent);

        } else if (id == R.id.menu) {
            intent = new Intent(RSVP.this, MenuActivity.class);
            startActivity(intent);
        }else if (id == R.id.invite) {
            intent = new Intent(RSVP.this, Invite.class);
            startActivity(intent);
        } else if (id == R.id.action_aboutus) {
            intent = new Intent(RSVP.this, About.class);
            startActivity(intent);
        } else if (id == R.id.action_settings) {
        }


        return super.onOptionsItemSelected(item);
    }
}


class MyViewHolder2 {
    TextView nameText;
    TextView rsvpText;

    MyViewHolder2(View row) {
        nameText = (TextView) row.findViewById(R.id.name);
        rsvpText = (TextView) row.findViewById(R.id.count);
    }
}

class mainHappening2 extends ArrayAdapter implements Parcelable {
    protected static final String TAG = "ERROR";
    Context context;
    String[] nameList;
    String[] rsvpList;
    TextView loading;

    public mainHappening2(Context c, ArrayList names, ArrayList rsvp) {
        super(c, R.layout.single_row2, R.id.textView, names);
        this.context = c;
        this.nameList = (String[]) names.toArray(new String[names.size()]);
        this.rsvpList = (String[]) rsvp.toArray(new String[rsvp.size()]);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final MyViewHolder2 holder;
        if (row == null) {
            //only run if row is created for first time
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_row2, parent, false);
            holder = new MyViewHolder2(row);
            row.setTag(holder);
        } else {
            holder = (MyViewHolder2) row.getTag();
        }
//        TextView nameText = (TextView) row.findViewById(R.id.nameText);


        holder.nameText.setText(nameList[position]);
        holder.rsvpText.setText(rsvpList[position] + " more");

        //image setting finished
        return row;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}