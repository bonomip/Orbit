package com.example.paolobonomi.eattheball.Activity.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.paolobonomi.eattheball.Activity.Engine.GameContract;
import com.example.paolobonomi.eattheball.Activity.Engine.GameStats;
import com.example.paolobonomi.eattheball.R;

/*
 * Created by Paolo Bonomi on 15/04/2017.
 */

public class DBAdapter {

    private static final String TAG = "DBAdapter";
    private static DBAdapter sInstance;
    private Context context;

    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private Cursor c;

    public static synchronized  DBAdapter getInstance(Context context){
        if (sInstance == null)
            sInstance = new DBAdapter(context.getApplicationContext());

        return sInstance;
    }

    private DBAdapter(Context context){
        this.context = context;
        this.dbHelper = DBHelper.getsInstance(context);
    }

    public DBAdapter open() throws SQLException{
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            throw e;
        }
        return this;
    }

    public void close(){
        db.close();
    }

    // TABLE SCORE

    private static final String SQL_SELECT_TOP_TEN_SCORES = "SELECT * FROM "+DBContract.Scores.TABLE_NAME
            +" ORDER BY "+DBContract.Scores.COLUMN_NAME_SCORE +" DESC";

    public long insertScore(Score score){
        Log.w(TAG, "Adding Score to DB="+score.toString());
        return db.insert(DBContract.Scores.TABLE_NAME, null, score.getAsContentValue());
    }

    private void cleanScoreTable(){
        c = db.rawQuery(SQL_SELECT_TOP_TEN_SCORES, null);
        c.moveToFirst();
        int n = c.getCount();
        if(n > 10) {
            c.move(10);
            for (int i = 10; i < c.getCount(); i++){
                long idx = c.getLong(c.getColumnIndex(DBContract.Scores._ID));
                db.delete(DBContract.Scores.TABLE_NAME,DBContract.Scores._ID+"="+idx, null);
                c.moveToNext();
            }
        }
    }

    public Cursor getTopTenScores(){
        cleanScoreTable();
        c = db.rawQuery(SQL_SELECT_TOP_TEN_SCORES, null);
        c.moveToFirst();
        return c;
    }

    public boolean isBestTenScore(long score){
        try {
            c = getTopTenScores();
            if(c.getCount() < 10)
                return true;
            for (int i = 0; i < c.getCount(); i++) {
                if (score > c.getInt(c.getColumnIndex(DBContract.Scores.COLUMN_NAME_SCORE)))
                    return true;
                c.moveToNext();
            }
            return false;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            throw e;
        }
    }

    public int getNumberOfScore(){
        c = db.rawQuery(SQL_SELECT_TOP_TEN_SCORES, null);
        return c.getCount();
    }

    public void populateHighScore(){
        try{
            insertScore(new Score("CHINAGUY", 2500));
            insertScore(new Score("YOURMOM", 2000));
            insertScore(new Score("", 1500));
            insertScore(new Score("DOGGO", 1250));
            insertScore(new Score("", 1000));
            insertScore(new Score("BOUSIE", 900));
            insertScore(new Score("NAKEDGUY", 750));
            insertScore(new Score("", 550));
            insertScore(new Score("", 400));
            insertScore(new Score("SEEYOU", 350));
        } catch ( Exception e ){ e.printStackTrace(); }
    }

    //TABLE SHOP LIST

    private static final String SQL_SELECT_ALL_SHOP_ITEM = "SELECT * FROM "+DBContract.ShopList.TABLE_NAME+
            " ORDER BY "+DBContract.ShopList.COLUMN_NAME_SKINID+" DESC";

    private static final String SQL_BUY_SKIN_ID = "UPDATE "+ DBContract.ShopList.TABLE_NAME + " SET "+DBContract.ShopList.COLUMN_NAME_BUY
            +"=1 WHERE "+DBContract.ShopList.COLUMN_NAME_SKINID+"=";

    private static final String SQL_GET_ITEM_SHOP_COST = "SELECT "+DBContract.ShopList.COLUMN_NAME_COST
            +" FROM "+DBContract.ShopList.TABLE_NAME+" WHERE "+DBContract.ShopList.COLUMN_NAME_SKINID+"=";

    private static final String SQL_UNEQUIP_ITEM = "UPDATE "+DBContract.ShopList.TABLE_NAME+" SET "
            +DBContract.ShopList.COLUMN_NAME_EQUIP+"=0 WHERE "+DBContract.ShopList.COLUMN_NAME_EQUIP+"=1";

    private static final String SQL_EQUIP_ITEM_ID = "UPDATE "+DBContract.ShopList.TABLE_NAME+" SET "
            +DBContract.ShopList.COLUMN_NAME_EQUIP+"=1 WHERE "+DBContract.ShopList.COLUMN_NAME_SKINID+"=";

    private static final String SQL_SELECT_EQUIPPED_ITEM = "SELECT * FROM "+DBContract.ShopList.TABLE_NAME
            +" WHERE "+DBContract.ShopList.COLUMN_NAME_EQUIP+"=1";

    private long insertItem(ShopItem item){
        Log.d(TAG, "Adding item to DB="+item.toString());
        return db.insert(DBContract.ShopList.TABLE_NAME, null, item.getAsContentValue());
    }

    public boolean buyShopItem(int id){
        try {
            db.execSQL(SQL_BUY_SKIN_ID + String.valueOf(id));
            c = db.rawQuery(SQL_GET_ITEM_SHOP_COST + String.valueOf(id), null);
            c.moveToFirst();
            final int payment = c.getInt(c.getColumnIndex(DBContract.ShopList.COLUMN_NAME_COST));
            PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(
                    GameContract.GAME_BALANCE,
                    PreferenceManager.getDefaultSharedPreferences(
                            context).getInt(
                            GameContract.GAME_BALANCE,
                            0)
                            - payment)
                    .apply();
            return true;
        } catch (Exception e ) { e.printStackTrace(); return false; }
    }

    void equipShopItem(int id){
        db.execSQL(SQL_UNEQUIP_ITEM);
        db.execSQL(SQL_EQUIP_ITEM_ID+id);
        updatePlayerSkin();
    }

    public void updatePlayerSkin(){
        c = db.rawQuery(SQL_SELECT_EQUIPPED_ITEM, null);
        c.moveToFirst();
        GameContract.PLAYER_SKIN_ID = c.getInt(c.getColumnIndex(DBContract.ShopList.COLUMN_NAME_SKINID));
    }

    public int getNumberOfShopItem(){
        c = db.rawQuery(SQL_SELECT_ALL_SHOP_ITEM, null);
        return c.getCount();
    }

    Cursor getAllItemShop(){
        c = db.rawQuery(SQL_SELECT_ALL_SHOP_ITEM, null);
        c.moveToFirst();
        return c;
    }

    public void populateShopList(){
        try{

            //ID, NAME, COST, BUY, EQUIP
            insertItem(new ShopItem(0, "default", 0, 1, 1, this.context));
            insertItem(new ShopItem(1, "viola", 100, 0, 0, this.context));
            insertItem(new ShopItem(2, "blu", 100, 0, 0, this.context));
            insertItem(new ShopItem(3, "verde", 100, 0, 0, this.context));
            insertItem(new ShopItem(4, "gialla", 100, 0, 0, this.context));
            insertItem(new ShopItem(5, "viola scuro", 100, 0, 0, this.context));
            insertItem(new ShopItem(6, "nera", 100, 0, 0, this.context));
            insertItem(new ShopItem(7, "fire", 250, 0, 0, this.context));
            insertItem(new ShopItem(8, "hippy", 250, 0, 0, this.context));
            insertItem(new ShopItem(9, "gatto", 500, 0, 0, this.context));
            insertItem(new ShopItem(10, "terra", 500, 0, 0, this.context));
            insertItem(new ShopItem(11, "psico", 1000, 0, 0, this.context));
            insertItem(new ShopItem(12, "invisibile", 1500, 0, 0, this.context));
        } catch ( Exception e ) { e.printStackTrace(); }
    }

}
