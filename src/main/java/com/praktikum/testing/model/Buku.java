package com.praktikum.testing.model;

import java.util.Objects;

public class Buku {
    private String isbn;
    private String judul;
    private String pengarang;
    private int jumlahTotal;
    private int jumlahTersedia;
    private double harga;

    // Constructor kosong
    public Buku() {
    }

    // Constructor lengkap
    public Buku(String isbn, String judul, String pengarang, int jumlahTotal, double harga) {
        this.isbn = isbn;
        this.judul = judul;
        this.pengarang = pengarang;
        this.jumlahTotal = jumlahTotal;
        this.jumlahTersedia = jumlahTotal; // awalnya semua tersedia
        this.harga = harga;
    }

    // Getters dan Setters
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPengarang() {
        return pengarang;
    }

    public void setPengarang(String pengarang) {
        this.pengarang = pengarang;
    }

    public int getJumlahTotal() {
        return jumlahTotal;
    }

    public void setJumlahTotal(int jumlahTotal) {
        this.jumlahTotal = jumlahTotal;
    }

    public int getJumlahTersedia() {
        return jumlahTersedia;
    }

    public void setJumlahTersedia(int jumlahTersedia) {
        this.jumlahTersedia = jumlahTersedia;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    // Kurangi jumlah tersedia (untuk pinjam buku)
    public boolean pinjamBuku() {
        if (jumlahTersedia > 0) {
            jumlahTersedia--;
            return true;
        }
        return false;
    }

    // Tambah jumlah tersedia (untuk kembalikan buku)
    public void kembalikanBuku() {
        if (jumlahTersedia < jumlahTotal) {
            jumlahTersedia++;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Buku)) return false;
        Buku buku = (Buku) o;
        return Objects.equals(isbn, buku.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return "Buku{" +
                "isbn='" + isbn + '\'' +
                ", judul='" + judul + '\'' +
                ", pengarang='" + pengarang + '\'' +
                ", jumlahTotal=" + jumlahTotal +
                ", jumlahTersedia=" + jumlahTersedia +
                ", harga=" + harga +
                '}';
    }

    public boolean isTersedia() {
        return jumlahTersedia > 0;
    }

    public String getPenulis() {
        return null;
    }
}
