package vikicc.logistics.http;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import vikicc.logistics.model.DynamicModel;
import vikicc.logistics.model.InvokeReturn;

/**
 * Created by Administrator on 2015/8/8.
 */
public class HttpXml {

    //根据输入流解析文件
    public static InvokeReturn parseXml(InputStream is, String modelName) throws Exception {
        InvokeReturn invokeReturn = new InvokeReturn();
        List<Object> objectList = new ArrayList<Object>();
        DynamicModel dynamicModel = new DynamicModel();
        Field[] fields = null;
        Object objectModel = null;
        XmlPullParser parser = Xml.newPullParser(); //由android.util.Xml创建一个XmlPullParser实例
        parser.setInput(is, "UTF-8");               //设置输入流 并指明编码方式
        int eventType = parser.getEventType();
        String attribute = "";

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.getAttributeCount() > 0) {
                attribute = parser.getAttributeValue(0).toString();
            }
            switch (eventType) {
                //声明List<CompanyModel>
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("Success")) {
                        parser.next();
                        invokeReturn.setSuccess(parser.getText().toString());
                    } else if (parser.getName().equals("Time")) {
                        parser.next();
                        invokeReturn.setTime(parser.getText().toString());
                    } else if (parser.getName().equals(modelName) || (parser.getName().equals("Object") && (attribute.equals("ArrayOf" + modelName) || attribute.equals(modelName)))) {


                        if (modelName != null) {
                            objectModel = dynamicModel.getBean("vikicc.logistics.model." + modelName);
                        }

                        if (objectModel != null) {
                            fields = objectModel.getClass().getDeclaredFields();
                        }
                    } else if (parser.getName().equals("Object") && parser.getAttributeValue(0).equals("xsd:dateTime")) {
                        parser.next();
                        invokeReturn.setRtime(parser.getText());
                    } else if (parser.getName().equals("Images")) {
                        parser.next();
                        if(parser.getText()!=null){
                            parser.next();
                            if(parser.getName().equals("ImageData")){
                                parser.next();
                                invokeReturn.setImages(parser.getText());
                                System.out.println("ImageData" + parser.getText());
                            }
                        }


                    }
                        else {
                        if (objectModel != null) {
                            String property = parser.getName();
                            if (fields != null) {
                                for (Field field : fields) {
                                    if (field.getName().equals(property)) {
                                        parser.next();
                                        if (!property.equals("Images")) {
                                            String value = parser.getText();
                                            if (value != null) {
                                                dynamicModel.setProperty(objectModel, property, value.toString());
                                            } else {
                                                dynamicModel.setProperty(objectModel, property, null);
                                            }
                                        }

                                        break;
                                    }
                                }
                            }
                        }
                    }
                    parser.next();
                    break;
                case XmlPullParser.END_TAG:
                    String n = parser.getName();

                    if (n.equals(modelName) || n.equals("Object")) {
                        if (objectModel != null) {

                            objectList.add(objectModel);
                            objectModel = null;
                            fields = null;
                        }
                    }
                    break;
            }
            eventType = parser.next();
        }

        invokeReturn.setListModel(objectList);
        return invokeReturn;
    }
}
