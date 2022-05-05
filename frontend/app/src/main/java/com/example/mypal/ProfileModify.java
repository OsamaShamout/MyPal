package com.example.mypal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class ProfileModify extends AppCompatActivity {

    EditText name_input;
    EditText location_input;
    EditText email_input;

    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profily_modify);

        name_input = (EditText) findViewById(R.id.usernameProfilEditText);
        location_input = (EditText) findViewById(R.id.userLocationProfile);
        email_input = (EditText) findViewById(R.id.userEmailEditText);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = preferences.getString("user_id", "");
    }

    String  name_input_string;
    String location_input_string;
    String email_input_string;

    String first_name;
    String last_name;

    boolean name_given;
    boolean location_given;
    boolean email_given;
    public void onClickApplyProfileChange(View view){

        name_input_string = name_input.getText().toString();
        location_input_string = location_input.getText().toString();
        email_input_string = email_input.getText().toString();

        if(!name_input_string.isEmpty()) {
            name_given = true;
            //Ensure a valid name is inputted correctly.
            Pattern p1 = Pattern.compile("^((\\w)+) ((\\w)+$)");
            Matcher m1 = p1.matcher(name_input_string);

            //From captured string
            if (m1.matches()) {
                first_name = m1.group(1);
                last_name = m1.group(3);
            } else {
                Toast.makeText(getApplicationContext(), "Name formatting is: First-name Last-Name", Toast.LENGTH_SHORT).show();
            }
        }

        if(location_input_string.isEmpty()) {
            location_given = true;
            //Ensure a valid country is inputted correctly.
            Pattern p2 = Pattern.compile("^((\\w)+)$");
            Matcher m2 = p2.matcher(location_input_string);
            if (m2.matches()) {
                location_input_string = m2.group(1);
            } else {
                Toast.makeText(getApplicationContext(), "Please input one word country.", Toast.LENGTH_SHORT).show();
            }
        }

        if(!email_input_string.isEmpty()){
            email_given = true;
            //Ensure a valid email is input.
            Pattern p1 = Pattern.compile("(^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$)");
            Matcher m1 = p1.matcher(email_input_string);

            if (m1.matches()) {
                email_input_string = m1.group(1);
            } else {
                Toast.makeText(getApplicationContext(), "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String url = "https://mcprojs.000webhostapp.com/backend/modify_profile.php";
        ModifyProfileAPI task = new ModifyProfileAPI();
        task.execute(url);

    }

    String verification;
    //Register User API
    public class ModifyProfileAPI extends AsyncTask<String, Void, String> {
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
                if(email_given){
                    jo.put("email", email_input_string);
                }
                else if(name_given){
                    jo.put("first_name", first_name);
                    jo.put("last_name", last_name);
                } else if (location_given) {
                    jo.put("location", location_input_string);
                }
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
                if(s.equalsIgnoreCase("Success\n")){
                    Toast.makeText(getApplicationContext(), "Profile Modified Succesffully", Toast.LENGTH_SHORT).show();

                }



            }
            catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error in receiving data.", Toast.LENGTH_LONG).show();
            }

        }
    }
}