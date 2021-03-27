package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText email;
    EditText password;
    EditText password1;
    EditText birthday;
    RadioButton sexF;
    RadioButton sexM;

    SQLiteHelper myHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.register_name);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        password1 = findViewById(R.id.register_password1);
        birthday = findViewById(R.id.register_birthday);
        sexM = findViewById(R.id.register_sexM);
        sexF = findViewById(R.id.register_sexF);

        myHelper = new SQLiteHelper(this);

    }

    public void btn_register_click(View view) {
        String str_username = username.getText().toString();
        String str_email = email.getText().toString();
        String str_password = password.getText().toString();
        String str_password1 = password1.getText().toString();
        String str_birthday = birthday.getText().toString();
        boolean isMale = sexM.isChecked();

        boolean readyToAdd = !str_username.isEmpty()
                && !str_email.isEmpty()
                && !str_password.isEmpty()
                && str_password.equals(str_password1)
                && !str_birthday.isEmpty();

        if(readyToAdd &&
            myHelper.addGamer(str_username, str_email, str_password, str_birthday, isMale, 0)){
            Toast.makeText(this,"Success",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, HomePage.class);
            intent.putExtra("login", str_email);
            intent.putExtra("username", str_username);
            startActivity(intent);
        } else {
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
        }
    }

    public void btn_register_cancel(View view){
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        onBackPressed();
    }
}