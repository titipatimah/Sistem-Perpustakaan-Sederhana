package com.praktikum.testing.repository;

import com.praktikum.testing.model.Buku;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Mock implementation dari RepositoryBuku untuk keperluan testing
 * Menggunakan in-memory storage dengan ConcurrentHashMap
 */
public class MockRepositoryBuku implements RepositoryBuku {

    private final Map<String, Buku> bukuMap = new ConcurrentHashMap<>();

    @Override
    public boolean simpan(Buku buku) {
        if (buku == null || buku.getIsbn() == null || buku.getIsbn().trim().isEmpty()) {
            return false;
        }
        bukuMap.put(buku.getIsbn(), buku); // overwrite kalau ISBN sudah ada
        return true;
    }

    @Override
    public Optional<Buku> cariByIsbn(String isbn) {
        if (isbn == null) return Optional.empty();
        return Optional.ofNullable(bukuMap.get(isbn));
    }

    @Override
    public List<Buku> cariByJudul(String judul) {
        if (judul == null || judul.trim().isEmpty()) return new ArrayList<>();
        return bukuMap.values().stream()
                .filter(b -> b.getJudul().toLowerCase().contains(judul.toLowerCase().trim()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Buku> cariByPengarang(String pengarang) {
        if (pengarang == null || pengarang.trim().isEmpty()) return new ArrayList<>();
        return bukuMap.values().stream()
                .filter(b -> b.getPengarang().toLowerCase().contains(pengarang.toLowerCase().trim()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hapus(String isbn) {
        if (isbn == null) return false;
        return bukuMap.remove(isbn) != null;
    }

    @Override
    public boolean updateJumlahTersedia(String isbn, int jumlahTersediaBaru) {
        if (isbn == null || jumlahTersediaBaru < 0) return false;
        Buku buku = bukuMap.get(isbn);
        if (buku == null) return false;
        if (jumlahTersediaBaru > buku.getJumlahTotal()) return false;
        buku.setJumlahTersedia(jumlahTersediaBaru);
        return true;
    }

    @Override
    public List<Buku> cariSemua() {
        return new ArrayList<>(bukuMap.values());
    }

    // ===================== Utility khusus testing =====================
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
