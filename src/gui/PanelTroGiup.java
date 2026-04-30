package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelTroGiup extends JPanel {
    public PanelTroGiup() {
        setLayout(new BorderLayout());
        setBackground(new Color(252, 248, 243));
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("HƯỚNG DẪN SỬ DỤNG");
        try {
            ImageIcon icon = new ImageIcon("images/icon_trogiup.png");
            Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            title.setIcon(new ImageIcon(img));
            title.setIconTextGap(15);
        } catch (Exception e) {}

        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(101, 67, 33));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        content.add(title);
        content.add(Box.createVerticalStrut(30));
        
        String[] guides = {
            "1. Quản Lý Phòng Bàn: Xem sơ đồ bàn, đặt bàn và thanh toán.",
            "2. Danh Mục: Quản lý Menu món ăn và chương trình Khuyến mãi.",
            "3. Hệ Thống: Cài đặt tài khoản và đăng xuất.",
            "4. Thống Kê: Xem báo cáo doanh thu theo ngày/tháng/năm."
        };
        
        for (String g : guides) {
            JLabel lbl = new JLabel(g);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lbl.setBorder(new EmptyBorder(0, 0, 15, 0));
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(lbl);
        }
        
        add(content, BorderLayout.CENTER);
    }
}