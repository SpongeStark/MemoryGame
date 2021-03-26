package com.example.memorygame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements Handler.Callback, Runnable{

    ImageButton[] buttons;
    Button btn;
    TextView text;
    int[] answer;
    int[] lib;
    private Handler myHandler;
    private Thread myThread;
    String mode;
    int currentNumBlocs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");

        myHandler = new Handler(this);
        currentNumBlocs = minNumBloc();

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
        currentNumBlocs = minNumBloc();
        myThread = new Thread(this);
        myThread.start();
    }

    public void goOn(View view){
        if(maxNumBloc() > currentNumBlocs){
            currentNumBlocs++;
            myThread = new Thread(this);
            myThread.start();
        }
    }

    @Override
    public void run() {
        // change the button to "restart"
        myHandler.sendMessage(getMessageOfIndex(7));
        btn.setOnClickListener(this::start);

        // start the game
        setRandomArray(currentNumBlocs);
        // ready
        myHandler.sendMessage(getMessageOfIndex(1));
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        // start
        myHandler.sendMessage(getMessageOfIndex(2));
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}
        // 全部闪一遍 | the buttons blink
        for(int i=1; i<answer.length; i++){
            myHandler.sendMessage(getMessageOfIndex(3,i));
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            myHandler.sendMessage(getMessageOfIndex(4,i));
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        }
        // set click listener of 4 buttons
        for (ImageButton button : buttons) {
            button.setOnClickListener(this::onClick);
        }
        // show "Your turn"
        myHandler.sendMessage(getMessageOfIndex(5));
    }

    private Message getMessageOfIndex(int index){
        Message msg = new Message();
        msg.what = index;
        return msg;
    }

    private Message getMessageOfIndex(int index, int arg1){
        Message msg = getMessageOfIndex(index);
        msg.arg1 = arg1;
        return msg;
    }

    /**
     * 所有改变UI的操作
     * All the actions of UI changes
     */
    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case 1:
                text.setText("ready");
                break;
            case 2:
                text.setText("Start");
                break;
            case 3:
                toBlink(findViewById(answer[msg.arg1]));
                break;
            case 4:
                toNormal(findViewById(answer[msg.arg1]));
                break;
            case 5:
                text.setText("Your turn");
                break;
            case 6:
                text.setText("Correct\nClick to continue");
                btn.setText("Continue");
                break;
            case 7:
                text.setText("Fail");
                btn.setText("Restart");
                break;
        }
        return true;
    }

    /**
     * 那个普通按钮的事件函数
     * The action of the normal button
     * @param view
     */
    public void onClick(View view){
        int btnID = view.getId();
        if(btnID == answer[answer[0]]){
            if(answer[0] == answer.length-1){
                successAction();
                removeAllAction();
            }else{
                answer[0]++;
            }
        }else{
            failAction();
            removeAllAction();
        }
    }

    /**
     * 移除4个ImageButton的事件函数
     * remove all the action listener of the 4 buttons
     */
    private void removeAllAction(){
        for (ImageButton button : buttons) {
            button.setOnClickListener(null);
        }
    }

    private void successAction(){
        myHandler.sendMessage(getMessageOfIndex(6));
        btn.setOnClickListener(this::goOn);
    }

    private void failAction(){
        myHandler.sendMessage(getMessageOfIndex(7));
        btn.setOnClickListener(this::start);
    }

    /**
     * 初始化随机数，给answer数组设置随机的四个button的id
     *
     * @param length 随机数组长度
     */
    private void setRandomArray(int length){
        answer = new int[++length];
        answer[0] = 1;
        Random r = new Random();
        for (int i = 1; i < length; i++){
            int index = r.nextInt(4);
            answer[i] = lib[index];
        }
    }

    private void toBlink(ImageButton btn){
        switch (btn.getId()){
            case R.id.btn1 :
                btn.setBackground(getDrawable(R.drawable.btn_red));
                break;
            case R.id.btn2 :
                btn.setBackground(getDrawable(R.drawable.btn_green));
                break;
            case R.id.btn3 :
                btn.setBackground(getDrawable(R.drawable.btn_yellow));
                break;
            case R.id.btn4 :
                btn.setBackground(getDrawable(R.drawable.btn_blue));
                break;
        }
    }

    private void toNormal(ImageButton btn) {
        switch (btn.getId()) {
            case R.id.btn1:
                btn.setBackground(getDrawable(R.drawable.btn_bg_red));
                break;
            case R.id.btn2:
                btn.setBackground(getDrawable(R.drawable.btn_bg_green));
                break;
            case R.id.btn3:
                btn.setBackground(getDrawable(R.drawable.btn_bg_yellow));
                break;
            case R.id.btn4:
                btn.setBackground(getDrawable(R.drawable.btn_bg_blue));
                break;
        }
    }

    private int minNumBloc(){
        switch (mode){
            case "EASY":
                return 1;
            case "DIFFICULT":
                return 3;
            case "EXPERT":
                return 5;
            default:
                return 0;
        }
    }

    private int maxNumBloc(){
        switch (mode){
            case "EASY":
                return 10;
            case "DIFFICULT":
                return 15;
            case "EXPERT":
                return 20;
            default:
                return 0;
        }
    }


}