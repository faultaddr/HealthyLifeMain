package cn.panyunyi.healthylife.app.server.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import cn.panyunyi.healthylife.app.server.MainActivity;
import cn.panyunyi.healthylife.app.server.R;
import cn.panyunyi.healthylife.app.server.db.DataBaseOpenHelper;
import cn.panyunyi.healthylife.app.server.ui.activity.MonitorActivity;
import cn.panyunyi.healthylife.app.server.util.TimeUtil;

/**
 * Created by panyunyi on 2018/2/10.
 */

public class MainListAdapter extends BaseAdapter {

    private Context mContext;
    private int typeFlag = 0;
    int s[] = {R.string.heart_rate, R.string.step_count, R.string.blood_pressure, R.string.blood_oxygen};
    int d[] = {R.drawable.heart, R.drawable.lamp, R.drawable.heart_pressure, R.drawable.oxygen};
    private String beats = "";
    private String steps = "";
    private onClickItemView itemView;
    private String TAG = "MainListAdapter";

    public void setBeats(String beatsAvg) {
        this.beats = beatsAvg;
        this.notifyDataSetChanged();
    }

    public void setSteps(String stepsCount) {
        steps = stepsCount;
        notifyDataSetChanged();
    }

    public MainListAdapter(Context context) {
        mContext = context;
    }
    //接口回调
    public interface onClickItemView{
        public void itemViewClicked(int id);
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
        final int  position =i;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.main_page_list_item, null);
        if(itemView!=null){
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
                            Intent intent = new Intent();
                            intent.setClass(mContext, MonitorActivity.class);
                            ((MainActivity) mContext).startActivityForResult(intent, 0);
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
                entries.add(new Entry(1f, 1500f));
                entries.add(new Entry(2f, 1600f));
                entries.add(new Entry(3f, 2000f));
                date.add(123 + "");
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


}

