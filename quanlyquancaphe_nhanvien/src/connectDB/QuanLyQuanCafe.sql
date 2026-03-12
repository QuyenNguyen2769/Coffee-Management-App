CREATE DATABASE QuanLyQuanCafe
GO

USE QuanLyQuanCafe
GO

-------------------------------------------------
-- Bảng Nhân Viên
-------------------------------------------------
CREATE TABLE NhanVien (
    maNV VARCHAR(10) PRIMARY KEY,
    hoTen NVARCHAR(100),
    sdt VARCHAR(15),
    diaChi NVARCHAR(200),
    gioiTinh NVARCHAR(10),
    ngaySinh DATE,
    email VARCHAR(100),
    ngayVaoLam DATE
)

-------------------------------------------------
-- Bảng Tài Khoản
-------------------------------------------------
CREATE TABLE TaiKhoan (
    userName VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100),
    maNV VARCHAR(10),

    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
)

-------------------------------------------------
-- Bảng Loại Sản Phẩm
-------------------------------------------------
CREATE TABLE LoaiSanPham (
    maLoai VARCHAR(10) PRIMARY KEY,
    tenLoai NVARCHAR(100),
    moTa NVARCHAR(200)
)

-------------------------------------------------
-- Bảng Sản Phẩm
-------------------------------------------------
CREATE TABLE SanPham (
    maSP VARCHAR(10) PRIMARY KEY,
    tenSP NVARCHAR(100),
    giaBan FLOAT,
    donViTinh NVARCHAR(20),
    maLoai VARCHAR(10),
    moTa NVARCHAR(200),
    soLuongTon INT,
    hanSuDung DATE,
    ngayNhap DATE,

    FOREIGN KEY (maLoai) REFERENCES LoaiSanPham(maLoai)
)

-------------------------------------------------
-- Bảng Bàn
-------------------------------------------------
CREATE TABLE Ban (
    maBan VARCHAR(10) PRIMARY KEY,
    soBan INT,
    trangThai NVARCHAR(20),
    sucChua INT
)

-------------------------------------------------
-- Bảng Hóa Đơn
-------------------------------------------------
CREATE TABLE HoaDon (
    maHD VARCHAR(10) PRIMARY KEY,
    ngayLap DATETIME,
    tongTien FLOAT,
    trangThai NVARCHAR(30),
    ghiChu NVARCHAR(200),
    maNV VARCHAR(10),
    maBan VARCHAR(10),

    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maBan) REFERENCES Ban(maBan)
)

-------------------------------------------------
-- Bảng Chi Tiết Hóa Đơn
-------------------------------------------------
CREATE TABLE ChiTietHoaDon (
    maCTHD VARCHAR(10) PRIMARY KEY,
    maHD VARCHAR(10),
    maSP VARCHAR(10),
    soLuong INT,
    donGia FLOAT,
    thanhTien FLOAT,

    FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),
    FOREIGN KEY (maSP) REFERENCES SanPham(maSP)
)