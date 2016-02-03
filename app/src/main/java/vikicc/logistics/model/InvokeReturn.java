package vikicc.logistics.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by LiuWeiJie on 2015/8/8 0008.
 * Email:1031066280@qq.com
 */
public class InvokeReturn {
    private String Success;
    private String Time;
    private String Rtime;
    private String Images;
    private List<Object> ListModel=new ArrayList<Object>();

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public List<Object> getListModel() {
        return ListModel;
    }

    public void setListModel(List<Object> listModel) {
        ListModel = listModel;
    }

    public String getRtime() {
        return Rtime;
    }

    public void setRtime(String rtime) {
        Rtime = rtime;
    }

    public String getImages() {
        return Images;
    }

    public void setImages(String images) {
        Images = images;
    }
}
