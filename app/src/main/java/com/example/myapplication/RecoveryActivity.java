package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class RecoveryActivity extends AppCompatActivity {

    private EditText recoveryemail;
    private ImageButton recoverybutton, backbutton;
    private Intent loginintent;
    private FirebaseAuth auth;
    private String emailstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);


        recoveryemail = findViewById(R.id.recovery_email);
        backbutton = findViewById(R.id.r_back_button);
        recoverybutton = findViewById(R.id.recover_button);

        auth = FirebaseAuth.getInstance();

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
                emailstring = recoveryemail.getText().toString();
                if(emailstring.isEmpty()) Toast.makeText(getApplicationContext(), "Please Enter Login Email", Toast.LENGTH_LONG).show();
                else
                {
                     ResetPassword();
                }
            }
        });
    }

    private void ResetPassword()
    {
        recoverybutton.setVisibility(View.INVISIBLE);
        auth.sendPasswordResetEmail(emailstring).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Recovery email sent to" + emailstring, Toast.LENGTH_LONG).show();
                startActivity(loginintent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error: "+ e.getMessage(), Toast.LENGTH_LONG).show();
                recoverybutton.setVisibility(View.VISIBLE);
            }
        });
    }

}