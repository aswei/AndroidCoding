package cn.com.coding.androidcoding.app;

import android.app.Application;
import android.content.Context;

import cn.com.coding.androidcodinglibrary.AppUtils;

/**
 * Created by
 * 作者：aswei on 2017-2-28.
 * 邮箱：lipswei@gmail.com
 */

public class baseApplication extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        AppUtils.init(mContext);
        //CrashHandler.getInstance().init(mContext);
    }

    public static Context getAppContext() {
        return mContext;
    }
}
