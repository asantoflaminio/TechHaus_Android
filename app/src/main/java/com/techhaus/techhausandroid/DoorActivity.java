package com.techhaus.techhausandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class DoorActivity extends AppCompatActivity {
    private RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        TextView txtInfo = (TextView) findViewById(R.id.DoorName);
        if(getIntent() != null){
            String info = getIntent().getStringExtra("devName");
            txtInfo.setText(info);
        }
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
                    String lock = result.getString("lock");
                    if(status.equals("closed")){
                        //imagen blind down
                        //tag de cerrado/down
                        Log.d("mytag", "ESTA closed");
                        ImageView icon = (ImageView) findViewById(R.id.openClosedIcon);
                        TextView what = (TextView) findViewById(R.id.textView5);
                        icon.setImageResource(R.drawable.closed);
                        icon.setTag(Integer.valueOf(R.drawable.closed));
                        what.setText("Closed");
                    }else{
                        //imagen de blind up
                        //tag de up
                        ImageView icon = (ImageView) findViewById(R.id.openClosedIcon);
                        icon.setImageResource(R.drawable.open);
                        icon.setTag(Integer.valueOf(R.drawable.open));
                        TextView what = (TextView) findViewById(R.id.textView5);
                        what.setText("Open");
                    }
                    if(lock.equals("locked")){
                        ImageView icon = (ImageView) findViewById(R.id.lockIcon);
                        TextView what = (TextView) findViewById(R.id.textView6);
                        icon.setImageResource(R.drawable.locked_inside);
                        icon.setTag(Integer.valueOf(R.drawable.locked_inside));
                        what.setText("Locked");
                    }else{
                        ImageView icon = (ImageView) findViewById(R.id.lockIcon);
                        TextView what = (TextView) findViewById(R.id.textView6);
                        icon.setImageResource(R.drawable.unlocked_inside);
                        icon.setTag(Integer.valueOf(R.drawable.unlocked_inside));
                        what.setText("Unlocked");
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

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()){
                case R.id.nav_devices:
                    Intent intent = new Intent(DoorActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(DoorActivity.this, NotificationsActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(DoorActivity.this, RoutinesActivity.class);
                    intent3.putExtra("info", "Routines");
                    startActivity(intent3);
                    break;
            }


            return true;
        }
    };
}
