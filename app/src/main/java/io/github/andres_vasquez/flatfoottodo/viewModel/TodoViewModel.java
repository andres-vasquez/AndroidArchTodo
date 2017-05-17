package io.github.andres_vasquez.flatfoottodo.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.Nullable;

import java.util.List;

import io.github.andres_vasquez.flatfoottodo.database.AppDatabase;
import io.github.andres_vasquez.flatfoottodo.model.Todo;

/**
 * Created by andresvasquez on 5/16/17.
 */

/**
 * TodoViewModel class for get todos tasks from database with Room
 */
public class TodoViewModel extends AndroidViewModel {
    private AppDatabase mDb;

    @Nullable
    private LiveData<List<Todo>> mTodoList;

    @Nullable
    public LiveData<List<Todo>> getTodoList() {
        return mTodoList;
    }

    public TodoViewModel(Application application) {
        super(application);
        openDb();
    }

    public void openDb() {
        mDb = AppDatabase.getDatabase(getApplication());

        // Receive changes
        subscribeToDbChanges();
    }

    /**
     * Get all Todos in LiveData variable
     */
    private void subscribeToDbChanges() {
        mTodoList = mDb.todoModel().getAllTodos();
    }
}
