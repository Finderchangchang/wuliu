package vikicc.logistics.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2015/8/8.
 */
public class DynamicModel {
    //包名+model名
    public Object getBean(String className)throws Exception{
        Class cls=null;
        try {
            cls=Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("类错误");
        }
        Constructor[]cons=null;
        try {
            cons = cls.getConstructors();//得到所有构造器
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("构造器错误");
        }
        //如果上面没错，就有构造方法
        Constructor defcon=cons[0];//得到默认构造器，第0个事默认构造器，无参构造方法
        Object obj=defcon.newInstance();//实例化，得到一个对象
        return obj;

    }
    //创建的model对象，字段名，字段值
    public void setProperty(Object bean,String propertyName,Object propertyValue)throws Exception{
        Class cls=bean.getClass();
        Method[]methos=cls.getMethods();//得到所有的方法
        for(Method m:methos){
            if(m.getName().equalsIgnoreCase("set"+propertyName)){
                //找到方法就注入
                m.invoke(bean,propertyValue);
                break;
            }
        }
    }
}
