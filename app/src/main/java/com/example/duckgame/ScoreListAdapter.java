package com.example.duckgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;
import androidx.annotation.NonNull;

public class ScoreListAdapter extends ArrayAdapter {

    private final Context context;
    private final int resource;
    private final String[] items;

    SharedPreferences persistentScores;

    ScoreListAdapter (Context context, int resource, String[] items){
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.items = items;

        persistentScores = context.getSharedPreferences("highScores", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public View getView(final int position, View view, ViewGroup parent){

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(resource, null, true);

        TextView LevelName = view.findViewById(R.id.LevelName);
        TextView Scores = view.findViewById(R.id.Scores);

        LevelName.setText(String.valueOf(getItem(position)));
        Set<String> set = persistentScores.getStringSet(String.valueOf(position), null);

        if (set != null) {
            ArrayList<String> levelScores = new ArrayList<>();
            levelScores.addAll(set);

            String highScores = "";

            if (levelScores.size() >= 1) highScores += levelScores.get(0);
            if (levelScores.size() >= 2) highScores += ", " + levelScores.get(1);
            if (levelScores.size() >= 3) highScores += ", " + levelScores.get(2);
            if (levelScores.size() >= 4) highScores += ", " + levelScores.get(3);
            if (levelScores.size() >= 5) highScores += ", " + levelScores.get(4);
            Scores.setText(highScores);
        }

        return view;
    }
}
