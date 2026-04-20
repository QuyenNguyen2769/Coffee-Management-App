package sp;

import java.io.Serializable;
import java.util.Objects;

public class SanPham implements Serializable{
	private int maSP;
	private String tenSp;
	private int soLuong;
	private double donGia;
	private String loai;
	public SanPham(int maSP, String tenSp, int soLuong, double donGia,String loai) {
		this.maSP = maSP;
		this.tenSp = tenSp;
		this.soLuong = soLuong;
		this.donGia = donGia;
		this.loai = loai;
	}
	public SanPham(int maSP) {
		this(maSP,"",0,0.0,"");
	}
	public int getMaSP() {
		return maSP;
	}
	public void setMaSP(int maSP) {
		this.maSP = maSP;
	}
	public String getTenSp() {
		return tenSp;
	}
	public void setTenSp(String tenSp) {
		this.tenSp = tenSp;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	public double getDonGia() {
		return donGia;
	}
	public void setDonGia(double donGia) {
		this.donGia = donGia;
	}
	
	public String getLoai() {
		return loai;
	}
	public void setLoai(String loai) {
		this.loai = loai;
	}
	@Override
	public int hashCode() {
		return Objects.hash(maSP);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SanPham other = (SanPham) obj;
		return maSP == other.maSP;
	}
}
