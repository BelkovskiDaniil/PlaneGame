package com.example.planegame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class GameOver extends AppCompatActivity {

    TextView tvPoints, tvPoints2;
    ImageView imageView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        imageView = findViewById(R.id.imageView2);
        int points = Objects.requireNonNull(getIntent().getExtras()).getInt("points");
        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText(String.valueOf(points));

        SharedPreferences prefs = getSharedPreferences("Score", MODE_PRIVATE);
        String myString = prefs.getString("Score_key", "0");

        if (Integer.parseInt(myString) < points) {
            SharedPreferences prefs2 = getSharedPreferences("Score", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs2.edit();
            editor.putString("Score_key", String.valueOf(points));
            editor.apply();

            imageView.setImageResource(R.drawable.text_box02);
        }

        else {
            imageView.setImageResource(R.drawable.text_box01);
        }

        prefs = getSharedPreferences("Score", MODE_PRIVATE);
        myString = prefs.getString("Score_key", "0");

        tvPoints2 = findViewById(R.id.tvPoints2);
        tvPoints2.setText(myString);
    }

    public void restart(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void exit(View view) {
        finish();
    }

    public void goMenu(View view) {
        Intent intent = new Intent(GameOver.this, StartUp.class);
        startActivity(intent);
        finish();
    }
}
