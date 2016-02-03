package vikicc.logistics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import vikicc.custom.method.VikiccUtils;
import vikicc.logistics.R;
import vikicc.logistics.model.ExpressModel;
import vikicc.logistics.ui.activity.MainActivity;

/**
 * Created by liuliu on 2015/08/26   9:27
 * 查询Fragment的ListView的Adapter
 *
 * @author 柳伟杰
 * @Email 1031066280@qq.com
 */
public class SearchAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<ExpressModel> list;
    private LinearLayout ll_big;
    private LinearLayout ll_small;

    public SearchAdapter(List<ExpressModel> list, Context context) {
        this.list = list;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.main_search_item, null);
            holder.express_num = (TextView) convertView.findViewById(R.id.main_search_item_express_num);
            holder.express_status = (TextView) convertView.findViewById(R.id.main_search_item_express_status);
            holder.express_sender = (TextView) convertView.findViewById(R.id.main_search_item_express_sender);
            holder.express_consigneer_big = (TextView) convertView.findViewById(R.id.main_search_item_express_consigneer_big);
            holder.express_consigneer_small = (TextView) convertView.findViewById(R.id.main_search_item_express_consigneer_small);
            holder.express_start = (TextView) convertView.findViewById(R.id.main_search_item_express_start);
            holder.express_end = (TextView) convertView.findViewById(R.id.main_search_item_express_end);
            ll_big = (LinearLayout) convertView.findViewById(R.id.main_search_item_express_ll_big);
            ll_small = (LinearLayout) convertView.findViewById(R.id.main_search_item_express_ll_small);
            holder.express_ll_not_isfast = (LinearLayout) convertView.findViewById(R.id.main_search_item_not_isfast);
            holder.express_ll_fast_add = (LinearLayout) convertView.findViewById(R.id.main_search_item_express_ll_fast_add);
            convertView.setTag(holder);
            holder.rootView = convertView.findViewById(R.id.search_ll_item);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (VikiccUtils.getScannerWidth(MainActivity.mIntaile) <= 480) {
            ll_small.setVisibility(View.VISIBLE);
            ll_big.setVisibility(View.GONE);
        } else {
            ll_small.setVisibility(View.GONE);
            ll_big.setVisibility(View.VISIBLE);
        }
        String senderName = list.get(position).getSenderName();
        String receiverName = list.get(position).getReceiverName();
        if (holder != null) {
            if (senderName == null && receiverName == null) {
                holder.express_ll_not_isfast.setVisibility(View.GONE);
                holder.express_ll_fast_add.setVisibility(View.VISIBLE);
            } else {
                holder.express_ll_not_isfast.setVisibility(View.VISIBLE);
                holder.express_ll_fast_add.setVisibility(View.GONE);
                holder.express_consigneer_big.setText(list.get(position).getReceiverName() + "");
                holder.express_consigneer_small.setText(list.get(position).getReceiverName() + "");
                holder.express_sender.setText(list.get(position).getSenderName() + "");
                if (list.get(position) != null) {
                    if (list.get(position).getSendAddress() != null) {
                        if (list.get(position).getSendAddress().length() > 0) {
                            holder.express_start.setText(list.get(position).getSendAddress() + "");
                        } else {
                            holder.express_start.setText("未知");
                        }
                    } else {
                        holder.express_start.setText("未知");
                    }
                    if (list.get(position).getReceiveAddress() != null) {
                        if (list.get(position).getReceiveAddress().length() > 0) {
                            holder.express_end.setText(list.get(position).getReceiveAddress() + "");
                        } else {
                            holder.express_end.setText("未知");
                        }
                    } else {
                        holder.express_end.setText("未知");
                    }
                }
            }
        }
        holder.express_num.setText(list.get(position).getExpressDeliveryId() + "");
        String type = list.get(position).getExpressStatus();
        if (type.equals("0")) {
            holder.express_status.setText("寄件");
        } else if (type.equals("1")) {
            holder.express_status.setText("收件");
        } else {
            holder.express_status.setText("未知");
        }

        return convertView;
    }

    //查询的ViewHolder
    class ViewHolder {
        public View rootView;
        public TextView express_num;
        public TextView express_status;
        public TextView express_sender;
        public TextView express_consigneer_big;
        public TextView express_consigneer_small;
        public TextView express_start;
        public TextView express_end;
        public LinearLayout express_ll_not_isfast;
        public LinearLayout express_ll_fast_add;
        public int position;
    }

}
