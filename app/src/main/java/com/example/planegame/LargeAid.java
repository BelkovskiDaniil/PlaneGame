package com.example.planegame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

public class LargeAid {

    Bitmap largeAid;
    Context context;
    int sax, say;

    public LargeAid(Context context, int sax, int say) {
        this.context = context;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int newWidth = height / 20;
        int newHeight = height / 30;

        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.first_aid_kit_big);

        largeAid = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);
        this.sax = sax;
        this.say = say;
    }

    public Bitmap getShot() {
        return largeAid;
    }

    public int getShotWidth() {
        return largeAid.getWidth();
    }

    public int getShotHeight() {
        return largeAid.getHeight();
    }
}
