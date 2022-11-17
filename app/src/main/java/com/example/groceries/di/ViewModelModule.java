package com.example.groceries.di;

import com.example.groceries.data.local.repository.contract.ICartRepository;
import com.example.groceries.data.local.repository.contract.ICategoryRepository;
import com.example.groceries.data.local.repository.contract.IProductRepository;
import com.example.groceries.data.local.repository.implementation.CartRepositoryImpl;
import com.example.groceries.data.local.repository.implementation.CategoryRepositoryImpl;
import com.example.groceries.data.local.repository.implementation.ProductRepositoryImpl;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

@InstallIn(ViewModelComponent.class)
@Module
public class ViewModelModule {

    @Provides
    IProductRepository provideProductRepository(ProductRepositoryImpl productRepo) {
        return  productRepo;
    }

    @Provides
    ICategoryRepository provideCategoryRepository(CategoryRepositoryImpl categoryRepo) {
        return  categoryRepo;
    }

    @Provides
    ICartRepository provideCartRepository(CartRepositoryImpl cartRepository) {
        return  cartRepository;
    }


}
