package com.nithesh.wordie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class WordDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);

        Toolbar toolbar = findViewById(R.id.detailActivityToolbar);
        setSupportActionBar(toolbar);

        TextView actualWordTextView = findViewById(R.id.actualWordTextView);
        TextView actualPosTextView = findViewById(R.id.actualPosTextView);
        TextView actualMeaningTextView = findViewById(R.id.actualMeaningTextView);

        Intent receiveIntent = getIntent();
        String actualWord = receiveIntent.getStringExtra(MainActivity.WORD_KEY_WORD);
        String actualPos = receiveIntent.getStringExtra(MainActivity.WORD_KEY_POS);
        String actualMeaning = receiveIntent.getStringExtra(MainActivity.WORD_KEY_MEANING);

        actualWordTextView.setText(actualWord.trim());
        actualPosTextView.setText(actualPos.trim());
        actualMeaningTextView.setText(actualMeaning.trim());

    }
}