package com.nithesh.wordie;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String WORD_KEY_WORD = "actual word key";
    public static final String WORD_KEY_POS = "actual pos key";
    public static final String WORD_KEY_MEANING = "actual meaning key";
    public static ArrayList<Word> wordArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView wordListView = findViewById(R.id.wordList);
        WordAdapter wordAdapter = new WordAdapter(this, wordArrayList);

        wordListView.setEmptyView(findViewById(R.id.empty_view));
        wordListView.setAdapter(wordAdapter);
        //WordAdapter wordAdapter = new WordAdapter(this, R.layout.word_layout, wordArrayList);
        //wordListView.setAdapter(wordAdapter);


        wordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });


        Toolbar myToolBar = findViewById(R.id.mainActivityToolbar);
        setSupportActionBar(myToolBar);
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);


    }


    private void sendToDetailActivity(Word word) {
        String actualWord = word.getWord();
        String actualPos = word.getPos();
        String actualMeaning = word.getDefinition();
        Intent detailIntent = new Intent(this, WordDetail.class);
        detailIntent.putExtra(WORD_KEY_WORD, actualWord);
        detailIntent.putExtra(WORD_KEY_POS, actualPos);
        detailIntent.putExtra(WORD_KEY_MEANING, actualMeaning);
        startActivity(detailIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.searchmenu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchAction).getActionView();
        searchView.setIconified(false);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.searchAction == item.getItemId()) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}