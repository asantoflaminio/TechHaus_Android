package com.techhaus.techhausandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.techhaus.techhausandroid.Adapter.MyAdapterRoutines;
import com.techhaus.techhausandroid.Models.TitleCreator;
import com.techhaus.techhausandroid.Models.TitleParent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RoutinesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private RequestQueue mQueue;
    List<TitleParent> _titleParents;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((MyAdapterRoutines) recyclerView.getAdapter()).onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines);
        String url = API.getRoutines();
        mQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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

    //    TextView txtInfo = (TextView) findViewById(R.id.txtInfo);

    //    if(getIntent() != null){
    //        String info = getIntent().getStringExtra("info");
    //        txtInfo.setText(info);
    //    }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar10);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                Intent intent = new Intent(RoutinesActivity.this, HelpActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_settings:

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void processResponse(JSONObject response) {
        try {
            _titleParents = new ArrayList<>();
            JSONArray jsonArray = response.getJSONArray("routines");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject routine = jsonArray.getJSONObject(i);
                String routineName = routine.getString("name");
                TitleParent title = new TitleParent(String.format(routineName, i));
                _titleParents.add(title);
            }
            recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            MyAdapterRoutines adapter = new MyAdapterRoutines(this, initData());
            adapter.setParentClickableViewAnimationDefaultDuration();
            adapter.setParentAndIconExpandOnClick(true);
            recyclerView.setAdapter(adapter);

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
                    Intent intent = new Intent(RoutinesActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(RoutinesActivity.this, NotificationsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(RoutinesActivity.this, RoutinesActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
