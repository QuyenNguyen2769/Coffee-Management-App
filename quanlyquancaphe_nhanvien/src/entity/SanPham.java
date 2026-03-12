package entity;

public class SanPham {

    private String maSP;
    private String tenSP;
    private double gia;
    private String maLoai;

    public SanPham() {
    }

    public SanPham(String maSP, String tenSP, double gia, String maLoai) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.gia = gia;
        this.maLoai = maLoai;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }
}