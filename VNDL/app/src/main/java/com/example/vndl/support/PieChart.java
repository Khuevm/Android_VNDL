package com.example.vndl.support;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PieChart extends View {
    private Paint mPaint;
    private RectF mRectF;

    private int lineWidth = 0;
    private float correctProgress = 0;
    private float wrongProgress = 0;

    private int wrongColor;
    private int correctColor;
    private int backgroundColor;

    //Constructor được sử dụng khi add view lúc runtime.
    public PieChart(Context context) {
        super(context);
        init();
    }

    //Constructor được gọi khi khai báo view trong XML
    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setColor(int backgroundColor, int correctColor, int wrongColor) {
        this.backgroundColor = backgroundColor;
        this.correctColor = correctColor;
        this.wrongColor = wrongColor;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setProgress(float correctProgress, float wrongProgress) {
        this.correctProgress = correctProgress;
        this.wrongProgress = wrongProgress;
    }

    private void init() {
        mPaint = new Paint();
        mRectF = new RectF();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(lineWidth);

        float startAngle = -90;
        float sweepAngleCorrect = correctProgress*360;
        float sweepAngleWrong =  (wrongProgress + correctProgress)*360;

        // Draw Background
        mPaint.setColor(backgroundColor);
        canvas.drawArc(mRectF, startAngle, 360, false, mPaint);

        //Wrong Process
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(wrongColor);
        canvas.drawArc(mRectF, startAngle, sweepAngleWrong, false, mPaint);

        //Correct Process
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(correctColor);
        canvas.drawArc(mRectF, startAngle, sweepAngleCorrect, false, mPaint);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.left = getPaddingLeft()+lineWidth/2;
        mRectF.top = getPaddingTop()+lineWidth/2;
        mRectF.right = getWidth() - getPaddingRight()-lineWidth/2;
        mRectF.bottom = getHeight() - getPaddingBottom()-lineWidth/2;
    }
}
