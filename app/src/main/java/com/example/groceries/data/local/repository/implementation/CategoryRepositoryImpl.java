package com.example.groceries.data.local.repository.implementation;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.groceries.data.local.dao.CategoryDao;
import com.example.groceries.data.local.entity.Category;
import com.example.groceries.data.local.repository.contract.ICategoryRepository;
import com.example.groceries.data.local.tasks.DeleteTask;
import com.example.groceries.data.local.tasks.FetchTask;
import com.example.groceries.data.local.tasks.UpdateTask;
import com.example.groceries.helper.TaskCallback;
import com.example.groceries.data.local.tasks.InsertTask;
import com.example.groceries.helper.TaskResult;

import java.util.List;

import javax.inject.Inject;

public class CategoryRepositoryImpl implements ICategoryRepository {


    CategoryDao categoryDao;

    @Inject
    public CategoryRepositoryImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }


    @Override
    public void getData(String query, TaskCallback<Category> callback) {
        new FetchTask<>(query,categoryDao,callback).execute();
    }

    @Override
    public LiveData<List<Category>> getLiveData() {
        return categoryDao.getLiveData();
    }

    @Override
    public void insertCategories(TaskCallback<Long> callback, List<Category> categories) {
         new InsertTask<>(categoryDao,callback).execute(categories);
    }

    @Override
    public void deleteCategory(TaskCallback<Category> callback, List<Category> category) {
        new DeleteTask<>(categoryDao,callback).execute(category);
    }

    @Override
    public void updateCategory(TaskCallback<Category> callback, List<Category> category) {
        new UpdateTask<>(categoryDao,callback).execute(category);
    }
}
