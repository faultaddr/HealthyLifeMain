package cn.panyunyi.healthylife.app.main;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {
/*    @BindView(R.id.text)
    TextView textView;
    @BindView(R.id.press)
    Button button;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

/*        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PropertyValuesHolder rotationHolder = PropertyValuesHolder.ofFloat("Rotation", 60f, -60f, 40f, -40f, -20f, 20f, 10f, -10f, 0f);
                PropertyValuesHolder colorHolder = PropertyValuesHolder.ofInt("BackgroundColor", 0xffffffff, 0xffff00ff, 0xffffff00, 0xffffffff);
                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(textView, rotationHolder, colorHolder);
                animator.setDuration(3000);
                animator.setInterpolator(new AccelerateInterpolator());
                animator.start();*/


       /*         ValueAnimator animator = ValueAnimator.ofInt(0,600);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curValue = (int)animation.getAnimatedValue();
                textView.layout(textView.getLeft(),curValue,textView.getRight(),curValue+textView.getHeight());
            }
        });

        animator.setDuration(1000);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();*/

/*
              }           
          });      */
    }
}
