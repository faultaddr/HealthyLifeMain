package cn.panyunyi.healthylife.app.server;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.ecloud.pulltozoomview.PullToZoomListViewEx;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.panyunyi.healthylife.app.main.MyEventBusIndex;
import cn.panyunyi.healthylife.app.server.event.MessageEvent;
import cn.panyunyi.healthylife.app.server.ui.activity.MonitorActivity;
import cn.panyunyi.healthylife.app.server.ui.adapter.MainListAdapter;
import cn.panyunyi.healthylife.app.server.ui.custom.RadarView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public String TAG = "MainActivity";



    @BindView(R.id.main_pic)
    PullToZoomListViewEx pullToZoomListViewEx;
    @BindView(R.id.mine)
    LinearLayout mMine;
    @BindView(R.id.find)
    LinearLayout mFind;
    @BindView(R.id.sports)
    LinearLayout mSports;
    @BindView(R.id.main_page)
    LinearLayout mMainPage;



    private EventBus eventBus = null;


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

        initMainView();
        initOtherView();

    }

    private void initOtherView() {
        mFind.setOnClickListener(this);
        mMainPage.setOnClickListener(this);
        mMine.setOnClickListener(this);
        mSports.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initMainView() {
        RadarView.ViewConfig config = new RadarView.ViewConfig();
        RadarView radarView =
                config.startColor(Color.parseColor("#fffff1"))
                        .endColor(Color.parseColor("#c2ffec"))
                        .circleCount(1)
                        .lineColor(Color.parseColor("#c7ffec"))
                        .bgPic(R.drawable.main_pic)
                        .config(this);

        radarView.startScan();

        pullToZoomListViewEx.setZoomView(radarView);

        Window window = this.getWindow();
        window.setStatusBarColor(Color.BLACK);
        pullToZoomListViewEx.setAdapter(new MainListAdapter(MainActivity.this));
        pullToZoomListViewEx.getPullRootView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "position = " + position);
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
                Log.i(TAG,"list item "+i+" was clicked");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, MonitorActivity.class);
                    startActivity(intent);
            }
        });
    }

    /**
     * 粘性事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onDataSynEvent(MessageEvent event) {
        switch (event.getMessageType()){
            case 0:
                count= Integer.parseInt(event.getMessageContent());
                break;
        }
    }

    public static int count = 0;




    protected void onResume() {
        super.onResume();

    }

    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_page:
                /*
                * 跳转到主页面
                * */
                break;
            case R.id.sports:
                /*
                * 跳转到运动页面
                * */
                break;
            case R.id.find:
                /*
                *
                * 推荐相关信息
                * */
                break;
            case R.id.mine:
                /*
                * 我的设置等相关
                * */
                break;
        }
    }
}