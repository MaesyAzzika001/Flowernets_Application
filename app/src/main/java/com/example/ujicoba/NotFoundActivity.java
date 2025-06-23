package com.example.ujicoba;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NotFoundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_not_found);
        SharedPreferences preferences = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("IS_LOGGED_IN", false);

        // Jika belum login, pindahkan ke LoginActivity
        if (!isLoggedIn) {
            moveToLogin();
            return;
        }

        // Inisialisasi BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Menangani klik item navigasi
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId") // Mengatasi masalah Constant Expression Required
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(NotFoundActivity.this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(NotFoundActivity.this, ProfileActivity.class));
                    return true;
                }else if (itemId == R.id.nav_scan) {
                    startActivity(new Intent(NotFoundActivity.this, ActivityCameraScan.class));
                    return true;
                }else if (itemId == R.id.nav_cart) {
                    startActivity(new Intent(NotFoundActivity.this, ActivityCart.class));
                    return true;
                }else if (itemId == R.id.nav_chat) {
                    startActivity(new Intent(NotFoundActivity.this, ChatActivity.class));
                    return true;
                }
                return false;
            }
        });
    }

    private void logoutUser() {
        SharedPreferences preferences = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear(); // Hapus semua data login
        editor.apply();

        moveToLogin();
    }

    private void moveToLogin() {
        Intent intent = new Intent(NotFoundActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Animasi transisi
        finish(); // Tutup MainActivity agar tidak bisa kembali

    }
}