package com.example.mypal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void OnClickReturnToLogIn(View view){
        Intent intent = new Intent(this,LogIn.class);
        startActivity(intent);
    }

    boolean signed_up = false;

    public void OnClickRegister(View view){
        //Gather registration information


        //Send to DB


        //If success pass user to logged in page.

        signed_up = true;

        if(signed_up){
            Intent intent = new Intent(this,Homepage.class);
            startActivity(intent);
        }
    }


}