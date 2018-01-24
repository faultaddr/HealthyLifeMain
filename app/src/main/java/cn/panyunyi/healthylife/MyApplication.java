package cn.panyunyi.healthylife;

import android.app.Application;

import net.wequick.small.Small;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Small.preSetUp(this);
    }
}