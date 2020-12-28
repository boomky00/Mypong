package com.example.mypong;
import android.graphics.RectF;
public class Ball {

    //attribute
    private RectF mRect;
    private float mXVelocity;
    private float mYVelocity;
    private float mBallWidth;
    private float mBallHeight;

    Ball(int screenX){

        mBallWidth = screenX / 100;
        mBallHeight = screenX / 100;
        mRect = new RectF();
    }

    RectF getRect(){
        return mRect;

    }

    void update(long fps){
        // Move the top left corner
        mRect.left = mRect.left + (mXVelocity / fps);
        mRect.top = mRect.top + (mYVelocity / fps);

        // Match up the bottom right corner based on the size of the ball
        mRect.right = mRect.left + mBallWidth;
        mRect.bottom = mRect.top + mBallHeight;
    }

    // Reverse the vertical direction of travel
    void reverseYVelocity(){
        mYVelocity = -mYVelocity;

    }

    // Reverse the horizontal direction of travel
    void reverseXVelocity(){

        mXVelocity = -mXVelocity;
    }
    void reset(int x, int y){
        // Initialise the four points of the rectangle which defines the ball
        mRect.left = x / 2;
        mRect.top = 0;
        mRect.right = x / 2 + mBallWidth;
        mRect.bottom = mBallHeight;

        // How fast will the ball travel
        // You could even increase it as the game progresses to make it harder
        mYVelocity = -(y / 3);
        mXVelocity = (y / 3);
    }

    void increaseVelocity(){
        // increase the speed by 10%
        mXVelocity = mXVelocity * 1.1f;
        mYVelocity = mYVelocity * 1.1f;

    }

    void batBounce(RectF batPosition){
        float batCenter = batPosition.left + (batPosition.width() / 2);
        float ballCenter = mRect.left + (mBallWidth / 2);
        float relativeIntersect = (batCenter - ballCenter);
        if(relativeIntersect < 0){
            mXVelocity = Math.abs(mXVelocity);
        }else{
            mXVelocity = -Math.abs(mXVelocity);
        }
        reverseYVelocity();
    }
}
