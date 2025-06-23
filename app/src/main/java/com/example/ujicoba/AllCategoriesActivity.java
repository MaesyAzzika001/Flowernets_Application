package com.example.ujicoba;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
// Hapus Toast jika tidak digunakan lagi
// import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AllCategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);

        // Tombol Kembali
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            // Kembali ke activity sebelumnya
            finish();
        });

        // Setup setiap kategori dengan nama yang sesuai dengan database
        setupCategory(findViewById(R.id.cat_wedding), R.drawable.arch, "Wedding");
        setupCategory(findViewById(R.id.cat_event), R.drawable.placard, "Event");
        setupCategory(findViewById(R.id.cat_graduation), R.drawable.education, "Graduation");
        // Pastikan nama kategori di bawah ini juga ada di database produk Anda
        setupCategory(findViewById(R.id.cat_birthday), R.drawable.hbd, "Birthday");
        setupCategory(findViewById(R.id.cat_bouquet), R.drawable.icon_bouquet, "Bouquet");
        setupCategory(findViewById(R.id.cat_standing_flower), R.drawable.icon_standing, "Standing flower");
        setupCategory(findViewById(R.id.cat_special_day), R.drawable.anniv, "Special Day");
        setupCategory(findViewById(R.id.cat_hampers), R.drawable.hamper, "Hampers");
        setupCategory(findViewById(R.id.cat_custom_request), R.drawable.icon_custom, "Custom Request");

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
                    startActivity(new Intent(AllCategoriesActivity.this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(AllCategoriesActivity.this, ProfileActivity.class));
                    return true;
                } else if (itemId == R.id.nav_scan) {
                    startActivity(new Intent(AllCategoriesActivity.this, ActivityCameraScan.class));
                    return true;
                } else if (itemId == R.id.nav_cart) {
                    startActivity(new Intent(AllCategoriesActivity.this, ActivityCart.class));
                    return true;
                } else if (itemId == R.id.nav_chat) {
                    startActivity(new Intent(AllCategoriesActivity.this, ChatActivity.class));
                    return true;
                }
                return false;
            }
        });
    }

    private void setupCategory(View categoryView, int iconResId, final String categoryName) {
        ImageView icon = categoryView.findViewById(R.id.category_icon);
        TextView nameTextView = categoryView.findViewById(R.id.category_name);

        icon.setImageResource(iconResId);
        nameTextView.setText(categoryName);

        categoryView.setOnClickListener(v -> {
            // --- PERUBAHAN UTAMA ADA DI SINI ---
            // Tidak lagi menampilkan Toast, tapi memanggil metode untuk membuka daftar produk
            openProductListByCategory(categoryName);
        });
    }

    private void openProductListByCategory(String category) {
        Intent intent = new Intent(AllCategoriesActivity.this, ProductListActivity.class);
        // Mengirim nama kategori ke ProductListActivity
        intent.putExtra(ProductListActivity.EXTRA_CATEGORY, category);
        startActivity(intent);
    }

    private void moveToLogin() {
        Intent intent = new Intent(AllCategoriesActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Animasi transisi
        finish(); // Tutup MainActivity agar tidak bisa kembali
    }
}