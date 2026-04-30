package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PanelQuanLyPhongBan extends JPanel {

    private JPanel pnlTables;
    private JPanel selectedTableCard = null;

    // Cache hình ảnh bàn để tránh đọc file nhiều lần
    private static ImageIcon tableIconCache = null;

    private ImageIcon getTableIcon() {
        if (tableIconCache == null) {
            try {
                // Lấy trực tiếp file "coffee-table.png" mà bạn vừa tải vào folder images
                // Dùng getScaledInstance trên 1 dòng cực gọn để đảm bảo lọt vừa khung 120x120 (tránh vỡ layout nếu ảnh gốc hơi to)
                tableIconCache = new ImageIcon(new ImageIcon("images/coffee-table.png").getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH));
            } catch (Exception e) {
                System.out.println("Không tìm thấy icon bàn: " + e.getMessage());
            }
        }
        return tableIconCache;
    }

    // Hàm cực xịn: Tự động đổi màu (tint) icon đen thành màu bất kỳ mà không làm mất độ trong suốt
    private ImageIcon getColorizedIcon(ImageIcon sourceIcon, Color color) {
        if (sourceIcon == null) return null;
        java.awt.image.ImageFilter filter = new java.awt.image.RGBImageFilter() {
            public int filterRGB(int x, int y, int argb) {
                int a = (argb >> 24) & 0xFF;
                if (a == 0) return argb; // Bỏ qua pixel trong suốt
                
                int origR = (argb >> 16) & 0xFF;
                int origG = (argb >> 8) & 0xFF;
                int origB = argb & 0xFF;
                
                // Tính độ sáng của pixel gốc
                int lum = (origR + origG + origB) / 3;
                
                // Áp dụng màu mới: Nét màu đen sẽ thành màu mới, nền trắng/trong suốt giữ nguyên
                int newR = (int)(color.getRed() * (255 - lum) / 255f + lum);
                int newG = (int)(color.getGreen() * (255 - lum) / 255f + lum);
                int newB = (int)(color.getBlue() * (255 - lum) / 255f + lum);
                
                return (a << 24) | (newR << 16) | (newG << 8) | newB;
            }
        };
        java.awt.image.ImageProducer producer = new java.awt.image.FilteredImageSource(sourceIcon.getImage().getSource(), filter);
        Image coloredImage = java.awt.Toolkit.getDefaultToolkit().createImage(producer);
        return new ImageIcon(coloredImage);
    }

    public PanelQuanLyPhongBan() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- TITLE BAR ---
        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        pnlTitle.setBackground(new Color(111, 78, 55)); // Màu Nâu Cà phê
        JLabel lblTitle = new JLabel("Danh Sách Bàn");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        pnlTitle.add(lblTitle);
        add(pnlTitle, BorderLayout.NORTH);

        // --- MAIN CONTENT ---
        JPanel pnlContent = new JPanel(new BorderLayout(10, 10));
        pnlContent.setBackground(new Color(235, 235, 235));
        pnlContent.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(pnlContent, BorderLayout.CENTER);

        // --- LEFT PANEL (Clock + Action Buttons) ---
        JPanel pnlLeft = new JPanel(new BorderLayout(0, 10));
        pnlLeft.setPreferredSize(new Dimension(200, 0));
        pnlLeft.setOpaque(false);

        // Clock Panel
        JPanel pnlClock = new JPanel(new GridLayout(2, 1));
        pnlClock.setBackground(Color.WHITE);
        pnlClock.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            new EmptyBorder(10, 10, 10, 10)
        ));
        JLabel lblTime = new JLabel("00:00:00", JLabel.CENTER);
        lblTime.setFont(new Font("Segoe UI", Font.BOLD, 28));
        JLabel lblDate = new JLabel("01/01/2021", JLabel.CENTER);
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        pnlClock.add(lblTime);
        pnlClock.add(lblDate);
        pnlLeft.add(pnlClock, BorderLayout.NORTH);

        Timer timer = new Timer(1000, e -> {
            lblTime.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));
            lblDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        });
        timer.start();

        // Left Action Buttons
        JPanel pnlLeftBtns = new JPanel(new GridLayout(8, 1, 0, 20)); // 8 nút
        pnlLeftBtns.setOpaque(false);
        Color btnGreen = new Color(34, 139, 34); // Forest Green
        Color btnGray = new Color(128, 128, 128); // Xám đậm
        
        JButton btnDatTruoc = createLeftBtn("Đặt bàn trước (F4)", btnGreen);
        JButton btnNhanBan = createLeftBtn("Nhận bàn (F5)", btnGreen);
        JButton btnHuyDat = createLeftBtn("Huỷ đặt bàn (F6)", btnGreen);
        JButton btnXemCT = createLeftBtn("Xem chi tiết (F7)", btnGreen);
        JButton btnChuyen = createLeftBtn("Chuyển bàn (F8)", btnGray);
        JButton btnGoiMon = createLeftBtn("Gọi món (F9)", btnGray);
        JButton btnDonBan = createLeftBtn("Dọn bàn (F10)", btnGray);
        JButton btnDanhSach = createLeftBtn("Danh sách đặt (F11)", btnGray);

        btnDanhSach.addActionListener(e -> {
            Window owner = SwingUtilities.getWindowAncestor(this);
            DialogDanhSachDatBan dialog = new DialogDanhSachDatBan(owner, this);
            dialog.setVisible(true);
        });

        btnDatTruoc.addActionListener(e -> {
            Window owner = SwingUtilities.getWindowAncestor(this);
            String tId = "001"; String st = "Trống"; String cap = "2 người"; String kv = "Tầng 1";
            if (selectedTableCard != null) {
                tId = (String) selectedTableCard.getClientProperty("tableId");
                int status = (int) selectedTableCard.getClientProperty("status");
                st = status == 0 ? "Trống" : (status == 1 ? "Đã đặt" : "Đang dùng");
                cap = selectedTableCard.getClientProperty("capacity") + " người";
            }
            DialogDatBan dialog = new DialogDatBan(owner, tId, st, cap, kv);
            dialog.setVisible(true);
            loadTables("Tất cả", "Tất cả", "Tất cả", "");
        });

        btnNhanBan.addActionListener(e -> {
            if (selectedTableCard == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn đã đặt để nhận!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int status = (int) selectedTableCard.getClientProperty("status");
            if (status != 1) {
                JOptionPane.showMessageDialog(this, "Chỉ có thể nhận bàn khi trạng thái đang là 'Đã đặt'!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String currentId = (String) selectedTableCard.getClientProperty("tableId");
            // Gọi món sẽ tự tạo hóa đơn và chuyển bàn sang Đang dùng
            Window owner = SwingUtilities.getWindowAncestor(this);
            DialogGoiMon dialog = new DialogGoiMon(owner, currentId);
            dialog.setVisible(true);
            loadTables("Tất cả", "Tất cả", "Tất cả", "");
        });

        btnHuyDat.addActionListener(e -> {
            if (selectedTableCard == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn đã đặt để hủy!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int status = (int) selectedTableCard.getClientProperty("status");
            if (status != 1) {
                JOptionPane.showMessageDialog(this, "Chỉ có thể hủy bàn khi trạng thái đang là 'Đã đặt'!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int opt = JOptionPane.showConfirmDialog(this, "Xác nhận Hủy đặt bàn cho Bàn " + selectedTableCard.getClientProperty("tableId") + "?", "Hủy đặt bàn", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                int maBan = Integer.parseInt((String) selectedTableCard.getClientProperty("tableId"));
                new dao.BanDAO().capNhatTrangThaiBan(maBan, "Trống");
                loadTables("Tất cả", "Tất cả", "Tất cả", "");
            }
        });
        btnXemCT.addActionListener(e -> {
            if (selectedTableCard == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn cần xem!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int status = (int) selectedTableCard.getClientProperty("status");
            if (status == 0) {
                JOptionPane.showMessageDialog(this, "Bàn này đang trống, không có hóa đơn để xem!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String currentId = (String) selectedTableCard.getClientProperty("tableId");
            Window owner = SwingUtilities.getWindowAncestor(this);
            DialogGoiMon dialog = new DialogGoiMon(owner, currentId);
            dialog.setVisible(true);
            loadTables("Tất cả", "Tất cả", "Tất cả", ""); // Refresh
        });
        btnChuyen.addActionListener(e -> {
            if (selectedTableCard == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn cần chuyển từ danh sách!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int status = (int) selectedTableCard.getClientProperty("status");
            if (status == 0) {
                JOptionPane.showMessageDialog(this, "Bàn này đang trống, không có khách để chuyển!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String currentId = (String) selectedTableCard.getClientProperty("tableId");
            int capacity = (int) selectedTableCard.getClientProperty("capacity");
            
            Window owner = SwingUtilities.getWindowAncestor(this);
            DialogChuyenBan dialog = new DialogChuyenBan(owner, currentId, capacity);
            dialog.setVisible(true);
            loadTables("Tất cả", "Tất cả", "Tất cả", ""); // Refresh
        });
        btnGoiMon.addActionListener(e -> {
            if (selectedTableCard == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn cần gọi món!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String currentId = (String) selectedTableCard.getClientProperty("tableId");
            Window owner = SwingUtilities.getWindowAncestor(this);
            DialogGoiMon dialog = new DialogGoiMon(owner, currentId);
            dialog.setVisible(true);
            loadTables("Tất cả", "Tất cả", "Tất cả", ""); // Refresh
        });
        
        btnDonBan.addActionListener(e -> {
            if (selectedTableCard == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn cần dọn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int status = (int) selectedTableCard.getClientProperty("status");
            if (status == 0) {
                JOptionPane.showMessageDialog(this, "Bàn này đã trống, không cần dọn!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String currentId = (String) selectedTableCard.getClientProperty("tableId");
            int maBan = Integer.parseInt(currentId);
            dao.HoaDonDAO hdDAO = new dao.HoaDonDAO();
            
            // Kiểm tra xem bàn này còn hóa đơn nào chưa thanh toán không
            if (hdDAO.getHoaDonHienTai(maBan) != -1) {
                JOptionPane.showMessageDialog(this, "Bàn này vẫn còn hóa đơn chưa thanh toán!\nVui lòng vào 'Gọi món' và Thanh toán trước khi dọn bàn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int opt = JOptionPane.showConfirmDialog(this, "Xác nhận dọn dẹp Bàn " + currentId + " và chuyển về trạng thái Trống?", "Dọn bàn", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                if (hdDAO.donBan(maBan)) {
                    JOptionPane.showMessageDialog(this, "Đã dọn bàn thành công!");
                    loadTables("Tất cả", "Tất cả", "Tất cả", ""); // Refresh
                }
            }
        });

        pnlLeftBtns.add(btnDatTruoc);
        pnlLeftBtns.add(btnNhanBan);
        pnlLeftBtns.add(btnHuyDat);
        pnlLeftBtns.add(btnXemCT);
        pnlLeftBtns.add(btnChuyen);
        pnlLeftBtns.add(btnGoiMon);
        pnlLeftBtns.add(btnDonBan);
        pnlLeftBtns.add(btnDanhSach);
        
        pnlLeft.add(pnlLeftBtns, BorderLayout.CENTER);

        pnlContent.add(pnlLeft, BorderLayout.WEST);

        // --- KEY BINDINGS ---
        InputMap im = pnlLeftBtns.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = pnlLeftBtns.getActionMap();

        im.put(KeyStroke.getKeyStroke("F4"), "F4");
        am.put("F4", new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) { btnDatTruoc.doClick(); }});
        
        im.put(KeyStroke.getKeyStroke("F5"), "F5");
        am.put("F5", new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) { btnNhanBan.doClick(); }});
        
        im.put(KeyStroke.getKeyStroke("F6"), "F6");
        am.put("F6", new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) { btnHuyDat.doClick(); }});
        
        im.put(KeyStroke.getKeyStroke("F7"), "F7");
        am.put("F7", new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) { btnXemCT.doClick(); }});
        
        im.put(KeyStroke.getKeyStroke("F8"), "F8");
        am.put("F8", new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) { btnChuyen.doClick(); }});
        
        im.put(KeyStroke.getKeyStroke("F9"), "F9");
        am.put("F9", new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) { btnGoiMon.doClick(); }});
        
        im.put(KeyStroke.getKeyStroke("F10"), "F10");
        am.put("F10", new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) { btnDonBan.doClick(); }});
        
        im.put(KeyStroke.getKeyStroke("F11"), "F11");
        am.put("F11", new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) { btnDanhSach.doClick(); }});

        // --- CENTER PANEL (Filters + Grid) ---
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setOpaque(false);

        // Filters Panel
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            new EmptyBorder(10, 10, 10, 10)
        ));

        Font lblFont = new Font("Segoe UI", Font.BOLD, 16);
        Font compFont = new Font("Segoe UI", Font.PLAIN, 16);

        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(lblFont);
        pnlFilter.add(lblTrangThai);
        JComboBox<String> cbTrangThai = new JComboBox<>(new String[]{"Tất cả", "Trống", "Đã đặt", "Đang dùng"});
        cbTrangThai.setFont(compFont);
        cbTrangThai.setPreferredSize(new Dimension(140, 35));
        pnlFilter.add(cbTrangThai);

        JLabel lblSoNguoi = new JLabel("Số người:");
        lblSoNguoi.setFont(lblFont);
        pnlFilter.add(lblSoNguoi);
        JComboBox<String> cbSoNguoi = new JComboBox<>(new String[]{"Tất cả", "2 người", "4 người", "6 người", "8 người", "10 người"});
        cbSoNguoi.setFont(compFont);
        cbSoNguoi.setPreferredSize(new Dimension(120, 35));
        pnlFilter.add(cbSoNguoi);

        JLabel lblKhuVuc = new JLabel("Khu vực:");
        lblKhuVuc.setFont(lblFont);
        pnlFilter.add(lblKhuVuc);
        JComboBox<String> cbKhuVuc = new JComboBox<>(new String[]{"Tất cả", "Tầng 1", "Tầng 2", "Sân vườn", "VIP"});
        cbKhuVuc.setFont(compFont);
        cbKhuVuc.setPreferredSize(new Dimension(120, 35));
        pnlFilter.add(cbKhuVuc);

        JLabel lblBanSo = new JLabel("Bàn số:");
        lblBanSo.setFont(lblFont);
        pnlFilter.add(lblBanSo);
        JTextField txtBanSo = new JTextField();
        txtBanSo.setFont(compFont);
        txtBanSo.setPreferredSize(new Dimension(100, 35));
        pnlFilter.add(txtBanSo);

        JButton btnTim = new JButton("Tìm") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnTim.setBackground(new Color(34, 139, 34)); // Forest Green
        btnTim.setForeground(Color.WHITE);
        btnTim.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnTim.setPreferredSize(new Dimension(100, 35));
        btnTim.setFocusPainted(false);
        btnTim.setContentAreaFilled(false);
        btnTim.setBorderPainted(false);
        btnTim.addActionListener(e -> {
            loadTables(cbTrangThai.getSelectedItem().toString(), cbSoNguoi.getSelectedItem().toString(), cbKhuVuc.getSelectedItem().toString(), txtBanSo.getText().trim());
        });
        btnTim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTim.setBackground(new Color(46, 184, 46));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTim.setBackground(new Color(34, 139, 34));
            }
        });
        
        JButton btnLamMoi = new JButton("Làm mới") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnLamMoi.setBackground(new Color(0, 153, 255)); // Blue
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLamMoi.setPreferredSize(new Dimension(120, 35));
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setContentAreaFilled(false);
        btnLamMoi.setBorderPainted(false);
        btnLamMoi.addActionListener(e -> {
            cbTrangThai.setSelectedIndex(0);
            cbSoNguoi.setSelectedIndex(0);
            cbKhuVuc.setSelectedIndex(0);
            txtBanSo.setText("");
            loadTables("Tất cả", "Tất cả", "Tất cả", "");
        });
        btnLamMoi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLamMoi.setBackground(new Color(51, 175, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLamMoi.setBackground(new Color(0, 153, 255));
            }
        });

        // Bắt sự kiện đổi lựa chọn tự động lọc
        cbTrangThai.addActionListener(e -> btnTim.doClick());
        cbSoNguoi.addActionListener(e -> btnTim.doClick());
        cbKhuVuc.addActionListener(e -> btnTim.doClick());
        txtBanSo.addActionListener(e -> btnTim.doClick());

        pnlFilter.add(btnTim);
        pnlFilter.add(btnLamMoi);

        pnlCenter.add(pnlFilter, BorderLayout.NORTH);

        // Tables Grid
        pnlTables = new JPanel(new GridLayout(0, 5, 10, 15));
        pnlTables.setBackground(Color.WHITE);
        pnlTables.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Khởi tạo bảng ban đầu
        loadTables("Tất cả", "Tất cả", "Tất cả", "");

        JPanel pnlWrapper = new JPanel(new BorderLayout());
        pnlWrapper.setBackground(Color.WHITE);
        pnlWrapper.add(pnlTables, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(pnlWrapper);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        pnlCenter.add(scrollPane, BorderLayout.CENTER);

        pnlContent.add(pnlCenter, BorderLayout.CENTER);

        // --- SOUTH LEGEND ---
        // (Đã bị xoá theo yêu cầu để thay bằng text trong từng thẻ bàn)
    }

    private JButton createLeftBtn(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);

        // Hiệu ứng Hover đổi màu và đổi con trỏ chuột
        Color hoverColor = bg.brighter();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hoverColor);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bg);
                btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return btn;
    }



    private JPanel createLegendItem(String text, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setOpaque(false);
        JLabel lblColor = new JLabel();
        lblColor.setOpaque(true);
        lblColor.setBackground(color);
        lblColor.setPreferredSize(new Dimension(20, 15));
        lblColor.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        p.add(lblColor);
        p.add(new JLabel(text));
        return p;
    }

    private JPanel createTableItem(String id, String capacity, int status, boolean isVip) {
        String statusStr = "Trống";
        Color statusColor = new Color(39, 174, 96); // Darker Green text
        
        if (status == 1) {
            statusStr = "Đã đặt";
            statusColor = new Color(211, 84, 0); // Orange/Yellowish text
        }
        else if (status == 2) {
            statusStr = "Đang dùng";
            statusColor = new Color(192, 57, 43); // Darker red text
        }

        // Tạo thẻ bao quanh (Card) bo góc và đổ bóng
        JPanel card = new JPanel(new BorderLayout(0, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int marginX = 10; // Giảm chiều rộng của thẻ
                int shadowSize = 4;
                int x = marginX;
                int y = shadowSize / 2;
                int w = getWidth() - marginX * 2 - shadowSize;
                int h = getHeight() - shadowSize * 2;

                boolean isHovered = Boolean.TRUE.equals(getClientProperty("isHovered"));
                boolean isSelected = Boolean.TRUE.equals(getClientProperty("isSelected"));

                // Đổ bóng (Shadow)
                if (isHovered && !isSelected) {
                    g2.setColor(new Color(0, 0, 0, 40)); // Đậm hơn khi hover
                    g2.fillRoundRect(x + 4, y + 4, w, h, 15, 15);
                } else {
                    g2.setColor(new Color(0, 0, 0, 20));
                    g2.fillRoundRect(x + 3, y + 3, w, h, 15, 15);
                    g2.setColor(new Color(0, 0, 0, 10));
                    g2.fillRoundRect(x + 1, y + 1, w, h, 15, 15);
                }

                // Nền trắng của thẻ
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(x, y, w, h, 15, 15);

                // Viền thẻ
                if (isSelected) {
                    g2.setColor(Color.BLACK); // Viền đen khi được chọn
                    g2.setStroke(new BasicStroke(2.5f));
                    g2.drawRoundRect(x, y, w, h, 15, 15);
                } else if (isHovered) {
                    g2.setColor(new Color(150, 150, 150)); // Viền xám khi hover
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(x, y, w, h, 15, 15);
                } else {
                    g2.setColor(new Color(225, 225, 225)); // Viền nhạt mặc định
                    g2.setStroke(new BasicStroke(1.0f));
                    g2.drawRoundRect(x, y, w, h, 15, 15);
                }
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(10, 15, 20, 15));

        // Sự kiện Hover và Select
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.putClientProperty("isHovered", true);
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                card.repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.putClientProperty("isHovered", false);
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                card.repaint();
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (selectedTableCard != null && selectedTableCard != card) {
                    selectedTableCard.putClientProperty("isSelected", false);
                    selectedTableCard.repaint();
                }
                selectedTableCard = card;
                card.putClientProperty("isSelected", true);
                card.repaint();
            }
        });

        // Bỏ viền và màu nền của ô ảnh (để trong suốt)
        JPanel imgBox = new JPanel(new BorderLayout());
        imgBox.setOpaque(false); 
        imgBox.setBorder(null);  
        imgBox.setPreferredSize(new Dimension(160, 160));
        imgBox.setMaximumSize(new Dimension(160, 160));
        
        // VIP Crown
        if (isVip) {
            JLabel lblVip = new JLabel("VIP", JLabel.CENTER);
            lblVip.setForeground(new Color(243, 156, 18));
            lblVip.setFont(new Font("Segoe UI", Font.BOLD, 16));
            imgBox.add(lblVip, BorderLayout.NORTH);
        }

        // Table image
        ImageIcon icon = getTableIcon();
        if (icon != null) {
            // Tự động tô màu icon cho trùng với màu của trạng thái (Xanh lá / Cam / Đỏ)
            ImageIcon coloredIcon = getColorizedIcon(icon, statusColor);
            JLabel lblImg = new JLabel(coloredIcon);
            lblImg.setHorizontalAlignment(JLabel.CENTER);
            imgBox.add(lblImg, BorderLayout.CENTER);
        } else {
            JLabel noImg = new JLabel("No Image", JLabel.CENTER);
            imgBox.add(noImg, BorderLayout.CENTER);
        }

        // Dùng GridBagLayout để căn giữa ô ảnh trong phần CENTER của card
        JPanel imgWrapper = new JPanel(new GridBagLayout());
        imgWrapper.setOpaque(false);
        imgWrapper.add(imgBox);
        card.add(imgWrapper, BorderLayout.CENTER);

        // Khung thông tin chữ
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel lblCap = new JLabel(capacity);
        lblCap.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCap.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(lblCap);

        JLabel lblId = new JLabel(id);
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblId.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(lblId);

        // Dòng trạng thái
        JPanel stPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        stPanel.setOpaque(false);
        JLabel lblStTitle = new JLabel("Trạng thái: ");
        lblStTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JLabel lblStVal = new JLabel(statusStr);
        lblStVal.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStVal.setForeground(statusColor);
        stPanel.add(lblStTitle);
        stPanel.add(lblStVal);
        
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(stPanel);

        card.add(infoPanel, BorderLayout.SOUTH);
        
        // Lưu ID và thông tin để tiện lấy khi click nút "Chuyển bàn"
        card.putClientProperty("tableId", id.replace("Bàn: ", "").trim());
        card.putClientProperty("status", status);
        try {
            card.putClientProperty("capacity", Integer.parseInt(capacity.replace(" người", "").trim()));
        } catch (Exception e) {
            card.putClientProperty("capacity", 0);
        }

        return card;
    }

    public void loadTables(String filterTrangThai, String filterSoNguoi, String filterKhuVuc, String filterBanSo) {
        pnlTables.removeAll();

        dao.BanDAO banDAO = new dao.BanDAO();
        java.util.ArrayList<entity.Ban> dsBan = banDAO.getAllBan();
        
        for(entity.Ban b : dsBan) {
            int soBan = b.getSoBan();
            String cap = b.getSucChua() + " người";
            String kv = b.getKhuVuc();
            String strStatus = b.getTrangThai();
            
            // 0: Trống, 1: Đã đặt, 2: Đang dùng
            int status = strStatus.equalsIgnoreCase("Trống") ? 0 : strStatus.equalsIgnoreCase("Đã đặt") ? 1 : 2;
            boolean vip = kv.equalsIgnoreCase("VIP"); 
            String idStr = String.format("%03d", soBan);

            // Kiểm tra bộ lọc
            boolean matchTrangThai = filterTrangThai.equals("Tất cả") || filterTrangThai.equals(strStatus);
            boolean matchSoNguoi = filterSoNguoi.equals("Tất cả") || filterSoNguoi.equals(cap);
            boolean matchKhuVuc  = filterKhuVuc.equals("Tất cả") || filterKhuVuc.equals(kv);
            boolean matchBanSo   = filterBanSo.isEmpty() || idStr.contains(filterBanSo) || String.valueOf(soBan).contains(filterBanSo);

            if (matchTrangThai && matchSoNguoi && matchKhuVuc && matchBanSo) {
                pnlTables.add(createTableItem("Bàn: " + idStr, cap, status, vip));
            }
        }
        
        pnlTables.revalidate();
        pnlTables.repaint();
    }
}
