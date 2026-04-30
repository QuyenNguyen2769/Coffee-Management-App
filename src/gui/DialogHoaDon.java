package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.text.DecimalFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DialogHoaDon extends JDialog {
    private static final Color CLR_WHITE = Color.WHITE;
    private static final Color CLR_BROWN = new Color(101, 67, 33);
    private static final Color CLR_TEXT  = new Color(28, 20, 10);
    private DecimalFormat df = new DecimalFormat("#,###đ");
    
    public DialogHoaDon(Frame owner, String maHD, String nv, String kh, String ban, DefaultTableModel details, double total) {
        super(owner, "Hóa Đơn Chi Tiết", true);
        setSize(550, 780); 
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setBackground(CLR_WHITE);
        pnlMain.setBorder(new EmptyBorder(25, 25, 25, 25));

        // LOGO IMAGE
        try {
            ImageIcon icon = new ImageIcon("images/coffee_logo.jpg");
            if (new File("images/logo.png").exists()) icon = new ImageIcon("images/logo.png");
            
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(img));
            lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
            pnlMain.add(lblLogo);
        } catch (Exception e) {
            // Nếu không tìm thấy logo thì bỏ qua
        }

        pnlMain.add(Box.createVerticalStrut(10));
        
        // HEADER
        JLabel lblStore = new JLabel("CAFE QMT");
        lblStore.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblStore.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblStore.setForeground(CLR_BROWN);
        pnlMain.add(lblStore);

        JLabel lblAddr = new JLabel("Số 2 Nguyễn Văn Bảo, Gò Vấp, TP. HCM");
        lblAddr.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblAddr.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlMain.add(lblAddr);

        JLabel lblTel = new JLabel("Hotline: 0901 000 001");
        lblTel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlMain.add(lblTel);

        pnlMain.add(Box.createVerticalStrut(20));
        JLabel lblTitle = new JLabel("HÓA ĐƠN THANH TOÁN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlMain.add(lblTitle);
        pnlMain.add(Box.createVerticalStrut(15));

        // INFO BLOCK - Tăng kích thước tối đa lên 500
        pnlMain.add(infoLine("Mã hóa đơn:", maHD));
        pnlMain.add(infoLine("Thời gian:", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())));
        pnlMain.add(infoLine("Nhân viên:", nv));
        pnlMain.add(infoLine("Khách hàng:", (kh == null || kh.isEmpty()) ? "Khách vãng lai" : kh));
        if(ban != null && !ban.isEmpty()) pnlMain.add(infoLine("Vị trí:", "Bàn " + ban));
        else pnlMain.add(infoLine("Vị trí:", "Mang về"));

        pnlMain.add(Box.createVerticalStrut(15));
        pnlMain.add(divider()); 
        pnlMain.add(Box.createVerticalStrut(10));

        // ITEMS LIST
        for (int i = 0; i < details.getRowCount(); i++) {
            String ten = details.getValueAt(i, 1).toString();
            String sl  = details.getValueAt(i, 3).toString();
            String tt  = details.getValueAt(i, 4).toString();
            pnlMain.add(itemLine(ten, sl, tt));
            pnlMain.add(Box.createVerticalStrut(8));
        }

        pnlMain.add(Box.createVerticalStrut(10));
        pnlMain.add(divider());
        pnlMain.add(Box.createVerticalStrut(15));

        // TOTAL BLOCK
        pnlMain.add(totalLine("TỔNG CỘNG:", df.format(total), 26));
        
        pnlMain.add(Box.createVerticalStrut(45));
        JLabel lblThank = new JLabel("Cảm ơn Quý khách! Hẹn gặp lại.");
        lblThank.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        lblThank.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlMain.add(lblThank);

        // NÚT BẤM
        JButton btnPrint = new JButton("XUẤT HÓA ĐƠN & IN");
        try {
            ImageIcon printIcon = new ImageIcon("images/icon_hoadon.png");
            Image imgPrint = printIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            btnPrint.setIcon(new ImageIcon(imgPrint));
        } catch (Exception e) {}

        btnPrint.setPreferredSize(new Dimension(0, 70));
        btnPrint.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnPrint.setBackground(new Color(101, 67, 33)); // Màu Nâu đậm
        btnPrint.setForeground(Color.WHITE);            // Chữ Trắng tinh
        btnPrint.setFocusPainted(false);
        btnPrint.setBorderPainted(false);
        btnPrint.setOpaque(true);
        
        btnPrint.addActionListener(e -> {
            exportToImage(pnlMain, maHD);
            dispose();
        });

        add(new JScrollPane(pnlMain), BorderLayout.CENTER);
        add(btnPrint, BorderLayout.SOUTH);
    }

    private JComponent divider() {
        JSeparator s = new JSeparator();
        s.setForeground(new Color(210, 210, 210));
        s.setMaximumSize(new Dimension(500, 1));
        return s;
    }

    private JPanel infoLine(String lbl, String val) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(500, 28));
        JLabel l1 = new JLabel(lbl); l1.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JLabel l2 = new JLabel(val); l2.setFont(new Font("Segoe UI", Font.BOLD, 15));
        p.add(l1, BorderLayout.WEST);
        p.add(l2, BorderLayout.EAST);
        return p;
    }

    private JPanel itemLine(String name, String qty, String price) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(500, 32));
        JLabel l1 = new JLabel("<html><body style='width: 250px'>" + name + "</body></html>");
        l1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JLabel l2 = new JLabel("x" + qty + "    " + price);
        l2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        p.add(l1, BorderLayout.WEST);
        p.add(l2, BorderLayout.EAST);
        return p;
    }

    private JPanel totalLine(String lbl, String val, int size) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(500, 45));
        JLabel l1 = new JLabel(lbl); l1.setFont(new Font("Segoe UI", Font.BOLD, size - 4));
        JLabel l2 = new JLabel(val); l2.setFont(new Font("Segoe UI", Font.BOLD, size));
        l2.setForeground(new Color(217, 119, 6));
        p.add(l1, BorderLayout.WEST);
        p.add(l2, BorderLayout.EAST);
        return p;
    }

    private void exportToImage(JPanel panel, String maHD) {
        try {
            File dir = new File("hoaDon");
            if (!dir.exists()) dir.mkdirs();
            
            // Ép kích thước rộng hẳn ra để không bao giờ mất chữ
            int exportW = 550; 
            int exportH = panel.getPreferredSize().height + 20;
            
            panel.setSize(exportW, exportH);
            panel.addNotify();
            panel.validate();
            
            BufferedImage bi = new BufferedImage(exportW, exportH, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = bi.createGraphics();
            
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, exportW, exportH);
            panel.printAll(g2);
            g2.dispose();
            
            File file = new File("hoaDon/" + maHD + ".png");
            ImageIO.write(bi, "png", file);
            JOptionPane.showMessageDialog(this, "Hóa đơn đã được lưu tại: " + file.getAbsolutePath());
            if (Desktop.isDesktopSupported()) Desktop.getDesktop().open(file);
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}
