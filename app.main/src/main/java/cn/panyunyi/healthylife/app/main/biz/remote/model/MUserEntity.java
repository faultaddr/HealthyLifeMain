package cn.panyunyi.healthylife.app.main.biz.remote.model;

/**
 * Created by panyu on 2018/4/25.
 */
public class MUserEntity {
    private String userName;
    private String userPassword;
    private String picSrc;
    private int userId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getPicSrc() {
        return picSrc;
    }

    public void setPicSrc(String picSrc) {
        this.picSrc = picSrc;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MUserEntity that = (MUserEntity) o;

        if (userId != that.userId) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null)
            return false;
        if (userPassword != null ? !userPassword.equals(that.userPassword) : that.userPassword != null)
            return false;
        if (picSrc != null ? !picSrc.equals(that.picSrc) : that.picSrc != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (userPassword != null ? userPassword.hashCode() : 0);
        result = 31 * result + (picSrc != null ? picSrc.hashCode() : 0);
        result = 31 * result + userId;
        return result;
    }
}
