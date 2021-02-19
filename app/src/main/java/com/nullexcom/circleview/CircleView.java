package com.nullexcom.circleview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Random;

public class CircleView extends View {

    private int mStrokeWidth = 50;
    private Paint mPaint = new Paint();

    private int angle = 330;
    private ValueAnimator mAnimator;
    private boolean isInScreen = false;

    private int[] colors = new int[] {
            Color.RED,
            Color.parseColor("#4A90E2"),
            Color.parseColor("#40C4FF"),
            Color.parseColor("#0094CC")
    };


    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mStrokeWidth = dp(15);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.RED);
        mPaint.setDither(true);

        mAnimator = ValueAnimator.ofFloat(0, 100);
        mAnimator.setRepeatCount(0);
        mAnimator.setDuration(1000);
        mAnimator.setInterpolator(new OvershootInterpolator());

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] location = new int[2];
                getLocationOnScreen(location);
                boolean isShow = isShow(location);
                if (!isShow) {
                    isInScreen = false;
                    return;
                }
                if (isInScreen) {
                    return;
                }
                isInScreen = isShow(location);
                startAnimation();
            }
        });
    }

    private boolean isShow(int[] location) {
        Context context = getContext();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        if (location[0] <= 0 || location[0] >= screenWidth) return false;
        return location[1] > 0 && location[1] < screenHeight;
    }

    private int dp(int dp) {
        Context context = getContext();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(dp(100), dp(100));
            return;
        }
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        int size = Math.min(w, h);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float currentValue = (float) mAnimator.getAnimatedValue();
        float a = angle * currentValue / 100f;
        int alpha = Math.min((int) (50 + currentValue * 255 / 100), 255);
        mPaint.setAlpha(alpha);

        int strokeWidth = (int) (mStrokeWidth * currentValue / 100);
        mPaint.setStrokeWidth(strokeWidth);
        int half = strokeWidth / 2;
        int w = getWidth();
        int h = getHeight();

        if (angle <= 90) {
            mPaint.setColor(colors[0]);
        } else if (angle <= 180) {
            mPaint.setColor(colors[1]);
        } else if (angle <= 270) {
            mPaint.setColor(colors[2]);
        } else {
            mPaint.setColor(colors[3]);
        }


        canvas.drawArc(half, half, w - half, h - half, -90, -a, false, mPaint);
        invalidate();
    }

    public void startAnimation() {
        if (getVisibility() != VISIBLE) {
            return;
        }
        if (mAnimator == null) {
            return;
        }
        mAnimator.cancel();
        mAnimator.start();
    }

    public void setAngle(int angle) {
        if (angle == 360) {
            this.angle = 330;
        } else {
            this.angle = angle;
        }
        startAnimation();
    }
}
