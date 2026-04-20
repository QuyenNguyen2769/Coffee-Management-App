package entity;

public class KhachHang {
    private String maKH;
    private String hoTen;
    private String sdt;
    private String diaChi;
    private int    diemTichLuy;
    private String email;

    public KhachHang(String maKH, String hoTen, String sdt,
                     String diaChi, int diemTichLuy, String email) {
        this.maKH       = maKH;
        this.hoTen      = hoTen;
        this.sdt        = sdt;
        this.diaChi     = diaChi;
        this.diemTichLuy = diemTichLuy;
        this.email      = email;
    }

    public KhachHang(String maKH) {
        this(maKH, "", "", "", 0, "");
    }

	public String getMaKH() {
		return maKH;
	}

	public void setMaKH(String maKH) {
		this.maKH = maKH;
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

	public int getDiemTichLuy() {
		return diemTichLuy;
	}

	public void setDiemTichLuy(int diemTichLuy) {
		this.diemTichLuy = diemTichLuy;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

  
}