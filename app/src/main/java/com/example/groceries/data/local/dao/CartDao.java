package com.example.groceries.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.groceries.data.local.entity.Cart;
import com.example.groceries.data.local.entity.Category;

import java.util.List;

@Dao
public interface CartDao extends IBaseDao<Cart>{
    @Query("SELECT * FROM cart")
    LiveData<List<Cart>> getLiveData();
}
