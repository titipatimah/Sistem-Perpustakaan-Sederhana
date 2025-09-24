package com.praktikum.testing.repository;

import java.util.List;
import java.util.Optional;

public interface RepositoryBuku {
    // Simpan buku baru
    <Buku> boolean simpan(Buku buku);

    // Cari buku berdasarkan ISBN
    <Buku> Optional<Buku> cariByIsbn(String isbn);

    // Cari buku berdasarkan judul (bisa lebih dari 1)
    <Buku> List<Buku> cariByJudul(String judul);

    // Cari buku berdasarkan pengarang (bisa lebih dari 1)
    <Buku> List<Buku> cariByPengarang(String pengarang);

    // Hapus buku berdasarkan ISBN
    boolean hapus(String isbn);

    // Update jumlah buku tersedia berdasarkan ISBN
    boolean updateJumlahTersedia(String isbn, int jumlahTersediaBaru);

    // Ambil semua buku yang ada
    <Buku> List<Buku> cariSemua();
}

