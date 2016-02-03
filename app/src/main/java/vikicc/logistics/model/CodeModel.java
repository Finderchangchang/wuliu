package vikicc.logistics.model;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/7.
 */
@Table(name = "code1")
public class CodeModel implements Serializable {
    private int id;
    private String Key;
    private String Value;
    private String tableName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
