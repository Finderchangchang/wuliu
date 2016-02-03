package vikicc.logistics.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import java.util.List;

import vikicc.custom.method.VikiccUtils;
import vikicc.logistics.R;
import vikicc.logistics.base.BaseActivity;
import vikicc.logistics.model.TerminalModel;

/**
 * 设置Ip地址与端口
 */
public class SettingHttpActivity extends BaseActivity {

    public static SettingHttpActivity mIntaile;
    @CodeNote(id = R.id.total_top_ll_right, click = "onClick")
    LinearLayout ll_right;
    @CodeNote(id = R.id.setting_http_ip)
    EditText ip;
    @CodeNote(id = R.id.setting_http_port)
    EditText port;
    @CodeNote(id = R.id.total_top_ll_left, click = "onClick")
    LinearLayout ll_left;
    @CodeNote(id = R.id.total_top_txt_title)
    TextView title;
    @CodeNote(id = R.id.setting_http_submit, click = "onClick")
    Button http_submit;
    String isSys_Setting;//系统设置跳转到的网络设置
    String isLogin_Setting;//登录跳转到网络设置
    String isReg_Setting;//用户注册跳转到网络设置


    @Override
    public void initViews() {
        setContentView(R.layout.activity_setting_http);
        mIntaile = this;
        ll_left.setVisibility(View.VISIBLE);
        ll_right.setVisibility(View.GONE);
        title.setText("网络设置");
        isSys_Setting = getIntent().getStringExtra(VikiccUtils.BACK_SYS_SETTING_TO_SETTING_HTTP);
        isLogin_Setting = getIntent().getStringExtra(VikiccUtils.BACK_Login_TO_SETTING_HTTP);
        isReg_Setting = getIntent().getStringExtra(VikiccUtils.BACK_RegUser_TO_SETTING_HTTP);
    }

    @Override
    public void initEvents() {
        List<TerminalModel> list = finalDb.findAll(TerminalModel.class);
        if (list.size() > 0) {
            mobileConfigModel = list.get(0);
            ip.setText(mobileConfigModel.getTerminalIP());
            port.setText(mobileConfigModel.getTerminalCount());
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.total_top_ll_left://点击返回按钮
                finish();
                break;
            case R.id.setting_http_submit://提交
                if (ip.getText().toString().trim().equals("") || port.getText().toString().trim().equals("")) {
                    VikiccUtils.ToastShort(this, "Ip地址,端口号不能为空");
                } else {
                    mobileConfigModel.setTerminalIP(ip.getText().toString().trim());
                    mobileConfigModel.setTerminalCount(port.getText().toString().trim());
                    if (mobileConfigModel.getId() == 1) {
                        finalDb.update(mobileConfigModel);
                    } else {
                        finalDb.save(mobileConfigModel);
                    }
                    VikiccUtils.ToastShort(this, "设置成功");
                    if (isLogin_Setting == null && isReg_Setting == null && isSys_Setting == null) {
                        if (VikiccUtils.isEmptyString(mobileConfigModel.getTerminalId())) {
                            VikiccUtils.IntentPost(this, RegUserActivity.class, null);
                        } else {
                            VikiccUtils.IntentPost(this, LoginActivity.class, null);
                        }
                    }
                    finish();
                }
                break;
        }
    }
}

