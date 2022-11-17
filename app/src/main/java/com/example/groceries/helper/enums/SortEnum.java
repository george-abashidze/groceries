package com.example.groceries.helper.enums;

public enum SortEnum {
    NONE(0),
    BY_NAME(1),
    BY_PRICE(2),
    BY_NAME_DESC(3),
    BY_PRICE_DESC(4);

    private final int value;

    SortEnum(final int i) {
        value = i;
    }

    public int getValue() { return value; }
}
