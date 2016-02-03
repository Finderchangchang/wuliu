package vikicc.logistics.model;

import android.graphics.Bitmap;

import net.tsz.afinal.utils.Utils;

import java.io.Serializable;

/**
 * 保存图片模型 Administrator on 2015/8/20.
 */
public class ImageObject implements Serializable {
    private int id;
    private String ImageId;
    private Bitmap ImageData;

    public String getImageId() {
        return ImageId;
    }

    public void setImageId(String imageId) {
        ImageId = imageId;
    }

    public Bitmap getImageData() {
        return ImageData;
    }

    public void setImageData(Bitmap imageData) {
        ImageData = imageData;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
