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
import android.view.MenuItem;
import android.view.View;
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

public class ACActivity extends AppCompatActivity {

    private String[] ac_mode_select = {"Cool", "Heat", "Fan"};
    private String[] hor_swing_select = {"Auto", "-90", "-45", "0", "45", "90"};
    private String[] ver_swing_select = {"Auto", "22", "45", "67", "90"};
    private String[] fan_select = {"Auto", "25", "50", "75", "100"};

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

        TextView txtTitleInfo = (TextView) findViewById(R.id.ACName);
        if(getIntent() != null){
            String info = getIntent().getStringExtra("devName");
            txtTitleInfo.setText(info);
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

                    mode = mode.replace(mode.charAt(0), mode.toUpperCase().charAt(0));
                    TextView mode_text = findViewById(R.id.current_ac_mode);
                    mode_text.setText(mode);

                    fspeed = fspeed.replace(fspeed.charAt(0), fspeed.toUpperCase().charAt(0));
                    TextView fspeed_text = findViewById(R.id.current_fan);
                    fspeed_text.setText(fspeed);

                    vswing = vswing.replace(vswing.charAt(0), vswing.toUpperCase().charAt(0));
                    TextView vswing_text = findViewById(R.id.current_ver_swing);
                    vswing_text.setText(vswing);

                    hswing = hswing.replace(hswing.charAt(0), hswing.toUpperCase().charAt(0));
                    TextView hswing_text = findViewById(R.id.current_hor_swing);
                    hswing_text.setText(hswing);

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

    public void dialog_ac_mode(View v){

        final ArrayList<String> selection = new ArrayList<>();

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle(R.string.SelectMode);
        alt_bld.setNegativeButton(R.string.Cancel, null);
        alt_bld.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(!selection.isEmpty()) {
                    changeMode(getIntent().getStringExtra("devId"), selection.get(0), "/setMode");
                    TextView new_mode = (TextView)findViewById(R.id.current_ac_mode);
                    new_mode.setText(selection.get(0));
                    Toast.makeText(ACActivity.this, selection.get(0), Toast.LENGTH_SHORT).show();
                }
            }});
        alt_bld.setSingleChoiceItems(ac_mode_select, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                selection.clear();
                selection.add(ac_mode_select[item]);
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    public void dialog_hor_swing(View v){

        final ArrayList<String> selection = new ArrayList<>();

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle(R.string.SelectHorizontalSwing);
        alt_bld.setNegativeButton(R.string.Cancel, null);
        alt_bld.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(!selection.isEmpty()) {
                    changeMode(getIntent().getStringExtra("devId"), selection.get(0), "/setHorizontalSwing");
                    TextView new_mode = (TextView)findViewById(R.id.current_hor_swing);
                    new_mode.setText(selection.get(0));
                    Toast.makeText(ACActivity.this, selection.get(0), Toast.LENGTH_SHORT).show();
                }
            }});
        alt_bld.setSingleChoiceItems(hor_swing_select, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                selection.clear();
                selection.add(hor_swing_select[item]);
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    public void dialog_ver_swing(View v){

        final ArrayList<String> selection = new ArrayList<>();

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle(R.string.SelectVerticalSwing);
        alt_bld.setNegativeButton(R.string.Cancel, null);
        alt_bld.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(!selection.isEmpty()) {
                    changeMode(getIntent().getStringExtra("devId"), selection.get(0), "/setVerticalSwing");
                    TextView new_mode = (TextView)findViewById(R.id.current_ver_swing);
                    new_mode.setText(selection.get(0));
                    Toast.makeText(ACActivity.this, selection.get(0), Toast.LENGTH_SHORT).show();
                }
            }});
        alt_bld.setSingleChoiceItems(ver_swing_select, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                selection.clear();
                selection.add(ver_swing_select[item]);
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    public void dialog_fan(View v){

        final ArrayList<String> selection = new ArrayList<>();

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle(R.string.SelectFanSpeed);
        alt_bld.setNegativeButton(R.string.Cancel, null);
        alt_bld.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(!selection.isEmpty()) {
                    changeMode(getIntent().getStringExtra("devId"), selection.get(0), "/setFanSpeed");
                    TextView new_mode = (TextView)findViewById(R.id.current_fan);
                    new_mode.setText(selection.get(0));
                    Toast.makeText(ACActivity.this, selection.get(0), Toast.LENGTH_SHORT).show();
                }
            }});
        alt_bld.setSingleChoiceItems(fan_select, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                selection.clear();
                selection.add(fan_select[item]);
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
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
