package entity;

import java.sql.Date;

public class NhanVien {
	private String maNV;
	private String hoTen;
	private String sdt;
	private String diaChi;
	private String gioiTinh;
	private String ngaySinh;
	private String email;
	private Date ngayVaoLam;
	private Double  luong;
	
	
	
	
	public NhanVien(String maNV, String hoTen, String sdt, String diaChi, String gioiTinh, String ngaySinh,
			String email, Date ngayVaoLam, Double luong) {
		this.maNV = maNV;
		this.hoTen = hoTen;
		this.sdt = sdt;
		this.diaChi = diaChi;
		this.gioiTinh = gioiTinh;
		this.ngaySinh = ngaySinh;
		this.email = email;
		this.ngayVaoLam = ngayVaoLam;
		this.luong = luong;
	}
	
	public NhanVien(String maNV) {
		this(maNV,"","","","","","",null,0.0);
	}

	public String getMaNV() {
		return maNV;
	}
	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}
	public String getHoTen() {
		return hoTen;
	}
	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}
	public String getSdt() {
		return sdt;
	}
	public void setSdt(String sdt) {
		this.sdt = sdt;
	}
	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	public String getGioiTinh() {
		return gioiTinh;
	}
	public void setGioiTinh(String gioiTinh) {
		this.gioiTinh = gioiTinh;
	}
	public String getNgaySinh() {
		return ngaySinh;
	}
	public void setNgaySinh(String ngaySinh) {
		this.ngaySinh = ngaySinh;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getNgayVaoLam() {
		return ngayVaoLam;
	}
	public void setNgayVaoLam(Date ngayVaoLam) {
		this.ngayVaoLam = ngayVaoLam;
	}
	public Double getLuong() {
		return luong;
	}
	public void setLuong(Double luong) {
		this.luong = luong;
	}
	
	
	
	
}
