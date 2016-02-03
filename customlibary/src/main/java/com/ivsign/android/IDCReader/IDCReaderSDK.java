package com.ivsign.android.IDCReader;

import android.content.Context;
import android.os.Environment;

import vikicc.custom.method.VikiccUtils;

/**
 * cvr-100读取身份证方法（文件夹位置不可改变）
 * main文件夹下面jniLibs文件夹下存储解析身份证的 .so
 *
 * @author 柳伟杰
 */
public class IDCReaderSDK {

    private static final String TAG = "unpack";

    public IDCReaderSDK() {
        //if( 0==wltInit("") )
        //Log.i(TAG,  "wltInit success");
    }

    public static int Init(Context context) {
//        if(VikiccUtils.hasSDCard()){
//            return wltInit(Environment.getExternalStorageDirectory() + "/wltlib");
//        }else{
//            return wltInit(Environment.getExternalStorageDirectory() + "/wltlib");
//        }
        //return wltInit(Environment.getExternalStorageDirectory() + "/wltlib");
        //System.out.println(Environment.getExternalStorageDirectory()+"......
        // ..........................................context.getFilesDir().getAbsolutePath()"+context.getFilesDir().getAbsolutePath());
        return wltInit(context.getFilesDir().getAbsolutePath()+ "/wltlib");
    }

    public static int unpack(byte[] wltdata, byte[] licdata) {
        return wltGetBMP(wltdata, licdata);
    }

    // native functin interface
    public static native int wltInit(String workPath);

    public static native int wltGetBMP(byte[] wltdata, byte[] licdata);

    /* this is used to load the 'wltdecode' library on application
     */
    static {
        System.loadLibrary("wltdecode");
    }
}
