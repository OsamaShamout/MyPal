package com.example.mypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;

public class Homepage extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    ImageButton log_out;
    TextView greetings;
    BottomNavigationView bottomNavigationView;
    String user_id;
    String name;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Homepage.this);
        user_id = preferences.getString("user_id", "");
        name = preferences.getString("name", "");

        greetings = (TextView) findViewById(R.id.greetingsMessage);
        Calendar right_now = Calendar.getInstance();
        int hour = right_now.get(Calendar.HOUR);
        if(hour >= 5 && hour <=11){
            greetings.setText("Good Morning,\n" + name+"!");
        }else if(hour >=12 && hour <= 19){
            greetings.setText("Good afternoon,\n"+name+"!");
        }
        else{
            greetings.setText("Good evening,\n" +name+"!");
        }


        Log.e("Hour is ", String.valueOf(hour));


        log_out = (ImageButton) findViewById(R.id.logoutButton);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavMenu);

        bottomNavigationView.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.home:
                Toast.makeText(getApplicationContext(), "You are already in home page.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.favorites:
                Intent intent = new Intent(this,FavoritePage.class);
                startActivity(intent);
                break;
            case R.id.profile:
                Intent intent2 = new Intent(this,Profile.class);
                startActivity(intent2);
                break;
        }

        return false;
    }

    public void OnClickCreateNewActivity(View view){
        Intent intent = new Intent(this,CreateActivity.class);
        startActivity(intent);

    }

    public void OnClickOpenActivity(View view){
        Intent intent = new Intent(this,ActivitiesPage.class);
        startActivity(intent);

    }

    public void logOut(View view){

        AlertDialog logout1 = new AlertDialog.Builder(Homepage.this).create();
        logout1.setTitle("Log out");
        logout1.setTitle("Are you sure you want to log out?");
        logout1.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Homepage.this, LogIn.class);
                startActivity(intent);
            }
        });
        logout1.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        logout1.show();

    }
}