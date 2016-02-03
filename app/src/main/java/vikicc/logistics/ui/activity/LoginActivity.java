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
import java.util.List;

import vikicc.custom.method.VikiccUtils;
import vikicc.logistics.R;
import vikicc.logistics.base.BaseActivity;
import vikicc.logistics.base.Utils;
import vikicc.logistics.model.TerminalModel;

/**
 * 登陆Activity
 */

public class LoginActivity extends BaseActivity {

    public static LoginActivity mIntaile;

    @CodeNote(id = R.id.total_top_ll_right, click = "onClick")
    public LinearLayout ll_right;
    @CodeNote(id = R.id.total_top_txt_right)
    public TextView tv_right;
    @CodeNote(id = R.id.total_top_txt_title)
    public TextView tv_title;//标题

    @CodeNote(id = R.id.login_btn, click = "onClick")
    private Button btn_login;//登陆
    @CodeNote(id = R.id.login_reg, click = "onClick")
    private Button btn_reg;//注册

    @CodeNote(id = R.id.login_userid)
    EditText txt_userid;//用户编码
    @CodeNote(id = R.id.login_pwd)
    EditText txt_pwd;//密码
    Dialog dialog;
    TerminalModel termainl;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_login);
        mIntaile = this;
        List<TerminalModel> lists = finalDb.findAll(TerminalModel.class);
        if (lists.size() > 0) {//存在用户
            termainl = lists.get(0);
            txt_userid.setText(termainl.getTerminalPersonnalId());
            txt_pwd.setFocusable(true);
            txt_pwd.setFocusableInTouchMode(true);
            txt_pwd.requestFocus();
        }
    }

    @Override
    public void initEvents() {
        tv_title.setText("登录");
        ll_right.setVisibility(View.VISIBLE);
        tv_right.setText("网络设置");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.total_top_ll_right://顶部右侧点击进行网络设置
                VikiccUtils.IntentPost(this, SettingHttpActivity.class, new VikiccUtils.putListener() {
                    @Override
                    public void put(Intent intent) {
                        intent.putExtra(VikiccUtils.BACK_Login_TO_SETTING_HTTP, "true");
                    }
                });//跳转到设置网络页面
                break;
            case R.id.login_btn://登录按钮触发事件
                if (txt_userid.getText().toString().trim().equals("") || txt_pwd.getText().toString().trim().equals("")) {
                    VikiccUtils.ToastShort(this, "请输入人员编码和密码！");
                } else {
                    List<TerminalModel> list = finalDb.findAll(TerminalModel.class);
                    if (list != null) {
                        if (list.size() == 1)
                            mobileConfigModel = list.get(0);
                    }
                    if (VikiccUtils.isEmptyString(mobileConfigModel.getTerminalId())) {
                        VikiccUtils.ToastShort(this, "请注册设备");
                        VikiccUtils.IntentPost(this, RegUserActivity.class, null);//跳转到注册页面
                        finish();
                    } else if (VikiccUtils.isEmptyString(mobileConfigModel.getTerminalIP()) && VikiccUtils.isEmptyString(mobileConfigModel.getTerminalCount())) {
                        VikiccUtils.ToastShort(this, "请设置ip与端口");
                        VikiccUtils.IntentPost(this, SettingHttpActivity.class, null);//跳转到注册页面
                        finish();
                    }
                    checkLogin();
                }
                break;
            case R.id.login_reg://注册按钮触发事件
                VikiccUtils.IntentPost(this, RegUserActivity.class, new VikiccUtils.putListener() {
                    @Override
                    public void put(Intent intent) {
                        intent.putExtra(VikiccUtils.BACK_Login_TO_RegUser, "true");
                    }
                });//跳转到注册页面
                break;
        }
    }

    //检查登陆
    private void checkLogin() {
        dialog = VikiccUtils.ProgressDialog(LoginActivity.mIntaile, dialog, "登录中，请稍候...");
        dialog.show();
        //查找数据库是否存在该用户
        List<TerminalModel> lists = (List<TerminalModel>) finalDb.findAllByWhere(TerminalModel.class, "TerminalPersonnalId='" + txt_userid.getText().toString() + "'");

        if (lists.size() > 0) {//存在用户
            try {
                if (lists.get(0).getTerminalPassword().equals(Utils.hashPassword(txt_pwd.getText().toString()))) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //登陆成功
                            VikiccUtils.IntentPost(LoginActivity.mIntaile, MainActivity.class, null);
                            dialog.dismiss();
                            finish();
                        }
                    }, 2000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            //密码错误
                            VikiccUtils.ToastShort(LoginActivity.mIntaile, "密码错误！");
                        }
                    }, 2000);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        dialog.setCanceledOnTouchOutside(false);
    }

}
