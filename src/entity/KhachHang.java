package entity;

public class KhachHang {
    private int maKH;
    private String hoTen;
    private String sdt;
    private String diaChi;
    private int diemTichLuy;
    private String email;
    private String loaiKhach;

    public KhachHang() {
    }

    public KhachHang(int maKH, String hoTen, String sdt, String diaChi, int diemTichLuy, String email, String loaiKhach) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.diemTichLuy = diemTichLuy;
        this.email = email;
        this.loaiKhach = loaiKhach;
    }

    public int getMaKH() { return maKH; }
    public void setMaKH(int maKH) { this.maKH = maKH; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public int getDiemTichLuy() { return diemTichLuy; }
    public void setDiemTichLuy(int diemTichLuy) { this.diemTichLuy = diemTichLuy; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLoaiKhach() { return loaiKhach; }
    public void setLoaiKhach(String loaiKhach) { this.loaiKhach = loaiKhach; }
}
