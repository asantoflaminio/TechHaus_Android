package com.techhaus.techhausandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class OvenActivity extends AppCompatActivity {

    Spinner heat_spinner, grill_spinner, convection_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oven);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        TextView txtInfo = (TextView) findViewById(R.id.OvenName);
        if(getIntent() != null){
            String info = getIntent().getStringExtra("devName");
            txtInfo.setText(info);
        }

        //simple spinners
        heat_spinner = findViewById(R.id.spinner5);
        final List<String> heat_spinner_list = new ArrayList<>();
        heat_spinner_list.add("Conventional");
        heat_spinner_list.add("Bottom");
        heat_spinner_list.add("Top");

        ArrayAdapter<String> heat_spinner_adapter = new ArrayAdapter<>(OvenActivity.this, R.layout.support_simple_spinner_dropdown_item, heat_spinner_list);
        heat_spinner.setAdapter(heat_spinner_adapter);
        heat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(OvenActivity.this, heat_spinner_list.get(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        grill_spinner = findViewById(R.id.spinner6);
        final List<String> grill_spinner_list = new ArrayList<>();
        grill_spinner_list.add("Large");
        grill_spinner_list.add("Eco");
        grill_spinner_list.add("Off");

        ArrayAdapter<String> grill_spinner_adapter = new ArrayAdapter<>(OvenActivity.this, R.layout.support_simple_spinner_dropdown_item, grill_spinner_list);
        grill_spinner.setAdapter(grill_spinner_adapter);

        grill_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(OvenActivity.this, grill_spinner_list.get(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        convection_spinner = findViewById(R.id.spinner7);
        final List<String> convection_spinner_list = new ArrayList<>();
        convection_spinner_list.add("Normal");
        convection_spinner_list.add("Eco");
        convection_spinner_list.add("Off");

        ArrayAdapter<String> convection_spinner_adapter = new ArrayAdapter<>(OvenActivity.this, R.layout.support_simple_spinner_dropdown_item, convection_spinner_list);
        convection_spinner.setAdapter(convection_spinner_adapter);

        convection_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(OvenActivity.this, convection_spinner_list.get(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()){
                case R.id.nav_devices:
                    Intent intent = new Intent(OvenActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(OvenActivity.this, NotificationsActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(OvenActivity.this, RoutinesActivity.class);
                    intent3.putExtra("info", "Routines");
                    startActivity(intent3);
                    break;
            }


            return true;
        }
    };


}
