package entity;

import java.sql.Date;

public class SanPham {
    private String maSP;
    private String maThue;
    private String tenSP;
    private Double giaBan;
    private String donViTinh;
    private String moTa;
    private int    soLuongTon;
    private String hanSuDung;
    private Date   ngayNhap;

    public SanPham(String maSP, String maThue, String tenSP, Double giaBan,
                   String donViTinh, String moTa, int soLuongTon,
                   String hanSuDung, Date ngayNhap) {
        this.maSP       = maSP;
        this.maThue     = maThue;
        this.tenSP      = tenSP;
        this.giaBan     = giaBan;
        this.donViTinh  = donViTinh;
        this.moTa       = moTa;
        this.soLuongTon = soLuongTon;
        this.hanSuDung  = hanSuDung;
        this.ngayNhap   = ngayNhap;
    }

    public SanPham(String maSP) {
        this(maSP, "", "", 0.0, "", "", 0, "", null);
    }

	public String getMaSP() {
		return maSP;
	}

	public void setMaSP(String maSP) {
		this.maSP = maSP;
	}

	public String getMaThue() {
		return maThue;
	}

	public void setMaThue(String maThue) {
		this.maThue = maThue;
	}

	public String getTenSP() {
		return tenSP;
	}

	public void setTenSP(String tenSP) {
		this.tenSP = tenSP;
	}

	public Double getGiaBan() {
		return giaBan;
	}

	public void setGiaBan(Double giaBan) {
		this.giaBan = giaBan;
	}

	public String getDonViTinh() {
		return donViTinh;
	}

	public void setDonViTinh(String donViTinh) {
		this.donViTinh = donViTinh;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public int getSoLuongTon() {
		return soLuongTon;
	}

	public void setSoLuongTon(int soLuongTon) {
		this.soLuongTon = soLuongTon;
	}

	public String getHanSuDung() {
		return hanSuDung;
	}

	public void setHanSuDung(String hanSuDung) {
		this.hanSuDung = hanSuDung;
	}

	public Date getNgayNhap() {
		return ngayNhap;
	}

	public void setNgayNhap(Date ngayNhap) {
		this.ngayNhap = ngayNhap;
	}

   
}