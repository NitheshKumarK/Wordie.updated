package com.nithesh.wordie;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    public static ArrayList<Word> wordArrayList ;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private ValueEventListener valueEventListener;
    public static String UUID_USERS;
    private WordAdapter wordAdapter;
    private  ProgressBar progressBar;
    private ListView wordListView;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar  = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        wordArrayList = new ArrayList<>();
        wordListView = findViewById(R.id.wordList);
        wordAdapter = new WordAdapter(this, wordArrayList);
        wordListView.setAdapter(wordAdapter);


        Toolbar myToolBar = findViewById(R.id.mainActivityToolbar);
        setSupportActionBar(myToolBar);
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

         UUID_USERS = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("words-collection").child(UUID_USERS);
        attachChildEventListener();

        wordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = (Word) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this,WordDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(SearchActivity.WORD_OBJECT_KEY, word);
                intent.putExtra(SearchActivity.BUNDLE_KEY,bundle);
                startActivity(intent);
            }
        });
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

    private void attachChildEventListener(){
        if(childEventListener == null){
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Word word = snapshot.getValue(Word.class);
                    wordAdapter.add(word);
                    progressBar.setVisibility(View.GONE);
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
        if(valueEventListener == null){
            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.hasChildren()){
                        View emptyView = findViewById(R.id.empty_view);
                        progressBar.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        wordListView.setEmptyView(emptyView);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };

        }
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detachChildEventListener();
        wordAdapter.clear();
        progressBar.setVisibility(View.GONE);
    }

    private void detachChildEventListener(){
        if(childEventListener != null){
            databaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }
        if (valueEventListener != null){
            databaseReference.removeEventListener(valueEventListener);
            valueEventListener = null;
        }
    }
}