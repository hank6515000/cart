package com.example.my_project_01;

import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.my_project_01.ui.notifications.BuyCartActivity;

import java.util.Timer;
import java.util.TimerTask;

public class FloatingWindow extends FrameLayout {

    private Bundle bundle;

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public FloatingWindow(@NonNull Context context) {
        super(context);
        init();
    }

    public FloatingWindow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatingWindow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        final Context context = getContext();

        ImageView view = new ImageView(context);
        view.setImageResource(R.drawable.game_circle);
        LayoutParams lp = new LayoutParams(dp2px(context, 64), dp2px(context, 64));
        lp.topMargin = dp2px(context, 580);
        lp.rightMargin = dp2px(context, 10);
        lp.gravity = Gravity.END;
        addView(view, lp);

        view.setOnTouchListener(new OnTouchListener() {
            float translationX;
            float translationY;
            float downX;
            float downY;
            boolean move;
            int slop = ViewConfiguration.get(context).getScaledTouchSlop();
            long time1, time2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                if (action == MotionEvent.ACTION_DOWN) {
                    downX = event.getRawX();
                    downY = event.getRawY();
                    move = false;
                    time1 = System.currentTimeMillis();
                } else if (action == MotionEvent.ACTION_MOVE) {
                    float moveX = event.getRawX() - downX;
                    float moveY = event.getRawY() - downY;
                    v.setTranslationX(translationX + moveX);
                    v.setTranslationY(translationY + moveY);
                    if (moveX > slop && moveY > slop) {
                        move = true;
                    }
                } else if (action == MotionEvent.ACTION_UP) {
                    translationX = v.getTranslationX();
                    translationY = v.getTranslationY();
                    time2 = System.currentTimeMillis();
                    if ((time2 - time1) <= 70) {
                        Intent intent = new Intent(getContext(), GameRock.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                }
                return true;
            }
        });

        ImageView view2 = new ImageView(context);
        view2.setImageResource(R.drawable.shopping_circle);
        LayoutParams lp2 = new LayoutParams(dp2px(context, 64), dp2px(context, 64));
        lp2.topMargin = dp2px(context, 580);
        lp2.rightMargin = dp2px(context, 80);
        lp2.gravity = Gravity.END;
        addView(view2, lp2);

        view2.setOnTouchListener(new OnTouchListener() {
            float translationX;
            float translationY;
            float downX;
            float downY;
            boolean move;
            int slop = ViewConfiguration.get(context).getScaledTouchSlop();
            long time1, time2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                if (action == MotionEvent.ACTION_DOWN) {
                    downX = event.getRawX();
                    downY = event.getRawY();
                    move = false;
                    time1 = System.currentTimeMillis();
                } else if (action == MotionEvent.ACTION_MOVE) {
                    float moveX = event.getRawX() - downX;
                    float moveY = event.getRawY() - downY;
                    v.setTranslationX(translationX + moveX);
                    v.setTranslationY(translationY + moveY);
                    if (moveX > slop && moveY > slop) {
                        move = true;
                    }
                } else if (action == MotionEvent.ACTION_UP) {
                    translationX = v.getTranslationX();
                    translationY = v.getTranslationY();
                    time2 = System.currentTimeMillis();
                    if ((time2 - time1) <= 70) {
                        Intent intent = new Intent(getContext(), BuyCartActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                }
                return true;
            }
        });
    }

    private int dp2px(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}


