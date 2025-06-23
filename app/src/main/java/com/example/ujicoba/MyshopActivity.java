package com.example.ujicoba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ujicoba.adapters.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MyshopActivity extends AppCompatActivity {

    // Views di header sekarang di-handle di sini, sisanya di fragment
    private TextView tvShopName;
    private Button btnEditProfile;
    private DatabaseHelper dbHelper;
    private int currentUserId;

    // Komponen baru untuk tabs
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshop);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Cek Sesi Login
        SharedPreferences preferences = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("IS_LOGGED_IN", false);
        if (!isLoggedIn) {
            moveToLogin();
            return;
        }
        currentUserId = preferences.getInt("LOGGED_IN_USER_ID", -1);
        if (currentUserId == -1) {
            Toast.makeText(this, "Sesi tidak ditemukan, silakan login kembali.", Toast.LENGTH_LONG).show();
            moveToLogin();
            return;
        }

        // Inisialisasi Views di Activity
        tvShopName = findViewById(R.id.tv_shop_name);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        dbHelper = new DatabaseHelper(this);

        // Inisialisasi Tabs dan ViewPager
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        // Menghubungkan TabLayout dengan ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Toko");
                    break;
                case 1:
                    tab.setText("Produk");
                    break;
                case 2:
                    tab.setText("Kategori");
                    break;
            }
        }).attach();

        // Setup Bottom Navigation
        setupBottomNavigation();

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MyshopActivity.this, EditMyshopActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Memuat data yang hanya ada di header activity
        loadHeaderProfile();
    }

    // Metode ini hanya memuat data untuk header (nama toko)
    private void loadHeaderProfile() {
        Cursor cursor = dbHelper.getUserProfile(currentUserId);
        if (cursor != null && cursor.moveToFirst()) {
            String shopName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME));
            tvShopName.setText(shopName != null && !shopName.isEmpty() ? shopName : "Nama Toko");
            cursor.close();
        } else {
            Toast.makeText(this, "Gagal memuat nama toko.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(MyshopActivity.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(MyshopActivity.this, ProfileActivity.class));
                return true;
            } else if (itemId == R.id.nav_scan) {
                startActivity(new Intent(MyshopActivity.this, ActivityCameraScan.class));
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(MyshopActivity.this, ActivityCart.class));
                return true;
            } else if (itemId == R.id.nav_chat) {
                startActivity(new Intent(MyshopActivity.this, ChatActivity.class));
                return true;
            }
            return false;
        });
    }

    private void moveToLogin() {
        Intent intent = new Intent(MyshopActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}