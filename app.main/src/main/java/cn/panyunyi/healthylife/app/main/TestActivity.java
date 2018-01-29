package cn.panyunyi.healthylife.app.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.panyunyi.healthylife.app.main.ui.custom.RadarView;

public class TestActivity extends AppCompatActivity {
/*    @BindView(R.id.text)
    TextView textView;
    @BindView(R.id.press)
    Button button;*/
@BindView(R.id.gradientDemo)
RadarView gardientDemo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        btnOpen(gardientDemo);

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

    public void btnOpen(View view) {
        gardientDemo.startScan();
    }

    public void btnClose(View view) {
        gardientDemo.stopScan();
    }
}
