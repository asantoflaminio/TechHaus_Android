package com.techhaus.techhausandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rout);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        final TextView rutNameView = (TextView) findViewById(R.id.RutinaName);
        rutNameView.setText(getIntent().getStringExtra("rutName"));
        ImageView playIcon = (ImageView) findViewById(R.id.playRut);
        playIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), rutNameView.getText().toString()+ " was played",
                        Toast.LENGTH_LONG).show();

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()){
                case R.id.nav_devices:
                    Intent intent = new Intent(RoutActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(RoutActivity.this, NotificationsActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(RoutActivity.this, RoutinesActivity.class);
                    intent3.putExtra("info", "Routines");
                    startActivity(intent3);
                    break;
            }


            return true;
        }
    };
}
