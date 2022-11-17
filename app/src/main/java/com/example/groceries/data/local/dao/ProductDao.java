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
import com.example.groceries.helper.enums.SortEnum;

import java.util.List;

@Dao
public interface ProductDao extends IBaseDao<Product> {

}
