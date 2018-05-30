package cn.panyunyi.healthylife.app.main.biz.local.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.panyunyi.healthylife.app.main.biz.local.model.BeatEntity;
import cn.panyunyi.healthylife.app.main.biz.local.model.PressEntity;
import cn.panyunyi.healthylife.app.main.db.DataBaseOpenHelper;

public class PressDataDao {



        private static PressDataDao dao;
        Context mContext;
        DataBaseOpenHelper helper;
        String sql = "CREATE TABLE press(" +
                "date VarChar(40)," + "press VarChar(10)," +
                "userId int(11), PRIMARY KEY(date)" +
                ");";
        ArrayList<String> s = new ArrayList<>();
        private String TAG = "PressDataDao";

        public PressDataDao(Context applicationContext) {
            this.mContext = applicationContext;
            s.add(sql);
            helper = DataBaseOpenHelper.getInstance(mContext, "press", 1, s);
        }

        public static PressDataDao getInstance(Context context) {
            if (dao == null) {
                return new PressDataDao(context);
            } else {
                return dao;
            }
        }

        public PressEntity getDataByDate(String currentDate) {
            /*
             * TODO 向后端请求 currentDate 对应的步数。
             *
             * */
            PressEntity entity=new PressEntity();
            Cursor cursor = helper.query("press", "where date='" + currentDate + "'");
            if (cursor.getCount() == 0) {
                //为空的Cursor
                return null;
            }
            while (cursor.moveToNext()) {
                //光标移动成功
                //把数据取出
                int datePos = cursor.getColumnIndex("date");
                int pressPos = cursor.getColumnIndex("press");
                int userPos = cursor.getColumnIndex("userId");
                entity.date = cursor.getString(datePos);
                entity.press = cursor.getString(pressPos);
                entity.userId = cursor.getInt(userPos);
            }
            return entity;
        }

        public List<PressEntity> getAllPress() {
            List<PressEntity> beatList = new ArrayList<>();

            Cursor cursor = helper.query("press", "");

            if (cursor.getCount() == 0) {
                //为空的Cursor
                return null;
            }
            while (cursor.moveToNext()) {
                PressEntity entity=new PressEntity();
                int datePos = cursor.getColumnIndex("date");
                int pressPos = cursor.getColumnIndex("press");
                int userPos = cursor.getColumnIndex("userId");
                entity.date = cursor.getString(datePos);
                entity.press = cursor.getString(pressPos);
                entity.userId = cursor.getInt(userPos);

                beatList.add(0, entity);
            }
            return beatList;

        }

        public void addNewData(PressEntity entity) {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put("date", entity.date);
            contentvalues.put("press", entity.press);
            contentvalues.put("userId", entity.userId);
            helper.insert("press", contentvalues);
        }

        public void updateCurrentData(PressEntity entity) {
            ContentValues contentvalues = new ContentValues();
            contentvalues.put("date", entity.date);
            contentvalues.put("press", entity.press);
            contentvalues.put("userId", entity.userId);
            helper.update("press", contentvalues, "date=?", new String[]{entity.date});
        }
    }



