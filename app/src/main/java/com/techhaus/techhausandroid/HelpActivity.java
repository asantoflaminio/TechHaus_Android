package com.techhaus.techhausandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.techhaus.techhausandroid.Adapter.MyAdapterHelp;
import com.techhaus.techhausandroid.Models.TitleCreator;
import com.techhaus.techhausandroid.Models.TitleParent;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<TitleParent> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_help);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        list = new ArrayList<>();
        TitleParent title5 = new TitleParent(getString(R.string.GetKnow));
        TitleParent title1 = new TitleParent(getString(R.string.PerAct));
        TitleParent title2 = new TitleParent(getString(R.string.FavDev));
        TitleParent title3 = new TitleParent(getString(R.string.PlayRout));
        TitleParent title4 = new TitleParent(getString(R.string.Managing));


        list.add(title5);
        list.add(title1);
        list.add(title2);
        list.add(title3);
        list.add(title4);

        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerViewH);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapterHelp adapter = new MyAdapterHelp(this, initData());
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);
        recyclerView.setAdapter(adapter);

    }

    private List<ParentObject> initData() {
        TitleCreator titleCreator = TitleCreator.get(this);
        List<TitleParent> titles = list;
        List<ParentObject> parentObject = new ArrayList<>();
        for(TitleParent title: titles){
            List<Object> childList = new ArrayList<>();
            title.setChildObjectList(childList);
            parentObject.add(title);
        }
        return parentObject;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        Intent intent = new Intent(HelpActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        return true;
    }
}
