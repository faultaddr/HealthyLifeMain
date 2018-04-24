package panyunyi.cn.healthy_opencv;

import android.app.Application;

import net.wequick.small.Small;

public class SmallApp extends Application {

    public SmallApp() {
        Small.preSetUp(this);
    }

}