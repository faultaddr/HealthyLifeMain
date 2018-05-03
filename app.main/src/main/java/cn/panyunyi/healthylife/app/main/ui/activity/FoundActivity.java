package cn.panyunyi.healthylife.app.main.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.panyunyi.healthylife.app.main.R;
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


    private MyPagerAdapter adapter;

    private Context mContext;

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
        adapter = new MyPagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        refreshViews(0);
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

}
