package cn.panyunyi.healthylife.app.main.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import cn.panyunyi.healthylife.app.main.R;
import cn.panyunyi.healthylife.app.main.biz.local.dao.BeatDataDao;
import cn.panyunyi.healthylife.app.main.biz.local.model.BeatEntity;
import cn.panyunyi.healthylife.app.main.db.DataBaseOpenHelper;
import cn.panyunyi.healthylife.app.main.util.TimeUtil;

/**
 * Created by panyunyi on 2018/2/10.
 */

public class MainListAdapter extends BaseAdapter {

    int s[] = {R.string.heart_rate, R.string.step_count, R.string.blood_pressure, R.string.blood_oxygen};
    int d[] = {R.drawable.heart, R.drawable.lamp, R.drawable.heart_pressure, R.drawable.oxygen};
    private Context mContext;
    private int typeFlag = 0;
    private String beats = "";
    private String steps = "";
    private onClickItemView itemView;
    private String TAG = "MainListAdapter";

    public MainListAdapter(Context context) {
        mContext = context;
    }

    public void setBeats(String beatsAvg) {
        this.beats = beatsAvg;
        this.notifyDataSetChanged();
    }

    public void setSteps(String stepsCount) {
        steps = stepsCount;
        notifyDataSetChanged();
    }

    public void setOnClickItemView(onClickItemView onClickMyTextView) {
        this.itemView = onClickMyTextView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.i(TAG, "getview start");
        Log.i(TAG, "beats is" + beats);
        Log.i(TAG, "steps is" + steps);
        final int position = i;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.main_page_list_item, null);
        if (itemView != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.itemViewClicked(position);
                }
            });
        }
        switch (i) {
            case 0: {
                TextView title = view.findViewById(R.id.main_list_item_text);
                TextView date = view.findViewById(R.id.date);
                TextView description = view.findViewById(R.id.detail);
                TextView unit = view.findViewById(R.id.unit);
                LineChart chart = view.findViewById(R.id.chart);
                date.setVisibility(View.VISIBLE);
                date.setText(TimeUtil.getCurrentDate());

                /*
                 * 判断是否测量过心率
                 */
                if (beats.equals("")) {
                    unit.setText("现在去测量");
                    unit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            itemView.itemViewClicked(view.getId());
                        }
                    });
                } else {
                    description.setText(beats);
                    unit.setText(" /分钟");
                }
                title.setText(mContext.getText(s[i]));
                title.setTextSize(16);
                Drawable drawable = mContext.getDrawable(d[i]);
                drawable.setBounds(20, 0, 80, 60);
                title.setCompoundDrawables(drawable, null, null, null);
                initChartData(0, chart);
                chart.setVisibility(View.VISIBLE);
                view.setTag(i);
            }
            break;
            case 1: {
                TextView title = (TextView) view.findViewById(R.id.main_list_item_text);
                TextView date = view.findViewById(R.id.date);
                TextView description = view.findViewById(R.id.detail);
                date.setVisibility(View.VISIBLE);
                date.setText(TimeUtil.getCurrentDate());
                TextView unit = view.findViewById(R.id.unit);
                if (steps.equals("")) {
                    unit.setVisibility(View.INVISIBLE);
                } else {
                    unit.setVisibility(View.VISIBLE);
                    description.setText(steps);
                    unit.setText(" 步");
                }
                LineChart chart = view.findViewById(R.id.chart);
                chart.setVisibility(View.VISIBLE);
                initChartData(i, chart);
                title.setText(mContext.getText(s[i]));
                title.setTextSize(16);
                Drawable drawable = mContext.getDrawable(d[i]);
                drawable.setBounds(20, 0, 80, 60);
                title.setCompoundDrawables(drawable, null, null, null);
                view.setTag(i);
            }
            break;

            case 2:
            case 3:

                TextView title = (TextView) view.findViewById(R.id.main_list_item_text);
                TextView date = view.findViewById(R.id.date);
                final TextView description = view.findViewById(R.id.detail);
                date.setVisibility(View.VISIBLE);
                date.setText(TimeUtil.getCurrentDate());
                TextView unit = view.findViewById(R.id.unit);
                unit.setVisibility(View.INVISIBLE);
                title.setText(mContext.getText(s[i]));
                title.setTextSize(16);
                Drawable drawable = mContext.getDrawable(d[i]);
                drawable.setBounds(20, 0, 80, 60);
                title.setCompoundDrawables(drawable, null, null, null);
                view.setTag(i);
                break;

        }
        return view;
    }

    private void initChartData(int i, Object t) {
        switch (i) {
            case 0: {
                List<Entry> entries = new ArrayList<>();
                List<String> index = new ArrayList<>();
                List<BeatEntity> beatList = BeatDataDao.getInstance(mContext).getAllBeats();
                int count = 0;
                if (beatList != null) {
                    for (BeatEntity entity : beatList) {
                        entries.add(new Entry((float) count, (float) Integer.parseInt(entity.beats)));
                        count++;
                    }

                    if (entries.size() != 0) {
                        LineDataSet dataSet = new LineDataSet(entries, "心率");
                        // add entries to dataset..
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
                        ((LineChart) t).setData(lineData);
                        ((LineChart) t).animateY(3000);
                        ((LineChart) t).invalidate(); // refresh
                        Description description = new Description();
                        description.setText("心率波动值");
                        ((LineChart) t).setDescription(description);
                    } else {
                        ((LineChart) t).setVisibility(View.GONE);
                    }
                } else {
                    ((LineChart) t).setVisibility(View.GONE);
                }
            }
            break;

            case 1:
                DataBaseOpenHelper helper = DataBaseOpenHelper.getInstance(mContext, "step", 1, null);
                Cursor cursor = helper.query("step", "");
                if (cursor.getCount() == 0) {
                    typeFlag = 0;
                    ((LineChart) t).setVisibility(View.GONE);
                    return;
                }
                List<Entry> entries = new ArrayList<Entry>();
                List<String> date = new ArrayList<>();
                int count = 0;
                while (cursor.moveToNext()) {

                    //光标移动成功
                    //把数据取出
                    int datePos = cursor.getColumnIndex("currentDate");
                    int stepsPos = cursor.getColumnIndex("steps");

                    // turn your data into Entry objects
                    entries.add(new Entry((float) count, (float) Integer.parseInt(cursor.getString(stepsPos))));
                    date.add(cursor.getString(datePos));
                    count++;
                }
                if (entries.size() != 0) {
                    LineDataSet dataSet = new LineDataSet(entries, "步数"); // add entries to dataset..
                    dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    dataSet.setCubicIntensity(0.2f);
                    dataSet.setDrawFilled(true);
                    dataSet.setDrawCircles(false);
                    dataSet.setLineWidth(1.8f);
                    dataSet.setCircleRadius(4f);
                    dataSet.setCircleColor(Color.RED);
                    dataSet.setHighLightColor(Color.rgb(244, 117, 117));
                    dataSet.setColor(Color.RED);
                    dataSet.setFillColor(Color.RED);
                    dataSet.setFillAlpha(100);
                    dataSet.setDrawHorizontalHighlightIndicator(false);
                    LineData lineData = new LineData(dataSet);
                    ((LineChart) t).setData(lineData);
                    ((LineChart) t).animateY(3000);
                    ((LineChart) t).invalidate(); // refresh
                    Description description = new Description();
                    description.setText("最近七天步数排行");
                    ((LineChart) t).setDescription(description);
                }
                break;
            default:
                DataBaseOpenHelper helper1 = DataBaseOpenHelper.getInstance(mContext, "beats", 1, new ArrayList<String>());
                try {
                    Cursor cursor1 = helper1.query("beats", "");
                } catch (Exception e) {
                    typeFlag = 0;
                    ((LineChart) t).setVisibility(View.GONE);
                    return;
                }
        }
    }

    @Override
    public int getItemViewType(int i) {


        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    //接口回调
    public interface onClickItemView {
        public void itemViewClicked(int id);
    }


}

