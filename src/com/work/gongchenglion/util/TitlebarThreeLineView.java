package com.work.gongchenglion.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 标题栏左边点的三条线
 * <p/>
 * Created by wom
 */
public class TitlebarThreeLineView extends View {

    private Paint paint;

    public TitlebarThreeLineView(Context context) {
        this(context, null);
    }

    public TitlebarThreeLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(ScreenHelper.dp2px(getContext(), 3));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0, getHeight() / 2);

        canvas.drawLine(0, 0, getWidth(), 0, paint);//中间的线

        int d = ScreenHelper.dp2px(getContext(), 8);
        canvas.drawLine(0, -d, getWidth(), -d, paint);
        canvas.drawLine(0, d, getWidth(), d, paint);

    }

//	@Override
//	public void setOnClickListener(OnClickListener l) {
//		super.setOnClickListener(l);
//	}
    
}
