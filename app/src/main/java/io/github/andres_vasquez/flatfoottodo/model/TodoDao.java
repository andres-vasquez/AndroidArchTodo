package io.github.andres_vasquez.flatfoottodo.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by andresvasquez on 4/21/17.
 */

@Dao
public interface TodoDao {

    @Insert(onConflict = IGNORE)
    long insertTodoTask(Todo todo);

    @Update(onConflict = REPLACE)
    int updateTodoTask(Todo todo);

    @Query("DELETE FROM Todo")
    void deleteAll();

    @Query("SELECT * FROM Todo")
    LiveData<List<Todo>> getAllTodos();
}
