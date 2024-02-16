package com.example.planegame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import java.util.Random;

public class OurPlane {
    Context context;
    Bitmap ourPlane;
    int ox, oy;
    Random random;
    public OurPlane(Context context) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int newWidth = height / 8;
        int newHeight = height / 8;

        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.plane);

        this.context = context;
        ourPlane = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);
        random = new Random();
        ox = random.nextInt(PlaneGame.screenWidth);
        oy = PlaneGame.screenHeight - ourPlane.getHeight() - 100;
    }

    public Bitmap getOurPlane() {
        return ourPlane;
    }

    public int getOurPlaneWidth() {
        return ourPlane.getWidth();
    }

    public int getOurPlaneHeight() {
        return ourPlane.getHeight();
    }
}
