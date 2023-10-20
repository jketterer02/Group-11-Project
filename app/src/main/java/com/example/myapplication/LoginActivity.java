package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity
{
    private EditText login_pass, login_email;
    private Button login_button;
    private TextView registerLink;
    private Intent homeintent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        login_email = findViewById(R.id.login_email);
        login_pass = findViewById(R.id.login_pass);
        login_button = findViewById(R.id.login_button);
        registerLink = findViewById(R.id.register_button);
        homeintent = new Intent(this, MainActivity.class);
        addListeners();
    }
    private void addListeners()
    {
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("Hello World!");
            }
        });
    }

    private void showMessage(String text)
    {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
}