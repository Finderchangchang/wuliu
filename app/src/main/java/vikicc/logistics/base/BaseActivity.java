package vikicc.logistics.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;

import com.ivsign.android.IDCReader.CvrManager;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import Decoder.BASE64Encoder;
import vikicc.custom.method.VikiccUtils;
import vikicc.logistics.http.HttpUtil;
import vikicc.logistics.model.ExpressModel;
import vikicc.logistics.model.HttpModel;
import vikicc.logistics.model.ImageObject;
import vikicc.logistics.model.InvokeReturn;
import vikicc.logistics.model.TerminalModel;
import vikicc.logistics.task.AddExpressTask;

/**
 * BaseActivity声明相关通用方法
 * <p/>
 * Created by LiuWeiJie on 2015/7/22 0022.
 * Email:1031066280@qq.com
 */
public abstract class BaseActivity extends FinalActivity {

    public VikiccUtils vUtils;
    public HttpUtil httpUtil;
    public HttpModel httpModel;
    private String mSessionId;
    private String mUserId;
    private String mPassword;
    private String mIp;
    private String mPort;
    public FinalDb finalDb;
    public TerminalModel mobileConfigModel = new TerminalModel();
    public CvrManager cvrManager;
    String check_time;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("----------------", "onCreate");
        finalDb = FinalDb.create(BaseActivity.this);
        vUtils = new VikiccUtils();
        httpUtil = new HttpUtil();
        httpModel = new HttpModel();

        cvrManager = new CvrManager();
        check_time = VikiccUtils.ReadString(getBaseContext(), VikiccUtils.CHECK_UPLOAD_TIME);
        if (check_time == "") {
            check_time = "5";
        } else {
            check_time = check_time.replaceAll("分", "");
        }
        initViews();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        initEvents();
    }

    public HttpModel getHttpModel() {
        if (finalDb.findAll(TerminalModel.class).size() > 0) {
            mobileConfigModel = finalDb.findAll(TerminalModel.class).get(0);
            mIp = mobileConfigModel.getTerminalIP();
            mPort = mobileConfigModel.getTerminalCount();
        }
        mSessionId = VikiccUtils.ReadString(this, VikiccUtils.KEY_SESSIONID);
        mUserId = "Admin";
        try {
            mPassword = Utils.hashPassword("1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // mIp = VikiccUtils.ReadString(this, KeyModel.KEY_IP);
        // mPort = VikiccUtils.ReadString(this, KeyModel.KEY_PORT);
        if (!VikiccUtils.isEmptyString(mSessionId)) {
            httpModel.setSessionId(mSessionId);
        }
        if (!VikiccUtils.isEmptyString(mUserId)) {
            httpModel.setUserId(mUserId);
        }
        if (!VikiccUtils.isEmptyString(mPassword)) {
            httpModel.setPassword(mPassword);
        }
        if (!VikiccUtils.isEmptyString(mIp)) {
            if (!VikiccUtils.isEmptyString(mPort)) {
                httpModel.setURL("http://" + mIp + ":" + mPort + "/Service/Index.aspx");
            } else {
                httpModel.setURL(mIp);
            }
        }
        return httpModel;
    }

    public abstract void initViews();

    public abstract void initEvents();

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    boolean bool_thread = true;
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            while (bool_thread) {
                try {
                    Log.i("check_time", check_time);
                    Thread.sleep(Integer.parseInt(check_time) * 60000);
                    if (Utils.isNetworkAvailable(BaseActivity.this)) {
                        List<ExpressModel> list = finalDb.findAllByWhere(ExpressModel.class, "IsDownLoad=0");
                        list.size();
                        for (ExpressModel e : list) {
                            ImageObject[] imgs = new ImageObject[3];

                            ImageObject imageObject1 = new ImageObject();
                            imageObject1.setImageId("ConsignIdentityImageId");
                            if (!VikiccUtils.isEmptyString(e.getConsignIdentityImageId())) {
                                imageObject1.setImageData(Utils.getBitmapFromByte(Base64.decode(e.getConsignIdentityImageId(),Base64.DEFAULT)));
                            }
                            ImageObject imageObject2 = new ImageObject();
                            imageObject2.setImageId("GoodsImageId");
                            imageObject2.setImageData(Utils.getBitmapFromByte(Base64.decode(e.getGoodsImageId(),Base64.DEFAULT)));
                            ImageObject imageObject3 = new ImageObject();
                            imageObject3.setImageId("OrderImageId");
                            imageObject3.setImageData(Utils.getBitmapFromByte(Base64.decode(e.getOrderImageId(),Base64.DEFAULT)));
//                            if (!VikiccUtils.isEmptyString(e.getConsignIdentityImageId())) {
//                                imageObject1.setImageData(Utils.getBitmapFromByte(e.getConsignIdentityImageId().getBytes()));
//                            }
//                            imageObject2.setImageData(Utils.getBitmapFromByte(e.getGoodsImageId().getBytes()));
//                            imageObject3.setImageData(Utils.getBitmapFromByte(e.getOrderImageId().getBytes()));
                            imgs[0]=imageObject1;
                            imgs[1]=imageObject2;
                            imgs[2]=imageObject3;
                            e.setImages(imgs);
                            AddExpress(e);
                        }
                    }
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);// 发送消息
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Looper.loop();
        }
    };

    /*添加物流信息*/
    private void AddExpress(final ExpressModel expressModel) {
        new AddExpressTask().start(new AddExpressTask.AddExpressListener() {
            public void onCheckResult(boolean result, InvokeReturn invokeReturn) {
                expressModel.setIsDownLoad(false);
                if (invokeReturn != null) {
                    if (invokeReturn.getSuccess().equals("true")) {
                        //VikiccUtils.ToastShort(MainActivity.mIntaile, "信息上传成功！");
                        expressModel.setIsDownLoad(true);
                        finalDb.update(expressModel);
                        System.out.println("success.....");
                    } else {
                        // VikiccUtils.ToastShort(MainActivity.mIntaile, "信息上传失败");
                    }
                } else {

                    // VikiccUtils.ToastShort(MainActivity.mIntaile, "保存成功！网络错误");
                    System.out.println("filed.....");
                }


            }

            public HttpModel setHttpModel() {
                httpModel = getHttpModel();
                httpModel.setIsPost(true);
                try {
                    String xml = new BASE64Encoder().encode(expressModel.Serialization().getBytes("UTF-8"));
                    xml = xml.replaceAll("\\+", "%2B");
                    xml = xml.replaceAll("\\=", "%3D");
                    httpModel.setInformation(String.format("Express=%s", xml));
                    System.out.println("new base:" + expressModel.Serialization());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                httpModel.setObjective("MobileAddCompanyExpress");
                return httpModel;
            }
        });

    }

    /*activity销毁处理*/
    @Override
    protected void onDestroy() {
        bool_thread = false;
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        bool_thread = false;
        handler.removeCallbacks(mRunnable);
        super.onStop();
    }

    @Override
    protected void onStart() {
        bool_thread = true;
        new Thread(mRunnable).start();
        super.onStart();
    }

}
