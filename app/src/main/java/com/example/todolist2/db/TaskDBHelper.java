package com.example.todolist2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class TaskDBHelper extends SQLiteOpenHelper {
    public TaskDBHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlQuerry = String.format("CREATE TABLE %s (" + "_d INTEGER PRIMARY KEY AUTOINCREMENT, " + "%s TEXT)", TaskContract.TABLE,
        TaskContract.Columns.TAREFA);
        Log.d("TaskDBHelper", "Query to form table" + sqlQuerry);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i1) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TaskContract.TABLE);
    }
}
