package cn.panyunyi.healthylife.app.main.biz.local.dao;

import android.content.Context;

import cn.panyunyi.healthylife.app.main.biz.local.model.BeatEntity;
import cn.panyunyi.healthylife.app.main.db.DataBaseOpenHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/*
 *create by panyunyi on2018/4/19
 */


public class BeatDataDao {
    private String TAG = "BeatDataDao";
    private static BeatDataDao dao;
    Context mContext;
    DataBaseOpenHelper helper;
    String sql = "CREATE TABLE beat(" +
            "currentDate VarChar(40)," + "timeCount VarChar(10)," +
            "beats VarChar(40), PRIMARY KEY(currentDate)" +
            ");";
    ArrayList<String> s = new ArrayList<>();

    public static BeatDataDao getInstance(Context context) {
        if (dao == null) {
            return new BeatDataDao(context);
        } else {
            return dao;
        }
    }


    public BeatDataDao(Context applicationContext) {
        this.mContext = applicationContext;
        s.add(sql);
        helper = DataBaseOpenHelper.getInstance(mContext, "beat", 1, s);
    }

    public BeatEntity getCurrentDataByDate(String currentDate) {
        /*
         * TODO 向后端请求 currentDate 对应的步数。
         *
         * */
        BeatEntity beatEntity = new BeatEntity();
        Cursor cursor = helper.query("beat", "where currentDate='" + currentDate + "'");
        if (cursor.getCount() == 0) {
            //为空的Cursor
            return null;
        }
        while (cursor.moveToNext()) {
            //光标移动成功
            //把数据取出
            int datePos = cursor.getColumnIndex("currentDate");
            int beatsPos = cursor.getColumnIndex("beats");
            int timePos = cursor.getColumnIndex("timeCount");
            beatEntity.currentDate = cursor.getString(datePos);
            beatEntity.beats = cursor.getString(beatsPos);
            beatEntity.timeCount = cursor.getString(timePos);
        }
        return beatEntity;
    }

    public List<BeatEntity> getAllBeats() {
        List<BeatEntity> beatList = new ArrayList<>();

        Cursor cursor = helper.query("beat", "");

        if (cursor.getCount() == 0) {
            //为空的Cursor
            return null;
        }
        while (cursor.moveToNext()) {
            BeatEntity beatEntity = new BeatEntity();
            int datePos = cursor.getColumnIndex("currentDate");
            int beatsPos = cursor.getColumnIndex("beats");
            int timePos = cursor.getColumnIndex("timeCount");
            beatEntity.currentDate = cursor.getString(datePos);
            beatEntity.beats = cursor.getString(beatsPos);
            beatEntity.timeCount = cursor.getString(timePos);
            Log.i(TAG, beatEntity.beats);
            beatList.add(0,beatEntity);
        }
        return beatList;

    }

    public void addNewData(BeatEntity entity) {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("currentDate", entity.currentDate);
        contentvalues.put("beats", entity.beats);
        contentvalues.put("timeCount", entity.timeCount);
        helper.insert("beat", contentvalues);
    }

    public void updateCurrentData(BeatEntity entity) {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("currentDate", entity.currentDate);
        contentvalues.put("beats", entity.beats);
        contentvalues.put("timeCount", entity.timeCount);
        helper.update("beat", contentvalues, "currentDate=?", new String[]{entity.currentDate});
    }
}

