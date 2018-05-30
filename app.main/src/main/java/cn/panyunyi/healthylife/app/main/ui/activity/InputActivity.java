package cn.panyunyi.healthylife.app.main.ui.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.DateTimePicker;
import cn.addapp.pickers.picker.SinglePicker;
import cn.panyunyi.healthylife.app.main.R;
import cn.panyunyi.healthylife.app.main.biz.local.dao.PressDataDao;
import cn.panyunyi.healthylife.app.main.biz.local.model.PressEntity;
import cn.panyunyi.healthylife.app.main.biz.remote.service.LoginSession;

public class InputActivity extends AppCompatActivity {
    private String mYear,mMonth,mDay,mHour,mMinute;
    private String type;
    EditText editText;
    TextView textView;
    private PressDataDao dao;
    private String TAG="InputActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        onConstellationPicker(findViewById(R.id.type));
        onYearMonthDayTimePicker(findViewById(R.id.time));
        dao=new PressDataDao(this);
        editText= (EditText) findViewById(R.id.data);
        textView=(TextView)findViewById(R.id.submit);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,""+type);
                switch (type) {
                    case "血压":
                    if (LoginSession.getLoginSession().getLoginedUser() != null) {
                        PressEntity entity = new PressEntity();
                        entity.date = mYear + mMonth + mDay + mHour + mMinute;
                        entity.press = editText.getText().toString();
                        entity.userId = LoginSession.getLoginSession().getLoginedUser().getUserId();
                        dao.addNewData(entity);
                    }
                    break;
                    case "血氧":
                        break;
                }
            }
        });
    }

    public void onYearMonthDayTimePicker(View view) {
        DateTimePicker picker = new DateTimePicker(this, DateTimePicker.HOUR_24);

        picker.setDateRangeStart(2017, 1, 1);
        picker.setDateRangeEnd(2025, 11, 11);
        picker.setSelectedItem(2018,6,16,16,43);
        picker.setTimeRangeStart(9, 0);
        picker.setTimeRangeEnd(20, 30);
        picker.setTitleText("请选择");
//        picker.setStepMinute(5);
        picker.setWeightEnable(true);
        picker.setWheelModeEnable(true);
        LineConfig config = new LineConfig();
        config.setColor(Color.BLUE);//线颜色
        config.setAlpha(120);//线透明度
        config.setVisible(true);//线不显示 默认显示
        picker.setLineConfig(config);
        picker.setLabel(null,null,null,null,null);
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                Toast.makeText(InputActivity.this,year + "-" + month + "-" + day + " " + hour + ":" + minute,Toast.LENGTH_LONG).show();
                mYear=year;
                mMonth=month;
                mDay=day;
                mHour=hour;
                mMinute=minute;
            }
        });
        picker.show();
    }
    public void onConstellationPicker(View view) {
        boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("中文");
        SinglePicker<String> picker = new SinglePicker<>(this,
                isChinese ? new String[]{
"血压" ,"血氧"
                } : new String[]{
"xueya","xueyang"
                });
        picker.setCanLoop(false);//不禁用循环
        picker.setTopBackgroundColor(0xFFEEEEEE);
        picker.setTopHeight(50);
        picker.setTopLineColor(0xFF33B5E5);
        picker.setTopLineHeight(1);
        picker.setTitleText(isChinese ? "请选择" : "Please pick");
        picker.setTitleTextColor(0xFF999999);
        picker.setTitleTextSize(12);
        picker.setCancelTextColor(0xFF33B5E5);
        picker.setCancelTextSize(13);
        picker.setSubmitTextColor(0xFF33B5E5);
        picker.setSubmitTextSize(13);
        picker.setSelectedTextColor(0xFFEE0000);
        picker.setUnSelectedTextColor(0xFF999999);
        picker.setWheelModeEnable(false);
        LineConfig config = new LineConfig();
        config.setColor(Color.BLUE);//线颜色
        config.setAlpha(120);//线透明度
//        config.setRatio(1);//线比率
        picker.setLineConfig(config);
        picker.setItemWidth(200);
        picker.setBackgroundColor(0xFFE1E1E1);
        //picker.setSelectedItem(isChinese ? "处女座" : "Virgo");
        picker.setSelectedIndex(1);
        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                type=item;
            }
        });
        picker.show();
    }
}
