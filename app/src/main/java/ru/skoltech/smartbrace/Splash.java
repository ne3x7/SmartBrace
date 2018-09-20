package ru.skoltech.smartbrace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // here we will do stuff
            Thread.sleep(1000);
        } catch (Exception e) {
            // do nothing
        }

        SharedPreferences pref = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);

        Boolean logged = pref.getBoolean(getString(R.string.preference_logged_key), false);

        Intent intent;

        if (logged) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(getString(R.string.preference_logged_key), true);
            editor.commit();
        }

        startActivity(intent);
    }
}
