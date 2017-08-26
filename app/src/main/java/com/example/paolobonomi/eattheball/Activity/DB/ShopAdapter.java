package com.example.paolobonomi.eattheball.Activity.DB;

/*
 * Created by Paolo Bonomi on 01/08/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paolobonomi.eattheball.Activity.Activity.ShopActivity;
import com.example.paolobonomi.eattheball.Activity.Dialog.BuySkinDialog;
import com.example.paolobonomi.eattheball.Activity.Engine.GameContract;
import com.example.paolobonomi.eattheball.R;

import java.util.ArrayList;

public class ShopAdapter extends ArrayAdapter<ShopItem> {

    private LayoutInflater inflater;
    private ArrayList<ShopItem> items;
    private Context context;
    private DBAdapter db;

    public ShopAdapter(Context context, ArrayList<ShopItem> items){
        super(context, R.layout.shop_list_item, items);
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.items = items;
        this.db = DBAdapter.getInstance(context);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){

        if(convertView == null)
            convertView = inflater.inflate(R.layout.shop_list_item, parent, false );

        final ShopItem item = super.getItem(pos);

        ///cambiare immagini prova 0......12

        //SET IMAGE
        final ImageView image = (ImageView) convertView.findViewById(R.id.shop_image);
        image.setImageResource(GameContract.SHOP_IMAGES[item.getId()]);

        //SET BUTTON BUY
        final Button button_buy = (Button) convertView.findViewById(R.id.shop_buy);
        button_buy.setTypeface(GameContract.getTypeface(convertView.getContext()));
        button_buy.setTextSize(GameContract.FONT_SH_BUTTON);
        button_buy.setOnClickListener(null);

        if(item.isBought()){
            //item is bought
            button_buy.setTextColor(Color.parseColor("#777777"));
            button_buy.setText(R.string.button_buy_sold);
        } else {
            //item is not bought
            button_buy.setTextColor(GameContract.HG_COLOR[2]);
            button_buy.setText(String.valueOf(item.getCost()));
            button_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( item.getCost() <= PreferenceManager.getDefaultSharedPreferences(v.getContext()).getInt(GameContract.GAME_BALANCE, 0) )
                        new BuySkinDialog((Activity) v.getContext(), item.getId(), ShopAdapter.this).show();
                }
            });
        }


        //SET BUTTON EQUIP
        final Button button_equip = (Button) convertView.findViewById(R.id.shop_equip);
        button_equip.setTypeface(GameContract.getTypeface(convertView.getContext()));
        button_equip.setTextSize(GameContract.FONT_SH_BUTTON);
        button_equip.setTextColor(Color.parseColor("#777777"));
        button_equip.setOnClickListener(null);

        if(item.isBought() && item.isEquipped())
            button_equip.setTextColor(GameContract.HG_COLOR[3]);

        if(item.isBought() && !item.isEquipped())
            button_equip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    equipSkin(item.getId());
                    notifyDataSetChanged();
                }
            });

        return convertView;
    }

    private void equipSkin(int id){
        db.open().equipShopItem(id);
        db.close();
        refreshList();
    }

    public void refreshList(){
        items.clear();
        Cursor c = db.open().getAllItemShop();
        db.close();
        for(int i = 0; i < c.getCount(); i++){
            ShopItem item = new ShopItem(
                    c.getInt(c.getColumnIndex(DBContract.ShopList.COLUMN_NAME_SKINID)),
                    c.getString(c.getColumnIndex(DBContract.ShopList.COLUMN_NAME_NAME)),
                    c.getInt(c.getColumnIndex(DBContract.ShopList.COLUMN_NAME_COST)),
                    c.getInt(c.getColumnIndex(DBContract.ShopList.COLUMN_NAME_BUY)),
                    c.getInt(c.getColumnIndex(DBContract.ShopList.COLUMN_NAME_EQUIP)),
                    context
            );
            //add item to list
            items.add(0, item);
            c.moveToNext();
        }
    }

    public void refreshTextView(){
        final TextView text_wallet = (TextView) ((Activity) context).getWindow().getDecorView().findViewById(R.id.text_totScores);
        int wallet = PreferenceManager.getDefaultSharedPreferences(context).getInt(GameContract.GAME_BALANCE, 0);
        text_wallet.setText(context.getText(R.string.tot_scores)+":"+wallet);
    }
}
