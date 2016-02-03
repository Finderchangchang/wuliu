package vikicc.logistics.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import vikicc.custom.edittext.Lab_ImgEditText;
import vikicc.custom.method.VikiccUtils;
import vikicc.logistics.R;
import vikicc.logistics.base.BaseActivity;
import vikicc.logistics.base.Utils;
import vikicc.logistics.model.HttpModel;
import vikicc.logistics.model.InvokeReturn;
import vikicc.logistics.model.TerminalModel;
import vikicc.logistics.task.UpdatePassWordTask;

/**
 * Created by liuliu on 2015/09/02   10:41
 * <p/>
 * 修改密码
 *
 * @author 柳伟杰
 * @Email 1031066280@qq.com
 */
public class PassWordUpdateActivity extends BaseActivity {
    public static PassWordUpdateActivity mIntaile;
    @CodeNote(id = R.id.total_top_ll_left, click = "onClick")
    LinearLayout ll_top_left;
    @CodeNote(id = R.id.total_top_ll_right)
    LinearLayout ll_top_right;
    @CodeNote(id = R.id.total_top_txt_title)
    TextView txt_title;
    @CodeNote(id = R.id.main_my_txt_old_password)
    Lab_ImgEditText txt_old_pwd;
    @CodeNote(id = R.id.main_my_txt_new_password)
    Lab_ImgEditText txt_new_pwd;
    @CodeNote(id = R.id.main_my_txt_new_again_password)
    Lab_ImgEditText txt_new_again_pwd;
    @CodeNote(id = R.id.main_my_btn_update, click = "onClick")
    Button btn_update;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_pwd_update);
        mIntaile = this;
        txt_old_pwd.setEditText("原密码");
        txt_new_pwd.setEditText("新密码");
        txt_new_again_pwd.setEditText("重复密码");
    }

    @Override
    public void initEvents() {
        ll_top_left.setVisibility(View.VISIBLE);
        ll_top_right.setVisibility(View.GONE);
        txt_title.setText("修改密码");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.total_top_ll_left:
                this.mIntaile.finish();
                break;
            case R.id.main_my_btn_update:
                if (checkEditTextisNull()) {
                    /*提交到后台更新密码*/
                    UpatePwd();
                }
                break;
        }
    }

    private void clearText() {
        txt_old_pwd.setText("");
        txt_new_pwd.setText("");
        txt_new_again_pwd.setText("");
    }

    /*修改密码*/
    private void UpatePwd() {
        new UpdatePassWordTask().start(new UpdatePassWordTask.UpdatePassWordListener() {
            @Override
            public void onCheckResult(boolean result, InvokeReturn invokeReturn) {
                if (invokeReturn.getSuccess().equals("true")) {
                    VikiccUtils.ToastShort(PassWordUpdateActivity.mIntaile, "修改成功！");
                    try {
                        TerminalModel configModel = finalDb.findAll(TerminalModel.class).get(0);
                        configModel.setTerminalPassword(Utils.hashPassword(txt_new_pwd.getText().toString().trim()));
                        finalDb.update(configModel);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    finish();
                } else {
                    VikiccUtils.ToastShort(PassWordUpdateActivity.mIntaile, "修改失败！");
                    clearText();
                }

            }

            @Override
            public HttpModel setHttpModel() {
                httpModel = getHttpModel();
                httpModel.setIsPost(true);
                try {
                    httpModel.setInformation("TerminalId=" + mobileConfigModel.getTerminalId()
                            + "&TerminalIdOldPassword=" + Utils.hashPassword(txt_old_pwd.getText().toString().trim())
                            + "&TerminalIdPassword =" + Utils.hashPassword(txt_new_pwd.getText().toString().trim()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                httpModel.setObjective("MobileChangePassword");
                return httpModel;
            }
        });
    }

    private boolean checkEditTextisNull() {
        String old_pwd = txt_old_pwd.getText().toString().trim();
        String new_pwd = txt_new_pwd.getText().toString().trim();
        String new_again_pwd = txt_new_again_pwd.getText().toString().trim();
        if (old_pwd == "") {
            VikiccUtils.ToastShort(this, "请输入原始密码");
            return false;
        } else if (new_pwd == "") {
            VikiccUtils.ToastShort(this, "请设置新密码(6~32位)");
            return false;
        } else if (new_again_pwd == "" || !new_again_pwd.equals(new_pwd)) {
            VikiccUtils.ToastShort(this, "两次输入的密码不同，请重新输入");
            return false;
        }
        return true;
    }
}
