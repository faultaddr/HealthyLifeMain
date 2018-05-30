package cn.panyunyi.healthylife.app.main.ui.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import butterknife.ButterKnife;
import cn.panyunyi.healthylife.app.main.Constant;
import cn.panyunyi.healthylife.app.main.R;
import cn.panyunyi.healthylife.app.main.biz.remote.model.HealthReportItem;
import cn.panyunyi.healthylife.app.main.biz.remote.service.LoginSession;
import cn.panyunyi.healthylife.app.main.manager.GlobalHttpManager;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/*
 *create by panyunyi on2018/5/10
 */

public class HealthReportFragment extends Fragment {
    private String TAG = "HealthReportFragment";


    RatingBar beatRatingBar;

    RatingBar stepRatingBar;

    RatingBar generalRatingBar;

    RadarChart radarChart;

    TextView reportDetail;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_health_report, null);
        beatRatingBar = view.findViewById(R.id.beat_rating_bar);
        stepRatingBar = view.findViewById(R.id.step_rating_bar);
        generalRatingBar = view.findViewById(R.id.general_rating_bar);
        reportDetail = view.findViewById(R.id.report_detail);
        radarChart=view.findViewById(R.id.radar_chart);
        beatRatingBar.setNumStars(5);
        stepRatingBar.setNumStars(5);
        generalRatingBar.setNumStars(5);
        ButterKnife.bind(view);
        initData();
        return view;
    }

    private void initData() {
        if (LoginSession.getLoginSession().getLoginedUser() != null) {
            new CompositeDisposable().add(getHealthReport(LoginSession.getLoginSession().getLoginedUser().getUserId())
                    // Run on a background thread
                    .subscribeOn(Schedulers.io())
                    // Be notified on the main thread
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<HealthReportItem>() {
                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete()");
                        }

                        @Override
                        public void onNext(HealthReportItem healthReportItem) {
                            stepRatingBar.setRating(5f - healthReportItem.data.stepDegree);
                            beatRatingBar.setRating(5f - healthReportItem.data.beatDegree);
                            generalRatingBar.setRating(5f - healthReportItem.data.generalDegree);
                            reportDetail.setText(healthReportItem.data.reportMsg);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError()", e);
                            stepRatingBar.setRating(0.6f);
                            beatRatingBar.setRating(0.8f);
                            generalRatingBar.setRating(0.7f);
                            reportDetail.setText("身体最近状态很好，请继续保持");
                        }

                    }));

        }
        reportDetail.setText("身体最近状态很好，请继续保持");
        Description description=new Description();
        description.setText("健康雷达图");
        radarChart.setDescription(description);
        // 绘制线条宽度，圆形向外辐射的线条
        radarChart.setWebLineWidth(1.5f);
        // 内部线条宽度，外面的环状线条
        radarChart.setWebLineWidthInner(1.0f);
        // 所有线条WebLine透明度
        radarChart.setWebAlpha(100);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        // 点击的时候弹出对应的布局显示数据

        setData();

        XAxis xAxis = radarChart.getXAxis();

        // X坐标值字体大小
        xAxis.setTextSize(12f);

        YAxis yAxis = radarChart.getYAxis();

        // Y坐标值标签个数
        yAxis.setLabelCount(6, false);
        // Y坐标值字体大小
        yAxis.setTextSize(15f);
        // Y坐标值是否从0开始
        yAxis.setStartAtZero(true);

        Legend l = radarChart.getLegend();
        // 图例位置
        l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        // 图例X间距
        l.setXEntrySpace(2f);
        // 图例Y间距
        l.setYEntrySpace(1f);
    }


    private String[] mParties = new String[]{
            "Party A", "Party B", "Party C"
    };

    public void setData() {

        float mult = 150;
        int cnt = 3; // 不同的维度Party A、B、C...总个数

        // Y的值，数据填充
        ArrayList<RadarEntry> yVals1 = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> yVals2 = new ArrayList<RadarEntry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < cnt; i++) {
            yVals1.add(new RadarEntry((float) (Math.random() * mult) + mult / 2, i));
        }

        for (int i = 0; i < cnt; i++) {
            yVals2.add(new RadarEntry((float) (Math.random() * mult) + mult / 2, i));
        }

        // Party A、B、C..
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < cnt; i++)
            xVals.add(mParties[i % mParties.length]);

        RadarDataSet set1 = new RadarDataSet(yVals1, "Set 1");
        // Y数据颜色设置
        set1.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        // 是否实心填充区域
        set1.setDrawFilled(true);
        // 数据线条宽度
        set1.setLineWidth(2f);

        RadarDataSet set2 = new RadarDataSet(yVals2, "Set 2");
        set2.setColor(ColorTemplate.VORDIPLOM_COLORS[4]);
        set2.setDrawFilled(true);
        set2.setLineWidth(2f);

        ArrayList<RadarDataSet> sets = new ArrayList<RadarDataSet>();
        sets.add(set1);
        sets.add(set2);
        RadarData data=new RadarData(set1);

        // 数据字体大小
        data.setValueTextSize(8f);
        // 是否绘制Y值到图表
        data.setDrawValues(true);

        radarChart.setData(data);

        radarChart.invalidate();
    }

    private void initViews() {


    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected Observable<HealthReportItem> getHealthReport(int userId) {
        return Observable.defer(new Callable<ObservableSource<? extends HealthReportItem>>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public ObservableSource<? extends HealthReportItem> call() throws Exception {
                // Do some long running operation
                String result = null;
                ExecutorService exs = Executors.newCachedThreadPool();
                GlobalHttpManager.SendGet get = GlobalHttpManager.getInstance().getMethodManager(Constant.API_URL + "/GET/UserGrade?" + "userId=" + LoginSession.getLoginSession().getLoginedUser().getUserId());
                Future<Object> future = exs.submit(get);//使用线程池对象执行任务并获取返回对象
                try {
                    result = future.get().toString();//当调用了future的get方法获取返回的值得时候
                    //如果线程没有计算完成，那么这里就会一直阻塞等待线程执行完成拿到返回值
                    exs.shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return Observable.just(Objects.requireNonNull(JSON.parseObject(result, HealthReportItem.class)));
            }
        });
    }
}
