package cn.panyunyi.healthylife.app.server.application;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import cn.panyunyi.healthylife.app.server.biz.local.service.StepService;

public class Application extends android.app.Application {
    public final static String TAG = "Application";
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
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
