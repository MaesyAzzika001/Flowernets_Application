package com.example.ujicoba;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View; // Import View
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.Locale;

public class TokoActivity extends AppCompatActivity {
    private LinearLayout buttonProduk;
    private LinearLayout buttonPesanan;
    private LinearLayout buttonPelanggan;
    private LinearLayout buttonLaporan;
    private LinearLayout buttonMyshop; // Tombol baru
    private DatabaseHelper databaseHelper;
    private TextView tvLaporanPenjualan, tvJumlahProduk, tvProdukDilihat, tvPelangganBaru;
    private ImageView productImage, backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_toko);

        databaseHelper = new DatabaseHelper(this);
        tvLaporanPenjualan = findViewById(R.id.card_laporan_penjualan).findViewById(R.id.text_laporan_penjualan_value); // Anda perlu menambahkan ID ini
        tvJumlahProduk = findViewById(R.id.card_jumlah_produk).findViewById(R.id.text_jumlah_produk_value); // Anda perlu menambahkan ID ini
        tvProdukDilihat = findViewById(R.id.card_produk_dilihat).findViewById(R.id.text_produk_dilihat_value); // Anda perlu menambahkan ID ini
        tvPelangganBaru = findViewById(R.id.card_pelanggan_baru).findViewById(R.id.text_pelanggan_baru_value); // Anda perlu menambahkan ID ini
        backButton = findViewById(R.id.back_button);
        loadReportData();

        // Atur listeners
        backButton.setOnClickListener(v -> onBackPressed());
        // Handle WindowInsets for EdgeToEdge
        // Pastikan ID "main_toko_layout" adalah ID root layout di activity_toko.xml Anda
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bottomNavigationView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(TokoActivity.this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(TokoActivity.this, ProfileActivity.class));
                    return true;
                } else if (itemId == R.id.nav_scan) {
                    startActivity(new Intent(TokoActivity.this, ActivityCameraScan.class));
                    return true;
                } else if (itemId == R.id.nav_cart) {
                    startActivity(new Intent(TokoActivity.this, ActivityCart.class));
                    return true;
                } else if (itemId == R.id.nav_chat) {
                    startActivity(new Intent(TokoActivity.this, ChatActivity.class));
                    return true;
                }
                return false;
            }
        });

        // Inisialisasi LinearLayout tombol
        buttonProduk = findViewById(R.id.button_produk);
        buttonPesanan = findViewById(R.id.button_pesanan);
        buttonPelanggan = findViewById(R.id.button_pelanggan);
        buttonLaporan = findViewById(R.id.button_laporan);
        buttonMyshop = findViewById(R.id.button_myshop); // Inisialisasi tombol baru

        // Set OnClickListener untuk tombol Produk
        if (buttonProduk != null) {
            buttonProduk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TokoActivity.this, KelolaProdukActivity.class);
                    startActivity(intent);
                }
            });
        }

        // Set OnClickListener untuk tombol Pesanan
        if (buttonPesanan != null) {
            buttonPesanan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TokoActivity.this, KelolaPesananActivity.class);
                    startActivity(intent);
                }
            });
        }

        // Set OnClickListener untuk tombol Pelanggan
        if (buttonPelanggan != null) {
            buttonPelanggan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TokoActivity.this, KelolaPelangganActivity.class);
                    startActivity(intent);
                }
            });
        }

        // Set OnClickListener untuk tombol Laporan
        if (buttonLaporan != null) {
            buttonLaporan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TokoActivity.this, LaporanPenjualanActivity.class);
                    startActivity(intent);
                }
            });
        }

        // Set OnClickListener untuk tombol My Shop (BARU)
        if (buttonMyshop != null) {
            buttonMyshop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TokoActivity.this, MyshopActivity.class); // Ganti dengan MyshopActivity.class
                    startActivity(intent);
                }
            });
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Panggil lagi di onResume agar data selalu ter-update saat kembali ke halaman ini
        loadReportData();
    }
    private void moveToLogin() {
        Intent intent = new Intent(TokoActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    private void loadReportData() {
        // HAPUS super.onResume() dan panggilan rekursif dari sini

        // Ambil semua data dari database
        double totalRevenue = databaseHelper.getTotalRevenue();
        int productsSold = databaseHelper.getTotalProductsSoldCount();
        int productViews = databaseHelper.getTotalProductViews();
        int newCustomers = databaseHelper.getNewCustomerCountThisMonth();

        // Tampilkan hasil ke Logcat untuk debugging
        Log.d("TokoActivity", "Total Revenue from DB: " + totalRevenue);
        Log.d("TokoActivity", "Products Sold from DB: " + productsSold);

        // Format angka menjadi format mata uang Rupiah
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        formatRupiah.setMaximumFractionDigits(0);

        // Tampilkan data ke TextViews
        tvLaporanPenjualan.setText(formatRupiah.format(totalRevenue));
        tvJumlahProduk.setText(String.valueOf(productsSold));
        tvProdukDilihat.setText(String.valueOf(productViews));
        tvPelangganBaru.setText(String.valueOf(newCustomers));
    }


}
