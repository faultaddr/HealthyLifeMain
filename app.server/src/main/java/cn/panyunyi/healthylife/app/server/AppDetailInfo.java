package cn.panyunyi.healthylife.app.server;

import android.os.Parcel;
import android.os.Parcelable;

public class AppDetailInfo implements Parcelable{

    public String mPackageName;
    public AppDetailInfo(String packageName){
        this.mPackageName=packageName;
    }

    protected AppDetailInfo(Parcel in) {
    }

    public static final Creator<AppDetailInfo> CREATOR = new Creator<AppDetailInfo>() {
        @Override
        public AppDetailInfo createFromParcel(Parcel in) {
            return new AppDetailInfo(in);
        }

        @Override
        public AppDetailInfo[] newArray(int size) {
            return new AppDetailInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPackageName);
    }
}
