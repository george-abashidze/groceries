package com.example.groceries.data.local.dao;

import androidx.room.Dao;

import com.example.groceries.data.model.JoinCartProduct;
import com.example.groceries.data.model.JoinProductCart;

@Dao
public interface CartProductJoinDao extends IRawBaseDao<JoinCartProduct>{
}
