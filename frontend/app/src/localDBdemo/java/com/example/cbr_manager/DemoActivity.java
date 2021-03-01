package com.example.cbr_manager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.ListIterator;

public class DemoActivity extends AppCompatActivity {

    RoomDB db;
    ClientDBDao clientDao;

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
        db = RoomDB.getDatabase(getApplicationContext());
        clientDao = db.clientDao();
    }

    // close the database to prevent data leaks
    private void closeDB(){
        if(db != null){
            if(db.isOpen()){
                db.close();
            }
            db = null;
        }
    }


    private void displayText(String message) {
        TextView textView = (TextView) findViewById(R.id.textDisplay);
        textView.setText(message);
    }

    public void onClick_AddRecord(View v){
        displayText("Clicked add record!");
        ClientDB client = new ClientDB("Waldo", "Tester");
        clientDao.insert(client);
    }

    public void onClick_ClearAll(View v){
        displayText("Clicked clear all!");
        clientDao.clearAll();
    }

    public void onClick_DisplayRecords(View v){
        displayText("Clicked display record!");
        displayRecords(clientDao.getName());

    }

    public void onClick_Search(View v){
        displayText("Clicked search record!");
        int[] searchID = new int[] {4,5,6};
        displayRecords(clientDao.getByIds(searchID));
    }

    private void displayRecords(List<ClientDB> list){
        String display = "";
        for(int i=0; i<list.size(); i++){
            display = display + list.get(i).getId() + ", " + list.get(i).getFullName() + "\n";
        }
        displayText(display);
    }
}