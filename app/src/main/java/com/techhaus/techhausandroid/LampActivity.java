package com.techhaus.techhausandroid;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
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

public class LampActivity extends AppCompatActivity implements  ColorPicker.ColorPickerListener{
    TextView tvProgressLabel;
    private RequestQueue mQueue;
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Button colorB = (Button) findViewById(R.id.changeColorButton);
        colorB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog();
            }
        });

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
                            Toast.makeText(LampActivity.this, getString(R.string.TurnedOn), Toast.LENGTH_SHORT).show();
                        }
                        changeStatus(deviceId, "/turnOn");

                    }else{
                        if(++check > 1){
                            Toast.makeText(LampActivity.this, getString(R.string.TurnedOff), Toast.LENGTH_SHORT).show();
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

                Toast.makeText(LampActivity.this, getString(R.string.Brightness)+ ": " + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
                changeBrightness(seekBar.getProgress(), deviceId);
            }
        });

       // int progress = seekBar.getProgress();
       // tvProgressLabel = findViewById(R.id.textView);
       // tvProgressLabel.setText("Progress: " + progress);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar11);
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
        Intent intent = new Intent(LampActivity.this, ActivityOne.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(from.equals(getString(R.string.Favorites))){
            intent.putExtra("info", getString(R.string.Favorites));
        }else{
            intent.putExtra("info", getString(R.string.Lamps));
        }


        startActivity(intent);

        return true;
    }

    private void changeBrightness(int progress, String deviceId) {

        String url = API.getDevices() + deviceId + "/setBrightness";

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
        String url = API.getDevices() + devId + "/getState";
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
                    Intent intent2 = new Intent(LampActivity.this, NotificationsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(LampActivity.this, RoutinesActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent3.putExtra("info", getString(R.string.Routines));
                    startActivity(intent3);
                    break;
            }


            return true;
        }
    };

    private void openDialog() {
        //ColorPicker colorPicker = new ColorPicker();
        //colorPicker.show(getSupportFragmentManager(), "code dialog");

        final String [] items = new String[] {getString(R.string.BlueN), getString(R.string.GreenN), getString(R.string.VioletN), getString(R.string.PinkN), getString(R.string.RedN), getString(R.string.OrangeN), getString(R.string.YellowN), getString(R.string.WhiteN)};
        final Integer[] icons = new Integer[] {R.drawable.blue, R.drawable.green, R.drawable.violet, R.drawable.pink, R.drawable.red, R.drawable.orange, R.drawable.yellow, R.drawable.white};
        ListAdapter adapter = new ArrayAdapterWithIcon(this, items, icons);

        new AlertDialog.Builder(this).setTitle(R.string.ChooseColor)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item ) {
                        String code = "";
                        if(item == 4){
                            code = "FF0000"; //Red
                            Toast.makeText(LampActivity.this, getString(R.string.RedN), Toast.LENGTH_SHORT).show();
                        }else if(item == 3){
                            code = "FF69B4"; //Pink
                            Toast.makeText(LampActivity.this, getString(R.string.PinkN), Toast.LENGTH_SHORT).show();
                        }else if(item == 1){
                            code = "00FF00"; //Green
                            Toast.makeText(LampActivity.this, getString(R.string.GreenN), Toast.LENGTH_SHORT).show();

                        }else if(item == 0){
                            code = "0000FF"; //Blue
                            Toast.makeText(LampActivity.this, getString(R.string.BlueN), Toast.LENGTH_SHORT).show();

                        }else if(item == 2){
                            code = "8A2BE2"; //Violet
                            Toast.makeText(LampActivity.this, getString(R.string.VioletN), Toast.LENGTH_SHORT).show();

                        }else if(item == 6){
                            code = "FFFF00"; //Yellow
                            Toast.makeText(LampActivity.this, getString(R.string.YellowN), Toast.LENGTH_SHORT).show();

                        }else if(item == 5){
                            code = "FF8C00"; //Orange
                            Toast.makeText(LampActivity.this,  getString(R.string.OrangeN), Toast.LENGTH_SHORT).show();
                        }else if (item == 7){
                            //White
                            code = "FFFFFF";
                            Toast.makeText(LampActivity.this, getString(R.string.WhiteN), Toast.LENGTH_SHORT).show();

                        }
                        applyHex(code);
                    }
                }).show();
    }

    @Override
    public void applyHex(String hexcode) {
        ImageView circ = (ImageView) findViewById(R.id.circle);
        GradientDrawable drawable = (GradientDrawable) circ.getDrawable();
        // drawable.setColor(Integer.parseInt("F0"+color,16));
        drawable.setColor(Color.parseColor("#F0" + hexcode));
        //falta cambiar en API

        String url = API.getDevices() + getIntent().getStringExtra("devId") + "/setColor";

        JSONArray jarray = new JSONArray();
        jarray.put(hexcode);

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
}
