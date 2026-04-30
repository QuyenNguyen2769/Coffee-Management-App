package entity;

public class Ban {
    private String maBan;
    private int    soBan;
    private String trangThai;
    private int    sucChua;
    private String khuVuc;

    public Ban(String maBan, int soBan, String trangThai,
               int sucChua, String khuVuc) {
        this.maBan     = maBan;
        this.soBan     = soBan;
        this.trangThai = trangThai;
        this.sucChua   = sucChua;
        this.khuVuc    = khuVuc;
    }

    public Ban(String maBan) {
        this(maBan, 0, "", 0, "");
    }

	public String getMaBan() {
		return maBan;
	}

	public void setMaBan(String maBan) {
		this.maBan = maBan;
	}

	public int getSoBan() {
		return soBan;
	}

	public void setSoBan(int soBan) {
		this.soBan = soBan;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public int getSucChua() {
		return sucChua;
	}

	public void setSucChua(int sucChua) {
		this.sucChua = sucChua;
	}

	public String getKhuVuc() {
		return khuVuc;
	}

	public void setKhuVuc(String khuVuc) {
		this.khuVuc = khuVuc;
	}

   
}