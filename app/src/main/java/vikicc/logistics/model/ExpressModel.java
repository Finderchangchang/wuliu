package vikicc.logistics.model;

import android.graphics.Bitmap;

import net.tsz.afinal.annotation.sqlite.Table;

import java.io.Serializable;

import vikicc.logistics.base.Utils;


/**
 * Created by Administrator on 2015/8/7.
 */
@Table(name = "express")
public class ExpressModel implements Serializable {
    private int id;
    private String ExpressId;//主键ID
    private String ExpressDeliveryId;//快递单号
    private String TerminalId;//手机用户编码
    private String ExpressType;//快递类型
    private String ExpressDescription;//快递描述
    private String ExpressStatus;//快递状态
    private String ExpressCompanyId;//寄件单位编码
    private String ExpressCompanyPersonnelId;//经办人
    private String ExpressCompanyVehicleCargoId;//车辆货运编码
    private String SendCity;//寄件城市
    private String SenderPostcode;//寄件地邮编
    private String SenderName;//寄件人姓名
    private String SenderSex;//寄件人性别
    private String SenderIdentityType;//寄件人证件类型
    private String SenderIdentityNumber;//寄件人证件号码
    private String SenderBirthday;//寄件人出生日期
    private String SenderNation;//寄件人民族
    private String SenderNative;//寄件人籍贯
    private String SenderAddress;//寄件人地址
    private String SendAddress;//寄件地址
    private String SenderLinkway;//寄件人联系方式
    private String SenderTime;//寄件时间
    private String SenderComment;//寄件备注
    private String UploadTime;//上传时间
    private String ReceiverCity;//收件城市
    private String ReceiverPostcode;//收件地邮编
    private String ReceiverName;//收件人姓名
    private String ReceiverSex;//收件人性别
    private String ReceiverIdentityType;//收件人证件类型
    private String ReceiverIdentityNumber;//收件人证件号码
    private String ReceiverBirthday;//收件人出生日期
    private String ReceiverNation;//收件人民族
    private String ReceiverNative;//收件人籍贯
    private String ReceiverAddress;//收件人地址
    private String ReceiveAddress;//收件地址
    private String ReceiverLinkway;//收件人联系方式
    private String ReceiveTime;//收件时间
    private String ReceiverComment;//收件备注
    private String ConsignIdentityImageId;//寄件人或收件人身份证照片
    private String GoodsImageId;//物品图片
    private String OrderImageId;//采集单图片
    private boolean IsDownLoad;//是否下载采集单
    private int ExpressNumber;//货物数量
    private boolean IsSuspicious;//是否可疑
    private String SuspriciousDescription;//可疑描述
    private ImageObject[] Images;

    public String XML() {
        return "<?xml version=\"1.0\"?>\n" +
                "<ExpressModel xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
                "  <ExpressId>AAAAAAA</ExpressId>\n" +
                "  <ExpressCompanyId>46546513</ExpressCompanyId>\n" +
                "  <SenderBirthday xsi:nil=\"true\" />\n" +
                "  <SenderTime xsi:nil=\"true\" />\n" +
                "  <UploadTime xsi:nil=\"true\" />\n" +
                "  <ReceiverBirthday xsi:nil=\"true\" />\n" +
                "  <ReceiveTime xsi:nil=\"true\" />\n" +
                "  <IsDownLoad xsi:nil=\"true\" />\n" +
                "  <ExpressNumber>222</ExpressNumber>\n" +
                "  <IsSuspicious xsi:nil=\"true\" />\n" +
                "  <ChangeType>Insert</ChangeType>\n" +
                "</ExpressModel>\n";
    }

    // private String Platform;
    public String Serialization() {

        String xml = "<?xml version=\"1.0\"?>\n";
        xml += String
                .format("<%s  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n",
                        "ExpressModel");
        if (this.getExpressId() != null) {
            xml += "<ExpressId>" + Utils.XMlEncode(this.getExpressId()) + "</ExpressId>\n";
        }
        if (this.getExpressDeliveryId() != null) {
            xml += "<ExpressDeliveryId>" + Utils.XMlEncode(this.getExpressDeliveryId()) + "</ExpressDeliveryId>";
        }
        if (this.getSenderName() != null) {
            xml += "<SenderName>" + Utils.XMlEncode(this.getSenderName()) + "</SenderName>";
        }
        if (this.getExpressStatus() != null) {
            xml += "<ExpressStatus>" + Utils.XMlEncode(this.getExpressStatus()) + "</ExpressStatus>\n";
        }
        if (this.getExpressCompanyId() != null) {
            xml += "<ExpressCompanyId>" + Utils.XMlEncode(this.getExpressCompanyId()) + "</ExpressCompanyId>\n";
        }
        if (this.getExpressCompanyVehicleCargoId() != null) {
            xml += "<ExpressCompanyVehicleCargoId>" + Utils.XMlEncode(this.getExpressCompanyVehicleCargoId()) + "</ExpressCompanyVehicleCargoId>\n";
        }
        if (this.getExpressCompanyPersonnelId() != null) {
            xml += "<ExpressCompanyPersonnelId>" + Utils.XMlEncode(this.getExpressCompanyPersonnelId()) + "</ExpressCompanyPersonnelId>\n";
        }
        if (this.getSendCity() != null) {
            xml += "<SendCity>" + Utils.XMlEncode(this.getSendCity()) + "</SendCity>\n";
        }
        if (this.getSenderPostcode() != null) {
            xml += "<SenderPostcode>" + Utils.XMlEncode(this.getSenderPostcode()) + "</SenderPostcode>\n";
        }
        if (this.getSenderSex() != null) {
            xml += "<SenderSex>" + Utils.XMlEncode(this.getSenderSex()) + "</SenderSex>\n";
        }
        if (this.getSenderIdentityType() != null) {
            xml += "<SenderIdentityType>" + Utils.XMlEncode(this.getSenderIdentityType()) + "</SenderIdentityType>\n";
        }
        if (this.getSenderPostcode() != null) {
            xml += "<SenderPostcode>" + Utils.XMlEncode(this.getSenderPostcode()) + "</SenderPostcode>\n";
        }
        if (this.getSenderIdentityNumber() != null) {
            xml += "<SenderIdentityNumber>" + Utils.XMlEncode(this.getSenderIdentityNumber()) + "</SenderIdentityNumber>\n";
        }
        if (this.getSenderNation() != null) {
            xml += "<SenderNation>" + Utils.XMlEncode(this.getSenderNation()) + "</SenderNation>\n";
        }
        if (this.getSenderNative() != null) {
            xml += "<SenderNative>" + Utils.XMlEncode(this.getSenderNative()) + "</SenderNative>\n";
        }
        if (this.getSenderAddress() != null) {
            xml += "<SenderAddress>" + Utils.XMlEncode(this.getSenderAddress()) + "</SenderAddress>\n";
        }
        if (this.getSendAddress() != null) {
            xml += "<SendAddress>" + Utils.XMlEncode(this.getSendAddress()) + "</SendAddress>\n";
        }
        if (this.getSenderLinkway() != null) {
            xml += "<SenderLinkway>" + Utils.XMlEncode(this.getSenderLinkway()) + "</SenderLinkway>\n";
        }
        if (this.getSenderComment() != null) {
            xml += "<SenderComment>" + Utils.XMlEncode(this.getSenderComment()) + "</SenderComment>\n";
        }
        if (this.getReceiverCity() != null) {
            xml += "<ReceiverCity>" + Utils.XMlEncode(this.getReceiverCity()) + "</ReceiverCity>\n";
        }
        if (this.getReceiverPostcode() != null) {
            xml += "<ReceiverPostcode>" + Utils.XMlEncode(this.getReceiverPostcode()) + "</ReceiverPostcode>\n";
        }
        if (this.getReceiverName() != null) {
            xml += "<ReceiverName>" + Utils.XMlEncode(this.getReceiverName()) + "</ReceiverName>\n";
        }
        if (this.getReceiverSex() != null) {
            xml += "<ReceiverSex>" + Utils.XMlEncode(this.getReceiverSex()) + "</ReceiverSex>\n";
        }
        if (this.getTerminalId() != null) {
            xml += "<TerminalId>" + Utils.XMlEncode(this.getTerminalId()) + "</TerminalId>\n";
        }
        if (this.getExpressType() != null) {
            xml += "<ExpressType>" + Utils.XMlEncode(this.getExpressType()) + "</ExpressType>\n";
        }
        if (this.getExpressDescription() != null) {
            xml += "<ExpressDescription>" + Utils.XMlEncode(this.getExpressDescription()) + "</ExpressDescription>\n";
        }
        if (this.getSuspriciousDescription() != null) {
            xml += "<SuspriciousDescription>" + Utils.XMlEncode(this.getSuspriciousDescription()) + "</SuspriciousDescription>\n";
        }
        if (this.getExpressType() != null) {
            xml += "<ExpressType>" + Utils.XMlEncode(this.getExpressType()) + "</ExpressType>\n";
        }
        if (!((this.getExpressNumber() + "").equals(""))) {
            xml += "<ExpressNumber>" + Utils.XMlEncode(this.getExpressNumber() + "") + "</ExpressNumber>\n";
        }
        //if (this.isDownLoad() != null) {
        // xml += "<IsDownLoad>" + Utils.XMlEncode(this.isDownLoad() + "") + "</IsDownLoad>\n";
        //}
        if (this.getReceiverLinkway() != null) {
            xml += "<ReceiverLinkway>" + Utils.XMlEncode(this.getReceiverLinkway()) + "</ReceiverLinkway>\n";
        }
        if (this.getReceiverComment() != null) {
            xml += "<ReceiverComment>" + Utils.XMlEncode(this.getReceiverComment()) + "</ReceiverComment>\n";
        }
        if (this.getReceiveAddress() != null) {
            xml += "<ReceiveAddress>" + Utils.XMlEncode(this.getReceiveAddress()) + "</ReceiveAddress>\n";
        }
        if (this.getReceiverNative() != null) {
            xml += "<ReceiverNative>" + Utils.XMlEncode(this.getReceiverNative()) + "</ReceiverNative>\n";
        }
        if (this.getReceiverNation() != null) {
            xml += "<ReceiverNation>" + Utils.XMlEncode(this.getReceiverNation()) + "</ReceiverNation>\n";
        }
        if (this.getReceiverAddress() != null) {
            xml += "<ReceiverAddress>" + Utils.XMlEncode(this.getReceiverAddress()) + "</ReceiverAddress>\n";
        }
        if (this.getReceiverIdentityNumber() != null) {
            xml += "<ReceiverIdentityNumber>" + Utils.XMlEncode(this.getReceiverIdentityNumber()) + "</ReceiverIdentityNumber>\n";
        }
        if (this.getReceiverIdentityType() != null) {
            xml += "<ReceiverIdentityType>" + Utils.XMlEncode(this.getReceiverIdentityType()) + "</ReceiverIdentityType>\n";
        }


        // 添加多个图片
        if (this.getSenderBirthday() != null) {
            xml += "<SenderBirthday>" + Utils.XMlEncode(this.getSenderBirthday()) + "</SenderBirthday>\n";
        } else {
            xml += "<SenderBirthday xsi:nil=\"true\"/>\n";
        }
        if (this.getSenderTime() != null) {
            xml += "<SenderTime>" + Utils.XMlEncode(this.getSenderTime()) + "</SenderTime>\n";
        } else {
            xml += "<SenderTime  xsi:nil=\"true\"/>\n";
        }
        if (this.getUploadTime() != null) {
            xml += "<UploadTime>" + Utils.XMlEncode(this.getUploadTime()) + "</UploadTime>\n";
        } else {
            xml += "<UploadTime  xsi:nil=\"true\"/>\n";
        }
        if (this.getReceiverBirthday() != null) {
            xml += "<ReceiverBirthday>" + Utils.XMlEncode(this.getReceiverBirthday()) + "</ReceiverBirthday>\n";
        } else {
            xml += "<ReceiverBirthday  xsi:nil=\"true\"/>\n";
        }
        if (this.getReceiveTime() != null) {
            xml += "<ReceiveTime>" + Utils.XMlEncode(this.getReceiveTime()) + "</ReceiveTime>\n";
        } else {
            xml += "<ReceiveTime  xsi:nil=\"true\"/>\n";
        }

        xml += "<IsSuspicious>"+this.getIsSuspicious()+"</IsSuspicious>\n";
        xml += "<IsDownLoad>"+this.getIsDownLoad()+"</IsDownLoad>\n";
        xml += "<ChangeType>Insert</ChangeType>\n";

        if (this.getImages() != null) {
            xml += "  <Images>\n";
            for (int i = 0; i < this.getImages().length; i++) {
                ImageObject io = this.getImages()[i];

                if (io != null) {
                    xml += "    <ImageObject>\n";

                    xml += String.format("      <ImageId>%s</ImageId>\n",
                            Utils.XMlEncode(Utils.XMlEncode(io.getImageId())));
                    if (io.getImageData() != null) {
                        xml += String.format("      <ImageData>%s</ImageData>\n",
                                Utils.XMlEncode(Utils.encodeBitmap(io.getImageData())));

                    } else {
                        xml += String.format("      <ImageData>%s</ImageData>\n",
                                "");

                    }
                    xml += "    </ImageObject>\n";
                }


            }
            xml += "  </Images>";
        }


        xml += "\n</ExpressModel>";
        return xml;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExpressId() {
        return ExpressId;
    }

    public void setExpressId(String expressId) {
        ExpressId = expressId;
    }

    public String getExpressDeliveryId() {
        return ExpressDeliveryId;
    }

    public void setExpressDeliveryId(String expressDeliveryId) {
        ExpressDeliveryId = expressDeliveryId;
    }

    public String getTerminalId() {
        return TerminalId;
    }

    public void setTerminalId(String terminalId) {
        TerminalId = terminalId;
    }

    public String getExpressType() {
        return ExpressType;
    }

    public void setExpressType(String expressType) {
        ExpressType = expressType;
    }

    public String getExpressDescription() {
        return ExpressDescription;
    }

    public void setExpressDescription(String expressDescription) {
        ExpressDescription = expressDescription;
    }

    public String getExpressStatus() {
        return ExpressStatus;
    }

    public void setExpressStatus(String expressStatus) {
        ExpressStatus = expressStatus;
    }

    public String getExpressCompanyId() {
        return ExpressCompanyId;
    }

    public void setExpressCompanyId(String expressCompanyId) {
        ExpressCompanyId = expressCompanyId;
    }

    public String getExpressCompanyPersonnelId() {
        return ExpressCompanyPersonnelId;
    }

    public void setExpressCompanyPersonnelId(String expressCompanyPersonnelId) {
        ExpressCompanyPersonnelId = expressCompanyPersonnelId;
    }

    public String getExpressCompanyVehicleCargoId() {
        return ExpressCompanyVehicleCargoId;
    }

    public void setExpressCompanyVehicleCargoId(String expressCompanyVehicleCargoId) {
        ExpressCompanyVehicleCargoId = expressCompanyVehicleCargoId;
    }

    public String getSendCity() {
        return SendCity;
    }

    public void setSendCity(String sendCity) {
        SendCity = sendCity;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getSenderSex() {
        return SenderSex;
    }

    public void setSenderSex(String senderSex) {
        SenderSex = senderSex;
    }

    public String getSenderIdentityType() {
        return SenderIdentityType;
    }

    public void setSenderIdentityType(String senderIdentityType) {
        SenderIdentityType = senderIdentityType;
    }

    public String getSenderIdentityNumber() {
        return SenderIdentityNumber;
    }

    public void setSenderIdentityNumber(String senderIdentityNumber) {
        SenderIdentityNumber = senderIdentityNumber;
    }

    public String getSenderBirthday() {
        return SenderBirthday;
    }

    public void setSenderBirthday(String senderBirthday) {
        SenderBirthday = senderBirthday;
    }

    public String getSenderNation() {
        return SenderNation;
    }

    public void setSenderNation(String senderNation) {
        SenderNation = senderNation;
    }

    public String getSenderNative() {
        return SenderNative;
    }

    public void setSenderNative(String senderNative) {
        SenderNative = senderNative;
    }

    public String getSenderAddress() {
        return SenderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        SenderAddress = senderAddress;
    }


    public String getSenderTime() {
        return SenderTime;
    }

    public void setSenderTime(String senderTime) {
        SenderTime = senderTime;
    }

    public String getSenderComment() {
        return SenderComment;
    }

    public void setSenderComment(String senderComment) {
        SenderComment = senderComment;
    }

    public String getUploadTime() {
        return UploadTime;
    }

    public void setUploadTime(String uploadTime) {
        UploadTime = uploadTime;
    }

    public String getReceiverCity() {
        return ReceiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        ReceiverCity = receiverCity;
    }


    public String getReceiverName() {
        return ReceiverName;
    }

    public void setReceiverName(String receiverName) {
        ReceiverName = receiverName;
    }

    public String getReceiverSex() {
        return ReceiverSex;
    }

    public void setReceiverSex(String receiverSex) {
        ReceiverSex = receiverSex;
    }

    public String getReceiverIdentityType() {
        return ReceiverIdentityType;
    }

    public void setReceiverIdentityType(String receiverIdentityType) {
        ReceiverIdentityType = receiverIdentityType;
    }

    public String getReceiverIdentityNumber() {
        return ReceiverIdentityNumber;
    }

    public void setReceiverIdentityNumber(String receiverIdentityNumber) {
        ReceiverIdentityNumber = receiverIdentityNumber;
    }

    public String getReceiverBirthday() {
        return ReceiverBirthday;
    }

    public void setReceiverBirthday(String receiverBirthday) {
        ReceiverBirthday = receiverBirthday;
    }

    public String getReceiverNation() {
        return ReceiverNation;
    }

    public void setReceiverNation(String receiverNation) {
        ReceiverNation = receiverNation;
    }

    public String getReceiverNative() {
        return ReceiverNative;
    }

    public void setReceiverNative(String receiverNative) {
        ReceiverNative = receiverNative;
    }

    public String getReceiverAddress() {
        return ReceiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        ReceiverAddress = receiverAddress;
    }

    public String getReceiveAddress() {
        return ReceiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        ReceiveAddress = receiveAddress;
    }


    public String getReceiveTime() {
        return ReceiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        ReceiveTime = receiveTime;
    }

    public String getReceiverComment() {
        return ReceiverComment;
    }

    public void setReceiverComment(String receiverComment) {
        ReceiverComment = receiverComment;
    }

    public String getConsignIdentityImageId() {
        return ConsignIdentityImageId;
    }

    public void setConsignIdentityImageId(String consignIdentityImageId) {
        ConsignIdentityImageId = consignIdentityImageId;
    }

    public String getGoodsImageId() {
        return GoodsImageId;
    }

    public void setGoodsImageId(String goodsImageId) {
        GoodsImageId = goodsImageId;
    }

    public String getOrderImageId() {
        return OrderImageId;
    }

    public void setOrderImageId(String orderImageId) {
        OrderImageId = orderImageId;
    }

    public boolean isDownLoad() {
        return IsDownLoad;
    }

    public boolean getIsDownLoad() {
        return IsDownLoad;
    }

    public boolean isIsDownLoad() {
        return IsDownLoad;
    }

    public void setIsDownLoad(boolean isDownLoad) {
        IsDownLoad = isDownLoad;
    }

    public int getExpressNumber() {
        return ExpressNumber;
    }

    public void setExpressNumber(int expressNumber) {
        ExpressNumber = expressNumber;
    }

    public boolean isSuspicious() {
        return IsSuspicious;
    }

    public boolean getIsSuspicious() {
        return IsSuspicious;
    }

    public boolean isIsSuspicious() {
        return IsSuspicious;
    }

    public void setIsSuspicious(boolean isSuspicious) {
        IsSuspicious = isSuspicious;
    }

    public String getSuspriciousDescription() {
        return SuspriciousDescription;
    }

    public void setSuspriciousDescription(String suspriciousDescription) {
        SuspriciousDescription = suspriciousDescription;
    }

    public String getSenderPostcode() {
        return SenderPostcode;
    }

    public void setSenderPostcode(String senderPostcode) {
        SenderPostcode = senderPostcode;
    }

    public String getSendAddress() {
        return SendAddress;
    }

    public void setSendAddress(String sendAddress) {
        SendAddress = sendAddress;
    }

    public String getSenderLinkway() {
        return SenderLinkway;
    }

    public void setSenderLinkway(String senderLinkway) {
        SenderLinkway = senderLinkway;
    }

    public String getReceiverPostcode() {
        return ReceiverPostcode;
    }

    public void setReceiverPostcode(String receiverPostcode) {
        ReceiverPostcode = receiverPostcode;
    }

    public String getReceiverLinkway() {
        return ReceiverLinkway;
    }

    public void setReceiverLinkway(String receiverLinkway) {
        ReceiverLinkway = receiverLinkway;
    }

    public ImageObject[] getImages() {
        return Images;
    }

    public void setImages(ImageObject[] images) {
        Images = images;
    }
}
