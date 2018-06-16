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

public class ACActivity extends AppCompatActivity {

    Spinner mode_spinner, fan_spinner, vertical_swing_spinner, horizontal_swing_spinner;
    private RequestQueue mQueue;
    int check = 0;
    int check2 = 0;
    int check3 = 0;
    int check4 = 0;
    int check5 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        TextView txtInfo = (TextView) findViewById(R.id.ACName);
        if(getIntent() != null){
            String info = getIntent().getStringExtra("devName");
            txtInfo.setText(info);
        }
        mQueue = Volley.newRequestQueue(this);
        getState(getIntent().getStringExtra("devId"));
        //hacer getstate
        // el cambio de todos los modos q igual va adentro de getstate

        final String deviceId = getIntent().getStringExtra("devId");

        Switch s = (Switch) findViewById(R.id.switch2);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked){
                    if(++check5 > 1) {
                        Toast.makeText(ACActivity.this, getString(R.string.TurnedOn), Toast.LENGTH_SHORT).show();
                        changeStatus(deviceId, "/turnOn");
                    }


                }else{
                    Toast.makeText(ACActivity.this, getString(R.string.TurnedOff), Toast.LENGTH_SHORT).show();
                    changeStatus(deviceId, "/turnOff");
                }
            }
        });

        final Button tempDown = (Button) findViewById(R.id.TempDown);
        tempDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                TextView tmp = (TextView) findViewById(R.id.TempNumb);
                int temp = Integer.valueOf(tmp.getText().toString().replace("°", "").replace("C", ""));
                if(temp == 18){
                    Toast.makeText(ACActivity.this, getString(R.string.TempAtMin), Toast.LENGTH_SHORT).show();

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
                if(temp ==  38){
                    Toast.makeText(ACActivity.this, getString(R.string.TempAtMax), Toast.LENGTH_SHORT).show();

                }else{
                    temp = temp+1;
                    tmp.setText(temp + "°C");
                    updateTemp(temp, deviceId);
                }
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar4);
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
        Intent intent = new Intent(ACActivity.this, ActivityOne.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(from.equals(getString(R.string.Favorites))){
            intent.putExtra("info", getString(R.string.Favorites));
        }else{
            intent.putExtra("info", getString(R.string.ACs));
        }

        startActivity(intent);

        return true;
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
                    String mode = result.getString("mode").replace("[", "").replace("]", "").replace("\"", "");
                    String vswing = result.getString("verticalSwing").replace("[", "").replace("]", "").replace("\"", "");
                    String hswing = result.getString("horizontalSwing").replace("[", "").replace("]", "").replace("\"", "");
                    String fspeed = result.getString("fanSpeed").replace("[", "").replace("]", "").replace("\"", "");

                    TextView temp1 = (TextView) findViewById(R.id.TempNumb);
                    temp1.setText(temp + "°C");

                    Switch s = (Switch) findViewById(R.id.switch2);
                    if(status.equals("on")){
                        s.setChecked(true);
                    }else{
                        check5++;
                        s.setChecked(false);
                    }


                    //aca vienen los spinners!
                    //custom spinner
                    mode_spinner = findViewById(R.id.spinner);
                    final List<SpinnerData> customList1 = new ArrayList<>();
                    if(mode.equals("heat")){
                        customList1.add(new SpinnerData(R.drawable.heat, "Heat"));
                        customList1.add(new SpinnerData(R.drawable.cool, "Cool"));
                        customList1.add(new SpinnerData(R.drawable.fan, "Fan"));
                    }else if(mode.equals("cool")){
                        customList1.add(new SpinnerData(R.drawable.cool, "Cool"));
                        customList1.add(new SpinnerData(R.drawable.heat, "Heat"));
                        customList1.add(new SpinnerData(R.drawable.fan, "Fan"));
                    }else{
                        customList1.add(new SpinnerData(R.drawable.fan, "Fan"));
                        customList1.add(new SpinnerData(R.drawable.cool, "Cool"));
                        customList1.add(new SpinnerData(R.drawable.heat, "Heat"));
                    }


                    CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(ACActivity.this, R.layout.spinner_layout, customList1);
                    mode_spinner.setAdapter(customSpinnerAdapter);

                    mode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                            if(++check > 1) {
                                Toast.makeText(ACActivity.this, customList1.get(i).getIconName(), Toast.LENGTH_SHORT).show();
                                changeMode(devId, customList1.get(i).getIconName(), "/setMode");
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    fan_spinner = findViewById(R.id.spinner2);
                    final List<String> fan_spinner_list = new ArrayList<>();
                    if(fspeed.equals("auto")){
                        fan_spinner_list.add("Auto");
                        fan_spinner_list.add("25");
                        fan_spinner_list.add("50");
                        fan_spinner_list.add("75");
                        fan_spinner_list.add("100");
                    }else if(fspeed.equals("25")){
                        fan_spinner_list.add("25");
                        fan_spinner_list.add("50");
                        fan_spinner_list.add("75");
                        fan_spinner_list.add("100");
                        fan_spinner_list.add("Auto");
                    }else if(fspeed.equals("50")){
                        fan_spinner_list.add("50");
                        fan_spinner_list.add("25");
                        fan_spinner_list.add("75");
                        fan_spinner_list.add("100");
                        fan_spinner_list.add("Auto");
                    }else if(fspeed.equals("75")){
                        fan_spinner_list.add("75");
                        fan_spinner_list.add("100");
                        fan_spinner_list.add("Auto");
                        fan_spinner_list.add("50");
                        fan_spinner_list.add("25");
                    }else{
                        fan_spinner_list.add("100");
                        fan_spinner_list.add("25");
                        fan_spinner_list.add("50");
                        fan_spinner_list.add("75");
                        fan_spinner_list.add("Auto");
                    }


                    ArrayAdapter<String> fan_spinner_adapter = new ArrayAdapter<>(ACActivity.this, R.layout.support_simple_spinner_dropdown_item, fan_spinner_list);
                    fan_spinner.setAdapter(fan_spinner_adapter);

                    fan_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                            if(++check2 > 1) {
                                Toast.makeText(ACActivity.this, fan_spinner_list.get(i), Toast.LENGTH_SHORT).show();
                                changeMode(devId, fan_spinner_list.get(i), "/setFanSpeed");
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    vertical_swing_spinner = findViewById(R.id.spinner3);
                    final List<String> vertical_swing_spinner_list = new ArrayList<>();
                    if(vswing.equals("auto")){
                        vertical_swing_spinner_list.add("Auto");
                        vertical_swing_spinner_list.add("22");
                        vertical_swing_spinner_list.add("45");
                        vertical_swing_spinner_list.add("67");
                        vertical_swing_spinner_list.add("90");
                    }else if(vswing.equals("22")){
                        vertical_swing_spinner_list.add("22");
                        vertical_swing_spinner_list.add("45");
                        vertical_swing_spinner_list.add("67");
                        vertical_swing_spinner_list.add("90");
                        vertical_swing_spinner_list.add("Auto");
                    }else if(vswing.equals("45")){
                        vertical_swing_spinner_list.add("45");
                        vertical_swing_spinner_list.add("22");
                        vertical_swing_spinner_list.add("67");
                        vertical_swing_spinner_list.add("90");
                        vertical_swing_spinner_list.add("Auto");
                    }else if(vswing.equals("67")){
                        vertical_swing_spinner_list.add("67");
                        vertical_swing_spinner_list.add("90");
                        vertical_swing_spinner_list.add("Auto");
                        vertical_swing_spinner_list.add("22");
                        vertical_swing_spinner_list.add("45");
                    }else{
                        //90
                        vertical_swing_spinner_list.add("90");
                        vertical_swing_spinner_list.add("Auto");
                        vertical_swing_spinner_list.add("22");
                        vertical_swing_spinner_list.add("45");
                        vertical_swing_spinner_list.add("67");
                    }


                    ArrayAdapter<String> vertical_swing_spinner_adapter = new ArrayAdapter<>(ACActivity.this, R.layout.support_simple_spinner_dropdown_item, vertical_swing_spinner_list);
                    vertical_swing_spinner.setAdapter(vertical_swing_spinner_adapter);

                    vertical_swing_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                            if(++check3 > 1) {
                                Toast.makeText(ACActivity.this, vertical_swing_spinner_list.get(i), Toast.LENGTH_SHORT).show();
                                changeMode(devId, vertical_swing_spinner_list.get(i), "/setVerticalSwing");
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    horizontal_swing_spinner = findViewById(R.id.spinner4);
                    final List<String> horizontal_swing_spinner_list = new ArrayList<>();
                    if(hswing.equals("auto")){
                        horizontal_swing_spinner_list.add("Auto");
                        horizontal_swing_spinner_list.add("-90");
                        horizontal_swing_spinner_list.add("-45");
                        horizontal_swing_spinner_list.add("0");
                        horizontal_swing_spinner_list.add("45");
                        horizontal_swing_spinner_list.add("90");
                    }else if(hswing.equals("-90")){
                        horizontal_swing_spinner_list.add("-90");
                        horizontal_swing_spinner_list.add("-45");
                        horizontal_swing_spinner_list.add("0");
                        horizontal_swing_spinner_list.add("45");
                        horizontal_swing_spinner_list.add("90");
                        horizontal_swing_spinner_list.add("Auto");
                    }else if(hswing.equals("-45")){
                        horizontal_swing_spinner_list.add("-45");
                        horizontal_swing_spinner_list.add("-90");
                        horizontal_swing_spinner_list.add("0");
                        horizontal_swing_spinner_list.add("45");
                        horizontal_swing_spinner_list.add("90");
                        horizontal_swing_spinner_list.add("Auto");
                    }else if(hswing.equals("0")){
                        horizontal_swing_spinner_list.add("0");
                        horizontal_swing_spinner_list.add("Auto");
                        horizontal_swing_spinner_list.add("-90");
                        horizontal_swing_spinner_list.add("-45");
                        horizontal_swing_spinner_list.add("45");
                        horizontal_swing_spinner_list.add("90");
                    }else if(hswing.equals("45")){
                        horizontal_swing_spinner_list.add("45");
                        horizontal_swing_spinner_list.add("Auto");
                        horizontal_swing_spinner_list.add("-90");
                        horizontal_swing_spinner_list.add("-45");
                        horizontal_swing_spinner_list.add("0");
                        horizontal_swing_spinner_list.add("90");
                    }else{//90
                        horizontal_swing_spinner_list.add("90");
                        horizontal_swing_spinner_list.add("Auto");
                        horizontal_swing_spinner_list.add("-90");
                        horizontal_swing_spinner_list.add("-45");
                        horizontal_swing_spinner_list.add("0");
                        horizontal_swing_spinner_list.add("45");
                    }


                    ArrayAdapter<String> horizontal_swing_spinner_adapter = new ArrayAdapter<>(ACActivity.this, R.layout.support_simple_spinner_dropdown_item, horizontal_swing_spinner_list);
                    horizontal_swing_spinner.setAdapter(horizontal_swing_spinner_adapter);

                    horizontal_swing_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                            if(++check4 > 1) {
                                Toast.makeText(ACActivity.this, horizontal_swing_spinner_list.get(i), Toast.LENGTH_SHORT).show();
                                changeMode(devId, horizontal_swing_spinner_list.get(i), "/setHorizontalSwing");
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
                    Intent intent = new Intent(ACActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(ACActivity.this, NotificationsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(ACActivity.this, RoutinesActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent3.putExtra("info", getString(R.string.Routines));
                    startActivity(intent3);
                    break;
            }


            return true;
        }
    };
}
