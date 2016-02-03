package vikicc.logistics.task;

import java.io.InputStream;

import vikicc.logistics.http.HttpXml;
import vikicc.logistics.model.HttpModel;
import vikicc.logistics.model.InvokeReturn;

/**
 *
 * Created by LiuWeiJie on 2015/8/8 0008.
 * Email:1031066280@qq.com
 */
public class LoginTask extends HttpTask {
    private LoginListener mListener;
    private HttpModel httpModel;

    public interface LoginListener {
        public HttpModel setHttpModel();

        public void onCheckResult(boolean result, InvokeReturn model);
    }

    public void start(LoginListener listener) {
        mListener = listener;
        if (mListener != null) {
            httpModel = listener.setHttpModel();
            start();
        }
    }

    @Override
    public void onInvokeReturn(boolean result, Object object) {
        if (mListener != null) {
            if (result) {
                if (object != null) {
                    InvokeReturn model= null;
                    try {
                        model = HttpXml.parseXml((InputStream) object, "UserModel");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mListener.onCheckResult(true, model);
                }
            } else {
                mListener.onCheckResult(false, null);
            }
        }
    }

    @Override
    public HttpModel getHttpModel() {
        return httpModel;
    }
}
