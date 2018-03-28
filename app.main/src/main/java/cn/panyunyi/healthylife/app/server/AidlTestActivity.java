package cn.panyunyi.healthylife.app.server;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AidlTestActivity extends AppCompatActivity {

    private InfoService mService;
    private TextView mLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_test);

        mLog = (TextView) findViewById(R.id.log);

        Intent serviceIntent = new Intent()
                .setComponent(new ComponentName(
                        "cn.panyunyi.healthylife.app.server",
                        "cn.panyunyi.healthylife.app.server.MainService"));
        mLog.setText("Starting service…\n");
        startService(serviceIntent);
        mLog.append("Binding service…\n");
        bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mLog.append("Service binded!\n");
            mService = InfoService.Stub.asInterface(service);

            performListing();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            // This method is only invoked when the service quits from the other end or gets killed
            // Invoking exit() from the AIDL interface makes the Service kill itself, thus invoking this.
            mLog.append("Service disconnected.\n");
        }
    };

    private void performListing() {
        mLog.append("Requesting file listing…\n");
        long start = System.currentTimeMillis();
        long end = 0;
        try {
            AppDetailInfo appDetailInfo = mService.getAppDetailInfo("abc");
            end = System.currentTimeMillis();
            int index = 0;

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mLog.append("File listing took " + (((double) end - (double) start) / 1000d) + " seconds, or " + (end - start) + " milliseconds.\n");
    }
}
