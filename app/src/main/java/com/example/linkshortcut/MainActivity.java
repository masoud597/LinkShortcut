package com.example.linkshortcut;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.R.layout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // get components
        EditText textInputLink = findViewById(R.id.inputLink);
        Button btnAdd = findViewById(R.id.btnAdd);
        ListView listviewLinks = findViewById(R.id.listLinks);

        // get context for shared preference
        Context context = getApplicationContext();

        // initialize shared preference
        SharedPreferences sharedpref = context.getSharedPreferences("savedLinks", Context.MODE_PRIVATE);

        String saveData = sharedpref.getString("links", "");
        List<String> linksArray = (saveData.length() > 1) ? new ArrayList<>(Arrays.asList(saveData.split("~"))) : new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,layout.simple_list_item_1,linksArray);
        listviewLinks.setAdapter(adapter);

        // handle button click to add
        btnAdd.setOnClickListener(v -> {
            linksArray.add(textInputLink.getText().toString());
            adapter.notifyDataSetChanged();
            textInputLink.setText("");
            Save(sharedpref, linksArray);
        });
        // handle long press to remove
        listviewLinks.setOnItemLongClickListener((parent, view, position, id) -> {
            linksArray.remove(position);
            adapter.notifyDataSetChanged();
            Save(sharedpref, linksArray);

            Toast.makeText(this,"Succesfully Removed", Toast.LENGTH_SHORT).show();
            return true;
        });
        // handle click to open in browser
        listviewLinks.setOnItemClickListener((parent, view, position, id) -> {
            String link = linksArray.get(position);
            if (!link.startsWith("http://") && !link.startsWith("https://")) {
                link = "http://" + link;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Link format is not correct", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // save to shared preference after every add or remove
    private void Save(SharedPreferences sharedpref, List<String> links) {
            SharedPreferences.Editor editor = sharedpref.edit();
            String joinedLinks = TextUtils.join("~",links);
            editor.putString("links",joinedLinks);
            editor.apply();
    }
}