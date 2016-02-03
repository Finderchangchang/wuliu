package vikicc.logistics.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import Decoder.BASE64Encoder;
import vikicc.custom.method.VikiccUtils;
import vikicc.logistics.model.HttpModel;

/**
 * 访问后台的通讯类
 * <p>
 * Created by LiuWeiJie on 2015/8/6 0006.
 * Email:1031066280@qq.com
 */
public class HttpUtil {

    //将数据post到后台接收结果
    public static InputStream doPost(HttpModel model) throws HttpHostConnectException, Exception {
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);//设置Time_Out时间（网络超时）
        HttpPost post = new HttpPost(model.getURL());
        post.setHeader("Content-Type",
                "application/x-www-form-urlencoded;charset=UTF-8");
        if (!VikiccUtils.isEmptyString(model.getUserId())) {//用户ID
            post.addHeader("UserId", model.getUserId());
        }
        if (!VikiccUtils.isEmptyString(model.getPassword())) {//密码
            post.addHeader("Password", model.getPassword());
        }
        if (!VikiccUtils.isEmptyString(model.getObjective())) {//方法名
            post.addHeader("Objective", model.getObjective());
        }
        if (!VikiccUtils.isEmptyString(model.getInformation())) {
            post.setEntity(new StringEntity("Information=" + new BASE64Encoder().encode(model.getInformation().getBytes())));
        }
        HttpResponse response = httpClient.execute(post);
        // 判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entitys = response.getEntity();
            if (entitys != null) {
                return entitys.getContent();//获得服务器返回的值
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public static String doGet(HttpModel model) {
        /*建立HTTP Get对象*/
        HttpGet httpRequest = new HttpGet(model.getURL());
        try {
            /*发送请求并等待响应*/
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            /*若状态码为200 ok*/
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                /*读*/
                String strResult = EntityUtils.toString(httpResponse.getEntity());
                /*去没有用的字符*/
                return strResult;
            } else {
                return "Error Response: " + httpResponse.getStatusLine().toString();
            }
        } catch (Exception e) {

        }
        return "";
    }

    //解析HttpResponse成为String字符串
    public static String getResponseContent(InputStream is) {
        //InputStream is = null;
        String content = null;
        try {
            //is = response.getEntity().getContent();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[4048 * 10];
            int len = 0;

            try {
                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                content = new String(baos.toByteArray(), "UTF-8");
            } catch (Exception e) {
                content = new String(baos.toByteArray());
            } finally {
                try {
                    baos.close();
                } catch (IOException e) {

                }
            }

        } catch (Exception e) {
            content = "";
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return content;
    }

}

