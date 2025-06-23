// ActivityCart.java
package com.example.ujicoba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ActivityCart extends AppCompatActivity implements CartAdapter.CartInteractionListener {

    private RecyclerView rvCartItems;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private DatabaseHelper databaseHelper;

    private TextView tvTotalItemsCheckout;
    private TextView tvTotalPriceCheckout;
    private Button btnCheckout;
    private ImageButton btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this); // Hapus atau sesuaikan jika menyebabkan masalah layout
        setContentView(R.layout.activity_cart);

        SharedPreferences preferences = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("IS_LOGGED_IN", false);

        if (!isLoggedIn) {
            moveToLogin();
            return;
        }

        databaseHelper = new DatabaseHelper(this);

        // Inisialisasi Views
        rvCartItems = findViewById(R.id.rv_cart_items);
        tvTotalItemsCheckout = findViewById(R.id.tv_total_items_checkout);
        tvTotalPriceCheckout = findViewById(R.id.tv_total_price_checkout);
        btnCheckout = findViewById(R.id.btn_checkout);
        btnBack = findViewById(R.id.btn_back);


        // Setup RecyclerView
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartItemList, this);
        rvCartItems.setAdapter(cartAdapter);

        loadCartItems();

        btnCheckout.setOnClickListener(v -> {
            // Logika untuk proses checkout
            // Misalnya, pindah ke activity pembayaran dengan membawa data item yang di-checklist
            List<CartItem> checkedItems = new ArrayList<>();
            double totalCheckedPrice = 0;
            for (CartItem item : cartItemList) {
                if (item.isChecked()) {
                    checkedItems.add(item);
                    totalCheckedPrice += item.getTotalPrice();
                }
            }
            if (checkedItems.isEmpty()) {
                Toast.makeText(this, "Pilih item untuk di-checkout.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Checkout " + checkedItems.size() + " item(s) dengan total " + formatRupiah(totalCheckedPrice), Toast.LENGTH_LONG).show();
                // Intent ke activity checkout selanjutnya
            }
        });

        btnBack.setOnClickListener(v -> {
            // Kembali ke MainActivity atau activity sebelumnya
            // Jika ActivityCart selalu dibuka dari MainActivity:
            // startActivity(new Intent(ActivityCart.this, MainActivity.class));
            // finish();
            // Atau lebih simple:
            onBackPressed(); // Ini akan memanggil finish() jika tidak ada stack di atasnya
        });


        // Inisialisasi BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_cart); // Set cart sebagai item aktif

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(ActivityCart.this, MainActivity.class));
                finish(); // Tutup activity ini
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(ActivityCart.this, ProfileActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_scan) {
                startActivity(new Intent(ActivityCart.this, ActivityCameraScan.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_cart) {
                // Sudah di halaman cart
                return true;
            } else if (itemId == R.id.nav_chat) {
                startActivity(new Intent(ActivityCart.this, ChatActivity.class));
                finish();
                return true;
            }
            return false;
        });

        btnCheckout.setOnClickListener(v -> {
            ArrayList<CartItem> checkedItems = new ArrayList<>(); // Gunakan ArrayList untuk Parcelable
            double totalCheckedPrice = 0;

            for (CartItem item : cartItemList) {
                if (item.isChecked()) {
                    checkedItems.add(item);
                    totalCheckedPrice += item.getTotalPrice();
                }
            }

            if (checkedItems.isEmpty()) {
                Toast.makeText(this, "Pilih item untuk di-checkout.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ActivityCart.this, CheckoutActivity.class);
                intent.putParcelableArrayListExtra("CHECKED_ITEMS", checkedItems);
                // Anda juga bisa mengirim total harga jika dihitung di sini,
                // atau biarkan CheckoutActivity menghitungnya ulang.
                // intent.putExtra("TOTAL_PRICE", totalCheckedPrice);
                startActivity(intent);
            }
        });
    }

    private void loadCartItems() {
        cartItemList.clear();
        cartItemList.addAll(databaseHelper.getCartItems());
        cartAdapter.notifyDataSetChanged(); // Atau gunakan cartAdapter.updateCartItems(databaseHelper.getCartItems());
        updateCheckoutSummary();
    }

    private void updateCheckoutSummary() {
        int totalItems = 0;
        double totalPrice = 0;

        for (CartItem item : cartItemList) {
            if (item.isChecked()) {
                totalItems += item.getQuantity(); // Hitung semua kuantitas dari item yang tercentang
                totalPrice += item.getTotalPrice();
            }
        }

        tvTotalItemsCheckout.setText(totalItems + "x");
        tvTotalPriceCheckout.setText(formatRupiah(totalPrice));
    }

    private String formatRupiah(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(amount);
    }

    private void moveToLogin() {
        Intent intent = new Intent(ActivityCart.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    // Implementasi CartAdapter.CartInteractionListener
    @Override
    public void onCartUpdated() {
        updateCheckoutSummary();
    }

    @Override
    public void onItemRemoved(int position) {
        if (position >= 0 && position < cartItemList.size()) {
            // Hapus dari list di Activity, adapter akan di-notify dari DB operation yang sukses
            // atau refresh dari DB
            // cartItemList.remove(position); // Hapus dari list lokal
            // cartAdapter.notifyItemRemoved(position); // Notify adapter
            // cartAdapter.notifyItemRangeChanged(position, cartItemList.size()); // Update posisi
            // Lebih aman untuk reload dari DB untuk memastikan konsistensi
            loadCartItems(); // Ini akan mengambil data terbaru dan memperbarui summary
            Toast.makeText(this, "Item dihapus dari keranjang", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Muat ulang item keranjang setiap kali activity ini ditampilkan
        // untuk memastikan data selalu terbaru, misalnya setelah menambah dari MainActivity
        loadCartItems();
    }
}