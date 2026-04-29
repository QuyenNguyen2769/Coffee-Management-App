package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dao.DataManager;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class TrangChu extends JFrame {

    private static final Color BAR_BG    = new Color(245, 235, 215); // Màu be (beige)
    private static final Color BAR_HOVER = new Color(220, 210, 185); // Màu be đậm hơn khi hover
    private static final Color BAR_FG    = Color.BLACK; // Chữ màu đen
    private static final Font  BAR_FONT  = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font  ITEM_FONT = new Font("Segoe UI", Font.PLAIN, 16);

    private BufferedImage bgImage;
    private String tenNhanVien;
    private JPanel contentArea;

    public TrangChu() { this("Nguyễn Như Ngọc Quyên"); }

    public TrangChu(String tenNV) {
        this.tenNhanVien = tenNV;
        bgImage = loadBgImage();
        DataManager.loadAll();
        setTitle("Quản Lý Quán Cà Phê");
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Mở full màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(buildTopBar(), BorderLayout.NORTH);
        contentArea = buildHomePage();
        add(contentArea, BorderLayout.CENTER);
    }

    // Auto-copy AI generated images to project folder on startup
    // Đã xóa khối static tự động copy hình ảnh cũ vì người dùng đã có file ảnh trực tiếp.

    // ══════════════════════════════════════════════════════
    //  THANH MENU NGANG (custom JPanel)
    // ══════════════════════════════════════════════════════
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(242, 235, 225)); // Đổi màu nền bằng màu be nhạt (242, 235, 225) giống hình 1
        // Đã gỡ bỏ viền đen theo yêu cầu
        bar.setPreferredSize(new Dimension(0, 51));  // 34 * 1.5

        // ── Trái: menu buttons ──────────────────────────
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);

        JPanel mnuHeThong = makeMenuBtnImg("Hệ Thống", "images/icon_hethong.png");
        JPanel mnuDanhMuc = makeMenuBtnImg("Danh Mục", "images/icon_danhmuc.png");
        JPanel mnuXuLy    = makeMenuBtnImg("Xử Lý",    "images/icon_xuly.png");
        JPanel mnuTimKiem = makeMenuBtnImg("Tìm Kiếm", "images/icon_timkiem.png");
        JPanel mnuThongKe = makeMenuBtnImg("Thống Kê", "images/icon_thongke.png");

        left.add(mnuHeThong); left.add(mnuDanhMuc); left.add(mnuXuLy);
        left.add(mnuTimKiem); left.add(mnuThongKe);

        // ── Phải: QL tên + avatar (GridBagLayout để canh giữa dọc)
        JPanel right = new JPanel(new GridBagLayout());
        right.setOpaque(false);
        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.anchor = GridBagConstraints.CENTER;
        gbcR.insets = new Insets(0, 8, 0, 4);
        JLabel lblQL = new JLabel("QL: " + tenNhanVien);
        lblQL.setForeground(BAR_FG);
        lblQL.setFont(BAR_FONT);
        right.add(lblQL, gbcR);
        gbcR.insets = new Insets(0, 0, 0, 10);
        
        // Load avatar if exists, else fallback to generated
        Icon avtIcon = new ImageIcon(avatarImg());
        try {
            java.io.File f = new java.io.File("images/icon_quanly.png");
            if (f.exists()) {
                Image img = fastScale(javax.imageio.ImageIO.read(f), 32, 32);
                avtIcon = new ImageIcon(img);
            }
        } catch(Exception ex) {}
        right.add(new JLabel(avtIcon), gbcR);


        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);

        // ══ Popup: Hệ Thống ════════════════════════════
        JPopupMenu popHT = new JPopupMenu();
        JMenuItem miTrangChu = pItemImg("Trang Chủ",   "images/icon_trangchu.png");
        JMenuItem miTaiKhoan = pItemImg("Tài Khoản",   "images/icon_nhanvien.png");
        JMenuItem miTroGiup  = pItemImg("Trợ Giúp",    "images/icon_trogiup.png");
        JMenuItem miDangXuat = pItemImg("Đăng Xuất",   "images/icon_logout.png");
        JMenuItem miThoat    = pItemImg("Thoát",       "images/icon_exit.png");
        miDangXuat.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        miThoat   .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        popHT.add(miTrangChu); popHT.add(miTaiKhoan); popHT.add(miTroGiup);
        popHT.addSeparator();  popHT.add(miDangXuat); popHT.add(miThoat);
        attachPopup(mnuHeThong, popHT);

        // ══ Popup: Danh Mục ════════════════════════════
        JPopupMenu popDM = new JPopupMenu();
        JMenuItem miHoaDon    = pItemImg("Hoá Đơn",     "images/icon_hoadon.png");
        JMenuItem miKhachHang = pItemImg("Khách Hàng",  "images/icon_khachhang.png");
        JMenuItem miNhanVien  = pItemImg("Nhân Viên",   "images/icon_nhanvien.png");
        JMenuItem miDichVu    = pItemImg("Dịch Vụ",     "images/icon_dichvu.png");
        JMenuItem miKhuyenMai = pItemImg("Khuyến Mãi",  "images/icon_khuyenmai.png");
        popDM.add(miHoaDon); popDM.add(miKhachHang);
        popDM.add(miNhanVien); popDM.add(miDichVu); popDM.add(miKhuyenMai);
        attachPopup(mnuDanhMuc, popDM);

        // ══ Popup: Xử Lý ══════════════════════════════
        JPopupMenu popXL = new JPopupMenu();
        JMenuItem miPhongBan = pItemImg("Phòng Bàn",    "images/icon_phongban.png");
        JMenuItem miBanHang  = pItemImg("Bán Hàng",     "images/icon_banhang.png");
        JMenuItem miLapHD    = pItemImg("Lập Hoá Đơn",  "images/icon_hoadon.png");
        JMenuItem miDatBan   = pItemImg("Đặt Bàn",      "images/icon_phongban.png");
        popXL.add(miPhongBan); popXL.add(miBanHang);
        popXL.add(miLapHD); popXL.add(miDatBan);
        attachPopup(mnuXuLy, popXL);

        // ══ Popup: Tìm Kiếm ═══════════════════════════
        JPopupMenu popTK = new JPopupMenu();
        popTK.add(pItemImg("Tìm Nhân Viên",  "images/icon_nhanvien.png"));
        popTK.add(pItemImg("Tìm Khách Hàng", "images/icon_khachhang.png"));
        popTK.add(pItemImg("Phiếu Đặt Bàn",  "images/icon_phongban.png"));
        popTK.add(pItemImg("Tìm Hoá Đơn",    "images/icon_hoadon.png"));
        attachPopup(mnuTimKiem, popTK);

        // ══ Popup: Thống Kê ════════════════════════════
        JPopupMenu popTKe = new JPopupMenu();
        JMenu subDoanhThu = new JMenu("Doanh Thu");
        subDoanhThu.setFont(ITEM_FONT);
        try {
            java.io.File f = new java.io.File("images/icon_thongke.png");
            if (f.exists()) subDoanhThu.setIcon(new ImageIcon(fastScale(javax.imageio.ImageIO.read(f), 24, 24)));
        } catch(Exception ex) {}
        subDoanhThu.add(pItemImg("Theo Ngày",  "images/icon_calendar.png"));
        subDoanhThu.add(pItemImg("Theo Tháng", "images/icon_calendar.png"));
        subDoanhThu.add(pItemImg("Theo Năm",   "images/icon_calendar.png"));
        popTKe.add(subDoanhThu);
        popTKe.add(pItemImg("Khách Hàng", "images/icon_khachhang.png"));
        attachPopup(mnuThongKe, popTKe);



        // ══ Sự kiện ════════════════════════════════════
        miTrangChu.addActionListener(e -> switchContent(buildHomePage()));
        miDangXuat.addActionListener(e -> {
            if (confirm("Bạn có muốn đăng xuất không?", "Đăng Xuất")) {
                dispose(); new Login().setVisible(true);
            }
        });
        miThoat.addActionListener(e -> {
            if (confirm("Bạn có muốn thoát không?", "Thoát")) System.exit(0);
        });
        miNhanVien .addActionListener(e -> switchContent(new PanelQuanLyNhanVien()));
        miHoaDon   .addActionListener(e -> switchContent(placeholder("Quản Lý Hoá Đơn")));
        miKhachHang.addActionListener(e -> switchContent(placeholder("Quản Lý Khách Hàng")));
        miDichVu   .addActionListener(e -> switchContent(new PanelDichVu()));
        miKhuyenMai.addActionListener(e -> switchContent(new PanelKhuyenMai()));
        miPhongBan .addActionListener(e -> switchContent(new PanelQuanLyPhongBan()));
        miBanHang  .addActionListener(e -> switchContent(placeholder("Bán Hàng")));
        miLapHD    .addActionListener(e -> switchContent(placeholder("Lập Hoá Đơn")));
        miDatBan   .addActionListener(e -> switchContent(new PanelQuanLyPhongBan()));
        miTroGiup.addActionListener(e -> switchContent(new PanelTroGiup()));
        subDoanhThu.getItem(0).addActionListener(
        	    e -> switchContent(PanelThongKe.theoNgay()));
        	subDoanhThu.getItem(1).addActionListener(
        	    e -> switchContent(PanelThongKe.theoThang()));
        	subDoanhThu.getItem(2).addActionListener(
        	    e -> switchContent(PanelThongKe.theoNam()));
        	((JMenuItem) popTKe.getComponent(1)).addActionListener(
        	    e -> switchContent(PanelThongKe.khachHang()));
        return bar;
    }

    // ══════════════════════════════════════════════════════
    //  HELPER: nút menu trên thanh ngang
    // ══════════════════════════════════════════════════════
    private JPanel makeMenuBtnImg(String text, String imgPath) {
        JPanel btn = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(0, 8, 0, 8));
        
        try {
            java.io.File f = new java.io.File(imgPath);
            if (f.exists()) {
                Image img = fastScale(javax.imageio.ImageIO.read(f), 30, 30);
                btn.add(new JLabel(new ImageIcon(img)));
            }
        } catch (Exception e) {}

        JLabel lbl = new JLabel(text);
        lbl.setFont(BAR_FONT); lbl.setForeground(BAR_FG);
        btn.add(lbl);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(BAR_HOVER); btn.setOpaque(true); btn.repaint(); }
            public void mouseExited (MouseEvent e) { btn.setOpaque(false); btn.repaint(); }
        });
        return btn;
    }

    private JPanel makeMenuBtnE(String label) {
        JPanel btn = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(0, 8, 0, 8));
        JLabel lbl = new JLabel(label);
        lbl.setFont(BAR_FONT);
        lbl.setForeground(BAR_FG);
        btn.add(lbl);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(BAR_HOVER); btn.setOpaque(true); btn.repaint(); }
            public void mouseExited (MouseEvent e) { btn.setOpaque(false); btn.repaint(); }
        });
        return btn;
    }

    private JPanel makeMenuBtn(String text, BufferedImage icon) {
        JPanel btn = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(0, 8, 0, 8));
        btn.add(new JLabel(new ImageIcon(icon)));
        JLabel lbl = new JLabel(text);
        lbl.setFont(BAR_FONT); lbl.setForeground(BAR_FG);
        btn.add(lbl);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(BAR_HOVER); btn.setOpaque(true); btn.repaint(); }
            public void mouseExited (MouseEvent e) { btn.setOpaque(false); btn.repaint(); }
        });
        return btn;
    }

    private void attachPopup(JPanel btn, JPopupMenu pop) {
        btn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { pop.show(btn, 0, btn.getHeight()); }
        });
    }

    // ══════════════════════════════════════════════════════
    //  HELPER: JMenuItem load ảnh thật
    // ══════════════════════════════════════════════════════
    // ══════════════════════════════════════════════════════
    //  TỐI ƯU HÓA LOAD ẢNH: Scale ảnh siêu tốc
    // ══════════════════════════════════════════════════════
    private Image fastScale(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    private JMenuItem pItemImg(String text, String imgPath) {
        JMenuItem mi = new JMenuItem("  " + text);
        try {
            java.io.File f = new java.io.File(imgPath);
            if (f.exists()) {
                Image img = fastScale(javax.imageio.ImageIO.read(f), 24, 24);
                mi.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {}
        mi.setFont(ITEM_FONT);
        mi.setIconTextGap(8);
        return mi;
    }

    private JMenuItem pItem(String text, Color color, char shape) {
        JMenuItem mi = new JMenuItem("  " + text, new ImageIcon(icon16(color, shape)));
        mi.setFont(ITEM_FONT);
        mi.setIconTextGap(8);
        return mi;
    }

    // ══════════════════════════════════════════════════════
    //  VẼ ICON 20x20 cho menu bar (nền tròn màu + ký hiệu trắng)
    // ══════════════════════════════════════════════════════
    private BufferedImage icon20(Color bg, char shape) {
        int sz = 30;  // 20 * 1.5
        BufferedImage img = new BufferedImage(sz, sz, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(bg);
        g.fillOval(0, 0, sz, sz);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        drawShape(g, shape, sz);
        g.dispose();
        return img;
    }

    // ══════════════════════════════════════════════════════
    //  VẼ ICON 16x16 cho popup menu (nhỏ hơn)
    // ══════════════════════════════════════════════════════
    private BufferedImage icon16(Color bg, char shape) {
        int sz = 24;  // 16 * 1.5
        BufferedImage img = new BufferedImage(sz, sz, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(bg);
        g.fillOval(0, 0, sz, sz);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        drawShape(g, shape, sz);
        g.dispose();
        return img;
    }

    /** Vẽ ký hiệu lên icon 20x20 */
    private void drawShape(Graphics2D g, char shape, int sz) {
        int m = sz / 2;
        switch (shape) {
            case 'G': // Gear — bánh răng
                g.drawOval(m-4,m-4,8,8);
                for (int i=0;i<4;i++){
                    double a=Math.PI/4+Math.PI/2*i;
                    int x=(int)(m+7*Math.cos(a)), y=(int)(m+7*Math.sin(a));
                    g.fillOval(x-2,y-2,4,4);
                }
                break;
            case 'D': // Grid — 4 ô vuông
                g.fillRect(m-5,m-5,4,4); g.fillRect(m+1,m-5,4,4);
                g.fillRect(m-5,m+1,4,4); g.fillRect(m+1,m+1,4,4);
                break;
            case 'X': // Lightning — tia sét
                int[] px={m,m-3,m,m+3,m}; int[] py={m-6,m,m-1,m+1,m+6};
                g.fillPolygon(px,py,5);
                break;
            case 'S': // Search — kính lúp
                g.drawOval(m-5,m-5,9,9);
                g.drawLine(m+3,m+3,m+6,m+6);
                break;
            case 'C': // Chart — cột biểu đồ
                g.fillRect(m-6,m+1,3,5); g.fillRect(m-2,m-2,3,8); g.fillRect(m+2,m-5,3,11);
                g.drawLine(m-7,m+6,m+7,m+6);
                break;
        }
    }

    /** Vẽ ký hiệu lên icon 16x16 */
    private void drawShape16(Graphics2D g, char shape) {
        switch (shape) {
            case 'H': // House — ngôi nhà
                g.drawPolygon(new int[]{8,3,13}, new int[]{3,9,9}, 3);
                g.fillRect(5,9,6,5);
                break;
            case 'U': // User — người
                g.fillOval(5,3,6,6);
                g.fillRoundRect(3,10,10,5,4,4);
                break;
            case 'Q': // Question — dấu ?
                g.drawArc(5,3,6,6,0,210);
                g.fillRect(7,10,2,2); g.fillRect(7,13,2,2);
                break;
            case 'E': // Exit — cửa ra
                g.drawRect(3,4,6,8); g.drawLine(10,8,14,8);
                g.drawLine(12,6,14,8); g.drawLine(12,10,14,8);
                break;
            case 'A': // Arrow out
                g.drawLine(3,8,11,8);
                g.drawLine(9,5,11,8); g.drawLine(9,11,11,8);
                g.drawLine(13,3,13,13);
                break;
            case 'P': // Product — hộp
                g.drawRect(4,6,8,7); g.drawLine(4,6,8,3); g.drawLine(12,6,8,3);
                break;
            case 'G': // Gift — hộp quà
                g.fillRect(3,8,10,6); g.fillRect(4,6,8,2);
                g.drawLine(8,6,8,14); g.drawArc(5,4,3,4,0,180); g.drawArc(8,4,3,4,0,180);
                break;
            case 'B': // Bag/Cart — giỏ hàng
                g.drawOval(5,3,6,4); g.fillRect(3,8,10,5);
                g.drawLine(5,5,4,8); g.drawLine(11,5,12,8);
                break;
            case 'F': // File — hoá đơn
                g.drawRect(4,2,8,12); g.drawLine(6,6,10,6);
                g.drawLine(6,9,10,9); g.drawLine(6,12,9,12);
                break;
            case 'S': // Search
                g.drawOval(4,4,7,7); g.drawLine(10,10,13,13);
                break;
            case 'C': // Chart
                g.fillRect(3,10,3,4); g.fillRect(7,7,3,7); g.fillRect(11,4,3,10);
                g.drawLine(2,14,14,14);
                break;
        }
    }

    // ══════════════════════════════════════════════════════
    //  Avatar tròn góc phải menu bar
    // ══════════════════════════════════════════════════════
    private BufferedImage avatarImg() {
        BufferedImage img = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(210,160,60)); g.fillOval(0,0,24,24);
        g.setColor(Color.WHITE);
        g.fillOval(9,4,8,8);
        g.fillRoundRect(5,13,14,9,6,6);
        g.dispose();
        return img;
    }

    // ══════════════════════════════════════════════════════
    //  Nội dung chính
    // ══════════════════════════════════════════════════════
    private JPanel buildHomePage() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                if (bgImage != null) {
                    g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g2.setPaint(new GradientPaint(0,0,new Color(80,50,25),0,getHeight(),new Color(20,10,5)));
                    g2.fillRect(0,0,getWidth(),getHeight());
                }
            }
        };
        p.setOpaque(true);
        return p;
    }

    private void switchContent(JPanel panel) {
        remove(contentArea); contentArea = panel;
        add(contentArea, BorderLayout.CENTER);
        revalidate(); repaint();
    }

    private JPanel placeholder(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(245,246,250));
        JLabel lbl = new JLabel(title, JLabel.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lbl.setForeground(new Color(100,70,40));
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    private boolean confirm(String msg, String title) {
        return JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private BufferedImage loadBgImage() {
        for (String p : new String[]{"images/background.jpg","images/background.png"}) {
            try { File f = new File(p); if (f.exists()) return ImageIO.read(f); }
            catch (Exception ignored) {}
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TrangChu("Nguyễn Như Ngọc Quyên").setVisible(true));
    }
}
