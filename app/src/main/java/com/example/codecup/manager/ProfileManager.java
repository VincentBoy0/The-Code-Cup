package com.example.codecup.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * PROFILE MANAGER - Quản lý thông tin người dùng
 *
 * Sử dụng SharedPreferences để lưu trữ dữ liệu cục bộ
 */
public class ProfileManager {

    private static final String PREF_NAME_BASE = "user_profile";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ADDRESS = "address";

    private SharedPreferences sharedPreferences;

    /**
     * Default constructor: will try to read current logged-in user from app_prefs
     */
    public ProfileManager(Context context) {
        SharedPreferences appPrefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String user = appPrefs.getString("logged_in_user", null);
        if (user != null) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME_BASE + "_" + user, Context.MODE_PRIVATE);
        } else {
            // fallback to global profile (non-user-specific)
            sharedPreferences = context.getSharedPreferences(PREF_NAME_BASE, Context.MODE_PRIVATE);
        }
    }

    /**
     * Explicit constructor for a specific userId (email)
     */
    public ProfileManager(Context context, String userId) {
        if (userId != null && !userId.isEmpty()) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME_BASE + "_" + userId, Context.MODE_PRIVATE);
        } else {
            sharedPreferences = context.getSharedPreferences(PREF_NAME_BASE, Context.MODE_PRIVATE);
        }
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
        return sharedPreferences.getString(KEY_PHONE, "");
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public String getAddress() {
        return sharedPreferences.getString(KEY_ADDRESS, "");
    }

    // ===== CLEAR =====
    public void clearAll() {
        sharedPreferences.edit().clear().apply();
    }
}
