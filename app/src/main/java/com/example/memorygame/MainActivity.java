package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText password;

    SQLiteHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.main_email);
        password = findViewById(R.id.main_password);

        myHelper = new SQLiteHelper(this);
    }

    public void btn_register_click(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        
    }

    public void btn_valid_onClick(View view){
        String login = email.getText().toString();
        String pwd = password.getText().toString();

        if(myHelper.connect(login, pwd)){
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        }else{
            Toast.makeText(this,"Login or password is incorrect",Toast.LENGTH_LONG).show();
        }

    }
}