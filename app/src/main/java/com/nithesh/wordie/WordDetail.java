package com.nithesh.wordie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class WordDetail extends AppCompatActivity {
    private TextView wordTextView;
    private TextView posTextView;
    private TextView definitionTextView;

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
            Word word = (Word) bundle.getSerializable(SearchActivity.WORD_OBJECT_KEY);
            if (word != null){
                wordTextView.append(word.getWord());
                posTextView.append(word.getPos());
                definitionTextView.append(word.getDefinition());
        }
        }

    }
}