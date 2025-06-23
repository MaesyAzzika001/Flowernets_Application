// RiwayatPesananActivity.java
package com.example.ujicoba;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RiwayatPesananActivity extends AppCompatActivity {

    private RecyclerView rvOrderHistory;
    private OrderHistoryAdapter adapter;
    private List<Order> orderList = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private int loggedInUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_pesanan);

        dbHelper = new DatabaseHelper(this);

        // Ambil ID user yang sedang login (cara yang sama seperti di CheckoutActivity)
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String userEmail = prefs.getString("user_email", null);
        if (userEmail != null) {
            loggedInUserId = dbHelper.getUserId(userEmail);
        }

        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());

        rvOrderHistory = findViewById(R.id.rv_order_history);
        rvOrderHistory.setLayoutManager(new LinearLayoutManager(this));

        adapter = new OrderHistoryAdapter(this, orderList);
        rvOrderHistory.setAdapter(adapter);

        loadOrderHistory();
    }

    private void loadOrderHistory() {
        if (loggedInUserId == -1) {
            Toast.makeText(this, "Tidak dapat memuat riwayat, user tidak ditemukan.", Toast.LENGTH_LONG).show();
            return;
        }

        List<Order> userOrders = dbHelper.getOrdersByUserId(loggedInUserId);
        if (userOrders != null && !userOrders.isEmpty()) {
            orderList.clear();
            orderList.addAll(userOrders);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Anda belum memiliki riwayat pesanan.", Toast.LENGTH_SHORT).show();
            // Mungkin tampilkan gambar atau teks "Riwayat Kosong"
        }
    }
}