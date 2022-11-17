package com.example.groceries.data.local.repository.implementation;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.groceries.data.local.dao.CartDao;
import com.example.groceries.data.local.dao.CartProductJoinDao;
import com.example.groceries.data.local.entity.Cart;
import com.example.groceries.data.local.entity.Category;
import com.example.groceries.data.local.repository.contract.ICartRepository;
import com.example.groceries.data.local.tasks.DeleteTask;
import com.example.groceries.data.local.tasks.FetchTask;
import com.example.groceries.data.local.tasks.InsertTask;
import com.example.groceries.data.local.tasks.RawFetchTask;
import com.example.groceries.data.local.tasks.UpdateTask;
import com.example.groceries.data.model.JoinCartProduct;
import com.example.groceries.helper.TaskCallback;

import java.util.List;

import javax.inject.Inject;

public class CartRepositoryImpl implements ICartRepository {

    CartDao cartDao;
    CartProductJoinDao cartProductDao;

    @Inject
    public CartRepositoryImpl(CartDao cartDao,CartProductJoinDao cartProductDao){
        this.cartDao = cartDao;
        this.cartProductDao = cartProductDao;
    }

    @Override
    public LiveData<List<Cart>> getLiveData() {
        return cartDao.getLiveData();
    }

    @Override
    public void getData(String query, TaskCallback<Cart> callback) {
        new FetchTask<>(query,cartDao,callback).execute();
    }

    @Override
    public void getJoinedData(String query, TaskCallback<JoinCartProduct> callback) {
        new RawFetchTask<>(query,cartProductDao,callback).execute();
    }

    @Override
    public void insertItems(TaskCallback<Long> callback, List<Cart> carts) {
        new InsertTask<>(cartDao,callback).execute(carts);
    }

    @Override
    public void deleteItems(TaskCallback<Cart> callback, List<Cart> cart) {
        new DeleteTask<>(cartDao,callback).execute(cart);
    }

    @Override
    public void updateItems(TaskCallback<Cart> callback, List<Cart> cart) {
        new UpdateTask<>(cartDao,callback).execute(cart);
    }

}
