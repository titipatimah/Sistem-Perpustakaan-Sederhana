package com.praktikum.testing.service;

import com.praktikum.testing.model.book;
import com.praktikum.testing.model.Anggota;
import com.praktikum.testing.repository.repositorybuku;
import com.praktikum.testing.util.ValidationUtils;

import java.util.List;
import java.util.Optional;

public class ServicePerpustakaan {

    private final repositorybuku repositoryBuku;
    private final KalkulatorDenda kalkulatorDenda;

    public ServicePerpustakaan(repositorybuku repositoryBuku, KalkulatorDenda kalkulatorDenda) {
        this.repositoryBuku = repositoryBuku;
        this.kalkulatorDenda = kalkulatorDenda;
    }

    // ==================== Tambah Buku ====================
    public boolean tambahBuku(book buku) {
        if (!ValidationUtils.isValidBuku(buku)) {
            return false;
        }

        Optional<book> bukuExisting = repositoryBuku.cariByIsbn(buku.getIsbn());
        if (bukuExisting.isPresent()) {
            return false; // Buku sudah ada
        }

        return repositoryBuku.simpan(buku);
    }

    // ==================== Hapus Buku ====================
    public boolean hapusBuku(String isbn) {
        if (!ValidationUtils.isValidISBN(isbn)) {
            return false;
        }

        Optional<book> buku = repositoryBuku.cariByIsbn(isbn);
        if (!buku.isPresent()) {
            return false; // Buku tidak ditemukan
        }

        // Cek apakah ada yang sedang dipinjam
        if (buku.get().getJumlahTersedia() < buku.get().getJumlahTotal()) {
            return false; // Tidak bisa hapus karena ada yang dipinjam
        }

        return repositoryBuku.hapus(isbn);
    }

    // ==================== Cari Buku ====================
    public Optional<book> cariBukuByIsbn(String isbn) {
        if (!ValidationUtils.isValidISBN(isbn)) {
            return Optional.empty();
        }
        return repositoryBuku.cariByIsbn(isbn);
    }

    public List<book> cariBukuByJudul(String judul) {
        return repositoryBuku.cariByJudul(judul);
    }

    public List<book> cariBukuByPengarang(String pengarang) {
        return repositoryBuku.cariByPengarang(pengarang);
    }

    // ==================== Ketersediaan Buku ====================
    public boolean bukuTersedia(String isbn) {
        Optional<book> buku = repositoryBuku.cariByIsbn(isbn);
        return buku.isPresent() && buku.get().isTersedia();
    }

    public int getJumlahTersedia(String isbn) {
        Optional<book> buku = repositoryBuku.cariByIsbn(isbn);
        return buku.map(book::getJumlahTersedia).orElse(0);
    }

    // ==================== Pinjam Buku ====================
    public boolean pinjamBuku(String isbn, Anggota anggota) {
        // Validasi anggota
        if (!ValidationUtils.isValidAnggota(anggota) || !anggota.isAktif()) {
            return false;
        }

        // Cek apakah anggota masih bisa pinjam
        if (!anggota.bolehPinjamLagi()) {
            return false;
        }

        // Cek ketersediaan buku
        Optional<book> bukuOpt = repositoryBuku.cariByIsbn(isbn);
        if (!bukuOpt.isPresent() || !bukuOpt.get().isTersedia()) {
            return false;
        }

        book buku = bukuOpt.get();

        // Update jumlah tersedia
        boolean updateBerhasil = repositoryBuku.updateJumlahTersedia(isbn, buku.getJumlahTersedia() - 1);
        if (updateBerhasil) {
            anggota.tambahBukuDipinjam(isbn);
            return true;
        }
        return false;
    }

    // ==================== Kembalikan Buku ====================
    public boolean kembalikanBuku(String isbn, Anggota anggota) {
        // Validasi
        if (!ValidationUtils.isValidISBN(isbn) || anggota == null) {
            return false;
        }

        // Cek apakah anggota meminjam buku ini
        if (!anggota.getIdBukuDipinjam().contains(isbn)) {
            return false;
        }

        Optional<book> bukuOpt = repositoryBuku.cariByIsbn(isbn);
        if (!bukuOpt.isPresent()) {
            return false;
        }

        book buku = bukuOpt.get();

        // Update jumlah tersedia
        boolean updateBerhasil = repositoryBuku.updateJumlahTersedia(isbn, buku.getJumlahTersedia() + 1);
        if (updateBerhasil) {
            anggota.hapusBukuDipinjam(isbn);
            return true;
        }
        return false;
    }
}
