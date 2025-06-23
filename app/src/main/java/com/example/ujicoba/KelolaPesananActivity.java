package com.example.ujicoba;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log; // Import Log
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView; // Import ScrollView
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
// Import yang mungkin dibutuhkan
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class KelolaPesananActivity extends AppCompatActivity {

    private LinearLayout mainContentLayout;
    private DatabaseHelper dbHelper;
    private BottomNavigationView bottomNavigationView; // Deklarasikan di sini

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Jika Anda ingin menggunakan EdgeToEdge
        setContentView(R.layout.activity_kelola_pesanan);

        // Cek login
        SharedPreferences preferences = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("IS_LOGGED_IN", false);

        if (!isLoggedIn) {
            moveToLogin();
            return; // Penting untuk menghentikan eksekusi jika belum login
        }

        // Inisialisasi DB Helper dan Views
        dbHelper = new DatabaseHelper(this);
        ScrollView scrollView = findViewById(R.id.scrollView);
        // Pastikan LinearLayout di dalam ScrollView memiliki ID atau merupakan anak pertama
        if (scrollView.getChildCount() > 0 && scrollView.getChildAt(0) instanceof LinearLayout) {
            mainContentLayout = (LinearLayout) scrollView.getChildAt(0);
        } else {
            Toast.makeText(this, "Struktur layout ScrollView tidak sesuai untuk Kelola Pesanan.", Toast.LENGTH_LONG).show();
            // finish(); // Mungkin keluar jika layout krusial tidak ada
            // return;
        }

        ImageButton backButton = findViewById(R.id.back_button);
        Button tambahPesananButton = findViewById(R.id.btn_tambahpesanan);
        bottomNavigationView = findViewById(R.id.bottomNavigationView); // Inisialisasi di sini

        backButton.setOnClickListener(v -> finish());

        tambahPesananButton.setOnClickListener(v -> {
            Toast.makeText(this, "Fitur Tambah Pesanan belum diimplementasikan.", Toast.LENGTH_SHORT).show();
        });

        // Setup BottomNavigationView
        // bottomNavigationView.setSelectedItemId(R.id.nav_manage_orders); // Set item aktif jika ada ID-nya

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(KelolaPesananActivity.this, MainActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(KelolaPesananActivity.this, ProfileActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_scan) {
                    startActivity(new Intent(KelolaPesananActivity.this, ActivityCameraScan.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_cart) {
                    startActivity(new Intent(KelolaPesananActivity.this, ActivityCart.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_chat) {
                    startActivity(new Intent(KelolaPesananActivity.this, ChatActivity.class));
                    finish();
                    return true;
                }
                // Tambahkan case lain jika ada
                return false;
            }
        });

        // Muat data pesanan
        if (mainContentLayout != null) {
            loadAndDisplayOrders();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Muat ulang data saat activity kembali aktif
        if (mainContentLayout != null) {
            loadAndDisplayOrders();
        }
    }

    private void loadAndDisplayOrders() {
        // 1. Ambil ID pengguna yang sedang login
        SharedPreferences prefs = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        int currentUserId = prefs.getInt("LOGGED_IN_USER_ID", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Sesi pengguna tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show();
            moveToLogin(); // Langsung arahkan ke login jika sesi tidak valid
            return;
        }

        if (mainContentLayout == null) {
            Log.e("KelolaPesanan", "mainContentLayout is null. Tidak dapat memuat pesanan.");
            return;
        }

        // 2. Kosongkan layout dari data pesanan sebelumnya untuk menghindari duplikasi
        mainContentLayout.removeAllViews();

        // 3. Panggil metode yang benar untuk mengambil pesanan milik pengguna ini
        // BUKAN dbHelper.getAllOrders()
        List<Order> orderList = dbHelper.getOrdersByUserId(currentUserId); // <-- INI PERBAIKAN UTAMA
        LayoutInflater inflater = LayoutInflater.from(this);

        // 4. Cek apakah daftar pesanan kosong
        if (orderList == null || orderList.isEmpty()) {
            // Tampilkan pesan bahwa belum ada pesanan
            TextView emptyMsg = new TextView(this);
            emptyMsg.setText("Anda belum memiliki pesanan.");
            emptyMsg.setGravity(Gravity.CENTER);
            emptyMsg.setPadding(0, 50, 0, 0); // Beri sedikit jarak dari atas
            mainContentLayout.addView(emptyMsg);
        } else {
            // 5. Jika ada pesanan, tampilkan satu per satu
            for (Order order : orderList) {
                if (order == null) continue;

                View orderCardView = inflater.inflate(R.layout.item_card_pesanan_kelola, mainContentLayout, false);

                TextView tvIdPesan = orderCardView.findViewById(R.id.id_pesan_item);
                TextView tvStatusLunas = orderCardView.findViewById(R.id.status_lunas_item);
                TextView tvNamaProduk = orderCardView.findViewById(R.id.nama_produk_item);
                TextView tvDeskripsiProduk = orderCardView.findViewById(R.id.deskripsi_produk_item);
                TextView tvHarga = orderCardView.findViewById(R.id.price_item);
                TextView tvLihatDetail = orderCardView.findViewById(R.id.detail_item);

                tvIdPesan.setText("ID Pesan: " + (order.getOrderNumber() != null ? order.getOrderNumber() : "-"));
                tvStatusLunas.setText(order.getStatus() != null ? order.getStatus() : "Status N/A");

                if ("Lunas".equalsIgnoreCase(order.getStatus())) {
                    tvStatusLunas.setBackgroundResource(R.drawable.bg_status_lunas);
                    tvStatusLunas.setTextColor(Color.WHITE);
                }

                String productSummaryDisplay = order.getProductSummary() != null ? order.getProductSummary() : "Produk tidak tersedia";
                String productDescriptionDisplay = "";

                if (order.getItemsJson() != null && !order.getItemsJson().isEmpty()) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
                    try {
                        ArrayList<CartItem> items = gson.fromJson(order.getItemsJson(), type);
                        if (items != null && !items.isEmpty()) {
                            StringBuilder descBuilder = new StringBuilder();
                            for (int i = 0; i < items.size() && i < 2; i++) { // Tampilkan maks 2 item
                                if (items.get(i) != null && items.get(i).getProduk() != null) {
                                    descBuilder.append(items.get(i).getProduk().getNama());
                                    if (i == 0 && items.size() > 1) descBuilder.append(", ");
                                }
                            }
                            if (items.size() > 2) descBuilder.append("...");
                            productDescriptionDisplay = descBuilder.toString();
                        }
                    } catch (Exception e) {
                        Log.e("KelolaPesanan", "Error parsing itemsJson: " + e.getMessage());
                    }
                }
                if (productDescriptionDisplay.isEmpty()) {
                    productDescriptionDisplay = "Tidak ada deskripsi tambahan.";
                }


                tvNamaProduk.setText(productSummaryDisplay);
                tvDeskripsiProduk.setText(productDescriptionDisplay);


                Locale localeID = new Locale("in", "ID");
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                tvHarga.setText(formatRupiah.format(order.getTotalAmount()));

                final long orderAutoId = order.getOrderAutoId();
                tvLihatDetail.setOnClickListener(v -> {
                    // Hapus Toast yang lama
                    // Toast.makeText(KelolaPesananActivity.this, "Membuka detail pesanan...", Toast.LENGTH_SHORT).show();

                    // Intent ke halaman detail pesanan
                    Intent detailIntent = new Intent(KelolaPesananActivity.this, DetailOrderActivity.class);
                    detailIntent.putExtra("ORDER_AUTO_ID", orderAutoId); // Kirim ID unik pesanan
                    startActivity(detailIntent);
                });

                mainContentLayout.addView(orderCardView);
            }
        }
    }

    private void moveToLogin() {
        Intent intent = new Intent(KelolaPesananActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
