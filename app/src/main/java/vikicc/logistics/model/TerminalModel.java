package vikicc.logistics.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/5.
 */
public class TerminalModel implements Serializable {
    private int id;
    private String TerminalId;//设备IMEI编码
    private String TerminalName;//设备型号
    private String TerminalPassword;//密码
    private String TerminalPersonnalId;//人员编码
    private String TerminalCompanyId;//企业编码
    private String TerminalIP;//服务器ip
    private String TerminalCount;//端口号
    private String TerminalIsRegFast;

    public String Serializable() {
        String xml = "<?xml version=\"1.0\"?>\n";
        xml += String
                .format("<%s  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n",
                        "MobileConfigModel");
        xml += "<TerminalId>" + this.getTerminalId() + "</TerminalId>" +
                "<TerminalPersonnalId>" + this.getTerminalPersonnalId() + "</TerminalPersonnalId>" +
                "<TerminalCompanyId>" + this.getTerminalCompanyId() + "</TerminalCompanyId>" +
                "<TerminalPassword>" + this.getTerminalPassword() + "</TerminalPassword>";
        xml += "</MobileConfigModel>";
        return xml;
    }

    public String getTerminalId() {
        return TerminalId;
    }

    public void setTerminalId(String terminalId) {
        TerminalId = terminalId;
    }


    public String getTerminalName() {
        return TerminalName;
    }

    public void setTerminalName(String terminalName) {
        TerminalName = terminalName;
    }

    public String getTerminalPassword() {
        return TerminalPassword;
    }

    public void setTerminalPassword(String terminalPassword) {
        TerminalPassword = terminalPassword;
    }

    public String getTerminalPersonnalId() {
        return TerminalPersonnalId;
    }

    public void setTerminalPersonnalId(String terminalPersonnalId) {
        TerminalPersonnalId = terminalPersonnalId;
    }

    public String getTerminalCompanyId() {
        return TerminalCompanyId;
    }

    public void setTerminalCompanyId(String terminalCompanyId) {
        TerminalCompanyId = terminalCompanyId;
    }

    public String getTerminalIP() {
        return TerminalIP;
    }

    public void setTerminalIP(String terminalIP) {
        TerminalIP = terminalIP;
    }

    public String getTerminalCount() {
        return TerminalCount;
    }

    public void setTerminalCount(String terminalCount) {
        TerminalCount = terminalCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTerminalIsRegFast() {
        return TerminalIsRegFast;
    }

    public void setTerminalIsRegFast(String terminalIsRegFast) {
        TerminalIsRegFast = terminalIsRegFast;
    }
}
