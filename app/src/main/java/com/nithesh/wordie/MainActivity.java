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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ChildEventListener childEventListener;
    private WordAdapter wordAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ListView wordList;
    public static final String WORD_KEY_WORD = "actual word key";
    public static final String WORD_KEY_POS = "actual pos key";
    public static final String WORD_KEY_MEANING = "actual meaning key";
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance("https://wordie-16168.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("words");

        final ArrayList<Word> wordArrayList = new ArrayList<>();
        wordList = findViewById(R.id.wordList);
        wordAdapter = new WordAdapter(this, R.layout.word_layout, wordArrayList);
        wordList.setAdapter(wordAdapter);

        wordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = (Word) parent.getItemAtPosition(position);
                sendToDetailActivity(word);
            }
        });

        attachReadEventListener();
        Toolbar myToolBar = findViewById(R.id.mainActivityToolbar);
        setSupportActionBar(myToolBar);
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
    }

    private void attachReadEventListener() {
        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Word word = snapshot.getValue(Word.class);
                    wordAdapter.add(word);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            databaseReference.addChildEventListener(childEventListener);
        }
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
    protected void onDestroy() {
        super.onDestroy();
        if (databaseReference != null) {
            databaseReference.removeEventListener(childEventListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.searchmenu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchAction).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}