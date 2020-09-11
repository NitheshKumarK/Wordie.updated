package com.nithesh.wordie;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;

public class WordDetail extends AppCompatActivity {
    private TextView wordTextView;
    private TextView posTextView;
    private TextView definitionTextView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Word word = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        wordTextView = findViewById(R.id.actualWordTextView);
        posTextView = findViewById(R.id.actualPosTextView);
        definitionTextView = findViewById(R.id.actualDefinitionTextView);


        Intent receiveIntent = getIntent();
        Bundle bundle = receiveIntent.getBundleExtra(SearchActivity.BUNDLE_KEY);
        if (bundle!=null) {
            word = (Word) bundle.getSerializable(SearchActivity.WORD_OBJECT_KEY);
            if (word != null){
                wordTextView.append(word.getWord());
                posTextView.append(word.getPos());
                appendDefinition(word.getShortDefs());
        }
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("words-collection").child(MainActivity.UUID_USERS);

    }
    private void appendDefinition(String definition){
        try {
            JSONArray jsonArray = new JSONArray(definition);
            for(int i=0;i<jsonArray.length();i++){
                definitionTextView.append(i+1+". "+jsonArray.getString(i)+"\n\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savemenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(R.id.saveWord == item.getItemId()){
            if(word!=null){
                databaseReference.push().setValue(word).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(WordDetail.this, word.getWord()+" saved.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(WordDetail.this, "save failed!", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}