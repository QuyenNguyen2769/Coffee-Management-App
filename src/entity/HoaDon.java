package entity;

import java.util.Date;

public class HoaDon {

    private String maHD;
    private String maNV;
    private String maBan;
    private Date ngayLap;
    private double tongTien;

    public HoaDon() {
    }

    public HoaDon(String maHD, String maNV, String maBan, Date ngayLap, double tongTien) {
        this.maHD = maHD;
        this.maNV = maNV;
        this.maBan = maBan;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}