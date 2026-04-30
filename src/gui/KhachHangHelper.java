package gui;

import java.awt.Color;

/**
 * KhachHangHelper — hàm dùng chung giữa PanelKhachHang và PanelTimKhachHang
 */
public class KhachHangHelper {

    // Màu cho từng loại khách
    public static final Color C_MOI   = new Color(100, 149, 237); // Khách mới  — xanh biển
    public static final Color C_THUONG= new Color(34,  139,  34); // Thường     — xanh lá
    public static final Color C_THAN  = new Color(210, 105,  30); // Thân thiết — cam
    public static final Color C_VIP   = new Color(178,  34,  34); // VIP        — đỏ đậm

    /** Tính loại khách từ điểm tích lũy */
    public static String xepLoai(int diem) {
        if (diem >= 1000) return "VIP";
        if (diem >= 500)  return "Thân thiết";
        if (diem >= 100)  return "Thường";
        return "Khách mới";
    }

    /** Màu chữ tương ứng với loại khách */
    public static Color mauLoai(String loai) {
        if (loai == null) return Color.BLACK;
        switch (loai.trim()) {
            case "VIP":         return C_VIP;
            case "Thân thiết":  return C_THAN;
            case "Thường":      return C_THUONG;
            default:            return C_MOI;
        }
    }
}
