package com.example.autoroter;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.boundapi.AutoBundle;


public class MainActivity extends AppCompatActivity {

    @AutoBundle
    public int id;
    @AutoBundle
    public String name;
    @AutoBundle
    public boolean is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        System.out.println("init: activity");

    }

}
