package com.techhaus.techhausandroid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
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

public class LampActivity extends AppCompatActivity {
    TextView tvProgressLabel;
    private RequestQueue mQueue;
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        TextView txtInfo = (TextView) findViewById(R.id.LampName);
        if(getIntent() != null){
            String info = getIntent().getStringExtra("devName");
            txtInfo.setText(info);
        }
        mQueue = Volley.newRequestQueue(this);
        getState(getIntent().getStringExtra("devId"));



        final String deviceId = getIntent().getStringExtra("devId");

        Switch s = (Switch) findViewById(R.id.switch3);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                    if(isChecked){
                        if(++check > 1){
                            Toast.makeText(LampActivity.this, "Turned on", Toast.LENGTH_SHORT).show();
                        }
                        changeStatus(deviceId, "/turnOn");

                    }else{
                        if(++check > 1){
                            Toast.makeText(LampActivity.this, "Turned off", Toast.LENGTH_SHORT).show();
                        }
                        changeStatus(deviceId, "/turnOff");
                    }


            }
        });


        // set a change listener on the SeekBar
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                  }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                Toast.makeText(LampActivity.this, "Brightness: " + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
                changeBrightness(seekBar.getProgress(), deviceId);
            }
        });

       // int progress = seekBar.getProgress();
       // tvProgressLabel = findViewById(R.id.textView);
       // tvProgressLabel.setText("Progress: " + progress);
    }

    private void changeBrightness(int progress, String deviceId) {

        String url = "http://10.0.2.2:8080/api/devices/" + deviceId + "/setBrightness";

        Log.d("mytag", "Progress es " + progress);
        String t = String.valueOf(progress);
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

    private void getState(String devId) {
        String url = "http://10.0.2.2:8080/api/devices/" + devId + "/getState";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(JSONObject response) {

                try {
                   JSONObject result = response.getJSONObject("result");
                    String status = result.getString("status");
                    String color = result.getString("color");
                    String brightness = result.getString("brightness");
                    Switch s = (Switch) findViewById(R.id.switch3);
                    //color = "0xF0" + color;
                    SeekBar bar = (SeekBar) findViewById(R.id.seekBar);
                    ImageView circ = (ImageView) findViewById(R.id.circle);
                    GradientDrawable drawable = (GradientDrawable) circ.getDrawable();
                   // drawable.setColor(Integer.parseInt("F0"+color,16));
                    drawable.setColor(Color.parseColor("#F0" + color));


                   // col.setText("#" + color);
                    if(status.equals("on")){

                        s.setChecked(true);
                    }else{
                        check++;
                        s.setChecked(false);
                    }
                    bar.setProgress(Integer.parseInt(brightness));
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
        String url = "http://10.0.2.2:8080/api/devices/" + deviceId + s;
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


    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            //tvProgressLabel.setText("Brightness: " + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()){
                case R.id.nav_devices:
                    Intent intent = new Intent(LampActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(LampActivity.this, NotificationsActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(LampActivity.this, RoutinesActivity.class);
                    intent3.putExtra("info", "Routines");
                    startActivity(intent3);
                    break;
            }


            return true;
        }
    };
}
