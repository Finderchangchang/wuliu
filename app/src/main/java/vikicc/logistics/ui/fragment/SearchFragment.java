package vikicc.logistics.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.CodeNote;

import java.util.ArrayList;
import java.util.List;

import vikicc.custom.method.VikiccUtils;
import vikicc.custom.refresh.LoadMoreListView;
import vikicc.logistics.R;
import vikicc.logistics.adapter.SearchAdapter;
import vikicc.logistics.base.BaseFragment;
import vikicc.logistics.model.ExpressModel;
import vikicc.logistics.ui.activity.MainActivity;
import vikicc.logistics.ui.activity.MainSearchDetailedActivity;

/**
 * Created by liuliu on 2015/08/25   14:33
 *
 * @author 柳伟杰
 * @Email 1031066280@qq.com
 */
public class SearchFragment extends BaseFragment {

    @CodeNote(id = R.id.search_loadmore_view)
    LoadMoreListView loadmore_lv;
    @CodeNote(id = R.id.main_search_txt_search)
    EditText txt_val;
    @CodeNote(id = R.id.main_search_mes)
    TextView main_search_mes;
    SearchAdapter adapter;
    List<ExpressModel> list;//用来存储全部的List(总体数据)
    List<ExpressModel> search_list;//查询出来的存储List(查询出来的数据)
    List<ExpressModel> view_list;//需要显示的List(显示的数据，有页码限制)
    int page = 1;//当前显示的页数
    int no = 4;//每页显示的数据个数
    ExpressModel model;
    View footer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_main_search, container, false);
        FinalActivity.initInjectedView(this, view);
        footer = LayoutInflater.from(MainActivity.mIntaile).inflate(R.layout.listview_footer, container, false);
        search_list = new ArrayList<ExpressModel>();
        view_list = new ArrayList<ExpressModel>();
        setData();
        initEvents();
        initViews();
        return view;
    }

    private void initViews() {
        /*查询的有关内容*/
        txt_val.addTextChangedListener(new TextWatcher() {
            /*根据文本动态改变时，从数据库动态查询*/
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                page = 1;
                view_list = new ArrayList<ExpressModel>();
                initDatas(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        loadmore_lv.setLoadMore(new LoadMoreListView.onLoadListener() {
            @Override
            public void onLoad() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (page * no > view_list.size()) {
                            if (view_list.size() > 1) {
                                VikiccUtils.ToastShort(MainActivity.mIntaile, "无更多数据");
                            }
                            loadmore_lv.setFooterVisible(loadmore_lv.Footer_LoadEnd, 0);
                        } else {
                            page++;
                            initDatas(txt_val.getText().toString());
                            loadmore_lv.setFooterVisible(loadmore_lv.Footer_LoadEnd, 0);
                            loadmore_lv.setSelection(loadmore_lv.getCount() - 1);//加载完更多滚动到底部
                        }
                    }
                }, 3000);
            }
        });
        loadmore_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int mposition = position;
                VikiccUtils.IntentPost(MainActivity.mIntaile, MainSearchDetailedActivity.class, new VikiccUtils.putListener() {
                    @Override
                    public void put(Intent intent) {
                        intent.putExtra("model", view_list.get(mposition));
                    }
                });
            }
        });
    }

    private void initDatas(String search_val) {
        getPageVal(search_val);
        getViewList();
        adapter = new SearchAdapter(view_list, MainActivity.mIntaile);
        loadmore_lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (view_list.size() > 1) {
            loadmore_lv.Footer_noMore = true;
        } else {
            loadmore_lv.Footer_noMore = false;
        }
    }

    private void initEvents() {
        initDatas(txt_val.getText().toString());
    }

    /*获得所有数据（从后台获得数据并存储在list中）*/
    private void setData() {
        list = MainActivity.mIntaile.finalDb.findAll(ExpressModel.class);
        if (list.size() > 0) {
            main_search_mes.setVisibility(View.GONE);
            loadmore_lv.setVisibility(View.VISIBLE);
        } else {
            main_search_mes.setVisibility(View.VISIBLE);
            loadmore_lv.setVisibility(View.GONE);
        }
    }

    /*获得当前需要显示的数据(根据页码显示)*/
    private List<ExpressModel> getViewList() {
        for (int i = (page - 1) * no; i < page * no; i++) {
            if (i < search_list.size()) {
                view_list.add(search_list.get(i));
            }
        }
        return view_list;
    }

    /*根据查询条件进行查询*/
    private void getPageVal(String search_count) {
        if (list != null) {
            search_list = new ArrayList<ExpressModel>();
            if (search_count.trim().equals("")) {
                search_list = list;
            } else {
                for (int i = 0; i < list.size(); i++) {
                    ExpressModel model = list.get(i);
                    if (model.getExpressDeliveryId() == null) {
                        model.setExpressDeliveryId("");
                    }
                    if (model.getSenderLinkway() == null) {
                        model.setSenderLinkway("");
                    }
                    if (model.getReceiverLinkway() == null) {
                        model.setReceiverLinkway("");
                    }
                    if (model.getExpressDeliveryId().contains(search_count.toString().trim()) ||
                            model.getSenderLinkway().contains(search_count.toString().trim()) ||
                            model.getReceiverLinkway().contains(search_count.toString().trim())) {
                        search_list.add(model);
                    }
                }
            }
        }
    }
}
