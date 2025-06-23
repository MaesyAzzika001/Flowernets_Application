package com.example.ujicoba;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
// import android.os.Handler; // Tidak digunakan secara langsung di versi ini, kecuali untuk delay tambahan
import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import android.content.SharedPreferences;

// Locale sudah diimpor dari java.util.Locale

public class CheckoutActivity extends AppCompatActivity {

    private LinearLayout layoutProdukItemsCheckout;
    private TextView tvSubtotalProdukCheckout, tvSubtotalPengirimanCheckout;
    private TextView tvVoucherDiskonCheckout, tvTotalPembayaranCheckout;
    private TextView tvStoreNameCheckout;
    private DatabaseHelper dbHelper;

    private ArrayList<CartItem> checkedItems;
    private double subtotalProduk = 0;
    private double subtotalPengiriman = 0; // Atur ini berdasarkan logika pengiriman Anda
    private double voucherDiskon = 0;    // Atur ini berdasarkan logika voucher Anda
    private int loggedInUserId = -1; // Default value

    private RelativeLayout layoutPengiriman;
    private TextView tvOpsiPengiriman, tvOpsiPengirimanDetail;

    private RelativeLayout layoutAlamat;
    private TextView tvAlamatPengguna; // Tambahkan ID untuk TextView ini di XML
    private Address selectedAddress;

    private RelativeLayout layoutMetodePembayaran;
    private TextView tvMetodePembayaranInfo; // Beri ID ini pada TextView di XML
    private String selectedPaymentMethod = "Pilih Metode"; // Default

    // Tambahkan request code baru
    private static final int REQUEST_CODE_PAYMENT = 103;

    // Definisikan request code
    private static final int REQUEST_CODE_SHIPPING = 101;
    private String selectedShippingOption = "Ambil di toko"; // Default

    private static final int REQUEST_CODE_ADD_ADDRESS = 102;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        dbHelper = new DatabaseHelper(this);
        // Ambil ID user yang sedang login
        // Anda harus memiliki sistem sesi, contoh menggunakan SharedPreferences
        SharedPreferences prefs = getSharedPreferences("USER_PREF", MODE_PRIVATE); // <-- GUNAKAN NAMA YANG SAMA
        loggedInUserId = prefs.getInt("LOGGED_IN_USER_ID", -1); // <-- AMBIL USER ID YANG DISIMPAN
        String userEmail = prefs.getString("user_email", null);
        if (userEmail != null) {
            loggedInUserId = dbHelper.getUserId(userEmail);
        }
        // Inisialisasi Views (HANYA SEKALI)
        ImageButton btnBack = findViewById(R.id.btn_back_checkout);
        Button btnBeli = findViewById(R.id.btn_beli_checkout);

        layoutPengiriman = findViewById(R.id.layout_pengiriman_checkout);

        // Asumsi di dalam layout_pengiriman_checkout, ada TextView untuk menampilkan detail
        // Anda mungkin perlu menambahkan ID untuk TextView "Ambil di toko" di XML Anda
        // tvOpsiPengirimanDetail = findViewById(R.id.tv_opsi_pengiriman_detail);

        layoutPengiriman.setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutActivity.this, ShippingOptionsActivity.class);
            intent.putExtra("CURRENT_SHIPPING_OPTION", selectedShippingOption);
            startActivityForResult(intent, REQUEST_CODE_SHIPPING);
        });

        layoutAlamat = findViewById(R.id.layout_alamat_checkout);
        // Anda perlu TextView di dalam layout_alamat_checkout untuk menampilkan alamatnya
        // Misalnya dengan ID tv_alamat_pengguna
        // tvAlamatPengguna = findViewById(R.id.tv_alamat_pengguna);

        layoutAlamat.setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutActivity.this, AddAddressActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS);
        });

        layoutMetodePembayaran = findViewById(R.id.layout_metode_pembayaran_checkout);
        tvMetodePembayaranInfo = findViewById(R.id.tv_metode_pembayaran_info);
        tvMetodePembayaranInfo.setText(selectedPaymentMethod); // Set teks default

        layoutMetodePembayaran.setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutActivity.this, PaymentMethodActivity.class);
            intent.putExtra("CURRENT_PAYMENT_METHOD", selectedPaymentMethod);
            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
        });
        layoutProdukItemsCheckout = findViewById(R.id.layout_produk_items_checkout);
        tvSubtotalProdukCheckout = findViewById(R.id.tv_subtotal_produk_checkout);
        tvSubtotalPengirimanCheckout = findViewById(R.id.tv_subtotal_pengiriman_checkout);
        tvVoucherDiskonCheckout = findViewById(R.id.tv_voucher_diskon_checkout);
        tvTotalPembayaranCheckout = findViewById(R.id.tv_total_pembayaran_checkout);
        tvStoreNameCheckout = findViewById(R.id.tv_store_name_checkout);

        // Contoh statis nama toko, bisa diubah jika ada data toko
        // tvStoreNameCheckout.setText("by Toko Bunga Anda");

        btnBack.setOnClickListener(v -> finish()); // Kembali ke activity sebelumnya

        btnBeli.setOnClickListener(v -> {
            if (loggedInUserId == -1) {
                Toast.makeText(CheckoutActivity.this, "Sesi pengguna tidak valid. Silakan login kembali.", Toast.LENGTH_LONG).show();
                // Arahkan ke halaman login jika perlu
                return;
            }

            // 1. Generate Order Number
            String orderNumber = generateOrderNumber();

            // 2. Prepare Order Data
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            String currentTimestamp = sdf.format(new Date());

            String productSummary = "";
            if (!checkedItems.isEmpty() && checkedItems.get(0).getProduk() != null) {
                productSummary = checkedItems.get(0).getProduk().getNama();
                if (checkedItems.size() > 1) {
                    productSummary += " dan " + (checkedItems.size() - 1) + " lainnya";
                }
            } else {
                productSummary = "Beberapa item"; // Fallback jika produk pertama null
            }


            Gson gson = new Gson();
            String itemsJson = gson.toJson(checkedItems);

            Order newOrder = new Order();
            newOrder.setUserId(loggedInUserId); // <-- SET USER ID DI SINI
            newOrder.setOrderNumber(orderNumber);
            newOrder.setCustomerName("Pelanggan");
            newOrder.setProductSummary(productSummary);
            newOrder.setItemsJson(itemsJson);
            // Ambil total dari calculateTotals yang sudah memperhitungkan subtotalProduk, pengiriman, dan diskon
            double finalTotalAmount = subtotalProduk + subtotalPengiriman - voucherDiskon;
            newOrder.setTotalAmount(finalTotalAmount);
            newOrder.setOrderTimestamp(currentTimestamp);
            newOrder.setPaymentTimestamp(currentTimestamp);
            newOrder.setStatus("Lunas");

            long newOrderAutoId = dbHelper.insertOrder(newOrder);

            if (newOrderAutoId != -1) {
                dbHelper.clearCart();

                Intent successIntent = new Intent(CheckoutActivity.this, ActivityPaymentSuccess.class);
                successIntent.putExtra("ORDER_NUMBER", orderNumber);
                successIntent.putExtra("ORDER_AUTO_ID", newOrderAutoId);
                startActivity(successIntent);
                finish();
            } else {
                Toast.makeText(CheckoutActivity.this, "Gagal menyimpan pesanan.", Toast.LENGTH_SHORT).show();
            }
        });

        // Menerima data dari Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("CHECKED_ITEMS")) {
            checkedItems = intent.getParcelableArrayListExtra("CHECKED_ITEMS");
            if (checkedItems != null && !checkedItems.isEmpty()) {
                displayCheckedItems(); // Ini akan mengkalkulasi subtotalProduk
                calculateTotals();     // Ini akan menggunakan subtotalProduk untuk total akhir
            } else {
                Toast.makeText(this, "Tidak ada item untuk di-checkout.", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Error: Data checkout tidak ditemukan.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void displayCheckedItems() {
        if (layoutProdukItemsCheckout == null) return; // Guard clause
        layoutProdukItemsCheckout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        subtotalProduk = 0; // Reset subtotalProduk setiap kali item ditampilkan ulang

        if (checkedItems == null || checkedItems.isEmpty()) return;

        // Contoh: Mengambil nama toko dari item pertama (asumsi semua dari toko yg sama)
        // atau Anda bisa meneruskan info toko secara terpisah
        // if (tvStoreNameCheckout != null && checkedItems.get(0).getProduk() != null) {
        //     // Logika untuk mendapatkan nama toko
        // }

        for (CartItem item : checkedItems) {
            Produk produk = item.getProduk();
            if (produk == null) continue;

            View itemView = inflater.inflate(R.layout.item_checkout_product_display, layoutProdukItemsCheckout, false);

            ImageView ivProduct = itemView.findViewById(R.id.iv_product_checkout_item);
            TextView tvProductName = itemView.findViewById(R.id.tv_product_name_checkout_item);
            TextView tvProductDesc = itemView.findViewById(R.id.tv_product_details_checkout_item);
            TextView tvProductPriceTotal = itemView.findViewById(R.id.tv_product_price_total_checkout_item);

            tvProductName.setText(produk.getNama() != null ? produk.getNama() : "Nama Produk Tidak Tersedia");
            String hargaProdukStr = produk.getHarga();
            double hargaProdukDouble = 0;
            if (hargaProdukStr != null) {
                try {
                    hargaProdukDouble = Double.parseDouble(hargaProdukStr);
                } catch (NumberFormatException e) {
                    // Handle error parsing, mungkin log atau set harga default
                }
            }
            tvProductDesc.setText(item.getQuantity() + " x " + formatRupiah(hargaProdukDouble));
            tvProductPriceTotal.setText(formatRupiah(item.getTotalPrice()));

            String imagePathOrName = produk.getGambar();
            if (imagePathOrName != null && !imagePathOrName.isEmpty()) {
                File imageFile = new File(imagePathOrName);
                if (imageFile.exists() && imageFile.isFile()) {
                    Glide.with(this).load(imageFile)
                            .placeholder(R.drawable.chinese) // Ganti dengan placeholder Anda
                            .error(R.drawable.chinese)       // Ganti dengan error image Anda
                            .into(ivProduct);
                } else {
                    int imageResId = getResources().getIdentifier(imagePathOrName, "drawable", getPackageName());
                    if (imageResId != 0) {
                        Glide.with(this).load(imageResId)
                                .placeholder(R.drawable.chinese)
                                .error(R.drawable.chinese)
                                .into(ivProduct);
                    } else {
                        ivProduct.setImageResource(R.drawable.chinese); // Fallback
                    }
                }
            } else {
                ivProduct.setImageResource(R.drawable.chinese); // Fallback
            }
            layoutProdukItemsCheckout.addView(itemView);
            subtotalProduk += item.getTotalPrice(); // Akumulasi subtotal produk di sini
        }
    }

    private void calculateTotals() {
        // subtotalProduk sudah dihitung di displayCheckedItems()
        if (tvSubtotalProdukCheckout != null) {
            tvSubtotalProdukCheckout.setText(formatRupiah(subtotalProduk));
        }

        // Implementasikan logika untuk biaya pengiriman dan diskon voucher jika ada
        // Untuk contoh, kita set default 0
        subtotalPengiriman = 0;
        voucherDiskon = 0;

        if (tvSubtotalPengirimanCheckout != null) {
            tvSubtotalPengirimanCheckout.setText(formatRupiah(subtotalPengiriman));
        }
        if (tvVoucherDiskonCheckout != null) {
            tvVoucherDiskonCheckout.setText(formatRupiah(voucherDiskon, true));
        }

        double totalPembayaran = subtotalProduk + subtotalPengiriman - voucherDiskon;
        if (tvTotalPembayaranCheckout != null) {
            tvTotalPembayaranCheckout.setText(formatRupiah(totalPembayaran));
        }
    }

    private String formatRupiah(double amount) {
        return formatRupiah(amount, false);
    }

    private String formatRupiah(double amount, boolean isDiscount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String formatted = formatter.format(amount);
        if (isDiscount && amount > 0) {
            return "- " + formatted;
        } else if (isDiscount && amount == 0) { // Untuk diskon 0, tampilkan Rp0,- atau "-"
            return "Rp0,-";
        }
        return formatted;
    }

    private String generateOrderNumber() {
        Random random = new Random();
        int number = 1000000 + random.nextInt(9000000);
        return String.format(Locale.getDefault(), "%07d", number);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SHIPPING && resultCode == RESULT_OK) {
            if (data != null) {
                selectedShippingOption = data.getStringExtra("SELECTED_SHIPPING_OPTION");
                subtotalPengiriman = data.getDoubleExtra("SELECTED_SHIPPING_COST", 0.0);

                // Update UI
                // Pastikan TextView untuk opsi pengiriman memiliki ID
                // tvOpsiPengirimanDetail.setText(selectedShippingOption);

                Toast.makeText(this, "Opsi pengiriman: " + selectedShippingOption, Toast.LENGTH_SHORT).show();

                // Hitung ulang total pembayaran
                calculateTotals();
            }
        } else if (requestCode == REQUEST_CODE_ADD_ADDRESS && resultCode == RESULT_OK) {
            if (data != null) {
                selectedAddress = data.getParcelableExtra("NEW_ADDRESS");
                if (selectedAddress != null) {
                    // Update UI untuk menampilkan alamat baru
                    Toast.makeText(this, "Alamat dipilih: " + selectedAddress.getRecipientName(), Toast.LENGTH_SHORT).show();

                    // Contoh update UI (Anda harus punya TextView untuk ini di XML)
                    // tvAlamatPengguna.setText(selectedAddress.getRecipientName() + "\n" + selectedAddress.getStreetAddress());

                    // Juga update nama customer di order jika perlu
                    // Hal ini dilakukan saat tombol "Beli" ditekan
                }
            }
        }
        if (requestCode == REQUEST_CODE_PAYMENT && resultCode == RESULT_OK) {
            if (data != null) {
                selectedPaymentMethod = data.getStringExtra("SELECTED_PAYMENT_METHOD");
                tvMetodePembayaranInfo.setText(selectedPaymentMethod); // Update UI
                Toast.makeText(this, "Metode pembayaran: " + selectedPaymentMethod, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
