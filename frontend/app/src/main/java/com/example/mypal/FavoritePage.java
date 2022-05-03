package com.example.mypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class FavoritePage extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_page);
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
                Toast.makeText(getApplicationContext(), "You are alrady in favorites page.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                Intent intent2 = new Intent(this,Profile.class);
                startActivity(intent2);
                break;
        }

        return false;
    }
}