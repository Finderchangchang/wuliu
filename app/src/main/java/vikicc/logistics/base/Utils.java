package vikicc.logistics.base;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import vikicc.custom.method.VikiccUtils;

/**
 * Created by Administrator on 2015/8/21.
 */
public class Utils {
    private static String SALT = "Kiwi";
    private static String CHARSET = "UTF-8";

    /**
     * 从assets目录中复制整个文件夹内容
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldPath String  原文件路径  如：/aa
     * @param newPath String  复制后路径  如：xx:/bb/cc
     */
    public static void copyFilesFassets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //如果捕捉到错误则通知UI线程
            //MainActivity.handler.sendEmptyMessage(COPY_FALSE);
        }
    }

    public static void FileCopy(Context context, InputStream in, String name) {
        try {
            File paths = new File(context.getFilesDir().getAbsolutePath() + "/wltlib/");
            String p = context.getFilesDir().getAbsolutePath();
            if (!paths.isDirectory()) {
                paths.mkdir();
            }
            File path_xml = new File(paths, name);
            if (!path_xml.exists()) {
                FileOutputStream fos = new FileOutputStream(path_xml);
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = in.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                in.close();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void CopyWltlib(Context context) {
        InputStream lic = context.getClass().getClassLoader().getResourceAsStream("assets/wltlib/license.lic");
        InputStream dat = context.getClass().getClassLoader().getResourceAsStream("assets/wltlib/base.dat");
        FileCopy(context, lic, "license.lic");
        FileCopy(context, dat, "base.dat");
    }

    public static String XMlEncode(String strData) {
        if (strData == null)
            return "";

        strData = strData.replace("&", "&amp;");
        strData = strData.replace("<", "&lt;");
        strData = strData.replace(">", "&gt;");
        strData = strData.replace("&apos;", "&apos;");
        strData = strData.replace("\"", "&quot;");
        return strData;
    }

    public static String encodeBitmap(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
                    .trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Bitmap getBitmapFromByte(byte[] temp) {
        if (temp != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        } else {
            return null;
        }
    }

    public static String hashPassword(String password)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        password = password.toUpperCase().trim();
        for (int i = 0; i < 3; i++) {
            password = new String(md5(password + SALT)).substring(0, 20);
        }
        return password;
    }

    public static String md5(String source) throws UnsupportedEncodingException {
        return new String(md5(source.getBytes(CHARSET)));
    }

    public static byte[] md5(byte[] source) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();
            byte str[] = new byte[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = (byte) hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = (byte) hexDigits[byte0 & 0xf];
            }
            return str;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap savePhoto(Activity context, Uri uri) {

        try {
            // 首先取得屏幕对象
            Display display = context.getWindowManager().getDefaultDisplay();
            // 获取屏幕的宽和高
            int dw = (int) (display.getWidth() * 0.6f);
            int dh = (int) (display.getHeight() * 0.6f);
            /**
             * 为了计算缩放的比例，我们需要获取整个图片的尺寸，而不是图片
             * BitmapFactory.Options类中有一个布尔型变量inJustDecodeBounds，将其设置为true
             * 这样，我们获取到的就是图片的尺寸，而不用加载图片了。
             * 当我们设置这个值的时候，我们接着就可以从BitmapFactory.Options的outWidth和outHeight中获取到值
             */
            BitmapFactory.Options op = new BitmapFactory.Options();
            // op.inSampleSize = 8;
            op.inJustDecodeBounds = true;
            // Bitmap pic = BitmapFactory.decodeFile(imageFilePath,
            // op);//调用这个方法以后，op中的outWidth和outHeight就有值了
            // 由于使用了MediaStore存储，这里根据URI获取输入流的形式
            Bitmap pic = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, op);
            int wRatio = (int) Math.ceil(op.outWidth / (float) dw); // 计算宽度比例
            int hRatio = (int) Math.ceil(op.outHeight / (float) dh); // 计算高度比例

            /**
             * 接下来，我们就需要判断是否需要缩放以及到底对宽还是高进行缩放。 如果高和宽不是全都超出了屏幕，那么无需缩放。
             * 如果高和宽都超出了屏幕大小，则如何选择缩放呢》 这需要判断wRatio和hRatio的大小
             * 大的一个将被缩放，因为缩放大的时，小的应该自动进行同比率缩放。 缩放使用的还是inSampleSize变量
             */
            if (wRatio > 1 && hRatio > 1) {
                if (wRatio > hRatio) {
                    op.inSampleSize = wRatio;
                } else {
                    op.inSampleSize = hRatio;
                }
            }
            op.inJustDecodeBounds = false; // 注意这里，一定要设置为false，因为上面我们将其设置为true来获取图片尺寸了
            pic = BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(uri), null, op);

            return pic;
        } catch (Exception e) {
            //Log.i("TEST", e.toString());
        }
        return null;
    }

    public static Uri startCamra1(Activity context, int requestCode) {
        // Intent intent = new Intent(context, CameraActivity.class);
        //这里我们插入一条数据，ContentValues是我们希望这条记录被创建时包含的数据信息
        //这些数据的名称已经作为常量在MediaStore.Images.Media中,有的存储在MediaStore.MediaColumn中了
        //ContentValues values = new ContentValues();

        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "testing");
        values.put(MediaStore.Images.Media.DESCRIPTION, "this is description");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        //Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Uri uri = Uri.fromFile(new File(getDataPath() + "/temp/" + System.currentTimeMillis() + ".tmp"));
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, uri); //这样就将文件的存储方式和uri指定到了Camera应用中

        //由于我们需要调用完Camera后，可以返回Camera获取到的图片，
        //所以，我们使用startActivityForResult来启动Camera
        //context.startActivityForResult(intent, requestCode);
        return uri;
    }

    public static Uri startCamra(Activity context, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //这里我们插入一条数据，ContentValues是我们希望这条记录被创建时包含的数据信息
        //这些数据的名称已经作为常量在MediaStore.Images.Media中,有的存储在MediaStore.MediaColumn中了
        //ContentValues values = new ContentValues();

        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "testing");
        values.put(MediaStore.Images.Media.DESCRIPTION, "this is description");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        //Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Uri uri = Uri.fromFile(new File(getDataPath() + "/temp/" + System.currentTimeMillis() + ".tmp"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri); //这样就将文件的存储方式和uri指定到了Camera应用中

        //由于我们需要调用完Camera后，可以返回Camera获取到的图片，
        //所以，我们使用startActivityForResult来启动Camera
        context.startActivityForResult(intent, requestCode);
        return uri;
    }

    public static String getDataPath() {
        File defaultStorageFile = Environment.getExternalStorageDirectory();
        return String.format("%s/Logistics", defaultStorageFile.getAbsolutePath());
    }

    public static String getDataPath(String name) {
        return String.format("%s/%s", getDataPath(), name);
    }

    public static void ToastLong(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void ToastLong(View v, String text) {
        ToastLong(v.getContext(), text);
    }

    public static boolean checkCardId(String card) {
        try {

            if (!VikiccUtils.isEmptyString(card)) {
                IdCardValidator iv = new IdCardValidator();
                if (iv.isValidatedAllIdcard(card)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    //一、判断网络连接是否可用


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            //如果仅仅是用来判断网络连接
            //则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //二、判断GPS是否打开

    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = ((LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = lm.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }
    //三、判断WIFI是否打开


    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    //四、判断是否是3G网络


    public static boolean is3rd(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    //五、判断是wifi还是3g网络,用户的体现性在这里了，wifi就可以建议下载或者在线播放。


    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

}
