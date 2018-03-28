package cn.panyunyi.healthylife.app.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;



/**
 * @author panyunyi
 */
public class MainService extends Service {

    public final static String TAG="MainService>>>";

    private void log(String message) {
        Log.v("MainService", message);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log("Received start command.");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        log("Received binding.");
        return mBinder;
    }

    private final InfoService.Stub mBinder = new InfoService.Stub() {
        @Override
        public AppDetailInfo getAppDetailInfo(String packageName) throws RemoteException {
            AppDetailInfo appDetailInfo=new AppDetailInfo("com.example.aaa");
            Log.v(TAG,"IBinder调用成功");
            return appDetailInfo;
        }


    };
}