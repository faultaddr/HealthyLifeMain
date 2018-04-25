package cn.panyunyi.healthylife.app.main.biz.remote.service;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.panyunyi.growingup.Constant;
import com.example.panyunyi.growingup.entity.remote.User;
import com.example.panyunyi.growingup.entity.remote.UserInfo;
import com.orhanobut.logger.Logger;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by panyu on 2017/6/9.
 */

public class LoginImpl implements LoginManager {
    private User muser;
    String result;
    public static final MediaType JSONs
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    public LoginImpl(User user) {

        muser = user;

    }

    @Override
    public boolean login() {
        UserInfo loginedUser = new UserInfo();
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
        JSONObject jsonObject =new JSONObject();
        try {
            jsonObject.put("userId", muser.userId);
            jsonObject.put("userPassword", muser.userPassword);
        } catch (JSONException ex) {
            ex.printStackTrace();

        }

        loginPost ct = new loginPost(Constant.API_URL+"/login", jsonObject.toString());//实例化任务对象
        //大家对Future对象如果陌生，说明你用带返回值的线程用的比较少，要多加练习
        Future<Object> future = exs.submit(ct);//使用线程池对象执行任务并获取返回对象
        try {
            result = future.get().toString();//当调用了future的get方法获取返回的值得时候
            //如果线程没有计算完成，那么这里就会一直阻塞等待线程执行完成拿到返回值

            exs.shutdown();
            Log.i(">>>",result);
            loginedUser= JSON.parseObject(result,UserInfo.class);
            LoginSession.getLoginSession().setsLoginSession(loginedUser);
            return !LoginResult(result.equals("\"false\""));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }



    }

    private boolean LoginResult(boolean result) {
        return result;
    }


    class loginPost implements Callable<Object> {
        String url;
        String json;

        public loginPost(String url, String json) {
            this.url = url;

                this.json = json;

        }

        String post() throws IOException {
            RequestBody body =RequestBody.create(JSONs,json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            Log.i(">>>", json);
            String r=response.body().string();
            return r;
        }

        @Override
        public Object call() throws Exception {
            return post();
        }
    }
}
