package com.techhaus.techhausandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.techhaus.techhausandroid.Adapter.MyAdapter;
import com.techhaus.techhausandroid.Models.TitleCreator;
import com.techhaus.techhausandroid.Models.TitleParent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityOne extends AppCompatActivity {
    RecyclerView recyclerView;
    List<TitleParent> _titleParents;
    private RequestQueue mQueue;
    private RequestQueue mQueue2;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((MyAdapter) recyclerView.getAdapter()).onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        TextView txtInfo = (TextView) findViewById(R.id.txtInfo);


        if(getIntent() != null){
            String info = getIntent().getStringExtra("info");
            txtInfo.setText(info);
        }
        String urlTypes = API.getDeviceTypes();
        mQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlTypes, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    processResponse(response);
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

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar8);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        Intent intent = new Intent(ActivityOne.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        return true;
    }

    private void processResponse(JSONObject response) throws JSONException {
        String info = getIntent().getStringExtra("info");

        String searchId = "";
        JSONArray jsonArray = response.getJSONArray("devices");
        if(info.equals("Alarms")){
            searchId = "alarm" ;
        }else if(info.equals("Lamps")){
            searchId = "lamp";
        }else if(info.equals("Blinds")){
            searchId = "blind";
        }else if(info.equals("Doors")){
            searchId = "door";
        }else if(info.equals("ACs")){
            searchId = "ac";
        }else if(info.equals("Refrigerators")){
            searchId = "refrigerator";
        }else if(info.equals("Ovens")){
            searchId = "oven";
        }else{
            searchId = "faves";

        }
        if(searchId.equals("faves")){
            getFavedDevices();
        }else{
            String typeName = "";
            String typeId = "";
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject type = jsonArray.getJSONObject(i);
                typeName = type.getString("name");
                typeId = type.getString("id");
                if(typeName.equals(searchId)){
                    getDevicesForType(typeId);
                }

            }
        }


    }

    private void getFavedDevices() {
        String urlDevices = API.getDevices();
       // mQueue2 = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlDevices, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    processDevicesFaved(response);
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

    private void getDevicesForType(String typeId) {
        String urlDevicesForType = API.getDevices() + "devicetypes/" + typeId;
        //mQueue2 = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlDevicesForType, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    processDevices(response);
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

    private void processDevicesFaved(JSONObject response) throws JSONException {
        JSONArray jsonArray = response.getJSONArray("devices");
        _titleParents = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject device = jsonArray.getJSONObject(i);
            String meta = device.getString("meta");
            List<String> elephantList = Arrays.asList(meta.replace("{","").replace("}","").split(","));
            if(elephantList.size() > 1 && elephantList.get(1).equals(" faved")){
                String name = device.getString("name");
                TitleParent title = new TitleParent(String.format(name, i));
                _titleParents.add(title);
            }
        }
        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter adapter = new MyAdapter(this, initData());
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);
        recyclerView.setAdapter(adapter);
    }

    private void processDevices(JSONObject response) throws JSONException {
        JSONArray jsonArray = response.getJSONArray("devices");
        _titleParents = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject device = jsonArray.getJSONObject(i);
            String name = device.getString("name");
            TitleParent title = new TitleParent(String.format(name, i));
            _titleParents.add(title);
        }
        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter adapter = new MyAdapter(this, initData());
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);
        recyclerView.setAdapter(adapter);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()){
                case R.id.nav_devices:
                    Intent intent = new Intent(ActivityOne.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(ActivityOne.this, NotificationsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(ActivityOne.this, RoutinesActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent3.putExtra("info", getString(R.string.Routines));
                    startActivity(intent3);
                    break;
            }


            return true;
        }
    };

    private List<ParentObject> initData() {
        TitleCreator titleCreator = TitleCreator.get(this);
        List<TitleParent> titles = _titleParents;
        List<ParentObject> parentObject = new ArrayList<>();
        for(TitleParent title: titles){
            List<Object> childList = new ArrayList<>();
            title.setChildObjectList(childList);
            parentObject.add(title);
        }
        return parentObject;
    }
}
