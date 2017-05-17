/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.andres_vasquez.flatfoottodo.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import io.github.andres_vasquez.flatfoottodo.model.Todo;
import io.github.andres_vasquez.flatfoottodo.model.TodoDao;

import static io.github.andres_vasquez.flatfoottodo.database.AppDatabase.DATABASE_VERSION;

@Database(entities = {Todo.class},
        version = DATABASE_VERSION)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "todo";
    static final int DATABASE_VERSION = 1; //Variable public to use in anotation process

    private static AppDatabase INSTANCE;
    public abstract TodoDao todoModel();

    /**
     * Get database instance
     * @param context Context of the application
     * @return instance of database
     */
    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME).build();
        }
        return INSTANCE;
    }

    /**
     * Destroy database instance
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }
}