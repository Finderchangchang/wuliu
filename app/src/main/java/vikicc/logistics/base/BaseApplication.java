package vikicc.logistics.base;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;

/**
 * 作者:柳伟杰 邮件:1031066280@qq.com
 * 创建时间:15/6/21 下午10:13
 * 描述:
 */
public class BaseApplication extends Application {
    private static BaseApplication sInstance;
    private static Context context;
    public BaiduMapLocationService locationService;
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        context = getApplicationContext();
        locationService = new BaiduMapLocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
    }

    public static Context getContext() {
        return context;
    }

    public static BaseApplication getInstance() {
        return sInstance;
    }

    //系统处于资源匮乏的状态
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}