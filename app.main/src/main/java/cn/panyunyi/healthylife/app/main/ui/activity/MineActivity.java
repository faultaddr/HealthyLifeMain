package cn.panyunyi.healthylife.app.main.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.security.spec.ECField;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.panyunyi.healthylife.app.main.R;
import cn.panyunyi.healthylife.app.main.biz.remote.service.LoginSession;
import cn.panyunyi.healthylife.app.main.ui.custom.CircleImageView;
import cn.panyunyi.healthylife.app.main.util.DataCleanManager;

public class MineActivity extends AppCompatActivity implements View.OnClickListener{

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



    Snackbar snackbar;


//变量


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mine);
        ButterKnife.bind(this);
        initView();


    }

    private void initView() {
        mBackPost.setOnClickListener(this);
        mDeleteData.setOnClickListener(this);
        mMineInfo.setOnClickListener(this);
        mSearchUpdate.setOnClickListener(this);
        mUserPic.setOnClickListener(this);
        mRelatedInfo.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_activity_mine_info:
                if (LoginSession.getLoginSession().getLoginedUser()==null) {
                    Intent intent = new Intent();
                    intent.setClass(this, LoginActivity.class);
                    startActivityForResult(intent, 0);
                } else {

                }
                break;
            case R.id.mine_activity_search_update:

                break;
            case R.id.mine_activity_related_info:

                break;
            case R.id.mine_activity_back_post:

                break;
            case R.id.mine_activity_delete_data:
                try {
                    DataCleanManager.cleanApplicationData(this);
                } catch (Exception e) {
                    snackbar = Snackbar.make(mDeleteData, e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                }

                break;
        }
    }
}
