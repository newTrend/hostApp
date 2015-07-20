package com.hostapp.parminder.hostapp;

import android.content.Context;
import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MenuActivity extends ActionBarActivity {

    mainHappening adapterObj;
    ListView l;
    String TAG="check";
    Toolbar toolbar;
    ArrayList listName=new ArrayList();
    Button addbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        toolbar= (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState==null) {
            String tag_json_obj = "json_obj_req";
            String url = "http://10.10.20.169:82/newTrend/hostApp/getMenu.php";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
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
                                        //now user can perform the action
                                        addbutton= (Button) findViewById(R.id.addButton);
                                        addbutton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent=new Intent(MenuActivity.this,AddMenuItem.class);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                }
                                adapterObj = new mainHappening(getApplicationContext(), listName);
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
        else{
            l= (ListView) findViewById(R.id.listView);
            adapterObj= (mainHappening) savedInstanceState.getParcelable("myData");
            l.setAdapter(adapterObj);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {

            Intent home = new Intent(getApplicationContext(),RSVP.class);
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


class MyViewHolder{
    TextView nameText;
    MyViewHolder(View row){
        nameText = (TextView) row.findViewById(R.id.nameText);
    }
}
class mainHappening extends ArrayAdapter implements Parcelable {
    protected static final String TAG = "ERROR";
    Context context;
    String[] nameList;
    TextView loading;
    public mainHappening(Context c, ArrayList names) {
        super(c,R.layout.single_row,R.id.textView,names);
        this.context=c;
        this.nameList= (String[]) names.toArray(new String[names.size()]);
    }
    public View getView(int position,View convertView,ViewGroup parent){
        View row=convertView;
        final MyViewHolder holder;
        if(row==null) {
            //only run if row is created for first time
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_row, parent, false);
            holder =new MyViewHolder(row);
            row.setTag(holder);
        }
        else{
            holder= (MyViewHolder) row.getTag();
        }
//        TextView nameText = (TextView) row.findViewById(R.id.nameText);

        if(!(nameList[position].isEmpty()))
            holder.nameText.setText(nameList[position]);
        else
            holder.nameText.setText("Nothing to show");


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