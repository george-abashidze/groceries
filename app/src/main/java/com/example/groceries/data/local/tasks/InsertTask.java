package com.example.groceries.data.local.tasks;

import android.os.AsyncTask;

import com.example.groceries.data.local.dao.IBaseDao;
import com.example.groceries.helper.TaskCallback;
import com.example.groceries.helper.TaskResult;

import java.util.List;


public class InsertTask<T> extends AsyncTask<List<T>, Void, TaskResult<Long>> {

    IBaseDao<T> dao;
    TaskCallback<Long> callback;

    public InsertTask(IBaseDao<T> dao, TaskCallback<Long> callback) {
        this.callback = callback;
        this.dao = dao;
    }

    @SafeVarargs
    @Override
    protected final TaskResult<Long> doInBackground(List<T>... ts) {
        TaskResult<Long> result = null;
        try {
            List<Long> ids = dao.insertAll(ts[0]);
            result = new TaskResult<>(true,"Insert success",ids);
        }
        catch (Exception e){
            result = new TaskResult<>(false,"Insert fail: "+e.getMessage(),null);
        }
        return result;
    }

    @Override
    protected void onPostExecute(TaskResult<Long> result) {
        super.onPostExecute(result);

        if(callback != null){
            callback.onFinished(result);
        }
    }
}
