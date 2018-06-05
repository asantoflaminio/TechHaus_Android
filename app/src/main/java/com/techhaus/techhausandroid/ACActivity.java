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

public class ACActivity extends AppCompatActivity {

    Spinner mode_spinner, fan_spinner, vertical_swing_spinner, horizontal_swing_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        TextView txtInfo = (TextView) findViewById(R.id.ACName);
        if(getIntent() != null){
            String info = getIntent().getStringExtra("devName");
            txtInfo.setText(info);
        }

        //custom spinner
        mode_spinner = findViewById(R.id.spinner);
        final List<SpinnerData> customList1 = new ArrayList<>();
        customList1.add(new SpinnerData(R.drawable.cool, "Cool"));
        customList1.add(new SpinnerData(R.drawable.heat, "Heat"));
        customList1.add(new SpinnerData(R.drawable.fan, "Fan"));

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(ACActivity.this, R.layout.spinner_layout, customList1);
        mode_spinner.setAdapter(customSpinnerAdapter);

        mode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(ACActivity.this, customList1.get(i).getIconName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //simple spinners
        fan_spinner = findViewById(R.id.spinner2);
        final List<String> fan_spinner_list = new ArrayList<>();
        fan_spinner_list.add("Auto");
        fan_spinner_list.add("25");
        fan_spinner_list.add("50");
        fan_spinner_list.add("75");
        fan_spinner_list.add("100");

        ArrayAdapter<String> fan_spinner_adapter = new ArrayAdapter<>(ACActivity.this, R.layout.support_simple_spinner_dropdown_item, fan_spinner_list);
        fan_spinner.setAdapter(fan_spinner_adapter);

        fan_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(ACActivity.this, fan_spinner_list.get(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        vertical_swing_spinner = findViewById(R.id.spinner3);
        final List<String> vertical_swing_spinner_list = new ArrayList<>();
        vertical_swing_spinner_list.add("Auto");
        vertical_swing_spinner_list.add("22");
        vertical_swing_spinner_list.add("45");
        vertical_swing_spinner_list.add("67");
        vertical_swing_spinner_list.add("90");

        ArrayAdapter<String> vertical_swing_spinner_adapter = new ArrayAdapter<>(ACActivity.this, R.layout.support_simple_spinner_dropdown_item, vertical_swing_spinner_list);
        vertical_swing_spinner.setAdapter(vertical_swing_spinner_adapter);

        vertical_swing_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(ACActivity.this, vertical_swing_spinner_list.get(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        horizontal_swing_spinner = findViewById(R.id.spinner4);
        final List<String> horizontal_swing_spinner_list = new ArrayList<>();
        horizontal_swing_spinner_list.add("Auto");
        horizontal_swing_spinner_list.add("-90");
        horizontal_swing_spinner_list.add("-45");
        horizontal_swing_spinner_list.add("0");
        horizontal_swing_spinner_list.add("45");
        horizontal_swing_spinner_list.add("90");

        ArrayAdapter<String> horizontal_swing_spinner_adapter = new ArrayAdapter<>(ACActivity.this, R.layout.support_simple_spinner_dropdown_item, horizontal_swing_spinner_list);
        horizontal_swing_spinner.setAdapter(horizontal_swing_spinner_adapter);

        horizontal_swing_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(ACActivity.this, horizontal_swing_spinner_list.get(i), Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(ACActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(ACActivity.this, NotificationsActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(ACActivity.this, RoutinesActivity.class);
                    intent3.putExtra("info", "Routines");
                    startActivity(intent3);
                    break;
            }


            return true;
        }
    };
}
