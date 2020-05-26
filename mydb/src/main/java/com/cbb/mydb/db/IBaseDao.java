package com.cbb.mydb.db;

/**
 * Created by 坎坎.
 * Date: 2020/5/26
 * Time: 9:28
 * describe:
 */
public interface IBaseDao<T> {
    long insert(T entity);
}
