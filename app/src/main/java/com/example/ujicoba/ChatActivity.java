package com.example.ujicoba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<Chat> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hapus EdgeToEdge jika Anda menggunakan AppBarLayout/Toolbar
        // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        SharedPreferences preferences = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        if (!preferences.getBoolean("IS_LOGGED_IN", false)) {
            moveToLogin();
            return;
        }

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recycler_view_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Buat data contoh
        generateSampleChats();

        // Inisialisasi dan set adapter
        chatAdapter = new ChatAdapter(this, chatList);
        recyclerView.setAdapter(chatAdapter);

        // Set listener untuk klik item
        chatAdapter.setOnItemClickListener(chat -> {
            Toast.makeText(this, "Membuka chat dengan " + chat.getName(), Toast.LENGTH_SHORT).show();
            // Di sini Anda bisa start activity baru untuk detail chat
            // Intent intent = new Intent(ChatActivity.this, DetailChatActivity.class);
            // intent.putExtra("CHAT_NAME", chat.getName());
            // startActivity(intent);
        });

        // Setup Bottom Navigation
        setupBottomNavigation();
    }

    private void generateSampleChats() {
        chatList = new ArrayList<>();
        // Anda harus punya drawable dengan nama-nama ini, atau ganti dengan drawable Anda
        chatList.add(new Chat(R.drawable.logo_produk, "Delarosa.fleur", "Tentu, pesanan Anda sedang kami proses.", "14:30", 2));
        chatList.add(new Chat(R.drawable.toko2, "Lien Flower", "Would you like to pay online or in-person?", "14:25", 0));
        chatList.add(new Chat(R.drawable.toko3, "Pinus Flower", "Baik, terima kasih atas konfirmasinya.", "14:22", 0));
        chatList.add(new Chat(R.drawable.toko4, "Chic Threads", "Looking for the perfect outfit?", "13:50", 1));
        chatList.add(new Chat(R.drawable.toko5, "Iron & Grace", "Join our fitness studio and start your...", "13:15", 0));

    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_chat); // Menandai item chat sebagai aktif

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(ChatActivity.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(ChatActivity.this, ProfileActivity.class));
                return true;
            } else if (itemId == R.id.nav_scan) {
                startActivity(new Intent(ChatActivity.this, ActivityCameraScan.class));
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(ChatActivity.this, ActivityCart.class));
                return true;
            } else if (itemId == R.id.nav_chat) {
                // Sudah di halaman chat, tidak perlu aksi
                return true;
            }
            return false;
        });
    }

    private void moveToLogin() {
        Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}