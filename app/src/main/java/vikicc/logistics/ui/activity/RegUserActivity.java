package vikicc.logistics.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Decoder.BASE64Encoder;
import vikicc.custom.method.VikiccUtils;
import vikicc.logistics.R;
import vikicc.logistics.base.BaseActivity;
import vikicc.logistics.base.Utils;
import vikicc.logistics.data.XmlManager;
import vikicc.logistics.model.CodeModel;
import vikicc.logistics.model.HttpModel;
import vikicc.logistics.model.InvokeReturn;
import vikicc.logistics.model.PersonnelModel;
import vikicc.logistics.model.TerminalModel;
import vikicc.logistics.task.CheckConfigTask;
import vikicc.logistics.task.GetCodesTask;
import vikicc.logistics.task.GetPersonnelTask;

/**
 * 设置页面
 */
public class RegUserActivity extends BaseActivity {
    @CodeNote(id = R.id.total_top_ll_left, click = "onClick")
    LinearLayout title_ll_left;
    @CodeNote(id = R.id.total_top_ll_right, click = "onClick")
    LinearLayout ll_right;
    @CodeNote(id = R.id.total_top_txt_title)
    TextView txt_total_title;
    @CodeNote(id = R.id.total_top_txt_right)
    TextView txt_right;
    @CodeNote(id = R.id.total_top_txt_left)
    TextView title_tv_left;
    @CodeNote(id = R.id.setting_reg, click = "onClick")
    Button reg;
    @CodeNote(id = R.id.setting_equipment_code)
    EditText txt_imei;
    @CodeNote(id = R.id.setting_person_code)
    EditText setting_person_code;//人员编码
    @CodeNote(id = R.id.setting_company_code)
    EditText setting_company_code;//企业编码
    @CodeNote(id = R.id.setting_password)
    EditText setting_password;//用户密码
    String imei;
    public static RegUserActivity mIntaile;
    TerminalModel mobile = new TerminalModel();
    boolean isLogin = true;//是不是登陆
    String[] Codes;
    Dialog dialog;
    String isLogin_Reg;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_reg_user);
        mIntaile = this;
        isLogin_Reg = getIntent().getStringExtra(VikiccUtils.BACK_Login_TO_RegUser);
        imei = VikiccUtils.getTelephonyManager(this).getDeviceId();
        if (finalDb.findAll(TerminalModel.class).size() > 0) {
            mobile = finalDb.findAll(TerminalModel.class).get(0);
        }
    }

    @Override
    public void initEvents() {
        ll_right.setVisibility(View.VISIBLE);
        title_ll_left.setVisibility(View.VISIBLE);
        title_tv_left.setText("返回");
        txt_total_title.setText("用户注册");
        txt_right.setText("网络设置");
        txt_imei.setText("设备编码：" + imei);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_reg:
                List<TerminalModel> list = finalDb.findAll(TerminalModel.class);
                if (list != null) {
                    if (list.size() > 0)
                        mobile = list.get(0);
                }
                if (!VikiccUtils.isEmptyString(mobile.getTerminalIP())) {//ip不为空，执行登录
                    checkData();
                } else {
                    VikiccUtils.ToastShort(this, "请设置ip与端口");
                    VikiccUtils.IntentPost(this, SettingHttpActivity.class, null);
                }
                break;
            case R.id.total_top_ll_left:
                finish();
                break;
            case R.id.total_top_ll_right:
                VikiccUtils.IntentPost(this, SettingHttpActivity.class, new VikiccUtils.putListener() {
                    @Override
                    public void put(Intent intent) {
                        intent.putExtra(VikiccUtils.BACK_RegUser_TO_SETTING_HTTP, "true");
                    }
                });
                break;
        }
    }

    private void checkData() {
        if (setting_company_code.getText().toString().trim().equals("")) {
            VikiccUtils.ToastShort(this, "请输入企业编码！");
        } else if (setting_person_code.getText().toString().trim().equals("")) {
            VikiccUtils.ToastShort(this, "请输入人员编码！");
        } else if (setting_password.getText().toString().trim().equals("")) {
            VikiccUtils.ToastShort(this, "请输入密码！");
        } else {
            //注册
            CheckConfig();
        }
    }

    private void PostDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogin_Reg == null) {
                    VikiccUtils.IntentPost(RegUserActivity.this, LoginActivity.class, null);
                }
                dialog.dismiss();
                finish();
            }
        }, 2000);
    }

    /**
     * 获取人员详细信息
     */
    private void GetPerson() {
        new GetPersonnelTask().start(new GetPersonnelTask.GetPersonnelListener() {
            @Override
            public void onCheckResult(boolean result, InvokeReturn invokeReturn) {
                if (invokeReturn != null) {
                    if (invokeReturn.getListModel() != null) {
                        if (invokeReturn.getListModel().size() > 0) {
                            PersonnelModel p = (PersonnelModel) invokeReturn.getListModel().get(0);
                            p.setPersonnelPhotoImageId(invokeReturn.getImages());
                            List<PersonnelModel> lp = finalDb.findAll(PersonnelModel.class);
                            PersonnelModel dbp;
                            if (lp.size() == 1) {
                                dbp = lp.get(0);
                                finalDb.deleteById(PersonnelModel.class, dbp.getId());
                            }
                            finalDb.save(p);
                        }

                    }
                    PostDialog();
                } else {
                    VikiccUtils.ToastShort(RegUserActivity.this, "网络错误,更新人员详细信息失败！");
                }
            }

            @Override
            public HttpModel setHttpModel() {
                httpModel = getHttpModel();
                httpModel.setIsPost(true);
                httpModel.setInformation("PersonnelId=" + mobileConfigModel.getTerminalPersonnalId());
                httpModel.setObjective("MobileGetPersonnel");
                return httpModel;
            }
        });
    }

    /**
     * 判断变更时间，是否还需要更新
     *
     * @param btime
     */
    private void isRefush(String btime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int result;
        Date time = null;

        Date rtime = null;
        System.out.println(sdf.format(new Date()));
        String refushtime = VikiccUtils.ReadString(this, "RefushCodeTime");
        if (!(refushtime == null || refushtime.equals(""))) {
            //取出时间，跟变动时间比较
            try {
                rtime = sdf.parse(refushtime);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        } else {
            //保存当前更新时间
            rtime = new Date();
            VikiccUtils.WriteString(this, "RefushCodeTime", sdf.format(rtime));
            result = 1;
        }

        try {
            String stime = btime.substring(0, 10) + " " + btime.substring(11, 19);

            time = sdf.parse(stime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        result = time.compareTo(rtime);

        if (result < 0) {
            Codes = this.getResources().getStringArray(R.array.CodeName);
            if (Codes.length > 0) {
                GetCode(Codes[0], 0);
            }

            VikiccUtils.WriteString(this, "RefushCodeTime", sdf.format(time));
        } else {

            if (isLogin) {
                GetPerson();
            } else {
                VikiccUtils.ToastShort(RegUserActivity.this, "注册失败！");
            }
        }
    }

    /**
     * 读取字典变更时间
     */
    private void GetRefushTime() {
        //判断是否需要更新
        new GetCodesTask().start(new GetCodesTask.GetCodesListener() {
            @Override
            public void onCheckResult(boolean result, InvokeReturn invokeReturn) {

                if (result) {
                    InvokeReturn ir = invokeReturn;
                    if (ir != null) {
                        if (ir.getSuccess().equals("true")) {
                            System.out.println("time:" + ir.getTime());
                            isRefush(ir.getRtime());
                        } else {
                            isLogin = false;
                        }
                    } else {
                        isLogin = false;
                    }
                } else {
                    isLogin = false;
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

    /**
     * 读取网络字典
     *
     * @param name
     */
    private void GetCode(final String name, final int i) {
        new GetCodesTask().start(new GetCodesTask.GetCodesListener() {
            @Override
            public void onCheckResult(boolean result, InvokeReturn invokeReturn) {
                InvokeReturn ir = invokeReturn;
                if (result) {
                    if (invokeReturn != null) {
                        if (invokeReturn.getSuccess().equals("true")) {
                            System.out.println("codee list:" + ir.getListModel().size());
                            //保存到本地xml
                            List<CodeModel> list = new ArrayList<CodeModel>();
                            for (int i = 0; i < ir.getListModel().size(); i++) {
                                list.add((CodeModel) ir.getListModel().get(i));
                            }
                            XmlManager xml = new XmlManager(RegUserActivity.this);
                            System.out.println(name + "name*************");
                            xml.createXml(list, name + ".xml");
                            if (i < Codes.length) {


                                GetCode(Codes[i], i + 1);
                            }

                        } else {
                            isLogin = false;
                        }
                    }
                } else {
                    isLogin = false;
                }
                if (name.equals("Code_PersonnelType")) {
                    if (isLogin) {
                        GetPerson();
                    } else {
                        VikiccUtils.ToastShort(RegUserActivity.this, "注册失败！");
                    }
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

    //检测设备
    private void CheckConfig() {
        dialog = VikiccUtils.ProgressDialog(RegUserActivity.mIntaile, dialog, "注册中，请稍候...");
        dialog.show();
        new CheckConfigTask().start(new CheckConfigTask.CheckConfigListener() {
            @Override
            public void onCheckResult(boolean result, InvokeReturn invokeReturn) {
                if (result) {
                    //保存到本地
                    if (invokeReturn != null) {
                        if (invokeReturn.getSuccess().equals("true")) {
                            mobile.setTerminalIsRegFast(((TerminalModel) invokeReturn.getListModel().get(0)).getTerminalIsRegFast());
                            finalDb.update(mobile);
                            //更新字典
                            GetRefushTime();
                            //更新个人信息
                        } else {
                            dialog.dismiss();
                            VikiccUtils.ToastShort(RegUserActivity.this, "注册失败！");
                        }
                    }
                } else {
                    dialog.dismiss();
                    VikiccUtils.ToastShort(RegUserActivity.this, "网络错误，注册失败！");
                }
            }

            public HttpModel setHttpModel() {
                try {
                    mobile.setTerminalPassword(Utils.hashPassword(setting_password.getText().toString().trim()));
                    mobile.setTerminalId(imei);
                    mobile.setTerminalPersonnalId(setting_person_code.getText().toString().trim());
                    mobile.setTerminalCompanyId(setting_company_code.getText().toString().trim());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                httpModel = getHttpModel();
                httpModel.setIsPost(true);
                System.out.println(mobile);
                try {
                    String base = new BASE64Encoder().encode(mobile.Serializable().getBytes("UTF-8"));
                    base = base.replaceAll("\\+", "%2B");
                    base = base.replaceAll("\\=", "%3D");
                    httpModel.setInformation("MobileConfig=" + base);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                httpModel.setObjective("MobileCheckConfig");
                return httpModel;
            }
        });
    }
}
