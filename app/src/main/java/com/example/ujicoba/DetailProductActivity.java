package com.example.ujicoba;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DetailProductActivity extends AppCompatActivity {

    private ImageView productImage, backButton;
    private TextView productName, productPrice, productDetailText;
    private Button flowerTypeButton, colorButton, sizeButton, addToCartButton, buyNowButton;

    private DatabaseHelper databaseHelper;
    private Produk currentProduct;
    private int quantity = 1; // Default kuantitas
    private TextView quantityText;
    private LinearLayout reviewsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        databaseHelper = new DatabaseHelper(this);

        // Inisialisasi Views
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDetailText = findViewById(R.id.productDetailText);
        flowerTypeButton = findViewById(R.id.flowerTypeButton);
        colorButton = findViewById(R.id.colorButton);
        sizeButton = findViewById(R.id.sizeButton);
        addToCartButton = findViewById(R.id.addToCartButton);
        buyNowButton = findViewById(R.id.buyNowButton);
        backButton = findViewById(R.id.backButton);
        // Inisialisasi untuk kuantitas
        quantityText = findViewById(R.id.quantitySelector).findViewById(R.id.text_quantity); // Asumsi ID baru text_quantity
        ImageView minusButton = findViewById(R.id.quantitySelector).findViewById(R.id.button_minus); // Asumsi ID baru button_minus
        ImageView plusButton = findViewById(R.id.quantitySelector).findViewById(R.id.button_plus); // Asumsi ID baru button_plus

        reviewsContainer = findViewById(R.id.reviews_container); // Ganti ID sesuai layout Anda

        // Ambil ID produk dari intent
        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);

        if (productId != -1) {
            // Ambil data produk dari database
            currentProduct = databaseHelper.getProductById(productId);
            if (currentProduct != null) {
                populateUI(currentProduct);
                loadAndDisplayReviews(currentProduct.getId()); // Panggil metode ini
            } else {
                Toast.makeText(this, "Produk tidak ditemukan.", Toast.LENGTH_LONG).show();
                finish(); // Tutup activity jika produk tidak ada
            }
        } else {
            Toast.makeText(this, "ID produk tidak valid.", Toast.LENGTH_LONG).show();
            finish(); // Tutup activity jika ID tidak valid
        }

        // Atur listeners
        backButton.setOnClickListener(v -> onBackPressed());

        addToCartButton.setOnClickListener(v -> {
            if (currentProduct != null) {
                databaseHelper.addOrUpdateProductInCart(currentProduct.getId(), quantity);
                Toast.makeText(DetailProductActivity.this, quantity + " " + currentProduct.getNama() + " ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();
            }
        });

        buyNowButton.setOnClickListener(v -> {
            if (currentProduct != null) {
                databaseHelper.addOrUpdateProductInCart(currentProduct.getId(), quantity);
                Intent intent = new Intent(DetailProductActivity.this, ActivityCart.class);
                startActivity(intent);
            }
        });

        minusButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
            }
        });

        plusButton.setOnClickListener(v -> {
            if (quantity < currentProduct.getStok()) { // Batasi dengan stok
                quantity++;
                quantityText.setText(String.valueOf(quantity));
            } else {
                Toast.makeText(this, "Stok tidak mencukupi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateUI(Produk product) {
        // Set nama, deskripsi, dll.
        productName.setText(product.getNama());
        productDetailText.setText(product.getDeskripsi());

        // Format harga
        try {
            double priceValue = Double.parseDouble(product.getHarga());
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
            productPrice.setText(format.format(priceValue));
        } catch (NumberFormatException e) {
            productPrice.setText("Rp" + product.getHarga()); // Fallback
        }

        // Set detail pada tombol
        flowerTypeButton.setText(product.getJenis());
        colorButton.setText(product.getWarna());
        sizeButton.setText(product.getUkuran());

        // Set gambar produk
        int imageId = getResources().getIdentifier(product.getGambar(), "drawable", getPackageName());
        if (imageId != 0) {
            productImage.setImageResource(imageId);
        } else {
            productImage.setImageResource(R.drawable.buxket); // Gambar default
        }

        quantityText.setText(String.valueOf(quantity));
    }

    // Tambahkan metode baru ini di DetailProductActivity
    private void loadAndDisplayReviews(int productId) {
        reviewsContainer.removeAllViews();
        List<Review> reviews = databaseHelper.getReviewsByProductId(productId);

        if (reviews.isEmpty()) {
            // Tampilkan pesan jika tidak ada review
            TextView noReviewsText = new TextView(this);
            noReviewsText.setText("Belum ada penilaian.");
            reviewsContainer.addView(noReviewsText);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        for (Review review : reviews) {
            View reviewView = inflater.inflate(R.layout.review_item_layout, reviewsContainer, false);

            TextView reviewerName = reviewView.findViewById(R.id.tv_reviewer_name);
            TextView reviewDate = reviewView.findViewById(R.id.tv_review_date);
            LinearLayout starsLayout = reviewView.findViewById(R.id.layout_review_stars);
            TextView reviewComment = reviewView.findViewById(R.id.tv_review_comment);

            // TODO: Ambil nama user dari DB berdasarkan review.getUserId()
            reviewerName.setText("Pengguna " + review.getUserId());
            reviewDate.setText(review.getTimestamp());
            reviewComment.setText(review.getComment());

            // Tampilkan bintang
            starsLayout.removeAllViews();
            for (int i = 0; i < 5; i++) {
                ImageView star = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40, 40); // Ukuran bintang
                star.setLayoutParams(params);
                if (i < review.getRating()) {
                    star.setImageResource(R.drawable.ic_star_filled);
                } else {
                    star.setImageResource(R.drawable.ic_star_border);
                }
                starsLayout.addView(star);
            }

            reviewsContainer.addView(reviewView);
        }
    }
}