package vikicc.logistics.ui.activity;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import net.tsz.afinal.annotation.view.CodeNote;

import vikicc.custom.toast.CustomeDialog;
import vikicc.logistics.R;
import vikicc.logistics.base.BaiduMapLocationService;
import vikicc.logistics.base.BaseActivity;
import vikicc.logistics.base.BaseApplication;
import vikicc.logistics.model.TerminalModel;
import vikicc.logistics.ui.fragment.MyFragment;
import vikicc.logistics.ui.fragment.QuickAddFragment;
import vikicc.logistics.ui.fragment.SearchFragment;
import vikicc.logistics.ui.fragment.SendFragment;

/**
 * 项目主页面
 */
public class MainActivity extends BaseActivity {
    //    Mian页面title
    @CodeNote(id = R.id.total_top_txt_title)
    TextView txt_top_title;

    @CodeNote(id = R.id.total_bottom_ll_send, click = "onClick")
    LinearLayout ll_send;
    @CodeNote(id = R.id.total_bottom_ll_consignee, click = "onClick")
    LinearLayout ll_consignee;
    @CodeNote(id = R.id.total_bottom_ll_search, click = "onClick")
    LinearLayout ll_search;
    @CodeNote(id = R.id.total_bottom_ll_my, click = "onClick")
    LinearLayout ll_my;

    @CodeNote(id = R.id.total_bottom_img_send)
    ImageView img_send;
    @CodeNote(id = R.id.total_bottom_img_consignee)
    ImageView img_consignee;
    @CodeNote(id = R.id.total_bottom_img_search)
    ImageView img_search;
    @CodeNote(id = R.id.total_bottom_img_quick)
    ImageView total_bottom_img_quick;
    @CodeNote(id = R.id.total_bottom_img_my)
    ImageView img_my;
    @CodeNote(id = R.id.total_bottom_lab_send)
    TextView lab_send;
    @CodeNote(id = R.id.total_bottom_ll_quick, click = "onClick")
    LinearLayout total_bottom_ll_quick;
    @CodeNote(id = R.id.total_bottom_lab_consignee)
    TextView lab_consignee;
    @CodeNote(id = R.id.total_bottom_lab_quick)
    TextView total_bottom_lab_quick;
    @CodeNote(id = R.id.total_bottom_lab_search)
    TextView lab_search;
    @CodeNote(id = R.id.total_bottom_lab_my)
    TextView lab_my;
    @CodeNote(id = R.id.total_top_txt_right)
    TextView total_top_txt_right;
    @CodeNote(id = R.id.total_top_ll_right)
    LinearLayout total_top_ll_right;
    @CodeNote(id = R.id.total_top_ll_left)
    LinearLayout total_top_ll_left;
    FragmentTransaction transaction;
    public static MainActivity mIntaile;

    public String address_main;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_main);
        String path = getApplicationContext().getPackageResourcePath();
        String p = this.getFilesDir().getAbsolutePath();
        System.out.println(path);///data/app/vikicc.logistics-2.apk
        System.out.println(p);///data/data/vikicc.logistics/files
        mIntaile = this;
        transaction = getFragmentManager().beginTransaction();
        total_top_txt_right.setText("添加");
        total_top_ll_left.setVisibility(View.GONE);
    }

    @Override
    public void initEvents() {
        TerminalModel terminal = finalDb.findAll(TerminalModel.class).get(0);
        if (terminal.getTerminalIsRegFast().equals("false")) {
            total_bottom_ll_quick.setVisibility(View.GONE);
        }
        img_send.setImageResource(R.mipmap.frag_send_press);
        lab_send.setTextColor(getResources().getColor(R.color.press));
        transaction.add(R.id.main_fragment, new SendFragment());
        txt_top_title.setText("添加揽件信息");
        transaction.commit();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.total_bottom_ll_send://寄件
                transaction = getFragmentManager().beginTransaction();
                closeAllToNormal();
                txt_top_title.setText("添加揽件信息");
                img_send.setImageBitmap(null);
                img_send.setImageResource(R.mipmap.frag_send_press);
                lab_send.setTextColor(getResources().getColor(R.color.press));
                transaction.replace(R.id.main_fragment, new SendFragment());
                total_top_txt_right.setText("添加");
                total_top_ll_right.setVisibility(View.VISIBLE);
                total_top_ll_left.setVisibility(View.GONE);
                //提交修改
                transaction.commit();
                break;
            case R.id.total_bottom_ll_consignee://收件
                closeAllToNormal();
                transaction = getFragmentManager().beginTransaction();
                txt_top_title.setText("添加送件信息");
                img_consignee.setImageResource(R.mipmap.frag_consignee_press);
                lab_consignee.setTextColor(getResources().getColor(R.color.press));
                transaction.replace(R.id.main_fragment, new SendFragment());
                total_top_txt_right.setText("添加");
                total_top_ll_right.setVisibility(View.VISIBLE);
                total_top_ll_left.setVisibility(View.GONE);
                //提交修改
                transaction.commit();
                break;
            case R.id.total_bottom_ll_search://查询
                closeAllToNormal();
                transaction = getFragmentManager().beginTransaction();
                txt_top_title.setText("单号查询");
                img_search.setImageResource(R.mipmap.frag_search_press);
                lab_search.setTextColor(getResources().getColor(R.color.press));
                transaction.replace(R.id.main_fragment, new SearchFragment());
                total_top_ll_right.setVisibility(View.GONE);
                //提交修改
                transaction.commit();
                break;
            case R.id.total_bottom_ll_my://我的
                closeAllToNormal();
                transaction = getFragmentManager().beginTransaction();
                txt_top_title.setText("个人中心");
                img_my.setImageResource(R.mipmap.frag_my_press);
                lab_my.setTextColor(getResources().getColor(R.color.press));
                transaction.replace(R.id.main_fragment, new MyFragment());
                total_top_ll_right.setVisibility(View.GONE);
                total_top_ll_left.setVisibility(View.GONE);
                //提交修改
                transaction.commit();
                break;
            case R.id.total_bottom_ll_quick:
                closeAllToNormal();
                transaction = getFragmentManager().beginTransaction();
                txt_top_title.setText("快速添加");
                total_bottom_img_quick.setImageResource(R.mipmap.quick_add2);
                total_bottom_lab_quick.setTextColor(getResources().getColor(R.color.press));
                transaction.replace(R.id.main_fragment, new QuickAddFragment());
                total_top_txt_right.setText("添加");
                total_top_ll_right.setVisibility(View.VISIBLE);
                total_top_ll_left.setVisibility(View.GONE);
                //提交修改
                transaction.commit();
                break;
        }
    }

    private void closeAllToNormal() {
        img_consignee.setImageResource(R.mipmap.frag_consignee);
        img_my.setImageResource(R.mipmap.frag_my);
        img_send.setImageResource(R.mipmap.frag_send);
        img_search.setImageResource(R.mipmap.frag_search);
        total_bottom_img_quick.setImageResource(R.mipmap.quick_add);
        total_bottom_lab_quick.setTextColor(getResources().getColor(R.color.b2b2b2));
        lab_send.setTextColor(getResources().getColor(R.color.b2b2b2));
        lab_search.setTextColor(getResources().getColor(R.color.b2b2b2));
        lab_consignee.setTextColor(getResources().getColor(R.color.b2b2b2));
        lab_my.setTextColor(getResources().getColor(R.color.b2b2b2));
    }

    @Override
    public void onBackPressed() {
        showAlertDialog();
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
}
