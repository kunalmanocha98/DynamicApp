package com.vijayjaidewan01vivekrai.dynamic_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vijayjaidewan01vivekrai.dynamic_app.Models.Data;
import com.vijayjaidewan01vivekrai.dynamic_app.Models.Menu_header;
import com.vijayjaidewan01vivekrai.dynamic_app.Models.Menu_items;
import com.vijayjaidewan01vivekrai.dynamic_app.Models.Results;
import com.vijayjaidewan01vivekrai.dynamic_app.Models.TableRecord;
import com.vijayjaidewan01vivekrai.dynamic_app.Models.ToolBar;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";

    //TABLE DETAILS FOR DATA
    private static final String TABLE_DATA = "dataInfo";
    private static final String KEY_ID = "id";
    private static final String KEY_URL = "url";
    private static final String KEY_DATA = "data";
    private static final String KEY_CREATE = "created_on";
    private static final String KEY_UPDATE = "updated_on";


    private static final int DBversion = 1;

    //Parameter to store the Title of Navigation Drawer

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, DBversion);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //CREATE TABLE FOR DATA INFO
        db.execSQL("CREATE TABLE " + TABLE_DATA
                                + "("
                                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                + KEY_URL + " TEXT,"
                                + KEY_DATA + " TEXT,"
                                + KEY_CREATE + " TEXT,"
                                + KEY_UPDATE + " TEXT"
                                + ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
        onCreate(db);
    }

    public void addRecord(TableRecord record)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor cursor = db.query(TABLE_DATA,new String[]{KEY_ID,KEY_URL,KEY_DATA,KEY_CREATE,KEY_UPDATE}
                ,KEY_URL + "=?"
                ,new String[]{record.getUrl()}
                ,null,null,null);

        values.put(KEY_URL, record.getUrl());
        values.put(KEY_DATA, record.getData());
        values.put(KEY_UPDATE, record.getDateTime());

        if(cursor.moveToFirst())
        {
            values.put(KEY_CREATE, cursor.getString(cursor.getColumnIndex(KEY_CREATE)));

            int rows = db.update(TABLE_DATA,values,KEY_URL + " =?",new String[]{record.getUrl()});
            Log.d("Rows Updated","" + rows);
        }

        else {
            values.put(KEY_CREATE, record.getDateTime());

            long rowID = db.insert(TABLE_DATA, null, values);
            Log.i("Data : Row ID ", "" + rowID);
        }
        cursor.close();
        db.close();
    }

    public void getRecord(TableRecord record)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_DATA,new String[]{KEY_ID,KEY_URL,KEY_DATA,KEY_CREATE,KEY_UPDATE}
                                            ,KEY_URL + "=?"
                                            ,new String[]{record.getUrl()}
                                            ,null,null,null);

        if(cursor.moveToFirst())
        {
            Log.d("Fetched Data",cursor.getString(cursor.getColumnIndex(KEY_DATA)));
            record.setData(cursor.getString(cursor.getColumnIndex(KEY_DATA)));
        }
        cursor.close();
        db.close();
    }

}
