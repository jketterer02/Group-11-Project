package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private EditText newemail, newpass, newconfirmpass;
    private Button createnewaccount;
    private Intent homeintent;
    private TextView loginlink;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        newemail = findViewById(R.id.createaccount_email);
        newpass = findViewById(R.id.createaccount_pass);
        newconfirmpass = findViewById(R.id.createaccount_confirmpass);
        createnewaccount = findViewById(R.id.accountcreate_button);
        homeintent = new Intent(this,MainActivity.class);
        //Creates an instance of a firebaseauth that we can use to call the firebase API
        auth = FirebaseAuth.getInstance();
        addListeners();
    }
    private void addListeners()
    {
        createnewaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = newemail.getText().toString();
                String pass = newpass.getText().toString();
                String confirmpass = newconfirmpass.getText().toString();
                if(email.isEmpty()||pass.isEmpty()||confirmpass.isEmpty()) showMessage("Please Enter Login Credentials");
                else
                {
                    registeruser(email,pass);
                }
            }
        });

    }
    //This method creates a Toast Notification with the specified text
    private void showMessage(String text)
    {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
    //This method registers a user into firebase
    private void registeruser(String email, String pass)
    {
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    startActivity(homeintent);
                    finish();
                }
                else showMessage(task.getException().getMessage());
            }
        });
    }
}