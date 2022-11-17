package com.example.groceries.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.groceries.data.local.dao.CartDao;
import com.example.groceries.data.local.dao.CartProductJoinDao;
import com.example.groceries.data.local.dao.CategoryDao;
import com.example.groceries.data.local.dao.ProductCartJoinDao;
import com.example.groceries.data.local.dao.ProductDao;
import com.example.groceries.data.local.entity.Cart;
import com.example.groceries.data.local.entity.Category;
import com.example.groceries.data.local.entity.Product;

@Database(entities = {Category.class, Product.class, Cart.class}, version = 1)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
    public abstract CategoryDao categoryDao();
    public abstract CartDao cartDao();
    public abstract ProductCartJoinDao productCartDao();
    public abstract CartProductJoinDao cartProductDao();

}
