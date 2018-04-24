package cn.panyunyi.healthylife.app.main.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.panyunyi.healthylife.app.main.biz.local.dao.BeatDataDao;
import cn.panyunyi.healthylife.app.main.biz.local.model.BeatEntity;
import cn.panyunyi.healthylife.app.main.R;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.data_chart)
    CombinedChart chart;
    @BindView(R.id.data_list)
    RecyclerView dataList;
    Map<String, List<BeatEntity>> map;
    private String TAG = "DetailActivity";
    private DetailInfoAdapter mListAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        initVariables();
        initAdapter();
        getDataFromDatabase();


    }


    private void initVariables() {
        mContext = this;
        map = new HashMap<>();
    }

    private void initAdapter() {
        mListAdapter = new DetailInfoAdapter(this);
        mListAdapter.setListMap(map);
        dataList.setAdapter(mListAdapter);
        dataList.setLayoutManager(new LinearLayoutManager(this));

    }

    private void initChart() {
        if (map != null && map.size() != 0) {
            Set<String> set = map.keySet();
            Object[] s = set.toArray();
            Arrays.sort(s);
            List<BeatEntity> list = map.get(s[s.length - 1]);
            configChart(list);
        }
    }

    private void configChart(List<BeatEntity> list) {
        List<Entry> entries = new ArrayList<>();
        List<BarEntry> barEntrylist = new ArrayList<>();
        List<String> index = new ArrayList<>();
        int count = 0;
        for (BeatEntity entity : list) {
            entries.add(new Entry((float) count, (float) Integer.parseInt(entity.beats)));
            barEntrylist.add(new BarEntry((float) count, (float) Integer.parseInt(entity.beats)));
            count++;
        }

        if (entries.size() != 0) {
            LineDataSet dataSet = new LineDataSet(entries, "心率");
            BarDataSet barDataSet = new BarDataSet(barEntrylist, "心率");
            // add entries to dataset..
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            barDataSet.setBarBorderColor(Color.parseColor("#afbcf1"));
            barDataSet.setBarShadowColor(Color.GREEN);
            barDataSet.setBarBorderWidth(10);
            barDataSet.setDrawValues(false);
            dataSet.setCubicIntensity(0.2f);
            dataSet.setDrawFilled(true);
            dataSet.setDrawCircles(false);
            dataSet.setLineWidth(1.8f);
            dataSet.setCircleRadius(4f);
            dataSet.setCircleColor(Color.BLUE);
            dataSet.setHighLightColor(Color.rgb(244, 117, 117));
            dataSet.setColor(Color.BLUE);
            dataSet.setFillColor(Color.BLUE);
            dataSet.setFillAlpha(100);
            dataSet.setDrawHorizontalHighlightIndicator(false);
            LineData lineData = new LineData(dataSet);
            BarData barData = new BarData(barDataSet);
            CombinedData data = new CombinedData();
            data.setData(lineData);
            data.setData(barData);
            chart.setData(data);
            chart.animateY(3000);
            chart.invalidate(); // refresh
            Description description = new Description();
            description.setText("心率波动值");
            chart.setDescription(description);
        }
    }

    /**
     * 拿到所有的心率数据并按当次分类
     */
    private void getDataFromDatabase() {
        Observable.create(new ObservableOnSubscribe<Map<String, List<BeatEntity>>>() {
            @Override
            public void subscribe(ObservableEmitter<Map<String, List<BeatEntity>>> emitter) throws Exception {
                BeatDataDao dao = BeatDataDao.getInstance(getApplicationContext());
                List<BeatEntity> list = dao.getAllBeats();
                if (list != null) {
                    for (BeatEntity entity : list) {
                        if (map != null && map.containsKey(entity.timeCount)) {
                            map.get(entity.timeCount).add(entity);
                        } else {
                            List<BeatEntity> singleList = new ArrayList<>();
                            singleList.add(entity);
                            map.put(entity.timeCount, singleList);

                        }
                    }
                }
                emitter.onNext(map);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Map<String, List<BeatEntity>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
            }

            @Override
            public void onNext(Map<String, List<BeatEntity>> value) {
                Log.d(TAG, "" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "complete");
                mListAdapter.setListMap(map);
                mListAdapter.notifyDataSetChanged();
                initChart();
            }
        });


    }


    private class DetailInfoAdapter extends RecyclerView.Adapter<DetailInfoAdapter.MyViewHolder> {
        Context mContext;
        Object[] obj;
        private Map<String, List<BeatEntity>> listMap;

        public DetailInfoAdapter(Context context) {
            this.mContext = context;
        }

        public void setListMap(Map<String, List<BeatEntity>> map) {
            listMap = map;
            Set<String> set = map.keySet();
            obj = set.toArray();
            Arrays.sort(obj);
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.activity_detail_list_item, null);


            MyViewHolder holder = new MyViewHolder(view);


            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder viewHolder, int i) {
            final int postion = i;

            final List<BeatEntity> list = map.get(obj[i]);
            Log.i(TAG, "list size " + list.size());
            viewHolder.goIntoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    configChart(list);
                    dataList.scrollTo(0, 0);
                }
            });
            if (list != null && list.size() != 0) {
                int sum = 0;
                for (BeatEntity entity : list) {

                    sum += Integer.parseInt(entity.beats);

                }
                viewHolder.dateInfo.setText(list.get(0).currentDate);
                viewHolder.detailInfo.setText(sum / list.size() + " ");
            }

        }


        @Override
        public int getItemCount() {
            Log.i(TAG, map.size() + "");
            return map == null ? 0 : map.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView dateInfo;
            TextView detailInfo;
            TextView goIntoBtn;

            public MyViewHolder(View itemView) {
                super(itemView);
                detailInfo = itemView.findViewById(R.id.detail_info);
                dateInfo = itemView.findViewById(R.id.date_detail);
                goIntoBtn = itemView.findViewById(R.id.go_into);
            }
        }
    }


}
