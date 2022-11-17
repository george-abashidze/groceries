package com.example.groceries.data.local.tasks;

import android.os.AsyncTask;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.groceries.data.local.dao.IBaseDao;
import com.example.groceries.helper.TaskCallback;
import com.example.groceries.helper.TaskResult;

import java.util.List;

public class FetchTask<T> extends AsyncTask<Void,Void, TaskResult<T>> {

    IBaseDao<T> dao;
    TaskCallback<T> callback;
    String query;

    public FetchTask(String query,IBaseDao<T> dao, TaskCallback<T> callback) {
        this.dao = dao;
        this.callback = callback;
        this.query = query;
    }

    @Override
    protected TaskResult<T> doInBackground(Void... voids) {

        TaskResult<T> result = null;

        try {
            List<T> data = dao.getData(new SimpleSQLiteQuery(query));
            result = new TaskResult<>(true,"Fetch success",data);
        }
        catch (Exception e){
            result = new TaskResult<>(true,"Fetch failed",null);
        }
        return result;
    }

    @Override
    protected void onPostExecute(TaskResult<T> tTaskResult) {

        super.onPostExecute(tTaskResult);

        if(callback != null){
            callback.onFinished(tTaskResult);
        }


    }
}
