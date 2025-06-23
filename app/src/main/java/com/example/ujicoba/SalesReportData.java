package com.example.ujicoba;

// Kelas ini berfungsi sebagai kontainer untuk data laporan
public class SalesReportData {
    private double totalRevenue;
    private int productsSoldCount;
    private int totalProductViews;
    private int newCustomersCount;

    // Constructor
    public SalesReportData(double totalRevenue, int productsSoldCount, int totalProductViews, int newCustomersCount) {
        this.totalRevenue = totalRevenue;
        this.productsSoldCount = productsSoldCount;
        this.totalProductViews = totalProductViews;
        this.newCustomersCount = newCustomersCount;
    }

    // Getter methods
    public double getTotalRevenue() {
        return totalRevenue;
    }

    public int getProductsSoldCount() {
        return productsSoldCount;
    }

    public int getTotalProductViews() {
        return totalProductViews;
    }

    public int getNewCustomersCount() {
        return newCustomersCount;
    }
}