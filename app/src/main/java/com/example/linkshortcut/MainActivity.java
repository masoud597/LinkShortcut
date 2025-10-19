package com.example.linkshortcut;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.R.layout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // get components
        EditText inputLink = findViewById(R.id.inputLink);
        Button btnAdd = findViewById(R.id.btnAdd);
        ListView listLinks = findViewById(R.id.listLinks);

        // get context for shared preference
        Context context = getApplicationContext();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initialize shared preference
        SharedPreferences sharedpref = context.getSharedPreferences("savedLinks", Context.MODE_PRIVATE);
        String[] linksArray = {};
        // if already exists load links to listview
        if (sharedpref.contains("links")) {
            String joinedLinks = sharedpref.getString("links", "");
            linksArray = joinedLinks.split("~");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,layout.simple_list_item_1,linksArray);
            listLinks.setAdapter(adapter);
        }

        String[] finalLinksArray = linksArray;
        btnAdd.setOnClickListener(v -> {

            // TODO Get this thing to be able to add to list, might have to switch to List instead of normal Array

            Save(sharedpref, finalLinksArray);
        });

    }
    private void Save(SharedPreferences sharedpref, String[] links) {
            SharedPreferences.Editor editor = sharedpref.edit();
            String joinedLinks = TextUtils.join("~",links);
            editor.putString("links",joinedLinks);
            editor.apply();
    }
}