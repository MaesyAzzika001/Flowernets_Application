package com.example.ujicoba;

public class Kategori {
    private String nama;
    private int jumlahProduk;

    public Kategori(String nama, int jumlahProduk) {
        this.nama = nama;
        this.jumlahProduk = jumlahProduk;
    }

    public String getNama() {
        return nama;
    }

    public int getJumlahProduk() {
        return jumlahProduk;
    }
}