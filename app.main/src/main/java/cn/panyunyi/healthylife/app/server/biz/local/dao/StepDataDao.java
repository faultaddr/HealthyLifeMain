package cn.panyunyi.healthylife.app.server.biz.local.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import cn.panyunyi.healthylife.app.server.db.DataBaseOpenHelper;
import cn.panyunyi.healthylife.app.server.biz.local.model.StepEntity;

public class StepDataDao {
    Context mContext;
    DataBaseOpenHelper helper;
    String sql="CREATE TABLE step(" +
            "currentDate VarChar(40)," +
            "steps VarChar(40), PRIMARY KEY(currentDate)" +
            ");";
    ArrayList<String>s=new ArrayList<>();
    public StepDataDao(Context applicationContext) {
        this.mContext=applicationContext;
        s.add(sql);
        helper=DataBaseOpenHelper.getInstance(mContext,"step",1,s);
    }

    public StepEntity getCurrentDataByDate(String currentDate) {
        /*
        * TODO 向后端请求 currentDate 对应的步数。
        *
        * */
        StepEntity entity=new StepEntity();
        Cursor cursor=  helper.query("step","where currentDate='"+currentDate+"'");
        if (cursor.getCount()==0) {
            //为空的Cursor
            return null;
        }
        while(cursor.moveToNext()) {
            //光标移动成功
            //把数据取出
           int datePos= cursor.getColumnIndex("currentDate");
           int stepsPos=cursor.getColumnIndex("steps");
           entity.currentDate=cursor.getString(datePos);
           entity.steps=cursor.getString(stepsPos);
        }
        return entity;
    }

    public void addNewData(StepEntity entity) {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("currentDate",entity.currentDate);
        contentvalues.put("steps",entity.steps);
        helper.insert("step",contentvalues);
    }

    public void updateCurrentData(StepEntity entity) {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("currentDate",entity.currentDate);
        contentvalues.put("steps",entity.steps);
        helper.update("step",contentvalues,"currentDate=?",new String[]{entity.currentDate});
    }
}
