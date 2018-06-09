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
import android.widget.Button;
import android.widget.ImageView;
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

public class AlarmActivity extends AppCompatActivity implements  CodeDialog.CodeDialogListener, OldNewCodeDialog.OldNewCodeDialogListener{
    private RequestQueue mQueue;
    private String action;
    int check = 0;

    @Override
    public void applyTexts(String code) {
        Log.d("mytag", "Ok la action es " + action);
        Log.d("mytag", "code was " + code);
        if(action.equals("DISARM")){
            executeAction(getIntent().getStringExtra("devId"), code, "/disarm");
        }else if(action.equals("ARMSTAY")){
            executeAction(getIntent().getStringExtra("devId"), code, "/armStay");
        }else{
            executeAction(getIntent().getStringExtra("devId"), code, "/armAway");
        }
    }

    private void executeAction(final String id, String code, String s) {
        String url = "http://10.0.2.2:8080/api/devices/" + id + s;
        JSONArray jarray = new JSONArray();
        jarray.put(code);
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.PUT, url, jarray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                getState(id);
            }




        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag", "Shouldn't be here");
                error.printStackTrace();
                getState(id);
            }
        });
        mQueue.add(request);
    }

    private void changeCode(final String id, String oldCode, String newCode) {
        String url = "http://10.0.2.2:8080/api/devices/" + id + "/changeSecurityCode";
        JSONArray jarray = new JSONArray();
        jarray.put(oldCode);
        jarray.put(newCode);
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.PUT, url, jarray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                TextView txtInfo = (TextView) findViewById(R.id.AlarmName);
                Toast.makeText(txtInfo.getContext(), "Code updated", Toast.LENGTH_LONG).show();
                //ACA NUNCA ENTRA!!
                //NO PUEDO CHEQUEAR SI DEVUELVE NULL Y EL OLDCODE ESTUVO MAL!!
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag", "Shouldn't be here");
                TextView txtInfo = (TextView) findViewById(R.id.AlarmName);
                Toast.makeText(txtInfo.getContext(), "Code updated", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        TextView txtInfo = (TextView) findViewById(R.id.AlarmName);
        if(getIntent() != null){
            String info = getIntent().getStringExtra("devName");
            txtInfo.setText(info);
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        mQueue = Volley.newRequestQueue(this);
        getState(getIntent().getStringExtra("devId"));

        Button disarmB = (Button) findViewById(R.id.button6);
        disarmB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView what = (TextView) findViewById(R.id.textView2);
                if(what.getText().equals("disarmed")){
                    Toast.makeText(v.getContext(), "Alarm already disarmed", Toast.LENGTH_LONG).show();
                }else{
                    action = "DISARM";
                    openDialog();
                }

            }
        });

        Button armAwayB = (Button) findViewById(R.id.button5);
        armAwayB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView what = (TextView) findViewById(R.id.textView2);
                if(what.getText().equals("armedAway")){
                    Toast.makeText(v.getContext(), "Alarm already in armAway mode", Toast.LENGTH_LONG).show();
                }else{
                    action = "ARMAWAY";
                    openDialog();
                }

            }
        });

        Button armStayB = (Button) findViewById(R.id.button4);
        armStayB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView what = (TextView) findViewById(R.id.textView2);
                if(what.getText().equals("armedStay")){
                    Toast.makeText(v.getContext(), "Alarm already in armStay mode", Toast.LENGTH_LONG).show();
                }else{
                    action = "ARMSTAY";
                    openDialog();
                }

            }
        });

        Button passB = (Button) findViewById(R.id.passButton);
        passB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog2();
            }
        });


    }

    private void openDialog() {
        CodeDialog codeDialog = new CodeDialog();
        codeDialog.show(getSupportFragmentManager(), "code dialog");
    }

    private void openDialog2() {
        OldNewCodeDialog codeDialog = new OldNewCodeDialog();
        codeDialog.show(getSupportFragmentManager(), "code dialog2");
    }


    private void getState(String id) {
        String url = "http://10.0.2.2:8080/api/devices/" + id + "/getState";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // ok I guess

                JSONArray jsonArray = null;
                try {

                    JSONObject result = response.getJSONObject("result");
                    String status = result.getString("status");
                    TextView what = (TextView) findViewById(R.id.textView2);
                    if(status.equals(what.getText())){
                        Toast.makeText(what.getContext(), "Incorrect code", Toast.LENGTH_LONG).show();
                    }else{
                        if(++check > 1) {
                            Toast.makeText(what.getContext(), "Status updated", Toast.LENGTH_LONG).show();
                        }
                        what.setText(status);

                        if(status.equals("armedAway")){
                            //imagen blind down
                            //tag de cerrado/down
                            ImageView icon = (ImageView) findViewById(R.id.alarmStatusIcon);
                            icon.setImageResource(R.drawable.alarm_wo_people);
                            icon.setTag(Integer.valueOf(R.drawable.alarm_wo_people));
                        }else if(status.equals("armedStay")){
                            //imagen de blind up
                            //tag de up
                            ImageView icon = (ImageView) findViewById(R.id.alarmStatusIcon);
                            icon.setImageResource(R.drawable.alarm_w_people);
                            icon.setTag(Integer.valueOf(R.drawable.alarm_w_people));

                        }else{
                            //disarmed
                            ImageView icon = (ImageView) findViewById(R.id.alarmStatusIcon);
                            icon.setImageResource(R.drawable.unlocked_inside);
                            icon.setTag(Integer.valueOf(R.drawable.unlocked_inside));
                        }
                    }


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

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        Intent intent = new Intent(AlarmActivity.this, ActivityOne.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("info", "Alarms");
        startActivity(intent);

        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()){
                case R.id.nav_devices:
                    Intent intent = new Intent(AlarmActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(AlarmActivity.this, NotificationsActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(AlarmActivity.this, RoutinesActivity.class);
                    intent3.putExtra("info", "Routines");
                    startActivity(intent3);
                    break;
            }


            return true;
        }
    };

    @Override
    public void applyCodes(String oldcode, String newcode) {
        if(android.text.TextUtils.isDigitsOnly(newcode) && newcode.length() == 4){
            changeCode(getIntent().getStringExtra("devId"), oldcode, newcode);
            // en changecode quiero chequear q si me devuelve null la oldcode estuve mal
        }else{
            Toast.makeText(this, "Invalid format for new code", Toast.LENGTH_LONG).show();
        }

    }
}
