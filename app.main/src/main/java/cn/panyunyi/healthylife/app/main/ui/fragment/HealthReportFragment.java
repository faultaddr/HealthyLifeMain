package cn.panyunyi.healthylife.app.main.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.github.mikephil.charting.charts.RadarChart;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.panyunyi.healthylife.app.main.R;

/*
 *create by panyunyi on2018/5/10
 */

public class HealthReportFragment extends Fragment {

    @BindView(R.id.beat_rating_bar)
    RatingBar beatRatingBar;
    @BindView(R.id.step_rating_bar)
    RatingBar stepRatingBar;
    @BindView(R.id.general_rating_bar)
    RatingBar generalRatingBar;
    @BindView(R.id.radar_chart)
    RadarChart radarChart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_health_report, null);
        ButterKnife.bind(view);
        return view;
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
}
