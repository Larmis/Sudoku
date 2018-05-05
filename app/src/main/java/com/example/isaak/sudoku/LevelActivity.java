package com.example.isaak.sudoku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LevelActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        final Button btnLevel1 = findViewById(R.id.btnLevel1);
        btnLevel1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LevelActivity.this, ListActivity.class);
                intent.putExtra("level", 1);
                startActivity(intent);
            }
        });

        final Button btnLevel2 = findViewById(R.id.btnLevel2);
        btnLevel2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LevelActivity.this, ListActivity.class);
                intent.putExtra("level", 2);
                startActivity(intent);
            }
        });

    }

}
