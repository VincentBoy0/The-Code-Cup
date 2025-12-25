package com.example.codecutcodeshit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.codecutcodeshit.manager.ProfileManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * ACTIVITY MÀN HÌNH PROFILE
 *
 * Hiển thị và chỉnh sửa thông tin người dùng
 * - View mode: Chỉ xem thông tin
 * - Edit mode: Cho phép chỉnh sửa
 */
public class ProfileActivity extends AppCompatActivity {

    // Views
    private ImageView ivEdit;
    private TextView tvDisplayName, tvMemberSince;
    private EditText etFullName, etPhone, etEmail, etAddress;
    private Button btnSave, btnCancel;
    private BottomNavigationView bottomNavigation;

    // Manager
    private ProfileManager profileManager;

    // State
    private boolean isEditMode = false;

    // Lưu giá trị cũ để cancel
    private String oldFullName, oldPhone, oldEmail, oldAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileManager = new ProfileManager(this);

        initViews();
        loadProfileData();
        setupBottomNavigation();
        setupListeners();
        setupBackHandler();
    }

    /**
     * Xử lý nút Back - nếu đang edit mode thì hủy chỉnh sửa
     */
    private void setupBackHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEditMode) {
                    cancelEdit();
                } else {
                    // Cho phép back bình thường
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private void initViews() {
        ivEdit = findViewById(R.id.iv_edit);
        tvDisplayName = findViewById(R.id.tv_display_name);
        tvMemberSince = findViewById(R.id.tv_member_since);
        etFullName = findViewById(R.id.et_full_name);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
        etAddress = findViewById(R.id.et_address);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        bottomNavigation = findViewById(R.id.bottom_navigation);
    }

    private void loadProfileData() {
        String fullName = profileManager.getFullName();
        String phone = profileManager.getPhone();
        String email = profileManager.getEmail();
        String address = profileManager.getAddress();

        tvDisplayName.setText(fullName);
        etFullName.setText(fullName);
        etPhone.setText(phone);
        etEmail.setText(email);
        etAddress.setText(address);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_profile);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_orders) {
                startActivity(new Intent(this, MyOrdersActivity.class));
                return true;
            } else if (itemId == R.id.nav_rewards) {
                startActivity(new Intent(this, RewardsActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }

    private void setupListeners() {
        // Nút Edit
        ivEdit.setOnClickListener(v -> {
            if (!isEditMode) {
                enterEditMode();
            }
        });

        // Nút Save
        btnSave.setOnClickListener(v -> saveProfile());

        // Nút Cancel
        btnCancel.setOnClickListener(v -> cancelEdit());
    }

    /**
     * Chuyển sang Edit Mode
     */
    private void enterEditMode() {
        isEditMode = true;

        // Lưu giá trị cũ
        oldFullName = etFullName.getText().toString();
        oldPhone = etPhone.getText().toString();
        oldEmail = etEmail.getText().toString();
        oldAddress = etAddress.getText().toString();

        // Enable các EditText
        setEditTextEnabled(true);

        // Hiện các nút
        btnSave.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);

        // Ẩn nút edit
        ivEdit.setVisibility(View.GONE);

        // Focus vào field đầu tiên
        etFullName.requestFocus();

        Toast.makeText(this, "Edit mode enabled", Toast.LENGTH_SHORT).show();
    }

    /**
     * Thoát Edit Mode
     */
    private void exitEditMode() {
        isEditMode = false;

        // Disable các EditText
        setEditTextEnabled(false);

        // Ẩn các nút
        btnSave.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);

        // Hiện nút edit
        ivEdit.setVisibility(View.VISIBLE);
    }

    /**
     * Enable/Disable các EditText
     */
    private void setEditTextEnabled(boolean enabled) {
        etFullName.setEnabled(enabled);
        etPhone.setEnabled(enabled);
        etEmail.setEnabled(enabled);
        etAddress.setEnabled(enabled);
    }

    /**
     * Lưu thông tin profile
     */
    private void saveProfile() {
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        // Validate
        if (fullName.isEmpty()) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        // Lưu vào ProfileManager
        profileManager.saveAll(fullName, phone, email, address);

        // Cập nhật display name
        tvDisplayName.setText(fullName);

        // Thoát edit mode
        exitEditMode();

        Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Hủy chỉnh sửa, khôi phục giá trị cũ
     */
    private void cancelEdit() {
        // Khôi phục giá trị cũ
        etFullName.setText(oldFullName);
        etPhone.setText(oldPhone);
        etEmail.setText(oldEmail);
        etAddress.setText(oldAddress);

        // Thoát edit mode
        exitEditMode();

        Toast.makeText(this, "Changes discarded", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigation.setSelectedItemId(R.id.nav_profile);
    }
}

