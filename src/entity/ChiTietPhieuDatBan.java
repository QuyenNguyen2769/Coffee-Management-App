package entity;

public class ChiTietPhieuDatBan {
    private String maPDB;
    private String maBan;
    private String ghiChu;

    public ChiTietPhieuDatBan(String maPDB, String maBan, String ghiChu) {
        this.maPDB  = maPDB;
        this.maBan  = maBan;
        this.ghiChu = ghiChu;
    }

    public ChiTietPhieuDatBan(String maPDB, String maBan) {
        this(maPDB, maBan, "");
    }

	public String getMaPDB() {
		return maPDB;
	}

	public void setMaPDB(String maPDB) {
		this.maPDB = maPDB;
	}

	public String getMaBan() {
		return maBan;
	}

	public void setMaBan(String maBan) {
		this.maBan = maBan;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}


}