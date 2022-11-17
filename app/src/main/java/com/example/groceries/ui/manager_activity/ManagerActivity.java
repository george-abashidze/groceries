package com.example.groceries.ui.manager_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.groceries.R;
import com.example.groceries.adapter.BaseAdapter;
import com.example.groceries.adapter.view_binder.ManagerListHeaderBinder;
import com.example.groceries.adapter.view_binder.UserListHeaderBinder;
import com.example.groceries.adapter.view_binder.ManagerProductListItemBinder;
import com.example.groceries.data.local.entity.Category;
import com.example.groceries.data.local.entity.Product;
import com.example.groceries.data.model.JoinProductCart;
import com.example.groceries.databinding.ActivityManagerBinding;
import com.example.groceries.databinding.ManagerProductListHeaderBinding;
import com.example.groceries.databinding.ManagerProductListItemBinding;
import com.example.groceries.helper.Filter;
import com.example.groceries.helper.enums.SortEnum;
import com.example.groceries.ui.category.CategoryActivity;
import com.example.groceries.ui.dialog.ProductFormDialog;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManagerActivity extends AppCompatActivity {

    ActivityManagerBinding binding = null;
    ManagerActivityViewModel viewModel;
    BaseAdapter<JoinProductCart, ManagerProductListItemBinding, ManagerProductListHeaderBinding> productListAdapter;
    private ManagerListHeaderBinder headerBinder;
    private boolean userIsInteracting;
    Filter filter;

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle(getString(R.string.title_products));

        viewModel = new ViewModelProvider(this).get(ManagerActivityViewModel.class);
        filter = viewModel.getFilter();


        headerBinder = new ManagerListHeaderBinder(new ManagerListHeaderBinder.HeaderEvents() {
            @Override
            public void onCategoryItemSelected(long categoryId,int index) {
                if(userIsInteracting) {

                    if(filter.categoryId == categoryId)
                        return;
                    filter.categoryId = categoryId;
                    filter.categoryIndex = index;
                    viewModel.setFilter(filter);
                    productListAdapter.setFilter(filter);
                    //update product list
                    List<JoinProductCart> filteredAndSorted = viewModel.sortAndFilter(filter.categoryId,filter.sort,viewModel.products.getValue());
                    productListAdapter.setItems(filteredAndSorted);
                }
            }


            @Override
            public void onSortSelected(SortEnum sort) {
                if(userIsInteracting) {

                    if(filter.sort == sort)
                        return;
                    filter.sort = sort;
                    viewModel.setFilter(filter);
                    productListAdapter.setFilter(filter);
                    //update product list
                    List<JoinProductCart> filteredAndSorted = viewModel.sortAndFilter(filter.categoryId,filter.sort,productListAdapter.getItems());
                    productListAdapter.setItems(filteredAndSorted);
                }
            }
        });



        productListAdapter = new BaseAdapter<>(
                R.layout.manager_product_list_item,
                R.layout.manager_product_list_header,
                new ManagerProductListItemBinder(new ManagerProductListItemBinder.ManagerProductEvents() {
                    @Override
                    public void onEditClick(JoinProductCart item) {

                        ArrayList<Category> categories = new ArrayList<>(headerBinder.getCategories());
                        int quantityBeforeEdit = item.getQuantity();
                        if(categories.size() > 0)
                            categories.remove(0);

                        String title = getString(R.string.edit_product);

                        ProductFormDialog dialog = ProductFormDialog.newInstance(
                                title,
                                item.productFromObject(),
                                categories,
                                data -> {
                                    viewModel.updateProduct(data);
                                    if(quantityBeforeEdit != data.getQuantity()){
                                        viewModel.updateCartItem(item.getCartId(),data.getQuantity());
                                    }
                                }
                        );

                        dialog.show(getSupportFragmentManager(),ProductFormDialog.TAG);
                    }

                    @Override
                    public void onDeleteClick(JoinProductCart item) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(ManagerActivity.this);
                        builder.setMessage("Are you sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        viewModel.deleteProduct(item.productFromObject());
                                    }
                                })
                                .setNegativeButton("No", null).show();
                    }
                }),
                headerBinder,
                filter,
                true
        );

        binding.productList.setAdapter(productListAdapter);


        //---------------- UI Events -----------

        binding.addFab.setOnClickListener(view -> {

            ArrayList<Category> categories = new ArrayList<>(headerBinder.getCategories());

            if(categories.size() > 0)
                categories.remove(0);

            String title = getString(R.string.edit_product);

            ProductFormDialog dialog = ProductFormDialog.newInstance(
                    title,
                    null,
                    categories,
                    data -> {
                        viewModel.insertProduct(data);
                    }
            );

            dialog.show(getSupportFragmentManager(),ProductFormDialog.TAG);


        });

        //---------------- End of UI Events -----------



        //---------------- Observers -----------
        viewModel.products.observe(this, products -> {
            List<JoinProductCart> filteredAndSorted = viewModel.sortAndFilter(filter.categoryId,filter.sort,products);

            productListAdapter.setItems(filteredAndSorted);
        });

        viewModel.getCategories().observe(this, categories -> {

            categories.add(0,new Category(0,getString(R.string.all_categories)));

            if(categories.size() - 1 < filter.categoryIndex){
                filter.categoryId = 0;
                filter.categoryIndex = 0;
                viewModel.setFilter(filter);

                productListAdapter.setFilter(filter);

            }
            else if(categories.get(filter.categoryIndex).getId() != filter.categoryId){
                filter.categoryId = 0;
                filter.categoryIndex = 0;
                viewModel.setFilter(filter);

                productListAdapter.setFilter(filter);
            }

            headerBinder.setCategories(categories);
        });

        viewModel.daoResultMessage.observe(this, s -> Toast.makeText(ManagerActivity.this,s,Toast.LENGTH_LONG).show());
        //---------------- End of observers -----------


    }

    @Override
    protected void onResume() {
        super.onResume();
        //get data
        viewModel.getProducts();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_categories){
            CategoryActivity.Start(ManagerActivity.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    public static void Start(AppCompatActivity activity){

        Intent intent = new Intent(activity, ManagerActivity.class);
        activity.startActivity(intent);

    }
}