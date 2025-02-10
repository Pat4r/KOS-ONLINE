-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 31, 2025 at 02:31 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kosonline`
--

-- --------------------------------------------------------

--
-- Table structure for table `kamar`
--

CREATE TABLE `kamar` (
  `ID_Kamar` int(11) NOT NULL,
  `Nomor_Kamar` varchar(10) NOT NULL,
  `Status` enum('Kosong','Terisi') NOT NULL,
  `Harga_Per_Bulan` decimal(10,2) NOT NULL,
  `Fasilitas` text DEFAULT NULL,
  `Foto_Kamar` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kamar`
--

INSERT INTO `kamar` (`ID_Kamar`, `Nomor_Kamar`, `Status`, `Harga_Per_Bulan`, `Fasilitas`, `Foto_Kamar`) VALUES
(1, '01', 'Kosong', 1000000.00, '● AC\n● WiFi\n● Kamar Mandi Dalam\n● Tempat Tidur Queen Size\n● Lemari Pakaian', NULL),
(2, '02', 'Terisi', 900000.00, '● Kipas Angin\n● WiFi\n● Kamar Mandi Luar\n● Tempat Tidur Single\n● Meja Belajar', NULL),
(3, '03', 'Kosong', 1100000.00, '● AC\n● WiFi\n● Kamar Mandi Dalam\n● TV 32 inch\n● Tempat Tidur Queen Size', NULL),
(4, '04', 'Kosong', 1500000.00, '● AC\n● WiFi Premium\n● Kamar Mandi Dalam\n● Smart TV 42 inch\n● Mini Kulkas', NULL),
(5, '05', 'Terisi', 900000.00, '● Kipas Angin\n● WiFi\n● Kamar Mandi Luar\n● Meja Belajar\n● Rak Buku', NULL),
(6, '06', 'Kosong', 1300000.00, '● AC\n● WiFi Premium\n● Kamar Mandi Dalam\n● TV\n● Sofa', NULL),
(7, '07', 'Kosong', 1100000.00, '● AC\n● WiFi\n● Kamar Mandi Dalam\n● Lemari Pakaian\n● Meja Kerja', NULL),
(8, '08', 'Kosong', 1400000.00, '● AC\n● WiFi Premium\n● Kamar Mandi Dalam\n● Smart TV\n● Balkon Pribadi', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `pengguna`
--

CREATE TABLE `pengguna` (
  `ID_Pengguna` int(11) NOT NULL,
  `Nama_Pengguna` varchar(50) NOT NULL,
  `Password` varchar(100) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `Nomor_HP` varchar(15) DEFAULT NULL,
  `Tipe_Pengguna` enum('Pencari Kos','Pemilik Kos') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pengguna`
--

INSERT INTO `pengguna` (`ID_Pengguna`, `Nama_Pengguna`, `Password`, `Email`, `Nomor_HP`, `Tipe_Pengguna`) VALUES
(1, 'Taufik', 'Taufik123', 'taufik123@gmail.com', NULL, 'Pencari Kos'),
(2, 'opik', 'opik123', 'opik123@gmail.com', NULL, 'Pemilik Kos'),
(3, 'iyes', 'iyes123', 'iyes123@gmail.com', NULL, 'Pencari Kos'),
(4, 'lucky', 'lucky123', 'lucky12@gmail.com', '089765456787656', 'Pencari Kos');

-- --------------------------------------------------------

--
-- Table structure for table `penyewaan`
--

CREATE TABLE `penyewaan` (
  `ID_Penyewaan` int(11) NOT NULL,
  `ID_Pengguna` int(11) DEFAULT NULL,
  `ID_Kamar` int(11) DEFAULT NULL,
  `Tanggal_Mulai` date NOT NULL,
  `Durasi_Sewa` int(11) NOT NULL,
  `Total_Pembayaran` decimal(10,2) NOT NULL,
  `Status_Pembayaran` enum('Menunggu','Dibayar','Dibatalkan') NOT NULL,
  `Bukti_Pembayaran` varchar(255) DEFAULT NULL,
  `Tanggal_Pembayaran` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `penyewaan`
--

INSERT INTO `penyewaan` (`ID_Penyewaan`, `ID_Pengguna`, `ID_Kamar`, `Tanggal_Mulai`, `Durasi_Sewa`, `Total_Pembayaran`, `Status_Pembayaran`, `Bukti_Pembayaran`, `Tanggal_Pembayaran`) VALUES
(63, 1, 2, '2025-01-30', 3, 2700000.00, 'Dibayar', 'C:\\Users\\Victus by Omen\\Pictures\\316326_56589b42-f013-49a9-a28d-2edecdd0c3e1.jpg', '2025-01-30 00:00:00'),
(64, 3, 5, '2025-01-30', 6, 5400000.00, 'Dibayar', 'C:\\Users\\Victus by Omen\\Documents\\WhatsApp Image 2024-08-13 at 19.02.18_7001b70e.jpg', '2025-01-31 00:00:00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `kamar`
--
ALTER TABLE `kamar`
  ADD PRIMARY KEY (`ID_Kamar`);

--
-- Indexes for table `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`ID_Pengguna`),
  ADD UNIQUE KEY `Nama_Pengguna` (`Nama_Pengguna`),
  ADD UNIQUE KEY `Email` (`Email`);

--
-- Indexes for table `penyewaan`
--
ALTER TABLE `penyewaan`
  ADD PRIMARY KEY (`ID_Penyewaan`),
  ADD KEY `ID_Pengguna` (`ID_Pengguna`),
  ADD KEY `ID_Kamar` (`ID_Kamar`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `kamar`
--
ALTER TABLE `kamar`
  MODIFY `ID_Kamar` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `pengguna`
--
ALTER TABLE `pengguna`
  MODIFY `ID_Pengguna` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `penyewaan`
--
ALTER TABLE `penyewaan`
  MODIFY `ID_Penyewaan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=65;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `penyewaan`
--
ALTER TABLE `penyewaan`
  ADD CONSTRAINT `penyewaan_ibfk_1` FOREIGN KEY (`ID_Pengguna`) REFERENCES `pengguna` (`ID_Pengguna`),
  ADD CONSTRAINT `penyewaan_ibfk_2` FOREIGN KEY (`ID_Kamar`) REFERENCES `kamar` (`ID_Kamar`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
