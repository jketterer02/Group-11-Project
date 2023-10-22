package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {


    private TextView useremail, userID;
    FirebaseAuth auth;
    AuthCredential credential;
    private Intent newsintent;
    private ImageButton backtonews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        newsintent = new Intent(this,MainActivity.class);
        useremail = findViewById(R.id.tv);
        userID = findViewById(R.id.tv2);
        backtonews = findViewById(R.id.backtonewsbutton);
        addListeners();

    }
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        String UID = user.getUid();
        useremail.setText(email);
        userID.setText(UID);
    }
    private void addListeners()
    {
        backtonews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(newsintent);
                finish();
            }
        });

    }
}