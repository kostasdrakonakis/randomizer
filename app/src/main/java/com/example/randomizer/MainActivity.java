package com.example.randomizer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kostasdrakonakis.randomizer.Randomizer;
import com.kostasdrakonakis.randomizer.annotations.RandomInt;
import com.kostasdrakonakis.randomizer.annotations.RandomString;

public class MainActivity extends AppCompatActivity {

    @RandomInt
    int number;

    @RandomString
    String rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Randomizer.bind(this);
        setContentView(R.layout.activity_main);
    }
}
