package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    ImageButton[] buttons;
    Button btn;
    TextView text;
    int[] key;
    int[] lib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_game);

        buttons = new ImageButton[4];
        lib = new int[]{R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4};
        buttons[0] = findViewById(lib[0]);
        buttons[1] = findViewById(lib[1]);
        buttons[2] = findViewById(lib[2]);
        buttons[3] = findViewById(lib[3]);

        text = findViewById(R.id.txtInfo);
        text.setText("Click to Start");
        btn = findViewById(R.id.btn_start);
        btn.setOnClickListener(this::start);
    }

    public void start(View view){
        key = new int[]{1,lib[0],lib[1],lib[2],lib[1],lib[3]};
        text.setText("ready");
        new Handler().postDelayed(() -> {
            // TO DO
            for (ImageButton button : buttons) {
                button.setOnClickListener(this::onClick);
            }
            text.setText("start");
            removeButtonAction();
        }, 1000);
    }

    public void onClick(View view){
        int btnID = view.getId();
        if(btnID == key[key[0]]){
            if(key[0] == key.length-1){
                text.setText("Win");
                removeAllAction();
            }else{
                key[0]++;
            }
        }else{
            text.setText("Fail");
            removeAllAction();
        }
    }

    private void removeAllAction(){
        for (ImageButton button : buttons) {
            button.setOnClickListener(null);
        }
    }

    private void mode_easy(){

    }

    private void removeButtonAction(){
        btn.setOnClickListener(null);
    }

}