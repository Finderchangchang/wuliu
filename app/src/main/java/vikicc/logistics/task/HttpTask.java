package vikicc.logistics.task;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.apache.http.conn.HttpHostConnectException;

import vikicc.logistics.http.HttpUtil;
import vikicc.logistics.model.HttpModel;

/**
 * 通用的HttpTask
 * <p/>
 * Created by LiuWeiJie on 2015/8/10 0010.
 * Email:1031066280@qq.com
 */
public abstract class HttpTask extends Thread {
    public abstract HttpModel getHttpModel();

    public abstract void onInvokeReturn(boolean result, Object object);

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != 0) {
                onInvokeReturn(false, null);
            } else {
                onInvokeReturn(true, msg.obj);
            }
        }
    };

    @Override
    public void run() {
        Looper.prepare();
        try {
            if (getHttpModel() != null) {
                if (getHttpModel().getIsPost()) {
                    handler.obtainMessage(0, HttpUtil.doPost(getHttpModel())).sendToTarget();
                } else {
                    handler.obtainMessage(0, HttpUtil.doGet(getHttpModel())).sendToTarget();
                }
            }

        }catch(Exception e) {
            handler.obtainMessage(1).sendToTarget();
        }
    }
}
