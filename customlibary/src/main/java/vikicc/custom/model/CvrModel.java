package vikicc.custom.model;

import android.graphics.Bitmap;

/**
 * Created by liuliu on 2015/09/05   17:58
 *
 * @author 柳伟杰
 * @Email 1031066280@qq.com
 */
public class CvrModel {
    //    "姓名："+decodeInfo[0]+"\n"+"性别："+decodeInfo[1]+"\n"+"民族："+decodeInfo[2]+"\n"
//            +"出生日期："+decodeInfo[3]+"\n"+"地址："+decodeInfo[4]+"\n"+"身份号码："+decodeInfo[5]+"\n"
//            +"签发机关："+decodeInfo[6]+"\n"+"有效期限："+decodeInfo[7]+"-"+decodeInfo[8]+"\n"
//            +decodeInfo[9]+"\n";
    private String name;
    private String sex;
    private String nation;
    private String birthday;
    private String address;
    private String idnumber;
    private String issued;//签发机关
    private String usefullife_begin;//有限期限
    private String usefullife_end;//有限期限
    private Bitmap cardimg;//身份证的照片

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getUsefullife_begin() {
        return usefullife_begin;
    }

    public void setUsefullife_begin(String usefullife_begin) {
        this.usefullife_begin = usefullife_begin;
    }

    public String getUsefullife_end() {
        return usefullife_end;
    }

    public void setUsefullife_end(String usefullife_end) {
        this.usefullife_end = usefullife_end;
    }

    public Bitmap getCardimg() {
        return cardimg;
    }

    public void setCardimg(Bitmap cardimg) {
        this.cardimg = cardimg;
    }
}
