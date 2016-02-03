package vikicc.logistics.data;

import android.content.Context;

import net.tsz.afinal.FinalDb;

import java.util.List;

/**
 * Created by Administrator on 2015/8/7.
 */
public class AfinalDataHelper {
    private Context mContext;
    private FinalDb db;
    public  AfinalDataHelper(Context context) {
        mContext = context;
        db= FinalDb.create(mContext, "mytest.db", false);
    }

    /**
     * 保存数据
     * @param model
     */
    public void SaveModel(Object model) {
        db.save(model);
    }

    public List<?> FindAllModel(Object model) {

        return db.findAll(model.getClass());
//        db.deleteAll(model.getClass());
//        db.dropTable(model.getClass());
//        db.update(model.getClass());
    }

    public void UpateModel(Object model) {
        db.update(model);
    }
    public void dropTable(Object model){
        db.dropTable(model.getClass());
    }

    //private void delete
}
