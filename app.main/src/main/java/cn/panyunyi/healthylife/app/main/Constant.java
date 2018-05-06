package cn.panyunyi.healthylife.app.main;

import android.content.Context;

import cn.panyunyi.healthylife.app.main.util.MyProperUtil;
import okhttp3.MediaType;

public class Constant {
    public static final MediaType JSONs
            = MediaType.parse("application/json; charset=utf-8");
    public static final int MSG_FROM_CLIENT = 0;
    public static final int MSG_FROM_SERVER = 1;
    public static final String stepsUpdate = "stepsUpdate";
    public static final String beatsUpdate = "beatsUpdate";
    public static final String loginStatus = "loginStatus";
    public static Context mContext;
    public static String API_URL;

    public Constant(Context context) {
        mContext = context;
    }

    public void setConstants() {
        API_URL = MyProperUtil.getProperties(mContext).getProperty("base_url");
    }


}
