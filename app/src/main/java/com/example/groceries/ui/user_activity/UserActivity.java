package com.example.groceries.ui.user_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.groceries.R;
import com.example.groceries.adapter.BaseAdapter;
import com.example.groceries.adapter.view_binder.UserListHeaderBinder;
import com.example.groceries.adapter.view_binder.UserProductListItemBinder;
import com.example.groceries.data.local.entity.Cart;
import com.example.groceries.data.local.entity.Category;
import com.example.groceries.data.model.JoinProductCart;
import com.example.groceries.databinding.ActivityUserBinding;
import com.example.groceries.databinding.ProductListHeaderBinding;
import com.example.groceries.databinding.UserProductListItemBinding;
import com.example.groceries.helper.Filter;
import com.example.groceries.helper.enums.SortEnum;
import com.example.groceries.ui.cart.CartActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserActivity extends AppCompatActivity {

    ActivityUserBinding binding = null;
    UserActivityViewModel viewModel;
    private boolean userIsInteracting;
    private UserListHeaderBinder userListHeaderBinder;
    private Filter filter;
    private int productsInCart;

    BaseAdapter<JoinProductCart, UserProductListItemBinding, ProductListHeaderBinding> productAdapter;

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    @SuppressLint({"UnsafeOptInUsageError", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setTitle(getString(R.string.title_products));

        viewModel = new ViewModelProvider(this).get(UserActivityViewModel.class);
        filter = viewModel.getFilter();

        userListHeaderBinder = new UserListHeaderBinder(new UserListHeaderBinder.HeaderEvents() {
            @Override
            public void onCategoryItemSelected(long categoryId,int index) {
                if(userIsInteracting) {
                    if(filter.categoryId == categoryId)
                        return;
                    filter.categoryId = categoryId;
                    filter.categoryIndex = index;
                    viewModel.setFilter(filter);
                    productAdapter.setFilter(filter);
                    //update product list
                    List<JoinProductCart> filteredAndSorted = viewModel.sortAndFilter(filter.categoryId,filter.sort,viewModel.products.getValue());
                    productAdapter.setItems(filteredAndSorted);
                }
            }

            @Override
            public void onSortSelected(SortEnum sort) {
                if(userIsInteracting) {

                    if(filter.sort == sort)
                        return;
                    filter.sort = sort;
                    viewModel.setFilter(filter);
                    productAdapter.setFilter(filter);
                    //update product list
                    List<JoinProductCart> filteredAndSorted = viewModel.sortAndFilter(filter.categoryId,filter.sort,productAdapter.getItems());
                    productAdapter.setItems(filteredAndSorted);
                }
            }
        });

        productAdapter = new BaseAdapter<>(
                R.layout.user_product_list_item,
                R.layout.product_list_header,
                new UserProductListItemBinder((id,position) -> {
                    if(binding.saveFab.getVisibility() != View.VISIBLE)
                        binding.saveFab.setVisibility(View.VISIBLE);
                    binding.saveFab.extend();

                    if(!productAdapter.getChangedItems().containsKey(id))
                        productAdapter.setItemAsChanged(id,position);

                    binding.saveFab.setText(productAdapter.getChangedItems().size()+" Changes");
                }),
                userListHeaderBinder,
                filter,
                true
        );

        binding.productList.setAdapter(productAdapter);

        binding.saveFab.setOnClickListener(view -> {
            saveChanges();
        });

        binding.cartFab.setOnClickListener(view -> {
            CartActivity.Start(UserActivity.this);
        });

        //--------------- Observers ---------------
        viewModel.products.observe(this, products -> {

            List<JoinProductCart> filteredAndSorted = viewModel.sortAndFilter(filter.categoryId,filter.sort,products);

            productAdapter.setItems(filteredAndSorted);
            productsInCart = 0;

            for(JoinProductCart p:products){
                if (p.getInCartCount() > 0){
                    productsInCart++;
                }
            }
            updateCartCount(productsInCart);
        });

        viewModel.getCategories().observe(this, categories -> {
            categories.add(0,new Category(0,getString(R.string.all_categories)));
            userListHeaderBinder.setCategories(categories);
        });

        viewModel.daoResultMessage.observe(this, s -> Toast.makeText(UserActivity.this,s,Toast.LENGTH_LONG).show());
        //--------------- End of  Observers ---------------


    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.getProducts();
    }

    private void updateCartCount(int count){
        binding.cartFab.setText(String.valueOf(count));
    }

    public static void Start(AppCompatActivity activity){

        Intent intent = new Intent(activity, UserActivity.class);
        activity.startActivity(intent);

    }

    private void saveChanges(){

        List<Cart> deletedItems = new ArrayList<>();
        List<Cart> insertedItems = new ArrayList<>();
        List<Cart> updatedItems = new ArrayList<>();

        for (Map.Entry<Long, JoinProductCart> set : productAdapter.getChangedItems().entrySet()) {

            JoinProductCart item = set.getValue();

            if(item.getInCartCount() == 0){
                deletedItems.add(item.cartItemFromObject());
            }
            else if(item.getInCartCount() > 0 && item.getCartId() == 0){
                insertedItems.add(item.cartItemFromObject());
            }
            else{
                updatedItems.add(item.cartItemFromObject());
            }
        }

        viewModel.saveChanges(deletedItems, insertedItems, updatedItems);

        binding.saveFab.setVisibility(View.GONE);
        productAdapter.clearChangedItems();

    }

}