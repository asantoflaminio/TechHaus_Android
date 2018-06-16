package com.techhaus.techhausandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
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
import java.util.List;

public class RefrigeratorActivity extends AppCompatActivity {

    Spinner fridge_mode_spinner;
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
                    Toast.makeText(RefrigeratorActivity.this, "Temperature is at minimum value", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(RefrigeratorActivity.this, "Temperature is at maximum value", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(RefrigeratorActivity.this, "Freezer temperature is at minimum value", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(RefrigeratorActivity.this, "Freezer temperature is at maximum value", Toast.LENGTH_SHORT).show();

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
    public boolean onOptionsItemSelected (MenuItem item) {
        String from = getIntent().getStringExtra("from");
        Intent intent = new Intent(RefrigeratorActivity.this, ActivityOne.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(from.equals("Favorites")){
            intent.putExtra("info", "Favorites");
        }else{
            intent.putExtra("info", "Refrigerators");
        }

        startActivity(intent);

        return true;
    }

    private void updateTemp(int temp, String deviceId) {

        String url = "http://10.0.2.2:8080/api/devices/" + deviceId + "/setTemperature";

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
                    //custom spinner
                    fridge_mode_spinner = (Spinner) findViewById(R.id.spinner_fridge_mode);
                    final List<SpinnerData> customList1 = new ArrayList<>();
                    Log.d("mytag", "mode isssss " +mode);



                    if(mode.equals("vacation")){
                        Log.d("mytag", "it's vacation");
                        customList1.add(new SpinnerData(R.drawable.vacation, "Vacation"));
                        customList1.add(new SpinnerData(R.drawable.party, "Party"));
                        customList1.add(new SpinnerData(R.drawable.fridge_default, "Default"));

                    }else if(mode.equals("default")){
                        customList1.add(new SpinnerData(R.drawable.fridge_default, "Default"));
                        customList1.add(new SpinnerData(R.drawable.vacation, "Vacation"));
                        customList1.add(new SpinnerData(R.drawable.party, "Party"));

                    }else{
                        //party
                        customList1.add(new SpinnerData(R.drawable.party, "Party"));
                        customList1.add(new SpinnerData(R.drawable.fridge_default, "Default"));
                        customList1.add(new SpinnerData(R.drawable.vacation, "Vacation"));

                    }

                    CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(RefrigeratorActivity.this, R.layout.spinner_layout, customList1);
                    fridge_mode_spinner.setAdapter(customSpinnerAdapter);

                    fridge_mode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                            if(++check > 1) {
                                Toast.makeText(RefrigeratorActivity.this, customList1.get(i).getIconName(), Toast.LENGTH_SHORT).show();
                                try {
                                    changeMode(id1, customList1.get(i).getIconName());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                           }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
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
        Log.d("mytag", "changeMode a "+ mode.toLowerCase());
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
