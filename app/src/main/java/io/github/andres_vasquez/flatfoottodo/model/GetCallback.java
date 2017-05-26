package io.github.andres_vasquez.flatfoottodo.model;

import java.util.List;

/**
 * Created by andresvasquez on 5/24/17.
 */

public interface GetCallback {
    void onLoad(List<Todo> todoList);
}
