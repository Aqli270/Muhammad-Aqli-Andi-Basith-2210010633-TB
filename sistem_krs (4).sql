-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jan 25, 2025 at 11:55 PM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sistem_krs`
--

-- --------------------------------------------------------

--
-- Table structure for table `dosen`
--

CREATE TABLE `dosen` (
  `id_dosen` int NOT NULL,
  `nama_dosen` varchar(100) NOT NULL,
  `fakultas` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `user_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `dosen`
--

INSERT INTO `dosen` (`id_dosen`, `nama_dosen`, `fakultas`, `email`, `user_id`) VALUES
(6, 'Jova', 'IT', 'jova@gmail.com', 10);

-- --------------------------------------------------------

--
-- Table structure for table `dosen_kelas`
--

CREATE TABLE `dosen_kelas` (
  `id_dosen` int NOT NULL,
  `id_kelas` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `dosen_kelas`
--

INSERT INTO `dosen_kelas` (`id_dosen`, `id_kelas`) VALUES
(6, 1),
(6, 2);

-- --------------------------------------------------------

--
-- Table structure for table `jadwal`
--

CREATE TABLE `jadwal` (
  `id_jadwal` int NOT NULL,
  `id_kelas` int DEFAULT NULL,
  `hari` varchar(20) NOT NULL,
  `waktu_mulai` varchar(150) NOT NULL,
  `waktu_selesai` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `jadwal`
--

INSERT INTO `jadwal` (`id_jadwal`, `id_kelas`, `hari`, `waktu_mulai`, `waktu_selesai`) VALUES
(1, 2, 'Senin', '9:30', '11:30'),
(2, 1, 'Kamis', '11:40', '13:00');

-- --------------------------------------------------------

--
-- Table structure for table `kelas`
--

CREATE TABLE `kelas` (
  `id_kelas` int NOT NULL,
  `id_mata_kuliah` int DEFAULT NULL,
  `nama_kelas` varchar(100) NOT NULL,
  `jadwal` varchar(100) NOT NULL,
  `ruang` varchar(50) NOT NULL,
  `kapasitas` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `kelas`
--

INSERT INTO `kelas` (`id_kelas`, `id_mata_kuliah`, `nama_kelas`, `jadwal`, `ruang`, `kapasitas`) VALUES
(1, 2, 'Kelas 4E', 'Test', 'Ruangan E', 30),
(2, 3, 'ALGO', 'Malam', 'Ruangan B', 20);

-- --------------------------------------------------------

--
-- Table structure for table `krs`
--

CREATE TABLE `krs` (
  `id_krs` int NOT NULL,
  `id_mahasiswa` int DEFAULT NULL,
  `id_kelas` int DEFAULT NULL,
  `status` varchar(50) DEFAULT 'Pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `krs`
--

INSERT INTO `krs` (`id_krs`, `id_mahasiswa`, `id_kelas`, `status`) VALUES
(12, 4, 2, 'Aktif'),
(19, 4, 2, 'Tidak'),
(25, 4, 1, 'Tidak'),
(28, NULL, 1, 'Aktif'),
(29, NULL, 2, 'Tidak Aktif'),
(30, NULL, 1, 'Aktif'),
(31, NULL, 2, 'Pending'),
(32, NULL, 1, ''),
(33, NULL, 2, ''),
(34, NULL, 1, 'Pending'),
(35, NULL, 1, ''),
(37, NULL, 2, ''),
(39, NULL, 1, 'Pending'),
(42, 4, 1, 'Aktif'),
(43, 4, 1, 'Pending'),
(44, 4, 2, 'Pending'),
(46, 4, 1, 'Pending'),
(47, 4, 1, 'Pending'),
(48, 4, 2, 'Pending');

-- --------------------------------------------------------

--
-- Table structure for table `mahasiswa`
--

CREATE TABLE `mahasiswa` (
  `id_mahasiswa` int NOT NULL,
  `nama_mahasiswa` varchar(100) NOT NULL,
  `program_studi` varchar(100) NOT NULL,
  `semester` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `user_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `mahasiswa`
--

INSERT INTO `mahasiswa` (`id_mahasiswa`, `nama_mahasiswa`, `program_studi`, `semester`, `email`, `user_id`) VALUES
(4, 'aqli basith andi', 'IT', '5', 'aqli@gmail.com', 5);

-- --------------------------------------------------------

--
-- Table structure for table `mahasiswa_kelas`
--

CREATE TABLE `mahasiswa_kelas` (
  `id_mahasiswa` int NOT NULL,
  `id_kelas` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mata_kuliah`
--

CREATE TABLE `mata_kuliah` (
  `id_mata_kuliah` int NOT NULL,
  `nama_mata_kuliah` varchar(100) NOT NULL,
  `sks` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `mata_kuliah`
--

INSERT INTO `mata_kuliah` (`id_mata_kuliah`, `nama_mata_kuliah`, `sks`) VALUES
(2, 'PBO 2', 4),
(3, 'Algoritma', 3),
(6, 'Visual ', 3);

-- --------------------------------------------------------

--
-- Table structure for table `mata_kuliah_dosen`
--

CREATE TABLE `mata_kuliah_dosen` (
  `id_mata_kuliah` int NOT NULL,
  `id_dosen` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `mata_kuliah_dosen`
--

INSERT INTO `mata_kuliah_dosen` (`id_mata_kuliah`, `id_dosen`) VALUES
(2, 6),
(3, 6);

-- --------------------------------------------------------

--
-- Table structure for table `nilai`
--

CREATE TABLE `nilai` (
  `id_nilai` int NOT NULL,
  `id_registrasi` int DEFAULT NULL,
  `nilai` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nilai`
--

INSERT INTO `nilai` (`id_nilai`, `id_registrasi`, `nilai`) VALUES
(1, 1, 'A'),
(31, 1, 'B'),
(32, 15, 'B'),
(34, 1, 'c'),
(35, 15, 'c'),
(37, 1, 'B'),
(38, 15, 'B'),
(40, 1, 'C'),
(41, 15, 'C'),
(43, 1, 'C'),
(44, 15, 'C'),
(45, 1, 'A+'),
(46, 15, 'A+');

-- --------------------------------------------------------

--
-- Table structure for table `registrasi_kelas`
--

CREATE TABLE `registrasi_kelas` (
  `id_registrasi` int NOT NULL,
  `id_kelas` int NOT NULL,
  `id_mahasiswa` int NOT NULL,
  `tanggal_registrasi` varchar(10) NOT NULL,
  `status` varchar(50) DEFAULT 'Pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `registrasi_kelas`
--

INSERT INTO `registrasi_kelas` (`id_registrasi`, `id_kelas`, `id_mahasiswa`, `tanggal_registrasi`, `status`) VALUES
(1, 2, 4, '2024-10-10', 'VALID'),
(15, 2, 4, '2025-01-24', 'VALID');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('admin','dosen','mahasiswa') NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `role`, `full_name`, `email`, `created_at`) VALUES
(2, 'JO\r\n', 'jo90', 'mahasiswa', 'Jonathan', 'jo@gmail.com', '2024-12-26 10:39:03'),
(5, 'aqli1', 'mahasiswa', 'mahasiswa', 'aqli basith', 'aqli@gmail.com', '2024-12-29 05:29:44'),
(10, 'jova ok', '2', 'dosen', 'Jova', 'jova@gmail.com', '2024-12-30 07:04:13'),
(17, 'aqli', '12345', 'admin', 'Muhammad Aqli Andi Basith', 'aqli1@gmail.com', '2025-01-24 12:32:51'),
(18, 'nova', 'novaayu', 'mahasiswa', 'Nova Ayunanda', 'ayunanda@gmail.com', '2025-01-24 12:33:33'),
(19, 'Jolly', 'jolly1', 'mahasiswa', 'jolly aja', 'jolly@gmail.com', '2025-01-24 12:37:27'),
(20, 'basith', 'ba1', 'dosen', 'basith aja 1', 'basith@gmail.com', '2025-01-24 12:56:11'),
(22, 'joy', 'joy1', 'admin', 'joy aja', 'joy@gmail.com', '2025-01-25 14:07:46');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `dosen`
--
ALTER TABLE `dosen`
  ADD PRIMARY KEY (`id_dosen`),
  ADD KEY `fk_email_users` (`email`),
  ADD KEY `fk_dosen_user_id` (`user_id`);

--
-- Indexes for table `dosen_kelas`
--
ALTER TABLE `dosen_kelas`
  ADD PRIMARY KEY (`id_dosen`,`id_kelas`),
  ADD KEY `id_kelas` (`id_kelas`);

--
-- Indexes for table `jadwal`
--
ALTER TABLE `jadwal`
  ADD PRIMARY KEY (`id_jadwal`),
  ADD KEY `id_kelas` (`id_kelas`);

--
-- Indexes for table `kelas`
--
ALTER TABLE `kelas`
  ADD PRIMARY KEY (`id_kelas`),
  ADD KEY `fk_mata_kuliah` (`id_mata_kuliah`);

--
-- Indexes for table `krs`
--
ALTER TABLE `krs`
  ADD PRIMARY KEY (`id_krs`),
  ADD KEY `krs_ibfk_1` (`id_mahasiswa`),
  ADD KEY `krs_ibfk_2` (`id_kelas`);

--
-- Indexes for table `mahasiswa`
--
ALTER TABLE `mahasiswa`
  ADD PRIMARY KEY (`id_mahasiswa`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `fk_mahasiswa_user_id` (`user_id`);

--
-- Indexes for table `mahasiswa_kelas`
--
ALTER TABLE `mahasiswa_kelas`
  ADD PRIMARY KEY (`id_mahasiswa`,`id_kelas`),
  ADD KEY `id_kelas` (`id_kelas`);

--
-- Indexes for table `mata_kuliah`
--
ALTER TABLE `mata_kuliah`
  ADD PRIMARY KEY (`id_mata_kuliah`);

--
-- Indexes for table `mata_kuliah_dosen`
--
ALTER TABLE `mata_kuliah_dosen`
  ADD PRIMARY KEY (`id_mata_kuliah`,`id_dosen`),
  ADD KEY `id_dosen` (`id_dosen`);

--
-- Indexes for table `nilai`
--
ALTER TABLE `nilai`
  ADD PRIMARY KEY (`id_nilai`),
  ADD KEY `fk_nilai_registrasi` (`id_registrasi`);

--
-- Indexes for table `registrasi_kelas`
--
ALTER TABLE `registrasi_kelas`
  ADD PRIMARY KEY (`id_registrasi`),
  ADD KEY `id_kelas` (`id_kelas`),
  ADD KEY `id_mahasiswa` (`id_mahasiswa`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `dosen`
--
ALTER TABLE `dosen`
  MODIFY `id_dosen` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `jadwal`
--
ALTER TABLE `jadwal`
  MODIFY `id_jadwal` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `kelas`
--
ALTER TABLE `kelas`
  MODIFY `id_kelas` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `krs`
--
ALTER TABLE `krs`
  MODIFY `id_krs` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;

--
-- AUTO_INCREMENT for table `mahasiswa`
--
ALTER TABLE `mahasiswa`
  MODIFY `id_mahasiswa` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `mata_kuliah`
--
ALTER TABLE `mata_kuliah`
  MODIFY `id_mata_kuliah` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `nilai`
--
ALTER TABLE `nilai`
  MODIFY `id_nilai` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `registrasi_kelas`
--
ALTER TABLE `registrasi_kelas`
  MODIFY `id_registrasi` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `dosen`
--
ALTER TABLE `dosen`
  ADD CONSTRAINT `fk_dosen_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_email_users` FOREIGN KEY (`email`) REFERENCES `users` (`email`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `dosen_kelas`
--
ALTER TABLE `dosen_kelas`
  ADD CONSTRAINT `dosen_kelas_ibfk_1` FOREIGN KEY (`id_dosen`) REFERENCES `dosen` (`id_dosen`) ON DELETE CASCADE,
  ADD CONSTRAINT `dosen_kelas_ibfk_2` FOREIGN KEY (`id_kelas`) REFERENCES `kelas` (`id_kelas`);

--
-- Constraints for table `jadwal`
--
ALTER TABLE `jadwal`
  ADD CONSTRAINT `jadwal_ibfk_1` FOREIGN KEY (`id_kelas`) REFERENCES `kelas` (`id_kelas`);

--
-- Constraints for table `kelas`
--
ALTER TABLE `kelas`
  ADD CONSTRAINT `fk_mata_kuliah` FOREIGN KEY (`id_mata_kuliah`) REFERENCES `mata_kuliah` (`id_mata_kuliah`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `kelas_ibfk_1` FOREIGN KEY (`id_mata_kuliah`) REFERENCES `mata_kuliah` (`id_mata_kuliah`);

--
-- Constraints for table `krs`
--
ALTER TABLE `krs`
  ADD CONSTRAINT `krs_ibfk_1` FOREIGN KEY (`id_mahasiswa`) REFERENCES `mahasiswa` (`id_mahasiswa`) ON DELETE SET NULL,
  ADD CONSTRAINT `krs_ibfk_2` FOREIGN KEY (`id_kelas`) REFERENCES `kelas` (`id_kelas`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `mahasiswa`
--
ALTER TABLE `mahasiswa`
  ADD CONSTRAINT `fk_mahasiswa_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_user_id_new` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `mahasiswa_kelas`
--
ALTER TABLE `mahasiswa_kelas`
  ADD CONSTRAINT `mahasiswa_kelas_ibfk_1` FOREIGN KEY (`id_mahasiswa`) REFERENCES `mahasiswa` (`id_mahasiswa`) ON DELETE CASCADE,
  ADD CONSTRAINT `mahasiswa_kelas_ibfk_2` FOREIGN KEY (`id_kelas`) REFERENCES `kelas` (`id_kelas`);

--
-- Constraints for table `mata_kuliah_dosen`
--
ALTER TABLE `mata_kuliah_dosen`
  ADD CONSTRAINT `mata_kuliah_dosen_ibfk_1` FOREIGN KEY (`id_mata_kuliah`) REFERENCES `mata_kuliah` (`id_mata_kuliah`),
  ADD CONSTRAINT `mata_kuliah_dosen_ibfk_2` FOREIGN KEY (`id_dosen`) REFERENCES `dosen` (`id_dosen`);

--
-- Constraints for table `nilai`
--
ALTER TABLE `nilai`
  ADD CONSTRAINT `fk_nilai_registrasi` FOREIGN KEY (`id_registrasi`) REFERENCES `registrasi_kelas` (`id_registrasi`) ON DELETE CASCADE,
  ADD CONSTRAINT `nilai_ibfk_1` FOREIGN KEY (`id_registrasi`) REFERENCES `registrasi_kelas` (`id_registrasi`);

--
-- Constraints for table `registrasi_kelas`
--
ALTER TABLE `registrasi_kelas`
  ADD CONSTRAINT `registrasi_kelas_ibfk_1` FOREIGN KEY (`id_kelas`) REFERENCES `kelas` (`id_kelas`) ON DELETE CASCADE,
  ADD CONSTRAINT `registrasi_kelas_ibfk_2` FOREIGN KEY (`id_mahasiswa`) REFERENCES `mahasiswa` (`id_mahasiswa`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
