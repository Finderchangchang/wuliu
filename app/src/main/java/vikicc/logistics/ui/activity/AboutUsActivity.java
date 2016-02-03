package vikicc.logistics.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import vikicc.logistics.R;
import vikicc.logistics.base.BaseActivity;

/**
 * Created by liuliu on 2015/09/02   11:15
 * 关于我们
 *
 * @author 柳伟杰
 * @Email 1031066280@qq.com
 */
public class AboutUsActivity extends BaseActivity {
    AboutUsActivity mIntaile;
    @CodeNote(id = R.id.total_top_ll_left, click = "onClick")
    LinearLayout ll_top_left;
    @CodeNote(id = R.id.total_top_ll_right)
    LinearLayout ll_top_right;
    @CodeNote(id = R.id.total_top_txt_title)
    TextView txt_title;
    @CodeNote(id = R.id.about_us_version)
    TextView txt_version;
    @CodeNote(id = R.id.about_us_btn_check_new, click = "onClick")
    Button btn_check;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_about_us);
        mIntaile = this;
    }

    @Override
    public void initEvents() {
        ll_top_left.setVisibility(View.VISIBLE);
        ll_top_right.setVisibility(View.GONE);
        txt_title.setText("关于我们");
        txt_version.setText("--V1.0.0--");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.total_top_ll_left:
                this.finish();
                break;
            case R.id.about_us_btn_check_new://检查新版本

                break;
        }
    }
}
