package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
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
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements View.OnClickListener {
    Button btn1,btn2,btn3,btn4,btn5,btn6,btn7;
    SearchView searchView;
    RecyclerView recyclerView;
    List<Article> articleList = new ArrayList<>();
    NewsRecyclerAdapter adapter;
    LinearProgressIndicator progressIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = (SearchView) view.findViewById(R.id.search_view);
        btn2 = (Button) view.findViewById(R.id.btn_2);
        btn3 = (Button) view.findViewById(R.id.btn_3);
        btn4 = (Button) view.findViewById(R.id.btn_4);
        btn5 = (Button) view.findViewById(R.id.btn_5);
        btn6 = (Button) view.findViewById(R.id.btn_6);
        btn7 = (Button) view.findViewById(R.id.btn_7);

        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.news_recyclerView);
        progressIndicator = (LinearProgressIndicator) view.findViewById(R.id.progress_line);


        getNews("GENERAL",null);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getNews("GENERAL", query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        setupRecyclerView();

        return view;
    }

    void setupRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsRecyclerAdapter(articleList, requireContext());
        recyclerView.setAdapter(adapter);
    }

    void getNews(String category, String query){
        progressIndicator.setVisibility(View.VISIBLE);
        NewsApiClient newsApiClient = new NewsApiClient("d70ae652c05b44ff97b52bd864da464c");
        //Gets all of the Top Head Lines News from the U.S.
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder().language("en").category(category).q(query).build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {

                        getActivity().runOnUiThread(()->{
                            progressIndicator.setVisibility(View.INVISIBLE);
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

    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        String category = btn.getText().toString();
        getNews(category, null);
    }
}