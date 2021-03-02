package com.example.cbr_manager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DemoActivity extends AppCompatActivity {

    RoomDB mDB;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    private List<ClientDB> returnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        openDB();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    // open the database to operate (possible operations is recorded in Dao interface)
    // RoomDB can have multiple tables, with each table representing one class object, defined in database entities
    private void openDB() {
        mDB = RoomDB.getDatabase(getApplicationContext());
    }

    // close the database to prevent data leaks
    private void closeDB() {
        if (mDB != null) {
            if (mDB.isOpen()) {
                mDB.close();
            }
            mDB = null;
        }
    }


    private void displayText(String message) {
        TextView textView = (TextView) findViewById(R.id.textDisplay);
        textView.setText(message);
    }

    public void onClick_AddRecord(View v) {
        displayText("Clicked add record!");
        ClientDB client = new ClientDB("Waldo", "Asdas");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDB.clientDao().insert(client);
            }
        });
    }

    public void onClick_ClearAll(View v) {
        displayText("Clicked clear all!");
        DBExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDB.clientDao().clearAll();
            }
        });
    }

    public void onClick_DisplayRecords(View v) {
        displayText("Clicked display record!");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                returnList = mDB.clientDao().getName();
                DemoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayRecords(returnList);
                    }
                });
            }
        });
    }

    public void onClick_Search(View v) {
        displayText("Clicked search record!");
        int[] searchID = new int[]{4, 5, 6};
        DBExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                returnList = mDB.clientDao().getByIds(searchID);
                DBExecutor.getInstance().getUIThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        displayRecords(returnList);
                    }
                });
            }
        });
    }

    private void displayRecords(List<ClientDB> list) {
        String display = "";
        for (int i = 0; i < list.size(); i++) {
            display = display + list.get(i).getId() + ", " + list.get(i).getFullName() + "\n";
        }
        displayText(display);
    }
}