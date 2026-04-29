package dao;

import java.io.*;
import java.util.*;

public class DataManager  {

    private static final String FILE_DICHVU    = "data/dichvu.dat";
    private static final String FILE_KHUYENMAI = "data/khuyenmai.dat";

    // ── Dữ liệu Dịch Vụ ──────────────────────────────
    private static List<Object[]> dsDichVu = new ArrayList<>();

    public static List<Object[]> getDsDichVu() { return dsDichVu; }

    public static void luuDichVu() {
        try {
            new File("data").mkdirs();
            ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE_DICHVU));
            oos.writeObject(dsDichVu);
            oos.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    public static void docDichVu() {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FILE_DICHVU));
            dsDichVu = (List<Object[]>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            // File chưa có, dùng dữ liệu mẫu
            dsDichVu = new ArrayList<>();
            Collections.addAll(dsDichVu, new Object[][]{
                {1, "Cà Phê Đen",  "Cà Phê",       "25,000đ", "ly",    "Đang Phục Vụ"},
                {2, "Cà Phê Sữa",  "Cà Phê",       "30,000đ", "ly",    "Đang Phục Vụ"},
                {3, "Trà Đào",     "Trà",           "35,000đ", "ly",    "Đang Phục Vụ"},
                {4, "Trà Sữa",     "Trà",           "40,000đ", "ly",    "Đang Phục Vụ"},
                {5, "Flan",        "Bánh & Ăn Vặt", "20,000đ", "phần", "Đang Phục Vụ"},
                {6, "Trà Tắc",     "Trà",           "25,000đ", "ly",    "Tạm Ngưng"},
            });
        }
    }

    // ── Dữ liệu Khuyến Mãi ───────────────────────────
    private static List<Object[]> dsKhuyenMai = new ArrayList<>();

    public static List<Object[]> getDsKhuyenMai() { return dsKhuyenMai; }

    public static void luuKhuyenMai() {
        try {
            new File("data").mkdirs();
            ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE_KHUYENMAI));
            oos.writeObject(dsKhuyenMai);
            oos.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    public static void docKhuyenMai() {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FILE_KHUYENMAI));
            dsKhuyenMai = (List<Object[]>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            dsKhuyenMai = new ArrayList<>();
            Collections.addAll(dsKhuyenMai, new Object[][]{
                {"CAFE10",    "Giảm 10% hóa đơn đầu tiên", "10%",      "Phần Trăm", "30/06/2026", "Đang Hoạt Động"},
                {"SUMMER50K", "Ưu đãi hè - Giảm 50.000đ",  "50,000đ",  "Số Tiền",   "31/08/2026", "Đang Hoạt Động"},
                {"WELCOME20", "Chào mừng khách hàng mới",   "20%",      "Phần Trăm", "01/04/2026", "Hết Hạn"},
            });
        }
    }

    // ── Load tất cả khi khởi động ─────────────────────
    public static void loadAll() {
        docDichVu();
        docKhuyenMai();
    }
}