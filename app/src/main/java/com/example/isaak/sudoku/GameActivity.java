package com.example.isaak.sudoku;

import android.os.Bundle;
import android.app.Activity;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String line = getIntent().getStringExtra("grid");

        setContentView(new Draw(this, line));

    }

}
