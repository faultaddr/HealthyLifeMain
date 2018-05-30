package cn.panyunyi.healthylife.app.main.application;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.util.Log;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.panyunyi.healthylife.app.main.Constant;
import cn.panyunyi.healthylife.app.main.MyEventBusIndex;
import cn.panyunyi.healthylife.app.main.biz.local.service.StepService;
import cn.panyunyi.healthylife.app.main.util.Tools;


public class Application extends android.app.Application {

    public final static String TAG = "Application";
    private static Application application;
    public static List<ApplicationInfo>mApplicationInfos;
    @Override
    public void onCreate() {
        Bugly.init(getApplicationContext(), "e53a111699", false);
        /*CrashReport.initCrashReport(getApplicationContext(), "222e7759a9", false);*/
        Context context = getApplicationContext();
// 获取当前包名
        String packageName = context.getPackageName();
// 获取当前进程名
        String processName = Tools.getProcessName(android.os.Process.myPid());
        Log.i(">>>processName",processName);
// 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
// 初始化Bugly
        CrashReport.initCrashReport(context, "e53a111699", true/*isdebug*/, strategy);
        CrashReport.startCrashReport();
// 如果通过“AndroidManifest.xml”来配置APP信息，初始化方法如下
// CrashReport.initCrashReport(context, strategy);
        Constant constant=new Constant(Application.this);
        constant.setConstants();
        queryFilterAppInfo();
        for(ApplicationInfo info:mApplicationInfos){
            Log.i(">>>",info.processName);
        }

        super.onCreate();
        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
        application = this;
        constant = new Constant(Application.this);
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

    private void queryFilterAppInfo() {
        PackageManager pm = this.getPackageManager();
        // 查询所有已经安装的应用程序
        List<ApplicationInfo> appInfos= pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);// GET_UNINSTALLED_PACKAGES代表已删除，但还有安装目录的
        List<ApplicationInfo> applicationInfos=new ArrayList<>();

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        // 通过getPackageManager()的queryIntentActivities方法遍历,得到所有能打开的app的packageName
        List<ResolveInfo>  resolveinfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        Set<String> allowPackages=new HashSet();
        for (ResolveInfo resolveInfo:resolveinfoList){
            allowPackages.add(resolveInfo.activityInfo.packageName);
        }

        for (ApplicationInfo app:appInfos) {
//            if((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0)//通过flag排除系统应用，会将电话、短信也排除掉
//            {
//                applicationInfos.add(app);
//            }
//            if(app.uid > 10000){//通过uid排除系统应用，在一些手机上效果不好
//                applicationInfos.add(app);
//            }
            if (allowPackages.contains(app.packageName)){
                applicationInfos.add(app);
            }
        }
        mApplicationInfos=applicationInfos;
    }

}
