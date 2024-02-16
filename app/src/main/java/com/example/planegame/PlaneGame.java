package com.example.planegame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Random;

public class PlaneGame extends View {
    Context context;
    private View myView;
    Bitmap background, lifeImageGreen, lifeImageYellow, lifeImageRed, lineMenu, resizedLineMenu, buttonBack, resizedLineButton, healthBar, resizedHealth, resizedGreen, resizedYellow, resizedRed, score, resizedScore, scorePlace, resizedScorePlace;
    Handler handler;
    long UPDATE_MILLIS = 30;
    static int screenWidth, screenHeight;
    int points = 0;
    int life = 12;
    Paint scorePaint;
    int TEXT_SIZE = 40;
    boolean paused = false, isAid = false;
    OurPlane ourPlane;
    EnemyPlane enemyPlane;
    SmallAid smallAid;
    LargeAid largeAid;
    Random random;
    ArrayList<SmallAid> smallAids;
    ArrayList<LargeAid> largeAids;
    ArrayList<Shot> ourShots;
    ArrayList<EnemyShot> enemyShots;
    boolean enemyShotAction = false;

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    @SuppressLint("ResourceAsColor")
    public PlaneGame (Context context) {
        super(context);
        this.context = context;
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        random = new Random();
        smallAids = new ArrayList<>();
        largeAids = new ArrayList<>();
        enemyShots = new ArrayList<>();
        ourShots = new ArrayList<>();
        ourPlane = new OurPlane(context);
        enemyPlane = new EnemyPlane(context);
        handler = new Handler();
        score = BitmapFactory.decodeResource(context.getResources(), R.drawable.text_score01);
        resizedScore = Bitmap.createScaledBitmap(score, 200, 40, false);
        scorePlace = BitmapFactory.decodeResource(context.getResources(), R.drawable.score);
        resizedScorePlace = Bitmap.createScaledBitmap(scorePlace, 200, 50, false);
        healthBar = BitmapFactory.decodeResource(context.getResources(), R.drawable.health);
        resizedHealth = Bitmap.createScaledBitmap(healthBar, 530, 120, false);
        buttonBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu);
        resizedLineButton = Bitmap.createScaledBitmap(buttonBack, 150, 120, false);
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background05);
        lineMenu = BitmapFactory.decodeResource(context.getResources(), R.drawable.background07);
        resizedLineMenu = Bitmap.createScaledBitmap(lineMenu, 100000, 10, false);

        lifeImageGreen = BitmapFactory.decodeResource(context.getResources(), R.drawable.health_point03);
        resizedGreen = Bitmap.createScaledBitmap(lifeImageGreen, 16, 65, false);

        lifeImageYellow = BitmapFactory.decodeResource(context.getResources(), R.drawable.health_point02);
        resizedYellow = Bitmap.createScaledBitmap(lifeImageYellow, 16, 65, false);
        lifeImageRed = BitmapFactory.decodeResource(context.getResources(), R.drawable.health_point01);
        resizedRed = Bitmap.createScaledBitmap(lifeImageRed, 16, 65, false);
        scorePaint = new Paint();
        scorePaint.setTextSize(TEXT_SIZE);
        scorePaint.setTextAlign(Paint.Align.LEFT);
        Typeface plain = ResourcesCompat.getFont(context, R.font.rockwell_extra_bold);
        scorePaint.setTypeface(plain);
        scorePaint.setColor(Color.parseColor("#FEFEFE"));
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myView = inflater.inflate(R.layout.plane_game, null);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        @SuppressLint("DrawAllocation") Rect dstRect = new Rect(0, 0, width, height);
        canvas.drawBitmap(background, null, dstRect, null);
        canvas.drawBitmap(resizedLineMenu, 0, 200, null);
        /*canvas.drawText("" + points, (float) width /2, TEXT_SIZE, scorePaint);*/
        canvas.drawBitmap(resizedLineButton, 20, 38, null);
        canvas.drawBitmap(resizedHealth, width - 20 - 530, 35, null);
        canvas.drawBitmap(resizedScore, 400, 50, null);
        canvas.drawBitmap(resizedScorePlace, 400, 100, null);
        int number = points;
        int count = 0;
        while (number != 0) {
            number /= 10;
            if (number != 0) {
                ++count;
            }
        }
        canvas.drawText("" + points, 390 + (float) resizedScorePlace.getWidth() / 2 - count * 10, 115 + (float) resizedScorePlace.getHeight() / 2, scorePaint);
        for (int i=life; i>=1; i--) {
            if (i >= 9) {
                canvas.drawBitmap(resizedGreen, screenWidth - lifeImageGreen.getWidth() * (12 - i)- 55, 62, null);
            }
            else if (i >= 5) {
                canvas.drawBitmap(resizedYellow, screenWidth - lifeImageYellow.getWidth() * (12 - i)- 55, 62, null);
            }
            else {
                canvas.drawBitmap(resizedRed, screenWidth - lifeImageRed.getWidth() * (12 - i)- 55, 62, null);
            }
        }
        if (life == 0){
            paused = true;
            handler = null;
            @SuppressLint("DrawAllocation") Intent intent = new Intent(context, GameOver.class);
            intent.putExtra("points", points);
            context.startActivity(intent);
            ((Activity) context).finish();
        }

        if (!isAid) {
            int randomNumber = random.nextInt(8000);
            if (randomNumber <= 3) {
                randomNumber = random.nextInt(width);
                if (randomNumber < 20) randomNumber = 20;
                else if (randomNumber > width - 20) randomNumber = width - 20;
                largeAid = new LargeAid(context, randomNumber, 250);
                largeAids.add(largeAid);
                isAid = true;
            }
            else if (randomNumber <= 10) {
                randomNumber = random.nextInt(width);
                if (randomNumber < 20) randomNumber = 20;
                else if (randomNumber > width - 20) randomNumber = width - 20;
                smallAid = new SmallAid(context, randomNumber, 250);
                smallAids.add(smallAid);
                isAid = true;
            }
        }
        enemyPlane.ex += enemyPlane.enemyVelocity;
        if (enemyPlane.ex + enemyPlane.getEnemyPlaneWidth() >= screenWidth) {
            enemyPlane.enemyVelocity *= -1;
        }

        if (enemyPlane.ex <= 0) {
            enemyPlane.enemyVelocity *= -1;
        }

        if (!enemyShotAction) {
            if (enemyPlane.ex >= 200 + random.nextInt(400)) {
                @SuppressLint("DrawAllocation") EnemyShot enemyShot = new EnemyShot(context, enemyPlane.ex + enemyPlane.getEnemyPlaneWidth() / 2, enemyPlane.ey);
                enemyShots.add(enemyShot);
                enemyShotAction = true;
            }

            else if (enemyPlane.ex >= 400 + random.nextInt(800)) {
                @SuppressLint("DrawAllocation") EnemyShot enemyShot = new EnemyShot(context, enemyPlane.ex + enemyPlane.getEnemyPlaneWidth() / 2, enemyPlane.ey);
                enemyShots.add(enemyShot);
                enemyShotAction = true;
            }

            else {
                @SuppressLint("DrawAllocation") EnemyShot enemyShot = new EnemyShot(context, enemyPlane.ex + enemyPlane.getEnemyPlaneWidth() / 2, enemyPlane.ey);
                enemyShots.add(enemyShot);
                enemyShotAction = true;
            }
        }

        canvas.drawBitmap(enemyPlane.getEnemyPlane(), enemyPlane.ex, enemyPlane.ey, null);

        if(ourPlane.ox > screenWidth - ourPlane.getOurPlaneWidth()) {
            ourPlane.ox = screenWidth - ourPlane.getOurPlaneWidth();
        }

        else if (ourPlane.ox < 0) {
            ourPlane.ox = 0;
        }

        canvas.drawBitmap(ourPlane.getOurPlane(), ourPlane.ox, ourPlane.oy, null);

        for (int i=0; i < smallAids.size(); i++) {
            smallAids.get(i).say += 10;
            canvas.drawBitmap(smallAids.get(i).getShot(), smallAids.get(i).sax, smallAids.get(i).say, null);
            if ((smallAids.get(i).sax >= ourPlane.ox) && smallAids.get(i).sax <= ourPlane.ox + ourPlane.getOurPlaneWidth() && smallAids.get(i).say >= ourPlane.oy && smallAids.get(i).say <= screenHeight) {
                if (life >= 10) {
                    life = 12;
                }
                else {
                    life += 2;
                }
                smallAids.remove(i);
            }
            else if (smallAids.get(i).say >= screenHeight) {
                smallAids.remove(i);
            }

            if(smallAids.size() < 1) {
                isAid = false;
            }
        }

        for (int i=0; i < largeAids.size(); i++) {
            largeAids.get(i).say += 10;
            canvas.drawBitmap(largeAids.get(i).getShot(), largeAids.get(i).sax, largeAids.get(i).say, null);
            if ((largeAids.get(i).sax >= ourPlane.ox) && largeAids.get(i).sax <= ourPlane.ox + ourPlane.getOurPlaneWidth() && largeAids.get(i).say >= ourPlane.oy && largeAids.get(i).say <= screenHeight) {
                life = 12;
                largeAids.remove(i);
            }
            else if (largeAids.get(i).say >= screenHeight) {
                largeAids.remove(i);
            }

            if(largeAids.size() < 1) {
                isAid = false;
            }
        }

        for (int i=0; i < enemyShots.size(); i++) {
            enemyShots.get(i).shy += 30;
            canvas.drawBitmap(enemyShots.get(i).getShot(), enemyShots.get(i).shx, enemyShots.get(i).shy, null);
            if ((enemyShots.get(i).shx >= ourPlane.ox) && enemyShots.get(i).shx <= ourPlane.ox + ourPlane.getOurPlaneWidth() && enemyShots.get(i).shy >= ourPlane.oy && enemyShots.get(i).shy <= screenHeight) {
                if (enemyShotAction) {
                    life--;
                    enemyShots.remove(i);
                }
            }
            else if (enemyShots.get(i).shy >= screenHeight) {
                enemyShots.remove(i);
            }
            if(enemyShots.size() < 1) {
                enemyShotAction = false;
            }
        }

        for (int i = 0; i < ourShots.size(); i++) {
            ourShots.get(i).shy -= 30;
            canvas.drawBitmap(ourShots.get(i).getShot(), ourShots.get(i).shx, ourShots.get(i).shy, null);
            if ((ourShots.get(i).shx >= enemyPlane.ex) && ourShots.get(i).shx <= enemyPlane.ex + enemyPlane.getEnemyPlaneWidth() && ourShots.get(i).shy <= enemyPlane.getEnemyPlaneWidth() && ourShots.get(i).shy <= enemyPlane.ey) {
                points += 10;
                ourShots.remove(i);
            }
            else if (ourShots.get(i).shy <= 220) {
                ourShots.remove(i);
            }
        }

        if (!paused) {
            handler.postDelayed(runnable, UPDATE_MILLIS);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int)event.getX();
        int touchY = (int)event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (ourShots.size() < 1) {
                Shot ourShot = new Shot(context, ourPlane.ox + ourPlane.getOurPlaneWidth() / 2, ourPlane.oy);
                ourShots.add(ourShot);
            }
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (20 <= touchX && touchX <= 170 && 35 <= touchY && touchY <= 155) {
                paused = true;
                handler = null;
                @SuppressLint("DrawAllocation") Intent intent = new Intent(context, StartUp.class);
                intent.putExtra("points", points);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
            ourPlane.ox = touchX;
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            ourPlane.ox = touchX;
        }
        return true;
    }
}
