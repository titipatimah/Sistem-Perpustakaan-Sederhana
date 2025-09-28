package com.praktikum.testing.service;

import com.praktikum.testing.model.Anggota;
import com.praktikum.testing.model.Buku;
import com.praktikum.testing.model.Peminjaman;
import com.praktikum.testing.repository.RepositoryBuku;
import com.praktikum.testing.util.ValidationUtils;

import java.time.LocalDate;
import java.util.*;

/**
 * Service utama untuk mengelola logika perpustakaan:
 * - Manajemen Buku
 * - Peminjaman / Pengembalian
 * - Perhitungan Denda
 */
public class ServicePerpustakaan {

    private final RepositoryBuku repositoryBuku;
    private final KalkulatorDenda kalkulatorDenda;
    private final Map<String, Peminjaman> riwayatPeminjaman = new HashMap<>();

    public ServicePerpustakaan(RepositoryBuku repositoryBuku, KalkulatorDenda kalkulatorDenda) {
        this.repositoryBuku = repositoryBuku;
        this.kalkulatorDenda = kalkulatorDenda;
    }

    // ================== Manajemen Buku ==================

    public boolean tambahBuku(Buku buku) {
        if (!ValidationUtils.isValidBuku(buku)) return false;
        if (repositoryBuku.cariByIsbn(buku.getIsbn()).isPresent()) return false;
        return repositoryBuku.simpan(buku);
    }

    public boolean hapusBuku(String isbn) {
        if (!ValidationUtils.isValidISBN(isbn)) return false;
        Optional<Buku> bukuOpt = repositoryBuku.cariByIsbn(isbn);
        if (bukuOpt.isEmpty()) return false;

        Buku buku = bukuOpt.get();
        // Tidak boleh hapus kalau masih ada buku yang dipinjam
        if (buku.getJumlahTersedia() < buku.getJumlahTotal()) {
            return false;
        }
        return repositoryBuku.hapus(isbn);
    }

    public Optional<Buku> cariBukuByIsbn(String isbn) {
        if (!ValidationUtils.isValidISBN(isbn)) return Optional.empty();
        return repositoryBuku.cariByIsbn(isbn);
    }

    public List<Buku> cariBukuByJudul(String judul) {
        return ValidationUtils.isValidString(judul) ? repositoryBuku.cariByJudul(judul) : List.of();
    }

    public List<Buku> cariBukuByPengarang(String pengarang) {
        return ValidationUtils.isValidString(pengarang) ? repositoryBuku.cariByPengarang(pengarang) : List.of();
    }

    public boolean bukuTersedia(String isbn) {
        return repositoryBuku.cariByIsbn(isbn)
                .map(Buku::isTersedia)
                .orElse(false);
    }

    public int getJumlahTersedia(String isbn) {
        return repositoryBuku.cariByIsbn(isbn)
                .map(Buku::getJumlahTersedia)
                .orElse(0);
    }

    // ================== Proses Pinjam ==================

    public boolean pinjamBuku(String isbn, Anggota anggota) {
        if (!ValidationUtils.isValidAnggota(anggota) || !anggota.isAktif()) return false;
        if (!anggota.bolehPinjamLagi()) return false;

        Optional<Buku> bukuOpt = repositoryBuku.cariByIsbn(isbn);
        if (bukuOpt.isEmpty() || !bukuOpt.get().isTersedia()) return false;

        Buku buku = bukuOpt.get();
        boolean updateBerhasil = repositoryBuku.updateJumlahTersedia(isbn, buku.getJumlahTersedia() - 1);

        if (updateBerhasil) {
            anggota.tambahBukuDipinjam(isbn);

            // buat data peminjaman
            String idPeminjaman = UUID.randomUUID().toString();
            LocalDate today = LocalDate.now();
            LocalDate jatuhTempo = today.plusDays(14); // default 14 hari
            Peminjaman peminjaman = new Peminjaman(idPeminjaman, anggota.getIdAnggota(), isbn, today, jatuhTempo);

            riwayatPeminjaman.put(idPeminjaman, peminjaman);
            return true;
        }
        return false;
    }

    // ================== Proses Kembali ==================

    public boolean kembalikanBuku(String isbn, Anggota anggota) {
        if (!anggota.getIdBukuDipinjam().contains(isbn)) return false;

        Optional<Buku> bukuOpt = repositoryBuku.cariByIsbn(isbn);
        if (bukuOpt.isEmpty()) return false;

        Buku buku = bukuOpt.get();
        boolean updateBerhasil = repositoryBuku.updateJumlahTersedia(isbn, buku.getJumlahTersedia() + 1);

        if (updateBerhasil) {
            anggota.hapusBukuDipinjam(isbn);
            return true;
        }
        return false;
    }

    // ================== Denda ==================

    public double hitungDenda(String idPeminjaman, Anggota anggota) {
        Peminjaman peminjaman = riwayatPeminjaman.get(idPeminjaman);
        if (peminjaman == null) return 0.0;
        return kalkulatorDenda.hitungDenda(peminjaman, anggota);
    }

    // ================== Utility ==================

    public List<Peminjaman> getRiwayatPeminjaman() {
        return new ArrayList<>(riwayatPeminjaman.values());
    }

    public Optional<Peminjaman> getPeminjamanById(String id) {
        return Optional.ofNullable(riwayatPeminjaman.get(id));
    }
}
