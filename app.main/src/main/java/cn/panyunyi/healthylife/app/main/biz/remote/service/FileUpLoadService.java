package cn.panyunyi.healthylife.app.main.biz.remote.service;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import cn.panyunyi.healthylife.app.main.GlobalHttpManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileUpLoadService implements Callable {
    MediaType mediaType;
    String uploadUrl;
    String localPath;
    private String TAG = "FileUpLoadService";

    public FileUpLoadService(MediaType m, String u, String l) {
        mediaType = m;
        uploadUrl = u;
        localPath = l;
    }

    public String put() throws IOException {
        File file = new File(localPath);
        //RequestBody body = RequestBody.create(mediaType, file);

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            String fileName = file.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("headImage", fileName, body);
        }

        Request request = new Request.Builder().url(uploadUrl).post(requestBody.build()).tag(this).build();
        // readTimeout("请求超时时间" , 时间单位);
        GlobalHttpManager.client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    Log.i(TAG, response.message() + " , body " + str);
                } else {
                    Log.i(TAG, response.message() + " error : body " + response.body().string());
                }
            }
        });
        return "true";

    }

    @Override
    public Object call() throws Exception {
        return put();
    }
}
