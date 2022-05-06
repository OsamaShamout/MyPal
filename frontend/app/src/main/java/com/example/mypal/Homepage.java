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
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Homepage extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    ImageButton log_out;
    TextView greetings;
    BottomNavigationView bottomNavigationView;
    String user_id;
    String name;
    String country;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Homepage.this);
        user_id = preferences.getString("user_id", "");
        name = preferences.getString("name", "");
        country = preferences.getString("country", "");

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
//
//        LinearLayout ll = (LinearLayout) findViewById(R.id.linearMain);

//        // Create new LinearLayout
//        LinearLayout linearLayout = new LinearLayout(this);
//        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT));
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        linearLayout.setBackgroundColor(0xff99ccff);
//
//        // add ImageView
//        ImageView iv = new ImageView(this);
//
//        //setting image resource
//        iv.setImageResource(R.drawable.beirut);
//        iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT));
//        linearLayout.addView(iv);
//        //textView
//        TextView tv = new TextView(this);
//        tv.setText("Hello world");
//        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT));
//        tv.setTextSize(16);
//        tv.setPadding(5, 3, 0, 3);
//        linearLayout.addView(tv);
//
//        ll.addView(linearLayout);
////
        // Set context view
        setContentView(R.layout.activity_homepage);
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

    String verification;
    //Register User API
    public class GetActivitiesfromDB extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {


            //Variables to initiate connection.
            URL url;
            HttpsURLConnection conn;

            try {
                //Establishing connection between application and API.
                url = new URL(urls[0]);
                conn = (HttpsURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset", "UTF-8");

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                conn.connect();

                //Creating new JSON object to communicate with it to DB.
                JSONObject jo = new JSONObject();
                StringBuffer packedData = new StringBuffer();

                //Send the variables to their respective $_POST.
                jo.put("user_id", user_id);
                jo.put("country", country);

                //Pack data to be processed by PHP for $_POST.
                boolean firstValue = true;

                Iterator it = jo.keys();

                do {
                    String key = it.next().toString();
                    String value = jo.get(key).toString();

                    if (firstValue) {
                        firstValue = false;
                    } else {
                        packedData.append("&");
                    }

                    packedData.append(URLEncoder.encode(key, "UTF-8"));
                    packedData.append("=");
                    packedData.append(URLEncoder.encode(value, "UTF-8"));

                } while (it.hasNext());

                //Log in console to track. "e" used as color red will appear more significantly while reading log.
                Log.e("Packed data:", packedData.toString());

                //Write to PHP file.
                OutputStream os = conn.getOutputStream();
                BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));

                wr.write(packedData.toString());
                wr.flush();
                wr.close();

                //InputStreams to obtain input from API..

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder total = new StringBuilder();
                String line;
                int serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                while ((line = in.readLine()) != null) {
                    total.append(line).append('\n');
                }
                Log.e("Tag", "Server Response is:" + total.toString() + ": " + serverResponseMessage + "\nResponse Code is: " + serverResponseCode);

                verification = total.toString();

                //Log server return.
                Log.e("test", "result from server: " + verification);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return verification;

        }


        protected void onPostExecute(String s){
            super.onPostExecute(s);
            try {
                Log.e("TAG POST:",s);




            }
            catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error in receiving data.", Toast.LENGTH_LONG).show();
            }

        }
    }
}