package com.example.duckgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.*;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class LevelsActivity extends AppCompatActivity {

    private final String TAG = "LevelsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        // Enable Fullscreen
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        FrameLayout ContentView = findViewById(R.id.levels);
        ContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        String[] levelNames = getResources().getStringArray(R.array.level_names);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.level_list_item, levelNames);
        ListView LevelList = findViewById(R.id.LevelList);
        LevelList.setAdapter(adapter);

        OnItemClickListener onItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int i, long l) {
                Intent intent = new Intent(parent.getContext(), GameActivity.class);
                intent.putExtra("id", i);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        };

        LevelList.setOnItemClickListener(onItemClickListener);
    }

    public void onClickButtonBack(View view) {
        finish();
        overridePendingTransition(0, 0);
    }
}
