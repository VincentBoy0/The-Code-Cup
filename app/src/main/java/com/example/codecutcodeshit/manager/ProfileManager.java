package com.example.codecutcodeshit.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * PROFILE MANAGER - Quản lý thông tin người dùng
 *
 * Sử dụng SharedPreferences để lưu trữ dữ liệu cục bộ
 */
public class ProfileManager {

    private static final String PREF_NAME = "user_profile";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ADDRESS = "address";

    private SharedPreferences sharedPreferences;

    public ProfileManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // ===== SAVE =====
    public void saveFullName(String fullName) {
        sharedPreferences.edit().putString(KEY_FULL_NAME, fullName).apply();
    }

    public void savePhone(String phone) {
        sharedPreferences.edit().putString(KEY_PHONE, phone).apply();
    }

    public void saveEmail(String email) {
        sharedPreferences.edit().putString(KEY_EMAIL, email).apply();
    }

    public void saveAddress(String address) {
        sharedPreferences.edit().putString(KEY_ADDRESS, address).apply();
    }

    public void saveAll(String fullName, String phone, String email, String address) {
        sharedPreferences.edit()
                .putString(KEY_FULL_NAME, fullName)
                .putString(KEY_PHONE, phone)
                .putString(KEY_EMAIL, email)
                .putString(KEY_ADDRESS, address)
                .apply();
    }

    // ===== GET =====
    public String getFullName() {
        return sharedPreferences.getString(KEY_FULL_NAME, "Coffee Lover");
    }

    public String getPhone() {
        return sharedPreferences.getString(KEY_PHONE, "+1 234 567 8900");
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "coffee.lover@email.com");
    }

    public String getAddress() {
        return sharedPreferences.getString(KEY_ADDRESS, "123 Coffee Street, Bean City");
    }

    // ===== CLEAR =====
    public void clearAll() {
        sharedPreferences.edit().clear().apply();
    }
}

