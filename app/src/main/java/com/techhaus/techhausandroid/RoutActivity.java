package com.techhaus.techhausandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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

import java.util.ArrayList;
import java.util.HashMap;

public class RoutActivity extends AppCompatActivity {

    private RequestQueue mQueue;
   // private RequestQueue mQueue2;
   // private RequestQueue mQueue3;
   // private RequestQueue mQueue4;
    private HashMap<String, String> idConName;
    private HashMap<String, ArrayList<String>> idConActions;
    private String tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rout);
        tasks="";

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        final TextView rutNameView = (TextView) findViewById(R.id.RutinaName);
        rutNameView.setText(getIntent().getStringExtra("rutName"));
        ImageView playIcon = (ImageView) findViewById(R.id.playRut);
        playIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {


                final String url3 = API.getRoutines();
                final String rutName = rutNameView.getText().toString();
                Toast.makeText(v.getContext(), rutNameView.getText().toString()+" " + getString(R.string.WasPlayed),
                        Toast.LENGTH_LONG).show();
                //mQueue3 = Volley.newRequestQueue(v.getContext());
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url3, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String routID = "";
                            JSONArray jsonArray = response.getJSONArray("routines");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject routine = jsonArray.getJSONObject(i);
                                if(rutName.equals(routine.getString("name"))){
                                    routID = routine.getString("id");
                                    executeRoutine(v, routID);
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
        });
        String url = API.getRoutines();
        mQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("mytag", "A LLAMAR");
                processResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag", "Error de response");
                error.printStackTrace();
            }
        });

        mQueue.add(request);
        Log.d("mytag", "i'm out");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar12);
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
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        Intent intent = new Intent(RoutActivity.this, RoutinesActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("info", getString(R.string.Routines));
        startActivity(intent);

        return true;
    }
    private void executeRoutine(View v, String routID) {
        //mQueue4 = Volley.newRequestQueue(v.getContext());
        String url = API.getRoutines() + routID + "/execute";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("mytag", "Executed!");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag", "Error de response en execute");
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    private void processResponse(JSONObject response) {
        try {
            String tareas="";
            JSONArray actions;
            JSONArray jsonArray = response.getJSONArray("routines");
            String urlDevices = API.getDevices();
            idConActions = new HashMap<String, ArrayList<String>>();

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject routine = jsonArray.getJSONObject(i);
                String routineName = routine.getString("name");
                if(routineName.equals(getIntent().getStringExtra("rutName"))){
                    actions = routine.getJSONArray("actions");
                    for(int j = 0 ; j < actions.length(); j++){
                        JSONObject action = actions.getJSONObject(j);
                        String deviceId = action.getString("deviceId");
                        String actionName = action.getString("actionName");
                        if(idConActions.get(deviceId) != null){
                            idConActions.get(deviceId).add(actionName);
                        }else{
                            ArrayList<String> act = new ArrayList<String>();
                            act.add(actionName);
                            idConActions.put(deviceId, act);
                        }


                    }
                   // mQueue2 = Volley.newRequestQueue(this);
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlDevices, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                HashMap<String, String> idConName = new HashMap<String, String>();
                                JSONArray devices = response.getJSONArray("devices");
                                for(int i = 0; i < devices.length(); i++){
                                    JSONObject device = devices.getJSONObject(i);
                                    idConName.put(device.getString("id"), device.getString("name"));
                                }
                                for(String id: idConActions.keySet()){
                                    for(String id2: idConName.keySet()){
                                        if(id.equals(id2)){
                                            for(String action: idConActions.get(id)){
                                                tasks = tasks + idConName.get(id2)+ ": " + action + "\n";
                                            }
                                        }
                                    }
                                }

                                TextView taskList = (TextView) findViewById(R.id.TasksList);
                                taskList.setText(tasks);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("mytag", "Error de response2");
                            error.printStackTrace();
                        }
                    });

                    mQueue.add(request);

                }
            }



        } catch (JSONException e) {
            Log.d("mytag", "STH WENT WRONG");
            e.printStackTrace();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()){
                case R.id.nav_devices:
                    Intent intent = new Intent(RoutActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(RoutActivity.this, NotificationsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(RoutActivity.this, RoutinesActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent3.putExtra("info", getString(R.string.Routines));
                    startActivity(intent3);
                    break;
            }


            return true;
        }
    };
}
