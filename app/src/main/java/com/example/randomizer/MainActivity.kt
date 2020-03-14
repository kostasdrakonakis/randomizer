package com.example.randomizer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kostasdrakonakis.randomizer.Randomizer
import com.kostasdrakonakis.randomizer.annotations.RandomInt
import com.kostasdrakonakis.randomizer.annotations.RandomString

class MainActivity : AppCompatActivity() {
    @RandomInt
    @JvmField
    var number: Int = 0

    @RandomString
    lateinit var rand: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Randomizer.bind(this)
        setContentView(R.layout.activity_main)
    }
}