package com.example.mypal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


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

        //Validate Information from DB

        logged_in = true;
        //If true log in
        if(logged_in){
            Intent intent = new Intent(this,Homepage.class);
            startActivity(intent);
        }

    }
}