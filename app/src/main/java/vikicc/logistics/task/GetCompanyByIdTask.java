package vikicc.logistics.task;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

import vikicc.logistics.http.HttpXml;
import vikicc.logistics.model.CompanyModel;
import vikicc.logistics.model.HttpModel;
import vikicc.logistics.model.InvokeReturn;

/**
 * 根据ID获得公司信息
 * <p/>
 * Created by LiuWeiJie on 2015/8/6 0006.
 * Email:1031066280@qq.com
 */
public class GetCompanyByIdTask extends HttpTask {
    private GetCompanyByIdListener mListener;
    private HttpModel model;

    public interface GetCompanyByIdListener {
        public void onCheckResult(boolean result, CompanyModel companyModel);

        public HttpModel setHttpModel();
    }

    public void start(GetCompanyByIdListener listener) {
        mListener = listener;
        if (mListener != null) {
            model = mListener.setHttpModel();
            start();
        }
    }

    @Override
    public void onInvokeReturn(boolean result, Object object) {
        if (mListener != null) {
            if (result) {
                if (object != null) {
                    InvokeReturn model = null;
                    try {
                        model = HttpXml.parseXml((InputStream) object, "CompanyModel");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mListener.onCheckResult(true, (CompanyModel)model.getListModel().get(0));
                }
            } else {
                mListener.onCheckResult(false, null);
            }
        }
    }

    //设置HttpModel
    @Override
    public HttpModel getHttpModel() {
        return model;
    }

    //根据输入流解析文件
    public CompanyModel parse(InputStream is) {
        CompanyModel model = null;
        try {
            if (is == null) {
                Log.i("--------------", "空");
            }
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
                            model = new CompanyModel();
                            if (parser.getName().equals("Success")) {
                                parser.next();
                                Log.i("json-------", "Success:" + parser.getText().toString());
                            } else if (parser.getName().equals("Time")) {
                                parser.next();
                                Log.i("json-------", "Time:" + parser.getText().toString());
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
                        if (parser.getName().equals("Object")) {

                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;

    }
}
