package cn.com.coding.androidcoding.base;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by
 * 作者：aswei on 2017-2-28.
 * 邮箱：lipswei@fu-rui.com
 *
 * 管理activity的Manager
 */

public class BaseAppManager  {

    private static BaseAppManager instance = null ;
    private static List<Activity> mActivities = new LinkedList<Activity>();

    private BaseAppManager(){

    }

    public static BaseAppManager getInstance(){
        if (instance == null) {
            synchronized (BaseAppManager.class){
                if (null == instance) {
                    instance =new BaseAppManager();
                }
            }
        }
        return  instance;
    }

    public int size(){
        return  mActivities.size();
    }

    public synchronized Activity getForWardActivity(){
        return size() >0 ?mActivities.get(size() - 1) : null ;
    }

    public synchronized  void addActivity(Activity activity){
        mActivities.add(activity);
    }

    public synchronized void removeActivity(Activity activity){
        if (mActivities.contains(activity)) {
            mActivities.remove(activity);
        }
    }

    public synchronized void clear(){
        for (int i = mActivities.size() - 1; i > -1; i--){
            Activity activity = mActivities.get(i);
            removeActivity(activity);
            activity.finish();
            i = mActivities.size();
        }
    }

    public synchronized void clearTop(){
        for (int i =mActivities.size() - 2; i > -1; i--){
            Activity activity = mActivities.get(i);
            removeActivity(activity);
            activity.finish();
            i = mActivities.size() - 1;
        }
    }
}
