package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsHolder> {

    private List<Article> articleList;

    //likecounts are stored in a hashmap
    private Map<String, Integer> likecounts = new HashMap<>();
    //likedarticles are stored in a hashmap with boolean values
    private Map<String, Boolean> likedarticles = new HashMap<>();

    FirebaseAuth auth = FirebaseAuth.getInstance();

    //local database to store like counts
    //works between users
    private SharedPreferences sharedPreferenceslikecount;
    private SharedPreferences sharedPreferenceslikedarticles;

    NewsRecyclerAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        //creates the sharedPreferences database
        this.sharedPreferenceslikecount = context.getSharedPreferences("likecounts", Context.MODE_PRIVATE);
        this.sharedPreferenceslikedarticles = context.getSharedPreferences("likedarticles"+ auth.getUid(), Context.MODE_PRIVATE);
        initializelikecounts();
    }

    private void initializelikecounts()
    {
        //clear like count and liked articles
        likecounts.clear();
        likedarticles.clear();
        for (int i=0;i<articleList.size();i++)
        {
            Article article = articleList.get(i);
            // Gets like count from sharedpreferences, defaults to 0 if not found
            int storedLikeCount = sharedPreferenceslikecount.getInt(article.getUrl(), 0);
            // Puts the value from sharedpreferences into the likecounts hashmap
            likecounts.put(article.getUrl(), storedLikeCount);
            // Check if article was liked before and update likedarticles
            boolean isLiked = sharedPreferenceslikedarticles.getBoolean(article.getUrl() + "_liked", false);
            //Put boolean value into the likedarticles hashmap
            likedarticles.put(article.getUrl(), isLiked);
        }
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        Article article = articleList.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.sourceTextView.setText(article.getSource().getName());
        Picasso.get().load(article.getUrlToImage()).error(R.drawable.error_image_icon).placeholder(R.drawable.error_image_icon).into(holder.imageView);

        //Puts you into ViewNews Activity
        holder.itemView.setOnClickListener((view -> {
            Intent intent = new Intent(view.getContext(), ViewNewsActivity.class);
            intent.putExtra("url", article.getUrl());
            view.getContext().startActivity(intent);
        }));

        //sets the like count and like button state for the current article
        holder.like_count.setText(String.valueOf(likecounts.get(article.getUrl())));
        //sets the liked boolean value of the current article
        holder.liked = likedarticles.get(article.getUrl());
        //if the article was liked, set the imageview accordingly to boolean value
        if (holder.liked) holder.likebutton.setImageResource(R.drawable.liked_button);
        else holder.likebutton.setImageResource(R.drawable.like_button);

        holder.likebutton.setOnClickListener(v -> {
            int currentLikeCount = likecounts.get(article.getUrl());
            boolean isLiked = likedarticles.get(article.getUrl());
            //updates likecount
            if (isLiked)
            {
                holder.likebutton.setImageResource(R.drawable.like_button);
                likecounts.put(article.getUrl(), currentLikeCount - 1);
            } else
            {
                holder.likebutton.setImageResource(R.drawable.liked_button);
                likecounts.put(article.getUrl(), currentLikeCount + 1);
            }
            //Update the like count TextView
            holder.like_count.setText(String.valueOf(likecounts.get(article.getUrl())));
            //flip the value of the likedarticles value, changing key
            likedarticles.put(article.getUrl(), !isLiked);
            //updates the database with likecounts and liked boolean value
            sharedPreferenceslikecount.edit().putInt(article.getUrl(), likecounts.get(article.getUrl())).apply();
            sharedPreferenceslikedarticles.edit().putBoolean(article.getUrl() + "_liked", !isLiked ).apply();
        });
    }

    void updateRecyclerView(List<Article> data)
    {
        articleList.clear();
        articleList.addAll(data);
        initializelikecounts();
    }

    @Override
    public int getItemCount() {return articleList.size();}

    static class NewsHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView sourceTextView;
        ImageView imageView;
        ImageButton likebutton;
        TextView like_count;
        boolean liked = false;

        NewsHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.article_title);
            sourceTextView = itemView.findViewById(R.id.article_source);
            imageView = itemView.findViewById(R.id.article_image);
            likebutton = itemView.findViewById(R.id.like_button);
            like_count = itemView.findViewById(R.id.like_count);

            likebutton.setOnClickListener(v -> {
                if (liked) likebutton.setImageResource(R.drawable.like_button);
                else likebutton.setImageResource(R.drawable.liked_button);
                liked = !liked;
            });
        }
    }
}

