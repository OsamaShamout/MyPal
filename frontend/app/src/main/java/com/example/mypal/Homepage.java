package com.example.mypal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Homepage extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    ImageButton log_out;
    TextView greetings;
    BottomNavigationView bottomNavigationView;
    String user_id;
    String name;
    String country;

    ImageView img1;
    ImageView img2;
    ImageView img3;

    //Name Texts
    TextView txt_n1;
    TextView txt_n2;
    TextView txt_n3;

    //Date Texts
    TextView txt_d1;
    TextView txt_d2;
    TextView txt_d3;


    Calendar right_now;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Homepage.this);
        user_id = preferences.getString("user_id", "");
        name = preferences.getString("name", "");
        country = preferences.getString("country", "");

        greetings = (TextView) findViewById(R.id.greetingsMessage);
        right_now = Calendar.getInstance();



        int hour = right_now.get(Calendar.HOUR);
        if(hour >= 5 && hour <=11){
            greetings.setText("Good Morning,\n" + name+"!");
        }else if(hour >=12 && hour <= 19){
            greetings.setText("Good afternoon,\n"+name+"!");
        }
        else{
            greetings.setText("Good evening,\n" +name+"!");
        }

         img1 = (ImageView) findViewById(R.id.activityImage1);
         img2 = (ImageView) findViewById(R.id.activityImage2);
         img3 = (ImageView) findViewById(R.id.activityImage3);

         txt_n1 = findViewById(R.id.activityTextView1);
         txt_n2  = findViewById(R.id.activityTextView2);
         txt_n3  = findViewById(R.id.activityTextView3);


         txt_d1 = findViewById(R.id.activityDate1);
         txt_d2 = findViewById(R.id.activityDate2);
         txt_d3 = findViewById(R.id.activityDate3);


         Log.e("Hour is ", String.valueOf(hour));


        log_out = (ImageButton) findViewById(R.id.logoutButton);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavMenu);

        bottomNavigationView.setOnItemSelectedListener(this);

        String url = "https://mcprojs.000webhostapp.com/backend/fetch_home.php";
        GetActivitiesfromDB task = new GetActivitiesfromDB();
        task.execute(url);



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
        String tag = view.getTag().toString();
        Intent intent = new Intent(this,ActivitiesPage.class);
        intent.putExtra("activity_tag", tag);
        if(tag.equalsIgnoreCase("txt_n_1") || tag.equalsIgnoreCase("txt_d_1")|| tag.equalsIgnoreCase("num1")) {
            intent.putExtra("activity_id", activity1_id);
            startActivity(intent);
        }else if (tag.equalsIgnoreCase("txt_n_2") || tag.equalsIgnoreCase("txt_d_2") || tag.equalsIgnoreCase("num2")) {
            intent.putExtra("activity_id", activity2_id);
            startActivity(intent);
        }else if (tag.equalsIgnoreCase("txt_n_3") || tag.equalsIgnoreCase("txt_d_3") || tag.equalsIgnoreCase("num3")) {
            intent.putExtra("activity_id", activity3_id);
            startActivity(intent);
        }

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
    //Initialize Activity Variables
    String activity1_name = "";
    String activity1_description = "";
    String activity1_location = "";
    String activity1_date = "";
    String activity1_id = "";

    String activity2_name = "";
    String activity2_description = "";
    String activity2_location  = "";
    String activity2_date  = "";
    String activity2_id = "";

    String activity3_name  = "";
    String activity3_description = "";
    String activity3_location = "";
    String activity3_date = "";
    String activity3_id = "";

    //Activity Dates Formatted Strings
    String activity1_date_new = "";
    String activity2_date_new = "";
    String activity3_date_new = "";

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


        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                //Convert JSON Arrays into Strings.

                JSONArray main_obj = new JSONArray(s);

                String obj_str = main_obj.toString();


                //Obtain the 3 activites
                JSONObject activity1 = main_obj.getJSONObject(0);
                JSONObject activity2 = main_obj.getJSONObject(1);
                JSONObject activity3 = main_obj.getJSONObject(2);

                Log.e("Activity 1: ", activity1.toString());
                Log.e("Activity 2: ", activity2.toString());
                Log.e("Activity 3: ", activity3.toString());

                //Get the 3 activities data and convert to string
                 activity1_name = activity1.getString("name");
                 activity1_description = activity1.getString("description");
                 activity1_location = activity1.getString("location");
                 activity1_date = activity1.getString("date");
                 activity1_id= activity1.getString("activity_id");

                //Get the 3 activities data and convert to string
                 activity2_name = activity2.getString("name");
                 activity2_description = activity2.getString("description");
                 activity2_location = activity2.getString("location");
                 activity2_date = activity2.getString("date");
                 activity2_id= activity2.getString("activity_id");

                //Get the 3 activities data and convert to string
                 activity3_name = activity3.getString("name");
                 activity3_description = activity3.getString("description");
                 activity3_location = activity3.getString("location");
                 activity3_date = activity3.getString("date");
                 activity3_id= activity3.getString("activity_id");

                Log.e("Activity 1 name: ", activity1_name);
                Log.e("Activity 1 date: ", activity1_date);
                Log.e("Activity 3 ID: ", activity1_id);

                Log.e("Activity 2 name: ", activity2_name);
                Log.e("Activity 2 date: ", activity2_date);
                Log.e("Activity 3 ID: ", activity2_id);

                Log.e("Activity 3 name: ", activity3_name);
                Log.e("Activity 3 date: ", activity3_date);
                Log.e("Activity 3 ID: ", activity3_id);

                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                Date dt1 = format1.parse(activity1_date);
                Date dt2 = format1.parse(activity2_date);
                Date dt3 = format1.parse(activity3_date);


                //Format day name of event occurring
                DateFormat format2 = new SimpleDateFormat("EEE, MMM d, ''yy");
                activity1_date_new = format2.format(dt1);
                activity2_date_new = format2.format(dt2);
                activity3_date_new = format2.format(dt3);




                //Cannot set from background, run on main thread
                //Set names
                txt_n1.setText(activity1_name);
                txt_n2.setText(activity2_name);
                txt_n3.setText(activity3_name);

                //Set Dates
                txt_d1.setText(activity1_date_new);
                txt_d2.setText(activity2_date_new);
                txt_d3.setText(activity3_date_new);



            }
            catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error in post execution.", Toast.LENGTH_LONG).show();
            }
        }
    }
}