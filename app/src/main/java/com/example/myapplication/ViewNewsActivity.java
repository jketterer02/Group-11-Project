package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.myapplication.databinding.ActivityMainBinding;

public class ViewNewsActivity extends AppCompatActivity {

    WebView webView;
    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_news);

        backbutton = findViewById(R.id.back_to_news);

        String URL = getIntent().getStringExtra("url");
        webView = findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(URL);

        addListeners();

    }

    @Override
    public void onBackPressed()
    {
        if(webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    private void addListeners()
    {

        //Listener for clicking back button
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onBackPressed();}
        });


    }
}