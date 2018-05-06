package cn.panyunyi.healthylife.app.main;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.panyunyi.healthylife.app.main.ui.custom.RadarView;

public class TestActivity extends AppCompatActivity implements SensorEventListener {
    private static final int sensorTypeD = Sensor.TYPE_STEP_DETECTOR;
    private static final int sensorTypeC = Sensor.TYPE_STEP_COUNTER;
    /*    @BindView(R.id.text)
        TextView textView;
        @BindView(R.id.press)
        Button button;*/
    @BindView(R.id.gradientDemo)
    RadarView gardientDemo;
    @BindView(R.id.text)
    TextView textView;
    private SensorManager mSensorManager;
    private Sensor mStepDetector;

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
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mStepDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mStepDetector, SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void btnOpen(View view) {
        gardientDemo.startScan();
    }

    public void btnClose(View view) {
        gardientDemo.stopScan();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case sensorTypeC: {
                Toast.makeText(TestActivity.this, "步数累计：" + sensorEvent.values[0] + "》》》", Toast.LENGTH_SHORT).show();
            }
            break;
            case sensorTypeD: {
                Toast.makeText(TestActivity.this, "步数累加：" + sensorEvent.values[0] + "》》》", Toast.LENGTH_SHORT).show();
            }
        }
        //Toast.makeText(TestActivity.this, "步数：" + sensorEvent.values[0] + "》》》", Toast.LENGTH_SHORT).show();
        //textView.setText(sensorEvent.values[0]+"步");
        String s = "";
        for (int i = 0; i < sensorEvent.values.length; i++) {
            s = s + "" + sensorEvent.values[i] + "/";
        }
        textView.setText((int) sensorEvent.values[1] + "  步");

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
