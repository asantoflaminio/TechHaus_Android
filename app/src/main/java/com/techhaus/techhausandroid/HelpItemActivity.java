package com.techhaus.techhausandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class HelpItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String layout = getIntent().getStringExtra("helpName");

        if (layout.equals(getString(R.string.GetKnow)))
            setContentView(R.layout.help_item_activity);
        else if (layout.equals(getString(R.string.PerAct)))
            setContentView(R.layout.help_item_activity1);
        else if (layout.equals(getString(R.string.FavDev)))
            setContentView(R.layout.help_item_activity2);
        else if (layout.equals(getString(R.string.PlayRout)))
            setContentView(R.layout.help_item_activity3);
        else if (layout.equals(getString(R.string.Managing)))
            setContentView(R.layout.help_item_activity4);


        final TextView txtInfo = (TextView) findViewById(R.id.help_item_title);
        if (getIntent() != null) {
            String info = getIntent().getStringExtra("helpName");
            txtInfo.setText(info);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_help_item);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String from = getIntent().getStringExtra("from");

        Intent intent = new Intent(HelpItemActivity.this, HelpActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("from", from);
        startActivity(intent);
        return true;
    }
}