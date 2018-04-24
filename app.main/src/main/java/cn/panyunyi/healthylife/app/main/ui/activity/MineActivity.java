package cn.panyunyi.healthylife.app.main.ui.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.security.spec.ECField;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.panyunyi.healthylife.app.main.R;
import cn.panyunyi.healthylife.app.main.util.DataCleanManager;

public class MineActivity extends AppCompatActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mine);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.mine_activity_mine_info, R.id.mine_activity_search_update, R.id.mine_activity_related_info, R.id.mine_activity_back_post, R.id.mine_activity_delete_data})
    public void onClick(RelativeLayout layout) {
        switch (layout.getId()) {
            case R.id.mine_activity_mine_info:

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
                }catch (Exception e){
                    snackbar = Snackbar.make(view, "Snack Bar Text", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                }

                break;
        }
    }


}
