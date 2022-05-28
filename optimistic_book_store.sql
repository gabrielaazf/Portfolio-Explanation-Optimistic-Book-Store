-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 18, 2022 at 04:16 AM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 8.1.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `optimistic`
--

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

CREATE TABLE `book` (
  `BookSKU` int(11) NOT NULL,
  `BookISBN` varchar(13) NOT NULL,
  `BookName` varchar(255) NOT NULL,
  `BookGenre` varchar(50) NOT NULL,
  `PublisherID` int(11) NOT NULL,
  `BookPrice` int(11) NOT NULL,
  `BookStockQty` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `book`
--

INSERT INTO `book` (`BookSKU`, `BookISBN`, `BookName`, `BookGenre`, `PublisherID`, `BookPrice`, `BookStockQty`) VALUES
(1, '9781603094207', 'How to Influence your Friend', 'Other', 8, 189999, 37),
(2, '9781603094208', 'Become an Expert in Python Programming', 'Education', 4, 219999, 19),
(3, '9781603094209', 'Become an Expert in Java Programming', 'Education', 7, 219999, 10),
(4, '9781603094210', 'Become an Expert in C Programming', 'Education', 4, 220000, 18),
(5, '1234567654098', 'Wrinkle In Time', 'Fantasy', 1, 1200000, 45),
(6, '1234567891234', 'C++ Java Coding', 'Mystery', 6, 90000, 1),
(7, '9781603094215', 'How to get boyfriend', 'Other', 8, 189999, 7),
(8, '9781603094220', 'Become an Expert in Adobe Illuustrator', 'Education', 4, 219999, 9),
(9, '1234567896384', 'Susah Nya Jadi Orang', 'Mystery', 4, 123333, 20);

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `UserID` int(11) NOT NULL,
  `BookSKU` int(11) NOT NULL,
  `Qty` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `detail_order`
--

CREATE TABLE `detail_order` (
  `OrderID` int(11) NOT NULL,
  `BookSKU` int(11) NOT NULL,
  `Qty` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `detail_order`
--

INSERT INTO `detail_order` (`OrderID`, `BookSKU`, `Qty`) VALUES
(1, 5, 1),
(2, 7, 1),
(3, 2, 1),
(4, 5, 2),
(5, 1, 1),
(5, 6, 37);

-- --------------------------------------------------------

--
-- Table structure for table `header_order`
--

CREATE TABLE `header_order` (
  `OrderID` int(11) NOT NULL,
  `UserID` int(11) NOT NULL,
  `PaymentMethodID` int(11) NOT NULL,
  `CardNumber` varchar(16) DEFAULT NULL,
  `TransactionDate` date NOT NULL,
  `StatusOrder` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `header_order`
--

INSERT INTO `header_order` (`OrderID`, `UserID`, `PaymentMethodID`, `CardNumber`, `TransactionDate`, `StatusOrder`) VALUES
(1, 3, 1, '1234567812345678', '2022-01-16', 'Completed'),
(2, 3, 1, '1234567812345678', '2022-01-16', 'Packed'),
(3, 3, 1, '1234567812345678', '2022-01-16', 'Confirmed'),
(4, 3, 3, '082113131313', '2022-01-17', 'Confirmed'),
(5, 3, 1, '1234123412341234', '2022-01-17', 'Waiting');

-- --------------------------------------------------------

--
-- Table structure for table `payment_method`
--

CREATE TABLE `payment_method` (
  `PaymentMethodID` int(11) NOT NULL,
  `PaymentMethodName` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `payment_method`
--

INSERT INTO `payment_method` (`PaymentMethodID`, `PaymentMethodName`) VALUES
(1, 'Credit Card'),
(2, 'Debit Card'),
(3, 'E-wallet');

-- --------------------------------------------------------

--
-- Table structure for table `publisher`
--

CREATE TABLE `publisher` (
  `PublisherID` int(11) NOT NULL,
  `PublisherName` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `publisher`
--

INSERT INTO `publisher` (`PublisherID`, `PublisherName`) VALUES
(1, 'GramediaPustaka'),
(2, 'HarperCollins'),
(3, 'Simon & Schuster'),
(4, 'Dedalus Books'),
(5, 'Capstone Publishers'),
(6, 'BBC Books'),
(7, 'Abrams Books'),
(8, 'Ace Books');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `UserID` int(11) NOT NULL,
  `UserFullName` varchar(50) NOT NULL,
  `UserAddress` varchar(100) NOT NULL,
  `UserPhoneNumber` varchar(15) NOT NULL,
  `Username` varchar(30) NOT NULL,
  `UserPassword` varchar(50) NOT NULL,
  `UserEmail` varchar(50) NOT NULL,
  `UserGender` char(1) NOT NULL,
  `UserBirthDate` date NOT NULL,
  `UserRole` varchar(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`UserID`, `UserFullName`, `UserAddress`, `UserPhoneNumber`, `Username`, `UserPassword`, `UserEmail`, `UserGender`, `UserBirthDate`, `UserRole`) VALUES
(1, 'megan charlene', 'Jalan Wonosobo Jakarta Selatan', '+62812369231', 'megan.charlene', 'megan1234', 'megan.charlene@gmail.com', 'F', '1990-05-16', 'Staff'),
(2, 'Gerald Benny', 'Jalan Kebon Jeruk Jakarta', '+62830283621', 'benny.gerald', 'gerald123', 'benny.gerald', 'M', '2002-01-04', 'Customer'),
(3, 'Emily Indrakusuma', 'Jalan Puri Sentra Surabaya', '+628112345678', 'emily_indra', 'emily123', 'emily.indrakusuma@gmail.com', 'F', '2001-01-31', 'Customer'),
(4, 'Ewaldo Samuel', 'tangerang selatan', '+6282137237482', 'ewaldo', 'ewaldo123', 'ewaldo@gmail.com', 'M', '2001-01-13', 'Customer');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`BookSKU`),
  ADD KEY `FK_Book_PublisherID` (`PublisherID`);

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`UserID`,`BookSKU`),
  ADD KEY `FK_Cart_BookSKU` (`BookSKU`),
  ADD KEY `UserID` (`UserID`);

--
-- Indexes for table `detail_order`
--
ALTER TABLE `detail_order`
  ADD PRIMARY KEY (`OrderID`,`BookSKU`),
  ADD KEY `FK_DetailOrder_BookSKU` (`BookSKU`);

--
-- Indexes for table `header_order`
--
ALTER TABLE `header_order`
  ADD PRIMARY KEY (`OrderID`),
  ADD KEY `FK_HeaderOrder_PaymentMethodID` (`PaymentMethodID`),
  ADD KEY `FK_HeaderOrder_UserID` (`UserID`);

--
-- Indexes for table `payment_method`
--
ALTER TABLE `payment_method`
  ADD PRIMARY KEY (`PaymentMethodID`);

--
-- Indexes for table `publisher`
--
ALTER TABLE `publisher`
  ADD PRIMARY KEY (`PublisherID`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`UserID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `book`
--
ALTER TABLE `book`
  MODIFY `BookSKU` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `header_order`
--
ALTER TABLE `header_order`
  MODIFY `OrderID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `payment_method`
--
ALTER TABLE `payment_method`
  MODIFY `PaymentMethodID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `publisher`
--
ALTER TABLE `publisher`
  MODIFY `PublisherID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `UserID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `book`
--
ALTER TABLE `book`
  ADD CONSTRAINT `FK_Book_PublisherID` FOREIGN KEY (`PublisherID`) REFERENCES `publisher` (`PublisherID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `FK_Cart_BookSKU` FOREIGN KEY (`BookSKU`) REFERENCES `book` (`BookSKU`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_Cart_UserID` FOREIGN KEY (`UserID`) REFERENCES `user` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `detail_order`
--
ALTER TABLE `detail_order`
  ADD CONSTRAINT `FK_DetailOrder_BookSKU` FOREIGN KEY (`BookSKU`) REFERENCES `book` (`BookSKU`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_DetailOrder_OrderID` FOREIGN KEY (`OrderID`) REFERENCES `header_order` (`OrderID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `header_order`
--
ALTER TABLE `header_order`
  ADD CONSTRAINT `FK_HeaderOrder_PaymentMethodID` FOREIGN KEY (`PaymentMethodID`) REFERENCES `payment_method` (`PaymentMethodID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_HeaderOrder_UserID` FOREIGN KEY (`UserID`) REFERENCES `user` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
