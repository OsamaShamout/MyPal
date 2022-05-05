package com.example.mypal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class profily_modify extends AppCompatActivity {

    EditText name_input;
    EditText location_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profily_modify);

        name_input = (EditText) findViewById(R.id.usernameProfilEditText);
        location_input = (EditText) findViewById(R.id.userLocationProfile);
    }

    String  name_input_string;
    String location_input_string;

    String first_name;
    String last_name;
    public void onClickApplyProfileChange(View view){

        name_input_string = name_input.getText().toString();
        location_input_string = location_input.getText().toString();

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

        //Ensure a valid country is inputted correctly.
        Pattern p2 = Pattern.compile("^((\\w)+)$");
        Matcher m2 = p2.matcher(name_input_string);
        if (m2.matches()) {
            location_input_string = m2.group(1);
        } else {
            Toast.makeText(getApplicationContext(), "Please input one word country.", Toast.LENGTH_SHORT).show();
        }

        String url = "https://mcprojs.000webhostapp.com/backend/create_activity.php";
        CreateActivity.CreateActivityAPI task = new CreateActivity.CreateActivityAPI();
        task.execute(url);

    }
}