package com.example.mypal;

import androidx.appcompat.app.AppCompatActivity;

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
            Toast.makeText(getApplicationContext(),"Cannot make activity with less than 1 member :(\nThink of more friends to come!", Toast.LENGTH_SHORT).show();
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
        Pattern p = Pattern.compile("(0[1-9]|[12][0-9]|[3][01])([/.\\-])(0[1-9]|1[0-2])(\\2)2022");
        Matcher m = p.matcher(activity_date_string);

        




        //Obtain Activity Tag
        AdapterView<?> parent = spinner_list;
        int number = spinner_list.getSelectedItemPosition();
        activity_tag_string = spinner_list.getItemAtPosition(number).toString();



        Log.e("The activity name is: ","Name: " + activity_name_string + " Descr: " + " Date: " + activity_date_string + "Tag: " +activity_tag_string + " Capacity: " + capacity);

        Toast.makeText(getApplicationContext(), "Activity Successfully Created!", Toast.LENGTH_SHORT).show();

    }
}