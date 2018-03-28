package cn.panyunyi.healthylife.app.server;

import cn.panyunyi.healthylife.app.server.AppDetailInfo;
interface InfoService {
    AppDetailInfo getAppDetailInfo(String packageName);
}
