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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RefrigeratorActivity extends AppCompatActivity {

    Spinner fridge_mode_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //custom spinner
        fridge_mode_spinner = (Spinner) findViewById(R.id.spinner_fridge_mode);
        final List<SpinnerData> customList1 = new ArrayList<>();
        customList1.add(new SpinnerData(R.drawable.fridge_default, "Default"));
        customList1.add(new SpinnerData(R.drawable.party, "Party"));
        customList1.add(new SpinnerData(R.drawable.vacation, "Vacation"));

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(RefrigeratorActivity.this, R.layout.spinner_layout, customList1);
        fridge_mode_spinner.setAdapter(customSpinnerAdapter);

        fridge_mode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(RefrigeratorActivity.this, customList1.get(i).getIconName(), Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(RefrigeratorActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(RefrigeratorActivity.this, NotificationsActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(RefrigeratorActivity.this, RoutinesActivity.class);
                    intent3.putExtra("info", "Routines");
                    startActivity(intent3);
                    break;
            }


            return true;
        }
    };
}
