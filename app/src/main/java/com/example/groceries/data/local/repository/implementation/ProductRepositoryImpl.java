package com.example.groceries.data.local.repository.implementation;

import android.os.AsyncTask;

import com.example.groceries.data.local.dao.ProductCartJoinDao;
import com.example.groceries.data.local.dao.ProductDao;
import com.example.groceries.data.local.entity.Category;
import com.example.groceries.data.local.entity.Product;
import com.example.groceries.data.local.repository.contract.IProductRepository;
import com.example.groceries.data.local.tasks.DeleteTask;
import com.example.groceries.data.local.tasks.FetchTask;
import com.example.groceries.data.local.tasks.RawFetchTask;
import com.example.groceries.data.local.tasks.UpdateTask;
import com.example.groceries.data.model.JoinProductCart;
import com.example.groceries.helper.TaskCallback;
import com.example.groceries.data.local.tasks.InsertTask;
import com.example.groceries.helper.TaskResult;

import java.util.List;

import javax.inject.Inject;

public class ProductRepositoryImpl implements IProductRepository {

    ProductDao productDao;
    ProductCartJoinDao prodCartDao;

    @Inject
    public ProductRepositoryImpl(ProductDao productDao,ProductCartJoinDao prodCartDao) {

        this.productDao = productDao;
        this.prodCartDao = prodCartDao;
    }

    @Override
    public void getData(String query, TaskCallback<Product> callback) {
         new FetchTask<>(query,productDao,callback).execute();
    }

    @Override
    public void getJoinedData(String query, TaskCallback<JoinProductCart> callback) {
        new RawFetchTask<>(query,prodCartDao,callback).execute();
    }

    @Override
    public void insertProducts(TaskCallback<Long> callback, List<Product> products) {
         new InsertTask<>(productDao, callback).execute(products);
    }

    @Override
    public void deleteProduct(TaskCallback<Product> callback, List<Product> product) {
        new DeleteTask<>(productDao, callback).execute(product);
    }

    @Override
    public void updateProduct(TaskCallback<Product> callback, List<Product> product) {
        new UpdateTask<>(productDao, callback).execute(product);
    }
}
