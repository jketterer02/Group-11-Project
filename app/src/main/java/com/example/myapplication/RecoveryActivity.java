package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class RecoveryActivity extends AppCompatActivity {

    private EditText recoveryemail;
    private ImageButton recoverybutton, backbutton;
    private Intent loginintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);


        recoveryemail = findViewById(R.id.recovery_email);
        backbutton = findViewById(R.id.r_back_button);
        recoverybutton = findViewById(R.id.recover_button);

        loginintent = new Intent(this,LoginActivity.class);
        addListeners();
    }

    private void addListeners()
    {
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(loginintent);
                finish();
            }
        });

        recoverybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailstring = recoveryemail.getText().toString();
                if(emailstring.isEmpty()) Toast.makeText(getApplicationContext(), "Please Enter Login Email", Toast.LENGTH_LONG).show();
                else
                {
                    Toast.makeText(getApplicationContext(), "Send email to user", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}