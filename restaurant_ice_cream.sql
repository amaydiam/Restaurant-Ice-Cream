/*
SQLyog Ultimate v11.33 (64 bit)
MySQL - 10.1.25-MariaDB : Database - restaurant_ice_cream
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`restaurant_ice_cream` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `restaurant_ice_cream`;

/*Table structure for table `absensi_meja` */

DROP TABLE IF EXISTS `absensi_meja`;

CREATE TABLE `absensi_meja` (
  `id_absensi_meja` int(10) NOT NULL AUTO_INCREMENT,
  `id_meja` varchar(255) DEFAULT NULL,
  `id_one_signal` text,
  `waktu_absensi_meja` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_absensi_meja`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;

/*Data for the table `absensi_meja` */

insert  into `absensi_meja`(`id_absensi_meja`,`id_meja`,`id_one_signal`,`waktu_absensi_meja`) values (20,'6','8d967087-8666-49f4-8f59-935f847cc0a4','2017-08-01 14:57:11'),(21,'6','8d967087-8666-49f4-8f59-935f847cc0a4','2017-08-01 14:57:58');

/*Table structure for table `absensi_pegawai` */

DROP TABLE IF EXISTS `absensi_pegawai`;

CREATE TABLE `absensi_pegawai` (
  `id_absensi_pegawai` int(10) NOT NULL AUTO_INCREMENT,
  `tipe_pegawai` enum('1','2') DEFAULT NULL,
  `id_one_signal` text,
  `waktu_absensi_pegawai` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_absensi_pegawai`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

/*Data for the table `absensi_pegawai` */

insert  into `absensi_pegawai`(`id_absensi_pegawai`,`tipe_pegawai`,`id_one_signal`,`waktu_absensi_pegawai`) values (15,'1','5115fc5f-bde7-49d9-a056-ded8b0842cb8','2017-08-01 16:11:09'),(16,'1','5115fc5f-bde7-49d9-a056-ded8b0842cb8','2017-08-03 20:54:46');

/*Table structure for table `detail_pesanan` */

DROP TABLE IF EXISTS `detail_pesanan`;

CREATE TABLE `detail_pesanan` (
  `id_detail_pesanan` int(10) NOT NULL AUTO_INCREMENT,
  `id_pesanan` int(10) DEFAULT NULL,
  `id_menu` int(10) DEFAULT NULL,
  `jumlah_pesanan` int(10) DEFAULT NULL,
  `harga_pesanan` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_detail_pesanan`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;

/*Data for the table `detail_pesanan` */

insert  into `detail_pesanan`(`id_detail_pesanan`,`id_pesanan`,`id_menu`,`jumlah_pesanan`,`harga_pesanan`) values (16,7,2,2,'500'),(17,7,3,5,'600'),(18,8,1,5,'200'),(19,9,3,2,'600'),(20,10,3,2,'600'),(21,11,1,22,'200'),(22,12,1,22,'200'),(23,13,1,22,'200'),(24,14,1,22,'200'),(25,15,1,22,'200'),(26,16,1,22,'200'),(27,17,1,22,'200'),(28,18,1,22,'200'),(29,19,1,22,'200'),(30,20,1,22,'200'),(31,21,1,3,'200'),(32,22,1,2,'200');

/*Table structure for table `kategori_menu` */

DROP TABLE IF EXISTS `kategori_menu`;

CREATE TABLE `kategori_menu` (
  `id_kategori_menu` int(10) NOT NULL AUTO_INCREMENT,
  `nama_kategori_menu` varchar(255) DEFAULT NULL,
  `gambar_kategori_menu` text,
  PRIMARY KEY (`id_kategori_menu`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `kategori_menu` */

insert  into `kategori_menu`(`id_kategori_menu`,`nama_kategori_menu`,`gambar_kategori_menu`) values (1,'Aneka Makanant','/source/upload/image/gambar_kategori_menu/gambar_kategori_menu_1501551186.jpg'),(4,'makanan','/source/upload/image/gambar_kategori_menu/gambar_kategori_menu_1501768517.jpg');

/*Table structure for table `meja` */

DROP TABLE IF EXISTS `meja`;

CREATE TABLE `meja` (
  `id_meja` int(10) NOT NULL AUTO_INCREMENT,
  `nama_meja` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_meja`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

/*Data for the table `meja` */

insert  into `meja`(`id_meja`,`nama_meja`) values (1,'Meja 1'),(2,'Meja 2'),(3,'Meja 3'),(4,'Meja 4'),(5,'Meja 5'),(6,'Meja 6');

/*Table structure for table `menu` */

DROP TABLE IF EXISTS `menu`;

CREATE TABLE `menu` (
  `id_menu` int(10) NOT NULL AUTO_INCREMENT,
  `id_sub_kategori_menu` int(10) DEFAULT NULL,
  `nama_menu` varchar(255) DEFAULT NULL,
  `harga_menu` varchar(255) DEFAULT NULL,
  `stok_menu` int(10) DEFAULT NULL,
  `gambar_menu` text,
  PRIMARY KEY (`id_menu`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `menu` */

insert  into `menu`(`id_menu`,`id_sub_kategori_menu`,`nama_menu`,`harga_menu`,`stok_menu`,`gambar_menu`) values (1,1,'Nasi Goreng Gila','200',3,NULL),(2,2,'Ice Cream Nangka','500',6,NULL),(3,1,'ayam','600',NULL,'/source/upload/image/gambar_menu/gambar_menu_1501480992.jpg');

/*Table structure for table `pesanan` */

DROP TABLE IF EXISTS `pesanan`;

CREATE TABLE `pesanan` (
  `id_pesanan` int(10) NOT NULL AUTO_INCREMENT,
  `id_meja` int(10) DEFAULT NULL,
  `kode_pesanan` varchar(255) DEFAULT NULL,
  `nama_pemesan` varchar(255) DEFAULT NULL,
  `waktu_pesan` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status_pesanan` enum('Menunggu Pesanan','Pesanan Sedang Disiapkan','Pesanan Sedang Dinikmati','Pesanan Telah Dibayar','Pesanan Dibatalkan') DEFAULT 'Menunggu Pesanan',
  `catatan` text,
  PRIMARY KEY (`id_pesanan`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;

/*Data for the table `pesanan` */

insert  into `pesanan`(`id_pesanan`,`id_meja`,`kode_pesanan`,`nama_pemesan`,`waktu_pesan`,`status_pesanan`,`catatan`) values (6,6,'Meja6_1501483598','ok','2017-07-31 13:46:58','Pesanan Dibatalkan',''),(7,6,'Meja6_1501483753','gh','2017-07-31 13:50:03','Pesanan Dibatalkan','ok'),(8,4,'Meja4_1501551288','Amay','2017-08-01 08:35:41','Pesanan Dibatalkan','ok'),(9,4,'Meja4_1501560173','Amay','2017-08-01 11:04:31','Pesanan Dibatalkan','ayamnya jangan garing2'),(10,6,'Meja6_1501574309','amay','2017-08-01 14:59:05','Pesanan Dibatalkan',''),(11,6,'Meja6_1501574805','ghh','2017-08-01 15:07:02','Pesanan Dibatalkan',''),(12,6,'Meja6_1501574805','ghh','2017-08-01 16:11:42','Pesanan Dibatalkan',''),(13,6,'Meja6_1501574805','ghh','2017-08-01 16:12:34','Pesanan Dibatalkan',''),(14,6,'Meja6_1501574805','ghh','2017-08-01 16:13:20','Pesanan Sedang Dinikmati',''),(15,6,'Meja6_1501574805','ghh','2017-08-01 16:13:59','Pesanan Sedang Dinikmati',''),(16,6,'Meja6_1501574805','ghh','2017-08-01 16:28:43','Pesanan Sedang Dinikmati',''),(17,6,'Meja6_1501574805','ghh','2017-08-01 16:29:15','Pesanan Sedang Dinikmati',''),(18,6,'Meja6_1501574805','ghh','2017-08-01 16:29:40','Pesanan Sedang Dinikmati',''),(19,6,'Meja6_1501574805','ghh','2017-08-01 16:30:10','Pesanan Sedang Dinikmati',''),(20,6,'Meja6_1501574805','ghh','2017-08-01 16:31:14','Pesanan Sedang Dinikmati',''),(21,6,'Meja6_1501579889','ok','2017-08-01 16:31:50','Pesanan Sedang Dinikmati',''),(22,6,'Meja6_1501580070','ok','2017-08-01 16:34:48','Menunggu Pesanan','');

/*Table structure for table `sub_kategori_menu` */

DROP TABLE IF EXISTS `sub_kategori_menu`;

CREATE TABLE `sub_kategori_menu` (
  `id_sub_kategori_menu` int(10) NOT NULL AUTO_INCREMENT,
  `id_kategori_menu` int(10) DEFAULT NULL,
  `nama_sub_kategori_menu` varchar(255) DEFAULT NULL,
  `gambar_sub_kategori_menu` text,
  PRIMARY KEY (`id_sub_kategori_menu`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `sub_kategori_menu` */

insert  into `sub_kategori_menu`(`id_sub_kategori_menu`,`id_kategori_menu`,`nama_sub_kategori_menu`,`gambar_sub_kategori_menu`) values (1,1,'Nasi Goreng',NULL),(2,2,'Kopi',NULL),(3,2,'Jus','/source/upload/image/gambar_sub_kategori_menu/gambar_sub_kategori_menu_1501259782.jpg');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
