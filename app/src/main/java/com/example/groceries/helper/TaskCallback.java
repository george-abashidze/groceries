package com.example.groceries.helper;

public interface TaskCallback<T> {
    void onFinished(TaskResult<T> result);
}
