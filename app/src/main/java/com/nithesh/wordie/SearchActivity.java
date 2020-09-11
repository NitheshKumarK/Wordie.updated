package com.nithesh.wordie;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

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
    private static ProgressBar progressBar;
    public static final String WORD_OBJECT_KEY = "key for the word object";
    public static final String  BUNDLE_KEY = "key for the bundle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        requestListView = findViewById(R.id.requestListView);
        progressBar = findViewById(R.id.progressBar);

        intent = getIntent();
        searchQueryWord = intent.getStringExtra(SearchManager.QUERY);
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word selectedWord =(Word) parent.getItemAtPosition(position);
                Intent intent = new Intent(SearchActivity.this,WordDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(WORD_OBJECT_KEY, selectedWord);
                intent.putExtra(BUNDLE_KEY, bundle);
                startActivity(intent);
            }
        });

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
        progressBar.setVisibility(View.GONE);
        wordAdapter = new WordAdapter(SearchActivity.this, data);
        requestListView.setAdapter(wordAdapter);
        if (data.isEmpty()) {
            View emptyView = findViewById(R.id.search_empty_view);
            emptyView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            requestListView.setEmptyView(emptyView);
        }
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
        if(searchResultWordList!=null) {
            wordAdapter.clear();
            wordAdapter.notifyDataSetChanged();
        }
        searchResultWordList = null;

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
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}