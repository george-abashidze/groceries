package com.example.groceries.ui.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.example.groceries.R;
import com.example.groceries.adapter.BaseAdapter;
import com.example.groceries.adapter.view_binder.CartProductListItemBinder;
import com.example.groceries.adapter.view_binder.UserListHeaderBinder;
import com.example.groceries.data.local.entity.Cart;
import com.example.groceries.data.local.entity.Category;
import com.example.groceries.data.model.JoinCartProduct;
import com.example.groceries.databinding.ActivityCartBinding;
import com.example.groceries.databinding.CartListItemBinding;
import com.example.groceries.databinding.ProductListHeaderBinding;
import com.example.groceries.helper.Filter;
import com.example.groceries.helper.enums.SortEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CartActivity extends AppCompatActivity {

    CartActivityViewModel viewModel;
    ActivityCartBinding binding = null;
    private boolean userIsInteracting;
    private UserListHeaderBinder userListHeaderBinder;
    private Filter filter;
    BaseAdapter<JoinCartProduct, CartListItemBinding, ProductListHeaderBinding> adapter;
    private double total;

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle(getString(R.string.cart));

        viewModel = new ViewModelProvider(this).get(CartActivityViewModel.class);
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
                    adapter.setFilter(filter);
                    //update product list
                    List<JoinCartProduct> filteredAndSorted = viewModel.sortAndFilter(filter.categoryId,filter.sort,viewModel.cartItems.getValue());
                    adapter.setItems(filteredAndSorted);
                }
            }

            @Override
            public void onSortSelected(SortEnum sort) {
                if(userIsInteracting) {

                    if(filter.sort == sort)
                        return;
                    filter.sort = sort;
                    viewModel.setFilter(filter);
                    adapter.setFilter(filter);
                    //update product list
                    List<JoinCartProduct> filteredAndSorted = viewModel.sortAndFilter(filter.categoryId,filter.sort,adapter.getItems());
                    adapter.setItems(filteredAndSorted);
                }
            }
        });


        adapter = new BaseAdapter<>(
                R.layout.cart_list_item,
                R.layout.product_list_header,
                new CartProductListItemBinder(new CartProductListItemBinder.CartProductListEvents() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onIncrement(long itemId, int index, double price) {
                        if(binding.saveFab.getVisibility() != View.VISIBLE)
                            binding.saveFab.setVisibility(View.VISIBLE);
                        binding.saveFab.extend();

                        if(!adapter.getChangedItems().containsKey(itemId))
                            adapter.setItemAsChanged(itemId,index);

                        total += price;
                        updateTotal();
                        binding.saveFab.setText(adapter.getChangedItems().size()+" Changes");
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDecrement(long itemId, int index, double price) {
                        if(binding.saveFab.getVisibility() != View.VISIBLE)
                            binding.saveFab.setVisibility(View.VISIBLE);
                        binding.saveFab.extend();

                        if(!adapter.getChangedItems().containsKey(itemId))
                            adapter.setItemAsChanged(itemId,index);

                        JoinCartProduct item = adapter.getItemAt(index);
                        if (item.getQuantity() <= 0)
                            adapter.removeItemAt(index);
                        total -= price;
                        if(total < 0)
                            total = 0;
                        updateTotal();
                        binding.saveFab.setText(adapter.getChangedItems().size()+" Changes");
                    }
                }),
                userListHeaderBinder,
                filter,
                true
        );

        binding.productList.setAdapter(adapter);

        binding.saveFab.setOnClickListener(view -> {
            saveChanges();
        });

        // ----------------- observers ---------------
        viewModel.cartItems.observe(this, carts -> {

            List<JoinCartProduct> filteredAndSorted = viewModel.sortAndFilter(filter.categoryId,filter.sort,carts);

            adapter.setItems(filteredAndSorted);

            total = 0;

            for(JoinCartProduct item:carts){
                total+= item.getQuantity() * item.getUnitPrice();
            }

            updateTotal();
        });

        viewModel.getCategories().observe(this, categories -> {
            categories.add(0,new Category(0,getString(R.string.all_categories)));
            userListHeaderBinder.setCategories(categories);
        });

        viewModel.daoResultMessage.observe(this, s -> Toast.makeText(CartActivity.this,s,Toast.LENGTH_LONG).show());
        // -------------------- end of observers ------------

        viewModel.getItems();

    }

    @SuppressLint("SetTextI18n")
    private void updateTotal(){
        binding.total.setText(Html.fromHtml(getString(R.string.total_value,String.format(Locale.US,"%.2f",total))));
    }

    private void saveChanges(){
        List<Cart> deletedItems = new ArrayList<>();
        List<Cart> updatedItems = new ArrayList<>();

        for (Map.Entry<Long, JoinCartProduct> set : adapter.getChangedItems().entrySet()) {

            JoinCartProduct item = set.getValue();

            if(item.getQuantity() == 0){
                deletedItems.add(item.cartItemFromObject());
            }
            else{
                updatedItems.add(item.cartItemFromObject());
            }
        }

        viewModel.saveChanges(deletedItems,updatedItems);

        binding.saveFab.setVisibility(View.GONE);
        adapter.clearChangedItems();
    }

    public static void Start(AppCompatActivity activity){

        Intent intent = new Intent(activity, CartActivity.class);
        activity.startActivity(intent);

    }
}