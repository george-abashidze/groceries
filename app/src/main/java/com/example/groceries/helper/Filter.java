package com.example.groceries.helper;

import com.example.groceries.helper.enums.SortEnum;

public class Filter {

    public long categoryId;
    public int categoryIndex;
    public SortEnum sort;

    public Filter(int categoryId, SortEnum sort) {
        this.categoryId = categoryId;
        this.sort = sort;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public SortEnum getSort() {
        return sort;
    }

    public void setSort(SortEnum sort) {
        this.sort = sort;
    }


}
