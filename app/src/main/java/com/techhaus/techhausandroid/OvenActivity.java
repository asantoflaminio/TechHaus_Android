package com.techhaus.techhausandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
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

public class OvenActivity extends AppCompatActivity {

    Spinner heat_spinner, grill_spinner, convection_spinner;
    private RequestQueue mQueue;
    int check = 0;
    int check2 = 0;
    int check3 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oven);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        TextView txtInfo = (TextView) findViewById(R.id.OvenName);
        if(getIntent() != null){
            String info = getIntent().getStringExtra("devName");
            txtInfo.setText(info);
        }
        mQueue = Volley.newRequestQueue(this);
        getState(getIntent().getStringExtra("devId"));



        final String deviceId = getIntent().getStringExtra("devId");
        final Button tempDown = (Button) findViewById(R.id.TempDown);
        tempDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                TextView tmp = (TextView) findViewById(R.id.TempNumb);
                int temp = Integer.valueOf(tmp.getText().toString().replace("°", "").replace("C", ""));
                if(temp == 90){
                    Toast.makeText(OvenActivity.this, "Temperature is at minimum value", Toast.LENGTH_SHORT).show();

                }else{
                    temp = temp-10;
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
                if(temp ==  230){
                    Toast.makeText(OvenActivity.this, "Temperature is at maximum value", Toast.LENGTH_SHORT).show();

                }else{
                    temp = temp+10;
                    tmp.setText(temp + "°C");
                    updateTemp(temp, deviceId);
                }
            }
        });

        Switch s = (Switch) findViewById(R.id.switch1);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked){
                    Toast.makeText(OvenActivity.this, "Turned on", Toast.LENGTH_SHORT).show();
                    changeStatus(deviceId, "/turnOn");

                }else{
                    Toast.makeText(OvenActivity.this, "Turned off", Toast.LENGTH_SHORT).show();
                    changeStatus(deviceId, "/turnOff");
                }
            }
        });


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar6);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        Intent intent = new Intent(OvenActivity.this, ActivityOne.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("info", "Ovens");
        startActivity(intent);

        return true;
    }

    private void changeStatus(String deviceId, String s) {
        String url = API.getDevices() + deviceId + s;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // ok I guess

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

    private void getState(final String devId) {
        String url = API.getDevices() + devId + "/getState";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // ok I guess


                try {
                    JSONObject result = response.getJSONObject("result");
                    String temp = result.getString("temperature");
                    String status = result.getString("status");
                    String heat = result.getString("heat").replace("[", "").replace("]", "").replace("\"", "");
                    String grill = result.getString("grill").replace("[", "").replace("]", "").replace("\"", "");
                    String conv = result.getString("convection").replace("[", "").replace("]", "").replace("\"", "");
                    TextView temp1 = (TextView) findViewById(R.id.TempNumb);
                    temp1.setText(temp + "°C");
                    Log.d("mytag", "status es " + status);
                    Switch s = (Switch) findViewById(R.id.switch1);
                    if(status.equals("on")){
                        s.setChecked(true);
                    }else{
                        s.setChecked(false);
                    }
                    Log.d("mytag", "heat es" + heat);
                    heat_spinner = findViewById(R.id.spinner5);
                    final List<String> heat_spinner_list = new ArrayList<>();
                    if(heat.equals("conventional")){
                        heat_spinner_list.add("Conventional");
                        heat_spinner_list.add("Bottom");
                        heat_spinner_list.add("Top");
                    }else if(heat.equals("top")){
                        heat_spinner_list.add("Top");
                        heat_spinner_list.add("Conventional");
                        heat_spinner_list.add("Bottom");

                    }else{
                        //bottom
                        heat_spinner_list.add("Bottom");
                        heat_spinner_list.add("Conventional");
                        heat_spinner_list.add("Top");
                    }

                    ArrayAdapter<String> heat_spinner_adapter = new ArrayAdapter<>(OvenActivity.this, R.layout.support_simple_spinner_dropdown_item, heat_spinner_list);
                    heat_spinner.setAdapter(heat_spinner_adapter);
                    heat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                            if(++check > 1) {
                                Toast.makeText(OvenActivity.this, heat_spinner_list.get(i), Toast.LENGTH_SHORT).show();
                                changeMode(devId, heat_spinner_list.get(i), "/setHeat");
                            }
                           }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    Log.d("mytag", "grill es" + grill);
                    grill_spinner = findViewById(R.id.spinner6);
                    final List<String> grill_spinner_list = new ArrayList<>();
                    if(grill.equals("off")){
                        grill_spinner_list.add("Off");
                        grill_spinner_list.add("Large");
                        grill_spinner_list.add("Eco");

                    }else if(grill.equals("large")){
                        grill_spinner_list.add("Large");
                        grill_spinner_list.add("Off");
                        grill_spinner_list.add("Eco");
                    }else{
                        //eco
                        grill_spinner_list.add("Eco");
                        grill_spinner_list.add("Off");
                        grill_spinner_list.add("Large");

                    }
                    ArrayAdapter<String> grill_spinner_adapter = new ArrayAdapter<>(OvenActivity.this, R.layout.support_simple_spinner_dropdown_item, grill_spinner_list);
                    grill_spinner.setAdapter(grill_spinner_adapter);

                    grill_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                            if(++check2 > 1) {
                                Toast.makeText(OvenActivity.this, grill_spinner_list.get(i), Toast.LENGTH_SHORT).show();
                                changeMode(devId, grill_spinner_list.get(i), "/setGrill");
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    convection_spinner = findViewById(R.id.spinner7);
                    final List<String> convection_spinner_list = new ArrayList<>();
                    if(conv.equals("eco")){
                        convection_spinner_list.add("Eco");
                        convection_spinner_list.add("Off");
                        convection_spinner_list.add("Normal");
                    }else if(conv.equals("normal")){
                        convection_spinner_list.add("Normal");
                        convection_spinner_list.add("Eco");
                        convection_spinner_list.add("Off");
                    }else{
                        //off
                        convection_spinner_list.add("Off");
                        convection_spinner_list.add("Normal");
                        convection_spinner_list.add("Eco");
                    }
                    ArrayAdapter<String> convection_spinner_adapter = new ArrayAdapter<>(OvenActivity.this, R.layout.support_simple_spinner_dropdown_item, convection_spinner_list);
                    convection_spinner.setAdapter(convection_spinner_adapter);

                    convection_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                            if(++check3 > 1) {
                                Toast.makeText(OvenActivity.this, convection_spinner_list.get(i), Toast.LENGTH_SHORT).show();
                                changeMode(devId, convection_spinner_list.get(i), "/setConvection");
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

    private void changeMode(String devId, String option, String action) {
        String url = API.getDevices() + devId + action;
        JSONArray jarray = new JSONArray();
        jarray.put(option.toLowerCase());

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

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()){
                case R.id.nav_devices:
                    Intent intent = new Intent(OvenActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(OvenActivity.this, NotificationsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(OvenActivity.this, RoutinesActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent3.putExtra("info", "Routines");
                    startActivity(intent3);
                    break;
            }


            return true;
        }
    };


}
