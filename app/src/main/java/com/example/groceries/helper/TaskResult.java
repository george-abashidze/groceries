package com.example.groceries.helper;

import java.util.List;

public class TaskResult<T> {
    private final boolean isSuccess;
    private final String message;
    private final List<T> data;

    public TaskResult(boolean isSuccess, String message, List<T> data) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public List<T> getData() {
        return data;
    }
}
