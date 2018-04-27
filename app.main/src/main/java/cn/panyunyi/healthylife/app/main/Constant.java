package cn.panyunyi.healthylife.app.main;

import android.content.Context;

import cn.panyunyi.healthylife.app.main.util.MyProperUtil;
import okhttp3.MediaType;

public class Constant {
    public static Context mContext;
    public static String API_URL;
    public static final MediaType JSONs
            = MediaType.parse("application/json; charset=utf-8");

    public Constant(Context context) {
        mContext=context;
    }
    public void setConstants(){
        API_URL= MyProperUtil.getProperties(mContext).getProperty("base_url");
    }
    public static final int MSG_FROM_CLIENT = 0;
    public static final int MSG_FROM_SERVER =1 ;


}
