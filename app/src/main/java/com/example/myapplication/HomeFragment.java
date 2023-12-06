package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    List<Article> articleList = new ArrayList<>();
    NewsRecyclerAdapter adapter;
    LinearProgressIndicator progressIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.news_recyclerView);
        progressIndicator = (LinearProgressIndicator) view.findViewById(R.id.progress_line);

        setupRecyclerView();
        getNews("GENERAL", null);
        return view;
    }

    void setupRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsRecyclerAdapter(articleList, requireContext());
        recyclerView.setAdapter(adapter);
    }

    void changeInProgress(boolean showChange){
        if(showChange)
            progressIndicator.setVisibility(View.VISIBLE);
        else
            progressIndicator.setVisibility(View.INVISIBLE);
    }

    void getNews(String category, String query){
        changeInProgress(true);
        NewsApiClient newsApiClient = new NewsApiClient("d70ae652c05b44ff97b52bd864da464c");
        //Gets all of the Top Head Lines News from the U.S.
        /*
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder().language("en").category(category).q(query).build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {

                        getActivity().runOnUiThread(()->{
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
        */

        //Gets of all of the general news from given date up to current date and sort by articles by popularity.
        //NOTE: Can only retrieve articles that were published from a month ago to now due to using free version of NEWS API.

        newsApiClient.getEverything(
                new EverythingRequest.Builder().language("en").q("general").from("2023-11-29").sortBy("popularity").build(),

                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {

                        getActivity().runOnUiThread(()->{
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