package entity;

import java.sql.Date;

public class HoaDon {
    private String maHD;
    private String maNV;
    private String maBan;
    private Date   ngayLap;
    private Double tongTien;
    private String trangThai;
    private String ghiChu;
    private String phuongThucThanhToan;

    public HoaDon(String maHD, String maNV, String maBan, Date ngayLap,
                  Double tongTien, String trangThai, String ghiChu,
                  String phuongThucThanhToan) {
        this.maHD                = maHD;
        this.maNV                = maNV;
        this.maBan               = maBan;
        this.ngayLap             = ngayLap;
        this.tongTien            = tongTien;
        this.trangThai           = trangThai;
        this.ghiChu              = ghiChu;
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public HoaDon(String maHD) {
        this(maHD, "", "", null, 0.0, "", "", "");
    }

	public String getMaHD() {
		return maHD;
	}

	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}

	public String getMaNV() {
		return maNV;
	}

	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}

	public String getMaBan() {
		return maBan;
	}

	public void setMaBan(String maBan) {
		this.maBan = maBan;
	}

	public Date getNgayLap() {
		return ngayLap;
	}

	public void setNgayLap(Date ngayLap) {
		this.ngayLap = ngayLap;
	}

	public Double getTongTien() {
		return tongTien;
	}

	public void setTongTien(Double tongTien) {
		this.tongTien = tongTien;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

	public String getPhuongThucThanhToan() {
		return phuongThucThanhToan;
	}

	public void setPhuongThucThanhToan(String phuongThucThanhToan) {
		this.phuongThucThanhToan = phuongThucThanhToan;
	}

   
}