package com.cbb.mydb.bean;

import com.cbb.mydb.annotation.DbField;
import com.cbb.mydb.annotation.DbTable;

/**
 * Created by 坎坎.
 * Date: 2020/5/26
 * Time: 9:30
 * describe:
 */
@DbTable("tb_user")
public class User {
    @DbField("_id")
    private Integer id;
    private String name;
    private String password;

    public User(Integer id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
