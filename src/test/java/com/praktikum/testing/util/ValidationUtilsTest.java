package com.praktikum.testing.util;
import com.praktikum.testing.model.Buku;
import com.praktikum.testing.model.Anggota;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit Test untuk ValidationUtils")
public class ValidationUtilsTest {

    @Test
    @DisplayName("Email valid harus mengembalikan true")
    void testEmailValid() {
        assertTrue(ValidationUtils.isValidEmail("mahasiswa@univ.ac.id"));
        assertTrue(ValidationUtils.isValidEmail("test@email.com"));
        assertTrue(ValidationUtils.isValidEmail("user123@domain.org"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "email-tanpa-at.com", "email9", "@domain.com", "email@@domain.com"})
    @DisplayName("Email tidak valid harus mengembalikan false")
    void testEmailTidakValid(String emailTidakValid) {
        assertFalse(ValidationUtils.isValidEmail(emailTidakValid));
    }

    @Test
    @DisplayName("Email null harus mengembalikan false")
    void testEmailNull() {
        assertFalse(ValidationUtils.isValidEmail(null));
    }

    @Test
    @DisplayName("Nomor telepon valid harus mengembalikan true")
    void testNomorTeleponValid() {
        assertTrue(ValidationUtils.isValidNomorTelepon("08123456789"));
        assertTrue(ValidationUtils.isValidNomorTelepon("08523456789"));
        assertTrue(ValidationUtils.isValidNomorTelepon("03123456789"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "123456789", "07123456789", "081234", "02123456789"})
    @DisplayName("Nomor telepon tidak valid harus mengembalikan false")
    void testNomorTeleponTidakValid(String teleponTidakValid) {
        assertFalse(ValidationUtils.isValidNomorTelepon(teleponTidakValid));
    }

    @Test
    @DisplayName("ISBN valid harus mengembalikan true")
    void testISBNValid() {
        assertTrue(ValidationUtils.isValidISBN("1234567890"));
        assertTrue(ValidationUtils.isValidISBN("1234567890123"));
        assertTrue(ValidationUtils.isValidISBN("123-456-789-0"));
    }

    @Test
    @DisplayName("Buku valid harus mengembalikan true")
    void testBukuValid() {
        Buku buku = new Buku("1234567890", "Pemrograman Java", "John Doe", 5, 130000.0);
        assertTrue(ValidationUtils.isValidBuku(buku));
    }

    @Test
    @DisplayName("Buku dengan data tidak valid harus mengembalikan false")
    void testBukuTidakValid() {
        // Buku null
        assertFalse(ValidationUtils.isValidBuku(null));

        // ISBN tidak valid
        Buku bukuIsbnTidakValid = new Buku("123", "Judul", "Pengarang", 5, 100000.0);
        assertFalse(ValidationUtils.isValidBuku(bukuIsbnTidakValid));

        // Jumlah negatif
        Buku bukuJumlahNegatif = new Buku("1234567890", "Judul", "Pengarang", -1, 0);
        assertFalse(ValidationUtils.isValidBuku(bukuJumlahNegatif));

        // Harga negatif
        Buku bukuHargaNegatif = new Buku("1234567890", "Judul", "Pengarang", 5, -1000.0);
        assertFalse(ValidationUtils.isValidBuku(bukuHargaNegatif));
    }

    @Test
    void testAnggotaValid() {
    Anggota anggota = new Anggota("A001", "John Doe", "john@univ.ac.id", "08123456789", Anggota.TipeAnggota.MAHASISWA);
        assertTrue(ValidationUtils.isValidAnggota(anggota));
    }

    @Test
    @DisplayName("String valid harus mengembalikan true")
    void testStringValid() {
        assertTrue(ValidationUtils.isValidString("text"));
        assertTrue(ValidationUtils.isValidString("text dengan spasi"));
        assertFalse(ValidationUtils.isValidString(""));
        assertFalse(ValidationUtils.isValidString(" "));
        assertFalse(ValidationUtils.isValidString(null));
    }

    @Test
    @DisplayName("Angka positif dan non-negatif harus valid")
    void testValidasiAngka() {
        assertTrue(ValidationUtils.isAngkaPositif((int) 1.0));
        assertTrue(ValidationUtils.isAngkaPositif((int) 100.5));
        assertFalse(ValidationUtils.isAngkaPositif((int) 0.0));
        assertFalse(ValidationUtils.isAngkaPositif((int) -1.0));

        assertTrue(ValidationUtils.isAngkaNonNegatif(0.0));
        assertTrue(ValidationUtils.isAngkaNonNegatif(1.0));
        assertFalse(ValidationUtils.isAngkaNonNegatif(-0.1));
    }
}