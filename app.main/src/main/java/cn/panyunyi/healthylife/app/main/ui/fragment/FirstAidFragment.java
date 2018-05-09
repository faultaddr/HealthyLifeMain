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
            "choujing", "zhongfeng", "chudian", "chongwuyaoshang", "chuxue", "toubushoushang", "tufaerlong", "xinzangzhouting",
            "rengonghuxi", "zhashang", "fucuoyao", "guzhe", "nishui", "shouzhiqieduan", "yiwukahou", "muci", "shaoshang", "shouzhiqieshang",
            "shixuexiuke", "yiwuruyan", "weichuankong", "zhijiashoucuo"
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
                ((TextView) (convertView.findViewById(R.id.text))).setText(gridDataList.get(position).toString());
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
