package vikicc.logistics.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.ivsign.android.IDCReader.CvrManager;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.CodeNote;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Decoder.BASE64Encoder;
import vikicc.custom.edittext.Lab_ImgEditText;
import vikicc.custom.method.VikiccUtils;
import vikicc.custom.model.CvrModel;
import vikicc.custom.spinner.popsipnner.AbstractSpinerAdapter;
import vikicc.custom.spinner.popsipnner.SpinerPopWindow;
import vikicc.logistics.R;
import vikicc.logistics.base.BaiduMapLocationService;
import vikicc.logistics.base.BaseApplication;
import vikicc.logistics.base.BaseFragment;
import vikicc.logistics.base.Utils;
import vikicc.logistics.data.XmlManager;
import vikicc.logistics.model.CodeModel;
import vikicc.logistics.model.ExpressModel;
import vikicc.logistics.model.HttpModel;
import vikicc.logistics.model.ImageObject;
import vikicc.logistics.model.InvokeReturn;
import vikicc.logistics.model.TerminalModel;
import vikicc.logistics.task.AddExpressTask;
import vikicc.logistics.ui.activity.MainActivity;
import vikicc.logistics.zxing.activity.CaptureActivity;

import static vikicc.logistics.ui.activity.MainActivity.mIntaile;

/**
 * 寄件
 * Created by Administrator on 2015/8/19.
 */
public class SendFragment extends BaseFragment implements AbstractSpinerAdapter.IOnItemSelectListener {
    private Uri imageFilePath;//图片路径
    private List<String> nameList = new ArrayList<String>();
    private ExpressModel expressModel = new ExpressModel();
    private String Expresstype = "0";
    SpinerPopWindow m;
    @CodeNote(id = R.id.total_top_txt_title)
    TextView total_title;
    List<CodeModel> types = new ArrayList<CodeModel>();
    @CodeNote(id = R.id.main_txt_expressNum)
    Lab_ImgEditText txt_expressNum;//快递单号
    @CodeNote(id = R.id.main_spinner_expressType)
    Lab_ImgEditText spinner_expressType;//快递类型
    @CodeNote(id = R.id.mian_txt_expressDesc)
    Lab_ImgEditText txt_expressDesc;//快递描述
    @CodeNote(id = R.id.mian_txt_expressDesc_more)
    EditText txt_expressDesc_more;//快递描述输入文本框
    @CodeNote(id = R.id.mian_txt_sendExpress)
    Lab_ImgEditText txt_sendExpress;//寄件
    @CodeNote(id = R.id.mian_txt_consigneExpress)
    Lab_ImgEditText txt_consigneExpress;//收件
    @CodeNote(id = R.id.main_ll_consignee_message)
    LinearLayout ll_consignee;//收件显示隐藏的内容
    @CodeNote(id = R.id.main_txt_sender)
    EditText txt_sender;//寄件人姓名
    @CodeNote(id = R.id.main_txt_consigner)
    EditText txt_consigner;//收件人姓名
    @CodeNote(id = R.id.main_btn_send_read, click = "onClick")
    Button btn_send_read;//寄件读取
    @CodeNote(id = R.id.main_btn_consigner_read, click = "onClick")
    Button btn_consigner_read;//收件读取
    @CodeNote(id = R.id.main_txt_sender_idcard)
    EditText main_txt_sender_idcard;//证件号码
    @CodeNote(id = R.id.mian_txt_sender_tel)
    EditText mian_txt_sender_tel;//联系方式
    @CodeNote(id = R.id.main_txt_sender_address)
    EditText main_txt_sender_address;//寄件地址
    @CodeNote(id = R.id.main_txt_consigner_zipcode)
    EditText main_txt_consigner_zipcode;//邮编
    @CodeNote(id = R.id.main_txt_consigner_idcard)
    EditText main_txt_consigner_idcard;//证件号码
    @CodeNote(id = R.id.main_txt_consigner_tel)
    EditText main_txt_consigner_tel;//联系方式
    @CodeNote(id = R.id.main_txt_postcode)
    TextView main_txt_postcode;//邮编文字
    @CodeNote(id = R.id.main_txt_consigner_address)
    EditText main_txt_consigner_address;//收件地址
    @CodeNote(id = R.id.cb_isTure)
    CheckBox cb_isTure;//是否可疑
    boolean res_desc = false;
    boolean res_send = false;
    boolean res_consign = false;
    @CodeNote(id = R.id.mian_img_express, click = "onClick")
    ImageView mian_img_express;//运单图
    @CodeNote(id = R.id.main_img_package, click = "onClick")
    ImageView main_img_package;//包裹图
    @CodeNote(id = R.id.main_img_idcard, click = "onClick")
    ImageView main_img_idcard;//证件照
    @CodeNote(id = R.id.main_txt_goodsCount)
    Lab_ImgEditText txt_goodscount;//货物数量
    LinearLayout total_top_ll_right;//右侧添加
    @CodeNote(id = R.id.frag_ll_sender)
    LinearLayout ll_main_send;
    @CodeNote(id = R.id.frag_ll_senders)
    LinearLayout ll_main_sends;
    @CodeNote(id = R.id.main_ll_send_express)
    LinearLayout ll_send;//寄件显示隐藏的内容
    /*-----------寄件人s相关组件----------*/
    @CodeNote(id = R.id.main_ll_sends_express)
    LinearLayout ll_sends;
    @CodeNote(id = R.id.mian_txt_sendsExpress)
    Lab_ImgEditText txt_sendsExpress;//寄件人s
    @CodeNote(id = R.id.main_txt_senders)
    EditText txt_senders;//寄件人输入框
    @CodeNote(id = R.id.main_txt_senders_idcard)
    EditText txt_senders_idcard;//寄件人身份证号
    @CodeNote(id = R.id.mian_txt_senders_tel)
    EditText txt_senders_tel;//寄件人手机号
    @CodeNote(id = R.id.main_txt_senders_address)
    EditText txt_senders_address;
    CvrManager cvrManager = new CvrManager();
    Dialog dialog;
    boolean isPostFrag = true;
    boolean isCheckCardsender = true;
    boolean isCheckCardR = true;
    boolean[] isCarmer = new boolean[]{false, false, false};
    Dialog dialogs;
    @CodeNote(id = R.id.send_ll_card)
    LinearLayout send_ll_card;
    @CodeNote(id = R.id.send_ll_sendcard)
    LinearLayout send_ll_sendcard;
    @CodeNote(id = R.id.send_ll_send2card)
    LinearLayout send_ll_send2card;
    Bitmap[] bitmaps = new Bitmap[3];
    String imageFileUri;
    @CodeNote(id = R.id.refresh_address_send_iv, click = "onClick")
    ImageView refresh_address_send_iv;
    @CodeNote(id = R.id.refresh_address_get_iv, click = "onClick")
    ImageView refresh_address_get_iv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.frag_main_send, container, false);
        FinalActivity.initInjectedView(this, viewRoot);

        spinnerManage();
        finalDb = MainActivity.mIntaile.finalDb;
        total_top_ll_right = (LinearLayout) mIntaile.findViewById(R.id.total_top_ll_right);
        total_top_ll_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (checkData()) {
                    dialogs = VikiccUtils.ProgressDialog(MainActivity.mIntaile, dialogs, "保存中，请稍候...");
                    dialogs.show();
                    getExpress();
                }
            }
        });
        setButtonVisible();
        initEvents();
        return viewRoot;
    }

    /*自定义Spinner显示数据以及绑定*/
    private void spinnerManage() {
        GetCodes();
        expressModel.setExpressType("1");
        m = new SpinerPopWindow(mIntaile);
        m.refreshData(nameList, 0);
        m.setItemListener(this);
    }

    private TextWatcher Textweather = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!Utils.checkCardId(main_txt_sender_idcard.getText().toString().trim())) {
                main_txt_sender_idcard.setTextColor(Color.RED);
                isCheckCardsender = false;
            } else {
                main_txt_sender_idcard.setTextColor(Color.BLACK);
                isCheckCardsender = true;
            }
        }
    };
    private TextWatcher Textweather1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!Utils.checkCardId(txt_senders_idcard.getText().toString().trim())) {
                txt_senders_idcard.setTextColor(Color.RED);
                isCheckCardsender = false;
            } else {
                txt_senders_idcard.setTextColor(Color.BLACK);
                isCheckCardsender = true;
            }
        }
    };
    private TextWatcher Textweather2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!Utils.checkCardId(main_txt_consigner_idcard.getText().toString().trim())) {
                main_txt_consigner_idcard.setTextColor(Color.RED);
                isCheckCardR = false;
            } else {
                main_txt_consigner_idcard.setTextColor(Color.BLACK);
                isCheckCardR = true;
            }
        }
    };

    public void initEvents() {

        //拍照
        //-------物流类型-------
        spinner_expressType.setKeyListener(null);
        spinner_expressType.setEditText(R.mipmap.main_more_message_right, R.mipmap.main_more_message_bottom, "物流类型", new Lab_ImgEditText.ImgClickListener() {
            public void IntentPost(boolean result) {
                closeAll();
                if (result) {
                    spinner_expressType.setTxtRight("bottom");
                    showSpinWindow();
                } else {
                    spinner_expressType.setTxtRight("left");
                }
            }
        }, nameList, m);
        //---------快递单号----------
        txt_expressNum.setEditText(R.mipmap.logistics_search, "快递单号", new Lab_ImgEditText.ImgClickListener() {
            public void IntentPost(boolean result) {
                closeAll();
                if (result) {
                    Intent openCameraIntent = new Intent(mIntaile, CaptureActivity.class);
                    startActivityForResult(openCameraIntent, 0);
                    txt_expressNum.resc = true;
                }
            }//设置Img点击事件
        });
        //---------快递描述-----------
        txt_expressDesc.setEditText(R.mipmap.main_more_message_right, R.mipmap.main_more_message_bottom, "快递描述", new Lab_ImgEditText.ImgClickListener() {
            public void IntentPost(boolean result) {
                closeAll();
                if (result) {
                    res_desc = true;
                    txt_expressDesc.setTxtRight("bottom");
                    txt_expressDesc_more.setVisibility(View.VISIBLE);
                    txt_expressDesc_more.setFocusableInTouchMode(true);
                    txt_expressDesc_more.setFocusable(true);
                    txt_expressDesc_more.requestFocus();
                } else {
                    res_desc = false;
                    txt_expressDesc.setTxtRight("left");
                    txt_expressDesc_more.setVisibility(View.GONE);
                }
            }
        });
        //-------寄件人----------
        txt_sendExpress.setEditText(R.mipmap.main_more_message_right, R.mipmap.main_more_message_bottom, "寄  件  人", new Lab_ImgEditText.ImgClickListener() {
            public void IntentPost(boolean result) {
                closeAll();
                if (result) {
                    res_send = true;
                    txt_sendExpress.setTxtRight("bottom");
                    txt_sender.setVisibility(View.VISIBLE);
                    txt_sender.setFocusableInTouchMode(true);
                    txt_sender.setFocusable(true);
                    txt_sender.requestFocus();
                    ll_send.setVisibility(View.VISIBLE);
                } else {
                    res_send = false;
                    txt_sendExpress.setTxtRight("left");
                    ll_send.setVisibility(View.GONE);
                }
            }
        });
        //-------寄件人----------
        txt_sendsExpress.setEditText(R.mipmap.main_more_message_right, R.mipmap.main_more_message_bottom, "寄  件  人", new Lab_ImgEditText.ImgClickListener() {
            public void IntentPost(boolean result) {
                closeAll();
                if (result) {
                    res_send = true;
                    txt_sendsExpress.setTxtRight("bottom");
                    txt_senders.setVisibility(View.VISIBLE);
                    txt_senders.setFocusableInTouchMode(true);
                    txt_senders.setFocusable(true);
                    txt_senders.requestFocus();
                    ll_sends.setVisibility(View.VISIBLE);
                } else {
                    res_send = false;
                    txt_sendExpress.setTxtRight("left");
                    ll_sends.setVisibility(View.GONE);
                }
            }
        });
        //---------收件人--------------
        txt_consigneExpress.setEditText(R.mipmap.main_more_message_right, R.mipmap.main_more_message_bottom, "收  件  人", new Lab_ImgEditText.ImgClickListener() {
            public void IntentPost(boolean result) {
                closeAll();
                if (result) {
                    res_consign = true;
                    txt_consigneExpress.setTxtRight("bottom");
                    txt_consigner.setVisibility(View.VISIBLE);
                    txt_consigner.setFocusableInTouchMode(true);
                    txt_consigner.setFocusable(true);
                    txt_consigner.requestFocus();
                    ll_consignee.setVisibility(View.VISIBLE);
                } else {
                    res_consign = false;
                    txt_consigneExpress.setTxtRight("left");
                    ll_consignee.setVisibility(View.GONE);
                }
            }
        });
        txt_senders_idcard.addTextChangedListener(Textweather1);
        main_txt_sender_idcard.addTextChangedListener(Textweather);
        main_txt_consigner_idcard.addTextChangedListener(Textweather2);
        txt_goodscount.setEditText("快递数量", "个");
        txt_goodscount.setPadding(155, 0, 0, 0);
    }

    /*添加物流信息*/
    private void AddExpress(final ExpressModel expressModel) {
        new AddExpressTask().start(new AddExpressTask.AddExpressListener() {
            public void onCheckResult(boolean result, InvokeReturn invokeReturn) {
                expressModel.setIsDownLoad(false);
                if (invokeReturn != null) {
                    if (invokeReturn.getSuccess().equals("true")) {
                        dialogs.dismiss();
                        VikiccUtils.ToastShort(MainActivity.mIntaile, "保存成功");
                        Clear();
                        expressModel.setIsDownLoad(true);
                    } else {
                        dialogs.dismiss();
                        VikiccUtils.ToastShort(MainActivity.mIntaile, "保存失败");
                    }
                } else {
                    dialogs.dismiss();
                    VikiccUtils.ToastShort(MainActivity.mIntaile, "本地保存成功！");
                    Clear();
                }
                if (expressModel.getImages()[0].getImageData() != null) {
                    expressModel.setConsignIdentityImageId(Utils.encodeBitmap(expressModel.getImages()[0].getImageData()));
                }
                expressModel.setGoodsImageId(Utils.encodeBitmap(expressModel.getImages()[1].getImageData()));
                expressModel.setOrderImageId(Utils.encodeBitmap(expressModel.getImages()[2].getImageData()));
                finalDb.save(expressModel);
            }

            public HttpModel setHttpModel() {
                httpModel = MainActivity.mIntaile.getHttpModel();
                httpModel.setIsPost(true);
                try {
                    String xml = new BASE64Encoder().encode(expressModel.Serialization().getBytes("UTF-8"));
                    xml = xml.replaceAll("\\+", "%2B");
                    xml = xml.replaceAll("\\=", "%3D");
                    httpModel.setInformation(String.format("Express=%s", xml));
                    System.out.println("new base:" + expressModel.Serialization());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                httpModel.setObjective("MobileAddCompanyExpress");
                return httpModel;
            }
        });

    }

    //读取页面数据
    private void getExpress() {
        TerminalModel mb = new TerminalModel();
        mb = finalDb.findAll(TerminalModel.class).get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        expressModel.setExpressDescription(txt_expressDesc_more.getText().toString());
        expressModel.setIsSuspicious(cb_isTure.isChecked());
        expressModel.setExpressCompanyId(mb.getTerminalCompanyId());//giding
        expressModel.setIsDownLoad(false);
        expressModel.setExpressCompanyPersonnelId(mb.getTerminalPersonnalId());//guding
        expressModel.setExpressDeliveryId(txt_expressNum.getText().toString());
        expressModel.setExpressNumber(Integer.valueOf(txt_goodscount.getText().toString()));//货物数量
        expressModel.setExpressStatus(Expresstype);
        ImageObject[] imgs = new ImageObject[3];
        ImageObject imageObject1 = new ImageObject();
        imageObject1.setImageId("ConsignIdentityImageId");

        ImageObject imageObject2 = new ImageObject();
        imageObject2.setImageId("GoodsImageId");

        ImageObject imageObject3 = new ImageObject();
        imageObject3.setImageId("OrderImageId");
        if (isCarmer[2]) {
            imageObject1.setImageData(bitmaps[2]);
        }
        if (isCarmer[1]) {
            imageObject2.setImageData(bitmaps[1]);
        }
        if (isCarmer[0]) {
            imageObject3.setImageData(bitmaps[0]);
        }
        imgs[0] = imageObject1;
        imgs[1] = imageObject2;
        imgs[2] = imageObject3;
        expressModel.setImages(imgs);
        expressModel.setReceiveAddress(main_txt_consigner_address.getText().toString());
        expressModel.setReceiverIdentityNumber(main_txt_consigner_idcard.getText().toString());
        expressModel.setReceiverIdentityType("11");
        expressModel.setReceiverLinkway(main_txt_consigner_tel.getText().toString());
        expressModel.setReceiverName(txt_consigner.getText().toString());
        expressModel.setReceiverPostcode(main_txt_consigner_zipcode.getText().toString());
        if (isPostFrag) {//寄件
            expressModel.setExpressStatus("0");
            expressModel.setSendAddress(main_txt_sender_address.getText().toString());
            expressModel.setSenderLinkway(mian_txt_sender_tel.getText().toString());
            expressModel.setSenderName(txt_sender.getText().toString());
            expressModel.setSenderIdentityNumber(main_txt_sender_idcard.getText().toString());
        } else {//收件
            expressModel.setExpressStatus("1");
            expressModel.setSendAddress(txt_senders_address.getText().toString());
            expressModel.setSenderLinkway(txt_senders_tel.getText().toString());
            expressModel.setSenderName(txt_senders.getText().toString());
            expressModel.setSenderIdentityNumber(txt_senders_idcard.getText().toString());
        }
        expressModel.setSenderIdentityType("11");
        expressModel.setTerminalId(mb.getTerminalId());
        expressModel.setUploadTime(sdf.format(new Date()).replaceAll(" ", "T"));
        AddExpress(expressModel);
    }

    //关闭所有的需要隐藏的内容（并将图片箭头设置为向右）
    private void closeAll() {
        ll_consignee.setVisibility(View.GONE);
        ll_send.setVisibility(View.GONE);
        txt_expressDesc_more.setVisibility(View.GONE);
        //保存快递描述的相关信息
        String txt = txt_expressDesc_more.getText().toString();
        /*如果屏幕宽度小于480设置显示详细，控制缩略显示的长度*/
        if (VikiccUtils.getScannerWidth(mIntaile) > 480) {//解决屏幕适配问题
            if (txt.length() > 20) {
                txt = txt.substring(0, 20) + "...";
            }
        } else {
            if (txt.length() > 15) {
                txt = txt.substring(0, 15) + "...";
            }
        }
        txt_expressDesc.setText(txt);
        /*设置“快递描述“右侧图片箭头向右*/
        if (res_desc) {
            txt_expressDesc.setTxtRight("left");
            res_desc = false;
        }
        if (res_send) {
            txt_sendExpress.setText(txt_sender.getText().toString().trim());
            txt_sendExpress.setTxtRight("left");
            txt_sendsExpress.setText(txt_senders.getText().toString().trim());
            res_send = false;
        }
        if (res_consign) {
            txt_consigneExpress.setText(txt_consigner.getText());
            txt_consigneExpress.setTxtRight("left");
            res_consign = false;
        }
    }

    //显示自定义Spinner
    private void showSpinWindow() {
        m.setWidth(spinner_expressType.getWidth());
        m.showAsDropDown(spinner_expressType);
    }

    /*自定义spinner的item点击事件*/
    public void onItemClick(int pos) {
        if (pos >= 0 && pos <= nameList.size()) {
            String value = nameList.get(pos);
            spinner_expressType.setText(value);
            expressModel.setExpressType(types.get(pos).getKey());
        }
    }

    /*更新字典*/
    private void GetCodes() {
        XmlManager xml = new XmlManager(mIntaile);
        types = xml.getXml("Code_ExpressType.xml");
        if (types != null) {
            for (int i = 0; i < types.size(); i++) {
                nameList.add(types.get(i).getValue());
            }
            if (types.size() > 0) {
                spinner_expressType.setText(types.get(0).getValue());
            }
        }
    }

    /*Fragment的控件点击事件控制（仅限于本Fragment）*/
    public void onClick(View v) {
        int type = 0;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (v.getId()) {
            case R.id.main_img_idcard:
                //证件照
                startCamera(11);
                break;
            case R.id.main_img_package:
                startCamera(12);
                //包裹图
                break;
            case R.id.mian_img_express:
                startCamera(13);
                //运单图
                break;
            case R.id.main_btn_send_read://寄件人身份证读取按钮
                startRead();
                break;
            case R.id.main_btn_consigner_read://收件人身份证读取按钮
                startRead();
                break;
            case R.id.refresh_address_send_iv://寄件地址刷新
                if (locationService != null) {
                    locationService.start();
                } else {
                    VikiccUtils.ToastShort(MainActivity.mIntaile, "定位失败！");
                }
                break;
            case R.id.refresh_address_get_iv://收件地址刷新
                if (locationService != null) {
                    locationService.start();
                } else {
                    VikiccUtils.ToastShort(MainActivity.mIntaile, "定位失败！");
                }
                break;
        }
    }

    private boolean isSend = false;//true,寄件。false,收件
    public BaiduMapLocationService locationService;

    @Override
    public void onStart() {
        super.onStart();
        locationService = ((BaseApplication) MainActivity.mIntaile.getApplication()).locationService;
        locationService.registerListener(mBaiduListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
    }

    /**
     * 定位结果回调，重写onReceiveLocation方法
     */
    private BDLocationListener mBaiduListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                String address = location.getAddrStr();
                if (address != "") {
                    if (isSend) {//寄件地址
                        main_txt_sender_address.setText(address);
                    } else {//收件地址
                        main_txt_consigner_address.setText(address);
                    }
                }
            }
            locationService.stop();
        }
    };

    StringBuffer path = new StringBuffer();

    public void startCamera(int type) {
        // 利用系统自带的相机应用:拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        path = new StringBuffer();
        path.append(MainActivity.mIntaile.getExternalFilesDir(null)).append("/IMG_Logistics_" + type + ".jpg");
        File file = new File(path.toString());
        Uri uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, type);
    }

    /*点击按钮进行读取证件照信息*/
    private void startRead() {
        dialog = VikiccUtils.ProgressDialog(MainActivity.mIntaile, dialog, "读卡中，请稍候...");
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (cvrManager.connection(MainActivity.mIntaile)) {
                    cvrManager.ett = "";
                    CvrModel model = cvrManager.read();
                    if (model != null) {
                        if (isPostFrag) {
                            txt_sender.setText(model.getName().trim());
                            main_txt_sender_idcard.setText(model.getIdnumber().trim());
                            main_txt_sender_idcard.requestFocus();
                            main_txt_sender_idcard.setSelection(model.getIdnumber().trim().length());
                        } else {
                            txt_consigner.setText(model.getName().trim());
                            main_txt_consigner_idcard.setText(model.getIdnumber().trim());
                            main_txt_consigner_idcard.requestFocus();
                            main_txt_consigner_idcard.setSelection(model.getIdnumber().trim().toString().length());
                        }
                        if (model.getCardimg() != null) {
                            main_img_idcard.setImageBitmap(model.getCardimg());
                            bitmaps[2] = model.getCardimg();
                            isCarmer[2] = true;
                        }
                        cvrManager.close();
                        dialog.dismiss();
                    }
                } else {
                    dialog.dismiss();
                    VikiccUtils.ToastShort(mIntaile, cvrManager.ett);
                }
            }
        }, 2000);

        if (cvrManager.ett != null && cvrManager.ett.trim() != "") {
            dialog.dismiss();
            VikiccUtils.ToastShort(mIntaile, cvrManager.ett);
        }
    }

    /*接收拍照成功返回的内容。*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        path = new StringBuffer();
        path.append(MainActivity.mIntaile.getExternalFilesDir(null));
        if (resultCode == -1) {
            Bitmap bm = null;
            if (requestCode == 11) {
                path.append("/IMG_Logistics_11.jpg");
                bm = getimage(path.toString());
                main_img_idcard.setImageBitmap(bm);
                bitmaps[2] = getimage(path.toString());
                System.gc();
                isCarmer[2] = true;
            } else if (requestCode == 12) {
                path.append("/IMG_Logistics_12.jpg");
                bm = getimage(path.toString());
                main_img_package.setImageBitmap(bm);
                bitmaps[1] = getimage(path.toString());
                System.gc();
                isCarmer[1] = true;
            } else if (requestCode == 13) {
                path.append("/IMG_Logistics_13.jpg");
                bm = getimage(path.toString());
                mian_img_express.setImageBitmap(bm);
                bitmaps[0] = bm;
                System.gc();
                isCarmer[0] = true;
            } else {
                String expressNum = data.getStringExtra("result");
                txt_expressNum.setText(expressNum);
                txt_expressNum.setSelection(expressNum.length());
            }
        }
    }

    private Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /*控制按钮显示隐藏（即寄件，收件页面跳转效果）*/
    private void setButtonVisible() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        total_title = (TextView) MainActivity.mIntaile.findViewById(R.id.total_top_txt_title);
        /*isPostFrag结果为true，寄件。false，收件*/
        if (total_title.getText().toString().trim().equals("添加揽件信息")) {
            isPostFrag = true;
            isSend = true;
            main_txt_sender_address.setText(MainActivity.mIntaile.address_main);
            ll_main_send.setVisibility(View.VISIBLE);
            ll_main_sends.setVisibility(View.GONE);
            main_txt_postcode.setVisibility(View.VISIBLE);
            main_txt_consigner_zipcode.setVisibility(View.VISIBLE);
            btn_consigner_read.setVisibility(View.GONE);
            expressModel.setExpressStatus("0");
            send_ll_card.setVisibility(View.GONE);
            expressModel.setSenderTime(sdf.format(new Date()).replaceAll(" ", "T"));
            refresh_address_send_iv.setVisibility(View.VISIBLE);
            refresh_address_get_iv.setVisibility(View.GONE);
        } else if (total_title.getText().toString().trim().equals("添加送件信息")) {
            isPostFrag = false;
            isSend = false;
            main_txt_consigner_address.setText(MainActivity.mIntaile.address_main);

            ll_main_send.setVisibility(View.GONE);
            ll_main_sends.setVisibility(View.VISIBLE);
            main_txt_postcode.setVisibility(View.GONE);
            main_txt_consigner_zipcode.setVisibility(View.GONE);
            btn_consigner_read.setVisibility(View.VISIBLE);
            expressModel.setExpressStatus("1");
            send_ll_send2card.setVisibility(View.GONE);
            expressModel.setReceiveTime(sdf.format(new Date()).replaceAll(" ", "T"));
            refresh_address_send_iv.setVisibility(View.GONE);
            refresh_address_get_iv.setVisibility(View.VISIBLE);
        }
    }

    /*检测输入数据*/
    private boolean checkData() {
        String sender = txt_sender.getText().toString().trim();
        String senders = txt_senders.getText().toString().trim();
        if (txt_expressNum.getText().toString().trim().equals("")) {
            VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入快递单号！");
            return false;
        }
        if (isPostFrag) {
            if (sender.equals("") && senders.equals("")) {
                VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入寄件人姓名！");
                return false;

            } else if (main_txt_sender_idcard.getText().toString().trim().equals("") && txt_senders_idcard.getText().toString().trim().equals("")) {
                VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入寄件人身份证号码！");
                return false;

            } else if (!isCheckCardsender) {
                VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入正确的寄件人身份证号码！");
                return false;
            } else if (main_txt_sender_address.getText().toString().trim().equals("") && txt_senders_address.getText().toString().trim().equals("")) {
                VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入寄件地址！");
                return false;
            } else if (txt_consigner.getText().toString().trim().equals("")) {
                VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入收件人姓名！");
                return false;
            } else if (main_txt_consigner_zipcode.getText().toString().trim().equals("")) {
                VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入收件地邮编！");
                return false;

            } else if (!main_txt_consigner_idcard.getText().toString().trim().equals("")) {
                if (!isCheckCardR) {
                    VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入正确的收件人身份证号码！");
                    return false;
                }
            } else if (main_txt_consigner_address.getText().toString().trim().equals("")) {
                VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入收件人地址！");
                return false;
            }
        } else {
            if (txt_consigner.getText().toString().trim().equals("")) {
                VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入收件人姓名！");
                return false;
            } else if (main_txt_consigner_idcard.getText().toString().trim().equals("")) {
                VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入收件人证件号码！");
                return false;
            } else if (!isCheckCardR) {
                VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入正确的收件人身份证号码！");
                return false;
            } else if (main_txt_consigner_tel.getText().toString().trim().equals("")) {
                VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入收件人联系电话！");
                return false;
            } else if (main_txt_consigner_address.getText().toString().trim().equals("")) {
                VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入收件人地址！");
                return false;
            } else if (sender.equals("") && senders.equals("")) {
                VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入寄件人姓名！");
                return false;

            } else if ((!main_txt_sender_idcard.getText().toString().trim().equals("")) || (!txt_senders_idcard.getText().toString().trim().equals(""))) {
                if (!isCheckCardsender) {
                    VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入正确的寄件人身份证号码！");
                    return false;
                }
            } else if (mian_txt_sender_tel.getText().toString().trim().equals("") && txt_senders_tel.getText().toString().trim().equals("")) {
                VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入寄件人联系方式！");
                return false;
            } else if (main_txt_sender_address.getText().toString().trim().equals("") && txt_senders_address.getText().toString().trim().equals("")) {
                VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入寄件地址！");
                return false;
            }
        }
        if (txt_goodscount.getText().toString().trim().equals("")) {
            VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入货物数量！");
            return false;
        } else if (!isCarmer[0]) {
            VikiccUtils.ToastShort(MainActivity.mIntaile, "请为运单拍照！");
            return false;
        } else if (!isCarmer[1]) {
            VikiccUtils.ToastShort(MainActivity.mIntaile, "请为包裹拍照！");
            return false;
        } else if (!isCarmer[2]) {
            VikiccUtils.ToastShort(MainActivity.mIntaile, "请为人员拍照！");
            return false;
        }
        return true;
    }

    /*清理页面数据*/
    private void Clear() {
        String status = expressModel.getExpressStatus();
        expressModel = new ExpressModel();
        expressModel.setExpressStatus(status);
        txt_expressNum.setText("");
        expressModel.setExpressType("1");
        spinner_expressType.setText(types.get(0).getValue());
        txt_expressDesc.setText("");
        txt_expressDesc_more.setText("");
        txt_sender.setText("");
        txt_sendExpress.setText("");
        txt_consigner.setText("");
        main_txt_sender_idcard.setText("");
        mian_txt_sender_tel.setText("");
        main_txt_sender_address.setText("");
        txt_sendsExpress.setText("");
        txt_senders.setText("");
        txt_senders_idcard.setText("");
        txt_senders_tel.setText("");
        txt_senders_address.setText("");
        txt_consigneExpress.setText("");
        main_txt_consigner_zipcode.setText("");
        main_txt_consigner_idcard.setText("");
        main_txt_consigner_tel.setText("");
        main_txt_consigner_zipcode.setText("");
        main_txt_consigner_address.setText("");
        cb_isTure.setChecked(false);
        txt_goodscount.setText("");
        mian_img_express.setImageResource(R.mipmap.main_add_img);
        main_img_package.setImageResource(R.mipmap.main_add_img);
        main_img_idcard.setImageResource(R.mipmap.main_add_img);
    }
}
