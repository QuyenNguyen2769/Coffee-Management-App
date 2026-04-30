package entity;

public class KhachHang {
    private String maKH;
    private String hoTen;
    private String sdt;
    private String diaChi;
    private int    diemTichLuy;
    private String email;
    private String loaiKhach; // "Khách mới" / "Thường" / "Thân thiết" / "VIP"

    public KhachHang(String maKH, String hoTen, String sdt,
                     String diaChi, int diemTichLuy, String email) {
        this(maKH, hoTen, sdt, diaChi, diemTichLuy, email, "Khách mới");
    }

    public KhachHang(String maKH, String hoTen, String sdt,
                     String diaChi, int diemTichLuy, String email, String loaiKhach) {
        this.maKH        = maKH;
        this.hoTen       = hoTen;
        this.sdt         = sdt;
        this.diaChi      = diaChi;
        this.diemTichLuy = diemTichLuy;
        this.email       = email;
        this.loaiKhach   = loaiKhach;
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

	public String getLoaiKhach() {
		return loaiKhach != null ? loaiKhach : "Khách mới";
	}

	public void setLoaiKhach(String loaiKhach) {
		this.loaiKhach = loaiKhach;
	}

	/** Kiểm tra nhanh xem có phải khách VIP không */
	public boolean isVIP() {
		return "VIP".equalsIgnoreCase(loaiKhach);
	}

	/** Kiểm tra xem có phải thành viên (không phải khách lẻ) không */
	public boolean isThanhVien() {
		return loaiKhach != null && !loaiKhach.equalsIgnoreCase("Khách mới");
	}

}