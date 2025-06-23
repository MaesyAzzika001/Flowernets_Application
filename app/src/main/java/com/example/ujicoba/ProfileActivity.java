package com.example.ujicoba;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        setupMenu(R.id.menu_edit_profile, R.drawable.iconamoon_profile_light, "Edit Profile", EditProfileActivity.class);
        setupMenu(R.id.menu_security, R.drawable.material_symbols_privacy_tip_outline, "Security", NotFoundActivity.class);
        setupMenu(R.id.menu_order_history, R.drawable.vector, "Riwayat Pesanan", KelolaPesananActivity.class);
        setupMenu(R.id.menu_privacy, R.drawable.ic_outline_lock, "Privacy", NotFoundActivity.class);
        setupMenu(R.id.menu_voucher, R.drawable.material_symbols_credit_card_outline, "My Voucher", NotFoundActivity.class);
        setupMenu(R.id.menu_help, R.drawable.mdi_question_mark_circle_outline, "Help & Support", NotFoundActivity.class);
        setupMenu(R.id.menu_terms, R.drawable.tabler_circle_letter_i, "Terms and Policies", NotFoundActivity.class);
        setupMenu(R.id.menu_my_shop, R.drawable.toko, "Toko", TokoActivity.class);
        setupMenu(R.id.menu_report_problem, R.drawable.ic_sharp_outlined_flag, "Report a Problem", NotFoundActivity.class);
        setupMenu(R.id.menu_add_account, R.drawable.ic_sharp_people_outline, "Admin", AdminActivity.class);

// Menu Logout
        LinearLayout menuLogout = findViewById(R.id.menu_logout);
        ImageView iconLogout = menuLogout.findViewById(R.id.menu_icon);
        TextView textLogout = menuLogout.findViewById(R.id.menu_text);
        iconLogout.setImageResource(R.drawable.back); // ganti dengan icon logout
        textLogout.setText("Logout");
        menuLogout.setOnClickListener(v -> logoutUser());


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
                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                    return true;
                }else if (itemId == R.id.nav_scan) {
                    startActivity(new Intent(ProfileActivity.this, ActivityCameraScan.class));
                    return true;
                }else if (itemId == R.id.nav_cart) {
                    startActivity(new Intent(ProfileActivity.this, ActivityCart.class));
                    return true;
                }else if (itemId == R.id.nav_chat) {
                    startActivity(new Intent(ProfileActivity.this, ChatActivity.class));
                    return true;
                }
                return false;
            }
        });


    }

    private void setupMenu(int layoutId, int iconRes, String title, Class<?> targetActivity) {
        LinearLayout menu = findViewById(layoutId);
        ImageView icon = menu.findViewById(R.id.menu_icon);
        TextView text = menu.findViewById(R.id.menu_text);
        icon.setImageResource(iconRes);
        text.setText(title);
        menu.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, targetActivity);
            startActivity(intent);
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
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Animasi transisi
        finish(); // Tutup MainActivity agar tidak bisa kembali

    }
}