package com.nithesh.wordie;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

import java.io.IOException;

public class WordDetail extends AppCompatActivity {
    private TextView definitionTextView;
    private DatabaseReference databaseReference;
    private Word word = null;
    private Boolean saveMenuVisibility = false;
    private Boolean deleteMenuVisibility = false;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        TextView wordTextView = findViewById(R.id.actualWordTextView);
        TextView posTextView = findViewById(R.id.actualPosTextView);
        definitionTextView = findViewById(R.id.actualDefinitionTextView);
        ImageButton soundImageButton = findViewById(R.id.soundImageButton);


        Intent receiveIntent = getIntent();
        saveMenuVisibility = receiveIntent.getBooleanExtra(SearchActivity.SHOW_SAVE_MENU, false);
        deleteMenuVisibility = receiveIntent.getBooleanExtra(MainActivity.SHOW_DELETE_MENU, false);
        Bundle bundle = receiveIntent.getBundleExtra(SearchActivity.BUNDLE_KEY);
        if (bundle != null) {
            word = (Word) bundle.getSerializable(SearchActivity.WORD_OBJECT_KEY);
            if (word != null) {
                wordTextView.append(word.getWord());
                posTextView.append(word.getPos());
                appendDefinition(word.getShortDefs());
                if (word.getSoundId().isEmpty()) {
                    soundImageButton.setVisibility(View.GONE);
                } else {
                    final String soundId = word.getSoundId();
                    soundImageButton.setVisibility(View.VISIBLE);
                    soundImageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                playSound(soundId);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("words-collection").child(MainActivity.UUID_USERS);
    }

    private void appendDefinition(String definition) {
        try {
            JSONArray jsonArray = new JSONArray(definition);
            for (int i = 0; i < jsonArray.length(); i++) {
                definitionTextView.append(i + 1 + ". " + jsonArray.getString(i) + "\n\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savemenu, menu);
        MenuItem saveMenuItem = menu.getItem(0);
        MenuItem deleteMenuItem = menu.getItem(1);
        deleteMenuItem.setVisible(deleteMenuVisibility);
        saveMenuItem.setVisible(saveMenuVisibility);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.saveWord == item.getItemId()) {
            if (word != null) {
                databaseReference.push().setValue(word).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(WordDetail.this, word.getWord() + " saved.", Toast.LENGTH_SHORT).show();
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
        if (item.getItemId() == R.id.delete) {
            if (word != null) {
                databaseReference.child(word.getWordReference()).removeValue();
                Toast.makeText(this, "Successfully removed!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void playSound(String soundId) throws IOException {

        String firstLetterOfSoundId = soundId.substring(0, 1);
        String soundUrl = "https://media.merriam-webster.com/audio/prons/en/us/mp3/"
                + firstLetterOfSoundId + "/" + soundId + ".mp3";

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build());
        mediaPlayer.setDataSource(soundUrl);
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer = null;
        }
    }
}