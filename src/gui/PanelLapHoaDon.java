package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import dao.*;
import entity.*;

public class PanelLapHoaDon extends JPanel {

    private static final Color BG_PAGE    = new Color(252, 248, 243);
    private static final Color CLR_BROWN  = new Color(101, 67,  33);
    private static final Color CLR_AMBER  = new Color(217, 119, 6);
    private static final Color CLR_CREAM  = new Color(254, 243, 224);
    private static final Color CLR_GREEN  = new Color(22,  163, 74);
    private static final Color CLR_GRAY   = new Color(107, 114, 128);
    private static final Color CLR_BORDER = new Color(229, 215, 195);
    private static final Color CLR_TEXT   = new Color(28,  20,  10);
    private static final Color CLR_WHITE  = Color.WHITE;

    private DefaultTableModel cartModel;
    private JTable            cartTable;
    private JLabel            lblTotal;
    private JPanel            menuPanel;
    private double            totalAmount = 0;
    
    // Thêm các biến quản lý Khách hàng & Khuyến mãi
    private JTextField        txtSDT;
    private JLabel            lblKhachInfo;
    private JComboBox<String> cbKM;
    private JLabel            lblGiamGia;
    private JLabel            lblVAT;
    private JLabel            lblTienHang;
    private KhachHang         khHienTai = null;
    private ArrayList<KhuyenMai> dsKM = new ArrayList<>();
    private double            tienGiam = 0;
    private double            tienVAT = 0;
    private DecimalFormat     df = new DecimalFormat("#,###đ");

    public PanelLapHoaDon() {
        setLayout(new BorderLayout(10, 0));
        setBackground(BG_PAGE);
        setBorder(new EmptyBorder(15, 20, 15, 20));

        add(buildLeftPanel(), BorderLayout.CENTER);
        add(buildRightPanel(), BorderLayout.EAST);
    }

    // ── BÊN TRÁI: DANH SÁCH MÓN ───────────────────────────────────
    private JPanel buildLeftPanel() {
        JPanel left = new JPanel(new BorderLayout(0, 15));
        left.setOpaque(false);

        // Header tiêu đề & Tìm kiếm
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);
        JLabel title = new JLabel("  BÁN HÀNG");
        try {
            ImageIcon icon = new ImageIcon("images/icon_banhang.png");
            Image img = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            title.setIcon(new ImageIcon(img));
        } catch(Exception e) {}
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(CLR_BROWN);
        
        JTextField txtSearch = new JTextField(15);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtSearch.setText("Tìm tên món...");
        txtSearch.addActionListener(e -> {
            String query = txtSearch.getText().trim();
            if (query.equals("Tìm tên món...")) query = "";
            loadMenu(query);
        });

        // Xóa text khi click vào ô search
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals("Tìm tên món...")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(CLR_TEXT);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText("Tìm tên món...");
                    txtSearch.setForeground(CLR_GRAY);
                }
            }
        });

        hdr.add(title, BorderLayout.WEST);
        hdr.add(txtSearch, BorderLayout.EAST);
        left.add(hdr, BorderLayout.NORTH);

        // Lưới danh sách món
        menuPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 12, 12));
        menuPanel.setBackground(CLR_WHITE);
        menuPanel.setBorder(new CompoundBorder(
            new LineBorder(CLR_BORDER, 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));

        loadMenu("");

        JScrollPane scroll = new JScrollPane(menuPanel);
        scroll.getVerticalScrollBar().setUnitIncrement(100); // Tốc độ tối ưu
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CLR_WHITE);
        left.add(scroll, BorderLayout.CENTER);

        return left;
    }

    private void loadMenu(String query) {
        menuPanel.removeAll();
        for (Object[] row : DataManager.getDsDichVu()) {
            String tenSP = row[1].toString().toLowerCase();
            boolean matches = query.isEmpty() || tenSP.contains(query.toLowerCase());
            
            if (matches && row[5].toString().contains("Phục Vụ")) {
                menuPanel.add(buildItemCard(row));
            }
        }
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private JPanel buildItemCard(Object[] data) {
        String tenMon = data[1].toString();
        JPanel card = new JPanel(new BorderLayout(0, 5)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(CLR_BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(175, 215)); 
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Logic tìm ảnh thông minh
        JLabel lblImg = new JLabel("", SwingConstants.CENTER);
        String path = findBestImagePath(tenMon);
        
        if (path != null) {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(145, 115, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(img));
        } else {
            // Placeholder nếu không có ảnh
            lblImg.setText("IMAGE");
            lblImg.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblImg.setForeground(CLR_GRAY);
        }
        
        lblImg.setOpaque(true);
        lblImg.setBackground(new Color(250, 244, 234));
        lblImg.setPreferredSize(new Dimension(0, 115));

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        JLabel lblName = new JLabel("<html><body style='width: 130px'>" + tenMon + "</body></html>");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        JLabel lblPrice = new JLabel(formatPrice(parsePrice(data[3].toString())));
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 15)); 
        lblPrice.setForeground(CLR_AMBER);
        info.add(lblName);
        info.add(lblPrice);

        card.add(lblImg, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                addToCart(tenMon, data[3].toString());
            }
            @Override public void mouseEntered(MouseEvent e) {
                card.setBackground(CLR_CREAM);
                card.repaint();
            }
        });

        return card;
    }

    private String findBestImagePath(String tenMon) {
        String[] extensions = {".jpg", ".png", ".jpeg"};
        String cleanName = convertToFileName(tenMon);
        
        // 1. Thử tên đầy đủ (ví dụ: aquafina500ml)
        for (String ext : extensions) {
            String p = "images/" + cleanName + ext;
            if (new File(p).exists()) return p;
        }
        
        // 2. Thử từ đầu tiên (ví dụ: aquafina)
        String firstWord = cleanName.split("[0-9]")[0]; // Bỏ phần số nếu có
        if (firstWord.length() > 2) {
            for (String ext : extensions) {
                String p = "images/" + firstWord + ext;
                if (new File(p).exists()) return p;
            }
        }

        // 3. Xử lý trường hợp đặc biệt
        if (cleanName.contains("cappuccino")) {
             if (new File("images/capuchino.jpg").exists()) return "images/capuchino.jpg";
        }
        if (cleanName.contains("c2")) {
             if (new File("images/C2.jpg").exists()) return "images/C2.jpg";
        }
        if (cleanName.contains("coca")) return "images/cocacola.jpg";
        if (cleanName.contains("pepsi")) return "images/pepsi.jpg";
        if (cleanName.contains("7up")) return "images/7up.jpg";

        return null;
    }

    private String convertToFileName(String str) {
        if (str == null) return "";
        // Chuyển về chữ thường, bỏ dấu và khoảng trắng
        String n = str.toLowerCase()
            .replace("à", "a").replace("á", "a").replace("ạ", "a").replace("ả", "a").replace("ã", "a")
            .replace("â", "a").replace("ầ", "a").replace("ấ", "a").replace("ậ", "a").replace("ẩ", "a").replace("ẫ", "a")
            .replace("ă", "a").replace("ằ", "a").replace("ắ", "a").replace("ặ", "a").replace("ẳ", "a").replace("ẵ", "a")
            .replace("è", "e").replace("é", "e").replace("ẹ", "e").replace("ẻ", "e").replace("ẽ", "e")
            .replace("ê", "e").replace("ề", "e").replace("ế", "e").replace("ệ", "e").replace("ể", "e").replace("ễ", "e")
            .replace("ì", "i").replace("í", "i").replace("ị", "i").replace("ỉ", "i").replace("ĩ", "i")
            .replace("ò", "o").replace("ó", "o").replace("ọ", "o").replace("ỏ", "o").replace("õ", "o")
            .replace("ô", "o").replace("ồ", "o").replace("ố", "o").replace("ộ", "o").replace("ổ", "o").replace("ỗ", "o")
            .replace("ơ", "o").replace("ờ", "o").replace("ớ", "o").replace("ợ", "o").replace("ở", "o").replace("ỡ", "o")
            .replace("ù", "u").replace("ú", "u").replace("ụ", "u").replace("ủ", "u").replace("ũ", "u")
            .replace("ư", "u").replace("ừ", "u").replace("ứ", "u").replace("ự", "u").replace("ử", "u").replace("ữ", "u")
            .replace("ỳ", "y").replace("ý", "y").replace("ỵ", "y").replace("ỷ", "y").replace("ỹ", "y")
            .replace("đ", "d")
            .replace(" ", "");
        return n;
    }


    // ── BÊN PHẢI: GIỎ HÀNG ──────────────────────────────────
    private JPanel buildRightPanel() {
        JPanel right = new JPanel(new BorderLayout(0, 10));
        right.setPreferredSize(new Dimension(480, 0)); // Tăng lên 480 để nhích bảng qua trái
        right.setBackground(CLR_WHITE);
        right.setBorder(new CompoundBorder(
            new MatteBorder(0, 1, 0, 0, CLR_BORDER),
            new EmptyBorder(10, 15, 10, 15)
        ));

        JLabel title = new JLabel("ĐƠN HÀNG");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        right.add(title, BorderLayout.NORTH);

        // Bảng giỏ hàng
        String[] cols = {"Tên Món", "SL", "Thành Tiền"};
        cartModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { 
                return c == 1; // Chỉ cho phép sửa cột Số Lượng
            }
        };
        cartTable = new JTable(cartModel);
        cartTable.setRowHeight(45);
        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        cartTable.setShowVerticalLines(false);
        cartTable.setGridColor(new Color(245, 245, 245));

        // Lắng nghe sự thay đổi số lượng
        cartModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 1) {
                int row = e.getFirstRow();
                try {
                    int newQty = Integer.parseInt(cartModel.getValueAt(row, 1).toString());
                    if (newQty <= 0) {
                        cartModel.removeRow(row);
                    } else {
                        updateRowPrice(row, newQty);
                    }
                } catch (Exception ex) {
                    // Nếu nhập sai định dạng thì không làm gì hoặc báo lỗi
                }
                updateTotal();
            }
        });

        JTableHeader header = cartTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        JScrollPane scroll = new JScrollPane(cartTable);
        scroll.getVerticalScrollBar().setUnitIncrement(100);
        scroll.setBorder(new LineBorder(new Color(240, 240, 240)));
        right.add(scroll, BorderLayout.CENTER);

        // Phần tổng tiền & nút
        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setOpaque(false);

        // 1. Dòng Khách hàng (SĐT)
        JPanel pnlKhach = new JPanel(new BorderLayout(10, 0));
        pnlKhach.setOpaque(false);
        txtSDT = new JTextField(10);
        txtSDT.setFont(new Font("Segoe UI", Font.BOLD, 15));
        txtSDT.setBorder(new TitledBorder("SĐT Khách"));
        lblKhachInfo = new JLabel("Khách lẻ");
        lblKhachInfo.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblKhachInfo.setForeground(CLR_BROWN);
        pnlKhach.add(txtSDT, BorderLayout.WEST);
        pnlKhach.add(lblKhachInfo, BorderLayout.CENTER);
        
        // 2. Dòng Khuyến mãi
        JPanel pnlKM = new JPanel(new BorderLayout(10, 0));
        pnlKM.setOpaque(false);
        cbKM = new JComboBox<>(new String[]{"Không có"});
        cbKM.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbKM.setBorder(new TitledBorder("Chọn Khuyến mãi"));
        pnlKM.add(cbKM, BorderLayout.CENTER);

        // 3. Bảng chi tiết giá
        JPanel pnlPriceDetail = new JPanel(new GridLayout(3, 2, 0, 5));
        pnlPriceDetail.setOpaque(false);
        pnlPriceDetail.setBorder(new EmptyBorder(10, 5, 10, 5));
        
        pnlPriceDetail.add(new JLabel("Tiền hàng:"));
        lblTienHang = new JLabel("0đ", SwingConstants.RIGHT);
        pnlPriceDetail.add(lblTienHang);
        
        pnlPriceDetail.add(new JLabel("Giảm giá:"));
        lblGiamGia = new JLabel("-0đ", SwingConstants.RIGHT);
        lblGiamGia.setForeground(CLR_AMBER);
        pnlPriceDetail.add(lblGiamGia);
        
        pnlPriceDetail.add(new JLabel("Thuế VAT (10%):"));
        lblVAT = new JLabel("0đ", SwingConstants.RIGHT);
        pnlPriceDetail.add(lblVAT);

        // 4. Tổng cộng
        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(false);
        totalRow.setBorder(new EmptyBorder(5, 0, 10, 0));
        JLabel lblT1 = new JLabel("TỔNG CỘNG:");
        lblT1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal = new JLabel("0đ");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 32)); 
        lblTotal.setForeground(new Color(192, 57, 43));
        totalRow.add(lblT1, BorderLayout.WEST);
        totalRow.add(lblTotal, BorderLayout.EAST);

        JButton btnPay = new JButton("THANH TOÁN & IN");
        try {
            ImageIcon printIcon = new ImageIcon("images/icon_hoadon.png");
            Image imgPrint = printIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            btnPay.setIcon(new ImageIcon(imgPrint));
        } catch (Exception e) {}

        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btnPay.setBackground(new Color(101, 67, 33)); // Màu Nâu đậm
        btnPay.setForeground(Color.WHITE);            // Chữ Trắng tinh
        btnPay.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70)); // Full width
        btnPay.setFocusPainted(false);
        btnPay.setBorderPainted(false);
        btnPay.setOpaque(true);
        btnPay.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPay.setCursor(new Cursor(Cursor.HAND_CURSOR));

        footer.add(pnlKhach);
        footer.add(pnlKM);
        footer.add(pnlPriceDetail);
        footer.add(totalRow);
        footer.add(Box.createVerticalStrut(10));
        footer.add(btnPay);
        right.add(footer, BorderLayout.SOUTH);

        // --- SỰ KIỆN ---
        txtSDT.addActionListener(e -> findCustomer());
        cbKM.addActionListener(e -> tinhToanGia());
        btnPay.addActionListener(e -> processCheckout());

        return right;
    }

    private void findCustomer() {
        String sdt = txtSDT.getText().trim();
        if (sdt.isEmpty()) {
            khHienTai = null;
            lblKhachInfo.setText("Khách lẻ");
            tinhToanGia();
            return;
        }
        KhachHangDAO dao = new KhachHangDAO();
        khHienTai = dao.timKhachHangTheoSDT(sdt);
        if (khHienTai != null) {
            lblKhachInfo.setText(khHienTai.getHoTen() + " (" + khHienTai.getLoaiKhach() + ")");
            lblKhachInfo.setForeground(CLR_GREEN);
        } else {
            lblKhachInfo.setText("SĐT không tồn tại");
            lblKhachInfo.setForeground(Color.RED);
        }
        loadValidPromotions();
        tinhToanGia();
    }

    private void loadValidPromotions() {
        cbKM.removeAllItems();
        cbKM.addItem("Không có");
        dsKM.clear();
        
        double currentSubtotal = 0;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            currentSubtotal += parsePrice(cartModel.getValueAt(i, 2).toString());
        }

        KhuyenMaiDAO dao = new KhuyenMaiDAO();
        ArrayList<KhuyenMai> all = dao.getKhuyenMaiHopLe(0);
        for (KhuyenMai km : all) {
            if (km.getDieuKienToiThieu() <= currentSubtotal) {
                dsKM.add(km);
                cbKM.addItem(km.getTenKM() + " (Giảm " + km.getMucGiamGia() + "%)");
            }
        }
    }

    private void tinhToanGia() {
        double subtotal = 0;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            subtotal += parsePrice(cartModel.getValueAt(i, 2).toString());
        }
        lblTienHang.setText(df.format(subtotal));

        int idx = cbKM.getSelectedIndex();
        if (idx > 0 && idx <= dsKM.size()) {
            KhuyenMai km = dsKM.get(idx - 1);
            tienGiam = subtotal * (km.getMucGiamGia() / 100.0);
        } else {
            tienGiam = 0;
        }
        
        if (khHienTai != null && khHienTai.getLoaiKhach().equalsIgnoreCase("VIP")) {
            tienGiam += subtotal * 0.05;
        }
        
        lblGiamGia.setText("-" + df.format(tienGiam));
        tienVAT = (subtotal - tienGiam) * 0.1;
        lblVAT.setText(df.format(tienVAT));

        totalAmount = subtotal - tienGiam + tienVAT;
        lblTotal.setText(df.format(totalAmount));
    }

    private void addToCart(String name, String priceStr) {
        double unitPrice = parsePrice(priceStr);
        boolean found = false;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            if (cartModel.getValueAt(i, 0).equals(name)) {
                int qty = Integer.parseInt(cartModel.getValueAt(i, 1).toString()) + 1;
                cartModel.setValueAt(qty, i, 1);
                // Tự động kích hoạt listener để tính lại thành tiền
                found = true;
                break;
            }
        }
        if (!found) {
            cartModel.addRow(new Object[]{name, 1, priceStr});
        }
        updateTotal();
    }

    private void updateTotal() {
        loadValidPromotions();
        tinhToanGia();
    }

    private void processCheckout() {
        if (totalAmount == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món trước khi thanh toán!");
            return;
        }
        int c = JOptionPane.showConfirmDialog(this, "Xác nhận thanh toán " + df.format(totalAmount) + "?", "Thanh Toán", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            // Lưu hóa đơn vào DB
            String tenKhach = (khHienTai != null) ? khHienTai.getHoTen() : "Khách Lẻ";
            Integer maKH_val = null;
            try { if (khHienTai != null) maKH_val = Integer.parseInt(khHienTai.getMaKH()); }
            catch (Exception ignored) {}
            int maHD_int = DataManager.themHoaDon(1, 1, maKH_val, totalAmount, "Đã thanh toán");
            String maHD = "HD" + String.format("%03d", maHD_int > 0 ? maHD_int : DataManager.getDsHoaDon().size());
            
            // Chuẩn hóa dữ liệu bảng để khớp với DialogHoaDon (5 cột: ID, Tên, Loại, SL, TT)
            DefaultTableModel modelXuat = new DefaultTableModel(new String[]{"ID", "Tên", "Loại", "SL", "Thành Tiền"}, 0);
            for (int i = 0; i < cartModel.getRowCount(); i++) {
                modelXuat.addRow(new Object[]{
                    "", 
                    cartModel.getValueAt(i, 0), // Tên
                    "", 
                    cartModel.getValueAt(i, 1), // SL
                    cartModel.getValueAt(i, 2)  // Thành Tiền
                });
            }

            DialogHoaDon dlg = new DialogHoaDon(
                (Frame)SwingUtilities.getWindowAncestor(this), 
                maHD, "admin", tenKhach, "", modelXuat, totalAmount
            );
            dlg.setVisible(true);
            
            // Reset
            cartModel.setRowCount(0);
            txtSDT.setText("");
            khHienTai = null;
            lblKhachInfo.setText("Khách lẻ");
            updateTotal();
        }
    }

    private void updateRowPrice(int row, int qty) {
        String name = cartModel.getValueAt(row, 0).toString();
        double unitPrice = 0;
        for (Object[] item : DataManager.getDsDichVu()) {
            if (item[1].toString().equals(name)) {
                unitPrice = parsePrice(item[3].toString());
                break;
            }
        }
        cartModel.setValueAt(formatPrice(qty * unitPrice), row, 2);
    }

    private double parsePrice(String priceStr) {
        if (priceStr == null) return 0;
        String clean = priceStr.replaceAll("[^0-9]", "");
        try {
            return Double.parseDouble(clean);
        } catch (Exception e) { return 0; }
    }

    private String formatPrice(double p) {
        return String.format("%,.0fđ", p);
    }

    // WrapLayout Helper
    static class WrapLayout extends FlowLayout {
        WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }
        @Override public Dimension preferredLayoutSize(Container t) { return layout(t, true); }
        @Override public Dimension minimumLayoutSize(Container t)   { return layout(t, false); }
        private Dimension layout(Container t, boolean pref) {
            synchronized (t.getTreeLock()) {
                int w = t.getWidth(); if (w == 0) w = Integer.MAX_VALUE;
                Insets ins = t.getInsets();
                w -= ins.left + ins.right + getHgap() * 2;
                int x = getHgap(), y = getVgap(), rowH = 0, maxW = 0;
                for (Component c : t.getComponents()) {
                    if (!c.isVisible()) continue;
                    Dimension d = pref ? c.getPreferredSize() : c.getMinimumSize();
                    if (x + d.width > w) { x = getHgap(); y += rowH + getVgap(); rowH = 0; }
                    x += d.width + getHgap(); rowH = Math.max(rowH, d.height);
                    maxW = Math.max(maxW, x);
                }
                return new Dimension(maxW + ins.left + ins.right, y + rowH + getVgap() + ins.top + ins.bottom);
            }
        }
    }
}
