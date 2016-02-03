package vikicc.logistics.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
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
import vikicc.custom.spinner.popsipnner.AbstractSpinerAdapter;
import vikicc.custom.spinner.popsipnner.SpinerPopWindow;
import vikicc.logistics.R;
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
 * Created by Administrator on 2015/9/12.
 */
public class QuickAddFragment extends BaseFragment implements AbstractSpinerAdapter.IOnItemSelectListener {
    SpinerPopWindow m;
    private Uri imageFilePath;//图片路径
    ExpressModel expressModel = new ExpressModel();
    private List<String> nameList = new ArrayList<String>();//物流状态数据源
    private List<CodeModel> codes = new ArrayList<CodeModel>();//物流字典
    @CodeNote(id = R.id.qadd_txt_expressNum)
    Lab_ImgEditText qadd_txt_expressNum;//快递单号
    @CodeNote(id = R.id.qadd_img_package, click = "onClick")
    ImageView qadd_img_package;//包裹照片
    @CodeNote(id = R.id.qadd_img_idcard, click = "onClick")
    ImageView qadd_img_idcard;//证件照
    @CodeNote(id = R.id.qadd_img_express, click = "onClick")
    ImageView qadd_img_express;//快递单照片
    @CodeNote(id = R.id.qadd_spinner_expressType)
    Lab_ImgEditText qadd_spinner_expressType;//下拉列表物流状态
    @CodeNote(id = R.id.qadd_isTure)
    CheckBox qadd_isTure;//是否可疑
    boolean[] isCarmer = new boolean[]{false, false, false};//是否拍照
    Dialog dialogs;//对话框
    LinearLayout total_top_ll_right;//右侧添加
    Bitmap[] bitmaps = new Bitmap[3];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_main_quickadd, container, false);
        FinalActivity.initInjectedView(this, view);
        finalDb = FinalDb.create(MainActivity.mIntaile);
        expressModel.setExpressStatus("0");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        expressModel.setSenderTime(sdf.format(new Date()).replaceAll(" ", "T"));
        //保存
        total_top_ll_right = (LinearLayout) mIntaile.findViewById(R.id.total_top_ll_right);
        total_top_ll_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (checkData()) {
                    dialogs = VikiccUtils.ProgressDialog(MainActivity.mIntaile, dialogs, "保存中，请稍候...");
                    dialogs.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getExpress();
                        }
                    }, 2000);
                }
            }
        });
        GetCodes();
        m = new SpinerPopWindow(mIntaile);
        m.refreshData(nameList, 0);
        m.setItemListener(this);
        initEvent();
        return view;
    }

    public void initEvent() {
        //---------快递单号----------
        qadd_txt_expressNum.setEditText(R.mipmap.logistics_search, "快递单号", new Lab_ImgEditText.ImgClickListener() {
            public void IntentPost(boolean result) {
                if (result) {
                    Intent openCameraIntent = new Intent(mIntaile, CaptureActivity.class);
                    startActivityForResult(openCameraIntent, 0);
                    qadd_txt_expressNum.resc = true;
                }
            }//设置Img点击事件
        });
        qadd_spinner_expressType.setEditText(R.mipmap.main_more_message_right, R.mipmap.main_more_message_bottom, "快递状态", new Lab_ImgEditText.ImgClickListener() {
            public void IntentPost(boolean result) {
                if (result) {
                    qadd_spinner_expressType.setTxtRight("bottom");
                    showSpinWindow();
                } else {
                    qadd_spinner_expressType.setTxtRight("left");
                }
            }
        }, nameList, m);
    }

    Bitmap bm = null;
    StringBuffer path = new StringBuffer();

    public void startCamera(int type) {
        // 利用系统自带的相机应用:拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        path = new StringBuffer();
        path.append(MainActivity.mIntaile.getExternalFilesDir(null)).append("/IMG_Logistics_Fast_" + type + ".jpg");
        File file = new File(path.toString());
        Uri uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, type);
    }

    public void onClick(View v) {
        int type = 0;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (v.getId()) {
            case R.id.qadd_img_idcard:
                //证件照
                startCamera(11);
                break;
            case R.id.qadd_img_package:
                startCamera(12);
                //包裹图
                break;
            case R.id.qadd_img_express:
                startCamera(13);
                //运单图
                break;

        }
//        if (type != 0) {
//            imageFilePath = Utils.startCamra1(mIntaile, type);
//            Intent intent = new Intent(mIntaile, CameraActivity.class);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFilePath);
//            startActivityForResult(intent, type);
//        }
    }


    //读取页面数据
    private void getExpress() {
        TerminalModel mb = finalDb.findAll(TerminalModel.class).get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        expressModel.setTerminalId(mb.getTerminalId());
        expressModel.setIsSuspicious(qadd_isTure.isChecked());
        expressModel.setExpressCompanyId(mb.getTerminalCompanyId());
        expressModel.setIsDownLoad(false);
        expressModel.setExpressCompanyPersonnelId(mb.getTerminalPersonnalId());
        expressModel.setExpressDeliveryId(qadd_txt_expressNum.getText().toString());
        ImageObject[] imgs = new ImageObject[3];
        ImageObject imageObject1 = new ImageObject();
        imageObject1.setImageId("ConsignIdentityImageId");

        ImageObject imageObject2 = new ImageObject();
        imageObject2.setImageId("GoodsImageId");

        ImageObject imageObject3 = new ImageObject();
        imageObject3.setImageId("OrderImageId");
        path = new StringBuffer();
        path.append(MainActivity.mIntaile.getExternalFilesDir(null));
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
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
        expressModel.setUploadTime(sdf.format(new Date()).replaceAll(" ", "T"));
        AddExpress(expressModel);
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

    //显示自定义Spinner
    private void showSpinWindow() {
        m.setWidth(qadd_spinner_expressType.getWidth());
        m.showAsDropDown(qadd_spinner_expressType);
    }

    /*自定义spinner的item点击事件*/
    public void onItemClick(int pos) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (pos >= 0 && pos <= nameList.size()) {
            String value = nameList.get(pos);
            qadd_spinner_expressType.setText(value);
            expressModel.setExpressStatus(codes.get(pos).getKey());
            if (codes.get(pos).getKey().equals("0")) {
                expressModel.setSenderTime(sdf.format(new Date()).replaceAll(" ", "T"));
            } else {
                expressModel.setReceiveTime(sdf.format(new Date()).replaceAll(" ", "T"));
            }
        }
    }

    /*检测输入数据*/
    private boolean checkData() {
        if (qadd_txt_expressNum.getText().toString().trim().equals("")) {
            VikiccUtils.ToastShort(MainActivity.mIntaile, "请输入快递单号！");
            return false;
        } else if (!isCarmer[0]) {
            VikiccUtils.ToastShort(MainActivity.mIntaile, "请为运单拍照！");
            return false;
        } else if (!isCarmer[1]) {
            VikiccUtils.ToastShort(MainActivity.mIntaile, "请为包裹拍照！");
            return false;
        } else if (!isCarmer[2]) {
            VikiccUtils.ToastShort(MainActivity.mIntaile, "请为证件拍照！");
            return false;
        }
        return true;
    }

    /*更新字典*/
    private void GetCodes() {
        XmlManager xml = new XmlManager(mIntaile);
        codes = xml.getXml("Code_ExpressStatus.xml");
        if (codes != null) {
            for (int i = 0; i < codes.size(); i++) {
                nameList.add(codes.get(i).getValue());
            }
            if (codes.size() > 0) {
                qadd_spinner_expressType.setText(codes.get(0).getValue());
            }
        }
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
                        expressModel.setIsDownLoad(true);
                        Clear();
                    } else {
                        dialogs.dismiss();
                        VikiccUtils.ToastShort(MainActivity.mIntaile, "保存失败");
                    }
                } else {
                    Clear();
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

    /*接收拍照成功返回的内容。*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            path = new StringBuffer();
            path.append(MainActivity.mIntaile.getExternalFilesDir(null));
            Bitmap bm = null;
            if (requestCode == 11) {
                path.append("/IMG_Logistics_Fast_11.jpg");
                bm = getimage(path.toString());
                qadd_img_idcard.setImageBitmap(bm);
                bitmaps[2] = bm;
                isCarmer[2] = true;
            } else if (requestCode == 12) {
                path.append("/IMG_Logistics_Fast_12.jpg");
                bm = getimage(path.toString());
                qadd_img_package.setImageBitmap(bm);
                bitmaps[1] = bm;
                isCarmer[1] = true;
            } else if (requestCode == 13) {
                path.append("/IMG_Logistics_Fast_13.jpg");
                bm = getimage(path.toString());
                qadd_img_express.setImageBitmap(bm);
                bitmaps[0] = bm;
                isCarmer[0] = true;
            } else {
                String expressNum = data.getStringExtra("result");
                qadd_txt_expressNum.setText(expressNum);
                qadd_txt_expressNum.setSelection(expressNum.length());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*清理页面数据*/
    private void Clear() {
        expressModel = new ExpressModel();
        qadd_txt_expressNum.setText("");
        qadd_img_express.setImageResource(R.mipmap.main_add_img);
        qadd_img_package.setImageResource(R.mipmap.main_add_img);
        qadd_img_idcard.setImageResource(R.mipmap.main_add_img);
    }
}
