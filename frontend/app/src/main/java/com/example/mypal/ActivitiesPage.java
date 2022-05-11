package com.example.mypal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
import java.util.Date;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class ActivitiesPage extends AppCompatActivity {

    String user_id;
    String name;
    String country;
    String activity_url = "https:/mypal.org/activity123";
    int members_available;

    TextView activity_members;

    //Increase-Decrease Activity Memebers variables
    private int members_to_register = 1;
    ImageButton increase_member;
    ImageButton decrease_member;

    Button favorite_activity;

    String activity_id;

    String activity_name_str;
    String activity_description_str;
    String activity_location_str;
    String activity_date_str;
    String activity_capacity_str;
    String activity_creator_str;
    String activity_tag_str;
    String activity_date_new;



    TextView activity_name;
    TextView activity_creator;
    TextView activity_descr;
    TextView activity_locc;
    TextView activity_datee;
    Button TagButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_page);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = preferences.getString("user_id", "");
        name = preferences.getString("name", "");
        country = preferences.getString("country", "");

        activity_name = (TextView) findViewById(R.id.eventInformationText);
        activity_creator = (TextView) findViewById(R.id.activity_creator);
        activity_descr = (TextView) findViewById(R.id.activityDescriptionActivity);
        activity_locc = (TextView) findViewById(R.id.activity_loc_ac);
        activity_datee = (TextView) findViewById(R.id.activity_date_ac);

        TagButton = (Button) findViewById(R.id.tagButton);

        activity_members = (TextView) findViewById(R.id.membersNumber);

        increase_member = (ImageButton) findViewById(R.id.increaseMemberButton);
        decrease_member = (ImageButton) findViewById(R.id.decreaseMemberButton);

        favorite_activity = (Button) findViewById(R.id.favoriteActivity);

        Intent intent = getIntent();
        activity_id = intent.getStringExtra("activity_id");

        String api_url = "https://mcprojs.000webhostapp.com/backend/fetch_activity.php";
        GetActivitiesfromDB task = new GetActivitiesfromDB();
        task.execute(api_url);
    }


    public void OnClickReturnHomepage(View view) {
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);
    }

    public void increaseMembers(View view) {
        if (members_to_register == 30) {
            Toast.makeText(getApplicationContext(), "Woah that many people!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            members_to_register++;
            activity_members.setText("\t\t" + members_to_register);
        }

    }

    public void decreaseMembers(View view) {
        //Ensure no more capacity below 0.
        if (members_to_register == 1) {
            Toast.makeText(getApplicationContext(), "You can't register no one.\n", Toast.LENGTH_SHORT).show();
            return;
        } else {
            members_to_register--;
            activity_members.setText("\t\t" + members_to_register);
        }

    }

    int capacity_rem;
    public void registerMembers(View view) {
        capacity_rem = members_available - members_to_register;
        String url = "https://mcprojs.000webhostapp.com/backend/register_activity_members.php";
        if(capacity_rem < 0){
            Toast.makeText(getApplicationContext(), "Sorry but members specified cannot be all registered.", Toast.LENGTH_SHORT).show();
        }
        else{
            RegisterMembers task = new RegisterMembers();
            task.execute(url);
        }
    }

    public void addtoSaved(View view) {

    }

    public void ShareActivity(View view) {
        AlertDialog share = new AlertDialog.Builder(ActivitiesPage.this).create();
        share.setTitle("Share this activity");
        share.setTitle("Link to be copied: \n" + activity_url);
        share.setButton(AlertDialog.BUTTON_POSITIVE, "Copy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Activity URL", activity_url);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Copied to clipboard.", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });
        share.setButton(AlertDialog.BUTTON_NEGATIVE, "Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        share.show();
    }

    String verification0;
    public class RegisterMembers extends AsyncTask<String, Void, String> {
        @SuppressLint("LongLogTag")
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
                jo.put("activity_id", activity_id);
                jo.put("capacity", capacity_rem);
                jo.put("members_registered", members_to_register);
                jo.put("user_id", user_id);
                jo.put("name", name);

                Log.e("Activity ID is;", activity_id);
                Log.e("Activity cap is;", String.valueOf(capacity_rem));
                Log.e("Activity members_registered is;", String.valueOf(members_to_register));
                Log.e("USER ID is;", user_id);
                Log.e("Name is;", name);






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

                verification0 = total.toString();

                //Log server return.
                Log.e("test", "result from server: " + verification0);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return verification0;

        }


        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Toast.makeText(getApplicationContext(), "Registration Successful.", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error in post execution.", Toast.LENGTH_LONG).show();
            }
        }
    }

    String verification2;
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
                jo.put("activity_id", activity_id);

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

                verification2 = total.toString();

                //Log server return.
                Log.e("test", "result from server: " + verification2);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return verification2;

        }


        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                //Convert JSON Arrays into Strings.

                JSONArray main_obj = new JSONArray(s);


                //Obtain the activity_data
                JSONObject activity = main_obj.getJSONObject(0);

                //Get the activity's data and convert to string
                activity_name_str = activity.getString("name");
                activity_description_str = activity.getString("description");
                activity_location_str = activity.getString("location");
                activity_date_str = activity.getString("date");
                activity_capacity_str = activity.getString("capacity");
                activity_creator_str = activity.getString("activity_creator");
                activity_tag_str = activity.getString("activity_tag");

                members_available = Integer.parseInt(activity_capacity_str);


                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                Date dt1 = format1.parse(activity_date_str);

                //Format day name of event occurring
                DateFormat format2 = new SimpleDateFormat("EEE, MMM d, ''yy");
                activity_date_new = format2.format(dt1);

                activity_name.setText(activity_name_str);
                activity_descr.setText(activity_description_str);
                activity_locc.setText(activity_location_str);
                activity_datee.setText(activity_date_new);
                activity_creator.setText(activity_creator_str);
                TagButton.setText(activity_tag_str);


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error in post execution.", Toast.LENGTH_LONG).show();
            }
        }
    }
}