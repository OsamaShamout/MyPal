package com.example.mypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Profile extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    String user_id;
    BottomNavigationView bottomNavigationView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavMenu);
        bottomNavigationView.setOnItemSelectedListener(this);

        GetProfileInformation task = new GetProfileInformation();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = preferences.getString("user_id", "");

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
        Intent intent = new Intent(this, ProfileModify.class);
        startActivity(intent);
    }

    public class GetProfileInformation extends AsyncTask<String, Void, String> {
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

                if(s.equalsIgnoreCase("User not registered\n")){
                    Toast.makeText(getApplicationContext(), "User not registered", Toast.LENGTH_SHORT).show();
                    dialogue.setText("User not registerd");
                }else{
                    String[] split_values = s.split("_");
                    Log.e("Split return", split_values[0]);
                    Log.e("Split user_id", split_values[1]);
                    //  user_id = Integer.parseInt(split_values[1].toString());
                    String returned_statement = split_values[0];

                    if (returned_statement.equalsIgnoreCase("Password mismatch")) {
                        Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                        dialogue.setText("Incorrect Password");
                    }else if(returned_statement.equalsIgnoreCase("Password match")){
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                        logged_in = true;
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LogIn.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("user_id",split_values[1]);
                        editor.apply();

                        //Validate Information from DB
                        if(logged_in) {
                            Intent intent = new Intent(LogIn.this, Homepage.class);
                            startActivity(intent);
                        }
                    }

                }








            }
            catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error in receiving data.", Toast.LENGTH_LONG).show();
            }

        }
    }
}