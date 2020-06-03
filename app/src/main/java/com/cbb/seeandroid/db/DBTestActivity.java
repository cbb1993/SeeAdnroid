package com.cbb.seeandroid.db;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cbb.mydb.bean.User;
import com.cbb.mydb.db.BaseDao;
import com.cbb.mydb.db.BaseDaoFactory;
import com.cbb.seeandroid.R;

import java.util.List;

/**
 * Created by 坎坎.
 * Date: 2020/5/26
 * Time: 10:05
 * describe:
 */
public class DBTestActivity extends AppCompatActivity {
    public static final String TAG = DBTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_test);
    }

    public void insert(View view) {
        BaseDao<User> baseDao = BaseDaoFactory.getInstance().getBaseDao(User.class);
        long id1 = baseDao.insert(new User(1, "张三", "123456"));
        long id2 = baseDao.insert(new User(2, "李四", "123456"));
        long id3 = baseDao.insert(new User(3, "王五", "123456"));

        Log.e("------","--------"+id1+"---"+id2+"---"+id3);
//        Toast.makeText(this, "插入id---->" + id, Toast.LENGTH_SHORT).show();
    }


    public void delete(View view) {
        BaseDao<User> baseDao = BaseDaoFactory.getInstance().getBaseDao(User.class);
        User where=new User();
        where.setId(2);
        baseDao.delete(where);
    }

    public void update(View view) {
        BaseDao<User> baseDao = BaseDaoFactory.getInstance().getBaseDao(User.class);
        User user=new User();
        user.setName("abcd");
        User where=new User();
        where.setId(3);
        baseDao.update(user,where);
    }

    public void select(View view) {
        BaseDao<User> baseDao = BaseDaoFactory.getInstance().getBaseDao(User.class);
        User where=new User();
//        where.setId(2);
        List<User> query = baseDao.query(where);
        Log.e("--------",""+query.toString());
    }
}
