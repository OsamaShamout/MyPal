package com.example.mypal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class CreateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Spinner spinner_list;
    EditText activity_name;
    EditText activity_description;
    EditText activity_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        activity_name = (EditText) findViewById(R.id.editTextActivityName);
        activity_description = (EditText) findViewById(R.id.editTextActivitytDescrip);
        activity_date = (EditText) findViewById(R.id.editTextActivityDate);


        //Define the spinner
        spinner_list= findViewById(R.id.listOfTags);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.activity_tags, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_list.setAdapter(adapter);
        spinner_list.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    String activity_name_string;
    String activity_description_string;
    String activity_date_string;
    String activity_tag_string;
    public void onClickRegisterActivity(View view){

        //Obtain all information to register activity
        activity_name_string = activity_name.toString();
        activity_description_string = activity_description.toString();
        activity_date_string = activity_date.toString();

        //Obtain Activity Tag
        AdapterView<?> parent = spinner_list;
        int number = spinner_list.getSelectedItemPosition();
        activity_tag_string = spinner_list.getItemAtPosition(number).toString();


    }
}