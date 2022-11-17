package com.example.groceries.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.groceries.data.local.entity.Category;
import com.example.groceries.data.local.entity.Product;

import java.util.List;

@Dao
public interface CategoryDao extends IBaseDao<Category> {
    @Query("SELECT * FROM category")
    LiveData<List<Category>> getLiveData();
}
