package com.example.ujicoba;

public class Toko {
    private int id;
    private String namaToko;
    private String lokasi;
    private String status;

    public Toko(int id, String namaToko, String lokasi, String status) {
        this.id = id;
        this.namaToko = namaToko;
        this.lokasi = lokasi;
        this.status = status;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getNamaToko() {
        return namaToko;
    }

    public String getLokasi() {
        return lokasi;
    }

    public String getStatus() {
        return status;
    }
}