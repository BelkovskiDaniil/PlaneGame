package com.example.planegame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

public class EnemyShot {
    Bitmap shot;
    Context context;
    int shx, shy;

    public EnemyShot(Context context, int shx, int shy) {
        this.context = context;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int newWidth = height / 50;
        int newHeight = height / 50 * 2;

        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_enemy);

        shot = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);
        this.shx = shx;
        this.shy = shy + 250;
    }

    public Bitmap getShot() {
        return shot;
    }

    public int getShotWidth() {
        return shot.getWidth();
    }

    public int getShotHeight() {
        return shot.getHeight();
    }
}
