package com.example.groceries.data.local.tasks;

import android.os.AsyncTask;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.groceries.data.local.dao.IBaseDao;
import com.example.groceries.helper.TaskCallback;
import com.example.groceries.helper.TaskResult;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class DeleteTask<T> extends AsyncTask<List<T>, Void, TaskResult<T>> {

    IBaseDao<T> dao;
    TaskCallback<T> callback;

    public DeleteTask(IBaseDao<T> dao, TaskCallback<T> callback) {
        this.callback = callback;
        this.dao = dao;
    }

    @SafeVarargs
    @Override
    protected final TaskResult<T> doInBackground(List<T>... ts) {
        TaskResult<T> result = null;

        try {
            dao.deleteAll(ts[0]);
            result = new TaskResult<>(true,"Delete success",null);
        }
        catch (Exception e){
            result = new TaskResult<>(false,"Delete failed: "+e.getMessage(),null);
        }

        return result;
    }

    @Override
    protected void onPostExecute(TaskResult<T> result) {
        super.onPostExecute(result);

        if(callback != null){
            callback.onFinished(result);
        }
    }
}
