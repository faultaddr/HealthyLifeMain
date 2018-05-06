package cn.panyunyi.healthylife.app.main.biz.local.model;

public class StepEntity {
    public String steps;
    public String currentDate;
    public int userId;

    public String getSteps() {
        return this.steps;
    }

    public void setSteps(String s) {
        this.steps = s;
    }

    public void setCurDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
