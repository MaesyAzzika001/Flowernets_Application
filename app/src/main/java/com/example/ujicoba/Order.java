package com.example.ujicoba;

// Pastikan untuk mengimpor Parcelable jika Anda mengirim objek ini antar activity melalui Intent
// import android.os.Parcel;
// import android.os.Parcelable;

public class Order { // Mungkin perlu implement Parcelable jika Anda butuh

    // 1. FIELDS (DEKLARASI VARIABEL)
    // Pastikan field ini ada, sesuai dengan kolom di database
    private long orderAutoId; // <-- FIELD YANG HILANG
    private int userId;
    private String orderNumber;
    private String customerName;
    private String productSummary;
    private String itemsJson;
    private double totalAmount;
    private String orderTimestamp;
    private String paymentTimestamp;
    private String status;

    // 2. CONSTRUCTORS
    // Constructor kosong (diperlukan untuk beberapa library seperti GSON atau saat membuat objek baru)
    public Order() {
    }

    // Constructor lengkap (digunakan saat mengambil data dari database)
    public Order(long orderAutoId, int userId, String orderNumber, String customerName, String productSummary,
                 String itemsJson, double totalAmount, String orderTimestamp, String paymentTimestamp, String status) {
        this.orderAutoId = orderAutoId; // <-- Inisialisasi field
        this.userId = userId;
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.productSummary = productSummary;
        this.itemsJson = itemsJson;
        this.totalAmount = totalAmount;
        this.orderTimestamp = orderTimestamp;
        this.paymentTimestamp = paymentTimestamp;
        this.status = status;
    }

    // 3. GETTERS AND SETTERS
    // Metode getter inilah yang dicari oleh KelolaPesananActivity

    public long getOrderAutoId() { // <-- METHOD GETTER YANG HILANG
        return orderAutoId;
    }

    public void setOrderAutoId(long orderAutoId) {
        this.orderAutoId = orderAutoId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductSummary() {
        return productSummary;
    }

    public void setProductSummary(String productSummary) {
        this.productSummary = productSummary;
    }

    public String getItemsJson() {
        return itemsJson;
    }

    public void setItemsJson(String itemsJson) {
        this.itemsJson = itemsJson;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(String orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }

    public String getPaymentTimestamp() {
        return paymentTimestamp;
    }

    public void setPaymentTimestamp(String paymentTimestamp) {
        this.paymentTimestamp = paymentTimestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}