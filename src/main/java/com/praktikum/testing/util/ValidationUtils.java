package com.praktikum.testing.util;

import com.praktikum.testing.model.Buku;
import com.praktikum.testing.model.Anggota;

public class ValidationUtils {

    // Validasi email
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // Validasi email sederhana
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    // Validasi nomor telepon (format Indonesia)
    public static boolean isValidNomorTelepon(String telepon) {
        if (telepon == null || telepon.trim().isEmpty()) {
            return false;
        }

        // Hapus semua spasi dan tanda hubung
        String teleponBersih = telepon.replaceAll("[\\s-]", "");

        // Nomor telepon Indonesia harus dimulai dengan 08 atau +628
        // dan panjangnya 10-13 digit
        return teleponBersih.matches("^(08|\\+628)[0-9]{8,11}$");
    }

    // Validasi ISBN sederhana - 10 atau 13 digit
    public static boolean isValidISBN(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }

        // Hapus tanda hubung dan spasi
        String isbnBersih = isbn.replaceAll("[\\s-]", "");

        // Harus berupa 10 atau 13 digit
        return isbnBersih.matches("[0-9]{10}") || isbnBersih.matches("[0-9]{13}");
    }

    // Validasi Buku
    public static boolean isValidBuku(Buku buku) {
        if (buku == null) {
            return false;
        }

        return isValidISBN(buku.getIsbn()) &&
                isValidString(buku.getJudul()) &&
                isValidString(buku.getPenulis()) &&
                buku.getJumlahTersedia() >= 0 &&
                buku.getJumlahTersedia() <= buku.getJumlahTotal() &&
                isValidAngkaPositif(buku.getHarga());
    }

    // Validasi Anggota
    public static boolean isValidAnggota(Anggota anggota) {
        if (anggota == null) {
            return false;
        }

        return isValidString(anggota.getNama()) &&
                isValidString(anggota.getAnggotaID()) &&
                isValidEmail(anggota.getEmail()) &&
                isValidNomorTelepon(anggota.getTelepon());
    }

    // Validasi string (tidak null dan tidak kosong setelah trim)
    public static boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }

    // Validasi angka positif
    public static boolean isValidAngkaPositif(double angka) {
        return angka > 0;
    }

    // Validasi angka non-negatif
    public static boolean isAngkaNonNegatif(double angka) {
        return angka >= 0;
    }

    public static boolean isValidNISN(String number) {
        return false;
    }

    public static boolean isAngkaPositif(int i) {
        return false;
    }
}
