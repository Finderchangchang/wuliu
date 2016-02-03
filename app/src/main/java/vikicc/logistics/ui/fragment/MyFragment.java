package vikicc.logistics.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.CodeNote;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vikicc.custom.image.CircleImageView;
import vikicc.custom.method.VikiccUtils;
import vikicc.custom.toast.CustomeDialog;
import vikicc.logistics.R;
import vikicc.logistics.base.BaseFragment;
import vikicc.logistics.base.Utils;
import vikicc.logistics.data.XmlManager;
import vikicc.logistics.model.CodeModel;
import vikicc.logistics.model.ExpressModel;
import vikicc.logistics.model.PersonnelModel;
import vikicc.logistics.ui.activity.AboutUsActivity;
import vikicc.logistics.ui.activity.MainActivity;
import vikicc.logistics.ui.activity.SystemSettingActivity;

/**
 * Created by liuliu on 2015/08/26   11:32
 *
 * @author 柳伟杰
 * @remark 个人中心
 * @Email 1031066280@qq.com
 */
public class MyFragment extends BaseFragment {
    @CodeNote(id = R.id.frag_ll_my_sys_setting, click = "onClick")
    LinearLayout ll_sys_setting;//系统设置
    @CodeNote(id = R.id.frag_ll_my_about_us, click = "onClick")
    LinearLayout ll_about_us;//关于我们
    @CodeNote(id = R.id.frag_ll_my_clear_cache, click = "onClick")
    LinearLayout ll_clear_cache;//清除缓存
    @CodeNote(id = R.id.btn_exit, click = "onClick")
    Button btn_exit;//退出
    @CodeNote(id = R.id.frag_img_my)
    CircleImageView frag_img_my;//头像
    @CodeNote(id = R.id.frag_txt_my_name)
    TextView frag_txt_my_name;//用户名
    @CodeNote(id = R.id.frag_txt_my_user_type)
    TextView frag_txt_my_user_type;//人员类型
    @CodeNote(id = R.id.frag_txt_my_company_name)
    TextView frag_txt_my_company_name;//公司名称

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_main_my, container, false);
        FinalActivity.initInjectedView(this, view);
        finalDb = FinalDb.create(MainActivity.mIntaile);
        PersonnelModel person = new PersonnelModel();
        List<CodeModel> type = new ArrayList<CodeModel>();
        type = new XmlManager(MainActivity.mIntaile).getXml("Code_PersonnelType.xml");
        if (finalDb.findAll(PersonnelModel.class).size() >= 0) {
            person = finalDb.findAll(PersonnelModel.class).get(0);
            if (!VikiccUtils.isEmptyString(person.getPersonnelPhotoImageId())) {
                frag_img_my.setImageBitmap(Utils.getBitmapFromByte(Base64.decode(person.getPersonnelPhotoImageId(), Base64.DEFAULT)));
            } else {
                frag_img_my.setImageResource(R.drawable.face);
            }
            frag_txt_my_name.setText(person.getPersonnelName());
            frag_txt_my_company_name.setText(person.getCompanyId());
            if (type != null) {
                if (type.size() > 0) {
                    for (CodeModel c : type) {
                        if (c.getKey().equals(person.getPersonnelType())) {
                            frag_txt_my_user_type.setText(c.getValue());
                            break;
                        }
                    }
                } else {
                    frag_txt_my_user_type.setText("普通员工");
                }
            }

        }

        return view;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frag_ll_my_sys_setting:
                VikiccUtils.IntentPost(MainActivity.mIntaile, SystemSettingActivity.class, null);
                break;
            case R.id.frag_ll_my_about_us:
                VikiccUtils.IntentPost(MainActivity.mIntaile, AboutUsActivity.class, null);
                break;
            case R.id.frag_ll_my_clear_cache:
                DeleteData();
                break;
            case R.id.btn_exit:
                showAlertDialog();
                break;
        }
    }

    //退出
    public void showAlertDialog() {
        CustomeDialog.Builder builder = new CustomeDialog.Builder(MainActivity.mIntaile);
        builder.setMessage("确定要退出系统？");

        builder.setTitle("提示");
        builder.setPositiveButtonText("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                dialog = null;
                MainActivity.mIntaile.finish();
            }
        });

        builder.setNegativeButtonText("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    /**
     * 清理缓存
     */
    private void DeleteData() {
        List<ExpressModel> list = finalDb.findAll(ExpressModel.class);
        for (ExpressModel e : list) {
            String time = e.getUploadTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time = time.replaceAll("T", " ");
            try {
                Date t = sdf.parse(time);
                long diff = new Date().getTime() - t.getTime();
                long days = diff / (1000 * 60 * 60 * 24);
                if (days > 30) {
                    finalDb.delete(e);
                }
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        VikiccUtils.ToastShort(MainActivity.mIntaile, "清理完成");
    }

}
