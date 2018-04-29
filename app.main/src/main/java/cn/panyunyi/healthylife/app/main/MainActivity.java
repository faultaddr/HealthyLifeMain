package cn.panyunyi.healthylife.app.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ecloud.pulltozoomview.PullToZoomListViewEx;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.panyunyi.healthylife.app.main.event.MessageEvent;
import cn.panyunyi.healthylife.app.main.ui.activity.DetailActivity;
import cn.panyunyi.healthylife.app.main.ui.activity.MineActivity;
import cn.panyunyi.healthylife.app.main.ui.activity.MonitorActivity;
import cn.panyunyi.healthylife.app.main.ui.adapter.MainListAdapter;
import cn.panyunyi.healthylife.app.main.ui.custom.RadarView;


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
    private MainListAdapter adapter;
    public String beats = "";
    public String steps = "";
    private final int REQUEST_CALL_CAMERA = 0;


    public MainActivity() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            beats = data.getStringExtra("beats");
            adapter.notifyDataSetChanged();
        }
        Log.i(TAG, beats + "");


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        initAdapter();
        initMainView();
        initOtherView();

    }

    private void initAdapter() {
        adapter = new MainListAdapter(this);
        adapter.setBeats(beats);
        adapter.setSteps(steps);
        adapter.notifyDataSetChanged();
        adapter.setOnClickItemView(new MainListAdapter.onClickItemView() {
            @Override
            public void itemViewClicked(int id) {
                //TODO item 的点击事件分发到这里
                switch (id) {
                    case 1:

                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
            }
        });
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
        adapter = new MainListAdapter(MainActivity.this);
        pullToZoomListViewEx.setAdapter(adapter);


        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        pullToZoomListViewEx.setHeaderLayoutParams(localObject);
        pullToZoomListViewEx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "list item " + i + " was clicked");
                if (requestPermission() == 1) {
                    startMonitorActivity();
                }
            }
        });
        adapter.setOnClickItemView(new MainListAdapter.onClickItemView() {
            @Override
            public void itemViewClicked(int id) {
                if (requestPermission() == 1) {
                    startMonitorActivity();
                }
            }
        });

    }

    /*
     * start monitorActivity
     * */
    public void startMonitorActivity() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, MonitorActivity.class);
        startActivityForResult(intent, 0);
    }

    public void startMineActivity() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, MineActivity.class);
        startActivity(intent);
    }

    /**
     * 注册权限申请回调
     *
     * @param requestCode  申请码
     * @param permissions  申请的权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startMonitorActivity();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "CALL_PHONE Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }

    }

    /*
     * android 6.0+
     * 相机权限需要动态获取
     *
     *
     *
     * */
    private int requestPermission() {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CALL_CAMERA);
                return 0;
            } else {
                //已有权限
                return 1;
            }

        } else {
            //API 版本在23以下
            return 1;
        }

    }

    /**
     * 粘性事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onDataSynEvent(MessageEvent event) {
        switch (event.getMessageType()) {
            //步数计数器
            case Constant.stepsUpdate:
                steps = event.getMessageContent();
                Log.i(TAG, "step count is" + steps);
                adapter.setSteps(steps);
                adapter.notifyDataSetChanged();
                break;
            case Constant.beatsUpdate:
                beats = event.getMessageContent();
                Log.i(TAG, "beats is" + beats);
                adapter.setBeats(beats);
                adapter.notifyDataSetChanged();

                break;

        }
    }


    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_page:
                /*
                 * 跳转到主页面
                 * */
                break;
            case R.id.sports:
                /*
                 * 跳转到运动页面
                 * */
                Intent intent = new Intent();
                intent.setClass(this, DetailActivity.class);
                startActivity(intent);
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
                startMineActivity();
                break;
        }
    }


}