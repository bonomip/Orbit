package com.example.paolobonomi.eattheball.Activity.Dialog;

/*
 * Created by Paolo Bonomi on 03/08/2017.
 */

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.paolobonomi.eattheball.Activity.DB.DBAdapter;
import com.example.paolobonomi.eattheball.Activity.DB.ShopAdapter;
import com.example.paolobonomi.eattheball.Activity.Engine.GameContract;
import com.example.paolobonomi.eattheball.R;

public class BuySkinDialog extends Dialog {

    Activity parent;
    ShopAdapter adapter;
    int skin_id;

    public BuySkinDialog(Activity activity, int skin_id, ShopAdapter adapter){
        super(activity);
        this.skin_id = skin_id;
        this.parent = activity;
        this.adapter = adapter;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.buy_skin_dialog);
        setCancelable(true);

        final DBAdapter db = DBAdapter.getInstance(parent.getApplicationContext());

        final TextView dialog_title = (TextView) findViewById(R.id.bs_dialog_title);
        dialog_title.setTextSize(GameContract.FONT_DIALOG);
        dialog_title.setTypeface(GameContract.getTypeface(getContext()), Typeface.BOLD);
        dialog_title.setTextColor(GameContract.COLOR_WHITE);

        final TextView dialog_message = (TextView) findViewById(R.id.bs_dialog_message);
        dialog_message.setTextSize(GameContract.FONT_DIALOG_TEXT);
        dialog_message.setTypeface(GameContract.getTypeface(getContext()), Typeface.BOLD);
        dialog_message.setTextColor(GameContract.COLOR_WHITE);

        final Button button_yes = (Button) findViewById(R.id.bs_dialog_button_yes);
        button_yes.setTextSize(GameContract.FONT_DIALOG);
        button_yes.setTypeface(GameContract.getTypeface(getContext()), Typeface.BOLD);
        button_yes.setTextColor(GameContract.COLOR_WHITE);
        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.open().buyShopItem(skin_id);
                db.close();
                adapter.refreshList();
                adapter.notifyDataSetChanged();
                adapter.refreshTextView();
                dismiss();
            }
        });

        final Button button_no = (Button) findViewById(R.id.bs_dialog_button_no);
        button_no.setTextSize(GameContract.FONT_DIALOG);
        button_no.setTypeface(GameContract.getTypeface(getContext()), Typeface.BOLD);
        button_no.setTextColor(GameContract.COLOR_WHITE);
        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}
