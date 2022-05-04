package com.example.mypal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Spinner spinner_list;
    EditText activity_name;
    EditText activity_description;
    EditText activity_date;
    TextView activity_capacity;

    //Increase-Decrease Activity Capacity variables
    private int capacity;
    ImageButton increase_capacity;
    ImageButton decrease_capacity;


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



        //Define the spinner
        spinner_list= findViewById(R.id.listOfTags);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.activity_tags, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_list.setAdapter(adapter);
        spinner_list.setOnItemSelectedListener(this);

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

    String activity_name_string;
    String activity_description_string;
    String activity_date_string;
    String activity_tag_string;
    String activity_capacity_string;
    @SuppressLint("LongLogTag")
    public void onClickRegisterActivity(View view){

        //Obtain all information to register activity
        activity_name_string = activity_name.getText().toString();
        activity_description_string = activity_description.getText().toString();
        activity_date_string = activity_date.getText().toString();
        activity_tag_string = activity_capacity.getText().toString();

        //Validate Data Is Correct
        //Make sure a valid tag is chosen.
        if(activity_tag_string.equalsIgnoreCase("Choose One")){
            Toast.makeText(getApplicationContext(), "Please choose a valid tag", Toast.LENGTH_SHORT).show();
            return;
        }

        //Make sure a valid date is input.
        Pattern p = Pattern.compile("((0[1-9]|[12][0-9]|[3][01])([/.\\-])(0[1-9]|1[0-2])(\\3)(20\\d\\d))");
        Matcher m = p.matcher(activity_date_string);

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

        if(m.matches()) {
            day = m.group(2);
            month = m.group(4);
            year = m.group(6);
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

        if(Integer.parseInt(day) < current_day){
            Toast.makeText(getApplicationContext(), "Activity cannot be created in the past days.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Send Input to DB.







        activity_date_string = day + "/" + month + "/" + year;
        Log.e("Date from pattern: ", activity_date_string);

        //Obtain Activity Tag
        AdapterView<?> parent = spinner_list;
        int number = spinner_list.getSelectedItemPosition();
        activity_tag_string = spinner_list.getItemAtPosition(number).toString();



        Log.e("The activity name is: ","Name: " + activity_name_string + " Descr: " + " Date: " + activity_date_string + "Tag: " +activity_tag_string + " Capacity: " + capacity);

        Toast.makeText(getApplicationContext(), "Activity Successfully Created!", Toast.LENGTH_SHORT).show();

    }
}