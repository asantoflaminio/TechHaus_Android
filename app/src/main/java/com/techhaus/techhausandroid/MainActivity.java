package com.techhaus.techhausandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GridLayout mainGrid;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        setSingleEvent(mainGrid);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()){
                case R.id.nav_devices:
                    break;
                case R.id.nav_notifications:
                    Intent intent2 = new Intent(MainActivity.this, NotificationsActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_routines:
                    Intent intent3 = new Intent(MainActivity.this, RoutinesActivity.class);
                    intent3.putExtra("info", getString(R.string.Routines));
                    startActivity(intent3);
                    break;
            }

            return true;
        }
    };


    private void setSingleEvent(GridLayout mainGrid){

        for(int i = 0; i < mainGrid.getChildCount(); i++){
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, ActivityOne.class);
                    if(finalI == 0){
                        intent.putExtra("info", getString(R.string.Favorites));
                    }else if(finalI == 1){
                        intent.putExtra("info", getString(R.string.Alarms));
                    }else if(finalI == 2){
                        intent.putExtra("info", getString(R.string.Lamps));
                    }else if(finalI == 3){
                        intent.putExtra("info", getString(R.string.Blinds));
                    }else if(finalI == 4){
                        intent.putExtra("info", getString(R.string.Doors));
                    }else if(finalI == 5){
                        intent.putExtra("info", getString(R.string.ACs));
                    }else if(finalI == 6){
                        intent.putExtra("info", getString(R.string.Refrigerators));
                    }else {
                        intent.putExtra("info", getString(R.string.Ovens));
                    }


                    startActivity(intent);

                }
            });
        }
    }
}


