package entity;

import java.sql.Date;

public class KhuyenMai {
    private String maKM;
    private String tenKM;
    private Double mucGiamGia;
    private Date   ngayBatDau;
    private Date   ngayKetThuc;
    private Double dieuKienToiThieu;

    public KhuyenMai(String maKM, String tenKM, Double mucGiamGia,
                     Date ngayBatDau, Date ngayKetThuc,
                     Double dieuKienToiThieu) {
        this.maKM              = maKM;
        this.tenKM             = tenKM;
        this.mucGiamGia        = mucGiamGia;
        this.ngayBatDau        = ngayBatDau;
        this.ngayKetThuc       = ngayKetThuc;
        this.dieuKienToiThieu  = dieuKienToiThieu;
    }

    public KhuyenMai(String maKM) {
        this(maKM, "", 0.0, null, null, 0.0);
    }

	public String getMaKM() {
		return maKM;
	}

	public void setMaKM(String maKM) {
		this.maKM = maKM;
	}

	public String getTenKM() {
		return tenKM;
	}

	public void setTenKM(String tenKM) {
		this.tenKM = tenKM;
	}

	public Double getMucGiamGia() {
		return mucGiamGia;
	}

	public void setMucGiamGia(Double mucGiamGia) {
		this.mucGiamGia = mucGiamGia;
	}

	public Date getNgayBatDau() {
		return ngayBatDau;
	}

	public void setNgayBatDau(Date ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}

	public Date getNgayKetThuc() {
		return ngayKetThuc;
	}

	public void setNgayKetThuc(Date ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}

	public Double getDieuKienToiThieu() {
		return dieuKienToiThieu;
	}

	public void setDieuKienToiThieu(Double dieuKienToiThieu) {
		this.dieuKienToiThieu = dieuKienToiThieu;
	}

  
}