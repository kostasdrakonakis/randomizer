package test;

import android.app.Activity;

import com.kostasdrakonakis.randomizer.annotations.RandomInt;
import com.kostasdrakonakis.randomizer.annotations.RandomString;

public class MyActivity extends Activity {
    @RandomInt
    int number;

    @RandomString
    String rand;
}