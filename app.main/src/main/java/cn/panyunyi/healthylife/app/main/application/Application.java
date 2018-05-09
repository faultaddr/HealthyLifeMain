package cn.panyunyi.healthylife.app.main.application;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import cn.panyunyi.healthylife.app.main.Constant;
import cn.panyunyi.healthylife.app.main.MyEventBusIndex;
import cn.panyunyi.healthylife.app.main.biz.local.service.StepService;


public class Application extends android.app.Application {

    public final static String TAG = "Application";
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
        application = this;
        Constant constant = new Constant(Application.this);
        constant.setConstants();
        startStepService();
    }

    public void startStepService() {
        Intent intent = new Intent();
        intent.setAction("cn.panyunyi.StepService");
        intent.setClass(getApplicationContext(), StepService.class);
        startService(intent);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.i(TAG, "service connected");
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.i(TAG, "service disconnected");
            }
        }, 0);


    }

}
