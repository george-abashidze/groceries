package com.example.groceries.ui.category;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.groceries.R;
import com.example.groceries.adapter.BaseAdapter;
import com.example.groceries.adapter.view_binder.CategoryListItemBinder;
import com.example.groceries.data.local.entity.Category;
import com.example.groceries.databinding.ActivityCategoryBinding;
import com.example.groceries.databinding.CategoryListItemBinding;
import com.example.groceries.ui.dialog.SingleInputDialog;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CategoryActivity extends AppCompatActivity {

    ActivityCategoryBinding binding = null;
    CategoryActivityViewModel viewModel;
    BaseAdapter<Category, CategoryListItemBinding,?> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle(getString(R.string.title_categories));

        adapter = new BaseAdapter<>(
                R.layout.category_list_item,
                0,
                new CategoryListItemBinder(new CategoryListItemBinder.CategoryEvents() {
                    @Override
                    public void onEditClick(Category item) {

                        String title = getString(R.string.edit_category);
                        SingleInputDialog dialog = SingleInputDialog.newInstance(title, item.getName(), new SingleInputDialog.DialogCallback() {
                            @Override
                            public void onFinish(String data) {
                                item.setName(data);
                                viewModel.updateCategory(item);
                            }
                        });

                        dialog.show(getSupportFragmentManager(),SingleInputDialog.TAG);
                    }

                    @Override
                    public void onDeleteClick(Category item) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
                        builder.setMessage("Are you sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        viewModel.deleteCategory(item);
                                    }
                                })
                                .setNegativeButton("No", null).show();
                    }
                }),
                null,
                null,
                false
        );

        viewModel = new ViewModelProvider(this).get(CategoryActivityViewModel.class);
        binding.categoryList.setAdapter(adapter);

        viewModel.getCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                adapter.setItems(categories);
            }
        });

        viewModel.daoResultMessage.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(CategoryActivity.this,s,Toast.LENGTH_LONG).show();
            }
        });

        binding.addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = getString(R.string.new_category);
                SingleInputDialog dialog = SingleInputDialog.newInstance(title, null, new SingleInputDialog.DialogCallback() {
                    @Override
                    public void onFinish(String data) {
                        viewModel.insertCategory(data);
                    }
                });

                dialog.show(getSupportFragmentManager(),SingleInputDialog.TAG);
            }
        });
    }

    public static void Start(AppCompatActivity activity){

        Intent intent = new Intent(activity, CategoryActivity.class);
        activity.startActivity(intent);

    }
}