package com.example.mypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Profile extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavMenu);
        bottomNavigationView.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.home:
                Intent intent = new Intent(this,Homepage.class);
                startActivity(intent);
                break;
            case R.id.favorites:
                Intent intent2 = new Intent(this,FavoritePage.class);
                startActivity(intent2);
                break;
            case R.id.profile:
                Toast.makeText(getApplicationContext(), "You are already in profile page.", Toast.LENGTH_SHORT).show();
                break;
        }

        return false;
    }

    public void onClickModifyProfile(View view){

    }
}