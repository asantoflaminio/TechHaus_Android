
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

import java.util.Arrays;
import java.util.List;

public class BlindActivity extends AppCompatActivity {
    private RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blind);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        final TextView txtInfo = (TextView) findViewById(R.id.BlindName);
        if(getIntent() != null){
            String info = getIntent().getStringExtra("devName");
            txtInfo.setText(info);
        }

        mQueue = Volley.newRequestQueue(this);
        final String url = "http://10.0.2.2:8080/api/devices";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String devName = txtInfo.getText().toString();
                    JSONArray jsonArray = response.getJSONArray("devices");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject device = jsonArray.getJSONObject(i);
                        if(devName.equals(device.getString("name"))){

                           // GETSTATE ETC
                            Log.d("mytag", "FOUND");
                            getState(device.getString("id"));
                           /* String meta = device.getString("meta");
                            List<String> elephantList = Arrays.asList(meta.replace("{","").replace("}","").split(","));

                            if(elephantList.size() > 1 && elephantList.get(1).equals(" faved")){
                                heartIcon.setImageResource(R.drawable.heart_filled);
                                heartIcon.setTag(Integer.valueOf(R.drawable.heart_filled));
                            }else{
                                heartIcon.setTag(Integer.valueOf(R.drawable.heart_unfilled));
                            }*/
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

    private void getState(String id) {
        String url = "http://10.0.2.2:8080/api/devices/" + id + "/getState";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // ok I guess

                JSONArray jsonArray = null;
                try {
                    Log.d("mytag", "en try");
                    JSONObject result = response.getJSONObject("result");
                    String status = result.getString("status");
                    Log.d("mytag", "OK!");
                    if(status.equals("closing") || status.equals("closed")){
                        //imagen blind down
                        //tag de cerrado/down
                        Log.d("mytag", "ESTA DOWN");
                        ImageView icon = (ImageView) findViewById(R.id.UpDownIcon);
                        TextView what = (TextView) findViewById(R.id.textView4);
                        icon.setImageResource(R.drawable.blind_down);
                        icon.setTag(Integer.valueOf(R.drawable.blind_down));
                        what.setText("Down");
                    }else{
                        //imagen de blind up
                        //tag de up
                        ImageView icon = (ImageView) findViewById(R.id.UpDownIcon);
                        icon.setImageResource(R.drawable.blind_up);
                        icon.setTag(Integer.valueOf(R.drawable.blind_up));
                        TextView what = (TextView) findViewById(R.id.textView4);
                        what.setText("Up");
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
                    Intent intent = new Intent(BlindActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(BlindActivity.this, NotificationsActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(BlindActivity.this, RoutinesActivity.class);
                    intent3.putExtra("info", "Routines");
                    startActivity(intent3);
                    break;
            }


            return true;
        }
    };
}
