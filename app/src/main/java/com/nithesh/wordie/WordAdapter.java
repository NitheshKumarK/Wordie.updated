package com.nithesh.wordie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class WordAdapter extends ArrayAdapter<Word> {
    public WordAdapter(@NonNull Context context, int resource, @NonNull List<Word> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View wordView = convertView;
        if (convertView == null){
            wordView = LayoutInflater.from(getContext()).inflate(R.layout.word_layout,parent,false);
    }
        Word word = getItem(position);
        TextView wordTextView = wordView.findViewById(R.id.word_textView);
        wordTextView.setText(word.getWord());
        return wordView;
    }
}
