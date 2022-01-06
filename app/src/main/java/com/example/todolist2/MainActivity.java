package com.example.todolist2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist2.db.TaskContract;
import com.example.todolist2.db.TaskDBHelper;

public class MainActivity extends AppCompatActivity {

    TaskDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateUI();
    }

    private void updateUI(){
        try
        {
            helper = new TaskDBHelper(MainActivity.this);
            SQLiteDatabase sqlDB = helper.getReadableDatabase();
            Cursor cursor = sqlDB.query(TaskContract.TABLE, new String[]
                    {TaskContract.Columns._ID, TaskContract.Columns.TAREFA},
                    null, null, null, null, null);

            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                    this,
                    R.layout.celula,
                    cursor,
                    new String[]{TaskContract.Columns.TAREFA},
                    new int[]{R.id.txtCelula},
                    0
            );

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void adicionarItem(View view){
        try
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add a task");
            builder.setMessage("What is your task?");
            final EditText inputField = new EditText(this);
            builder.setView(inputField);
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String tarefa = inputField.getText().toString();
                    if(tarefa.equals("")){
                        Toast.makeText(getApplicationContext(), "Well, that was a waste, no task added", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.d("MainActivity", tarefa);

                        helper = new TaskDBHelper(MainActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.clear();
                        values.put(TaskContract.Columns.TAREFA, tarefa);

                        db.insertWithOnConflict(TaskContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                        updateUI();
                    }
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.create().show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void apagarItem(View view){
        try
        {
            View v = (View) view.getParent();
            TextView taskTextView = (TextView) v.findViewById(R.id.txtCelula);
            String tarefa = taskTextView.getText().toString();

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to delete this task?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                            TaskContract.TABLE,
                            TaskContract.Columns.TAREFA, tarefa);

                    helper = new TaskDBHelper(MainActivity.this);
                    SQLiteDatabase sqlDB = helper.getReadableDatabase();
                    sqlDB.execSQL(sql);
                    updateUI();
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.create().show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}