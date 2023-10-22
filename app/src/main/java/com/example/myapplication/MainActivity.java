// If we have errors enable virtualization

package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private TextView tv;
    FirebaseAuth auth;

    RecyclerView recyclerView;
    List<Article> articleList = new ArrayList<>();
    NewsRecyclerAdapter adapter;
    LinearProgressIndicator progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        //tv = findViewById(R.id.tv);

        recyclerView = findViewById(R.id.news_recyclerView);
        progressIndicator = findViewById(R.id.progress_line);

        setupRecyclerView();
        getNews();

    }
    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();

        String email = user.getEmail();
        //tv.setText(email);
    }

    void setupRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsRecyclerAdapter(articleList);
        recyclerView.setAdapter(adapter);
    }

    void changeInProgress(boolean showChange){
        if(showChange)
            progressIndicator.setVisibility(View.VISIBLE);
        else
            progressIndicator.setVisibility(View.INVISIBLE);
    }

    void getNews(){
        changeInProgress(true);
        NewsApiClient newsApiClient = new NewsApiClient("d70ae652c05b44ff97b52bd864da464c");
        //Gets all of the Top Head Lines News from the U.S.
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder().language("en").build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {

                        runOnUiThread(()->{
                            changeInProgress(false);
                            articleList = response.getArticles();
                            adapter.updateRecyclerView(articleList);
                            adapter.notifyDataSetChanged();
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("Failure!", throwable.getMessage());
                    }
                }
        );
    }
}