package com.techhaus.techhausandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private String[] dev_select = {"Alarms","Lamps","Blinds","Doors","ACs","Refrigerators", "Ovens"};
    private boolean[] dev_selected = {true, true, true, true, true, true, true};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        TextView tv = findViewById(R.id.devices_selected);

        String devices = "";

        for(int i = 0; i < dev_select.length; i++) {
            if (dev_selected[i]) {
                if(i != 0)
                    devices += ", ";
                devices += dev_select[i];
            }
        }

        tv.setText(devices);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        Intent intent = new Intent(SettingsActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        return true;
    }

    public void dialog_setDev(View v) {

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        //alt_bld.setIcon(R.drawable.icon);
        alt_bld.setTitle(R.string.DevForNotif);
        alt_bld.setNegativeButton(R.string.Cancel, null);
        alt_bld.setPositiveButton("OK", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
                   String devi = "";

                   for(int i = 0; i < dev_select.length; i++) {
                       if (dev_selected[i]) {
                           if(i != 0)
                               devi += ", ";
                           devi += dev_select[i];
                       }
                   }

                   TextView tv = findViewById(R.id.devices_selected);
                   tv.setText(devi);
               }
        });
        alt_bld.setMultiChoiceItems(dev_select, dev_selected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(dev_selected[which] == true)
                    if(!isChecked)
                        dev_selected[which] = false;
                if(dev_selected[which] == false)
                    if(isChecked)
                        dev_selected[which] = true;
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

}
