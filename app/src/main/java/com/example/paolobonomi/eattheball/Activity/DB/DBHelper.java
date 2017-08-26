package com.example.paolobonomi.eattheball.Activity.DB;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
 * Created by Paolo Bonomi on 15/04/2017.
 */

class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";
    private static DBHelper sInstance;

    static synchronized DBHelper getsInstance(Context context){
        if(sInstance == null)
            sInstance = new DBHelper(context.getApplicationContext());

        return sInstance;
    }

    private DBHelper(Context context){
        super(context, DBContract.DB_NAME, null, DBContract.DB_VERSION);
    }

    private static final String SQL_CREATE_TABLE_SCORES =
            "create table " //
            + DBContract.Scores.TABLE_NAME + " (" //
            + DBContract.Scores._ID + " integer primary key autoincrement, "
            + DBContract.Scores.COLUMN_NAME_NAME + " text not null, "
            + DBContract.Scores.COLUMN_NAME_SCORE + " integer not null"
            + ")";

    private static final String SQL_CREATE_TABLE_SHOPLIST =
            "create table " //
                    + DBContract.ShopList.TABLE_NAME + " (" //
                    + DBContract.ShopList.COLUMN_NAME_SKINID + " integer primary key, "
                    + DBContract.ShopList.COLUMN_NAME_NAME + " text not null, "
                    + DBContract.ShopList.COLUMN_NAME_COST + " integer not null, "
                    + DBContract.ShopList.COLUMN_NAME_BUY + " integer default 0, "
                    + DBContract.ShopList.COLUMN_NAME_EQUIP + " integer default 0"
                    + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, "Creating DB="+DBContract.DB_NAME);
        try {
            db.execSQL(SQL_CREATE_TABLE_SCORES);
            db.execSQL(SQL_CREATE_TABLE_SHOPLIST);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            throw e;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
