package entity;

import java.sql.Date;

public class PhieuDatBan {
    private String maPDB;
    private String maKH;
    private Date   ngayDat;
    private Date   ngayDen;
    private String trangThai;

    public PhieuDatBan(String maPDB, String maKH, Date ngayDat,
                       Date ngayDen, String trangThai) {
        this.maPDB     = maPDB;
        this.maKH      = maKH;
        this.ngayDat   = ngayDat;
        this.ngayDen   = ngayDen;
        this.trangThai = trangThai;
    }

    public PhieuDatBan(String maPDB) {
        this(maPDB, "", null, null, "");
    }

    public String getMaPDB() { return maPDB; }

	public String getMaKH() {
		return maKH;
	}

	public void setMaKH(String maKH) {
		this.maKH = maKH;
	}

	public Date getNgayDat() {
		return ngayDat;
	}

	public void setNgayDat(Date ngayDat) {
		this.ngayDat = ngayDat;
	}

	public Date getNgayDen() {
		return ngayDen;
	}

	public void setNgayDen(Date ngayDen) {
		this.ngayDen = ngayDen;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public void setMaPDB(String maPDB) {
		this.maPDB = maPDB;
	}
  
}