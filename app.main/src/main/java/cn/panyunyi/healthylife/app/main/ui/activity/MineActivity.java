package cn.panyunyi.healthylife.app.main.ui.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.panyunyi.healthylife.app.main.Constant;
import cn.panyunyi.healthylife.app.main.R;
import cn.panyunyi.healthylife.app.main.biz.remote.service.FileUpLoadService;
import cn.panyunyi.healthylife.app.main.biz.remote.service.LoginSession;
import cn.panyunyi.healthylife.app.main.event.MessageEvent;
import cn.panyunyi.healthylife.app.main.ui.custom.CircleImageView;
import cn.panyunyi.healthylife.app.main.ui.fragment.RelatedInfoFragment;
import cn.panyunyi.healthylife.app.main.util.DataCleanManager;
import cn.panyunyi.healthylife.app.main.util.UriToPathUtil;
import okhttp3.MediaType;

public class MineActivity extends AppCompatActivity implements View.OnClickListener {

    //view 相关
    @BindView(R.id.mine_activity_back_post)
    RelativeLayout mBackPost;
    @BindView(R.id.mine_activity_delete_data)
    RelativeLayout mDeleteData;
    @BindView(R.id.mine_activity_related_info)
    RelativeLayout mRelatedInfo;
    @BindView(R.id.mine_activity_search_update)
    RelativeLayout mSearchUpdate;
    @BindView(R.id.mine_activity_mine_info)
    RelativeLayout mMineInfo;
    @BindView(R.id.user_pic)
    CircleImageView mUserPic;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.fragment_container)
    FrameLayout frameLayout;
    Snackbar snackbar;
    RelatedInfoFragment fragment;
    private String TAG = "MineActivity";

    //变量
    @Override
    public void onBackPressed() {
        if (fragment.isVisible()) {
            getFragmentManager().beginTransaction().remove(fragment).commit();
            fragment = null;
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = "false";
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            String s = UriToPathUtil.getRealFilePath(getApplicationContext(), uri);
            System.out.println(s);
            //File file = new File(s);
            ExecutorService exs = Executors.newCachedThreadPool();

            FileUpLoadService fileUpLoad = new FileUpLoadService(MediaType.parse("multipart/form-data;boundary=2000000"), Constant.API_URL + "/uploadUserPic?userId=" + LoginSession.getLoginSession().getLoginedUser().getUserId(), s);

            Future future = exs.submit(fileUpLoad);//使用线程池对象执行任务并获取返回对象
            try {
                result = future.get().toString();
                Log.i(TAG, "fileUpload result" + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {

                e.printStackTrace();
            }
            if (result.equals("true")) {
                ContentResolver cr = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    CircleImageView imageView = (CircleImageView) findViewById(R.id.user_pic);
                    /* 将Bitmap设定到ImageView */
                    imageView.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(), e);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mine);
        ButterKnife.bind(this);
        initView();
        EventBus.getDefault().register(this);


    }

    private void initView() {
        mBackPost.setOnClickListener(this);
        mDeleteData.setOnClickListener(this);
        mMineInfo.setOnClickListener(this);
        mSearchUpdate.setOnClickListener(this);
        mUserPic.setOnClickListener(this);
        mRelatedInfo.setOnClickListener(this);
        if (LoginSession.getLoginSession().getLoginedUser() != null) {
            mUserName.setText(LoginSession.getLoginSession().getLoginedUser().getUserName());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLoginStatus(MessageEvent messageEvent) {
        if (messageEvent.getMessageType().equals(Constant.loginStatus)) {
            if (messageEvent.getMessageContent().equals("true")) {
                mUserName.setText(messageEvent.getMessageContent());
            } else {
                mUserName.setText("游客");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_activity_mine_info:
                if (LoginSession.getLoginSession().getLoginedUser() == null) {
                    Intent intent = new Intent();
                    intent.setClass(this, LoginActivity.class);
                    startActivityForResult(intent, 0);
                } else {
                    //从本地选取头像上传
                    /* 开启Pictures画面Type设定为image */
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    /* 使用Intent.ACTION_GET_CONTENT这个Action */
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    /* 取得相片后返回本画面 */
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.mine_activity_search_update:

                break;
            case R.id.mine_activity_related_info:
                fragment = new RelatedInfoFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.fragment_container, fragment);
                transaction.show(fragment);
                transaction.commit();
                break;
            case R.id.mine_activity_back_post:

                break;
            case R.id.mine_activity_delete_data:
                try {
                    ProgressDialog dialog = new ProgressDialog(this);
                    dialog.create();
                    DataCleanManager.cleanApplicationData(this);
                } catch (Exception e) {
                    snackbar = Snackbar.make(mDeleteData, e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                }

                break;
        }
    }
}
