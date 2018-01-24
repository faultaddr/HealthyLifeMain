package cn.panyunyi.healthylife.app.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecloud.pulltozoomview.PullToZoomListViewEx;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.panyunyi.healthylife.app.main.ui.custome.MainPicView;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_pic)
    PullToZoomListViewEx pullToZoomListViewEx;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MainPicView.Builder builder=new MainPicView.Builder();
        MainPicView mainPicView=builder.
                backgroundPic(R.drawable.main_activity_main_pic).
                plateRadius(200).
                plateWidth(60).
                stepCount(5000).
                build(this);


        //scrollView.setZoomView(imageView);
        pullToZoomListViewEx.setZoomView(mainPicView);


        String[] adapterData = new String[]{"Activity", "Service", "Content Provider", "Intent", "BroadcastReceiver", "ADT", "Sqlite3", "HttpClient",
                "DDMS", "Android Studio", "Fragment", "Loader", "Activity", "Service", "Content Provider", "Intent",
                "BroadcastReceiver", "ADT", "Sqlite3", "HttpClient", "Activity", "Service", "Content Provider", "Intent",
                "BroadcastReceiver", "ADT", "Sqlite3", "HttpClient"};

        pullToZoomListViewEx.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, adapterData));
        pullToZoomListViewEx.getPullRootView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("MainActivity>>>", "position = " + position);
            }
        });


        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        pullToZoomListViewEx.setHeaderLayoutParams(localObject);
        pullToZoomListViewEx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,TestActivity.class);
                startActivity(intent);
            }
        });
    }


}
