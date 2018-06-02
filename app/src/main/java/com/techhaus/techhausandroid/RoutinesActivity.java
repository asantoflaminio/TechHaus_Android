package com.techhaus.techhausandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.techhaus.techhausandroid.Adapter.MyAdapter;
import com.techhaus.techhausandroid.Models.TitleChild;
import com.techhaus.techhausandroid.Models.TitleCreator;
import com.techhaus.techhausandroid.Models.TitleParent;

import java.util.ArrayList;
import java.util.List;

public class RoutinesActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((MyAdapter) recyclerView.getAdapter()).onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines);

        TextView txtInfo = (TextView) findViewById(R.id.txtInfo);


        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter adapter = new MyAdapter(this, initData());
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);
        recyclerView.setAdapter(adapter);
        if(getIntent() != null){
            String info = getIntent().getStringExtra("info");
            txtInfo.setText(info);
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()){
                case R.id.nav_devices:
                    Intent intent = new Intent(RoutinesActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(RoutinesActivity.this, NotificationsActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(RoutinesActivity.this, RoutinesActivity.class);
                    intent3.putExtra("info", "Routines");
                    startActivity(intent3);
                    break;
            }


            return true;
        }
    };

    private List<ParentObject> initData() {
        TitleCreator titleCreator = TitleCreator.get(this);
        List<TitleParent> titles = titleCreator.getAll();
        List<ParentObject> parentObject = new ArrayList<>();
        for(TitleParent title: titles){
            List<Object> childList = new ArrayList<>();
            childList.add(new TitleChild("Algo", "Algo2"));
            title.setChildObjectList(childList);
            parentObject.add(title);
        }
        return parentObject;
    }
}
