package entity;

public class TaiKhoan {

    private String username;
    private String password;
    private String maNV;
    private String role;

    public TaiKhoan() {
    }

    public TaiKhoan(String username, String password, String maNV, String role) {
        this.username = username;
        this.password = password;
        this.maNV = maNV;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}