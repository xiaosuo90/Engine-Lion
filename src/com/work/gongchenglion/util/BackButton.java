package com.work.gongchenglion.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * 返回按钮
 * <p/>
 * Created by apple on 14-9-20.
 */
public class BackButton extends Button {

    private boolean touch = false;

    public BackButton(Context context) {
        this(context, null);
    }

    public BackButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setBackgroundColor(Color.alpha(0));
        setPadding(ScreenHelper.dp2px(getContext(), 50), 0, 0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch = true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (touch && (event.getX() > getWidth() || event.getY() > getHeight())) {
                    touch = false;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touch = false;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = ScreenHelper.dp2px(getContext(), 1.5f);
        int height = ScreenHelper.dp2px(getContext(), 12);
        Paint paint = new Paint();
        if (!touch)
            paint.setColor(Color.WHITE);
        else
            paint.setColor(Color.rgb(0xbb, 0xbb, 0xbb));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(width);
        paint.setAntiAlias(true);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        float a = (float) (width / Math.sqrt(2)) / 2;
        canvas.drawLine(-height - a, -a, 0, height, paint);
        canvas.drawLine(-height - a, a, 0, -height, paint);
    }
}