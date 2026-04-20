/* ===================================================
 * DATABASE QUẢN LÝ QUÁN CÀ PHÊ
 * =================================================== */

USE master
GO

IF EXISTS (SELECT * FROM sys.databases WHERE name = 'QuanLyQuanCaPhe')
BEGIN
    ALTER DATABASE QuanLyQuanCaPhe SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE QuanLyQuanCaPhe;
END
GO

CREATE DATABASE QuanLyQuanCaPhe
GO

USE QuanLyQuanCaPhe
GO

/* ====================================================
   1. TẠO BẢNG (ĐÚNG THỨ TỰ)
   ==================================================== */

-- Nhân viên
CREATE TABLE NhanVien (
    MaNV NVARCHAR(20) PRIMARY KEY,
    hoTen NVARCHAR(50) NOT NULL,
    gioiTinh NVARCHAR(10),
    sdt VARCHAR(20),
    email NVARCHAR(100),
    diaChi NVARCHAR(100),
    ngayVaoLam DATE DEFAULT GETDATE()
)
GO

-- Bàn
CREATE TABLE Ban (
    MaBan VARCHAR(10) PRIMARY KEY,
    SoBan INT,
    TrangThai NVARCHAR(20) 
        CHECK (TrangThai IN (N'Trống', N'Đang phục vụ', N'Đã đặt')),
    SucChua INT CHECK (SucChua > 0)
)
GO

-- Khách hàng
CREATE TABLE KhachHang (
    MaKH VARCHAR(10) PRIMARY KEY,
    TenKH NVARCHAR(100),
    SoDienThoai VARCHAR(15)
)
GO

-- Thuế
CREATE TABLE Thue (
    MaThue VARCHAR(20) PRIMARY KEY,
    TenThue NVARCHAR(100),
    ThueSuat FLOAT CHECK (ThueSuat >= 0)
)
GO

-- Sản phẩm
CREATE TABLE SanPham (
    MaSP VARCHAR(20) PRIMARY KEY,
    MaThue VARCHAR(20),
    TenSP NVARCHAR(200) NOT NULL,
    GiaBan DECIMAL(18,2) CHECK (GiaBan >= 0),
    DonViTinh NVARCHAR(50),
    MoTa NVARCHAR(MAX),
    SoLuongTon INT CHECK (SoLuongTon >= 0),
    HanSuDung DATE,
    NgayNhap DATE DEFAULT GETDATE(),

    FOREIGN KEY (MaThue) REFERENCES Thue(MaThue)
)
GO

-- Hóa đơn
CREATE TABLE HoaDon (
    MaHD INT IDENTITY(1,1) PRIMARY KEY,
    NgayLap DATETIME DEFAULT GETDATE(),
    TongTien DECIMAL(18,2) DEFAULT 0,
    TrangThai NVARCHAR(20)
        CHECK (TrangThai IN (N'Chưa thanh toán', N'Đã thanh toán', N'Đã hủy')),
    MaBan VARCHAR(10) NOT NULL,
    GhiChu NVARCHAR(100),
    MaNV NVARCHAR(20),

    FOREIGN KEY (MaBan) REFERENCES Ban(MaBan) ON DELETE NO ACTION,
    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV) ON DELETE NO ACTION
)
GO

-- Chi tiết hóa đơn
CREATE TABLE HoaDonChiTiet (
    MaHD INT,
    MaSP VARCHAR(20),
    SoLuong INT CHECK (SoLuong > 0),
    DonGia DECIMAL(18,2) CHECK (DonGia >= 0),

    PRIMARY KEY (MaHD, MaSP),

    FOREIGN KEY (MaHD) REFERENCES HoaDon(MaHD) ON DELETE CASCADE,
    FOREIGN KEY (MaSP) REFERENCES SanPham(MaSP)
)
GO

-- Phiếu đặt bàn
CREATE TABLE PhieuDatBan (
    MaPDB VARCHAR(10) PRIMARY KEY,
    MaKH VARCHAR(10),
    NgayDat DATETIME,
    NgayDen DATETIME,
    TrangThai NVARCHAR(20),

    FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH)
)
GO

-- Chi tiết phiếu đặt bàn
CREATE TABLE ChiTietPhieuDatBan (
    MaPDB VARCHAR(10),
    MaBan VARCHAR(10),
    GhiChu NVARCHAR(100),

    PRIMARY KEY (MaPDB, MaBan),

    FOREIGN KEY (MaPDB) REFERENCES PhieuDatBan(MaPDB),
    FOREIGN KEY (MaBan) REFERENCES Ban(MaBan)
)
GO

-- Tài khoản
CREATE TABLE TaiKhoan (
    UserName NVARCHAR(20) PRIMARY KEY,
    MatKhau NVARCHAR(100) NOT NULL,
    VaiTro NVARCHAR(20) DEFAULT N'Nhân viên',
    MaNV NVARCHAR(20) UNIQUE,

    FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV) ON DELETE CASCADE
)
GO

-- Khuyến mãi
CREATE TABLE KhuyenMai (
    MaKM VARCHAR(20) PRIMARY KEY,
    TenKM NVARCHAR(100),
    MucGiamGia FLOAT CHECK (MucGiamGia >= 0),
    NgayBatDau DATE,
    NgayKetThuc DATE
)
GO


/* ====================================================
   2. TRIGGER
   ==================================================== */

-- Tự động cập nhật tổng tiền hóa đơn
GO
CREATE TRIGGER trg_UpdateTongTien
ON HoaDonChiTiet
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    UPDATE HoaDon
    SET TongTien = ISNULL((
        SELECT SUM(SoLuong * DonGia)
        FROM HoaDonChiTiet
        WHERE HoaDonChiTiet.MaHD = HoaDon.MaHD
    ),0)
END
GO


/* ====================================================
   DỮ LIỆU MẪU
   ==================================================== */
-- FIX DATA THIẾU

-- Thue
INSERT INTO Thue VALUES ('T1', N'VAT 10%', 0.1)

-- Bàn
INSERT INTO Ban VALUES 
('B01', 1, N'Trống', 4),
('B02', 2, N'Trống', 4)

-- Nhân viên
INSERT INTO NhanVien VALUES 
('NV01', N'Admin', N'Nam', '0900', 'admin@gmail.com', N'HCM', GETDATE())

-- Sản phẩm
INSERT INTO SanPham VALUES 
('SP01', 'T1', N'Cà phê sữa', 30000, N'Ly', NULL, 100, NULL, GETDATE()),
('SP02', 'T1', N'Trà đào', 35000, N'Ly', NULL, 100, NULL, GETDATE())

-- Khách hàng
INSERT INTO KhachHang VALUES 
('KH01', N'Khách lẻ', '0900')
-- ========================
-- NHÂN VIÊN
-- ========================
INSERT INTO NhanVien VALUES 
('NV02', N'Lê Thị B', N'Nữ', '0912', 'b@gmail.com', N'HCM', GETDATE()),
('NV03', N'Phạm Văn C', N'Nam', '0913', 'c@gmail.com', N'HCM', GETDATE()),
('NV04', N'Hoàng Thị D', N'Nữ', '0914', 'd@gmail.com', N'HCM', GETDATE()),
('NV05', N'Nguyễn Văn E', N'Nam', '0915', 'e@gmail.com', N'HCM', GETDATE())

-- ========================
-- BÀN (nhiều trạng thái)
-- ========================
INSERT INTO Ban VALUES 
('B03', 3, N'Đang phục vụ', 6),
('B04', 4, N'Trống', 2),
('B05', 5, N'Đã đặt', 4),
('B06', 6, N'Trống', 8),
('B07', 7, N'Đang phục vụ', 4)

-- ========================
-- KHÁCH HÀNG
-- ========================
INSERT INTO KhachHang VALUES 
('KH02', N'Nguyễn Văn F', '0921'),
('KH03', N'Lê Thị G', '0922'),
('KH04', N'Trần Văn H', '0923'),
('KH05', N'Phạm Thị I', '0924')

-- ========================
-- THUẾ
-- ========================
INSERT INTO Thue VALUES 
('T2', N'VAT 5%', 0.05)

-- ========================
-- SẢN PHẨM (menu đa dạng)
-- ========================
INSERT INTO SanPham VALUES 
('SP03', 'T1', N'Cà phê đen', 25000, N'Ly', NULL, 100, NULL, GETDATE()),
('SP04', 'T1', N'Bạc xỉu', 32000, N'Ly', NULL, 100, NULL, GETDATE()),
('SP05', 'T1', N'Trà sữa trân châu', 40000, N'Ly', NULL, 100, NULL, GETDATE()),
('SP06', 'T2', N'Nước cam', 30000, N'Ly', NULL, 100, NULL, GETDATE()),
('SP07', 'T2', N'Sinh tố xoài', 45000, N'Ly', NULL, 100, NULL, GETDATE()),
('SP08', 'T2', N'Bánh tiramisu', 50000, N'Cái', NULL, 50, NULL, GETDATE()),
('SP09', 'T2', N'Bánh mì', 20000, N'Ổ', NULL, 80, NULL, GETDATE())

-- ========================
-- HÓA ĐƠN (nhiều trạng thái)
-- ========================
INSERT INTO HoaDon (TrangThai, MaBan, MaNV) VALUES 
(N'Chưa thanh toán', 'B03', 'NV02'),
(N'Đã thanh toán', 'B01', 'NV01'),
(N'Đã thanh toán', 'B02', 'NV03'),
(N'Chưa thanh toán', 'B07', 'NV04')

-- ========================
-- CHI TIẾT HÓA ĐƠN
-- ========================
INSERT INTO HoaDonChiTiet VALUES 
(2, 'SP01', 1, 30000),
(2, 'SP03', 2, 25000),

(3, 'SP05', 1, 40000),
(3, 'SP06', 2, 30000),

(4, 'SP02', 2, 35000),
(4, 'SP07', 1, 45000),

(4, 'SP04', 1, 32000),
(4, 'SP08', 2, 50000)

-- ========================
-- PHIẾU ĐẶT BÀN
-- ========================
INSERT INTO PhieuDatBan VALUES 
('PDB01', 'KH01', GETDATE(), DATEADD(HOUR, 2, GETDATE()), N'Đã đặt'),
('PDB02', 'KH02', GETDATE(), DATEADD(HOUR, 3, GETDATE()), N'Đã đặt'),
('PDB03', 'KH03', GETDATE(), DATEADD(DAY, 1, GETDATE()), N'Chờ')

-- ========================
-- CHI TIẾT ĐẶT BÀN
-- ========================
INSERT INTO ChiTietPhieuDatBan VALUES 
('PDB01', 'B05', N'Gần cửa sổ'),
('PDB02', 'B06', N'Tiệc sinh nhật'),
('PDB03', 'B04', N'Khách VIP')

-- ========================
-- TÀI KHOẢN
-- ========================
INSERT INTO TaiKhoan VALUES 
('admin', '123', N'Quản lý', 'NV01'),
('nv02', '123', N'Nhân viên', 'NV02'),
('nv03', '123', N'Nhân viên', 'NV03'),
('nv04', '123', N'Nhân viên', 'NV04')

-- ========================
-- KHUYẾN MÃI
-- ========================
INSERT INTO KhuyenMai VALUES 
('KM01', N'Giảm 10%', 0.1, GETDATE(), DATEADD(DAY, 30, GETDATE())),
('KM02', N'Giảm 20%', 0.2, GETDATE(), DATEADD(DAY, 10, GETDATE()))

/* ====================================================
   4. TEST / TRUY VẤN DEMO
   ==================================================== */

-- Doanh thu
SELECT SUM(TongTien) AS DoanhThu
FROM HoaDon 
WHERE TrangThai = N'Đã thanh toán'

-- Bàn đang phục vụ
SELECT * 
FROM Ban 
WHERE TrangThai = N'Đang phục vụ'

-- Top sản phẩm bán chạy
SELECT MaSP, SUM(SoLuong) AS TongBan
FROM HoaDonChiTiet
GROUP BY MaSP
ORDER BY TongBan DESC