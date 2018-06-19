package com.techhaus.techhausandroid;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.techhaus.techhausandroid.Adapter.MyAdapterNotif;
import com.techhaus.techhausandroid.Models.TitleCreator;
import com.techhaus.techhausandroid.Models.TitleParent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

 public class NotificationsActivity extends AppCompatActivity {

    Set<String> myNotif;
    private TextView emptyView;
    List<TitleParent> _titleParents;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        listado();



        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_notif);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void listado() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        myNotif = sharedPreferences.getStringSet("notifications", null);
        emptyView = (TextView) findViewById(R.id.empty_view);
        emptyView.setVisibility(View.VISIBLE);
        if(myNotif == null){
            //myNotif = new HashSet<String>();
            Log.d("mytag", "it was null");
            emptyView = (TextView) findViewById(R.id.empty_view);
            emptyView.setVisibility(View.VISIBLE);
        }else{
            emptyView = (TextView) findViewById(R.id.empty_view);
            if(myNotif.size() > 0){
                emptyView.setVisibility(View.GONE);
            }

            _titleParents = new ArrayList<>();
            for(String s: myNotif){
                Log.d("mytag", "String " + s);
                String[] cosas = s.split(";");
                TitleParent title = new TitleParent(cosas[0] + "\n" + getString(R.string.Update)+ ":" + cosas[1] + ": " + cosas[2]);
                List<Object> childList = new ArrayList<>();
                //childList.add(new TitleChild( getString(R.string.Update)+ ":", cosas[1] + ": " + cosas[2]));
                title.setChildObjectList(childList);
                _titleParents.add(title);


            }
            recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            MyAdapterNotif adapter = new MyAdapterNotif(this, initData());
            adapter.setParentClickableViewAnimationDefaultDuration();
            adapter.setParentAndIconExpandOnClick(true);
            recyclerView.setAdapter(adapter);
        }
    }

    private List<ParentObject> initData() {
        TitleCreator titleCreator = TitleCreator.get(this);
        List<TitleParent> titles = _titleParents;
        List<ParentObject> parentObject = new ArrayList<>();
        for(TitleParent title: titles){

            parentObject.add(title);
        }
        return parentObject;
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(1);
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
                Intent intent = new Intent(NotificationsActivity.this, HelpActivity.class);
                intent.putExtra("from", "notif");
                startActivity(intent);
                return true;

            case R.id.action_settings:
                Intent intent2 = new Intent(NotificationsActivity.this, SettingsActivity.class);
                intent2.putExtra("from", "notif");
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()){
                case R.id.nav_devices:
                    Intent intent = new Intent(NotificationsActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(NotificationsActivity.this, NotificationsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(NotificationsActivity.this, RoutinesActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent3.putExtra("info", getString(R.string.Routines));
                    startActivity(intent3);
                    break;
            }

            return true;
        }
    };

    public void showNotification(View v) {

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        myNotif = new HashSet<String>();
        editor.putStringSet("notifications", myNotif);
        editor.commit();
        emptyView = (TextView) findViewById(R.id.empty_view);
        emptyView.setVisibility(View.VISIBLE);
        listado();

    }

    public void showRefresh(View v) {

        listado();

    }


}
