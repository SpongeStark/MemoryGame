package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HomePage extends AppCompatActivity {

    TextView txtViewHello;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_home_page);

        txtViewHello = findViewById(R.id.welcomeText);
        Intent intent = getIntent();
        String userName = intent.getStringExtra("username");
        txtViewHello.setText("Hello "+userName);
    }

    public void easy_mode(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("mode", "EASY");
        startActivity(intent);
    }
}