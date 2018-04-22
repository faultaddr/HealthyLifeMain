package cn.panyunyi.healthylife.app.main.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class TimeUtil {
    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    public static String getCurrentDateDetail(){
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
