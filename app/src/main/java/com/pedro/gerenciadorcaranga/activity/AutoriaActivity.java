package com.pedro.gerenciadorcaranga.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pedro.gerenciadorcaranga.R;

public class AutoriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoria);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
