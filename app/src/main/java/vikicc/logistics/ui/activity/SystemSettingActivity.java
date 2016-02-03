package vikicc.logistics.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import vikicc.custom.method.VikiccUtils;
import vikicc.custom.spinner.nicespinner.NiceSpinner;
import vikicc.logistics.R;
import vikicc.logistics.base.BaseActivity;
import vikicc.logistics.data.XmlManager;
import vikicc.logistics.model.CodeModel;
import vikicc.logistics.model.HttpModel;
import vikicc.logistics.model.InvokeReturn;
import vikicc.logistics.task.GetCodesTask;

/**
 * Created by liuliu on 2015/09/05   13:53
 * 系统设置页面
 *
 * @author 柳伟杰
 * @Email 1031066280@qq.com
 */
public class SystemSettingActivity extends BaseActivity {
    @CodeNote(id = R.id.total_top_ll_left, click = "onClick")
    LinearLayout ll_left;
    @CodeNote(id = R.id.total_top_ll_right)
    LinearLayout ll_right;
    @CodeNote(id = R.id.frag_ll_sys_http_update_codes, click = "onClick")
    LinearLayout frag_ll_sys_http_update_codes;
    @CodeNote(id = R.id.frag_ll_sys_password_update, click = "onClick")
    LinearLayout frag_ll_sys_password_update;
    @CodeNote(id = R.id.total_top_txt_title)
    TextView total_top_title;
    @CodeNote(id = R.id.nicespinner)
    NiceSpinner spinner;
    @CodeNote(id = R.id.frag_ll_sys_http_setting, click = "onClick")
    LinearLayout ll_ss_http_setting;
    boolean isLogin = true;//是不是登陆
    String[] Codes;
    Dialog dialog;
    public static SystemSettingActivity mIntaile;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_sys_setting);
        ll_left.setVisibility(View.VISIBLE);
        ll_right.setVisibility(View.GONE);
        total_top_title.setText("系统设置");
        mIntaile = this;
    }

    @Override
    public void initEvents() {
        final List<String> data = new LinkedList<String>(Arrays.asList("5分", "10分", "15分", "20分", "30分"));
        spinner.attachDataSource(data);
        String time = VikiccUtils.ReadString(mIntaile, VikiccUtils.CHECK_UPLOAD_TIME);
        for (int i = 0; i < data.size(); i++) {
            if (!time.equals("")) {
                if (time.equals(data.get(i))) {
                    spinner.setSelectedIndex(i);
                }
            } else {
                VikiccUtils.WriteString(mIntaile, VikiccUtils.CHECK_UPLOAD_TIME, data.get(i));
            }

        }

        spinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VikiccUtils.WriteString(mIntaile, VikiccUtils.CHECK_UPLOAD_TIME, data.get(position));
            }
        });
    }

    /*监听页面点击事件，以及处理办法*/
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.total_top_ll_left:
                finish();
                break;
            case R.id.frag_ll_sys_http_setting:
                VikiccUtils.IntentPost(this, SettingHttpActivity.class, new VikiccUtils.putListener() {
                    @Override
                    public void put(Intent intent) {
                        intent.putExtra(VikiccUtils.BACK_SYS_SETTING_TO_SETTING_HTTP, "true");
                    }
                });
                break;
            case R.id.frag_ll_sys_password_update:
                VikiccUtils.IntentPost(this, PassWordUpdateActivity.class, null);
                break;
            case R.id.frag_ll_sys_http_update_codes:
                dialog = VikiccUtils.ProgressDialog(SystemSettingActivity.mIntaile, dialog, "更新字典中，请稍候...");
                dialog.show();
                GetRefushTime();
                break;
        }
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
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    dialog.dismiss();
                    VikiccUtils.ToastShort(SystemSettingActivity.this, "更新成功,已是最新！");
                }
            }, 2000);
        }
    }

    private void dialogDismiss(final String ToastVal) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
                if (ToastVal != null) {
                    VikiccUtils.ToastShort(SystemSettingActivity.mIntaile, ToastVal);
                }
            }
        }, 2000);
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
                            isRefush(ir.getRtime());
                        } else {
                            dialogDismiss("更新失败！");
                        }
                    } else {
                        dialogDismiss("更新失败！");

                    }
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                            VikiccUtils.ToastShort(SystemSettingActivity.this, "更新失败！");
                        }
                    }, 2000);

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
                            //保存到本地xml
                            List<CodeModel> list = new ArrayList<CodeModel>();
                            for (int i = 0; i < ir.getListModel().size(); i++) {
                                list.add((CodeModel) ir.getListModel().get(i));
                            }
                            XmlManager xml = new XmlManager(SystemSettingActivity.this);
                            xml.createXml(list, name + ".xml");
                            if (i < Codes.length) {
                                GetCode(Codes[i], i + 1);
                            }
                        } else {
                            dialogDismiss("更新失败！");

                        }
                    }
                } else {
                    isLogin = false;
                }
                if (name.equals("Code_PersonnelType")) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                            if (isLogin) {
                                VikiccUtils.ToastShort(SystemSettingActivity.this, "更新成功！");
                            } else {
                                VikiccUtils.ToastShort(SystemSettingActivity.this, "更新失败！");
                            }
                        }
                    }, 2000);

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
