package entity;

public class HoaDonChiTiet {
    private String maHD;
    private String maSP;
    private String maKM;
    private int    soLuong;
    private Double donGia;
    private Double thanhTien;
    private String ghiChu;

    public HoaDonChiTiet(String maHD, String maSP, String maKM,
                         int soLuong, Double donGia, Double thanhTien,
                         String ghiChu) {
        this.maHD      = maHD;
        this.maSP      = maSP;
        this.maKM      = maKM;
        this.soLuong   = soLuong;
        this.donGia    = donGia;
        this.thanhTien = thanhTien;
        this.ghiChu    = ghiChu;
    }

    public HoaDonChiTiet(String maHD, String maSP) {
        this(maHD, maSP, "", 0, 0.0, 0.0, "");
    }

	public String getMaHD() {
		return maHD;
	}

	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}

	public String getMaSP() {
		return maSP;
	}

	public void setMaSP(String maSP) {
		this.maSP = maSP;
	}

	public String getMaKM() {
		return maKM;
	}

	public void setMaKM(String maKM) {
		this.maKM = maKM;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public Double getDonGia() {
		return donGia;
	}

	public void setDonGia(Double donGia) {
		this.donGia = donGia;
	}

	public Double getThanhTien() {
		return thanhTien;
	}

	public void setThanhTien(Double thanhTien) {
		this.thanhTien = thanhTien;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

 
}