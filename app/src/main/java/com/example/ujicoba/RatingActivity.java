package com.example.ujicoba;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RatingActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Order currentOrder;
    private Produk currentProduct;
    private int currentRating = 0;
    private ImageView[] stars = new ImageView[5];
    private EditText etComment;
    private Button btnSend, btnCancel;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        dbHelper = new DatabaseHelper(this);
        findViews();
        setupRatingStars();

        long orderId = getIntent().getLongExtra("ORDER_ID", -1);
        if (orderId == -1) {
            Toast.makeText(this, "Order tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentOrder = dbHelper.getOrderById(orderId);
        // Asumsi kita menilai produk pertama dalam pesanan
        currentProduct = getFirstProductFromOrder(currentOrder);

        if (currentProduct == null) {
            Toast.makeText(this, "Produk tidak ditemukan dalam pesanan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // TODO: Populate product summary view

        btnBack.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());
        btnSend.setOnClickListener(v -> sendReview());
    }

    private void findViews() {
        etComment = findViewById(R.id.et_comment);
        btnSend = findViewById(R.id.btn_send_rating);
        btnCancel = findViewById(R.id.btn_cancel_rating);
        btnBack = findViewById(R.id.btn_back_rating);
        stars[0] = findViewById(R.id.star1);
        stars[1] = findViewById(R.id.star2);
        stars[2] = findViewById(R.id.star3);
        stars[3] = findViewById(R.id.star4);
        stars[4] = findViewById(R.id.star5);
    }

    private void setupRatingStars() {
        for (int i = 0; i < stars.length; i++) {
            final int rating = i + 1;
            stars[i].setOnClickListener(v -> setRating(rating));
        }
    }

    private void setRating(int rating) {
        currentRating = rating;
        for (int i = 0; i < stars.length; i++) {
            if (i < rating) {
                stars[i].setImageResource(R.drawable.ic_star_filled);
            } else {
                stars[i].setImageResource(R.drawable.ic_star_border);
            }
        }
    }

    private void sendReview() {
        if (currentRating == 0) {
            Toast.makeText(this, "Mohon berikan rating bintang", Toast.LENGTH_SHORT).show();
            return;
        }

        String comment = etComment.getText().toString().trim();
        SharedPreferences prefs = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        int userId = prefs.getInt("LOGGED_IN_USER_ID", -1);

        if (userId == -1) {
            Toast.makeText(this, "Sesi tidak valid. Silakan login kembali.", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        String timestamp = sdf.format(new Date());

        Review review = new Review();
        review.setProductId(currentProduct.getId());
        review.setUserId(userId);
        review.setOrderId(currentOrder.getOrderAutoId());
        review.setRating(currentRating);
        review.setComment(comment);
        review.setImagePath(null); // TODO: Handle image upload
        review.setTimestamp(timestamp);

        if (dbHelper.addReview(review)) {
            Toast.makeText(this, "Penilaian berhasil dikirim!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Gagal mengirim penilaian.", Toast.LENGTH_SHORT).show();
        }
    }

    private Produk getFirstProductFromOrder(Order order) {
        if (order != null && order.getItemsJson() != null && !order.getItemsJson().isEmpty()) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
            ArrayList<CartItem> items = gson.fromJson(order.getItemsJson(), type);
            if (items != null && !items.isEmpty()) {
                return items.get(0).getProduk();
            }
        }
        return null;
    }
}