package com.techhaus.techhausandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity {
    private String[] dev_select = {"Alarms","Lamps","Blinds","Doors","ACs","Refrigerators", "Ovens"};
    //private String[] dev_select = {getString(R.string.Alarms),getString(R.string.Lamps),getString(R.string.Blinds),getString(R.string.Doors),getString(R.string.ACs),getString(R.string.Refrigerators), getString(R.string.Ovens)};
    private boolean[] dev_selected = {true, true, true, true, true, true, true};
    Set<String> notDev;

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

        //traducciones
        dev_select[0] = getString(R.string.Alarms);
        dev_select[1] = getString(R.string.Lamps);
        dev_select[2] = getString(R.string.Blinds);
        dev_select[3] = getString(R.string.Doors);
        dev_select[4] = getString(R.string.ACs);
        dev_select[5] = getString(R.string.Refrigerators);
        dev_select[6] = getString(R.string.Ovens);

        String devices = "";
        SharedPreferences sharedPreferences = this.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        notDev = sharedPreferences.getStringSet("devices", null);
        if(notDev == null){
            notDev = new HashSet<String>();
        }
        if(notDev.contains("alarm")){
            dev_selected[0] = true;
        }else{
            dev_selected[0] = false;
        }
        if(notDev.contains("lamp")){
            dev_selected[1] = true;
        }else{
            dev_selected[1] = false;
        }
        if(notDev.contains("blind")){
            dev_selected[2] = true;
        }else{
            dev_selected[2] = false;
        }
        if(notDev.contains("door")){
            dev_selected[3] = true;
        }else{
            dev_selected[3] = false;
        }
        if(notDev.contains("ac")){
            dev_selected[4] = true;
        }else{
            dev_selected[4] = false;
        }
        if(notDev.contains("refrigerator")){
            dev_selected[5] = true;
        }else{
            dev_selected[5] = false;
        }
        if(notDev.contains("oven")){
            dev_selected[6] = true;
        }else{
            dev_selected[6] = false;
        }

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

                   SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
                   SharedPreferences.Editor editor = sharedPreferences.edit();
                   notDev = sharedPreferences.getStringSet("devices", null);

                   notDev = new HashSet<String>();


                   for(int i = 0; i < dev_select.length; i++) {
                       if (dev_selected[i]) {
                           if(i != 0)
                               devi += ", ";
                           devi += dev_select[i];
                           Log.d("mytag","Seleccionaste " + getDevice(i));
                           String dev = getDevice(i);
                           notDev.add(dev);
                           editor.putStringSet("devices", notDev);
                           editor.commit();
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
                    if(!isChecked){

                        dev_selected[which] = false;

                    }

                if(dev_selected[which] == false)
                    if(isChecked){

                        dev_selected[which] = true;

                    }

            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    public String getDevice(int num){
        if(num == 0){
            return "alarm";
        }else if(num == 1){
            return "lamp";
        }else if(num == 2){
            return "blind";
        }else if(num == 3){
            return "door";
        }else if(num == 4){
            return "ac";
        }else if(num == 5){
            return "refrigerator";
        }else{
            return "oven";
        }
    }

}
