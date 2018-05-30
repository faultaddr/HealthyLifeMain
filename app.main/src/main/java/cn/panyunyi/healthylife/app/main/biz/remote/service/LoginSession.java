package cn.panyunyi.healthylife.app.main.biz.remote.service;


import cn.panyunyi.healthylife.app.main.biz.remote.model.MUserEntity;
import cn.panyunyi.healthylife.app.main.util.ACache;

/**
 * Created by panyu on 2017/6/9.
 */

public class LoginSession {
    volatile static LoginSession sLoginSession = null;
    private MUserEntity loginedUser;

    private LoginSession() {
    }

    public static LoginSession getLoginSession() {
        if (sLoginSession == null) {
            synchronized (LoginSession.class) {
                if (sLoginSession == null) {
                    sLoginSession = new LoginSession();
                }

            }
        }
        return sLoginSession;
    }

    public void exit() {
        sLoginSession = null;
    }

    void setsLoginSession(MUserEntity user) {
        loginedUser = user;
    }

    public MUserEntity getLoginedUser() {
        return loginedUser;
    }
}
