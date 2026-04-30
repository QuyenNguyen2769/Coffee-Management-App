package dao;

import java.io.*;
import java.util.*;
import entity.SanPham;

public class DataManager {

    private static final String FILE_KHUYENMAI = "data/khuyenmai.dat";
    private static final String FILE_HOADON    = "data/hoadon.dat";

    // ── Dữ liệu Dịch Vụ (Lấy từ SQL) ──────────────────
    private static List<Object[]> dsDichVu = new ArrayList<>();
    private static SanPhamDAO spDAO = new SanPhamDAO();

    public static List<Object[]> getDsDichVu() { 
        return dsDichVu; 
    }

    public static void docDichVuTuSQL() {
        dsDichVu.clear();
        ArrayList<SanPham> list = spDAO.getAllSanPham();
        for (SanPham sp : list) {
            dsDichVu.add(new Object[]{
                sp.getMaSP(),
                sp.getTenSP(),
                sp.getMoTa(),
                String.format("%,.0fđ", sp.getGiaBan()),
                sp.getDonViTinh(),
                sp.getSoLuongTon() > 0 ? "Đang Phục Vụ" : "Hết Hàng"
            });
        }
    }

    public static void luuDichVu() {
        // Hiện tại dùng SQL nên hàm này tạm thời không cần xử lý file .dat
        // Trong tương lai có thể viết code UPDATE vào SQL tại đây
    }

    public static void docDichVu() {
        docDichVuTuSQL();
    }

    // ── Dữ liệu Khuyến Mãi ───────────────────────────
    private static List<Object[]> dsKhuyenMai = new ArrayList<>();
    public static List<Object[]> getDsKhuyenMai() { return dsKhuyenMai; }

    public static void luuKhuyenMai() {
        try {
            new File("data").mkdirs();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_KHUYENMAI));
            oos.writeObject(dsKhuyenMai);
            oos.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    public static void docKhuyenMai() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_KHUYENMAI));
            dsKhuyenMai = (List<Object[]>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            dsKhuyenMai = new ArrayList<>();
            Collections.addAll(dsKhuyenMai, new Object[][]{
                {"CAFE10",    "Giảm 10% hóa đơn đầu tiên", "10%",      "Phần Trăm", "30/06/2026", "Đang Hoạt Động"},
                {"SUMMER50K", "Ưu đãi hè - Giảm 50.000đ",  "50,000đ",  "Số Tiền",   "31/08/2026", "Đang Hoạt Động"},
                {"30/4-OFF",  "Lễ 30/4 Giảm 30%",         "30%",      "Phần Trăm", "01/05/2026", "Đang Hoạt Động"},
            });
        }
    }

    // ── Dữ liệu Hóa Đơn ──────────────────────────────
    private static List<Object[]> dsHoaDon = new ArrayList<>();
    public static List<Object[]> getDsHoaDon() { return dsHoaDon; }

    public static void luuHoaDon() {
        try {
            new File("data").mkdirs();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_HOADON));
            oos.writeObject(dsHoaDon);
            oos.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    public static void docHoaDon() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_HOADON));
            dsHoaDon = (List<Object[]>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            dsHoaDon = new ArrayList<>();
            Collections.addAll(dsHoaDon, new Object[][]{
                {"HD001", "30/04/2026 08:30", "admin", "Khách Vãng Lai", "85,000đ", "Đã Thanh Toán"},
                {"HD002", "30/04/2026 09:15", "admin", "Nguyễn Văn A", "120,000đ", "Đã Thanh Toán"},
            });
        }
    }

    // ── Load tất cả khi khởi động ─────────────────────
    public static void loadAll() {
        docDichVuTuSQL(); // Luôn lấy mới từ SQL
        docKhuyenMai();
        docHoaDon();
    }
}