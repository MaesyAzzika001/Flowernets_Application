package com.example.ujicoba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDB";
    // NAIKKAN VERSI DATABASE
    private static final int DATABASE_VERSION = 10;

    // USERS TABLE
    private static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id"; // DIUBAH KE PUBLIC
    public static final String COLUMN_EMAIL = "email"; // DIUBAH KE PUBLIC
    private static final String COLUMN_PASSWORD = "password"; // Biarkan private untuk keamanan
    // --- KOLOM BARU UNTUK PROFIL ---
    public static final String COLUMN_USER_NAME = "nama_toko";
    public static final String COLUMN_USER_PHONE = "telepon";
    public static final String COLUMN_USER_BIRTHDATE = "tanggal_lahir";
    public static final String COLUMN_USER_GENDER = "jenis_kelamin";
    public static final String COLUMN_USER_ADDRESS = "alamat";
    public static final String COLUMN_USER_PHOTO_URI = "photo_uri";
    // --- KOLOM BARU UNTUK PROFIL TOKO ---
    public static final String COLUMN_SHOP_WORK_DAYS = "work_days";
    public static final String COLUMN_SHOP_WORK_HOURS = "work_hours";
    public static final String COLUMN_SHOP_DESCRIPTION = "description";


    // ... (Definisi tabel dan kolom lain tetap sama) ...
    // PRODUCTS TABLE
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PRODUK_ID = "id"; // ID Produk
    public static final String COLUMN_NAMA = "nama";
    public static final String COLUMN_HARGA = "harga";
    public static final String COLUMN_DESKRIPSI = "deskripsi";
    public static final String COLUMN_KATEGORI = "kategori";
    public static final String COLUMN_JENIS = "jenis";
    public static final String COLUMN_WARNA = "warna";
    public static final String COLUMN_UKURAN = "ukuran";
    public static final String COLUMN_GAMBAR = "gambar";
    public static final String COLUMN_STOK = "jumlah";
    public static final String COLUMN_TANGGAL = "tanggal";

    // CART TABLE
    public static final String TABLE_CART = "cart_items";
    public static final String COLUMN_CART_ID = "cart_id";
    public static final String COLUMN_CART_PRODUK_ID = "product_id";
    public static final String COLUMN_CART_QUANTITY = "quantity";

    // ORDERS TABLE
    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDER_AUTO_ID = "order_auto_id";
    public static final String COLUMN_ORDER_USER_ID = "user_id"; // <-- Kolom Baru
    public static final String COLUMN_ORDER_NUMBER = "order_number";
    public static final String COLUMN_ORDER_CUSTOMER_NAME = "customer_name";
    public static final String COLUMN_ORDER_PRODUCT_SUMMARY = "product_summary";
    public static final String COLUMN_ORDER_ITEMS_JSON = "items_json";
    public static final String COLUMN_ORDER_TOTAL_AMOUNT = "total_amount";
    public static final String COLUMN_ORDER_TIMESTAMP = "order_timestamp";
    public static final String COLUMN_ORDER_PAYMENT_TIMESTAMP = "payment_timestamp";
    public static final String COLUMN_ORDER_STATUS = "status";
    // REVIEWS TABLE
    public static final String TABLE_REVIEWS = "reviews";
    public static final String COLUMN_REVIEW_ID = "review_id";
    public static final String COLUMN_REVIEW_PRODUCT_ID = "product_id";
    public static final String COLUMN_REVIEW_USER_ID = "user_id";
    public static final String COLUMN_REVIEW_ORDER_ID = "order_id";
    public static final String COLUMN_REVIEW_RATING = "rating"; // 1-5
    public static final String COLUMN_REVIEW_COMMENT = "comment";
    public static final String COLUMN_REVIEW_IMAGE_PATH = "image_path";
    public static final String COLUMN_REVIEW_TIMESTAMP = "timestamp";
    // ADDRESSES TABLE
    public static final String TABLE_ADDRESSES = "addresses";
    public static final String COLUMN_ADDRESS_ID = "address_id";
    public static final String COLUMN_ADDRESS_USER_ID = "user_id";
    public static final String COLUMN_RECIPIENT_NAME = "recipient_name";
    public static final String COLUMN_STREET_ADDRESS = "street_address";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_POSTAL_CODE = "postal_code";
    public static final String COLUMN_DETAILS = "details";

    public static final String COLUMN_USER_CREATION_DATE = "creation_date";
    public static final String COLUMN_PRODUCT_VIEWS = "views";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // PERBARUI CREATE TABLE USERS
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_USER_PHONE + " TEXT, " +
                COLUMN_USER_BIRTHDATE + " TEXT, " +
                COLUMN_USER_GENDER + " TEXT, " +
                COLUMN_USER_ADDRESS + " TEXT, " +
                COLUMN_USER_PHOTO_URI + " TEXT, " +
                COLUMN_USER_CREATION_DATE + " TEXT, " +
                COLUMN_SHOP_WORK_DAYS + " TEXT, " +    // KOLOM BARU
                COLUMN_SHOP_WORK_HOURS + " TEXT, " +   // KOLOM BARU
                COLUMN_SHOP_DESCRIPTION + " TEXT)";   // KOLOM BARU
        db.execSQL(createUsersTable);


        // ... (onCreate untuk tabel lain tetap sama) ...
        String createProductsTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_PRODUK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAMA + " TEXT, " +
                COLUMN_HARGA + " TEXT, " +
                COLUMN_DESKRIPSI + " TEXT, " +
                COLUMN_KATEGORI + " TEXT, " +
                COLUMN_JENIS + " TEXT, " +
                COLUMN_WARNA + " TEXT, " +
                COLUMN_UKURAN + " TEXT, " +
                COLUMN_GAMBAR + " TEXT, " +
                COLUMN_STOK + " INTEGER, " +
                COLUMN_TANGGAL + " TEXT, " +
                COLUMN_PRODUCT_VIEWS + " INTEGER DEFAULT 0)";
        db.execSQL(createProductsTable);

        String createCartTable = "CREATE TABLE " + TABLE_CART + " (" +
                COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CART_PRODUK_ID + " INTEGER, " +
                COLUMN_CART_QUANTITY + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_CART_PRODUK_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUK_ID + ")" +
                ")";
        db.execSQL(createCartTable);

        // Tabel Pesanan Baru dengan kolom user_id
        String createOrdersTable = "CREATE TABLE " + TABLE_ORDERS + " (" +
                COLUMN_ORDER_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ORDER_USER_ID + " INTEGER, " + // <-- Kolom Baru
                COLUMN_ORDER_NUMBER + " TEXT UNIQUE, " +
                COLUMN_ORDER_CUSTOMER_NAME + " TEXT, " +
                COLUMN_ORDER_PRODUCT_SUMMARY + " TEXT, " +
                COLUMN_ORDER_ITEMS_JSON + " TEXT, " +
                COLUMN_ORDER_TOTAL_AMOUNT + " REAL, " +
                COLUMN_ORDER_TIMESTAMP + " TEXT, " +
                COLUMN_ORDER_PAYMENT_TIMESTAMP + " TEXT, " +
                COLUMN_ORDER_STATUS + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_ORDER_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
        db.execSQL(createOrdersTable);

        // Tabel Penilaian Baru
        String createReviewsTable = "CREATE TABLE " + TABLE_REVIEWS + " (" +
                COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REVIEW_PRODUCT_ID + " INTEGER, " +
                COLUMN_REVIEW_USER_ID + " INTEGER, " +
                COLUMN_REVIEW_ORDER_ID + " INTEGER, " +
                COLUMN_REVIEW_RATING + " INTEGER, " +
                COLUMN_REVIEW_COMMENT + " TEXT, " +
                COLUMN_REVIEW_IMAGE_PATH + " TEXT, " +
                COLUMN_REVIEW_TIMESTAMP + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_REVIEW_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUK_ID + "), " +
                "FOREIGN KEY(" + COLUMN_REVIEW_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY(" + COLUMN_REVIEW_ORDER_ID + ") REFERENCES " + TABLE_ORDERS + "(" + COLUMN_ORDER_AUTO_ID + "))";
        db.execSQL(createReviewsTable);

        // Tabel Alamat Baru
        String createAddressesTable = "CREATE TABLE " + TABLE_ADDRESSES + " (" +
                COLUMN_ADDRESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ADDRESS_USER_ID + " INTEGER, " +
                COLUMN_RECIPIENT_NAME + " TEXT, " +
                COLUMN_STREET_ADDRESS + " TEXT, " +
                COLUMN_CITY + " TEXT, " +
                COLUMN_POSTAL_CODE + " TEXT, " +
                COLUMN_DETAILS + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_ADDRESS_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
        db.execSQL(createAddressesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Cara aman untuk upgrade: cek versi dan tambahkan kolom jika belum ada
        if (oldVersion < 9) {
            try {
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_SHOP_WORK_DAYS + " TEXT");
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_SHOP_WORK_HOURS + " TEXT");
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_SHOP_DESCRIPTION + " TEXT");
            } catch (Exception e) {
                Log.e("DBUpgrade", "Gagal menambah kolom profil toko, mungkin sudah ada.", e);
            }try {
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_CREATION_DATE + " TEXT");
            } catch (Exception e) { Log.e("DBUpgrade", "Gagal tambah kolom creation_date, mungkin sudah ada.");
            }try {
                db.execSQL("ALTER TABLE " + TABLE_PRODUCTS + " ADD COLUMN " + COLUMN_PRODUCT_VIEWS + " INTEGER DEFAULT 0");
            } catch (Exception e) { Log.e("DBUpgrade", "Gagal tambah kolom views, mungkin sudah ada.");
            }try {
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_NAME + " TEXT");
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_PHONE + " TEXT");
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_BIRTHDATE + " TEXT");
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_GENDER + " TEXT");
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_ADDRESS + " TEXT");
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_PHOTO_URI + " TEXT");
            } catch (Exception e) {
                Log.e("DatabaseHelper", "Error upgrading users table", e);
                // Jika gagal, fallback ke drop & recreate
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
                onCreate(db);
            }
        } else {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEWS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESSES);
            onCreate(db);
        }
    }

    public boolean updateShopProfile(int userId, String namaToko, String telepon, String alamat, String workDays, String workHours, String description, String photoUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, namaToko);
        values.put(COLUMN_USER_PHONE, telepon);
        values.put(COLUMN_USER_ADDRESS, alamat);
        values.put(COLUMN_SHOP_WORK_DAYS, workDays);
        values.put(COLUMN_SHOP_WORK_HOURS, workHours);
        values.put(COLUMN_SHOP_DESCRIPTION, description);

        // Hanya update photo URI jika tidak null
        if (photoUri != null) {
            values.put(COLUMN_USER_PHOTO_URI, photoUri);
        }

        int rows = db.update(TABLE_USERS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(userId)});
        return rows > 0;
    }

    // --- METODE BARU UNTUK PROFIL ---
    public boolean updateUserProfile(int userId, String namaToko, String telepon, String tglLahir, String gender, String alamat, String photoUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, namaToko);
        values.put(COLUMN_USER_PHONE, telepon);
        values.put(COLUMN_USER_BIRTHDATE, tglLahir);
        values.put(COLUMN_USER_GENDER, gender);
        values.put(COLUMN_USER_ADDRESS, alamat);
        values.put(COLUMN_USER_PHOTO_URI, photoUri);

        int rows = db.update(TABLE_USERS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(userId)});
        return rows > 0;
    }

    public Cursor getUserProfile(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, COLUMN_ID + " = ?", new String[]{String.valueOf(userId)}, null, null, null);
    }

    public long insertOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_USER_ID, order.getUserId()); // <-- Simpan User ID
        values.put(COLUMN_ORDER_NUMBER, order.getOrderNumber());
        values.put(COLUMN_ORDER_CUSTOMER_NAME, order.getCustomerName());
        values.put(COLUMN_ORDER_PRODUCT_SUMMARY, order.getProductSummary());
        values.put(COLUMN_ORDER_ITEMS_JSON, order.getItemsJson());
        values.put(COLUMN_ORDER_TOTAL_AMOUNT, order.getTotalAmount());
        values.put(COLUMN_ORDER_TIMESTAMP, order.getOrderTimestamp());
        values.put(COLUMN_ORDER_PAYMENT_TIMESTAMP, order.getPaymentTimestamp());
        values.put(COLUMN_ORDER_STATUS, order.getStatus());

        long id = db.insert(TABLE_ORDERS, null, values);
        return id;
    }

    // DatabaseHelper.java

// ... (kode lain di atasnya tetap sama)

    public Order getOrderById(long autoId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Order order = null;
        Cursor cursor = db.query(TABLE_ORDERS, null, COLUMN_ORDER_AUTO_ID + "=?",
                new String[]{String.valueOf(autoId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            order = new Order(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ORDER_AUTO_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_NUMBER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_CUSTOMER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PRODUCT_SUMMARY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEMS_JSON)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL_AMOUNT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TIMESTAMP)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PAYMENT_TIMESTAMP)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS))
            );
            cursor.close();
        }
        return order;
    }

    public Order getOrderByOrderNumber(String orderNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Order order = null;
        Cursor cursor = db.query(TABLE_ORDERS, null, COLUMN_ORDER_NUMBER + "=?",
                new String[]{orderNumber}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            order = new Order(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ORDER_AUTO_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_NUMBER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_CUSTOMER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PRODUCT_SUMMARY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEMS_JSON)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL_AMOUNT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TIMESTAMP)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PAYMENT_TIMESTAMP)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS))
            );
            cursor.close();
        }
        return order;
    }

    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Urutkan berdasarkan ID descending agar pesanan terbaru muncul di atas
        Cursor cursor = db.query(TABLE_ORDERS, null, null, null, null, null, COLUMN_ORDER_AUTO_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ORDER_AUTO_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_NUMBER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_CUSTOMER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PRODUCT_SUMMARY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEMS_JSON)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PAYMENT_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS))
                );
                orderList.add(order);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return orderList;
    }

    // Metode baru untuk mengambil riwayat pesanan berdasarkan User ID
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS, null, COLUMN_ORDER_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, COLUMN_ORDER_AUTO_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ORDER_AUTO_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_NUMBER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_CUSTOMER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PRODUCT_SUMMARY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEMS_JSON)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PAYMENT_TIMESTAMP)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS))
                );
                orderList.add(order);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return orderList;
    }

    // ... (Metode-metode lain yang sudah ada untuk User, Produk, Cart) ...
    // Pastikan metode seperti clearCart() ada jika Anda ingin menggunakannya
    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null, null);
        Log.d("DatabaseHelper", "Cart cleared.");
    }
    // Salin metode lain yang relevan dari respons sebelumnya jika belum ada
    public boolean registerUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_USER_CREATION_DATE, java.time.LocalDate.now().toString());
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + COLUMN_ID + ", " + COLUMN_EMAIL + " FROM " + TABLE_USERS, null);
    }

    // Metode BARU untuk menambah jumlah view produk
    public void incrementProductViewCount(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_PRODUCTS + " SET " + COLUMN_PRODUCT_VIEWS +
                " = " + COLUMN_PRODUCT_VIEWS + " + 1 WHERE " + COLUMN_PRODUK_ID + " = " + productId;
        db.execSQL(query);
    }
    public boolean insertProduct(String nama, String harga, String deskripsi, String kategori,
                                 String jenis, String warna, String ukuran, String gambar,
                                 int jumlah, String tanggal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, nama);
        values.put(COLUMN_HARGA, harga);
        values.put(COLUMN_DESKRIPSI, deskripsi);
        values.put(COLUMN_KATEGORI, kategori);
        values.put(COLUMN_JENIS, jenis);
        values.put(COLUMN_WARNA, warna);
        values.put(COLUMN_UKURAN, ukuran);
        values.put(COLUMN_GAMBAR, gambar);
        values.put(COLUMN_STOK, jumlah);
        values.put(COLUMN_TANGGAL, tanggal);
        long result = db.insert(TABLE_PRODUCTS, null, values);
        return result != -1;
    }

    public Produk getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUK_ID + " = ?", new String[]{String.valueOf(id)});
        Produk produk = null;
        if (cursor.moveToFirst()) {
            String nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA));
            String harga_str = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HARGA));
            String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESKRIPSI));
            String kategori = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KATEGORI));
            String jenis = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JENIS));
            String warna = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WARNA));
            String ukuran = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UKURAN));
            String gambar = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GAMBAR));
            int stok = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOK));
            String tanggal = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TANGGAL));
            produk = new Produk(id, nama, harga_str, deskripsi, kategori, jenis, warna, ukuran, gambar, stok, tanggal);
        }
        cursor.close();
        return produk;
    }

    public Cursor getAllProductsAsCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
    }

    public List<Produk> getAllProducts() {
        List<Produk> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUK_ID));
                String nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA));
                String harga = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HARGA));
                String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESKRIPSI));
                String kategori = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KATEGORI));
                String jenis = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JENIS));
                String warna = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WARNA));
                String ukuran = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UKURAN));
                String gambar = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GAMBAR));
                int stok = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOK));
                String tanggal = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TANGGAL));

                productList.add(new Produk(id, nama, harga, deskripsi, kategori, jenis, warna, ukuran, gambar, stok, tanggal));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_PRODUK_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void addOrUpdateProductInCart(int productId, int quantityChange) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CART,
                new String[]{COLUMN_CART_ID, COLUMN_CART_QUANTITY},
                COLUMN_CART_PRODUK_ID + "=?",
                new String[]{String.valueOf(productId)},
                null, null, null);

        int currentQuantity = 0;
        int cartItemId = -1;
        if (cursor.moveToFirst()) {
            cartItemId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_ID));
            currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY));
        }
        cursor.close();

        int newQuantity = currentQuantity + quantityChange;

        if (newQuantity <= 0) {
            if (cartItemId != -1) {
                db.delete(TABLE_CART, COLUMN_CART_ID + "=?", new String[]{String.valueOf(cartItemId)});
            }
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_PRODUK_ID, productId);
            values.put(COLUMN_CART_QUANTITY, newQuantity);
            if (cartItemId != -1) {
                db.update(TABLE_CART, values, COLUMN_CART_ID + "=?", new String[]{String.valueOf(cartItemId)});
            } else {
                db.insert(TABLE_CART, null, values);
            }
        }
    }

    public List<CartItem> getCartItems() {
        List<CartItem> cartItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c." + COLUMN_CART_ID + ", c." + COLUMN_CART_PRODUK_ID + ", c." + COLUMN_CART_QUANTITY + ", " +
                "p." + COLUMN_NAMA + ", p." + COLUMN_HARGA + ", p." + COLUMN_DESKRIPSI + ", " +
                "p." + COLUMN_KATEGORI + ", p." + COLUMN_JENIS + ", p." + COLUMN_WARNA + ", " +
                "p." + COLUMN_UKURAN + ", p." + COLUMN_GAMBAR + ", p." + COLUMN_STOK + ", p." + COLUMN_TANGGAL + " " +
                "FROM " + TABLE_CART + " c " +
                "JOIN " + TABLE_PRODUCTS + " p ON c." + COLUMN_CART_PRODUK_ID + " = p." + COLUMN_PRODUK_ID;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int cartId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_ID));
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_PRODUK_ID));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY));
                String nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA));
                String harga = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HARGA));
                String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESKRIPSI));
                String kategori = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KATEGORI));
                String jenis = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JENIS));
                String warna = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WARNA));
                String ukuran = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UKURAN));
                String gambar = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GAMBAR));
                int stok = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOK));
                String tanggal = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TANGGAL));
                Produk produk = new Produk(productId, nama, harga, deskripsi, kategori, jenis, warna, ukuran, gambar, stok, tanggal);
                cartItemList.add(new CartItem(cartId, produk, quantity, true));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartItemList;
    }

    public void updateCartItemQuantity(int cartId, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (newQuantity <= 0) {
            db.delete(TABLE_CART, COLUMN_CART_ID + "=?", new String[]{String.valueOf(cartId)});
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_QUANTITY, newQuantity);
            db.update(TABLE_CART, values, COLUMN_CART_ID + "=?", new String[]{String.valueOf(cartId)});
        }
    }

    public void removeCartItem(int cartId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_CART_ID + "=?", new String[]{String.valueOf(cartId)});
    }
    // Metode untuk mendapatkan ID user berdasarkan email
    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID}, COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            cursor.close();
            return userId;
        }
        return -1; // -1 jika user tidak ditemukan
    }
    // Tambahkan metode ini di dalam class DatabaseHelper

    public boolean addReview(Review review) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_REVIEW_PRODUCT_ID, review.getProductId());
        values.put(COLUMN_REVIEW_USER_ID, review.getUserId());
        values.put(COLUMN_REVIEW_ORDER_ID, review.getOrderId());
        values.put(COLUMN_REVIEW_RATING, review.getRating());
        values.put(COLUMN_REVIEW_COMMENT, review.getComment());
        values.put(COLUMN_REVIEW_IMAGE_PATH, review.getImagePath());
        values.put(COLUMN_REVIEW_TIMESTAMP, review.getTimestamp());

        long result = db.insert(TABLE_REVIEWS, null, values);
        return result != -1;
    }

    public List<Review> getReviewsByProductId(int productId) {
        List<Review> reviewList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REVIEWS, null, COLUMN_REVIEW_PRODUCT_ID + "=?",
                new String[]{String.valueOf(productId)}, null, null, COLUMN_REVIEW_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Review review = new Review(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_PRODUCT_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_USER_ID)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_ORDER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_RATING)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_COMMENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_IMAGE_PATH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_TIMESTAMP))
                );
                reviewList.add(review);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return reviewList;
    }

    // Tambahkan metode baru ini di DatabaseHelper
    public long addAddress(Address address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ADDRESS_USER_ID, address.getUserId());
        values.put(COLUMN_RECIPIENT_NAME, address.getRecipientName());
        values.put(COLUMN_STREET_ADDRESS, address.getStreetAddress());
        values.put(COLUMN_CITY, address.getCity());
        values.put(COLUMN_POSTAL_CODE, address.getPostalCode());
        values.put(COLUMN_DETAILS, address.getDetails());

        return db.insert(TABLE_ADDRESSES, null, values);
    }

    public List<Address> getAddressesByUserId(int userId) {
        List<Address> addressList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADDRESSES, null, COLUMN_ADDRESS_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Address address = new Address(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS_USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPIENT_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STREET_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTAL_CODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DETAILS))
                );
                addressList.add(address);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return addressList;
    }

    public List<Produk> getProductsByCategory(String category) {
        List<Produk> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            // Query untuk memilih produk dengan kategori tertentu
            String selection = COLUMN_KATEGORI + " = ?";
            String[] selectionArgs = { category };
            cursor = db.query(TABLE_PRODUCTS, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUK_ID));
                    String nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA));
                    String harga = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HARGA));
                    String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESKRIPSI));
                    String kategori = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KATEGORI));
                    String jenis = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JENIS));
                    String warna = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WARNA));
                    String ukuran = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UKURAN));
                    String gambar = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GAMBAR));
                    int stok = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOK));
                    String tanggal = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TANGGAL));

                    productList.add(new Produk(id, nama, harga, deskripsi, kategori, jenis, warna, ukuran, gambar, stok, tanggal));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return productList;
    }
    public double getTotalRevenue() {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;

        // Gunakan rawQuery untuk operasi SUM()
        String query = "SELECT SUM(" + COLUMN_ORDER_TOTAL_AMOUNT + ") FROM " + TABLE_ORDERS + " WHERE " + COLUMN_ORDER_STATUS + " = 'Lunas'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Ambil hasil dari SUM, yang ada di kolom pertama (indeks 0)
            total = cursor.getDouble(0);
            cursor.close();
        }
        return total;
    }


    public int getTotalProductsSoldCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int totalItems = 0;
        // Ambil semua JSON dari pesanan yang sudah 'Selesai'
        Cursor cursor = db.query(TABLE_ORDERS, new String[]{COLUMN_ORDER_ITEMS_JSON}, COLUMN_ORDER_STATUS + " = ?", new String[]{"Lunas"}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String json = cursor.getString(0);
                try {
                    // Parsing JSON untuk mendapatkan kuantitas
                    org.json.JSONArray items = new org.json.JSONArray(json);
                    for (int i = 0; i < items.length(); i++) {
                        org.json.JSONObject item = items.getJSONObject(i);
                        totalItems += item.getInt("quantity"); // Asumsi key di JSON adalah 'quantity'
                    }
                } catch (org.json.JSONException e) {
                    Log.e("DatabaseHelper", "Error parsing order items JSON", e);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return totalItems;
    }

public int getTotalProductViews() {
    SQLiteDatabase db = this.getReadableDatabase();
    int totalViews = 0;
    String query = "SELECT SUM(" + COLUMN_PRODUCT_VIEWS + ") FROM " + TABLE_PRODUCTS;
    Cursor cursor = db.rawQuery(query, null);
    if (cursor != null && cursor.moveToFirst()) {
        totalViews = cursor.getInt(0);
        cursor.close();
    }
    return totalViews;
}
    public int getNewCustomerCountThisMonth() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;

        // Dapatkan tanggal awal bulan ini dalam format YYYY-MM-DD
        java.time.LocalDate today = java.time.LocalDate.now();
        String firstDayOfMonth = today.withDayOfMonth(1).toString();

        String query = "SELECT COUNT(" + COLUMN_ID + ") FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_CREATION_DATE + " >= ?";
        Cursor cursor = db.rawQuery(query, new String[]{firstDayOfMonth});
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }
    public int getTotalToko() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        // Query untuk menghitung baris dimana nama_toko tidak null dan tidak kosong
        String query = "SELECT COUNT(" + COLUMN_ID + ") FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USER_NAME + " IS NOT NULL AND " + COLUMN_USER_NAME + " != ''";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0); // Hasil COUNT ada di kolom pertama
            }
            cursor.close();
        }
        return count;
    }
    public int getTotalUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        // Query untuk menghitung baris dimana email tidak null dan tidak kosong
        String query = "SELECT COUNT(" + COLUMN_ID + ") FROM " + TABLE_USERS +
                " WHERE " + COLUMN_EMAIL + " IS NOT NULL AND " + COLUMN_EMAIL + " != ''";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0); // Hasil COUNT ada di kolom pertama
            }
            cursor.close();
        }
        return count;
    }
    public List<Toko> getAllToko() {
        List<Toko> tokoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query untuk memilih semua kolom dari user yang merupakan toko
        String selection = COLUMN_USER_NAME + " IS NOT NULL AND " + COLUMN_USER_NAME + " != ''";
        Cursor cursor = db.query(TABLE_USERS, null, selection, null, null, null, COLUMN_USER_NAME + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String namaToko = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME));
                String lokasi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ADDRESS));

                // Status default "Aktif"
                String status = "Aktif";

                tokoList.add(new Toko(id, namaToko, lokasi, status));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return tokoList;
    }

    public List<Kategori> getAllCategoriesWithCount() {
        List<Kategori> kategoriList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Query untuk mengelompokkan berdasarkan kategori dan menghitung jumlahnya
        String query = "SELECT " + COLUMN_KATEGORI + ", COUNT(*) as jumlah FROM " +
                TABLE_PRODUCTS + " GROUP BY " + COLUMN_KATEGORI;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String namaKategori = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KATEGORI));
                int jumlah = cursor.getInt(cursor.getColumnIndexOrThrow("jumlah"));
                kategoriList.add(new Kategori(namaKategori, jumlah));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return kategoriList;
    }

}