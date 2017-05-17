package io.github.andres_vasquez.flatfoottodo.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import io.github.andres_vasquez.flatfoottodo.model.Todo;

/**
 * Created by andresvasquez on 5/16/17.
 */

/**
 * Class to handle database actions create/update in background thread
 */
public class TodoHelper {

    private static final String LOG = TodoHelper.class.getSimpleName();

    private Context mContext;
    private AppDatabase mDb;

    /**
     * Constructor of TodoHelper class to initialize database
     * @param mContext Context of the application
     */
    public TodoHelper(Context mContext) {
        this.mContext = mContext;
        mDb=AppDatabase.getDatabase(mContext);
    }

    /**
     * Insert todos registry in background
     * @param todo
     */
    public void insertTodoAsync(Todo todo){
        new InsertTodo().execute(todo);
    }

    /**
     * Update todos registry in background
     * @param todo
     */
    public void updateTodoAsync(Todo todo){
        new UpdateTodo().execute(todo);
    }

    /**
     * Async task to avoid Main Thread when inserts a todos registry
     */
    private class InsertTodo extends AsyncTask<Todo,Void,Long>{
        @Override
        protected Long doInBackground(Todo... params) {
            return mDb.todoModel().insertTodoTask(params[0]);
        }
        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            Log.i(LOG,"Inserted id: "+result);
        }
    }

    /**
     * Async task to avoid Main Thread when update a todos registry
     */
    private class UpdateTodo extends AsyncTask<Todo,Void,Integer>{
        @Override
        protected Integer doInBackground(Todo... params) {
            return mDb.todoModel().updateTodoTask(params[0]);
        }
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Log.i(LOG,"Affected row: "+result);
        }
    }
}
