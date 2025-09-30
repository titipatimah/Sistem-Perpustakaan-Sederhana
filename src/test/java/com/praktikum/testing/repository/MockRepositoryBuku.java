package com.praktikum.testing.repository;

import com.praktikum.testing.model.book;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Mock implementation dari RepositoryBuku untuk keperluan testing
 * Menggunakan in-memory storage dengan ConcurrentHashMap
 *
 * Catatan: Ini hanya untuk demo! Dalam test yang sebenarnya,
 * sebaiknya gunakan Mockito untuk mocking repository.
 */
public class MockRepositoryBuku implements repositorybuku {
    private final Map<String, book> bukuMap = new ConcurrentHashMap<>();

    @Override
    public boolean simpan(book buku) {
        if (buku == null || buku.getIsbn() == null) {
            return false;
        }

        // Simulasi operasi simpan ke database
        bukuMap.put(buku.getIsbn(), buku);
        return true;
    }

    @Override
    public Optional<book> cariByIsbn(String isbn) {
        if (isbn == null) {
            return Optional.empty();
        }
        book buku = bukuMap.get(isbn);
        return Optional.ofNullable(buku);
    }

    @Override
    public List<book> cariByJudul(String judul) {
        if (judul == null || judul.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return bukuMap.values().stream()
                .filter(buku -> buku.getJudul().toLowerCase()
                        .contains(judul.toLowerCase().trim()))
                .collect(Collectors.toList());
    }

    @Override
    public List<book> cariByPengarang(String pengarang) {
        if (pengarang == null || pengarang.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return bukuMap.values().stream()
                .filter(buku -> buku.getPengarang().toLowerCase()
                        .contains(pengarang.toLowerCase().trim()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hapus(String isbn) {
        if (isbn == null) {
            return false;
        }
        book bukuDihapus = bukuMap.remove(isbn);
        return bukuDihapus != null;
    }

    @Override
    public boolean updateJumlahTersedia(String isbn, int jumlahTersediaBaru) {
        if (isbn == null || jumlahTersediaBaru < 0) {
            return false;
        }

        book buku = bukuMap.get(isbn);
        if (buku == null) {
            return false;
        }

        // Cek apakah jumlah tersedia baru valid
        if (jumlahTersediaBaru > buku.getJumlahTotal()) {
            return false;
        }

        buku.setJumlahTersedia(jumlahTersediaBaru);
        return true;
    }

    @Override
    public List<book> cariSemua() {
        return new ArrayList<>(bukuMap.values());
    }

    // Utility methods untuk testing
    public void bersihkan() {
        bukuMap.clear();
    }

    public int ukuran() {
        return bukuMap.size();
    }

    public boolean mengandung(String isbn) {
        return bukuMap.containsKey(isbn);
    }
}