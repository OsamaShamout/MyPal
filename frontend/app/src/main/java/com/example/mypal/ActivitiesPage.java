package com.example.mypal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ActivitiesPage extends AppCompatActivity {

    String active_user = "User";
    String activity_url = "https:/mypal.org/activity123";

    TextView activity_members;

    //Increase-Decrease Activity Memebers variables
    private int members;
    ImageButton increase_member;
    ImageButton decrease_member;

    Button favorite_activity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_page);

        activity_members = (TextView) findViewById(R.id.membersNumber);

        increase_member = (ImageButton) findViewById(R.id.increaseMemberButton);
        decrease_member = (ImageButton) findViewById(R.id.decreaseMemberButton);

        favorite_activity = (Button) findViewById(R.id.favoriteActivity);

    }


    public void OnClickReturnHomepage(View view){
        Intent intent = new Intent(this,Homepage.class);
        startActivity(intent);
    }

    public void increaseMembers(View view) {
        if(members == 30){
            Toast.makeText(getApplicationContext(),"Woah that many people!\n", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            members++;
            activity_members.setText("\t\t" + members);
        }

    }

    public void decreaseMembers(View view) {
        //Ensure no more capacity below 0.
        if(members == 1){
            Toast.makeText(getApplicationContext(),"You can't register no one.\n", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            members--;
            activity_members.setText("\t\t" + members);
        }

    }


    public void registerMembers(View view){

    }

    public void addtoSaved(View view){

    }

    public void ShareActivity(View view){
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

    }
}