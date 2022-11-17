package com.example.groceries.data.local.dao;

import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

public interface IRawBaseDao<T> {
    @RawQuery
    List<T> getData(SupportSQLiteQuery query);
}
