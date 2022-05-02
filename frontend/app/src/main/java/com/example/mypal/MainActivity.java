package com.example.mypal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this,LogIn.class);
        startActivity(intent);

        //To make all elements fade in
        com.airbnb.lottie.LottieAnimationView animation = findViewById(R.id.i);
        TextView welcome_text = (TextView) findViewById(R.id.welcomeText);
        Button start_button = (Button) findViewById(R.id.letsGetStarted);


    }
}