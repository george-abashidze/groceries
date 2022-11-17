package com.example.groceries.data.local.repository.contract;

import android.os.AsyncTask;

import com.example.groceries.data.local.entity.Product;
import com.example.groceries.data.model.JoinProductCart;
import com.example.groceries.helper.TaskCallback;
import com.example.groceries.helper.TaskResult;

import java.util.List;

public interface IProductRepository {
    void getData(String query, TaskCallback<Product> callback);
    void getJoinedData(String query, TaskCallback<JoinProductCart> callback);
    void insertProducts(TaskCallback<Long> callback, List<Product> products);
    void deleteProduct(TaskCallback<Product> callback, List<Product> product);
    void updateProduct(TaskCallback<Product> callback, List<Product> product);
}
