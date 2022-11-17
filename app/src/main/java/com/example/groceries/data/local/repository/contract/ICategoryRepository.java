package com.example.groceries.data.local.repository.contract;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.groceries.data.local.entity.Category;
import com.example.groceries.data.local.entity.Product;
import com.example.groceries.helper.TaskCallback;
import com.example.groceries.helper.TaskResult;

import java.util.List;

public interface ICategoryRepository {
    void getData(String query, TaskCallback<Category> callback);
    LiveData<List<Category>> getLiveData();
    void insertCategories(TaskCallback<Long> callback, List<Category> categories);
    void deleteCategory(TaskCallback<Category> callback, List<Category> category);
    void updateCategory(TaskCallback<Category> callback, List<Category> category);
}
