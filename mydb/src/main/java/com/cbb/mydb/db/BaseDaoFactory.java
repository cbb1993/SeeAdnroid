package com.cbb.mydb.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by 坎坎.
 * Date: 2020/5/26
 * Time: 10:37
 * describe:
 */
public class BaseDaoFactory {
    private static final BaseDaoFactory instance=new BaseDaoFactory();
    public static BaseDaoFactory getInstance(){
        return instance;
    }
    private SQLiteDatabase sqLiteDatabase;
    // 创建数据库
    private BaseDaoFactory(){
        File file =new File(Environment.getExternalStorageDirectory(),"cbb.db");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(file,null);
    }

   //初始化BaseDao对象
    public <T> BaseDao<T> getBaseDao(Class<T> entityClass){
        BaseDao baseDao =null;
        try {
            baseDao = BaseDao.class.newInstance();
            baseDao.init(sqLiteDatabase,entityClass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return baseDao;
    }
}
