package com.example.carsproject;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    protected void initDb() throws SQLException {
        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(
                        getFilesDir().getPath()+"/cars.db",
                        null
                );
        String CreateQ = "CREATE TABLE IF NOT EXISTS CARS( " +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Brand TEXT NOT NULL," +
                "Price TEXT NOT NULL," +
                "Year TEXT NOT NULL );";

        db.execSQL(CreateQ);
        db.close();
    }

    protected void ExecSQL(String SQL, Object[] args, OnQuerySuccess success) throws SQLException{
        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(
                        getFilesDir().getPath()+"/cars.db",
                        null
                );
        db.execSQL(SQL,args);
        success.OnSuccess();
        db.close();
    }

    public void SelectSQL(String SQL, String[] args, OnSelectElement iterate)
    throws Exception
    {
        SQLiteDatabase db=
                SQLiteDatabase.openOrCreateDatabase(
                        getFilesDir().getPath()+"/cars.db",
                        null
                );
        Cursor cursor=db.rawQuery(SQL,args);
        while(cursor.moveToNext()){
            String ID = cursor.getString(cursor.getColumnIndex("ID"));
            String Brand = cursor.getString(cursor.getColumnIndex("Brand"));
            String Price = cursor.getString(cursor.getColumnIndex("Price"));
            String Year = cursor.getString(cursor.getColumnIndex("Year"));
            iterate.OnElementIterate(Brand,Price,Year,ID);
        }
        db.close();
    }

    protected interface OnQuerySuccess{
        public void OnSuccess();
    }

    protected interface OnSelectElement{
        public void OnElementIterate(String Brand, String Price, String Year, String ID);
    }
}
