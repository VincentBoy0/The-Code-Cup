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

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvSignupLink = findViewById(R.id.tv_signup_link);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

            // Check if the account exists
            android.content.SharedPreferences prefs = getSharedPreferences("user_accounts", MODE_PRIVATE);
            String storedPassword = prefs.getString(email, null);

            if (storedPassword == null) {
                // Account does not exist, prompt to sign up
                android.widget.Toast.makeText(this, "Account does not exist. Please sign up.", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            if (!storedPassword.equals(password)) {
                // Incorrect password
                android.widget.Toast.makeText(this, "Incorrect password.", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            // Clear in-memory singletons and load data for the new user
            RewardsManager.getInstance().clearData();
            OrderManager.getInstance().clearData();
            CartManager.getInstance().clearData();

            RewardsManager.getInstance().loadDataForUser(this, email);
            OrderManager.getInstance().loadDataForUser(this, email);
            CartManager.getInstance().loadDataForUser(this, email);

            // Save login state
            prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            prefs.edit().putBoolean("is_logged_in", true).putString("logged_in_user", email).apply();

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });

        tvSignupLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, com.example.codecup.SignupActivity.class));
        });
    }
}
