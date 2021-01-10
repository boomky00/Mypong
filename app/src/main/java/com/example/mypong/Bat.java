package com.example.mypong;
import android.graphics.RectF;

public class Bat {
    final int STOPPED = 0;
    final int LEFT = 1;
    final int RIGHT = 2;
    private final RectF mRect;
    private final float mLength;
    private final float mBatSpeed;
    private final int mScreenX;
    private float mXCoord;
    private int mBatMoving = STOPPED;

    Bat(int sx, int sy) {
        mScreenX = sx;
        mLength = mScreenX / 8;
        float height = sy / 40;
        mXCoord = mScreenX / 2;
        float mYCoord = sy - height;
        mRect = new RectF(mXCoord, mYCoord,
                mXCoord + mLength,
                mYCoord + height);
        mBatSpeed = mScreenX;
    }

    RectF getRect() {
        return mRect;

    }

    void setMovementState(int state) {

        mBatMoving = state;

    }


    void update(long fps) {
// Move the bat based on the mBatMoving variable and the speed of the previous frame
        if (mBatMoving == LEFT) {
            mXCoord = mXCoord - mBatSpeed / fps;
        }
        if (mBatMoving == RIGHT) {
            mXCoord = mXCoord + mBatSpeed / fps;
        }
// Stop the bat going off the screen
        if (mXCoord < 0) {
            mXCoord = 0;
        } else if (mXCoord + mLength > mScreenX) {
            mXCoord = mScreenX - mLength;
        }
// Update mRect based on the results from the previous code in update
        mRect.left = mXCoord;
        mRect.right = mXCoord + mLength;
    }

}
