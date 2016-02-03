package vikicc.logistics.ui.activity;


import android.os.Handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vikicc.custom.method.VikiccUtils;
import vikicc.logistics.R;
import vikicc.logistics.base.BaseActivity;
import vikicc.logistics.base.Utils;
import vikicc.logistics.data.XmlManager;
import vikicc.logistics.model.CodeModel;
import vikicc.logistics.model.HttpModel;
import vikicc.logistics.model.InvokeReturn;
import vikicc.logistics.model.TerminalModel;
import vikicc.logistics.task.GetCodesTask;

/**
 * app运行时启动页面
 */
public class LoadingActivity extends BaseActivity {

    public static LoadingActivity mIntaile;
    XmlManager xml;
    String[] Codes;
    boolean isLoading = true;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_loading);
        try {
            if (VikiccUtils.ReadString(this, VikiccUtils.KEY_PULLTOBACK) != null) {

            }
        } catch (Exception e) {
            VikiccUtils.WriteString(this, VikiccUtils.KEY_PULLTOBACK, "1000");
        }
        mIntaile = this;
        xml = new XmlManager(this);
        Utils.CopyWltlib(this);
    }

    @Override
    public void initEvents() {
        List<TerminalModel> list = finalDb.findAll(TerminalModel.class);
        if (list != null) {
            if (list.size() > 0) {
                mobileConfigModel = list.get(0);
                if (VikiccUtils.isEmptyString(mobileConfigModel.getTerminalIP()) ||
                        VikiccUtils.isEmptyString(mobileConfigModel.getTerminalCount())) {
                    VikiccUtils.ToastShort(this, "请设置ip与端口");
                    VikiccUtils.IntentPost(this, SettingHttpActivity.class, null);//跳转到注册页面
                    finish();
                } else if (VikiccUtils.isEmptyString(mobileConfigModel.getTerminalId())) {
                    VikiccUtils.ToastShort(this, "请注册设备");
                    VikiccUtils.IntentPost(this, RegUserActivity.class, null);//跳转到注册页面
                    finish();
                } else {
                    GetRefushTime();//更新字典
                }
            } else {
                VikiccUtils.IntentPost(this, SettingHttpActivity.class, null);//跳转到配置页面
                finish();
            }
        } else {
            VikiccUtils.IntentPost(this, SettingHttpActivity.class, null);//跳转到配置页面
            finish();
        }
    }

    /*Loading加载完成以后跳转到Login页面*/
    private void PostToLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                VikiccUtils.IntentPost(LoadingActivity.this, LoginActivity.class, null);
                finish();
            }
        }, 2000);
    }

    /*读取字典变更时间*/
    private void GetRefushTime() {
        //判断是否需要更新
        new GetCodesTask().start(new GetCodesTask.GetCodesListener() {
            @Override
            public void onCheckResult(boolean result, InvokeReturn invokeReturn) {
                if (result) {
                    InvokeReturn ir = invokeReturn;
                    if (ir != null) {
                        if (ir.getSuccess().equals("true")) {
                            isRefush(ir.getRtime());
                        } else {
                            VikiccUtils.ToastShort(LoadingActivity.mIntaile, "字典更新失败！");
                            PostToLogin();
                        }
                    } else {
                        VikiccUtils.ToastShort(LoadingActivity.mIntaile, "字典更新失败！");
                        PostToLogin();
                    }
                } else {
                    VikiccUtils.ToastShort(LoadingActivity.mIntaile, "字典更新失败！");
                    PostToLogin();
                }
            }

            @Override
            public HttpModel setHttpModel() {
                httpModel = getHttpModel();
                httpModel.setIsPost(true);
                httpModel.setObjective("MobileGetLastModified");
                return httpModel;
            }
        });
    }


    /*判断变更时间，是否还需要更新*/
    private void isRefush(String btime) {
        int result;
        Date time = null;
        Date rtime = null;
        String refushtime = VikiccUtils.ReadString(this, "RefushCodeTime");//获得数据库存储的时间
        if (refushtime != null) {
            //取出时间，跟变动时间比较
            rtime = VikiccUtils.StringToDate(refushtime);
        } else {//保存当前更新时间
            rtime = new Date();
            VikiccUtils.WriteString(this, "RefushCodeTime", VikiccUtils.DateToString(rtime));
        }
        String stime = btime.substring(0, 10) + " " + btime.substring(11, 19);
        result = VikiccUtils.StringToDate(stime).compareTo(rtime);
        if (result < 0) {
            Codes = this.getResources().getStringArray(R.array.CodeName);
            if (Codes.length > 0) {
                GetCode(Codes[0], 0);
            }
            VikiccUtils.WriteString(this, "RefushCodeTime", VikiccUtils.DateToString(time));//内存写入数据
        } else {
            PostToLogin();//跳转到登录页面
        }
    }

    /*读取网络字典*/
    private void GetCode(final String name, final int i) {
        new GetCodesTask().start(new GetCodesTask.GetCodesListener() {
            @Override
            public void onCheckResult(boolean result, InvokeReturn invokeReturn) {
                InvokeReturn ir = invokeReturn;
                if (result) {
                    if (invokeReturn != null) {
                        if (invokeReturn.getSuccess().equals("true")) {
                            //保存到本地xml
                            List<CodeModel> list = new ArrayList<CodeModel>();
                            for (int i = 0; i < ir.getListModel().size(); i++) {
                                list.add((CodeModel) ir.getListModel().get(i));
                            }
                            XmlManager xml = new XmlManager(LoadingActivity.mIntaile);
                            xml.createXml(list, name + ".xml");
                            if (i < Codes.length) {
                                GetCode(Codes[i], i + 1);
                            }
                        } else {
                            isLoading = false;
                        }
                    }
                }
                if (name.equals("Code_PersonnelType")) {
                    if (!isLoading) {
                        VikiccUtils.ToastShort(LoadingActivity.this, "字典更新失败！");
                    }
                    VikiccUtils.IntentPost(LoadingActivity.this, LoginActivity.class, null);
                    finish();
                }
            }

            @Override
            public HttpModel setHttpModel() {
                httpModel = getHttpModel();
                httpModel.setIsPost(true);
                httpModel.setInformation("CodeName=" + name);
                httpModel.setObjective("GetCode");
                return httpModel;
            }
        });
    }
}
