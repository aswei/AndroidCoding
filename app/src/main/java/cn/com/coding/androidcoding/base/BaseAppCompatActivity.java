package cn.com.coding.androidcoding.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MenuItem;

import butterknife.ButterKnife;
import cn.com.coding.androidcoding.R;
import cn.com.coding.androidcodinglibrary.view.widget.StatusBarCompat;
import cn.com.coding.androidcodinglibrary.widget.dialog.LoadingDialog;

/**
 * Created by
 * 作者：李鹏伟 on 2017-2-28.
 * 邮箱：lipengwei@fu-rui.com
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity{

    //屏幕信息
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0f;

    protected Context mContext =null;

    protected Toolbar mToolbar;
    public LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        super.onCreate(savedInstanceState);

        //沉浸式状态栏所需
        StatusBarCompat.compat(this);
//        StatusBarCompat.compat(this, getResources().getColor(R.color.colorAccent));

        mContext = this;
        BaseAppManager.getInstance().addActivity(this);

        getScreen();

        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }

        initViewsAndEvents();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        mToolbar = ButterKnife.findById(this,R.id.common_toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);//设置 左上角可点击
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置左上角返回加上图标
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //初始化ToolBar
    public void initToolbar(Toolbar toolbar){
        mToolbar = toolbar;
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    //获取屏幕信息数据
    private void getScreen() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenDensity = displayMetrics.density;
    }

    /**
     * 设置资源布局文件
     *
     * @return id of layout resource
     */
    protected abstract int getContentViewLayoutID();

    /**
     * 初始化控件
     */
    protected abstract void initViewsAndEvents();

    @Override
    public void finish() {
        super.finish();
        BaseAppManager.getInstance().removeActivity(this);
        overridePendingTransition(R.anim.right_in,R.anim.right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*
    * startActivity 启动一个activity
    * */
    protected void startActivity(Class<?> clazz){
        Intent mIntent = new Intent(this, clazz);
        startActivity(mIntent);
    }

    /**
     * startActivity 启动一个带bundle的activity
     * @param clazz
     * @param bundle
     */
    protected void startActivity(Class<?> clazz , Bundle bundle){
        Intent mIntent = new Intent(this,clazz);
        if (null !=bundle) {
            mIntent.putExtras(bundle);
        }
        startActivity(mIntent);
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected void startActivityForResult(Class<?> clazz,int requestCode){
        Intent mIntent = new Intent(this,clazz);
        startActivityForResult(mIntent,requestCode);

    }

    /**
     * startActivityForResult 带数据
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void startActivityForResult(Class<?> clazz, int requestCode, Bundle bundle){
        Intent mIntent =new Intent(this,clazz);
        if (null != bundle) {
            mIntent.putExtras(bundle);
        }
        startActivityForResult(mIntent,requestCode);
    }


    /**
     * 显示刷新的Loading
     */
    public void showLoadingDialog(){
        try {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setTitle(null);
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        hideLoadingDialog();
                    }
                    return true;
                }
            });
            if (!isFinishing()) {
                mLoadingDialog.show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 隐藏刷新的Loading
     */
    private void hideLoadingDialog() {
        try {
            if (mLoadingDialog != null) {
                if (mLoadingDialog.animation != null) {
                    mLoadingDialog.animation.reset();
                }
                mLoadingDialog.dismiss();
                mLoadingDialog =null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Toolbar getToolBar(){
        return mToolbar;
    }

}
