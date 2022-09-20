package com.faheem92mt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.faheem92mt.profile.ProfileActivity;

public class MainActivity extends AppCompatActivity {

    // method 1 - default
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // method 2
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // custom
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // default
        return super.onCreateOptionsMenu(menu);
    }

    // method 3
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //custom
        int id = item.getItemId();

        if (id==R.id.mnuProfile) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        }

        //default
        return super.onOptionsItemSelected(item);
    }


}