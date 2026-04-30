package entity;

public class TaiKhoan {
    private String userName;
    private String maNV;
    private String password;
    private String vaiTro;
    private String trangThai;

    public TaiKhoan(String userName, String maNV, String password,
                    String vaiTro, String trangThai) {
        this.userName = userName;
        this.maNV     = maNV;
        this.password = password;
        this.vaiTro   = vaiTro;
        this.trangThai = trangThai;
    }

    public TaiKhoan(String userName) {
        this(userName, "", "", "", "HoatDong");
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMaNV() {
		return maNV;
	}

	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVaiTro() {
		return vaiTro;
	}

	public void setVaiTro(String vaiTro) {
		this.vaiTro = vaiTro;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

   
}