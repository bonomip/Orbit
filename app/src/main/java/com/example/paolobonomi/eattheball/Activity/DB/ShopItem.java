package com.example.paolobonomi.eattheball.Activity.DB;

/*
 * Created by Paolo Bonomi on 01/08/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;

public class ShopItem {

    private Context context;
    private int id;
    private String name;
    private int cost;
    private boolean isEquipped;
    private boolean isBought;

    public ShopItem(int id, String name, int cost, int isBought, int isEquipped, Context context){
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.isBought = isBought == 1;
        this.isEquipped = isEquipped == 1;
        this.context = context;
    }

    public boolean isEquipped(){
        return isEquipped;
    }

    public boolean isBought(){
        return isBought;
    }

    public String getName(){
        return name;
    }

    public int getCost(){
        return cost;
    }

    public int getId(){
        return id;
    }

    public ContentValues getAsContentValue(){
        ContentValues cv = new ContentValues();
        cv.put(DBContract.ShopList.COLUMN_NAME_SKINID, this.id);
        cv.put(DBContract.ShopList.COLUMN_NAME_NAME, this.name);
        cv.put(DBContract.ShopList.COLUMN_NAME_COST, this.cost);
        cv.put(DBContract.ShopList.COLUMN_NAME_BUY, this.isBought ? 1 : 0);
        cv.put(DBContract.ShopList.COLUMN_NAME_EQUIP, this.isEquipped ? 1 : 0);

        return cv;
    }

    public String toString(){
        return "\nId="+this.id+
                "\nName="+this.name+
                "\nCost="+this.cost+
                "\nBuy="+this.isBought+
                "\nEquip="+this.isEquipped;
    }
}
