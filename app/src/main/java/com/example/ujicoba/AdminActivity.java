package com.example.ujicoba;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View; // Import View
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class AdminActivity extends AppCompatActivity {
    private LinearLayout buttonKelolaToko;
    private LinearLayout buttonKelolaPelanggan;
    // Deklarasikan TextView dan DatabaseHelper
    private TextView tvTotalToko, tvTotalUser;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        dbHelper = new DatabaseHelper(this);
        // Handle WindowInsets for EdgeToEdge
        // Ganti R.id.main_admin_layout dengan ID root layout di activity_admin.xml
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.kelola_buttons_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

            SharedPreferences preferences = getSharedPreferences("USER_PREF", MODE_PRIVATE);
            boolean isLoggedIn = preferences.getBoolean("IS_LOGGED_IN", false);

            // Jika belum login, pindahkan ke LoginActivity
            if (!isLoggedIn) {
                moveToLogin();
                return; // Penting untuk menghentikan eksekusi lebih lanjut di onCreate jika belum login
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
                        startActivity(new Intent(AdminActivity.this, MainActivity.class));
                        // finish(); // Opsional
                        return true;
                    } else if (itemId == R.id.nav_profile) {
                        startActivity(new Intent(AdminActivity.this, ProfileActivity.class));
                        // finish(); // Opsional
                        return true;
                    } else if (itemId == R.id.nav_scan) {
                        startActivity(new Intent(AdminActivity.this, ActivityCameraScan.class));
                        // finish(); // Opsional
                        return true;
                    } else if (itemId == R.id.nav_cart) {
                        startActivity(new Intent(AdminActivity.this, ActivityCart.class));
                        // finish(); // Opsional
                        return true;
                    } else if (itemId == R.id.nav_chat) {
                        startActivity(new Intent(AdminActivity.this, ChatActivity.class));
                        // finish(); // Opsional
                        return true;
                    }
                    return false;
                }
            }); // Akhir dari setOnNavigationItemSelectedListener

        // Inisialisasi Views
        buttonKelolaToko = findViewById(R.id.button_kelola_toko);
        buttonKelolaPelanggan = findViewById(R.id.button_kelola_pelanggan);
        tvTotalToko = findViewById(R.id.tv_total_toko); // Gunakan ID dari XML
        tvTotalUser = findViewById(R.id.tv_total_user); // Gunakan ID dari XML

        // Pastikan ID button_kelola_toko dan button_kelola_pelanggan ada di R.layout.activity_admin

        // Set OnClickListener untuk tombol Kelola Toko
        // Set OnClickListener untuk tombol Kelola Toko
        if (buttonKelolaToko != null) {
            buttonKelolaToko.setOnClickListener(v -> {
                Intent intent = new Intent(AdminActivity.this, ManajemenTokoActivity.class);
                startActivity(intent);
            });
        }

        // Set OnClickListener untuk tombol Kelola Pelanggan
        if (buttonKelolaPelanggan != null) {
            buttonKelolaPelanggan.setOnClickListener(v -> {
                Intent intent = new Intent(AdminActivity.this, ManajemenPelangganActivity.class);
                startActivity(intent);
            });
        }

        // Set OnClickListener untuk tombol Kelola Pelanggan
        if (buttonKelolaPelanggan != null) { // Tambahkan null check
            buttonKelolaPelanggan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Ganti ManajemenPelangganActivity.class dengan nama Activity manajemen pelangganmu
                    Intent intent = new Intent(AdminActivity.this, ManajemenPelangganActivity.class);
                    startActivity(intent);
                }
            });
        }
    } // Akhir dari onCreate
    @Override
    protected void onResume() {
        super.onResume();
        // Muat data setiap kali activity ditampilkan agar selalu update
        loadDashboardData();
    }
    private void loadDashboardData() {
        // Panggil metode dari DatabaseHelper
        int totalToko = dbHelper.getTotalToko();
        int totalUser = dbHelper.getTotalUser();

        // Tampilkan hasil ke TextView
        tvTotalToko.setText(String.valueOf(totalToko));
        tvTotalUser.setText(String.valueOf(totalUser));
    }

    private void moveToLogin() {
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Animasi transisi
        finish(); // Tutup AdminActivity agar tidak bisa kembali
    }
}