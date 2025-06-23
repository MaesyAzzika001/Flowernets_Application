package com.example.ujicoba;

import android.content.Intent; // Import Intent
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// 1. Implementasikan interface OnItemClickListener dari adapter
public class ProductListActivity extends AppCompatActivity implements ProductListAdapter.OnItemClickListener {

    public static final String EXTRA_CATEGORY = "extra_category";

    private RecyclerView rvProducts;
    private ProductListAdapter adapter;
    private List<Produk> productList;
    private DatabaseHelper databaseHelper;
    private TextView tvEmptyView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        String category = getIntent().getStringExtra(EXTRA_CATEGORY);
        if (category != null) {
            getSupportActionBar().setTitle(category);
        } else {
            getSupportActionBar().setTitle("Products");
        }

        rvProducts = findViewById(R.id.rv_products);
        tvEmptyView = findViewById(R.id.tv_empty_view);
        databaseHelper = new DatabaseHelper(this);

        setupRecyclerView();
        loadProductsByCategory(category);
    }

    private void setupRecyclerView() {
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void loadProductsByCategory(String category) {
        if (category == null) {
            productList = databaseHelper.getAllProducts();
        } else {
            productList = databaseHelper.getProductsByCategory(category);
        }

        if (productList.isEmpty()) {
            rvProducts.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);
        } else {
            rvProducts.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
            adapter = new ProductListAdapter(this, productList);
            rvProducts.setAdapter(adapter);

            // 2. Set listener untuk adapter
            adapter.setOnItemClickListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 3. Implementasikan metode dari interface yang akan dieksekusi saat item diklik
    @Override
    public void onItemClick(int position) {
        // Buat Intent untuk membuka DetailProductActivity
        Intent detailIntent = new Intent(this, DetailProductActivity.class);

        // Ambil produk yang diklik dari daftar produk
        Produk clickedProduct = productList.get(position);

        // Kirim ID produk ke DetailProductActivity
        // Pastikan key "PRODUCT_ID" sama dengan yang diterima di DetailProductActivity
        detailIntent.putExtra("PRODUCT_ID", clickedProduct.getId());

        // Mulai activity baru
        startActivity(detailIntent);
    }
}