package cn.panyunyi.healthylife.app.main.biz.remote.service;


import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import cn.panyunyi.healthylife.app.main.Constant;
import cn.panyunyi.healthylife.app.main.biz.remote.model.MUserEntity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by panyu on 2017/6/9.
 */

public class LoginImpl implements LoginManager {

    public static final MediaType JSONs
            = MediaType.parse("application/json; charset=utf-8");
    static OkHttpClient client =  new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build();  ;
    private static String TAG = "LoginImpl";
    String result;
    private MUserEntity muser;

    public LoginImpl(MUserEntity user) {

        muser = user;

    }

    @Override
    public String login() {
        MUserEntity loginedUser = new MUserEntity();
//        loginedUser.userId=muser.userId;
//        loginedUser.userAlipay=muser.userAlipay;
//        loginedUser.userClass=muser.userClass;
//        loginedUser.userGrade=muser.userGrade;
//        loginedUser.userInstitution=muser.userInstitution;


//        loginedUser.userRole=muser.userRole;
//        loginedUser.userTelephoneNumber=muser.userTelephoneNumber;
//        loginedUser.userWechat=muser.userWechat;
        //TODO 这里做访问网络的相关操作，进行登录验证，如果验证失败，吧上述属性都换成 游客 如果成功，存进LoginSession  这样可以随时存取。
        //首先创建执行线程的线程池,
        ExecutorService exs = Executors.newCachedThreadPool();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", muser.getUserName());
            jsonObject.put("UserPassword", muser.getUserPassword());
            Log.i(TAG, jsonObject.toString());
        } catch (JSONException ex) {
            ex.printStackTrace();

        }

        loginPost ct = new loginPost(Constant.API_URL + "/loginUser", jsonObject.toString());//实例化任务对象
        //大家对Future对象如果陌生，说明你用带返回值的线程用的比较少，要多加练习
        Future<Object> future = exs.submit(ct);//使用线程池对象执行任务并获取返回对象
        try {
            result = future.get().toString();//当调用了future的get方法获取返回的值得时候
            //如果线程没有计算完成，那么这里就会一直阻塞等待线程执行完成拿到返回值

            exs.shutdown();
            Log.i(">>>", result);
            loginedUser = JSON.parseObject(result, MUserEntity.class);
            LoginSession.getLoginSession().setsLoginSession(loginedUser);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    private boolean LoginResult(boolean result) {
        return result;
    }


    public static class loginPost implements Callable<Object> {
        String url;
        String json;

        public loginPost(String url, String json) {
            this.url = url;

            this.json = json;

        }

        String post() throws IOException {
            RequestBody body = RequestBody.create(JSONs, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Log.i(TAG, request.toString());
            Response response = client.newCall(request).execute();
            if(response.code()!=200)return "false";
            String r = response.body().string();
            return r;
        }

        @Override
        public Object call() throws Exception {
            return post();
        }
    }
}
