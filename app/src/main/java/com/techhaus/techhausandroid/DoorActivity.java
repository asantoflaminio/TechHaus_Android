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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


        final ImageView openClosedIcon = (ImageView) findViewById(R.id.openClosedIcon);

        openClosedIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                if(openClosedIcon.getTag().equals(R.drawable.closed)){
                    //la quiero abrir

                    TextView what = (TextView) findViewById(R.id.textView5);
                    ImageView lockIcon =(ImageView) findViewById(R.id.lockIcon);
                    if(!lockIcon.getTag().equals(Integer.valueOf(R.drawable.locked_inside))){
                        what.setText("Open");
                        openClosedIcon.setImageResource(R.drawable.open);
                        openClosedIcon.setTag(Integer.valueOf(R.drawable.open));
                        changeDoorState("open", getIntent().getStringExtra("devId"));
                    }else{
                        Toast.makeText(v.getContext(), "Door must be unlocked first", Toast.LENGTH_LONG).show();
                    }

                }else{
                    //la quiero cerrar
                    openClosedIcon.setImageResource(R.drawable.closed);
                    openClosedIcon.setTag(Integer.valueOf(R.drawable.closed));
                    TextView what = (TextView) findViewById(R.id.textView5);
                    what.setText("Closed");
                    changeDoorState("close", getIntent().getStringExtra("devId"));
                }
            }
        });



        //ahora con lock
        final ImageView lockIcon = (ImageView) findViewById(R.id.lockIcon);

        lockIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                if(lockIcon.getTag().equals(R.drawable.locked_inside)){
                    //la quiero abrir

                    TextView what = (TextView) findViewById(R.id.textView6);
                        what.setText("Unlocked");
                        lockIcon.setImageResource(R.drawable.unlocked_inside);
                        lockIcon.setTag(Integer.valueOf(R.drawable.unlocked_inside));
                        changeDoorState("unlock", getIntent().getStringExtra("devId"));


                }else{
                    //la quiero cerrar
                    ImageView openClosedIcon = (ImageView) findViewById(R.id.openClosedIcon);
                    if(!openClosedIcon.getTag().equals(Integer.valueOf(R.drawable.open))){
                        lockIcon.setImageResource(R.drawable.locked_inside);
                        lockIcon.setTag(Integer.valueOf(R.drawable.locked_inside));
                        TextView what = (TextView) findViewById(R.id.textView6);
                        what.setText("Locked");
                        changeDoorState("lock", getIntent().getStringExtra("devId"));
                    }else{
                        Toast.makeText(v.getContext(), "Door must be closed first", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar5);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        Intent intent = new Intent(DoorActivity.this, ActivityOne.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("info", "Doors");
        startActivity(intent);

        return true;
    }

    private void changeDoorState(String action, String devId) {

        String url = "http://10.0.2.2:8080/api/devices/" + devId + "/" + action;


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
                    Intent intent = new Intent(DoorActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(DoorActivity.this, NotificationsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(DoorActivity.this, RoutinesActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent3.putExtra("info", "Routines");
                    startActivity(intent3);
                    break;
            }


            return true;
        }
    };
}
