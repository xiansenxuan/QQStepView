package com.xuan.qqstepview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.DecelerateInterpolator;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final QQStepView step_view=findViewById(R.id.step_view);

        //属性动画 使圆弧转起来
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, step_view.getStepNumber());
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float stepNumber= (float) animation.getAnimatedValue();
                step_view.startInvalidate((int) stepNumber);
            }
        });
        valueAnimator.start();
    }
}
