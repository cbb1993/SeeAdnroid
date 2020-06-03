package com.cbb.mydb.db;

import java.util.List;

/**
 * Created by 坎坎.
 * Date: 2020/5/26
 * Time: 9:28
 * describe:
 */
public interface IBaseDao<T> {
    long insert(T entity);
    long update(T entity,T where);
    int delete(T where);
    List<T> query(T where);
    List<T> query(T where,String orderBy,Integer startIndex,Integer limit);
}
