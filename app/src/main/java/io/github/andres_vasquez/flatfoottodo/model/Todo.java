package io.github.andres_vasquez.flatfoottodo.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by andresvasquez on 5/16/17.
 */

@Entity
public class Todo {
    private @PrimaryKey(autoGenerate = true) int id;
    private String name;
    private boolean finished;

    public Todo(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
