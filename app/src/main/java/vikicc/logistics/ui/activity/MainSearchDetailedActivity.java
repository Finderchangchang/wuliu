package vikicc.logistics.ui.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import java.util.ArrayList;
import java.util.List;

import vikicc.logistics.R;
import vikicc.logistics.base.BaseActivity;
import vikicc.logistics.data.XmlManager;
import vikicc.logistics.model.CodeModel;
import vikicc.logistics.model.ExpressModel;

import static vikicc.logistics.ui.activity.MainActivity.mIntaile;

/**
 * Created by liuliu on 2015/09/07   14:40
 *
 * @author 柳伟杰
 * @Email 1031066280@qq.com
 */
public class MainSearchDetailedActivity extends BaseActivity {
    @CodeNote(id = R.id.total_top_ll_left, click = "onClick")
    LinearLayout ll_left;
    @CodeNote(id = R.id.total_top_txt_title)
    TextView txt_total_title;
    @CodeNote(id = R.id.total_top_ll_right)
    LinearLayout ll_right;
    @CodeNote(id = R.id.main_search_detailed_ll_sender)
    LinearLayout ll_sender;
    @CodeNote(id = R.id.main_search_detailed_ll_senders)
    LinearLayout ll_senders;

    @CodeNote(id = R.id.main_search_detailed_txt_express_deliveryid)
    TextView txt_deliveryid;//快递单号
    @CodeNote(id = R.id.main_search_detailed_txt_express_start_address)
    TextView txt_start_address;//寄件地址
    @CodeNote(id = R.id.main_search_detailed_txt_express_end_address)
    TextView txt_end_address;//收件地址
    @CodeNote(id = R.id.main_search_detailed_txt_express_status)
    TextView txt_status;//快递状态

    @CodeNote(id = R.id.main_search_detailed_txt_express_type)
    TextView txt_type;//快递类型
    @CodeNote(id = R.id.main_search_detailed_txt_express_types)
    TextView txt_types;//快递类型底部显示
    /*快递类型页面布局*/
    @CodeNote(id = R.id.main_search_detailed_ll_express_type)
    LinearLayout ll_express_type;
    @CodeNote(id = R.id.main_search_detailed_ll_express_types)
    LinearLayout ll_express_types;
    /*寄件人*/
    @CodeNote(id = R.id.main_search_detailed_txt_sender_idcard)
    TextView txt_sender_idcard;//身份证号
    @CodeNote(id = R.id.main_search_detailed_txt_sender_name)
    TextView txt_sender_name;//寄件人姓名
    @CodeNote(id = R.id.main_search_detailed_txt_sender_tel)
    TextView txt_sender_tel;//寄件人电话

    /*（收件）寄件人*/
    @CodeNote(id = R.id.main_search_detailed_txt_sender_idcards)
    TextView txt_sender_idcards;//身份证号
    @CodeNote(id = R.id.main_search_detailed_txt_sender_names)
    TextView txt_sender_names;//寄件人姓名
    @CodeNote(id = R.id.main_search_detailed_txt_sender_tels)
    TextView txt_sender_tels;//寄件人电话

    /*收件人*/
    @CodeNote(id = R.id.main_search_item_express_receiver_idcard)
    TextView txt_receiver_idcard;//身份证号
    @CodeNote(id = R.id.main_search_item_express_receiver)
    TextView txt_receiver_name;//寄件人姓名
    @CodeNote(id = R.id.main_search_item_express_receiver_tel)
    TextView txt_receiver_tel;//收件人电话

    @CodeNote(id = R.id.main_detail_ll_not_fast)
    LinearLayout detail_ll_not_fast;

    @CodeNote(id = R.id.main_search_detailed_ll_express_id_type)
    LinearLayout detailed_ll_express_id_type;

    @Override
    public void initViews() {
        setContentView(R.layout.main_search_detailed);
        ll_left.setVisibility(View.VISIBLE);
        ll_right.setVisibility(View.GONE);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.total_top_ll_left://点击返回关闭
                this.finish();
                break;
        }
    }

    @Override
    public void initEvents() {
        ExpressModel model = (ExpressModel) getIntent().getSerializableExtra("model");
        if (model != null) {
            txt_total_title.setText("详细信息");
            if (model.getExpressStatus().equals("0")) {
                txt_status.setText("寄件");
                ll_sender.setVisibility(View.VISIBLE);
                ll_senders.setVisibility(View.GONE);
            } else if (model.getExpressStatus().equals("1")) {
                txt_status.setText("收件");
                ll_sender.setVisibility(View.GONE);
                ll_senders.setVisibility(View.VISIBLE);
            } else {
                txt_status.setText("未知");
            }
            txt_deliveryid.setText(model.getExpressDeliveryId());
            txt_type.setText(model.getExpressType());

            //一般添加（寄件收件人信息显示）。快速添加（寄件收件人都隐藏）
            if (model.getReceiverName() == null && model.getSenderName() == null) {
                detail_ll_not_fast.setVisibility(View.GONE);
                ll_express_type.setVisibility(View.GONE);
                detailed_ll_express_id_type.setVisibility(View.VISIBLE);
            } else {
                detail_ll_not_fast.setVisibility(View.VISIBLE);
                ll_express_type.setVisibility(View.VISIBLE);
                detailed_ll_express_id_type.setVisibility(View.GONE);
                checkText(model.getSendAddress(), txt_start_address);
                checkText(model.getReceiveAddress(), txt_end_address);
                checkText(model.getSenderName(), txt_sender_name);
                checkText(model.getSenderIdentityNumber(), txt_sender_idcard);
                checkText(model.getSenderLinkway(), txt_sender_tel);
                checkText(model.getReceiverName(), txt_receiver_name);
                checkText(model.getReceiverIdentityNumber(), txt_receiver_idcard);
                checkText(model.getReceiverLinkway(), txt_receiver_tel);
                checkText(model.getSenderName(), txt_sender_names);
                checkText(model.getSenderIdentityNumber(), txt_sender_idcards);
                checkText(model.getSenderLinkway(), txt_sender_tels);
            }
            /*快递类型的文字长度大于2，快递类型：换行*/
            if (model.getExpressType() != null) {
                if (GetCodes(model.getExpressType()).length() > 2) {
                    ll_express_type.setVisibility(View.GONE);
                    ll_express_types.setVisibility(View.VISIBLE);
                    txt_types.setText(GetCodes(model.getExpressType()));
                } else {
                    ll_express_type.setVisibility(View.VISIBLE);
                    ll_express_types.setVisibility(View.GONE);
                    txt_type.setText(GetCodes(model.getExpressType()));
                }
            }
        }
    }

    private void checkText(String val, TextView textView) {
        if (val.length() > 0) {
            textView.setText(val);
        } else {
            textView.setText("未知");
        }
    }

    List<CodeModel> types = new ArrayList<CodeModel>();

    /*更新字典*/
    private String GetCodes(String item) {
        XmlManager xml = new XmlManager(mIntaile);
        types = xml.getXml("Code_ExpressType.xml");
        if (types != null) {
            return types.get(Integer.parseInt(item) - 1).getValue();
        }
        return null;
    }
}