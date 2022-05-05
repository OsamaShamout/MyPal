package com.example.mypal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class CreateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Spinner spinner_list_tags;
    Spinner spinner_list_countries;
    EditText activity_name;
    EditText activity_description;
    EditText activity_date;
    TextView activity_capacity;


    //Increase-Decrease Activity Capacity variables
    private int capacity;
    ImageButton increase_capacity;
    ImageButton decrease_capacity;

    String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        activity_name = (EditText) findViewById(R.id.editTextActivityName);
        activity_description = (EditText) findViewById(R.id.editTextActivitytDescrip);
        activity_date = (EditText) findViewById(R.id.editTextActivityDate);
        activity_capacity = (TextView) findViewById(R.id.capacityNumber);

        increase_capacity = (ImageButton) findViewById(R.id.increaseCapacityButton);
        decrease_capacity = (ImageButton) findViewById(R.id.decreaseMemberButton);

        capacity = 1;



        //Define the spinner of tags
        spinner_list_countries= findViewById(R.id.listofLocationsCreate);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countries_available, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_list_countries.setAdapter(adapter);
        spinner_list_countries.setOnItemSelectedListener(this);

        //Define the spinner of tags
        spinner_list_tags= findViewById(R.id.listOfTags);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.activity_tags, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_list_tags.setAdapter(adapter2);
        spinner_list_tags.setOnItemSelectedListener(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = preferences.getString("user_id", "");

    }

    String text;
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
         text = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void OnClickReturnHomepage(View view){
        Intent intent = new Intent(this,Homepage.class);
        startActivity(intent);
    }

    public void increaseCapacity(View view) {
        if(capacity == 30){
            Toast.makeText(getApplicationContext(),"Woah that many people!\nTry to decrease your list.", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            capacity++;
            activity_capacity.setText("\t\t" + capacity);
        }

    }

    public void decreaseCapacity(View view) {
        //Ensure no more capacity below 0.
        if(capacity == 1){
            Toast.makeText(getApplicationContext(),"Cannot make activity with less than 1 member :(", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"Think of more friends to come!", Toast.LENGTH_SHORT).show();

            return;
        }
        else{
            capacity--;
            activity_capacity.setText("\t\t" + capacity);
        }

    }
    Boolean logged_in;
    String activity_name_string;
    String activity_description_string;
    String activity_date_string;
    String activity_location_string;
    String activity_tag_string;
    String activity_capacity_string;
    @SuppressLint("LongLogTag")
    public void onClickRegisterActivity(View view){

        //Obtain all information to register activity
        activity_name_string = activity_name.getText().toString();
        activity_description_string = activity_description.getText().toString();
        activity_date_string = activity_date.getText().toString();
        activity_tag_string = activity_capacity.getText().toString();

        //                              Validate Data Is Correct
        //                              -------------------------


        //Ensure an activity name is given.
        Pattern p1 = Pattern.compile("^(\\s?)+((.)+)");
        Matcher m1 = p1.matcher(activity_name_string);

        if(m1.matches()) {
             activity_name_string = m1.group(2);
        }else{
            Toast.makeText(getApplicationContext(), "Please enter a valid activity name", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.e("Activity name is: " , activity_name_string);

        //Ensure an activity description is given.
        Pattern p2 = Pattern.compile("^(\\s?)+((.)+)");
        Matcher m2 = p2.matcher(activity_description_string);

        if(m2.matches()) {
            activity_description_string = m2.group(2);
        }else{
            Toast.makeText(getApplicationContext(), "Please enter a valid activity description", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.e("Activity description is: " , activity_description_string);

        //Ensure an activity location is correctly given.
        //Obtain Activity Location
        AdapterView<?> parent = spinner_list_countries;
        int number = spinner_list_countries.getSelectedItemPosition();
        activity_location_string = spinner_list_countries.getItemAtPosition(number).toString();

        //Ensure a valid country is chosen.
        if(activity_location_string.equalsIgnoreCase("Choose Country")){
            Toast.makeText(getApplicationContext(), "Please choose a country.", Toast.LENGTH_SHORT).show();
            return;
        }



        Log.e("Activity country is: " , activity_location_string);

        //Make sure a valid date is input.
        Pattern p3 = Pattern.compile("((0[1-9]|[12][0-9]|[3][01])([/.\\-])(0[1-9]|1[0-2])(\\3)(20\\d\\d))");
        Matcher m3 = p3.matcher(activity_date_string);

        //Format provided date to give out standardized input to DB.
        String day;
        String month;
        String year = "";

        Calendar right_now = Calendar.getInstance();
        int current_year = right_now.get(Calendar.YEAR);
        int current_month = right_now.get(Calendar.MONTH);
        int current_day = right_now.get(Calendar.DAY_OF_MONTH);

        //Adjust month with a plus 1
        current_month++;

        Log.e("The current time is: ", current_day + "-" + current_month + "-" + current_year);

        Log.e("This year is: " , String.valueOf(current_year));

        if(m3.matches()) {
            day = m3.group(2);
            month = m3.group(4);
            year = m3.group(6);
        }
        else{
            Toast.makeText(getApplicationContext(), "Please enter a valid date.\nDD/MM/YYYY", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.e("Year from pattern: ", year);
        //Check that the event is scheduled this year.

        Log.e("Year:", year);
        Log.e("Month:", month);
        Log.e("Day:", day);

        //Handle Errors from Input
        if(Integer.parseInt(year) > current_year){
            Toast.makeText(getApplicationContext(), "Activity has to be scheduled within the year " + current_year, Toast.LENGTH_SHORT).show();
            return;
        } else if (Integer.parseInt(year) < current_year){
            Toast.makeText(getApplicationContext(), "Activity cannot be created in previous years", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Integer.parseInt(day) == current_day && Integer.parseInt(month) == current_month){
            Toast.makeText(getApplicationContext(), "Activity cannot be created today", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Integer.parseInt(month) < current_month){
            Toast.makeText(getApplicationContext(), "Activity cannot be created in the past months.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Integer.parseInt(day) < current_day && current_month == Integer.parseInt(month)){
            Toast.makeText(getApplicationContext(), "Activity cannot be created in the past days.", Toast.LENGTH_SHORT).show();
            return;
        }

        activity_date_string = day + "/" + month + "/" + year;


        //Obtain Activity Tag
        AdapterView<?> parent1 = spinner_list_tags;
        int number1 = spinner_list_tags.getSelectedItemPosition();
        activity_tag_string = spinner_list_tags.getItemAtPosition(number1).toString();

        //Ensure a valid tag is chosen.
        if(activity_tag_string.equalsIgnoreCase("Choose Tag")){
            Toast.makeText(getApplicationContext(), "Please choose a valid tag", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.e("Activity tag is: " , activity_tag_string);

        Log.e("The activity name is: ","Name: " + activity_name_string + " Descr: " + " Date: " + activity_date_string + "Location: " + activity_location_string + "Tag: " +activity_tag_string + " Capacity: " + capacity);


        String url = "https://mcprojs.000webhostapp.com/backend/create_activity.php";
        CreateActivityAPI task = new CreateActivityAPI();
        task.execute(url);


    }

    String verification;
    //Register User API
    public class CreateActivityAPI extends AsyncTask<String, Void, String> {
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
                jo.put("name", activity_name_string);
                jo.put("description", activity_description_string);
                jo.put("location", activity_location_string);
                jo.put("date", activity_date_string);
                jo.put("capacity", capacity);
                jo.put("tag", activity_tag_string);
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
//                if(s.equalsIgnoreCase("User already exists\n")){
//                    Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
//
//                } else if (s.equalsIgnoreCase("Success\n")) {
//                    Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_SHORT).show();
//                    logged_in = true;
//                    //Validate Information from DB
//                    if(logged_in) {
//                        Intent intent = new Intent(CreateActivity.this, Homepage.class);
//                        startActivity(intent);
//                    }
//                }



            }
            catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error in receiving data.", Toast.LENGTH_LONG).show();
            }

        }
    }
}