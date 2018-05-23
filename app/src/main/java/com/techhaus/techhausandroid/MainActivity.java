package com.techhaus.techhausandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
    }

    private void setSingleEvent(GridLayout mainGrid){

        for(int i = 0; i < mainGrid.getChildCount(); i++){
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(finalI == 0){
                        Intent intent = new Intent(MainActivity.this, ActivityOne.class);
                        startActivity(intent);
                    }else if(finalI == 1){
                        Intent intent = new Intent(MainActivity.this, ActivityAlarms.class);
                        startActivity(intent);
                    }
                    else if(finalI == 2){
                        Intent intent = new Intent(MainActivity.this, ActivityLamps.class);
                        startActivity(intent);
                    }
                    else if(finalI == 3){
                        Intent intent = new Intent(MainActivity.this, ActivityBlinds.class);
                        startActivity(intent);
                    }
                    else if(finalI == 4){
                        Intent intent = new Intent(MainActivity.this, ActivityDoors.class);
                        startActivity(intent);
                    }
                    else if(finalI == 5){
                        Intent intent = new Intent(MainActivity.this, ActivityACs.class);
                        startActivity(intent);
                    }
                    else if(finalI == 6){
                        Intent intent = new Intent(MainActivity.this, ActivityRefrigerators.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(MainActivity.this, ActivityOvens.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}


