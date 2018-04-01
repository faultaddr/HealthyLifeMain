package cn.panyunyi.healthylife.app.server.biz.local.model;

public class StepEntity {
    public String steps;
    public String currentDate;
    public String getSteps() {
        return this.steps;
    }

    public void setCurDate(String currentDate) {
        this.currentDate=currentDate;
    }

    public void setSteps(String s) {
        this.steps=s;
    }
}
