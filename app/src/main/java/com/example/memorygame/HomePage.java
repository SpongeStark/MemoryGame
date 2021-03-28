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
import android.widget.TextView;
import android.widget.Toast;

public class HomePage extends AppCompatActivity implements Runnable {

    TextView txtViewHello;
    TextView txtViewScore;
    String login;
    SQLiteHelper myHelper;
    Handler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_home_page);

        txtViewHello = findViewById(R.id.welcomeText);
        txtViewScore = findViewById(R.id.scoreText);
        myHelper = new SQLiteHelper(this);
        myHandler = new Handler();

        Intent intent = getIntent();
        login = intent.getStringExtra("login");
        String userName = myHelper.getUserName(login);
        txtViewHello.setText("Hello "+userName);
        int score = myHelper.getScore(login);
        txtViewScore.setText("Score : "+ (score/2.0) );

        myHandler.post(this::run);
    }

    public void epic_mode(View view) {
//        myHelper.initScore(login);
//        int score = myHelper.getScore(login);
//        txtViewScore.setText("Score : "+ (score/2.0) );
        Toast.makeText(this, "Wait to develop", Toast.LENGTH_SHORT).show();
    }

    public void onClick(View view){
        String str_mode = getModeByButton((Button) view);
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("login", login);
        intent.putExtra("mode", str_mode);
        startActivity(intent);
    }

    private String getModeByButton(Button btn){
        switch (btn.getId()){
            case R.id.btn_mode_easy :
                return "EASY";
            case R.id.btn_mode_difficult :
                return "DIFFICULT";
            case R.id.btn_mode_expert :
                return "EXPERT";
            case R.id.btn_mode_epic :
                return "EPIC";
            default:
                return null;
        }
    }

    @Override
    public void run() {
        int score = myHelper.getScore(login);
        txtViewScore.setText("Score : "+ (score/2.0) );
        myHandler.postDelayed(this::run, 1000);
    }
}