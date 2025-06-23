// ActivityPaymentSuccess.java
package com.example.ujicoba;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityPaymentSuccess extends AppCompatActivity {

    private static final int DELAY_DURATION = 3000; // 3 detik
    private String orderNumberToPass;
    private long orderAutoIdToPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        // Ambil data pesanan yang mungkin dikirim dari CheckoutActivity
        // Misalnya, nomor pesanan untuk ditampilkan di KonfirmasiPembayaranActivity
        orderNumberToPass = getIntent().getStringExtra("ORDER_NUMBER");
        orderAutoIdToPass = getIntent().getLongExtra("ORDER_AUTO_ID", -1);


        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(ActivityPaymentSuccess.this, KonfirmasiPembayaranActivity.class);
            if (orderNumberToPass != null) {
                intent.putExtra("ORDER_NUMBER", orderNumberToPass);
            }
            if (orderAutoIdToPass != -1) {
                intent.putExtra("ORDER_AUTO_ID", orderAutoIdToPass);
            }
            startActivity(intent);
            finish(); // Tutup activity ini agar tidak bisa kembali dengan tombol back
        }, DELAY_DURATION);
    }
}