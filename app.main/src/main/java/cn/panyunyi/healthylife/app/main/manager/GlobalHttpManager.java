package cn.panyunyi.healthylife.app.main.manager;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
 *create by panyunyi on2018/4/30
 */
public class GlobalHttpManager {
    public static final MediaType JSONs
            = MediaType.parse("application/json; charset=utf-8");
    public static OkHttpClient client;
    private static String TAG = "GlobalHttpManager";
    private static GlobalHttpManager ourInstance = null;

    private GlobalHttpManager() {
        client = new OkHttpClient();
    }

    public static GlobalHttpManager getInstance() {
        if (ourInstance == null)
            ourInstance = new GlobalHttpManager();
        return ourInstance;
    }

    public SendGet getMethodManager(String url) {
        SendGet sendGet = new SendGet(url);
        return sendGet;
    }

    public SendPost postMethodManager(String url, String json) {
        SendPost sendPost = new SendPost(url, json);
        return sendPost;
    }
    public  class SendPost implements Callable<Object> {
        String url;
        String json;

        public SendPost(String url, String json) {
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
            String r = response.body().string();
            return r;
        }

        @Override
        public Object call() throws Exception {
            return post();
        }
    }


    public  class SendGet implements Callable<Object> {
        String url;

        public SendGet(String url) {
            this.url = url;
        }

        String get() throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            Log.i(TAG, request.toString());
            Response response = client.newCall(request).execute();
            String r = response.body().string();
            return r;
        }

        @Override
        public Object call() throws Exception {
            return get();
        }
    }
}
