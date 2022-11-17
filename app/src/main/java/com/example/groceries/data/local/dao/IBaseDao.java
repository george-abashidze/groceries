package com.example.groceries.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.groceries.data.local.entity.Product;

import java.util.List;

@Dao
public interface IBaseDao<T> {

    @Insert
    List<Long> insertAll(List<T> values);

    @Insert
    Long insert(T value);

    @Delete
    void delete(T value);

    @RawQuery
    List<T> getData(SupportSQLiteQuery query);

    @Update
    void updateItem(T item);

    @Update
    void updateItems(List<T> items);

    @Delete
    void deleteAll(List<T> items);
}
