package cn.panyunyi.healthylife.app.main.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.panyunyi.healthylife.app.main.Constant;
import cn.panyunyi.healthylife.app.main.GlobalHttpManager;
import cn.panyunyi.healthylife.app.main.R;
import cn.panyunyi.healthylife.app.main.biz.local.model.MyListItem;
import cn.panyunyi.healthylife.app.main.ui.custom.ZoomInTransform;


public class FoundActivity extends AppCompatActivity {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.choicest_head)
    TextView mChoicestHeadTv;
    @BindView(R.id.others)
    TextView mOthersTv;
    @BindView(R.id.interesting_things)
    TextView mInterestingTv;
    @BindView(R.id.info_list_view)
    RecyclerView mInfoListView;

    private MyPagerAdapter mViewPagerAdapter;
    private RecyclerViewAdapter mRecyclerAdapter;
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            if (msg.what == 1) {
                mRecyclerAdapter.notifyDataSetChanged();
            }
        }
    };

    private Context mContext;
    private List<MyListItem> itemList = new ArrayList<>();
    private String TAG = "FoundActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_found);
        ButterKnife.bind(this);
        mContext = this;
        initViews();
        initAdapter();
        initData();

    }

    private void initData() {
        new Thread() {
            public void run() {
                ExecutorService exs = Executors.newCachedThreadPool();
                GlobalHttpManager manager = GlobalHttpManager.getInstance();
                GlobalHttpManager.SendGet ct = manager.getMethodManager(Constant.API_URL + "/GET/news/all");//实例化任务对象
                //大家对Future对象如果陌生，说明你用带返回值的线程用的比较少，要多加练习
                Future<Object> future = exs.submit(ct);//使用线程池对象执行任务并获取返回对象
                try {
                    String result = future.get().toString();//当调用了future的get方法获取返回的值得时候
                    //如果线程没有计算完成，那么这里就会一直阻塞等待线程执行完成拿到返回值
                    Log.i(TAG, result);
                    itemList = JSON.parseArray(result, MyListItem.class);
                    mRecyclerAdapter.setDataList(itemList);
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                    exs.shutdown();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initViews() {

        mViewPager.setPageTransformer(true,
                new ZoomInTransform());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                refreshViews(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void refreshViews(int position) {
        switch (position % 3) {
            case 0:
                mChoicestHeadTv.setAlpha(1f);
                mInterestingTv.setAlpha(0.5f);
                mOthersTv.setAlpha(0.5f);
                break;
            case 1:
                mChoicestHeadTv.setAlpha(0.5f);
                mInterestingTv.setAlpha(1f);
                mOthersTv.setAlpha(0.5f);
                break;
            case 2:
                mChoicestHeadTv.setAlpha(0.5f);
                mInterestingTv.setAlpha(0.5f);
                mOthersTv.setAlpha(1f);
                break;

        }
    }

    private void initAdapter() {
        mViewPagerAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(0);
        refreshViews(0);
        mRecyclerAdapter = new RecyclerViewAdapter();
        mInfoListView.setLayoutManager(new LinearLayoutManager(this));
        mInfoListView.setAdapter(mRecyclerAdapter);
    }

    public class MyPagerAdapter extends PagerAdapter {
        private int drawableIds[] = {R.drawable.found_top_1, R.drawable.found_top_2, R.drawable.found_top_3};

        @Override
        public int getCount() {
            return 3;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(drawableIds[position]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return object == view;
        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<MyListItem> linkUrlList = new ArrayList<>();

        public void setDataList(List<MyListItem> oralData) {
            linkUrlList = oralData;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.activity_found_list_item, null);
            MyViewHolder viewHolder = new MyViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            Picasso.with(mContext)
                    .load(Constant.API_URL + linkUrlList.get(i).getImgUrl())
                    .resize(240, 240)
                    .centerCrop()
                    .into(myViewHolder.imageView);
            myViewHolder.description.setText(linkUrlList.get(i).getDescription());
        }

        @Override
        public int getItemCount() {
            return linkUrlList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView description;
        private ImageView getInto;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image);
            this.description = itemView.findViewById(R.id.description);
            this.getInto = itemView.findViewById(R.id.get_into);

        }
    }


}
