package com.example.ujicoba;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DetailOrderActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Order currentOrder;

    // Deklarasi semua View
    private TextView tvDeliveryInfo, tvDeliveryStatus, tvDeliveryTimestamp;
    private TextView tvCustomerNamePhone, tvCustomerAddress;
    private TextView tvDetailOrderNumber, tvDetailPaymentMethod, tvDetailProductName, tvDetailOrderTime, tvDetailPaymentTime;
    private LinearLayout layoutProductItems;
    private ImageButton btnBack;
    private Button btnBuyAgain, btnRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

        dbHelper = new DatabaseHelper(this);
        findViewsById();

        long orderId = getIntent().getLongExtra("ORDER_AUTO_ID", -1);

        if (orderId == -1) {
            Toast.makeText(this, "Error: ID Pesanan tidak valid.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentOrder = dbHelper.getOrderById(orderId);

        if (currentOrder == null) {
            Toast.makeText(this, "Error: Pesanan tidak ditemukan.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        populateUi();

        btnBack.setOnClickListener(v -> finish());
        btnBuyAgain.setOnClickListener(v -> Toast.makeText(this, "Fitur Beli Lagi belum tersedia", Toast.LENGTH_SHORT).show());
        btnRate.setOnClickListener(v -> {
            // Toast.makeText(this, "Fitur Nilai belum tersedia", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DetailOrderActivity.this, RatingActivity.class);
            intent.putExtra("ORDER_ID", currentOrder.getOrderAutoId());
            startActivity(intent);
        });

    }

    private void findViewsById() {
        // Info Pengiriman
        tvDeliveryInfo = findViewById(R.id.tv_delivery_info);
        tvDeliveryStatus = findViewById(R.id.tv_delivery_status);
        tvDeliveryTimestamp = findViewById(R.id.tv_delivery_timestamp);
        tvCustomerNamePhone = findViewById(R.id.tv_customer_name_phone);
        tvCustomerAddress = findViewById(R.id.tv_customer_address);

        // Waktu Pesan
        tvDetailOrderNumber = findViewById(R.id.tv_detail_order_number);
        tvDetailPaymentMethod = findViewById(R.id.tv_detail_payment_method);
        tvDetailProductName = findViewById(R.id.tv_detail_product_name);
        tvDetailOrderTime = findViewById(R.id.tv_detail_order_time);
        tvDetailPaymentTime = findViewById(R.id.tv_detail_payment_time);

        // Produk
        layoutProductItems = findViewById(R.id.layout_product_items);

        // Tombol
        btnBack = findViewById(R.id.btn_back_detail);
        btnBuyAgain = findViewById(R.id.btn_buy_again);
        btnRate = findViewById(R.id.btn_rate);
    }

    private void populateUi() {
        // Isi data pengiriman & alamat (contoh data statis, sesuaikan dengan data Anda)
        tvDeliveryInfo.setText("LalaMove: " + currentOrder.getOrderNumber());
        tvDeliveryStatus.setText("Pesanan " + currentOrder.getStatus());
        tvDeliveryTimestamp.setText(currentOrder.getPaymentTimestamp()); // Asumsi waktu tiba = waktu bayar
        tvCustomerNamePhone.setText(currentOrder.getCustomerName()); // Asumsi nama pelanggan
        tvCustomerAddress.setText("Alamat Pengiriman Belum Tersedia"); // Anda perlu menambahkan kolom alamat jika ada

        // Isi data Waktu Pesan
        tvDetailOrderNumber.setText(currentOrder.getOrderNumber());
        tvDetailPaymentMethod.setText("Dana"); // Contoh statis
        tvDetailProductName.setText(currentOrder.getProductSummary());
        tvDetailOrderTime.setText(currentOrder.getOrderTimestamp());
        tvDetailPaymentTime.setText(currentOrder.getPaymentTimestamp());

        // Isi data Produk dari JSON
        populateProductItems();
    }

    private void populateProductItems() {
        layoutProductItems.removeAllViews(); // Kosongkan dulu
        LayoutInflater inflater = LayoutInflater.from(this);

        if (currentOrder.getItemsJson() != null && !currentOrder.getItemsJson().isEmpty()) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
            ArrayList<CartItem> items = gson.fromJson(currentOrder.getItemsJson(), type);

            if (items != null && !items.isEmpty()) {
                for (CartItem item : items) {
                    // Anda perlu membuat layout item terpisah, misal `item_detail_product.xml`
                    // Untuk sementara, kita buat tampilan sederhana di sini
                    View itemView = inflater.inflate(R.layout.item_detail_product, layoutProductItems, false);

                    TextView tvProductName = itemView.findViewById(R.id.tv_product_name_detail);
                    TextView tvProductDesc = itemView.findViewById(R.id.tv_product_desc_detail);
                    TextView tvProductQty = itemView.findViewById(R.id.tv_product_qty_detail);
                    TextView tvTotalOrder = itemView.findViewById(R.id.tv_total_order_price);

                    if (item.getProduk() != null) {
                        tvProductName.setText(item.getProduk().getNama());
                        tvProductDesc.setText(item.getProduk().getDeskripsi()); // Sesuaikan
                    }
                    tvProductQty.setText(item.getQuantity() + "x");
                    tvTotalOrder.setText(formatRupiah(currentOrder.getTotalAmount()));

                    layoutProductItems.addView(itemView);
                }
            }
        }
    }

    private String formatRupiah(double amount) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(amount);
    }
}