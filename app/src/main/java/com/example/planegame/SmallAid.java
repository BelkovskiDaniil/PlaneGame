package com.example.planegame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

public class SmallAid {
    Bitmap smallAid;
    Context context;
    int sax, say;

    public SmallAid(Context context, int sax, int say) {
        this.context = context;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int newWidth = height / 25;
        int newHeight = height / 27;

        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.first_aid_kit_small);

        smallAid = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);
        this.sax = sax;
        this.say = say;
    }

    public Bitmap getShot() {
        return smallAid;
    }

    public int getShotWidth() {
        return smallAid.getWidth();
    }

    public int getShotHeight() {
        return smallAid.getHeight();
    }
}
