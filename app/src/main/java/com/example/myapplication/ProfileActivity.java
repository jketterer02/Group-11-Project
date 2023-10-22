package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {


    private TextView useremail;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        useremail = findViewById(R.id.tv);

    }
    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        String email = user.getEmail();
        useremail.setText(email);
    }
}