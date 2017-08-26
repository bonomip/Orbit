package com.example.paolobonomi.eattheball.Activity.Activity;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.example.paolobonomi.eattheball.Activity.DB.DBAdapter;
import com.example.paolobonomi.eattheball.Activity.DB.DBContract;
import com.example.paolobonomi.eattheball.Activity.DB.ShopAdapter;
import com.example.paolobonomi.eattheball.Activity.DB.ShopItem;
import com.example.paolobonomi.eattheball.Activity.Engine.GameContract;
import com.example.paolobonomi.eattheball.R;

import java.util.ArrayList;

/*
 * Created by Paolo Bonomi on 14/04/2017.
 */

public class ShopActivity extends AppCompatActivity {

    private TextView textTotScore;
    private ListView listShop;
    private ArrayList<ShopItem> items;
    private ShopAdapter adapter;
    private int totScores;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shop);

        if(GameContract.REVERSE_ORIENTATION) setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

        totScores = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()).getInt(GameContract.GAME_BALANCE, 0);
        textTotScore = (TextView) findViewById(R.id.text_totScores);
        textTotScore.setTypeface(GameContract.getTypeface(getApplicationContext()), Typeface.BOLD);
        textTotScore.setTextSize(GameContract.FONT_SH_TEXT);
        textTotScore.setText(getResources().getText(R.string.tot_scores)+":"+totScores);

        //instantiate list
        items = new ArrayList<ShopItem>();

        //get the adapter
        adapter = new ShopAdapter(this, items);

        //instantiate listview
        listShop = (ListView) findViewById(R.id.shop_list);

        //set adapter
        listShop.setAdapter(adapter);

    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        adapter.refreshList();
    }

}
