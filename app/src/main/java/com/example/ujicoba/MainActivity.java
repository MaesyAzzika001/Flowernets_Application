
package com.example.ujicoba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private RecyclerView rvBestSeller;
    private BestSellerAdapter bestSellerAdapter;
    private List<Produk> bestSellerProductList;

    private RecyclerView rvDeliveredDaily;
    private DeliveredDailyAdapter deliveredDailyAdapter;
    private List<Produk> deliveredDailyProductList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("IS_LOGGED_IN", false);
        setupCategoryButtons();

        if (!isLoggedIn) {
            moveToLogin();
            return;
        }

        databaseHelper = new DatabaseHelper(this);

        rvBestSeller = findViewById(R.id.rv_best_seller);
        rvBestSeller.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        bestSellerProductList = new ArrayList<>();
        bestSellerAdapter = new BestSellerAdapter(this, bestSellerProductList);
        rvBestSeller.setAdapter(bestSellerAdapter);

        rvDeliveredDaily = findViewById(R.id.rv_delivered_daily);
        rvDeliveredDaily.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        deliveredDailyProductList = new ArrayList<>();
        deliveredDailyAdapter = new DeliveredDailyAdapter(this, deliveredDailyProductList);
        rvDeliveredDaily.setAdapter(deliveredDailyAdapter);

        loadAllProducts();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Tidak perlu reload activity jika sudah di home
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            } else if (itemId == R.id.nav_scan) {
                startActivity(new Intent(MainActivity.this, ActivityCameraScan.class));
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(MainActivity.this, ActivityCart.class));
                return true;
            } else if (itemId == R.id.nav_chat) {
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
                return true;
            }
            return false;
        });
        // Set item home sebagai terpilih secara default jika belum
        if (savedInstanceState == null) { // Hanya jika activity baru dibuat, bukan dari recreate
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    public void addProductToCart(Produk produk) {
        if (databaseHelper != null && produk != null) {
            databaseHelper.addOrUpdateProductInCart(produk.getId(), 1); // Tambah 1 kuantitas
            Toast.makeText(this, produk.getNama() + " ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAllProducts() {
        List<Produk> allProducts = databaseHelper.getAllProducts();

        if (allProducts.isEmpty()) {
            addSampleProductsToDB();
            allProducts = databaseHelper.getAllProducts();
        }


        if (allProducts.isEmpty()) {
            Toast.makeText(this, "Tidak ada produk untuk ditampilkan.", Toast.LENGTH_LONG).show();
        } else {

            bestSellerProductList.clear();
            bestSellerProductList.addAll(allProducts);
            bestSellerAdapter.notifyDataSetChanged();

            // Update Delivered Daily
            deliveredDailyProductList.clear();
            deliveredDailyProductList.addAll(allProducts);
            deliveredDailyAdapter.notifyDataSetChanged();
        }
    }
    private void moveToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void logoutUser() {
        SharedPreferences preferences = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        moveToLogin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private void setupCategoryButtons() {
        CardView btnWedding = findViewById(R.id.btn_wedding);
        CardView btnEvent = findViewById(R.id.btn_event);
        CardView btnGift = findViewById(R.id.btn_gift);
        CardView btnGraduation = findViewById(R.id.btn_graduation);
        CardView btnAllCategories = findViewById(R.id.btn_all_categories);

        btnWedding.setOnClickListener(v -> openProductList("Wedding"));
        btnEvent.setOnClickListener(v -> openProductList("Event"));
        btnGift.setOnClickListener(v -> openProductList("Gift")); // Asumsi kategori Gift
        btnGraduation.setOnClickListener(v -> openProductList("Graduation"));

        btnAllCategories.setOnClickListener(v -> {
            // Intent ke AllCategoriesActivity yang sudah ada
            startActivity(new Intent(MainActivity.this, AllCategoriesActivity.class));
        });
    }

    // Metode helper untuk membuka ProductListActivity
    private void openProductList(String category) {
        Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
        intent.putExtra(ProductListActivity.EXTRA_CATEGORY, category);
        startActivity(intent);
    }

    private void addSampleProductsToDB() {
        databaseHelper.insertProduct("Standing Flowers A", "299000", "Bunga papan elegan", "Wedding", "Fresh", "Merah Putih", "Large", "flowers", 10, "2025-05-26");
        databaseHelper.insertProduct("Wedding Arch Decoration", "1500000", "Dekorasi lengkung pernikahan", "Wedding", "Artificial", "Pink", "Extra Large", "wedding", 5, "2025-05-25");
        databaseHelper.insertProduct("Chinese Board Flower", "450000", "Papan bunga ucapan duka cita", "Event", "Fresh", "Kuning Putih", "Medium", "chinese", 8, "2025-05-27");
        databaseHelper.insertProduct("Elegant Flower Bucket", "350000", "Buket bunga elegan untuk hadiah", "Gift", "Fresh", "Pink Rose", "Medium", "buxket", 15, "2025-05-29");
        // TAMBAHKAN DATA PRODUK GRADUATION
        databaseHelper.insertProduct("Graduation Sunflower", "150000", "Buket bunga matahari untuk wisuda", "Graduation", "Fresh", "Kuning", "Medium", "grad_sunflower", 20, "2025-06-01");
        databaseHelper.insertProduct("Graduation Bear Bouquet", "180000", "Buket bunga dengan boneka wisuda", "Graduation", "Artificial", "Campuran", "Medium", "grad_bear", 15, "2025-06-02");
        Toast.makeText(this, "Data contoh produk ditambahkan.", Toast.LENGTH_SHORT).show();
    }

}