package com.example.planegame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import java.util.Random;

public class EnemyPlane {
    Context context;
    Bitmap enemyPlane;
    int ex, ey;
    int enemyVelocity;
    Random random;

    public EnemyPlane(Context context) {
        this.context = context;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int newWidth = height / 8;
        int newHeight = height / 8;

        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.plane_enemy);
        enemyPlane = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);
        random = new Random();
        ex = 200 + random.nextInt(400);
        ey = 300;
        enemyVelocity = 14 + random.nextInt(10);
    }

    public Bitmap getEnemyPlane() {
        return enemyPlane;
    }

    public int getEnemyPlaneWidth() {
        return enemyPlane.getWidth();
    }

    public int getEnemyPlaneHeight() {
        return enemyPlane.getHeight();
    }
}
