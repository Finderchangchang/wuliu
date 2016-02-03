package vikicc.logistics.data;

import android.content.Context;
import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import vikicc.custom.method.Code;
import vikicc.logistics.model.CodeModel;

/**
 * xml操作相关问题
 * <p/>
 * Created by LiuWeiJie on 2015/8/4 0004.
 * Email:1031066280@qq.com
 */
public class XmlManager {
    private Context mContext;
    String path;

    public XmlManager(Context context) {
        mContext = context;
        path = mContext.getFilesDir().getAbsolutePath();
    }

    //    String path = "/data/data/vikicc.logistics/data";
//    String p=Environment.getExternalStorageDirectory().getAbsolutePath();


    public boolean createXml(List<CodeModel> list, String fileName) {
        boolean bFlag = false;
        FileOutputStream fileos = null;
        File paths = new File(path);
        //如果文件夹不存在则创建
        if (!paths.exists() && !paths.isDirectory()) {
            paths.mkdir();
        }
        File path_xml = new File(path, fileName);
        try {
            if (path_xml.exists()) {//路径存在进行删除
                path_xml.delete();//删除xml
            }
            if (path_xml.createNewFile()) {
                fileos = new FileOutputStream(path_xml);
                XmlSerializer serializer = Xml.newSerializer();
                serializer.setOutput(fileos, "UTF-8");
                serializer.startDocument("UTF-8", null);
                serializer.startTag(null, "CodeModelList");
                for (CodeModel model : list) {
                    serializer.startTag(null, "CodeModel");
                    serializer.attribute(null, "Key", model.getKey());
                    serializer.attribute(null, "Val", model.getValue());
                    serializer.endTag(null, "CodeModel");
                }
                serializer.startTag(null, "CodeModelList");
                serializer.endDocument();
                serializer.flush();
                fileos.close();
                bFlag = true;
            } else {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bFlag;
    }

    public List<CodeModel> getXml(String fileName) {
        String name = path + "/" + fileName;
        File path_xml = new File(path, fileName);
        List<CodeModel> al = new ArrayList<CodeModel>();
        if (path_xml.exists()) {//路径存在进行删除

            try {
                //InputStream inStream = getClass().getClassLoader().getResourceAsStream(name);
//                FileInputStream fis = new FileInputStream(name);
//                InputStream in = new BufferedInputStream(fis);
//                InputStream in = MyXMLReaderDOM4J.class.getClassLoader()
//                        .getResourceAsStream("resource.xml");
                // File f = new File("E:"+File.separator+"java2"+File.separator+"StreamDemo"+File.separator+"test.txt");
                InputStream in = new FileInputStream(path_xml);
                al = new SaxServiceAreaModel().getCodeList(in);
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        } else {

            return null;
        }
        return al;
    }


}
