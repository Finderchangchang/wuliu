package vikicc.logistics.task;

import java.io.InputStream;

import vikicc.logistics.http.HttpXml;
import vikicc.logistics.model.HttpModel;
import vikicc.logistics.model.InvokeReturn;

/**
 * Created by Administrator on 2015/8/22.
 */
public class UpdatePassWordTask extends HttpTask {
    private UpdatePassWordListener mListener;
    private HttpModel httpModel;

    public interface UpdatePassWordListener {
        public void onCheckResult(boolean result, InvokeReturn invokeReturn);

        public HttpModel setHttpModel();
    }

    public void start(UpdatePassWordListener listener) {
        mListener = listener;
        if (mListener != null) {
            httpModel = mListener.setHttpModel();
            start();
        }
    }

    @Override
    public HttpModel getHttpModel() {
        return httpModel;
    }

    @Override
    public void onInvokeReturn(boolean result, Object object) {
        if (mListener != null) {
            if (result) {
                if (object != null) {
                    InvokeReturn model = null;
                    try {
                         model = HttpXml.parseXml((InputStream) object, "ExpressModel");
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
}
