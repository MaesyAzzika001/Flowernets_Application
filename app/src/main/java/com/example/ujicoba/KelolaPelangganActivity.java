package com.example.ujicoba;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
// Import yang mungkin dibutuhkan
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class KelolaPelangganActivity extends AppCompatActivity {

    private LinearLayout mainContentLayoutPelanggan;
    private DatabaseHelper dbHelper;
    private BottomNavigationView bottomNavigationView; // Deklarasikan di sini

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Jika Anda ingin menggunakan EdgeToEdge
        setContentView(R.layout.activity_kelola_pelanggan);

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
            mainContentLayoutPelanggan = (LinearLayout) scrollView.getChildAt(0);
        } else {
            // Fallback atau error handling jika struktur layout tidak sesuai
            // Misalnya, Anda bisa membuat LinearLayout secara programatik dan menambahkannya ke ScrollView
            // atau pastikan XML Anda memiliki LinearLayout sebagai anak langsung dari ScrollView.
            // Untuk sekarang, kita asumsikan XML sudah benar.
            // Jika mainContentLayoutPelanggan tetap null, akan ada NullPointerException di loadAndDisplayCustomers.
            // Anda bisa menambahkan pengecekan null sebelum menggunakannya.
            Toast.makeText(this, "Struktur layout ScrollView tidak sesuai.", Toast.LENGTH_LONG).show();
            // finish(); // Mungkin keluar jika layout krusial tidak ada
            // return;
        }


        ImageButton backButton = findViewById(R.id.back_button);
        bottomNavigationView = findViewById(R.id.bottomNavigationView); // Inisialisasi di sini

        backButton.setOnClickListener(v -> finish());

        // Setup BottomNavigationView
        // Pastikan item menu untuk Kelola Pelanggan ada dan memiliki ID yang benar
        // Misalnya R.id.nav_manage_customers
        // bottomNavigationView.setSelectedItemId(R.id.nav_manage_customers); // Set item aktif jika ada

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(KelolaPelangganActivity.this, MainActivity.class));
                    finish(); // Tutup activity ini agar tidak menumpuk
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(KelolaPelangganActivity.this, ProfileActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_scan) {
                    startActivity(new Intent(KelolaPelangganActivity.this, ActivityCameraScan.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_cart) {
                    startActivity(new Intent(KelolaPelangganActivity.this, ActivityCart.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_chat) {
                    startActivity(new Intent(KelolaPelangganActivity.this, ChatActivity.class));
                    finish();
                    return true;
                }
                // Tambahkan case lain jika ada, misal untuk Kelola Produk, Kelola Pesanan
                // else if (itemId == R.id.nav_manage_products) {
                //     startActivity(new Intent(KelolaPelangganActivity.this, KelolaProdukActivity.class));
                //     finish();
                //     return true;
                // }
                return false;
            }
        });

        // Muat data pelanggan
        if (mainContentLayoutPelanggan != null) {
            loadAndDisplayCustomers();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Muat ulang data saat activity kembali aktif
        if (mainContentLayoutPelanggan != null) {
            loadAndDisplayCustomers();
        }
    }

    private void loadAndDisplayCustomers() {
        if (mainContentLayoutPelanggan == null) {
            // Jika layout utama tidak terinisialisasi, jangan lakukan apa-apa untuk menghindari NPE
            Log.e("KelolaPelanggan", "mainContentLayoutPelanggan is null in loadAndDisplayCustomers");
            return;
        }
        // Hapus view pelanggan yang sudah ada (hanya MaterialCardView)
        List<View> viewsToRemove = new ArrayList<>();
        for (int i = 0; i < mainContentLayoutPelanggan.getChildCount(); i++) {
            View child = mainContentLayoutPelanggan.getChildAt(i);
            // Pastikan hanya menghapus CardView yang merupakan item pelanggan,
            // bukan search bar atau tombol lain jika ada di dalam mainContentLayoutPelanggan
            if (child instanceof MaterialCardView) {
                viewsToRemove.add(child);
            }
        }
        for (View view : viewsToRemove) {
            mainContentLayoutPelanggan.removeView(view);
        }

        List<Order> allOrders = dbHelper.getAllOrders();
        LayoutInflater inflater = LayoutInflater.from(this);

        Map<String, List<Order>> customerOrdersMap = new HashMap<>();
        if (allOrders != null) { // Tambahkan null check untuk allOrders
            for (Order order : allOrders) {
                // Pastikan customerName tidak null untuk menghindari NPE saat menjadi key HashMap
                String customerNameKey = order.getCustomerName() != null ? order.getCustomerName() : "Pelanggan Tidak Dikenal";
                customerOrdersMap.computeIfAbsent(customerNameKey, k -> new ArrayList<>()).add(order);
            }
        }


        if (customerOrdersMap.isEmpty()) {
            // Cek apakah pesan "belum ada data" sudah ada, jika belum, tambahkan
            boolean messageExists = false;
            for (int i = 0; i < mainContentLayoutPelanggan.getChildCount(); i++) {
                if (mainContentLayoutPelanggan.getChildAt(i) instanceof TextView &&
                        ((TextView) mainContentLayoutPelanggan.getChildAt(i)).getText().toString().equals("Belum ada data pelanggan dari pesanan.")) {
                    messageExists = true;
                    break;
                }
            }
            if (!messageExists) {
                TextView emptyMsg = new TextView(this);
                emptyMsg.setText("Belum ada data pelanggan dari pesanan.");
                emptyMsg.setPadding(16, 32, 16, 16); // Tambahkan padding
                emptyMsg.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
                mainContentLayoutPelanggan.addView(emptyMsg);
            }
        } else {
            for (Map.Entry<String, List<Order>> entry : customerOrdersMap.entrySet()) {
                String customerName = entry.getKey();
                List<Order> ordersByCustomer = entry.getValue();

                if (ordersByCustomer.isEmpty()) continue;

                Order lastOrder = ordersByCustomer.get(0); // Pesanan terbaru

                View customerCardView = inflater.inflate(R.layout.item_card_pelanggan_kelola, mainContentLayoutPelanggan, false);

                TextView tvNoTransaksi = customerCardView.findViewById(R.id.no_transaksi_pelanggan_item);
                TextView tvNamaKontak = customerCardView.findViewById(R.id.nama_kontak_pelanggan_item);
                TextView tvEmail = customerCardView.findViewById(R.id.email_pelanggan_item);
                TextView tvWaktuPembayaran = customerCardView.findViewById(R.id.tanggal_waktu_pelanggan_item);
                // TextView tvTotalTransaksiLabel = customerCardView.findViewById(R.id.total_transaksi_pelanggan_item); // Jika ini label
                TextView tvHargaTotal = customerCardView.findViewById(R.id.price_total_pelanggan_item);

                tvNoTransaksi.setText("No. Transaksi Terakhir: " + (lastOrder.getOrderNumber() != null ? lastOrder.getOrderNumber() : "-"));
                tvNamaKontak.setText(customerName);
                tvEmail.setText("email_pelanggan@contoh.com"); // Placeholder, ganti dengan data asli jika ada
                tvWaktuPembayaran.setText(lastOrder.getPaymentTimestamp() != null ? lastOrder.getPaymentTimestamp() : "-");

                double totalSpending = 0;
                for (Order o : ordersByCustomer) {
                    totalSpending += o.getTotalAmount();
                }
                Locale localeID = new Locale("in", "ID");
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                tvHargaTotal.setText(formatRupiah.format(totalSpending));

                customerCardView.setOnClickListener(v -> {
                    Toast.makeText(KelolaPelangganActivity.this, "Detail Pelanggan: " + customerName, Toast.LENGTH_SHORT).show();
                    // Intent ke detail pelanggan jika ada
                });

                mainContentLayoutPelanggan.addView(customerCardView);
            }
        }
    }

    private void moveToLogin() {
        Intent intent = new Intent(KelolaPelangganActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Bersihkan stack activity
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
