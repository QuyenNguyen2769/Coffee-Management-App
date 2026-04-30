package entity;

public class Ban {
    private int soBan;
    private String trangThai;
    private int sucChua;
    private String khuVuc;

    public Ban() {
    }

    public Ban(int soBan, String trangThai, int sucChua, String khuVuc) {
        this.soBan = soBan;
        this.trangThai = trangThai;
        this.sucChua = sucChua;
        this.khuVuc = khuVuc;
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