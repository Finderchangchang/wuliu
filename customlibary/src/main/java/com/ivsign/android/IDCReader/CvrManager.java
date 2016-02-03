package com.ivsign.android.IDCReader;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import vikicc.custom.model.CvrModel;

/**
 * Created by liuliu on 2015/09/02   18:01
 *
 * @author 柳伟杰
 * @Email 1031066280@qq.com
 */
public class CvrManager {
    BluetoothAdapter myBluetoothAdapter = null;
    BluetoothServerSocket mBThServer = null;
    BluetoothSocket mBTHSocket = null;
    InputStream mmInStream = null;
    OutputStream mmOutStream = null;
    int Readflage = -99;

    byte[] cmd_SAM = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x12, (byte) 0xFF, (byte) 0xEE};
    byte[] cmd_find = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x20, 0x01, 0x22};
    byte[] cmd_selt = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x20, 0x02, 0x21};
    byte[] cmd_read = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x30, 0x01, 0x32};
    byte[] cmd_sleep = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x02, 0x00, 0x02};
    byte[] cmd_weak = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x02, 0x01, 0x03};
    byte[] recData = new byte[1500];

    String DEVICE_NAME1 = "CVR-100B";
    String DEVICE_NAME2 = "IDCReader";
    String DEVICE_NAME3 = "COM2";
    String DEVICE_NAME4 = "BOLUTEK";
    Context mContext;
    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static final String TAG = "IDCReaderSDK";
    public String ett = "";

    public boolean connection(Context context) {
        mContext = context;
        boolean result = false;
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = myBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (Iterator<BluetoothDevice> iterator = pairedDevices.iterator(); iterator.hasNext(); ) {
                BluetoothDevice device = iterator.next();
                if (DEVICE_NAME1.equals(device.getName()) || DEVICE_NAME2.equals(device.getName()) || DEVICE_NAME3.equals(device.getName()) || DEVICE_NAME4.equals(device.getName())) {
                    try {
                        myBluetoothAdapter.enable();
                        Intent discoverableIntent = new Intent(
                                BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);//使得蓝牙处于可发现模式，持续时间150s
                        discoverableIntent.putExtra(
                                BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 150);
                        mBTHSocket = device.createRfcommSocketToServiceRecord(MY_UUID);

                        int sdk = Integer.parseInt(Build.VERSION.SDK);
                        if (sdk >= 10) {
                            mBTHSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                        } else {
                            mBTHSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                        }

                        mBThServer = myBluetoothAdapter.listenUsingRfcommWithServiceRecord("myServerSocket", MY_UUID);
                        mBTHSocket.connect();
                        mmInStream = mBTHSocket.getInputStream();
                        mmOutStream = mBTHSocket.getOutputStream();

                    } catch (IOException e) {
                        ett = "设备连接异常！";
                    }
                    if ((mmInStream != null) && (mmInStream != null)) {
                        //ett = "设备连接成功！";
                        result = true;
                    } else {
                        ett = "设备连接失败！";
                    }
                    break;
                }
            }
        } else {
            ett = "蓝牙未打开";
        }
        return result;
    }

    public CvrModel read() {
        int readcount = 15;
        try {
            while (readcount > 1) {
                CvrModel model = ReadCard();
                readcount = readcount - 1;
                if (Readflage > 0) {
                    readcount = 0;
                    if (Readflage == 1) {
                        FileInputStream fis = new FileInputStream(mContext.getFilesDir().getAbsolutePath() + "/wltlib/zp.bmp");
//                        FileInputStream fis = new FileInputStream("/wltlib/zp.bmp");

                        Bitmap bmp = BitmapFactory.decodeStream(fis);
                        fis.close();
                        model.setCardimg(bmp);
                    } else {
                        ett = "照片解码失败，请检查路径" + mContext.getFilesDir().getAbsolutePath() + "/wltlib/";
                    }
                    return model;
                } else {
                    if (Readflage == -2) {
                        ett = "蓝牙连接异常";
                    }
                    if (Readflage == -3) {
                        ett = "无卡或卡片已读过";
                    }
                    if (Readflage == -4) {
                        ett = "无卡或卡片已读过";
                    }
                    if (Readflage == -5) {
                        ett = "读卡失败";
                    }
                    if (Readflage == -99) {
                        ett = "操作异常";
                    }
                }
            }

        } catch (IOException e) {
            ett = "读取数据异常！";
        }
        return null;
    }

    public void close() {
        try {
            if ((mmInStream == null) || (mmInStream == null)) {
                return;
            }
            mmOutStream.close();
            mmInStream.close();
            mBTHSocket.close();
            mBThServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //  @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    /*执行读卡操作*/
    private CvrModel ReadCard() {
        CvrModel model = new CvrModel();
        try {
            if ((mmInStream == null) || (mmInStream == null)) {
                Readflage = -2;//连接异常
                return null;
            }
            mmOutStream.write(cmd_find);
            Thread.sleep(200);
            int datalen = mmInStream.read(recData);
            if (recData[9] == -97) {
                mmOutStream.write(cmd_selt);
                Thread.sleep(200);
                datalen = mmInStream.read(recData);
                if (recData[9] == -112) {
                    mmOutStream.write(cmd_read);
                    Thread.sleep(1000);
                    byte[] tempData = new byte[1500];
                    if (mmInStream.available() > 0) {
                        datalen = mmInStream.read(tempData);
                    } else {
                        Thread.sleep(500);
                        if (mmInStream.available() > 0) {
                            datalen = mmInStream.read(tempData);
                        }
                    }
                    int flag = 0;
                    if (datalen < 1294) {
                        for (int i = 0; i < datalen; i++, flag++) {
                            recData[flag] = tempData[i];
                        }
                        Thread.sleep(1000);
                        if (mmInStream.available() > 0) {
                            datalen = mmInStream.read(tempData);
                        } else {
                            Thread.sleep(500);
                            if (mmInStream.available() > 0) {
                                datalen = mmInStream.read(tempData);
                            }
                        }
                        for (int i = 0; i < datalen; i++, flag++) {
                            recData[flag] = tempData[i];
                        }

                    } else {
                        for (int i = 0; i < datalen; i++, flag++) {
                            recData[flag] = tempData[i];
                        }
                    }
                    tempData = null;
                    if (flag == 1295) {
                        if (recData[9] == -112) {
                            byte[] dataBuf = new byte[256];
                            for (int i = 0; i < 256; i++) {
                                dataBuf[i] = recData[14 + i];
                            }
                            String TmpStr = new String(dataBuf, "UTF16-LE");
                            TmpStr = new String(TmpStr.getBytes("UTF-8"));
                            model.setName(TmpStr.substring(0, 15));
                            model.setSex(TmpStr.substring(15, 16));
                            model.setNation(TmpStr.substring(16, 18));
                            model.setBirthday(TmpStr.substring(18, 26));
                            model.setAddress(TmpStr.substring(26, 61));
                            model.setIdnumber(TmpStr.substring(61, 79));
                            model.setIssued(TmpStr.substring(79, 94));
                            model.setUsefullife_begin(TmpStr.substring(94, 102));//有限期限开始
                            model.setUsefullife_end(TmpStr.substring(102, 110));//有限期限结束
                            model.setCardimg(null);
//                            ett = "姓名：" + decodeInfo[0] + "\n" + "性别：" + decodeInfo[1] + "\n" + "民族：" + decodeInfo[2] + "\n"
//                                    + "出生日期：" + decodeInfo[3] + "\n" + "地址：" + decodeInfo[4] + "\n" + "身份号码：" + decodeInfo[5] + "\n"
//                                    + "签发机关：" + decodeInfo[6] + "\n" + "有效期限：" + decodeInfo[7] + "-" + decodeInfo[8] + "\n"
//                                    + decodeInfo[9] + "\n";
                            if (model.getSex().equals("1")) {
                                model.setSex("男");
                            } else {
                                model.setSex("女");
                            }
                            try {
                                model.setNation(decodeNation(Integer.parseInt(model.getSex())));
                            } catch (Exception e) {
                                model.setNation("汉");
                            }

                            //照片解码
                            try {
                                //InputStream abpath = getClass().getResourceAsStream("/wltlib/license.lic");
                                //String path = new String(InputStreamToByte(abpath ));
//                                String path = "file:///android_asset";
//                                String fileNames[] =mContext.getAssets().list("assets");
//                                for(int i=0;i<fileNames.length;i++){
//                                    System.out.println(fileNames[i]);
//                                }

                                int ret = IDCReaderSDK.Init(mContext);
                                if (ret == 0) {
                                    byte[] datawlt = new byte[1384];
                                    byte[] byLicData = {(byte) 0x05, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x5B, (byte) 0x03, (byte) 0x33, (byte) 0x01, (byte) 0x5A, (byte) 0xB3, (byte) 0x1E, (byte) 0x00};
                                    for (int i = 0; i < 1295; i++) {
                                        datawlt[i] = recData[i];
                                    }
                                    int t = IDCReaderSDK.unpack(datawlt, byLicData);
                                    if (t == 1) {
                                        Readflage = 1;//读卡成功
                                    } else {
                                        Readflage = 6;//照片解码异常
                                    }
                                } else {
                                    Readflage = 6;//照片解码异常
                                }
                            } catch (Exception e) {
                                Readflage = 6;//照片解码异常
                            }
                        } else {
                            Readflage = -5;//读卡失败！
                        }
                    } else {
                        Readflage = -5;//读卡失败
                    }
                } else {
                    Readflage = -4;//选卡失败
                }
            } else {
                Readflage = -3;//寻卡失败
            }

        } catch (IOException e) {
            Readflage = -99;//读取数据异常
        } catch (InterruptedException e) {
            Readflage = -99;//读取数据异常
        }
        return model;
    }

    private byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }

    /*根据code获得卡上的民族*/
    private String decodeNation(int code) {
        String nation;
        switch (code) {
            case 1:
                nation = "汉";
                break;
            case 2:
                nation = "蒙古";
                break;
            case 3:
                nation = "回";
                break;
            case 4:
                nation = "藏";
                break;
            case 5:
                nation = "维吾尔";
                break;
            case 6:
                nation = "苗";
                break;
            case 7:
                nation = "彝";
                break;
            case 8:
                nation = "壮";
                break;
            case 9:
                nation = "布依";
                break;
            case 10:
                nation = "朝鲜";
                break;
            case 11:
                nation = "满";
                break;
            case 12:
                nation = "侗";
                break;
            case 13:
                nation = "瑶";
                break;
            case 14:
                nation = "白";
                break;
            case 15:
                nation = "土家";
                break;
            case 16:
                nation = "哈尼";
                break;
            case 17:
                nation = "哈萨克";
                break;
            case 18:
                nation = "傣";
                break;
            case 19:
                nation = "黎";
                break;
            case 20:
                nation = "傈僳";
                break;
            case 21:
                nation = "佤";
                break;
            case 22:
                nation = "畲";
                break;
            case 23:
                nation = "高山";
                break;
            case 24:
                nation = "拉祜";
                break;
            case 25:
                nation = "水";
                break;
            case 26:
                nation = "东乡";
                break;
            case 27:
                nation = "纳西";
                break;
            case 28:
                nation = "景颇";
                break;
            case 29:
                nation = "柯尔克孜";
                break;
            case 30:
                nation = "土";
                break;
            case 31:
                nation = "达斡尔";
                break;
            case 32:
                nation = "仫佬";
                break;
            case 33:
                nation = "羌";
                break;
            case 34:
                nation = "布朗";
                break;
            case 35:
                nation = "撒拉";
                break;
            case 36:
                nation = "毛南";
                break;
            case 37:
                nation = "仡佬";
                break;
            case 38:
                nation = "锡伯";
                break;
            case 39:
                nation = "阿昌";
                break;
            case 40:
                nation = "普米";
                break;
            case 41:
                nation = "塔吉克";
                break;
            case 42:
                nation = "怒";
                break;
            case 43:
                nation = "乌孜别克";
                break;
            case 44:
                nation = "俄罗斯";
                break;
            case 45:
                nation = "鄂温克";
                break;
            case 46:
                nation = "德昂";
                break;
            case 47:
                nation = "保安";
                break;
            case 48:
                nation = "裕固";
                break;
            case 49:
                nation = "京";
                break;
            case 50:
                nation = "塔塔尔";
                break;
            case 51:
                nation = "独龙";
                break;
            case 52:
                nation = "鄂伦春";
                break;
            case 53:
                nation = "赫哲";
                break;
            case 54:
                nation = "门巴";
                break;
            case 55:
                nation = "珞巴";
                break;
            case 56:
                nation = "基诺";
                break;
            case 97:
                nation = "其他";
                break;
            case 98:
                nation = "外国血统中国籍人士";
                break;
            default:
                nation = "";
                break;
        }
        return nation;
    }
}
