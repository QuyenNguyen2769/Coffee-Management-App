package gui;

import java.awt.Color;

/**
 * NhanVienHelper — màu chữ chức vụ dùng chung cho PanelNhanVien và PanelTimNhanVien
 */
public class NhanVienHelper {

    // ── Palette chức vụ ──────────────────────────────────────
    public static final Color C_QUAN_LY    = new Color(178,  34,  34); // Đỏ đậm   — Quản lý
    public static final Color C_THU_NGAN   = new Color(70,  130, 180); // Xanh biển — Thu ngân
    public static final Color C_PHA_CHE    = new Color(34,  139,  34); // Xanh lá   — Pha chế
    public static final Color C_PHUC_VU    = new Color(210, 105,  30); // Cam nâu   — Phục vụ
    public static final Color C_PART_TIME  = new Color(128,   0, 128); // Tím       — Part-time
    public static final Color C_FULL_TIME     = new Color(0,   128, 128); // Teal      — Bảo vệ
    public static final Color C_DEFAULT    = new Color(80,   80,  80); // Xám       — Khác

    /**
     * Trả về màu chữ theo chức vụ.
     * So sánh không phân biệt hoa/thường, bỏ khoảng trắng.
     */
    public static Color mauChucVu(String chucVu) {
        if (chucVu == null) return C_DEFAULT;
        String cv = chucVu.trim().toLowerCase();
        if (cv.contains("quản lý") || cv.contains("quan ly") || cv.equals("admin"))
            return C_QUAN_LY;
        if (cv.contains("thu ngân") || cv.contains("thu ngan"))
            return C_THU_NGAN;
        if (cv.contains("pha chế") || cv.contains("pha che") || cv.contains("barista"))
            return C_PHA_CHE;
        if (cv.contains("phục vụ") || cv.contains("phuc vu") || cv.contains("waiter"))
            return C_PHUC_VU;
        if (cv.contains("part-time") || cv.contains("thời vụ"))
            return C_PART_TIME;
        if (cv.contains("full-time") || cv.contains("full-time"))
            return C_FULL_TIME;
        return C_DEFAULT;
    }
}
