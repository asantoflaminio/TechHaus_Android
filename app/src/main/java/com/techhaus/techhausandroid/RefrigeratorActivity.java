package com.techhaus.techhausandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RefrigeratorActivity extends AppCompatActivity {

    private String[] grpname = {"Default", "Party", "Vacation"};

    private RequestQueue mQueue;
    int check = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        TextView txtInfo = (TextView) findViewById(R.id.RefrigeratorName);
        if(getIntent() != null){
            String info = getIntent().getStringExtra("devName");
            txtInfo.setText(info);
        }
        mQueue = Volley.newRequestQueue(this);
        getTemps(getIntent().getStringExtra("devId"));
        getMode(getIntent().getStringExtra("devId"));
        final String deviceId = getIntent().getStringExtra("devId");

        final Button tempDown = (Button) findViewById(R.id.TempDown);
        tempDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                TextView tmp = (TextView) findViewById(R.id.TempNumb);
                int temp = Integer.valueOf(tmp.getText().toString().replace("°", "").replace("C", ""));
                if(temp == 2){
                    Toast.makeText(RefrigeratorActivity.this, getString(R.string.TempAtMin), Toast.LENGTH_SHORT).show();

                }else{
                    temp = temp-1;
                    tmp.setText(temp + "°C");
                    updateTemp(temp, deviceId);
                }
            }
        });

        final Button tempUp = (Button) findViewById(R.id.TempUp);
        tempUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                TextView tmp = (TextView) findViewById(R.id.TempNumb);
                int temp = Integer.valueOf(tmp.getText().toString().replace("°", "").replace("C", ""));
                if(temp == 8){
                    Toast.makeText(RefrigeratorActivity.this, getString(R.string.TempAtMax), Toast.LENGTH_SHORT).show();

                }else{
                    temp = temp+1;
                    tmp.setText(temp + "°C");
                    updateTemp(temp, deviceId);
                }
            }
        });

        final Button ftempDown = (Button) findViewById(R.id.FTempDown);
        ftempDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                TextView tmp = (TextView) findViewById(R.id.FreezerTempNumb);
                int temp = Integer.valueOf(tmp.getText().toString().replace("°", "").replace("C", ""));
                if(temp == -20){
                    Toast.makeText(RefrigeratorActivity.this, getString(R.string.FTempAtMin), Toast.LENGTH_SHORT).show();

                }else{
                    temp = temp-1;
                    tmp.setText(temp + "°C");
                   updateFreezerTemp(temp, deviceId);
                }
            }
        });

        final Button ftempUp = (Button) findViewById(R.id.FTempUp);
        ftempUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                TextView tmp = (TextView) findViewById(R.id.FreezerTempNumb);
                int temp = Integer.valueOf(tmp.getText().toString().replace("°", "").replace("C", ""));
                if(temp == -8){
                    Toast.makeText(RefrigeratorActivity.this, getString(R.string.FTempAtMax), Toast.LENGTH_SHORT).show();

                }else{
                    temp = temp+1;
                    tmp.setText(temp + "°C");
                   updateFreezerTemp(temp, deviceId);
                }
            }
        });


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar7);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        String from = getIntent().getStringExtra("from");
        Intent intent = new Intent(RefrigeratorActivity.this, ActivityOne.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(from.equals(getString(R.string.Favorites))){
            intent.putExtra("info", getString(R.string.Favorites));
        }else{
            intent.putExtra("info", getString(R.string.Refrigerators));
        }

        startActivity(intent);

        return true;
    }

    private void updateTemp(int temp, String deviceId) {

        String url = API.getDevices() + deviceId + "/setTemperature";

        String t = String.valueOf(temp);
        JSONArray jarray = new JSONArray();
        jarray.put(t);

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.PUT, url, jarray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag", "Error de response");
                error.printStackTrace();

            }
        });
        mQueue.add(request);
    }

    private void updateFreezerTemp(int temp, String deviceId) {

        String url = API.getDevices() + deviceId + "/setFreezerTemperature";

        String t = String.valueOf(temp);
        JSONArray jarray = new JSONArray();
        jarray.put(t);
        // final JSONObject jsonObject = new JSONObject();
        // jsonObject.put("", jarray);
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.PUT, url, jarray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag", "Error de response");
                error.printStackTrace();

            }
        });
        mQueue.add(request);
        //getTemps(id);
    }


    private void getTemps(String id) {
        String url = API.getDevices() + id + "/getState";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // ok I guess

                JSONArray jsonArray = null;
                try {
                    JSONObject result = response.getJSONObject("result");
                    String temp = result.getString("temperature");
                    String fTemp = result.getString("freezerTemperature");
                    TextView temp1 = (TextView) findViewById(R.id.TempNumb);
                    TextView temp2 = (TextView) findViewById(R.id.FreezerTempNumb);
                    temp1.setText(temp + "°C");
                    temp2.setText(fTemp + "°C");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag", "Error de response");
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void getMode(final String id1) {
        String url = API.getDevices() + id1 + "/getState";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // ok I guess

                JSONArray jsonArray = null;
                try {
                    JSONObject result = response.getJSONObject("result");
                    String mode = result.getString("mode").replace("[", "").replace("]", "").replace("\"", "");

                    TextView tv = (TextView) findViewById(R.id.fridgemode);
                    mode = mode.replace(mode.charAt(0), mode.toUpperCase().charAt(0));
                    tv.setText(mode);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag", "Error de response");
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void changeMode(final String id, String mode) throws JSONException {

        String url = API.getDevices() + id + "/setMode";
        String modeParam = "[\"" + mode.toLowerCase() + "\"]";
        JSONArray jarray = new JSONArray();
        jarray.put(mode.toLowerCase());

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.PUT, url, jarray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                getTemps(id);
            }




        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag", "Error de response");
                error.printStackTrace();
                getTemps(id);
            }
        });
        mQueue.add(request);

    }

    public void dialog(View v){

        final ArrayList<String> selection = new ArrayList<>();

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle(R.string.SelectMode);
        alt_bld.setNegativeButton(R.string.Cancel, null);
        alt_bld.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(!selection.isEmpty()) {
                    try {
                        changeMode(getIntent().getStringExtra("devId"),selection.get(0));
                        TextView new_mode = (TextView)findViewById(R.id.fridgemode);
                        new_mode.setText(selection.get(0));
                        Toast.makeText(RefrigeratorActivity.this, selection.get(0), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }});
        alt_bld.setSingleChoiceItems(grpname, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                selection.clear();
                selection.add(grpname[item]);
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()){
                case R.id.nav_devices:
                    Intent intent = new Intent(RefrigeratorActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(RefrigeratorActivity.this, NotificationsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(RefrigeratorActivity.this, RoutinesActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent3.putExtra("info", getString(R.string.Routines));
                    startActivity(intent3);
                    break;
            }


            return true;
        }
    };
}
