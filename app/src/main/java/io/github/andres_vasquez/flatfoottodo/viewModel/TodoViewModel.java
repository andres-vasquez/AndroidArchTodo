package io.github.andres_vasquez.flatfoottodo.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import io.github.andres_vasquez.flatfoottodo.utils.Constants;
import io.github.andres_vasquez.flatfoottodo.database.AppDatabase;
import io.github.andres_vasquez.flatfoottodo.model.Todo;

/**
 * Created by andresvasquez on 5/16/17.
 */

/**
 * TodoViewModel class for get todos tasks from database with Room
 */
public class TodoViewModel extends AndroidViewModel {

    private static final String LOG = TodoViewModel.class.getSimpleName();
    private AppDatabase mDb;

    @Nullable
    private LiveData<List<Todo>> mTodoList;

    private MutableLiveData<Integer> mFilter =new MutableLiveData<>();
    public MutableLiveData<Integer> getmFilter() {
        return mFilter;
    }

    @Nullable
    public LiveData<List<Todo>> getTodoList() {
        return mTodoList;
    }

    public TodoViewModel(Application application) {
        super(application);
        mFilter.setValue(Constants.FILTER_DEFAULT);
        openDb();
    }

    private void openDb() {
        mDb = AppDatabase.getDatabase(getApplication());

        // Receive changes
        subscribeToDbChanges();
    }

    /**
     * Get all Todos in LiveData variable
     */
    private void subscribeToDbChanges() {
        try {
            mTodoList = mDb.todoModel().getAllTodos();
        }catch (NullPointerException ex){
            Log.e(LOG,""+ex.getMessage());
        }
    }
}
