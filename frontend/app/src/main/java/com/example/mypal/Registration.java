package com.example.mypal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class Registration extends AppCompatActivity {

    EditText first_name;
    EditText last_name;
    EditText email;
    EditText password;
    EditText repeat_password;
    EditText country;

    Button register;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        first_name = (EditText) findViewById(R.id.firstNameEditText);
        last_name = (EditText) findViewById(R.id.lastNameEditText);
        email = (EditText) findViewById(R.id.editTextEmailAddress);
        password = (EditText) findViewById(R.id.editTextTextPassword);
        repeat_password = (EditText) findViewById(R.id.repeatPasswordEditText);
        country = (EditText) findViewById(R.id.locationEditText);

        register = (Button) findViewById(R.id.registrationButton);

    }

    public void OnClickReturnToLogIn(View view) {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

    boolean signed_up = false;

    String first_name_string;
    String last_name_string;
    String email_string;
    String password_string;
    String repeat_password_string;
    String country_string;

    public void OnClickRegister(View view) {
        //Gather registration information
        first_name_string = first_name.getText().toString();
        last_name_string = last_name.getText().toString();

        if(first_name_string.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter a first name.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(last_name_string.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter a last name.", Toast.LENGTH_SHORT).show();
            return;
        }


        //Validate information
        email_string = email.getText().toString();
        //Ensure a valid email is input.
        Pattern p1 = Pattern.compile("(^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$)");
        Matcher m1 = p1.matcher(email_string);

        if (m1.matches()) {
            email_string = m1.group(1);
        } else {
            Toast.makeText(getApplicationContext(), "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check if e-mail exists in database.


        if(password_string.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter a password.", Toast.LENGTH_SHORT).show();
            return;
        }
        //Ensure a valid password is input.
        Pattern p2 = Pattern.compile("(^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,30}$)");
        Matcher m2 = p2.matcher(password_string);

        if (m2.matches()) {
            password_string = m2.group(1);
        } else {
            AlertDialog password_alert = new AlertDialog.Builder(Registration.this).create();
            password_alert.setTitle("Password Checker has to have:");
            password_alert.setTitle("1. Minimum eight characters\n2. At least one uppercase letter\n3. One lowercase letter\n4. One number\n5. One special character");
            password_alert.setButton(AlertDialog.BUTTON_POSITIVE, "Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            return;
        }

        //Ensure repeated password is the same as previous password.
        if (!password_string.equals(repeat_password_string)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
        }

        password_string = password.getText().toString();
        repeat_password_string = repeat_password.getText().toString();
        country_string = country.getText().toString();


        //Send to DB


        //If success pass user to logged in page.

        signed_up = true;

        if (signed_up) {
            Intent intent = new Intent(this, Homepage.class);
            startActivity(intent);
        }
    }

    //Send data to DB.
    String result_db2;

    public class CheckEmail extends AsyncTask<String, Void, String> {
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
                jo.put("email", email_string);

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

                String verification = total.toString();


                //Log server return.
                Log.e("test", "result from server: " + result_db2);


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result_db2;

        }

        String email;
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
                try {
                    //Convert JSON objects into Strings.
                    JSONArray json_arr = new JSONArray(s);

                    JSONObject jsonObject = json_arr.getJSONObject(0);

                    email = jsonObject.getString("email");

                    Log.e("Tag email from server:" , email);

                    Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error in receiving data.", Toast.LENGTH_LONG).show();
                }


        }
    }
}