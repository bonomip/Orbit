package com.example.paolobonomi.eattheball.Activity.DB;

import android.provider.BaseColumns;

/*
 * Created by Paolo Bonomi on 15/04/2017.
 */

public class DBContract {

    static final int DB_VERSION = 1;
    static final String DB_NAME = "game.db";

    private DBContract(){}

    public static abstract class Scores implements BaseColumns {
        static final String TABLE_NAME = "scores";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SCORE = "score";
    }


    public static abstract class ShopList implements BaseColumns {
        static final String TABLE_NAME = "shoplist";
        public static final String COLUMN_NAME_SKINID = "ID";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_COST = "Cost";
        public static final String COLUMN_NAME_EQUIP = "isEquipped";
        public static final String COLUMN_NAME_BUY = "isBought";

    }


}
