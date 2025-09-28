package com.praktikum.testing.repository;

import com.praktikum.testing.model.Buku;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Mock Repository Buku - Manual Mock Implementation")
class MockRepositoryBukuTest {
    private MockRepositoryBuku mockRepository;
    private Buku buku1;
    private Buku buku2;
    private Buku buku3;

    @BeforeEach
    void setUp() {
        mockRepository = new MockRepositoryBuku();

        buku1 = new Buku("1234567890", "Pemrograman Java", "John Doe", 5, 150000.0);
        buku2 = new Buku("0987654321", "Algoritma dan Struktur Data", "Jane Smith", 3, 120000.0);
        buku3 = new Buku("1111111111", "Java Advanced", "John Doe", 4, 180000.0);
    }

    @Test
    @DisplayName("Simpan buku baru - harus berhasil")
    void testSimpanBukuBaru() {
        boolean hasil = mockRepository.simpan(buku1);
        assertTrue(hasil);
        assertEquals(1, mockRepository.ukuran());
        assertTrue(mockRepository.mengandung("1234567890"));
    }

    @Test
    @DisplayName("Simpan buku null - harus gagal")
    void testSimpanBukuNull() {
        boolean hasil = mockRepository.simpan(null);
        assertFalse(hasil);
        assertEquals(0, mockRepository.ukuran());
    }

    @Test
    @DisplayName("Cari buku by ISBN - ditemukan")
    void testCariByIsbn() {
        mockRepository.simpan(buku1);
        Optional<Buku> hasil = mockRepository.cariByIsbn("1234567890");
        assertTrue(hasil.isPresent());
        assertEquals("Pemrograman Java", hasil.get().getJudul());
    }

    @Test
    @DisplayName("Update jumlah tersedia valid")
    void testUpdateJumlahTersediaValid() {
        mockRepository.simpan(buku1);
        boolean hasil = mockRepository.updateJumlahTersedia("1234567890", 3);
        assertTrue(hasil);
        Optional<Buku> updated = mockRepository.cariByIsbn("1234567890");
        assertTrue(updated.isPresent());
        assertEquals(3, updated.get().getJumlahTersedia());
    }
}
