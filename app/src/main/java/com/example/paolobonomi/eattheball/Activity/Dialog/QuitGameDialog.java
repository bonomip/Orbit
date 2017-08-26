package com.example.paolobonomi.eattheball.Activity.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.paolobonomi.eattheball.Activity.Engine.GameContract;
import com.example.paolobonomi.eattheball.Activity.Engine.GameState;
import com.example.paolobonomi.eattheball.Activity.Activity.MainMenuActivity;
import com.example.paolobonomi.eattheball.R;

import static com.example.paolobonomi.eattheball.Activity.Engine.GameState.STOPPED;

/*
 * Created by Paolo Bonomi on 26/04/2017.
 */

public class QuitGameDialog extends Dialog {

    public QuitGameDialog(Activity activity){
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.quit_game_dialog);
        setCancelable(false);

        final TextView dialog_title = (TextView) findViewById(R.id.qg_dialog_title);
        dialog_title.setTextSize(GameContract.FONT_DIALOG);
        dialog_title.setTypeface(GameContract.getTypeface(getContext()), Typeface.BOLD);
        dialog_title.setTextColor(GameContract.COLOR_WHITE);

        final TextView dialog_message = (TextView) findViewById(R.id.qg_dialog_message);
        dialog_message.setTextSize(GameContract.FONT_DIALOG_TEXT);
        dialog_message.setTypeface(GameContract.getTypeface(getContext()), Typeface.BOLD);
        dialog_message.setTextColor(GameContract.COLOR_WHITE);

        final Button button_yes = (Button) findViewById(R.id.qg_dialog_button_yes);
        button_yes.setTextSize(GameContract.FONT_DIALOG);
        button_yes.setTypeface(GameContract.getTypeface(getContext()), Typeface.BOLD);
        button_yes.setTextColor(GameContract.COLOR_WHITE);
        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameState.set(STOPPED);
                v.getContext().startActivity(new Intent(v.getContext(), MainMenuActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) );
                dismiss();
            }
        });

        final Button button_no = (Button) findViewById(R.id.qg_dialog_button_no);
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
