package vikicc.logistics.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/25.
 */
public class PersonnelModel implements Serializable{
    private int id;
    private String PersonnelId;//人员编码
    private String CompanyId;//企业编码
    private String PersonnelType;//人员类型
    private String PersonnelName;//姓名
    private String PersonnelSex;//性别
    //private String PersonnelBirthday;//
    private String PersonnelNative;//籍贯
    //private String PersonnelNation;
    private String PersonnelPhotoImageId;
    private ImageObject Images;//照片

    public String getPersonnelId() {
        return PersonnelId;
    }

    public void setPersonnelId(String personnelId) {
        PersonnelId = personnelId;
    }

    public String getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(String companyId) {
        CompanyId = companyId;
    }

    public String getPersonnelType() {
        return PersonnelType;
    }

    public void setPersonnelType(String personnelType) {
        PersonnelType = personnelType;
    }

    public String getPersonnelName() {
        return PersonnelName;
    }

    public void setPersonnelName(String personnelName) {
        PersonnelName = personnelName;
    }

    public String getPersonnelSex() {
        return PersonnelSex;
    }

    public void setPersonnelSex(String personnelSex) {
        PersonnelSex = personnelSex;
    }

    public String getPersonnelNative() {
        return PersonnelNative;
    }

    public void setPersonnelNative(String personnelNative) {
        PersonnelNative = personnelNative;
    }

    public String getPersonnelPhotoImageId() {
        return PersonnelPhotoImageId;
    }

    public void setPersonnelPhotoImageId(String personnelPhotoImageId) {
        PersonnelPhotoImageId = personnelPhotoImageId;
    }

    public ImageObject getImages() {
        return Images;
    }

    public void setImages(ImageObject images) {
        Images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
