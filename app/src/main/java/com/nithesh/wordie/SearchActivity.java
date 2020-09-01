package com.nithesh.wordie;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Word>> {
    private static ArrayList<Word> searchResultWordList = null;
    private static String searchQueryWord;
    private Intent intent;
    private ListView requestListView;
    private WordAdapter wordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        requestListView = findViewById(R.id.requestListView);
        intent = getIntent();
        searchQueryWord = intent.getStringExtra(SearchManager.QUERY);
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.intent = intent;
    }

    @NonNull
    @Override
    public Loader<ArrayList<Word>> onCreateLoader(int id, @Nullable Bundle args) {
        return new WordLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Word>> loader, ArrayList<Word> data) {
        wordAdapter = new WordAdapter(SearchActivity.this, data);
        requestListView.setAdapter(wordAdapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Word>> loader) {
        //clear the list in here
        //so it won't last forever in the list
        searchResultWordList = null;
        wordAdapter.clear();
        wordAdapter.notifyDataSetChanged();


    }

    @Override
    protected void onStop() {
        super.onStop();
        searchResultWordList = null;
        wordAdapter.clear();
        wordAdapter.notifyDataSetChanged();
    }

    public static class WordLoader extends AsyncTaskLoader<ArrayList<Word>> {

        public WordLoader(@NonNull Context context) {
            super(context);
        }

        @Nullable
        @Override
        public ArrayList<Word> loadInBackground() {
            try {
                if (searchResultWordList == null) {
                    searchResultWordList = QueryUtils.getWord(searchQueryWord);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return searchResultWordList;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }
    }
}