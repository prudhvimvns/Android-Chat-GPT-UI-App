package com.example.androidactivitylifecycle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class ActivityIntent extends AppCompatActivity {

    // define objects for edit text and button
    EditText edittext;
    EditText editUrl;
    static int PERMISSION_CODE= 100;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);

        // Getting instance of edittext and button
        button = findViewById(R.id.button);
        edittext = findViewById(R.id.editText);

        if (ContextCompat.checkSelfPermission(ActivityIntent.this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(ActivityIntent.this,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_CODE);

        }

        // Attach set on click listener to the button for initiating intent
        button.setOnClickListener(arg -> {
            // getting phone number from edit text and changing it to String
            String phone_number = edittext.getText().toString();

            // Getting instance of Intent with action as ACTION_CALL
            Intent phone_intent = new Intent(Intent.ACTION_CALL);

            // Set data of Intent through Uri by parsing phone number
            phone_intent.setData(Uri.parse("tel:" + phone_number));

            // start Intent
            startActivity(phone_intent);
        });
    }
    public void GetUrlFromIntent(View view) {
        //String url = "http://www.google.com";
        editUrl = findViewById(R.id.urltxt);
        Intent i = new Intent(Intent.ACTION_VIEW);
        String url = editUrl.getText().toString();
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void ExitApp(View view) {
        finish();
    }
}
