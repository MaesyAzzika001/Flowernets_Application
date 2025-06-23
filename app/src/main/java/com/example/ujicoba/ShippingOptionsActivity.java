package com.example.ujicoba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout; // Import RelativeLayout
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class ShippingOptionsActivity extends AppCompatActivity {

    private RadioGroup radioGroupShipping;
    private ImageButton btnBack;

    // Deklarasikan RadioButton dan Layout
    private RadioButton rbAmbilDiTempat, rbGosend, rbLalamove, rbKurirku;
    private RelativeLayout layoutAmbilDiTempat, layoutGosend, layoutLalamove, layoutKurirku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_options);

        // Inisialisasi semua view
        findViews();

        // Menerima data opsi yang sedang aktif dari CheckoutActivity
        String currentOption = getIntent().getStringExtra("CURRENT_SHIPPING_OPTION");
        if (currentOption != null) {
            checkCurrentOption(currentOption);
        }

        // Setup listener
        btnBack.setOnClickListener(v -> finishAndSendResult());
        setupLayoutClickListeners();
    }

    private void findViews() {
        radioGroupShipping = findViewById(R.id.radio_group_shipping);
        btnBack = findViewById(R.id.btn_back_shipping);

        // Inisialisasi RadioButton
        rbAmbilDiTempat = findViewById(R.id.rb_ambil_ditempat);
        rbGosend = findViewById(R.id.rb_gosend);
        rbLalamove = findViewById(R.id.rb_lalamove);
        rbKurirku = findViewById(R.id.rb_kurirku);

        // Inisialisasi RelativeLayout
        layoutAmbilDiTempat = findViewById(R.id.layout_ambil_ditempat);
        layoutGosend = findViewById(R.id.layout_gosend);
        layoutLalamove = findViewById(R.id.layout_lalamove);
        layoutKurirku = findViewById(R.id.layout_kurirku);
    }

    private void setupLayoutClickListeners() {
        layoutAmbilDiTempat.setOnClickListener(v -> rbAmbilDiTempat.setChecked(true));
        layoutGosend.setOnClickListener(v -> rbGosend.setChecked(true));
        layoutLalamove.setOnClickListener(v -> rbLalamove.setChecked(true));
        layoutKurirku.setOnClickListener(v -> rbKurirku.setChecked(true));
    }

    private void checkCurrentOption(String currentOption) {
        if (Objects.equals(currentOption, "Ambil Di Tempat")) {
            rbAmbilDiTempat.setChecked(true);
        } else if (Objects.equals(currentOption, "GoSend")) {
            rbGosend.setChecked(true);
        } else if (Objects.equals(currentOption, "LalaMove")) {
            rbLalamove.setChecked(true);
        } else if (Objects.equals(currentOption, "Kurirku")) {
            rbKurirku.setChecked(true);
        }
    }

    private void finishAndSendResult() {
        int selectedId = radioGroupShipping.getCheckedRadioButtonId();
        if (selectedId == -1) {
            setResult(RESULT_CANCELED);
        } else {
            String selectedOptionName = "";
            if(selectedId == R.id.rb_ambil_ditempat) selectedOptionName = "Ambil Di Tempat";
            else if(selectedId == R.id.rb_gosend) selectedOptionName = "GoSend";
            else if(selectedId == R.id.rb_lalamove) selectedOptionName = "LalaMove";
            else if(selectedId == R.id.rb_kurirku) selectedOptionName = "Kurirku";

            double shippingCost = 0.0;
            if (selectedId == R.id.rb_gosend) shippingCost = 15000.0;
            else if (selectedId == R.id.rb_lalamove) shippingCost = 18000.0;
            else if (selectedId == R.id.rb_kurirku) shippingCost = 10000.0;

            Intent resultIntent = new Intent();
            resultIntent.putExtra("SELECTED_SHIPPING_OPTION", selectedOptionName);
            resultIntent.putExtra("SELECTED_SHIPPING_COST", shippingCost);
            setResult(RESULT_OK, resultIntent);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        finishAndSendResult();
        // super.onBackPressed() tidak dipanggil di sini agar finishAndSendResult yang mengontrol
    }
}