/* ===================================================
   DATABASE QUAN LY QUAN CA PHE
=================================================== */

USE master;
GO

-- Xóa database cũ nếu đã tồn tại
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'QLCAFE')
BEGIN
    ALTER DATABASE QLCAFE SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE QLCAFE;
END
GO

CREATE DATABASE QLCAFE;
GO

USE QLCAFE;
GO


/* =======================
   1. BANG NHAN VIEN
======================= */
CREATE TABLE NhanVien (
    maNV INT IDENTITY PRIMARY KEY,
    hoTen NVARCHAR(100),
    sdt VARCHAR(15),
    diaChi NVARCHAR(200),
    gioiTinh NVARCHAR(10),
    ngaySinh DATE,
    email VARCHAR(100),
    ngayVaoLam DATE,
    luong FLOAT,
    chucVu NVARCHAR(50)  -- tuong ung vaiTro ben TaiKhoan
);

/* =======================
   2. TAI KHOAN
======================= */
CREATE TABLE TaiKhoan (
    userName VARCHAR(50) PRIMARY KEY,
    maNV INT,
    password VARCHAR(100),
    vaiTro NVARCHAR(50),
    trangThai NVARCHAR(50),
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
);

/* =======================
   3. BAN
======================= */
CREATE TABLE Ban (
    maBan INT IDENTITY PRIMARY KEY,
    soBan INT,
    trangThai NVARCHAR(50),
    sucChua INT,
    khuVuc NVARCHAR(50)
);

/* =======================
   4. KHACH HANG
======================= */
CREATE TABLE KhachHang (
    maKH INT IDENTITY PRIMARY KEY,
    hoTen NVARCHAR(100),
    sdt VARCHAR(15),
    diaChi NVARCHAR(200),
    diemTichLuy INT,
    email VARCHAR(100),
    loaiKhach NVARCHAR(50)  -- VD: Khach moi / Thuong / Than thiet / VIP
);

/* =======================
   5. PHIEU DAT BAN
======================= */
CREATE TABLE PhieuDatBan (
    maPDB INT IDENTITY PRIMARY KEY,
    maKH INT,
    ngayDat DATETIME,
    ngayDen DATETIME,
    trangThai NVARCHAR(50),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH)
);

/* =======================
   6. CHI TIET PHIEU DAT BAN
======================= */
CREATE TABLE ChiTietPhieuDatBan (
    maPDB INT,
    maBan INT,
    ghiChu NVARCHAR(200),
    PRIMARY KEY (maPDB, maBan),
    FOREIGN KEY (maPDB) REFERENCES PhieuDatBan(maPDB),
    FOREIGN KEY (maBan) REFERENCES Ban(maBan)
);

/* =======================
   7. THUE
======================= */
CREATE TABLE Thue (
    maThue INT IDENTITY PRIMARY KEY,
    tenThue NVARCHAR(50),
    thueSuat FLOAT
);

/* =======================
   8. SAN PHAM
======================= */
CREATE TABLE SanPham (
    maSP INT IDENTITY PRIMARY KEY,
    maThue INT,
    tenSP NVARCHAR(100),
    giaBan FLOAT,
    donViTinh NVARCHAR(50),
    moTa NVARCHAR(200),
    soLuongTon INT,
    hanSuDung DATE,
    ngayNhap DATE,
    FOREIGN KEY (maThue) REFERENCES Thue(maThue)
);

/* =======================
   9. KHUYEN MAI
======================= */
CREATE TABLE KhuyenMai (
    maKM INT IDENTITY PRIMARY KEY,
    tenKM NVARCHAR(100),
    mucGiamGia FLOAT,
    ngayBatDau DATE,
    ngayKetThuc DATE,
    dieuKienToiThieu FLOAT
);

/* =======================
   10. HOA DON
======================= */
CREATE TABLE HoaDon (
    maHD INT IDENTITY PRIMARY KEY,
    maNV INT,
    maBan INT,
    ngayLap DATE,
    tongTien FLOAT,
    trangThai NVARCHAR(50),
    ghiChu NVARCHAR(200),
    phuongThucThanhToan NVARCHAR(50),
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maBan) REFERENCES Ban(maBan)
);

/* =======================
   11. HOA DON CHI TIET
======================= */
CREATE TABLE HoaDonChiTiet (
    maHD INT,
    maSP INT,
    maKM INT NULL,
    soLuong INT,
    donGia FLOAT,
    thanhTien FLOAT,
    ghiChu NVARCHAR(200),
    PRIMARY KEY (maHD, maSP),
    FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),
    FOREIGN KEY (maSP) REFERENCES SanPham(maSP),
    FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM)
);

-----------------------------------------------------
-- DỮ LIỆU MẪU
-----------------------------------------------------

-- NhanVien
INSERT INTO NhanVien (hoTen, sdt, diaChi, gioiTinh, ngaySinh, email, ngayVaoLam, luong, chucVu) VALUES
-- Quản lý
(N'Nguyễn Văn A','0901234567',N'HCM',N'Nam','1985-01-01','a@gmail.com','2020-01-01',15000000,N'Quản lý'),

-- Thu ngân
(N'Trần Thị B','0907654321',N'HCM',N'Nữ','1995-05-10','b@gmail.com','2022-02-01',9000000,N'Thu ngân'),

-- Pha chế
(N'Lê Văn C','0912345678',N'HCM',N'Nam','1998-03-12','c@gmail.com','2023-03-01',8000000,N'Pha chế'),
(N'Phạm Thị D','0923456789',N'HCM',N'Nữ','1999-07-21','d@gmail.com','2023-06-01',7800000,N'Pha chế'),

-- Phục vụ
(N'Hoàng Văn E','0934567890',N'HCM',N'Nam','2001-09-15','e@gmail.com','2024-01-10',6500000,N'Phục vụ'),
(N'Võ Thị F','0945678901',N'HCM',N'Nữ','2002-11-30','f@gmail.com','2024-02-20',6200000,N'Phục vụ'),
(N'Đặng Văn G','0956789012',N'HCM',N'Nam','2000-04-18','g@gmail.com','2024-03-05',6300000,N'Phục vụ'),

-- Part-time
(N'Nguyễn Thị H','0967890123',N'HCM',N'Nữ','2003-08-08','h@gmail.com','2024-04-01',4000000,N'Part-time'),
(N'Trần Văn I','0978901234',N'HCM',N'Nam','2004-12-12','i@gmail.com','2024-04-10',4200000,N'Part-time');
INSERT INTO TaiKhoan VALUES
('admin',1,'123456',N'Quản lý',N'Hoạt động'),
('thungan',2,'123456',N'Thu ngân',N'Hoạt động'),
('phache1',3,'123456',N'Pha chế',N'Hoạt động'),
('phache2',4,'123456',N'Pha chế',N'Hoạt động'),
('phucvu1',5,'123456',N'Phục vụ',N'Hoạt động'),
('phucvu2',6,'123456',N'Phục vụ',N'Hoạt động');
-- Ban
INSERT INTO Ban (soBan, trangThai, sucChua, khuVuc) VALUES
-- Tầng 1 (khu thường)
(1, N'Trống', 2, N'Tầng 1'),
(2, N'Trống', 2, N'Tầng 1'),
(3, N'Đang dùng', 4, N'Tầng 1'),
(4, N'Trống', 4, N'Tầng 1'),
(5, N'Đã đặt', 6, N'Tầng 1'),

-- Tầng 2 (view đẹp)
(6, N'Trống', 2, N'Tầng 2'),
(7, N'Đang dùng', 2, N'Tầng 2'),
(8, N'Trống', 4, N'Tầng 2'),
(9, N'Đã đặt', 4, N'Tầng 2'),
(10, N'Trống', 6, N'Tầng 2'),

-- Ngoài trời
(11, N'Trống', 2, N'Sân vườn'),
(12, N'Đang dùng', 4, N'Sân vườn'),
(13, N'Trống', 4, N'Sân vườn'),
(14, N'Đã đặt', 6, N'Sân vườn'),

-- Phòng VIP
(15, N'Trống', 8, N'VIP'),
(16, N'Đang dùng', 10, N'VIP');

-- KhachHang
INSERT INTO KhachHang (hoTen, sdt, diaChi, diemTichLuy, email, loaiKhach) VALUES
-- Khách mới
(N'Nguyễn Minh Anh','0901000001',N'HCM',0,'anh@gmail.com',N'Khách mới'),
(N'Trần Quốc Bảo','0901000002',N'HCM',10,'bao@gmail.com',N'Khách mới'),

-- Khách thường
(N'Lê Thị Cẩm','0901000003',N'HCM',120,'cam@gmail.com',N'Thường'),
(N'Phạm Văn Dũng','0901000004',N'HCM',200,'dung@gmail.com',N'Thường'),
(N'Hoàng Ngọc Em','0901000005',N'HCM',150,'em@gmail.com',N'Thường'),

-- Khách thân thiết
(N'Võ Thanh Hà','0901000006',N'HCM',500,'ha@gmail.com',N'Thân thiết'),
(N'Đặng Gia Huy','0901000007',N'HCM',650,'huy@gmail.com',N'Thân thiết'),

-- Khách VIP
(N'Nguyễn Thị Kim Loan','0901000008',N'HCM',1200,'loan@gmail.com',N'VIP'),
(N'Trần Hoàng Long','0901000009',N'HCM',1500,'long@gmail.com',N'VIP'),

-- Khách không email
(N'Lý Văn Nam','0901000010',N'HCM',50,NULL,N'Thường'),
(N'Bùi Thị Oanh','0901000011',N'HCM',80,NULL,N'Thường');


-- PhieuDatBan
INSERT INTO PhieuDatBan (maKH, ngayDat, ngayDen, trangThai) VALUES
-- Đã hoàn thành
(1, '2025-04-20', '2025-04-22', N'Đã đến'),
(2, '2025-04-21', '2025-04-23', N'Đã đến'),

-- Đang chờ
(3, '2025-04-22', '2025-04-25', N'Chờ'),
(4, '2025-04-23', '2025-04-26', N'Chờ'),

-- Đã hủy
(5, '2025-04-20', '2025-04-24', N'Đã hủy'),

-- Sắp tới
(6, '2025-04-24', '2025-04-27', N'Đã đặt'),
(7, '2025-04-24', '2025-04-28', N'Đã đặt'),

-- VIP đặt trước
(8, '2025-04-24', '2025-04-30', N'Đã đặt'),
(9, '2025-04-24', '2025-05-01', N'Đã đặt');
-- ChiTietPhieuDatBan
INSERT INTO ChiTietPhieuDatBan (maPDB, maBan, ghiChu) VALUES
(1, 3, N'2 người, gần cửa sổ'),
(2, 5, N'Nhóm 5 người'),
(3, 8, N'Yêu cầu yên tĩnh'),
(4, 10, N'Sinh nhật'),
(5, 2, N'Khách hủy'),
(6, 15, N'Phòng VIP'),
(7, 6, N'2 người'),
(8, 16, N'Khách VIP'),
(9, 14, N'Ngoài trời');
-- Thue
INSERT INTO Thue (tenThue, thueSuat) VALUES
(N'VAT 10%', 0.10),
(N'VAT 8%', 0.08),
(N'Không thuế', 0.00),
(N'Giảm thuế mùa lễ', 0.05),
(N'Thuế đặc biệt', 0.15);

-- SanPham
INSERT INTO SanPham 
(maThue, tenSP, giaBan, donViTinh, moTa, soLuongTon, hanSuDung, ngayNhap) VALUES

-- NƯỚC SUỐI
(3, N'Aquafina 500ml', 10000, N'Chai', N'Nước suối đóng chai', 200, '2025-12-31', '2025-04-01'),
(3, N'Lavie 500ml', 10000, N'Chai', N'Nước khoáng', 180, '2025-12-31', '2025-04-01'),

-- NƯỚC NGỌT
(1, N'Coca Cola 330ml', 15000, N'Lon', N'Nước ngọt có gas', 150, '2025-11-30', '2025-04-01'),
(1, N'Pepsi 330ml', 15000, N'Lon', N'Nước ngọt có gas', 140, '2025-11-30', '2025-04-01'),
(1, N'7Up 330ml', 15000, N'Lon', N'Nước chanh có gas', 120, '2025-11-30', '2025-04-01'),

-- TRÀ ĐÓNG CHAI
(2, N'Trà xanh 0 độ', 12000, N'Chai', N'Trà xanh đóng chai', 160, '2025-10-30', '2025-04-01'),
(2, N'Trà đào C2', 12000, N'Chai', N'Trà trái cây', 170, '2025-10-30', '2025-04-01'),

-- NƯỚC TĂNG LỰC
(1, N'Redbull', 20000, N'Lon', N'Nước tăng lực', 90, '2025-09-30', '2025-04-01'),
(1, N'Sting dâu', 18000, N'Chai', N'Nước tăng lực', 100, '2025-09-30', '2025-04-01'),

-- NƯỚC TRÁI CÂY
(2, N'Twister cam', 15000, N'Chai', N'Nước ép cam', 110, '2025-10-15', '2025-04-01'),
(2, N'Twister dứa', 15000, N'Chai', N'Nước ép dứa', 100, '2025-10-15', '2025-04-01'),

-- CÀ PHÊ VIỆT NAM
(1, N'Cà phê đen', 20000, N'Ly', N'Cà phê truyền thống', 999, NULL, '2025-04-01'),
(1, N'Cà phê sữa', 25000, N'Ly', N'Cà phê sữa đá', 999, NULL, '2025-04-01'),
(1, N'Bạc xỉu', 28000, N'Ly', N'Nhiều sữa ít cà phê', 999, NULL, '2025-04-01'),

-- CÀ PHÊ HIỆN ĐẠI
(1, N'Americano', 30000, N'Ly', N'Cà phê đen kiểu Mỹ', 999, NULL, '2025-04-01'),
(1, N'Latte', 35000, N'Ly', N'Cà phê sữa Ý', 999, NULL, '2025-04-01'),
(1, N'Cappuccino', 35000, N'Ly', N'Cà phê bọt sữa', 999, NULL, '2025-04-01'),

-- CÀ PHÊ ĐẶC BIỆT
(1, N'Mocha', 38000, N'Ly', N'Cà phê socola', 999, NULL, '2025-04-01'),
(1, N'Cold Brew', 40000, N'Ly', N'Ủ lạnh', 999, NULL, '2025-04-01');

INSERT INTO KhuyenMai (tenKM, mucGiamGia, ngayBatDau, ngayKetThuc, dieuKienToiThieu) VALUES

-- Giảm theo %
(N'Giảm 10% hóa đơn', 10, '2025-04-01', '2025-04-30', 50000),
(N'Giảm 20% cuối tuần', 20, '2025-04-05', '2025-04-06', 100000),

-- Khuyến mãi nhỏ
(N'Giảm 5%', 5, '2025-04-01', '2025-05-01', 30000),

-- Khách VIP
(N'VIP giảm 25%', 25, '2025-04-01', '2025-12-31', 200000),

-- Giờ vàng
(N'Giờ vàng 15%', 15, '2025-04-01', '2025-04-30', 80000),

-- Lễ
(N'30/4 - 1/5 giảm 30%', 30, '2025-04-30', '2025-05-01', 100000);

-- HoaDon
INSERT INTO HoaDon (maNV, maBan, ngayLap, tongTien, trangThai, ghiChu, phuongThucThanhToan) VALUES

-- Đã thanh toán
(1, 3, '2025-04-24', 65000, N'Đã thanh toán', N'', N'Tiền mặt'),
(2, 7, '2025-04-24', 120000, N'Đã thanh toán', N'Khách VIP', N'Chuyển khoản'),

-- Đang phục vụ
(2, 5, '2025-04-24', 0, N'Đang phục vụ', N'', NULL),

-- Chờ thanh toán
(1, 8, '2025-04-24', 90000, N'Chờ thanh toán', N'', N'Tiền mặt'),

-- Đã hủy
(1, 2, '2025-04-23', 0, N'Đã hủy', N'Khách không đến', NULL);
-- HoaDonChiTiet
INSERT INTO HoaDonChiTiet (maHD, maSP, maKM, soLuong, donGia, thanhTien, ghiChu) VALUES

-- Hóa đơn 1
(1, 1, NULL, 2, 30000, 60000, N''),
(1, 3, NULL, 1, 15000, 15000, N''),

-- Hóa đơn 2 (có khuyến mãi)
(2, 4, 1, 2, 15000, 24000, N'Giảm 20%'),
(2, 10, NULL, 3, 15000, 45000, N''),

-- Hóa đơn 4
(4, 2, NULL, 2, 10000, 20000, N''),
(4, 12, NULL, 1, 40000, 40000, N'');