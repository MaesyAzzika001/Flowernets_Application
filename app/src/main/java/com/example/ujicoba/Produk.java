// Produk.java
package com.example.ujicoba;

import android.os.Parcel;
import android.os.Parcelable;

public class Produk implements Parcelable { // Implement Parcelable
    private int id;
    private String nama;
    private String harga;
    private String deskripsi;
    private String kategori;
    private String jenis;
    private String warna;
    private String ukuran;
    private String gambar;
    private int stok;
    private String tanggal;

    // Konstruktor dan getter setter Anda ...

    public Produk(int id, String nama, String harga, String deskripsi, String kategori, String jenis, String warna, String ukuran, String gambar, int stok, String tanggal) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.kategori = kategori;
        this.jenis = jenis;
        this.warna = warna;
        this.ukuran = ukuran;
        this.gambar = gambar;
        this.stok = stok;
        this.tanggal = tanggal;
    }


    // Implementasi Parcelable
    protected Produk(Parcel in) {
        id = in.readInt();
        nama = in.readString();
        harga = in.readString();
        deskripsi = in.readString();
        kategori = in.readString();
        jenis = in.readString();
        warna = in.readString();
        ukuran = in.readString();
        gambar = in.readString();
        stok = in.readInt();
        tanggal = in.readString();
    }

    public static final Creator<Produk> CREATOR = new Creator<Produk>() {
        @Override
        public Produk createFromParcel(Parcel in) {
            return new Produk(in);
        }

        @Override
        public Produk[] newArray(int size) {
            return new Produk[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nama);
        dest.writeString(harga);
        dest.writeString(deskripsi);
        dest.writeString(kategori);
        dest.writeString(jenis);
        dest.writeString(warna);
        dest.writeString(ukuran);
        dest.writeString(gambar);
        dest.writeInt(stok);
        dest.writeString(tanggal);
    }

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getHarga() { return harga; }
    public void setHarga(String harga) { this.harga = harga; }
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    public String getWarna() { return warna; }
    public void setWarna(String warna) { this.warna = warna; }
    public String getUkuran() { return ukuran; }
    public void setUkuran(String ukuran) { this.ukuran = ukuran; }
    public String getGambar() { return gambar; }
    public void setGambar(String gambar) { this.gambar = gambar; }
    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
}