package com.example.groceries.data.local.repository.contract;

import androidx.lifecycle.LiveData;

import com.example.groceries.data.local.entity.Cart;
import com.example.groceries.data.local.entity.Category;
import com.example.groceries.data.model.JoinCartProduct;
import com.example.groceries.helper.TaskCallback;

import java.util.List;

public interface ICartRepository {
    LiveData<List<Cart>> getLiveData();
    void getData(String query, TaskCallback<Cart> callback);
    void getJoinedData(String query, TaskCallback<JoinCartProduct> callback);
    void insertItems(TaskCallback<Long> callback, List<Cart> carts);
    void deleteItems(TaskCallback<Cart> callback, List<Cart> cart);
    void updateItems(TaskCallback<Cart> callback, List<Cart> cart);
}
