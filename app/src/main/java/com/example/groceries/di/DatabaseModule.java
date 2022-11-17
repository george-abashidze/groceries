package com.example.groceries.di;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.groceries.data.local.LocalDatabase;
import com.example.groceries.data.local.dao.CartDao;
import com.example.groceries.data.local.dao.CartProductJoinDao;
import com.example.groceries.data.local.dao.CategoryDao;
import com.example.groceries.data.local.dao.ProductCartJoinDao;
import com.example.groceries.data.local.dao.ProductDao;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class DatabaseModule {

    @Provides
    CategoryDao provideCategoryDao(LocalDatabase db) {
        return db.categoryDao();
    }

    @Provides
    ProductDao provideProductDao(LocalDatabase db) {
        return db.productDao();
    }

    @Provides
    CartDao provideCartDao(LocalDatabase db) {
        return db.cartDao();
    }

    @Provides
    ProductCartJoinDao provideProductCartJoinDao(LocalDatabase db) {
        return db.productCartDao();
    }

    @Provides
    CartProductJoinDao provideCartProductJoinDao(LocalDatabase db){return db.cartProductDao();}

    @Provides
    @Singleton
    LocalDatabase provideAppDatabase(@ApplicationContext Context appContext) {
        return Room.databaseBuilder(
                appContext,
                LocalDatabase.class,
                "localDb"
        ).addCallback(new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                Executors.newSingleThreadScheduledExecutor().execute(() -> {
                    ContentValues values = new ContentValues();
                    values.put("name","Default");
                    db.insert("category", SQLiteDatabase.CONFLICT_REPLACE,values);
                });
            }
        }).build();
    }

}
