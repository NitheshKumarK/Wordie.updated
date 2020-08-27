package com.nithesh.wordie;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNewWordActivity extends AppCompatActivity {
        private MenuItem saveMenuItem;
        private DatabaseReference databaseReference;
        private FirebaseDatabase firebaseDatabase;
        private String posSelection;
        private EditText wordEditText;
        private EditText meaningEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_word);


        //get reference from database reference
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("words");

        //spinner things
        Spinner spinner = findViewById(R.id.posSpinner);
        ArrayAdapter<CharSequence> spinnerAdapter =ArrayAdapter.createFromResource(this,
                R.array.spinner_items,android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              posSelection = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       wordEditText = findViewById(R.id.wordEditText);

       meaningEditText = findViewById(R.id.meaningEditText);




        meaningEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveMenuItem.setVisible(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        saveMenuItem  = menu.findItem(R.id.saveWord);
        saveMenuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case(R.id.saveWord):
                pushWord();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    private void pushWord(){

        Word newWord = new Word(wordEditText.getText().toString()
                ,posSelection,
                meaningEditText.getText().toString());
        databaseReference.push().setValue(newWord);

    }
}