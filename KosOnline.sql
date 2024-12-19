
-- Pembuatan database
CREATE DATABASE KosOnline;
USE KosOnline;

-- Tabel Pengguna
CREATE TABLE Pengguna (
    ID_Pengguna INT AUTO_INCREMENT PRIMARY KEY,
    Nama_Pengguna VARCHAR(100) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    Tipe_Pengguna ENUM('Pencari Kos', 'Pemilik Kos') NOT NULL
);

-- Tabel Pemilik Kos
CREATE TABLE PemilikKos (
    ID_Pemilik INT AUTO_INCREMENT PRIMARY KEY,
    Nama_Pemilik VARCHAR(100) NOT NULL,
    Alamat VARCHAR(255) NOT NULL,
    Nomor_Telepon VARCHAR(15) NOT NULL
);

-- Tabel Kos
CREATE TABLE Kos (
    ID_Kos INT AUTO_INCREMENT PRIMARY KEY,
    Nama_Kos VARCHAR(100) NOT NULL,
    Alamat VARCHAR(255) NOT NULL,
    Deskripsi TEXT,
    Harga DECIMAL(10, 2) NOT NULL,
    Status_Ketersediaan ENUM('Tersedia', 'Tidak Tersedia') NOT NULL,
    ID_Pemilik INT NOT NULL,
    FOREIGN KEY (ID_Pemilik) REFERENCES PemilikKos(ID_Pemilik) ON DELETE CASCADE
);

-- Tabel Pemesanan
CREATE TABLE Pemesanan (
    ID_Pemesanan INT AUTO_INCREMENT PRIMARY KEY,
    ID_Kos INT NOT NULL,
    ID_Pengguna INT NOT NULL,
    Tanggal_CheckIn DATE NOT NULL,
    Tanggal_CheckOut DATE NOT NULL,
    Status_Pemesanan ENUM('Terkonfirmasi', 'Dibatalkan') NOT NULL,
    FOREIGN KEY (ID_Kos) REFERENCES Kos(ID_Kos) ON DELETE CASCADE,
    FOREIGN KEY (ID_Pengguna) REFERENCES Pengguna(ID_Pengguna) ON DELETE CASCADE
);

-- Tabel Transaksi
CREATE TABLE Transaksi (
    ID_Transaksi INT AUTO_INCREMENT PRIMARY KEY,
    ID_Pemesanan INT NOT NULL,
    Jumlah_Pembayaran DECIMAL(10, 2) NOT NULL,
    Metode_Pembayaran VARCHAR(50),
    Tanggal_Transaksi DATE NOT NULL,
    FOREIGN KEY (ID_Pemesanan) REFERENCES Pemesanan(ID_Pemesanan) ON DELETE CASCADE
);

-- Tabel Ulasan Kos
CREATE TABLE Ulasan (
    ID_Ulasan INT AUTO_INCREMENT PRIMARY KEY,
    ID_Kos INT NOT NULL,
    ID_Pengguna INT NOT NULL,
    Rating INT CHECK(Rating BETWEEN 1 AND 5),
    Komentar TEXT,
    FOREIGN KEY (ID_Kos) REFERENCES Kos(ID_Kos) ON DELETE CASCADE,
    FOREIGN KEY (ID_Pengguna) REFERENCES Pengguna(ID_Pengguna) ON DELETE CASCADE
);
