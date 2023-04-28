package com.example.FinPro1_ToDoList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DbHelper dbHelper;
    ArrayAdapter<String> nAdapter;
    ListView listTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);
        listTask = findViewById(R.id.listActivity);
        loadTaskList();
    }

    private void loadTaskList() {
        ArrayList<String> taskList = dbHelper.getTaskList();
        if(nAdapter == null) {
            nAdapter = new ArrayAdapter<String>(this, R.layout.row, R.id.nameactivity, taskList);
            listTask.setAdapter(nAdapter);
        } else {
            nAdapter.clear();
            nAdapter.addAll(taskList);
            nAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_IN);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemAdd: final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add Activity")
                        .setMessage("Add your activity for today!")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String task = String.valueOf(taskEditText.getText());
                                dbHelper.addTask(task);
                                loadTaskList();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void delTask (View view) {
        View parent = (View) view.getParent();
        final TextView taskTextView = (TextView) parent.findViewById(R.id.nameactivity);
        Log.e("String", (String) taskTextView.getText());
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Delete this activity?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String task = String.valueOf(taskTextView.getText());
                        dbHelper.delTask(task);
                        loadTaskList();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}