package com.example.ujicoba;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentMethodActivity extends AppCompatActivity {

    private RadioGroup radioGroupEmoney, radioGroupVa;
    // Tambahkan RadioGroup lain jika ada

    private RelativeLayout headerEmoney, headerVa;
    private ImageView arrowEmoney, arrowVa;
    private Button btnUsePayment;

    private String selectedPaymentMethod = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        findViews();
        setupCollapsibleSections();
        setupRadioGroups();

        btnUsePayment.setOnClickListener(v -> finishAndSendResult());
        findViewById(R.id.btn_back_payment).setOnClickListener(v -> finish());
    }

    private void findViews() {
        // Inisialisasi header, arrow, dan tombol (tidak berubah)
        headerEmoney = findViewById(R.id.header_emoney);
        headerVa = findViewById(R.id.header_va);
        arrowEmoney = findViewById(R.id.arrow_emoney);
        arrowVa = findViewById(R.id.arrow_va);
        radioGroupEmoney = findViewById(R.id.radiogroup_emoney);
        radioGroupVa = findViewById(R.id.radiogroup_va);
        btnUsePayment = findViewById(R.id.btn_use_payment);

        // --- Pola Baru untuk Inisialisasi Opsi Pembayaran ---

        // E-Money
        // DANA
        setupPaymentOption(R.id.option_dana, "DANA");

        // GOPAY
        setupPaymentOption(R.id.option_gopay, "GOPAY");

        // Link Aja
        setupPaymentOption(R.id.option_linkaja, "Link Aja");

        // OVO
        setupPaymentOption(R.id.option_ovo, "OVO");

        // Shopee Pay
        setupPaymentOption(R.id.option_shopeepay, "Shopee Pay");


        // Virtual Account
        // Pastikan Anda sudah menambahkan <include> untuk VA di dalam `radiogroup_va` pada XML

        // BCA
        setupPaymentOption(R.id.option_bca, "BCA");

        // BNI
        setupPaymentOption(R.id.option_bni, "BNI");

        // BRI
        setupPaymentOption(R.id.option_bri, "BRI");

        // Mandiri
        setupPaymentOption(R.id.option_mandiri, "Mandiri");

        // BSI
        setupPaymentOption(R.id.option_bsi, "BSI");
    }

    private void setupPaymentOption(int layoutId, final String paymentName) {
        View optionLayout = findViewById(layoutId);
        if (optionLayout == null) return; // Pengaman jika ID tidak ditemukan

        TextView tvPaymentName = optionLayout.findViewById(R.id.tv_payment_name);
        RadioButton rbPaymentOption = optionLayout.findViewById(R.id.rb_payment_option);

        // Atur teks pada TextView, bukan RadioButton
        tvPaymentName.setText(paymentName);

        // Saat seluruh baris layout diklik, pilih RadioButton yang bersangkutan
        optionLayout.setOnClickListener(v -> {
            rbPaymentOption.setChecked(true);
        });
    }

    private void setupCollapsibleSections() {
        headerEmoney.setOnClickListener(v -> toggleSection(radioGroupEmoney, arrowEmoney));
        headerVa.setOnClickListener(v -> toggleSection(radioGroupVa, arrowVa));
    }

    private void toggleSection(View section, ImageView arrow) {
        boolean isVisible = section.getVisibility() == View.VISIBLE;
        section.setVisibility(isVisible ? View.GONE : View.VISIBLE);
        rotateArrow(arrow, isVisible);
    }

    private void rotateArrow(ImageView arrow, boolean isExpanded) {
        ObjectAnimator.ofFloat(arrow, "rotation", isExpanded ? 180f : 0f, isExpanded ? 0f : 180f)
                .setDuration(300)
                .start();
    }

    private void setupRadioGroups() {
        radioGroupEmoney.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                radioGroupVa.clearCheck(); // Hapus pilihan di grup lain
                RadioButton rb = group.findViewById(checkedId);
                selectedPaymentMethod = rb.getText().toString();
            }
        });

        radioGroupVa.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                radioGroupEmoney.clearCheck(); // Hapus pilihan di grup lain
                RadioButton rb = group.findViewById(checkedId);
                selectedPaymentMethod = rb.getText().toString();
            }
        });
    }

    private void finishAndSendResult() {
        if (selectedPaymentMethod == null) {
            Toast.makeText(this, "Silakan pilih metode pembayaran", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("SELECTED_PAYMENT_METHOD", selectedPaymentMethod);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}