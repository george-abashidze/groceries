package com.example.groceries.ui.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.groceries.data.local.entity.Category;
import com.example.groceries.data.local.entity.Product;
import com.example.groceries.data.local.repository.contract.ICategoryRepository;
import com.example.groceries.helper.TaskCallback;
import com.example.groceries.helper.TaskResult;

import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class CategoryActivityViewModel extends ViewModel {

    ICategoryRepository categoryRepo;
    MutableLiveData<String> daoResultMessage = new MutableLiveData<>();

    @Inject
    public CategoryActivityViewModel(ICategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public LiveData<List<Category>> getCategories(){
        return categoryRepo.getLiveData();
    }

    public void insertCategory(String name){
        categoryRepo.insertCategories(new TaskCallback<Long>() {
            @Override
            public void onFinished(TaskResult<Long> result) {

                if(!result.isSuccess())
                    daoResultMessage.setValue(result.getMessage());

            }
        }, Collections.singletonList(new Category(name)));
    }

    public void deleteCategory(Category category){
        categoryRepo.deleteCategory(new TaskCallback<Category>() {
            @Override
            public void onFinished(TaskResult<Category> result) {
                if(!result.isSuccess()){
                    daoResultMessage.setValue(result.getMessage());
                }
            }
        }, Collections.singletonList(category));
    }

    public void updateCategory(Category category){
        categoryRepo.updateCategory(new TaskCallback<Category>() {
            @Override
            public void onFinished(TaskResult<Category> result) {
                if(!result.isSuccess()){
                    daoResultMessage.setValue(result.getMessage());
                }
            }
        }, Collections.singletonList(category));
    }



}
