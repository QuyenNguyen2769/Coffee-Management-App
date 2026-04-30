package gui;

import connectDB.ConnectDB;
import entity.TaiKhoan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private TaiKhoan taiKhoan;
    private JPanel contentArea;

    // Khai báo các Panel
    private PanelNhanVien pnNhanVien;
    private PanelKhachHang pnKhachHang;
    private PanelTaiKhoan pnTaiKhoan;
    private PanelTimNhanVien pnTimNhanVien;
    private PanelTimKhachHang pnTimKhachHang;
    private PanelPhieuDatBan pnPhieuDatBan;
    private PanelTimHoaDon pnTimHoaDon;

    public TrangChu() { this("Nguyễn Như Ngọc Quyên", new TaiKhoan("Admin", "AD001", "123", "Quản lý", "HoatDong")); }

    public TrangChu(String tenNV, TaiKhoan tk) {
        dao.DataManager.loadAll(); // Load dữ liệu của Mạnh
        this.tenNhanVien = tenNV;
        this.taiKhoan = tk;
        bgImage = loadBgImage();
        setTitle("Quản Lý Quán Cà Phê");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Khởi tạo các panel
        pnNhanVien = new PanelNhanVien();
        pnKhachHang = new PanelKhachHang();
        pnTaiKhoan = new PanelTaiKhoan(tk);
        pnTimNhanVien = new PanelTimNhanVien();
        pnTimKhachHang = new PanelTimKhachHang(pnKhachHang);
        pnPhieuDatBan = new PanelPhieuDatBan();
        pnTimHoaDon = new PanelTimHoaDon();

        add(buildTopBar(), BorderLayout.NORTH);
        contentArea = buildHomePage();
        add(contentArea, BorderLayout.CENTER);
    }

    // ══════════════════════════════════════════════════════
    //  THANH MENU NGANG (custom JPanel)
    // ══════════════════════════════════════════════════════
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(242, 235, 225)); // Đổi màu nền bằng màu be nhạt (242, 235, 225) giống hình 1
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
        JMenuItem miPhongBan = pItemImg("Phòng Bàn",   "images/icon_phongban.png");
        JMenuItem miLapHD    = pItemImg("Lập Hoá Đơn", "images/icon_hoadon.png");
        popXL.add(miPhongBan);
        popXL.add(miLapHD);
        attachPopup(mnuXuLy, popXL);

        // ══ Popup: Tìm Kiếm ═══════════════════════════
        JPopupMenu popTK = new JPopupMenu();
        JMenuItem miTimNhanVien = pItemImg("Tìm Nhân Viên",  "images/icon_nhanvien.png");
        JMenuItem miTimKhachHang = pItemImg("Tìm Khách Hàng", "images/icon_khachhang.png");
        JMenuItem miPhieuDatBan = pItemImg("Phiếu Đặt Bàn",  "images/icon_phongban.png");
        JMenuItem miTimHoaDon = pItemImg("Tìm Hoá Đơn",    "images/icon_hoadon.png");
        popTK.add(miTimNhanVien); popTK.add(miTimKhachHang);
        popTK.add(miPhieuDatBan); popTK.add(miTimHoaDon);
        attachPopup(mnuTimKiem, popTK);

        // ══ Popup: Thống Kê ════════════════════════════
        JPopupMenu popTKe = new JPopupMenu();
        JMenu subDoanhThu = new JMenu("Doanh Thu");
        subDoanhThu.setFont(ITEM_FONT);
        try {
            java.io.File f = new java.io.File("images/icon_thongke.png");
            if (f.exists()) subDoanhThu.setIcon(new ImageIcon(fastScale(javax.imageio.ImageIO.read(f), 24, 24)));
        } catch(Exception ex) {}
        
        JMenuItem miDTNgay = pItemImg("Theo Ngày",  "images/icon_calendar.png");
        JMenuItem miDTThang = pItemImg("Theo Tháng", "images/icon_calendar.png");
        JMenuItem miDTNam = pItemImg("Theo Năm",   "images/icon_calendar.png");
        
        subDoanhThu.add(miDTNgay);
        subDoanhThu.add(miDTThang);
        subDoanhThu.add(miDTNam);
        
        JMenuItem miTKKH = pItemImg("Khách Hàng", "images/icon_khachhang.png");
        
        popTKe.add(subDoanhThu);
        popTKe.add(miTKKH);
        attachPopup(mnuThongKe, popTKe);


        // ══ Sự kiện ════════════════════════════════════
        miTrangChu.addActionListener(e -> switchContent(buildHomePage()));
        miTaiKhoan.addActionListener(e -> switchContent(pnTaiKhoan));
        miTroGiup.addActionListener(e -> switchContent(new PanelTroGiup()));
        miDangXuat.addActionListener(e -> {
            if (confirm("Bạn có muốn đăng xuất không?", "Đăng Xuất")) {
                dispose(); new Login().setVisible(true);
            }
        });
        miThoat.addActionListener(e -> {
            if (confirm("Bạn có muốn thoát không?", "Thoát")) System.exit(0);
        });

        miNhanVien .addActionListener(e -> { pnNhanVien.loadData(); switchContent(pnNhanVien); });
        miKhachHang.addActionListener(e -> { pnKhachHang.loadData(); switchContent(pnKhachHang); });
        miHoaDon   .addActionListener(e -> switchContent(new PanelHoaDon()));
        miDichVu   .addActionListener(e -> switchContent(new PanelDichVu()));
        miKhuyenMai.addActionListener(e -> switchContent(new PanelKhuyenMai()));
        miPhongBan.addActionListener(e -> switchContent(new PanelQuanLyPhongBan()));
        miLapHD    .addActionListener(e -> switchContent(new PanelLapHoaDon()));

        // Thống kê
        miDTNgay.addActionListener(e -> switchContent(PanelThongKe.theoNgay()));
        miDTThang.addActionListener(e -> switchContent(PanelThongKe.theoThang()));
        miDTNam.addActionListener(e -> switchContent(PanelThongKe.theoNam()));
        miTKKH.addActionListener(e -> switchContent(PanelThongKe.khachHang()));

        miTimNhanVien.addActionListener(e -> switchContent(pnTimNhanVien));
        miTimKhachHang.addActionListener(e -> switchContent(pnTimKhachHang));
        miPhieuDatBan.addActionListener(e -> switchContent(pnPhieuDatBan));
        miTimHoaDon.addActionListener(e -> switchContent(pnTimHoaDon));
        
        return bar;
    }

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

    private void attachPopup(JPanel btn, JPopupMenu pop) {
        btn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { pop.show(btn, 0, btn.getHeight()); }
        });
    }

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
        SwingUtilities.invokeLater(() -> new TrangChu().setVisible(true));
    }
}
