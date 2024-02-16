package com.example.planegame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartUp extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);

        SharedPreferences prefs = getSharedPreferences("Score", MODE_PRIVATE);
        String myString = prefs.getString("Score_key", "0");

        TextView tv = findViewById(R.id.textView);
        tv.setText(myString);
    }

    public void startGame(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void exitGame(View view) {
        finish();
    }
}
