package cn.panyunyi.healthylife.app.server;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

class StepDataDao {
    Context mContext;
    String sql="CREATE TABLE healthylifemain.step(" +
            "   currentDate VarChar(40)   PRIMARY KEY(currentDate)," +
            "   steps VarChar(40)" +
            ");";
    ArrayList<String>s=new ArrayList<>();
    public StepDataDao(Context applicationContext) {
        this.mContext=applicationContext;
        s.add(sql);
    }

    public StepEntity getCurDataByDate(String currentDate) {
        /*
        * TODO 向后端请求 currentDate 对应的步数。
        *
        * */
        DataBaseOpenHelper.getInstance(mContext,"step",1,s);

    }

    public void addNewData(StepEntity entity) {
    }

    public void updateCurData(StepEntity entity) {
    }
}
