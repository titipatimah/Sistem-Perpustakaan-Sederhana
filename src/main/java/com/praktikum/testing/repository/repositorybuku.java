package com.praktikum.testing.repository;

import com.praktikum.testing.model.book;

import java.util.List;
import java.util.Optional;

public interface repositorybuku {
    boolean simpan(book buku);
    Optional<book> cariByIsbn(String isbn);
    List<book> cariByJudul(String judul);
    List<book> cariByPengarang(String pengarang);
    boolean hapus(String isbn);
    boolean updateJumlahTersedia(String isbn, int jumlahTersediaBaru);
    List<book> cariSemua();
}