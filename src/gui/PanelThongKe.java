package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Panel Thống Kê - Đã tạm thời gỡ bỏ mã nguồn để thiết kế lại.
 */
public class PanelThongKe extends JPanel {

    public enum LoaiThongKe { NGAY, THANG, NAM, KHACH_HANG }

    public PanelThongKe(LoaiThongKe loai) {
        setLayout(new BorderLayout());
        setBackground(new Color(252, 248, 243));
        
        JLabel lbl = new JLabel("Phần Thống Kê đang được thiết kế lại...", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        add(lbl, BorderLayout.CENTER);
    }

    public static PanelThongKe theoNgay()      { return new PanelThongKe(LoaiThongKe.NGAY); }
    public static PanelThongKe theoThang()     { return new PanelThongKe(LoaiThongKe.THANG); }
    public static PanelThongKe theoNam()       { return new PanelThongKe(LoaiThongKe.NAM); }
    public static PanelThongKe khachHang()     { return new PanelThongKe(LoaiThongKe.KHACH_HANG); }
}