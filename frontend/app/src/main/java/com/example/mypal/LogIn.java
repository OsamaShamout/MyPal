package com.example.mypal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogIn extends AppCompatActivity {

    EditText input_email;
    EditText input_password;

    String email_string;
    String password_string;

    Button log_in;

    SharedPreferences sharedpref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        input_email = (EditText) findViewById(R.id.editTextTextEmailAddressLogIn);
        input_password = (EditText)  findViewById(R.id.editTextPasswordLogIn);
        log_in = (Button) findViewById(R.id.loginButton);

    }

    public void OnClickReturnToWelcome(View view){
        Intent intent = new Intent(this,Welcome.class);
        startActivity(intent);
    }

    public void OnClickGoToRegistration(View view){
        Intent intent = new Intent(this,Registration.class);
        startActivity(intent);
    }

    boolean logged_in = false;
    public void OnClickLogIn(View view){
        email_string = input_email.getText().toString();
        password_string = input_password.getText().toString();

        

        //Validate Information from DB

        logged_in = true;
        //If true save e-mail to sharefpref.

        if(logged_in){
            Intent intent = new Intent(this,Homepage.class);
            startActivity(intent);
        }

    }
}