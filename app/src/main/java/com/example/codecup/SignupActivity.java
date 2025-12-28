package com.example.codecup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.codecup.manager.CartManager;
import com.example.codecup.manager.OrderManager;
import com.example.codecup.manager.RewardsManager;
import com.google.android.material.textfield.TextInputEditText;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPassword;
    private Button btnSignup;
    private TextView tvLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSignup = findViewById(R.id.btn_signup);
        tvLoginLink = findViewById(R.id.tv_login_link);

        btnSignup.setOnClickListener(v -> {
            String name = etName.getText() != null ? etName.getText().toString().trim() : "";
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

            if (email.isEmpty() || password.isEmpty()) {
                android.widget.Toast.makeText(this, "Please enter email and password", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            android.content.SharedPreferences accounts = getSharedPreferences("user_accounts", MODE_PRIVATE);
            if (accounts.contains(email)) {
                android.widget.Toast.makeText(this, "Account already exists. Please log in.", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            // Save account credentials
            accounts.edit().putString(email, password).apply();

            // Save initial profile for this user (full name) via ProfileManager
            com.example.codecup.manager.ProfileManager profileManager = new com.example.codecup.manager.ProfileManager(this, email);
            profileManager.saveFullName(name != null && !name.isEmpty() ? name : "Coffee Lover");
            profileManager.saveEmail(email);

            // Initialize per-user managers (will load empty data for new user)
            RewardsManager.getInstance().loadDataForUser(this, email);
            OrderManager.getInstance().loadDataForUser(this, email);
            CartManager.getInstance().loadDataForUser(this, email);

            // Mark logged in and save current user
            android.content.SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            prefs.edit().putBoolean("is_logged_in", true).putString("logged_in_user", email).apply();

            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        });

        tvLoginLink.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, com.example.codecup.LoginActivity.class));
        });
    }
}
