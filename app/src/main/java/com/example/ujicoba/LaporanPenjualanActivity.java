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

public class LaporanPenjualanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_laporan_penjualan);
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
                    startActivity(new Intent(LaporanPenjualanActivity.this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(LaporanPenjualanActivity.this, ProfileActivity.class));
                    return true;
                }else if (itemId == R.id.nav_scan) {
                    startActivity(new Intent(LaporanPenjualanActivity.this, ActivityCameraScan.class));
                    return true;
                }else if (itemId == R.id.nav_cart) {
                    startActivity(new Intent(LaporanPenjualanActivity.this, ActivityCart.class));
                    return true;
                }else if (itemId == R.id.nav_chat) {
                    startActivity(new Intent(LaporanPenjualanActivity.this, ChatActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
    private void moveToLogin() {
        Intent intent = new Intent(LaporanPenjualanActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Animasi transisi
        finish(); // Tutup MainActivity agar tidak bisa kembali
    }
}