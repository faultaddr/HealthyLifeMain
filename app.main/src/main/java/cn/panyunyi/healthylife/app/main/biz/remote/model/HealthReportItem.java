package cn.panyunyi.healthylife.app.main.biz.remote.model;

/*
 *create by panyunyi on 2018/5/10
 */
public class HealthReportItem {
    public int code;
    public String msg;
    public HealthReportItemData data;
    public static class HealthReportItemData{
        public int userId;
        public float beatDegree;
        public float stepDegree;
        public float generalDegree;
        public String reportMsg;
    }
}
