package cn.panyunyi.healthylife.app.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ecloud.pulltozoomview.PullToZoomListViewEx;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.panyunyi.healthylife.app.main.ui.custom.RadarView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final int sensorTypeD = Sensor.TYPE_STEP_DETECTOR;
    private static final int sensorTypeC = Sensor.TYPE_STEP_COUNTER;


    @BindView(R.id.main_pic)
    PullToZoomListViewEx pullToZoomListViewEx;

    private EventBus eventBus = null;
    private SensorManager mSensorManager;
    private Sensor mStepDetector;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        /*
        * EventBus
        *
        * */
        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
// Now the default instance uses the given index. Use it like this:
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        helloEventBus("hhh");
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mStepDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);


/*
        MainPicView.Builder builder=new MainPicView.Builder();
        MainPicView mainPicView=builder.
                backgroundPic(R.drawable.main_activity_main_pic).
                plateRadius(200).
                plateWidth(60).
                stepCount(5000).
                build(this);
*/

        RadarView.ViewConfig config = new RadarView.ViewConfig();
        RadarView radarView =
                config.startColor(Color.parseColor("#fffff1"))
                        .endColor(Color.parseColor("#c2ffec"))
                        .circleCount(1)
                        .lineColor(Color.parseColor("#c7ffec"))
                        .bgPic(R.drawable.main)
                        .config(this);

        radarView.startScan();

        pullToZoomListViewEx.setZoomView(radarView);

        Window window=this.getWindow();
        window.setStatusBarColor(Color.BLACK);
        pullToZoomListViewEx.setAdapter(new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEnabled(int i) {
                return false;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                int s[]={R.string.click_to_find_more_interesting_things,R.string.heart_rate,R.string.blood_pressure,R.string.blood_oxygen};
                int d[]={R.drawable.lamp,R.drawable.heart,R.drawable.heart_pressure,R.drawable.oxygen};
                LayoutInflater inflater=LayoutInflater.from(MainActivity.this);
                view=inflater.inflate(R.layout.main_page_list_item,null);
                TextView textView=(TextView) view.findViewById(R.id.main_list_item_text);
                textView.setText(MainActivity.this.getText(s[i]));
                textView.setTextSize(20);
                Drawable drawable=MainActivity.this.getDrawable(d[i]);
                drawable.setBounds(20,0,60,40);
                textView.setCompoundDrawables(drawable,null,null,null);

                return view;
            }

            @Override
            public int getItemViewType(int i) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 4;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        });
        pullToZoomListViewEx.getPullRootView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("MainActivity>>>", "position = " + position);
            }
        });


        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        pullToZoomListViewEx.setHeaderLayoutParams(localObject);
        pullToZoomListViewEx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(String message) {

    }

    public static int count = 0;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        switch (sensorEvent.sensor.getType()) {
            case sensorTypeC: {
                ;
            }
            break;
            case sensorTypeD: {
                count += (int) sensorEvent.values[0];
            }
        }
        //Toast.makeText(TestActivity.this, "步数：" + sensorEvent.values[0] + "》》》", Toast.LENGTH_SHORT).show();
        //textView.setText(sensorEvent.values[0]+"步");


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mStepDetector, SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}