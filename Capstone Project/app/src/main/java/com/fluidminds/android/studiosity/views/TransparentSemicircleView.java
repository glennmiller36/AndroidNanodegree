package com.fluidminds.android.studiosity.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * A custom view that draws a semicircle with transparent fill.
 */
public class TransparentSemicircleView extends View {

    Bitmap mBitmap;
    Canvas mCanvas;
    Paint mEraser;
    int mBackgroundColor;

    public TransparentSemicircleView(Context context) {
        super(context);
        Init();
    }

    public TransparentSemicircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public TransparentSemicircleView(Context context, AttributeSet attrs,
                                     int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }

    public void setDrawBackgroundColor(int color) {
        mBackgroundColor = color;
    }

    private void Init(){

        mEraser = new Paint();
        mEraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mEraser.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        if (w != oldw || h != oldh) {
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int w = getWidth();
        int h = getHeight();
        int radius = w > h ? h / 2 : w / 2;

        mBitmap.eraseColor(Color.TRANSPARENT);
        mCanvas.drawColor(mBackgroundColor);
        mCanvas.drawCircle(0, h / 2, radius, mEraser);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        super.onDraw(canvas);
    }
}
