package com.example.codecup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.appcompat.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private com.example.codecup.MenuAdapter adapter;
    private String currentQuery = "";
    private String currentCategory = "All";

    private static final String TAG = "MenuActivity";
    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_DARK_MODE = "dark_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply saved theme mode before inflating views
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean darkMode = prefs.getBoolean(KEY_DARK_MODE, false);
        AppCompatDelegate.setDefaultNightMode(darkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        RecyclerView recyclerView = findViewById(R.id.menuRecyclerView);
        SearchView searchView = findViewById(R.id.menuSearchView);
        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        TextView menuHeader = findViewById(R.id.menuHeader);
        SwitchCompat darkModeSwitch = findViewById(R.id.darkModeSwitch);

        // initialize switch state
        darkModeSwitch.setChecked(darkMode);

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Persist choice
            prefs.edit().putBoolean(KEY_DARK_MODE, isChecked).apply();
            // Apply the chosen mode
            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            // Recreate activity to apply theme change
            recreate();
        });

        adapter = new com.example.codecup.MenuAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        List<com.example.codecup.Category> categories = com.example.codecup.MenuData.getCategories();
        adapter.setCategories(categories);

        // Diagnostic: show toast and log how many categories/items loaded
        int catCount = categories != null ? categories.size() : 0;
        Toast.makeText(this, "Menu loaded: " + catCount + " categories", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Loaded categories: " + catCount);

        // Update header text so it's visible when activity opens
        menuHeader.setText("Menu (" + catCount + " categories)");

        // Prepare spinner data (All + category names)
        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("All");
        for (com.example.codecup.Category c : categories) {
            spinnerItems.add(c.getName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCategory = spinnerItems.get(position);
                adapter.filter(currentQuery, currentCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // keep currentCategory
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentQuery = query != null ? query : "";
                adapter.filter(currentQuery, currentCategory);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentQuery = newText != null ? newText : "";
                adapter.filter(currentQuery, currentCategory);
                return true;
            }
        });
    }
}
