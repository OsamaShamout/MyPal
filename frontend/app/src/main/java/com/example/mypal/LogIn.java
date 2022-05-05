package com.example.mypal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
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

public class LogIn extends AppCompatActivity {

    boolean logged_in = false;

    EditText input_email;
    EditText input_password;
    TextView dialogue;

    Button log_in;

    SharedPreferences sharedpref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        input_email = (EditText) findViewById(R.id.editTextTextEmailAddressLogIn);
        input_password = (EditText)  findViewById(R.id.editTextPasswordLogIn);
        log_in = (Button) findViewById(R.id.loginButton);
        dialogue = (TextView) findViewById(R.id.alertMessageTextView);

    }

    public void OnClickReturnToWelcome(View view){
        Intent intent = new Intent(this,Welcome.class);
        startActivity(intent);
    }

    public void OnClickGoToRegistration(View view){
        Intent intent = new Intent(this,Registration.class);
        startActivity(intent);
    }


    String verification;
    public void OnClickLogIn(View view){

        email_string = input_email.getText().toString();
        password_string = input_password.getText().toString();

        //Ensure a valid email is input.
        Pattern p1 = Pattern.compile("(^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$)");
        Matcher m1 = p1.matcher(email_string);

        if (m1.matches()) {
            email_string = m1.group(1);
        } else {
            Toast.makeText(getApplicationContext(), "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            dialogue.setText("Please enter a valid email address.");
            return;
        }

        //Check if e-mail exists in database.
        //Check if passwords match.
        String url1 = "https://mcprojs.000webhostapp.com/backend/log_in.php";


        //Perform to check conditions
        //Upon success page intent to HomePage (from method).
        SendLogInToDB task1 = new SendLogInToDB();
        task1.execute(url1);

    }

    //                  ---------------------------------------------------
    //                                          APIs
    //                  ---------------------------------------------------

    //Send data to Log In.
    String email_string;
    String password_string;
    public class SendLogInToDB extends AsyncTask<String, Void, String> {
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
                jo.put("password", password_string);

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
                } else if (s.equalsIgnoreCase("Password mismatch\n")) {
                    Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                    dialogue.setText("Incorrect Password");
                }else if(s.equalsIgnoreCase("Password match\n")){
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    logged_in = true;

                    //Validate Information from DB
                    if(logged_in) {
                        Intent intent = new Intent(LogIn.this, Homepage.class);
                        startActivity(intent);
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