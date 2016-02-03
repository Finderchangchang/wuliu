package vikicc.logistics.task;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

import vikicc.logistics.model.HttpModel;
import vikicc.logistics.model.InvokeReturn;

/**
 * 注册用户信息
 * <p>
 * Created by LiuWeiJie on 2015/8/8 0008.
 * Email:1031066280@qq.com
 */
public class RegUserTask extends HttpTask {
    private RegUserListener mListener;
    private HttpModel httpModel;

    public interface RegUserListener {
        public HttpModel setHttpModel();

        public void onCheckResult(boolean result, InvokeReturn model);
    }

    public void start(RegUserListener listener) {
        mListener = listener;
        if (mListener != null) {
            httpModel = listener.setHttpModel();
            start();
        }
    }

    //接收访问后台返回的内容
    @Override
    public void onInvokeReturn(boolean result, Object object) {
        if (mListener != null) {
            if (result) {
                if (object != null) {
                    InvokeReturn model = parse((InputStream) object, "UserModel");
                    mListener.onCheckResult(true, model);
                }
            } else {
                mListener.onCheckResult(false, null);
            }
        }
    }

    //根据输入流解析文件
    public InvokeReturn parse(InputStream is, String modelName) {
        InvokeReturn invokeReturn = null;
        try {
            XmlPullParser parser = Xml.newPullParser(); //由android.util.Xml创建一个XmlPullParser实例
            parser.setInput(is, "UTF-8");               //设置输入流 并指明编码方式

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    //声明List<CompanyModel>
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:
                        if (!parser.getName().equals("InvokeReturn")) {
                            invokeReturn = new InvokeReturn();
                            if (parser.getName().equals("Success")) {
                                parser.next();
                                invokeReturn.setSuccess(parser.getText().toString());
                            } else if (parser.getName().equals("Time")) {
                                parser.next();
                                invokeReturn.setTime(parser.getText().toString());
                            } else if (parser.getName().equals("Object")) {

                            } else {
                                if (parser.getName().equals("CompanyId")) {
                                    parser.next();
                                    Log.i("json-------", "CompanyId" + parser.getText().toString());

                                } else if (parser.getName().equals("CompanyPassword")) {
                                    parser.next();
                                    Log.i("json-------", "CompanyPassword" + parser.getText().toString());
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //清空List<CompanyModel>,CompanyModel
                        if (parser.getName().equals(" ")) {

                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invokeReturn;

    }


    @Override
    public HttpModel getHttpModel() {
        return httpModel;
    }
}
