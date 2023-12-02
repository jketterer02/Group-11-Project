package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    private EditText signin_pass, signin_email;
    private ImageButton signin_button, registerLink, googlesignin;
    private TextView password_recovery;
    private Intent homeintent;
    private Intent registerintent;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //sets the content view to be the login activity, needed so the app doesn't instantly crash
        setContentView(R.layout.activity_login);
        //sets xml elements into variables
        signin_email = findViewById(R.id.login_email);
        signin_pass = findViewById(R.id.login_pass);
        signin_button = findViewById(R.id.login_button);
        password_recovery = findViewById(R.id.forgot_password);
        registerLink = findViewById(R.id.register_button);
        googlesignin = findViewById(R.id.google_button);
        //Creates intents to use to change screens
        homeintent = new Intent(this,MainActivity.class);
        registerintent = new Intent(this, RegisterActivity.class);
        //Creates an instance of a firebaseauth that we can use to call the firebase API
        auth = FirebaseAuth.getInstance();

        //Sets the listeners for clicks on the login
        addListeners();
    }
    private void addListeners()
    {
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Makes login button invisible when pressed
                signin_button.setVisibility(View.INVISIBLE);

                String emailstring = signin_email.getText().toString();
                String passwordstring = signin_pass.getText().toString();
                if(emailstring.isEmpty()||passwordstring.isEmpty())
                {
                    //Makes login button visible again
                    signin_button.setVisibility(View.VISIBLE);
                    showMessage("Please Enter Login Credentials");
                }
                else
                {
                    login(emailstring,passwordstring);
                }
            }
        });

        //Listener for clicking register link
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(registerintent);
                finish();
            }
        });

        password_recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("Clicked Forgot Password Text");
            }
        });

        googlesignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("Clicked Google Signin Button");
            }
        });


    }

    //This method creates a Toast Notification with the specified text
    private void showMessage(String text)
    {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
    //This method authorizes the login credentials
    private void login(String x, String y)
    {
        auth.signInWithEmailAndPassword(x,y).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            //this method is called when we get back a response from the Firebase server
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    //redirects to main activity
                    showMessage("Login Successful!");
                    startActivity(homeintent);
                    finish();
                }
                else
                {
                    signin_button.setVisibility(View.VISIBLE);
                    //Creates a toast with the exception method upon failure
                    showMessage(task.getException().getMessage());

                }
            }
        });
    }

}

