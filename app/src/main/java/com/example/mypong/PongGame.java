package com.example.mypong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PongGame extends SurfaceView implements Runnable {
    //Attribute
    // Are we debugging?
    private final boolean DEBUGGING = true;
    // These objects are needed to do the drawing
    private final SurfaceHolder mOurHolder;
    private final Paint mPaint;
    // The number of milliseconds in a second
    private final int MILLIS_IN_SECOND = 1000;
    // Holds the resolution of the screen
    private final int mScreenX;
    private final int mScreenY;
    // How big will the text be?
    private final int mFontSize;
    private final int mFontMargin;
    // The game objects
    private final Bat mBat;
    private final Ball mBall;
    private Canvas mCanvas;
    // How many frames per second did we get?
    private long mFPS;
    // The current score and lives remaining
    private int mScore;
    private int mLives;

    // Here is the Thread and two control variables
    private Thread mGameThread = null;

    // This volatile variable can be accessed from inside and outside the thread
    private volatile boolean mPlaying;
    private boolean mPaused = true;

    //constructor

    public PongGame(Context context, int x, int y) {

        super(context);

        mScreenX = x;
        mScreenY = y;

        mFontSize = mScreenX / 20;

        mFontMargin = mScreenX / 75;

        mOurHolder = getHolder();
        mPaint = new Paint();

        mBall = new Ball(mScreenX);
        mBat = new Bat(mScreenX, mScreenY);

        startNewGame();


    }
     //method
     private void startNewGame() {
         mScore = 0;
         mLives = 3;
         mBall.reset(mScreenX, mScreenY);
    }

     private void draw() {

             if (mOurHolder.getSurface().isValid()) {
                 mCanvas = mOurHolder.lockCanvas(); // Lock the canvas (graphics memory)
                 mCanvas.drawColor(Color.argb(255, 26, 128, 182));
                 mPaint.setColor(Color.argb(255, 255, 255, 255));
                 mPaint.setTextSize(mFontSize);
                 mCanvas.drawRect(mBall.getRect(), mPaint);
                 mCanvas.drawRect(mBat.getRect(), mPaint);
                 mCanvas.drawText("Score: " + mScore + " Lives: " + mLives,

                         mFontMargin, mFontSize, mPaint);


                 if (DEBUGGING) {
                     printDebuggingText();
                 }
                 mOurHolder.unlockCanvasAndPost(mCanvas);

             }

         }


     private void printDebuggingText() {

         int debugSize = mFontSize / 2;
         int debugStart = 150;
         mPaint.setTextSize(debugSize);
         mCanvas.drawText("FPS: " + mFPS , 10, debugStart + debugSize, mPaint);
     }

     @Override
     public void run() {
         while (mPlaying) {
             long frameStartTime = System.currentTimeMillis();
             if(!mPaused){
                 update(); // update new positions
                 detectCollisions(); // detect collisions
             }
            //draw the scene
             draw();
            // How long did this frame/loop take?
             long timeThisFrame = System.currentTimeMillis() - frameStartTime;
            // check timeThisFrame > 0 ms because dividing by 0 will crashes game
             if (timeThisFrame > 0) {
            // Store frame rate to pass to the update methods of mBat and mBall
                 mFPS = MILLIS_IN_SECOND / timeThisFrame;
             }
         }

     }

    private void update() {
        mBall.update(mFPS);

        mBat.update(mFPS);
    }

    private void detectCollisions() {
        if (RectF.intersects(mBat.getRect(), mBall.getRect())) {

            mBall.batBounce(mBat.getRect());
            mBall.increaseVelocity();
            mScore++;

        }
        // Bottom
        if (mBall.getRect().bottom > mScreenY) {
            mBall.reverseYVelocity();
            mLives--;
            if (mLives == 0) {
                mPaused = true;
                startNewGame();
            }
        }
        // Top
        if (mBall.getRect().top < 0) {
            mBall.reverseYVelocity();
        }
        // Left
        if (mBall.getRect().left < 0) {
            mBall.reverseXVelocity();
        }
        // Right
        if (mBall.getRect().right > mScreenX) {
            mBall.reverseXVelocity();
        }
    }
     public void pause() {

         mPlaying = false;
         try {

             mGameThread.join();

         } catch (InterruptedException e) {

             Log.e("Error:", "joining thread");

         }

     }

    public void resume() {
        mPlaying = true;
        // Initialize the instance of Thread
        mGameThread = new Thread(this);
        // Start the thread
        mGameThread.start();

    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:

                mPaused = false;

                if (motionEvent.getX() > mScreenX / 2) {

                    mBat.setMovementState(mBat.RIGHT);
                } else {

                    mBat.setMovementState
                            (mBat.LEFT);
                }
                break;

            case MotionEvent.ACTION_UP:

                mBat.setMovementState(mBat.STOPPED);
                break;

        }
        return true;
    }
}
