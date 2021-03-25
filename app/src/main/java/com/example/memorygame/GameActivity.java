package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    ImageButton[] buttons;
    Button btn;
    TextView text;
    int[] answer;
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
        loop();
    }

    public void onClick(View view){
        int btnID = view.getId();
        if(btnID == answer[answer[0]]){
            if(answer[0] == answer.length-1){
                text.setText("Win");
                removeAllAction();
            }else{
                answer[0]++;
            }
        }else{
            text.setText("Fail");
            removeAllAction();
        }
    }

    private void loop(){
        setRandomArray(5);
        blink(1);
//        play();

    }

    private void removeAllAction(){
        for (ImageButton button : buttons) {
            button.setOnClickListener(null);
        }
    }

    private void removeButtonAction(){
        btn.setOnClickListener(null);
    }

    /**
     * 初始化随机数，给answer数组设置随机的四个button的id
     *
     * @param length 随机数组长度
     */
    private void setRandomArray(int length){
        answer = new int[length];
        answer[0] = 1;
        Random r = new Random();
        for (int i=1; i<length; i++){
            int index = r.nextInt(4);
            answer[i] = lib[index];
        }
    }

    private void blink(int startIndex){
        if(startIndex < answer.length){
            blinkOnce((ImageButton)findViewById(answer[startIndex]));
            new Handler().postDelayed(() -> {
                blink(startIndex+1);
            }, 700);
        }
//        for(int i=1; i<answer.length; i++){
//            blinkOnce((ImageButton)findViewById(answer[i]));
//        }
    }

    private void blinkOnce(ImageButton btn){
        switch (btn.getId()){
            case R.id.btn1 :
//                btn.setBackground(getDrawable(R.drawable.btn_red));
                blinkForOneButton(btn, 500, R.drawable.btn_red, R.drawable.btn_bg_red);
                break;
            case R.id.btn2 :
//                btn.setBackground(getDrawable(R.drawable.btn_green));
                blinkForOneButton(btn, 500, R.drawable.btn_green, R.drawable.btn_bg_green);
                break;
            case R.id.btn3 :
//                btn.setBackground(getDrawable(R.drawable.btn_yellow));
                blinkForOneButton(btn, 500, R.drawable.btn_yellow, R.drawable.btn_bg_yellow);
                break;
            case R.id.btn4 :
//                btn.setBackground(getDrawable(R.drawable.btn_blue));
                blinkForOneButton(btn, 500, R.drawable.btn_blue, R.drawable.btn_bg_blue);
                break;
        }
    }

    private void blinkForOneButton(ImageButton btn, int delay, int id_blinkBG, int id_normalBG){
        btn.setBackground(getDrawable(id_blinkBG));
        new Handler().postDelayed(() -> {
            btn.setBackground(getDrawable(id_normalBG));
        }, delay);
    }

    private void play(){
//        answer = new int[]{1,lib[0],lib[1],lib[2],lib[1],lib[3]};
        text.setText("ready");
        new Handler().postDelayed(() -> {
            // TO DO
            for (ImageButton button : buttons) {
                button.setOnClickListener(this::onClick);
            }
            text.setText("start");
            removeButtonAction();
        }, 500);
    }
}