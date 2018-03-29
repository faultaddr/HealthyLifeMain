package cn.panyunyi.healthylife.app.server;

class StepEntity {
    String steps;
    String currentDate;
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
