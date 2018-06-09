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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AlarmActivity extends AppCompatActivity {
    private RequestQueue mQueue;

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
}
