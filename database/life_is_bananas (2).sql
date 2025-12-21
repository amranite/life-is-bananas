-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 21, 2025 at 02:37 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `life_is_bananas`
--

-- --------------------------------------------------------

--
-- Table structure for table `bio_engineers`
--

CREATE TABLE `bio_engineers` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `specialization` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bio_engineers`
--

INSERT INTO `bio_engineers` (`id`, `name`, `email`, `phone_number`, `specialization`) VALUES
(2, 'Dr. Test Engineer', 'test@lifeisbananas.com', '+32123456789', 'Advanced Fruit Processing'),
(3, 'Dr. Sarah Johnson', 'sarah@lifeisbananas.com', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `formulas`
--

CREATE TABLE `formulas` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `version` varchar(20) DEFAULT NULL,
  `preparation_method` text DEFAULT NULL,
  `creation_date` date NOT NULL,
  `last_modified` date NOT NULL,
  `bio_engineer_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `formulas`
--

INSERT INTO `formulas` (`id`, `name`, `version`, `preparation_method`, `creation_date`, `last_modified`, `bio_engineer_id`) VALUES
(1, 'Banana Chips Formula', '1.0', NULL, '2025-12-21', '2025-12-21', 3);

-- --------------------------------------------------------

--
-- Table structure for table `freeze_dry_instructions`
--

CREATE TABLE `freeze_dry_instructions` (
  `id` int(11) NOT NULL,
  `formula_name` varchar(100) NOT NULL,
  `version` varchar(20) DEFAULT NULL,
  `creation_date` date NOT NULL,
  `steps` text DEFAULT NULL,
  `remarks` text DEFAULT NULL,
  `formula_id` int(11) NOT NULL,
  `bio_engineer_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ingredients`
--

CREATE TABLE `ingredients` (
  `id` int(11) NOT NULL,
  `quantity` double NOT NULL,
  `unit` enum('GRAM','KILOGRAM','MILLILITER','PERCENTAGE','PIECE') NOT NULL,
  `sequence` int(11) DEFAULT NULL,
  `formula_id` int(11) NOT NULL,
  `raw_material_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `nutritional_values`
--

CREATE TABLE `nutritional_values` (
  `id` int(11) NOT NULL,
  `calories` double DEFAULT NULL,
  `proteins` double DEFAULT NULL,
  `carbohydrates` double DEFAULT NULL,
  `fats` double DEFAULT NULL,
  `fibers` double DEFAULT NULL,
  `sugars` double DEFAULT NULL,
  `salt` double DEFAULT NULL,
  `vitamin_a` double DEFAULT NULL,
  `vitamin_c` double DEFAULT NULL,
  `calcium` double DEFAULT NULL,
  `iron` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `parameters`
--

CREATE TABLE `parameters` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `value` varchar(50) NOT NULL,
  `unit` varchar(20) DEFAULT NULL,
  `minimum_limit` double DEFAULT NULL,
  `maximum_limit` double DEFAULT NULL,
  `critical` tinyint(1) NOT NULL DEFAULT 0,
  `freeze_dry_instruction_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `name` varchar(150) NOT NULL,
  `description` text DEFAULT NULL,
  `shelf_life` int(11) DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  `batch_number` varchar(50) DEFAULT NULL,
  `production_date` date DEFAULT NULL,
  `status` enum('IN_DEVELOPMENT','TESTED','APPROVED','IN_PRODUCTION') NOT NULL DEFAULT 'IN_DEVELOPMENT',
  `formula_id` int(11) NOT NULL,
  `bio_engineer_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `description`, `shelf_life`, `weight`, `batch_number`, `production_date`, `status`, `formula_id`, `bio_engineer_id`) VALUES
(1, 'Tropical Banana Chips', 'Delicious freeze-dried banana snack', 365, 50, 'BATCH001', NULL, 'IN_DEVELOPMENT', 1, 3);

-- --------------------------------------------------------

--
-- Table structure for table `raw_materials`
--

CREATE TABLE `raw_materials` (
  `id` int(11) NOT NULL,
  `material_type` enum('FRUIT','VEGETABLE') NOT NULL,
  `name` varchar(100) NOT NULL,
  `organic_certified` tinyint(1) NOT NULL DEFAULT 0,
  `price` double DEFAULT NULL,
  `unit` varchar(20) DEFAULT NULL,
  `nutritional_value_id` int(11) NOT NULL,
  `ripeness_level` varchar(50) DEFAULT NULL,
  `sugar_content` double DEFAULT NULL,
  `acidity_level` double DEFAULT NULL,
  `firmness` varchar(50) DEFAULT NULL,
  `moisture_content` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bio_engineers`
--
ALTER TABLE `bio_engineers`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `formulas`
--
ALTER TABLE `formulas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `bio_engineer_id` (`bio_engineer_id`);

--
-- Indexes for table `freeze_dry_instructions`
--
ALTER TABLE `freeze_dry_instructions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `formula_id` (`formula_id`),
  ADD KEY `bio_engineer_id` (`bio_engineer_id`);

--
-- Indexes for table `ingredients`
--
ALTER TABLE `ingredients`
  ADD PRIMARY KEY (`id`),
  ADD KEY `formula_id` (`formula_id`),
  ADD KEY `raw_material_id` (`raw_material_id`);

--
-- Indexes for table `nutritional_values`
--
ALTER TABLE `nutritional_values`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `parameters`
--
ALTER TABLE `parameters`
  ADD PRIMARY KEY (`id`),
  ADD KEY `freeze_dry_instruction_id` (`freeze_dry_instruction_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD KEY `formula_id` (`formula_id`),
  ADD KEY `bio_engineer_id` (`bio_engineer_id`);

--
-- Indexes for table `raw_materials`
--
ALTER TABLE `raw_materials`
  ADD PRIMARY KEY (`id`),
  ADD KEY `nutritional_value_id` (`nutritional_value_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bio_engineers`
--
ALTER TABLE `bio_engineers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `formulas`
--
ALTER TABLE `formulas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `freeze_dry_instructions`
--
ALTER TABLE `freeze_dry_instructions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ingredients`
--
ALTER TABLE `ingredients`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `nutritional_values`
--
ALTER TABLE `nutritional_values`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `parameters`
--
ALTER TABLE `parameters`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `raw_materials`
--
ALTER TABLE `raw_materials`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `formulas`
--
ALTER TABLE `formulas`
  ADD CONSTRAINT `formulas_ibfk_1` FOREIGN KEY (`bio_engineer_id`) REFERENCES `bio_engineers` (`id`);

--
-- Constraints for table `freeze_dry_instructions`
--
ALTER TABLE `freeze_dry_instructions`
  ADD CONSTRAINT `freeze_dry_instructions_ibfk_1` FOREIGN KEY (`formula_id`) REFERENCES `formulas` (`id`),
  ADD CONSTRAINT `freeze_dry_instructions_ibfk_2` FOREIGN KEY (`bio_engineer_id`) REFERENCES `bio_engineers` (`id`);

--
-- Constraints for table `ingredients`
--
ALTER TABLE `ingredients`
  ADD CONSTRAINT `ingredients_ibfk_1` FOREIGN KEY (`formula_id`) REFERENCES `formulas` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `ingredients_ibfk_2` FOREIGN KEY (`raw_material_id`) REFERENCES `raw_materials` (`id`);

--
-- Constraints for table `parameters`
--
ALTER TABLE `parameters`
  ADD CONSTRAINT `parameters_ibfk_1` FOREIGN KEY (`freeze_dry_instruction_id`) REFERENCES `freeze_dry_instructions` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`formula_id`) REFERENCES `formulas` (`id`),
  ADD CONSTRAINT `products_ibfk_2` FOREIGN KEY (`bio_engineer_id`) REFERENCES `bio_engineers` (`id`);

--
-- Constraints for table `raw_materials`
--
ALTER TABLE `raw_materials`
  ADD CONSTRAINT `raw_materials_ibfk_1` FOREIGN KEY (`nutritional_value_id`) REFERENCES `nutritional_values` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
