package com.example.mypal;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class ShareActivity extends DialogFragment {

    String activity_url = "https:/mypal.org/activity123";
    public Dialog onCreateDialogShare(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.activity_created)
                .setPositiveButton(R.string.copy_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Copy to clipboard for sharing
                        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Activity URL", activity_url);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getActivity().getApplicationContext(), "Saved to clip board", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.done_copy, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}