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
import android.widget.CompoundButton;
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

public class OvenActivity extends AppCompatActivity {

    private String[] heat_select = {"Conventional", "Bottom", "Top"};
    private String[] grill_select = {"Large", "Eco", "Off"};
    private String[] convection_select = {"Normal", "Eco", "Off"};

    private RequestQueue mQueue;
    int check = 0;
    int check2 = 0;
    int check3 = 0;
    int check4 = 0;

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
                    if(++check4 > 1) {
                        Toast.makeText(OvenActivity.this, getString(R.string.TurnedOn), Toast.LENGTH_SHORT).show();
                        changeStatus(deviceId, "/turnOn");
                    }


                }else{
                    Toast.makeText(OvenActivity.this, getString(R.string.TurnedOff), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(OvenActivity.this, ActivityOne.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(from.equals(getString(R.string.Favorites))){
            intent.putExtra("info", getString(R.string.Favorites));
        }else{
            intent.putExtra("info", getString(R.string.Ovens));
        }

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
                        check4++;
                        s.setChecked(false);
                    }
                    Log.d("mytag", "heat es" + heat);
                    heat = heat.replace(heat.charAt(0), heat.toUpperCase().charAt(0));
                    TextView heat_text = findViewById(R.id.current_heat);
                    heat_text.setText(heat);


                    Log.d("mytag", "grill es" + grill);
                    grill = grill.replace(grill.charAt(0), grill.toUpperCase().charAt(0));
                    TextView grill_text = findViewById(R.id.current_grill);
                    grill_text.setText(grill);

                    conv = conv.replace(conv.charAt(0), conv.toUpperCase().charAt(0));
                    TextView conv_text = findViewById(R.id.current_convection);
                    conv_text.setText(conv);

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

    public void dialog_heat(View v){

        final ArrayList<String> selection = new ArrayList<>();

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle(R.string.SelectHeatMode);
        alt_bld.setNegativeButton(R.string.Cancel, null);
        alt_bld.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(!selection.isEmpty()) {
                    changeMode(getIntent().getStringExtra("devId"), selection.get(0), "/setHeat");
                    TextView new_mode = (TextView)findViewById(R.id.current_heat);
                    new_mode.setText(selection.get(0));
                    Toast.makeText(OvenActivity.this, selection.get(0), Toast.LENGTH_SHORT).show();
                }
            }});
        alt_bld.setSingleChoiceItems(heat_select, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                selection.clear();
                selection.add(heat_select[item]);
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    public void dialog_grill(View v){

        final ArrayList<String> selection = new ArrayList<>();

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle(R.string.SelectGrillMode);
        alt_bld.setNegativeButton(R.string.Cancel, null);
        alt_bld.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(!selection.isEmpty()) {
                    changeMode(getIntent().getStringExtra("devId"), selection.get(0), "/setGrill");
                    TextView new_mode = (TextView)findViewById(R.id.current_grill);
                    new_mode.setText(selection.get(0));
                    Toast.makeText(OvenActivity.this, selection.get(0), Toast.LENGTH_SHORT).show();
                }
            }});
        alt_bld.setSingleChoiceItems(grill_select, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                selection.clear();
                selection.add(grill_select[item]);
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    public void dialog_convection(View v){

        final ArrayList<String> selection = new ArrayList<>();

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle(R.string.SelectConvectionMode);
        alt_bld.setNegativeButton(R.string.Cancel, null);
        alt_bld.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(!selection.isEmpty()) {
                    changeMode(getIntent().getStringExtra("devId"), selection.get(0), "/setConvection");
                    TextView new_mode = (TextView)findViewById(R.id.current_convection);
                    new_mode.setText(selection.get(0));
                    Toast.makeText(OvenActivity.this, selection.get(0), Toast.LENGTH_SHORT).show();
                }
            }});
        alt_bld.setSingleChoiceItems(convection_select, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                selection.clear();
                selection.add(convection_select[item]);
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
                    Intent intent = new Intent(OvenActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(OvenActivity.this, NotificationsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(OvenActivity.this, RoutinesActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent3.putExtra("info", getString(R.string.Routines));
                    startActivity(intent3);
                    break;
            }


            return true;
        }
    };


}
