package com.example.groceries.data.local.tasks;

import android.os.AsyncTask;

import com.example.groceries.data.local.dao.IBaseDao;
import com.example.groceries.helper.TaskCallback;
import com.example.groceries.helper.TaskResult;

import java.util.List;

public class UpdateTask<T> extends AsyncTask<List<T>, Void, TaskResult<T>> {

    IBaseDao<T> dao;
    TaskCallback<T> callback;

    public UpdateTask(IBaseDao<T> dao, TaskCallback<T> callback) {
        this.dao = dao;
        this.callback = callback;
    }

    @SafeVarargs
    @Override
    protected final TaskResult<T> doInBackground(List<T>... ts) {
        TaskResult<T> result = null;

        try {
            dao.updateItems(ts[0]);
            result = new TaskResult<>(true,"Update success",null);
        }
        catch (Exception e){
            result = new TaskResult<>(false,"Update failed: "+e.getMessage(),null);
        }

        return result;
    }

    @Override
    protected void onPostExecute(TaskResult<T> result) {
        super.onPostExecute(result);

        if (callback != null){
            callback.onFinished(result);
        }
    }
}
