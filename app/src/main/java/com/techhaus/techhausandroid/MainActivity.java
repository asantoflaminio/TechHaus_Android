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

                    Intent intent = new Intent(MainActivity.this, ActivityOne.class);
                    if(finalI == 0){
                        intent.putExtra("info", "Favorites was selected");
                    }else if(finalI == 1){
                        intent.putExtra("info", "This is alarms ");
                    }else if(finalI == 2){
                        intent.putExtra("info", "This is lamps");
                    }else if(finalI == 3){
                        intent.putExtra("info", "This is blinds");
                    }else if(finalI == 4){
                        intent.putExtra("info", "This is doors");
                    }else if(finalI == 5){
                        intent.putExtra("info", "This is ACs");
                    }else if(finalI == 5){
                        intent.putExtra("info", "This is refrigerators");
                    }else {
                        intent.putExtra("info", "This is ovens");
                    }


                    startActivity(intent);

                }
            });
        }
    }
}


