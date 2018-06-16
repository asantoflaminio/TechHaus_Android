
package com.techhaus.techhausandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
        getState(getIntent().getStringExtra("devId"));


        final ImageView upDownIcon = (ImageView) findViewById(R.id.UpDownIcon);

        upDownIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                if(upDownIcon.getTag().equals(R.drawable.blind_down)){
                    //la quiero abrir
                    upDownIcon.setImageResource(R.drawable.blind_up);
                    upDownIcon.setTag(Integer.valueOf(R.drawable.blind_up));
                    TextView what = (TextView) findViewById(R.id.textView4);
                    what.setText("Up");
                    changeBlindState("up", getIntent().getStringExtra("devId"));
                }else{
                    //la quiero cerrar
                    upDownIcon.setImageResource(R.drawable.blind_down);
                    upDownIcon.setTag(Integer.valueOf(R.drawable.blind_down));
                    TextView what = (TextView) findViewById(R.id.textView4);
                    what.setText("Down");
                    changeBlindState("down", getIntent().getStringExtra("devId"));
                }
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
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
        Intent intent = new Intent(BlindActivity.this, ActivityOne.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(from.equals("Favorites")){
            intent.putExtra("info", "Favorites");
        }else{
            intent.putExtra("info", "Blinds");
        }


        startActivity(intent);

        return true;
    }

    private void changeBlindState(String action, String id) {
        Log.d("mytag","Ok my id es " + id);
        String url = "";
        if(action.equals("down")){
            url = API.getDevices() + id + "/down";
        }else{
            url = API.getDevices() + id + "/up";
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("mytag", "Status changed!");
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
        String url = API.getDevices() + id + "/getState";
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
                    Intent intent = new Intent(BlindActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(BlindActivity.this, NotificationsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(BlindActivity.this, RoutinesActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent3.putExtra("info", getString(R.string.Routines));
                    startActivity(intent3);
                    break;
            }


            return true;
        }
    };
}
