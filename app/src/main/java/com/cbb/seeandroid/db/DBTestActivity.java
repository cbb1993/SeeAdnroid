package com.cbb.seeandroid.db;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cbb.mydb.bean.User;
import com.cbb.mydb.db.BaseDao;
import com.cbb.mydb.db.BaseDaoFactory;
import com.cbb.seeandroid.R;

/**
 * Created by 坎坎.
 * Date: 2020/5/26
 * Time: 10:05
 * describe:
 */
public class DBTestActivity extends AppCompatActivity {
    public static final String TAG = DBTestActivity.class.getSimpleName();
    private BaseDao<User> baseDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_test);
    }

    public void create(View view) {
        baseDao = BaseDaoFactory.getInstance().getBaseDao(User.class);
        Toast.makeText(this, "建表成功", Toast.LENGTH_SHORT).show();
    }

    public void insert(View view) {
        if(baseDao == null){
            baseDao = BaseDaoFactory.getInstance().getBaseDao(User.class);
        }
        long id = baseDao.insert(new User(2, "张三", "123456"));
        Toast.makeText(this, "插入id---->" + id, Toast.LENGTH_SHORT).show();
    }


}
