package cn.panyunyi.healthylife.app.main.ui.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.panyunyi.healthylife.app.main.Constant;
import cn.panyunyi.healthylife.app.main.R;

/**
 *
 */
/*
 *create by panyunyi on2018/5/9
 */public class FirstAidFragment extends Fragment {
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    String str[] = {"tangshang", "fashao", "yatong", "niushang", "shiwuzhongdu", "meiqizhongdu", "jiujingzhongdu", "yugucihou", "zhongshu", "futong", "liubixue",
            "toutong", "dianxian", "shenjingshuaishuo", "huxikunnan", "jixingfeiyan", "weijingluan", "xinjiaotong", "naoyixue", "hunmi", "zhixi", "mifengzheshang",
            "choujin", "zhongfeng", "chudian", "chongwuyaoshang", "chuxue", "toubushoushang", "tufaerlong", "xinzangzhouting",
            "rengonghuxi", "zhashang", "fucuoyao", "guzhe", "nishui", "shouzhiqieduan", "yiwukahou", "muci", "shaoshang", "shouzhiqieshang",
            "shixuexiuke", "yiwuruyan", "weichuankong", "zhijiashoucuo"
    };
    String chinese[] = {
            "烫伤", "发烧", "牙痛", "扭伤", "食物中毒", "煤气中毒", "酒精中毒", "鱼骨刺喉", "中暑", "腹痛", "流鼻血",
            "头痛", "癫痫", "神经衰弱", "呼吸困难", "急性肺炎", "胃痉挛", "心绞痛", "脑溢血", "昏迷", "窒息", "蜜蜂蜇伤",
            "抽筋", "中风", "触电", "宠物咬伤", "出血", "头部受伤", "突发耳聋", "心脏骤停",
            "人工呼吸", "扎伤", "服错药", "骨折", "溺水", "手指切断", "异物卡喉", "木刺", "烧伤", "手指切伤",
            "失血休克", "异物入眼", "胃穿孔", "指甲受挫"
    };

    private MyGridViewAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_first_aid, null);
        ButterKnife.bind(view);
        GridView gridView = view.findViewById(R.id.grid_view);
        initAdapter();
        gridView.setAdapter(adapter);
        return view;
    }

    private void initAdapter() {
        adapter = new MyGridViewAdapter();
        adapter.setDataList(Arrays.asList(str));
    }


    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    public class MyGridViewAdapter extends BaseAdapter {
        private List gridDataList = new ArrayList<>();

        public void setDataList(List list) {
            gridDataList = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return 44;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.grid_view_item, null);
/*                Random random = new Random();
                int r = random.nextInt(256);
                int g = random.nextInt(256);
                int b = random.nextInt(256);
                convertView.findViewById(R.id.img).setBackgroundColor(Color.rgb(r,g,b));*/
                ((TextView) (convertView.findViewById(R.id.text))).setText(chinese[position]);
                Random random = new Random();
                int r = random.nextInt(4);
                switch (r) {
                    case 1:
                        ((ImageView) (convertView.findViewById(R.id.img))).setImageResource(R.drawable.grid_1);
                        break;
                    case 2:
                        ((ImageView) (convertView.findViewById(R.id.img))).setImageResource(R.drawable.grid_2);
                        break;
                    case 3:
                        ((ImageView) (convertView.findViewById(R.id.img))).setImageResource(R.drawable.grid_3);
                        break;
                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(getActivity());
                        HorizontalScrollView scrollView = new HorizontalScrollView(getActivity());

                        ImageView imageView = new ImageView(getActivity());
                        Picasso.with(getActivity())
                                .load(Constant.API_URL + "/img/firstaid/" + gridDataList.get(position) + ".jpg")
                                .resize(900, 600)
                                .into(imageView);

                        scrollView.addView(imageView);
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        dialog.show();
                        dialog.addContentView((View) scrollView, layoutParams);
                        dialog.setCanceledOnTouchOutside(false);

                    }
                });
            }

            return convertView;

        }
    }
}
