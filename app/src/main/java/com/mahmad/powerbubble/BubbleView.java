package com.mahmad.powerbubble;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Custom View for the floating bubble.
 */
public class BubbleView extends View {
    private Paint paint;
    private float lastX, lastY;
    private boolean dragging = false;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private Handler handler = new Handler();
    private Runnable fadeRunnable;
    private static final int FADE_DELAY = 2500; // ms
    private float downX, downY;
    private static final int TAP_THRESHOLD = 20; // px

    public BubbleView(Context context) {
        super(context);
        init(context);
    }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        // Bubble transparency is set here (255 = opaque, lower = more transparent)
        paint.setAlpha(255);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int radius = Math.min(getWidth(), getHeight()) / 2;
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = 110; // px
        setMeasuredDimension(size, size);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dragging = true;
                lastX = event.getRawX();
                lastY = event.getRawY();
                downX = lastX;
                downY = lastY;
                setAlpha(1f);
                handler.removeCallbacks(fadeRunnable);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (dragging) {
                    float dx = event.getRawX() - lastX;
                    float dy = event.getRawY() - lastY;
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                    if (params == null) {
                        params = (WindowManager.LayoutParams) getLayoutParams();
                    }
                    params.x += (int) dx;
                    params.y += (int) dy;
                    windowManager.updateViewLayout(this, params);
                }
                return true;
            case MotionEvent.ACTION_UP:
                dragging = false;
                startFadeTimer();
                float upX = event.getRawX();
                float upY = event.getRawY();
                float dist = (float) Math.hypot(upX - downX, upY - downY);
                if (dist < TAP_THRESHOLD) {
                    performClick();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void startFadeTimer() {
        if (fadeRunnable != null) handler.removeCallbacks(fadeRunnable);
        fadeRunnable = new Runnable() {
            @Override
            public void run() {
                setAlpha(0.4f);
            }
        };
        handler.postDelayed(fadeRunnable, FADE_DELAY);
    }

    private void showPopupMenu() {
        PopupMenuView.show(getContext(), windowManager, params);
        setAlpha(1f);
        startFadeTimer();
    }
}
