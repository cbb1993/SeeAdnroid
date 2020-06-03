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
    public User() {
    }

    public User(Integer id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
