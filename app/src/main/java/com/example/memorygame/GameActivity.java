package com.example.memorygame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements Handler.Callback, Runnable{

    //region View
    ImageButton[] buttons; // 4 Image Buttons
    Button btn; // a normal button
    TextView text; // the center text view
    TextView txtLife; // to show the current life on total life
    TextView txtMode; // to show the current mode
    TextView txtLevel; // to show thw current level
    //endregion
    private Handler myHandler;
    private Thread myThread;
    SQLiteHelper myHelper;
    String mode;
    String login;
    int[] answer; // the right answer
    int[] lib; // the 4 ids of the 4 buttons
    int minNumBloc;
    int maxNumBloc;
    int currentNumBlocs;
    int currentLift;
    int totalLife;
    int weight_2x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // UI initializations
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_game);

        // initialise all the information of mode
        Intent intent = getIntent();
        login = intent.getStringExtra("login");
        mode = intent.getStringExtra("mode");

        // initialise the Handler
        myHandler = new Handler(this);
        myHelper = new SQLiteHelper(this);

        // initialise the all the ImageButtons
        buttons = new ImageButton[4];
        lib = new int[]{R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4};
        buttons[0] = findViewById(lib[0]);
        buttons[1] = findViewById(lib[1]);
        buttons[2] = findViewById(lib[2]);
        buttons[3] = findViewById(lib[3]);
        // initialise the TextView
        text = findViewById(R.id.txtInfo);
        txtLife = findViewById(R.id.txtLife);
        txtMode = findViewById(R.id.txtMode);
        txtLevel = findViewById(R.id.txtLevel);
        // initialise the normal button
        btn = findViewById(R.id.btn_start);

        text.setText("Click the button to Start");
        txtMode.setText(mode);
        modeInit();
        // set the action of the normal button
        btn.setOnClickListener(this::start);
    }

    /**
     * 根据模式，初始化相应变量
     * initialise all the variable according to the mode
     */
    private void modeInit(){
        switch (mode){
            case "EASY":
                minNumBloc = 1;
                maxNumBloc = 5; // just to test
                totalLife = 2;
                weight_2x = 2;
                break;
            case "DIFFICULT":
                minNumBloc = 3;
                maxNumBloc = 10;
                totalLife = 2;
                weight_2x = 3;
                break;
            case "EXPERT":
                minNumBloc = 5;
                maxNumBloc = 10;
                totalLife = 3;
                weight_2x = 6;
                break;
            default:
                minNumBloc = 1;
                maxNumBloc = 10;
                totalLife = 3;
                weight_2x = 4;
        }
        currentNumBlocs = minNumBloc;
        myHandler.sendMessage(getMessageOfIndex(10));
        currentLift = totalLife;
        myHandler.sendMessage(getMessageOfIndex(9));
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

    //region 有关handleMessage | all about handle message
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
                text.setText("Correct\nClick here to continue");
                break;
            case 7:
                text.setText("Fail\nGame Over");
                btn.setText("Restart");
                break;
            case 8:
                text.setText("Fail\nClick here to retry");
                break;
            case 9:
                txtLife.setText("Life: "+currentLift+" / "+totalLife);
                break;
            case 10:
                int level = currentNumBlocs - minNumBloc + 1;
                txtLevel.setText(""+level);
                break;
            case 11:
                text.setText("Finish\nYou win");
                break;
        }
        return true;
    }
    //endregion

    //region 有关事件函数 | all about the event functions
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
        if(currentNumBlocs < maxNumBloc){ // To continue
            myHandler.sendMessage(getMessageOfIndex(6));
            text.setOnClickListener(this::goOn);
        }else{ // finish and win
            int score_2x = weight_2x * (maxNumBloc - minNumBloc + 1);
            myHelper.setScore(login, score_2x);
            myHandler.sendMessage(getMessageOfIndex(11));
            getDialog(maxNumBloc - minNumBloc + 1).show();
        }
    }

    private void failAction(){
        currentLift--;
        if(currentLift<=0){ // fail without rest lift
            int score_2x = weight_2x * (currentNumBlocs - minNumBloc);
            myHelper.setScore(login, score_2x);
            myHandler.sendMessage(getMessageOfIndex(9));
            btn.setOnClickListener(this::start);
            getDialog(currentNumBlocs - minNumBloc).show();
        }else{ // fail but can retry
            currentNumBlocs--;
            myHandler.sendMessage(getMessageOfIndex(8));
            myHandler.sendMessage(getMessageOfIndex(9));
//            btn.setOnClickListener(this::goOn);
            text.setOnClickListener(this::goOn);
        }
    }

    public void start(View view){
        modeInit();
        myThread = new Thread(this);
        myThread.start();
    }

    public void goOn(View view){
        if(maxNumBloc > currentNumBlocs){
            currentNumBlocs++;
            myHandler.sendMessage(getMessageOfIndex(10));
            myThread = new Thread(this);
            myThread.start();
        }
        text.setOnClickListener(null);
    }
    //endregion

    //region 使Image Button闪烁 | to blink the Image Buttons
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
    //endregion

    /**
     * 初始化随机数，给answer数组设置随机的四个button的id
     * get some random number from 0 to 3 and initialise the array <i>answer[]</i>
     *
     * @param numBloc the number of Blocs
     */
    private void setRandomArray(int numBloc){
        int length = ++ numBloc;
        answer = new int[length];
        answer[0] = 1;
        Random r = new Random();
        for (int i = 1; i < length; i++){
            int index = r.nextInt(4);
            answer[i] = lib[index];
        }
    }

    /**
     * get a dialog with the based information
     *
     * @param level the current level
     * @return a dialog that can be showed
     */
    private AlertDialog getDialog(int level){
        double score = level * weight_2x / 2.0;
        AlertDialog dialogResult = new AlertDialog.Builder(this)
                .setTitle("Finish")
                .setMessage("You gained "+score+" points.")
                .setPositiveButton("Retry", (dialog, which)-> {
                    text.setText("Click the button to Start");
                    txtMode.setText(mode);
                    modeInit();
                    btn.setOnClickListener(this::start);
                })
                .setNeutralButton("return to HomePage", (dialog, which) -> {
                    Intent returnIntent = new Intent(this, HomePage.class);
                    returnIntent.putExtra("login", login);
                    startActivity(returnIntent);
                })
                .create();
        return dialogResult;
    }

}