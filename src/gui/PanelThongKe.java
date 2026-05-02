package gui;

import dao.DataManager;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class PanelThongKe extends JPanel {

    private static final Color BG_PAGE    = new Color(252, 248, 243);
    private static final Color CLR_BROWN  = new Color(101, 67,  33);
    private static final Color CLR_AMBER  = new Color(217, 119, 6);
    private static final Color CLR_GREEN  = new Color(22,  163, 74);
    private static final Color CLR_BLUE   = new Color(59,  130, 246);
    private static final Color CLR_GRAY   = new Color(107, 114, 128);
    private static final Color CLR_BORDER = new Color(229, 215, 195);
    private static final Color CLR_TEXT   = new Color(28,  20,  10);
    private static final Color CLR_WHITE  = Color.WHITE;
    private static final Color CLR_CREAM  = new Color(254, 243, 224);

    public enum LoaiThongKe { NGAY, THANG, NAM, KHACH_HANG }
    private LoaiThongKe loai;

    private int selectedNgay  = java.time.LocalDate.now().getDayOfMonth();
    private int selectedThang = java.time.LocalDate.now().getMonthValue();
    private int selectedNam   = java.time.LocalDate.now().getYear();

    private JPanel contentPanel;
    private JComboBox<String> cboNgay, cboThang, cboNam;

    public PanelThongKe(LoaiThongKe loai) {
        this.loai = loai;
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);
        add(buildHeader(), BorderLayout.NORTH);
        contentPanel = buildContent();
        add(contentPanel, BorderLayout.CENTER);
    }

    public static PanelThongKe theoNgay()  { return new PanelThongKe(LoaiThongKe.NGAY); }
    public static PanelThongKe theoThang() { return new PanelThongKe(LoaiThongKe.THANG); }
    public static PanelThongKe theoNam()   { return new PanelThongKe(LoaiThongKe.NAM); }
    public static PanelThongKe khachHang() { return new PanelThongKe(LoaiThongKe.KHACH_HANG); }

    public void refreshContent() {
        remove(contentPanel);
        contentPanel = buildContent();
        add(contentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JPanel buildHeader() {
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(CLR_WHITE);
        hdr.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, CLR_BORDER),
            new EmptyBorder(18, 28, 18, 28)));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        
        JLabel lblT = new JLabel("  " + getTieuDe());
        try {
            ImageIcon icon = new ImageIcon(getIcoPath());
            Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            lblT.setIcon(new ImageIcon(img));
        } catch(Exception e) {}
        
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblT.setForeground(CLR_BROWN);
        JLabel lblS = new JLabel(getMoTa());
        lblS.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblS.setForeground(CLR_GRAY);
        left.add(lblT);
        left.add(Box.createVerticalStrut(3));
        left.add(lblS);
        hdr.add(left, BorderLayout.WEST);

        if (loai != LoaiThongKe.KHACH_HANG) {
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            right.setOpaque(false);
            right.add(buildBoLoc());
            hdr.add(right, BorderLayout.EAST);
        }
        return hdr;
    }

    private JPanel buildBoLoc() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        p.setOpaque(false);

        String[] nams = {"2023", "2024", "2025", "2026"};
        cboNam = styledCombo(nams, 85);
        cboNam.setSelectedItem(String.valueOf(selectedNam));

        String[] thangs = new String[12];
        for(int i=1; i<=12; i++) thangs[i-1] = "Tháng " + i;
        cboThang = styledCombo(thangs, 100);
        cboThang.setSelectedIndex(selectedThang - 1);

        String[] ngays = new String[31];
        for(int i=1; i<=31; i++) ngays[i-1] = "Ngày " + i;
        cboNgay = styledCombo(ngays, 90);
        cboNgay.setSelectedIndex(selectedNgay - 1);

        if (loai == LoaiThongKe.NGAY) { p.add(new JLabel("Ngày:")); p.add(cboNgay); }
        if (loai == LoaiThongKe.NGAY || loai == LoaiThongKe.THANG) { p.add(new JLabel("Tháng:")); p.add(cboThang); }
        p.add(new JLabel("Năm:"));
        p.add(cboNam);

        JButton btnLoc = new JButton(" Xem") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_BROWN);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        try {
            ImageIcon icon = new ImageIcon("images/icon_timkiem.png");
            Image img = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            btnLoc.setIcon(new ImageIcon(img));
        } catch(Exception e) {}
        btnLoc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLoc.setForeground(CLR_WHITE);
        btnLoc.setPreferredSize(new Dimension(85, 36));
        btnLoc.setContentAreaFilled(false);
        btnLoc.setBorderPainted(false);
        btnLoc.setFocusPainted(false);
        btnLoc.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLoc.addActionListener(e -> {
            selectedNam   = Integer.parseInt(cboNam.getSelectedItem().toString());
            selectedThang = cboThang.getSelectedIndex() + 1;
            selectedNgay  = cboNgay.getSelectedIndex() + 1;
            refreshContent();
        });
        p.add(Box.createHorizontalStrut(5));
        p.add(btnLoc);
        return p;
    }

    private JPanel buildContent() {
        if (loai == LoaiThongKe.KHACH_HANG) return buildKhachHangContent();

        JPanel main = new JPanel(new BorderLayout(0, 25));
        main.setBackground(BG_PAGE);
        main.setBorder(new EmptyBorder(22, 28, 22, 28));

        main.add(buildStatCardsDT(), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 25, 0));
        center.setOpaque(false);
        center.add(buildBieuDoPanel());
        center.add(buildBangPanel());
        main.add(center, BorderLayout.CENTER);

        return main;
    }

    private JPanel buildStatCardsDT() {
        JPanel row = new JPanel(new GridLayout(1, 4, 18, 0));
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 85));

        String[][] data = getDTStatData();
        Color[] colors = {CLR_GREEN, CLR_AMBER, CLR_BLUE, CLR_BROWN};
        Color[] bgs    = {new Color(220, 252, 231), new Color(254, 243, 199), 
                          new Color(219, 234, 254), new Color(253, 243, 221)};

        for (int i = 0; i < data.length; i++) {
            row.add(statCard(data[i][0], data[i][1], data[i][2], colors[i], bgs[i]));
        }
        return row;
    }

    private String[][] getDTStatData() {
        switch (loai) {
            case NGAY: {
                java.util.Map<String, String> m = 
                    DataManager.getThongKeNgay(selectedNgay, selectedThang, selectedNam);
                return new String[][]{
                    {"💰", "Doanh Thu Ngày "  + selectedNgay, m.getOrDefault("doanhThu",  "0đ")},
                    {"🧾", "Số Hóa Đơn",                      m.getOrDefault("soHoaDon",  "0")},
                    {"👤", "Tổng Khách Hàng",                  m.getOrDefault("khachHang", "0")},
                    {"📅", "Lọc Theo Ngày",                   
                        selectedNgay + "/" + selectedThang + "/" + selectedNam},
                };
            }
            case THANG: {
                java.util.Map<String, String> m = 
                    DataManager.getThongKeThang(selectedThang, selectedNam);
                return new String[][]{
                    {"💰", "Doanh Thu Tháng " + selectedThang, m.getOrDefault("doanhThu",    "0đ")},
                    {"🧾", "Tổng Hóa Đơn",                     m.getOrDefault("soHoaDon",    "0")},
                    {"🔄", "Đang Phục Vụ",                      m.getOrDefault("dangPhucVu",  "0")},
                    {"📆", "Năm",                               String.valueOf(selectedNam)},
                };
            }
            default: {   // NAM
                java.util.Map<String, String> m = 
                    DataManager.getThongKeNam(selectedNam);
                return new String[][]{
                    {"💰", "Doanh Thu Năm " + selectedNam, m.getOrDefault("doanhThu",  "0đ")},
                    {"🧾", "Tổng Hóa Đơn",                 m.getOrDefault("soHoaDon",  "0")},
                    {"👤", "Tổng Khách Hàng",               m.getOrDefault("khachHang", "0")},
                    {"🗓", "Năm",                           String.valueOf(selectedNam)},
                };
            }
        }
    }

    private JPanel buildBieuDoPanel() {
        JPanel wrap = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        wrap.setOpaque(false);

        JLabel lblT = new JLabel("  📊  " + getTenBieuDo());
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblT.setForeground(CLR_TEXT);
        lblT.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, CLR_BORDER),
            new EmptyBorder(12, 18, 12, 18)));
        wrap.add(lblT, BorderLayout.NORTH);

        JPanel chartArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawChart((Graphics2D) g);
            }
        };
        chartArea.setOpaque(false);
        chartArea.setBorder(new EmptyBorder(25, 45, 35, 25));
        wrap.add(chartArea, BorderLayout.CENTER);

        return wrap;
    }

    private void drawChart(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        long[] data = getChartRawData();
        String[] labels = getChartLabels();
        
        if (data == null || data.length == 0) return;

        int w = g.getClipBounds().width - 70;
        int h = g.getClipBounds().height - 60;
        int x0 = 45;
        int y0 = h + 25;

        double maxVal = 0;
        for (long v : data) maxVal = Math.max(maxVal, v);
        if (maxVal == 0) maxVal = 100000;
        maxVal = roundUpNice((long)maxVal);

        g.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g.setColor(new Color(240, 240, 240));
        for (int i = 0; i <= 5; i++) {
            int y = y0 - (i * h / 5);
            g.drawLine(x0, y, x0 + w, y);
            g.setColor(CLR_GRAY);
            g.drawString(String.format("%,.0f", i * maxVal / 5), 5, y + 4);
            g.setColor(new Color(240, 240, 240));
        }

        int barW = Math.min(35, w / data.length / 2);
        int gap = w / data.length;
        
        for (int i = 0; i < data.length; i++) {
            long val = data[i];
            int barH = (int) (val / maxVal * h);
            int x = x0 + i * gap + (gap - barW)/2;
            int y = y0 - barH;

            GradientPaint gp = new GradientPaint(x, y, CLR_AMBER, x, y0, new Color(251, 191, 36));
            g.setPaint(gp);
            g.fill(new RoundRectangle2D.Double(x, y, barW, barH, 6, 6));

            g.setColor(CLR_TEXT);
            String label = labels[i];
            FontMetrics fm = g.getFontMetrics();
            g.drawString(label, x + (barW - fm.stringWidth(label))/2, y0 + 18);
        }
    }

    private long[] getChartRawData() {
        switch (loai) {
            case NGAY:  return DataManager.getBieuDoNgay(selectedNgay, selectedThang, selectedNam);
            case THANG: return DataManager.getBieuDoThang(selectedNam);
            default:    return DataManager.getBieuDoNam();
        }
    }

    private String[] getChartLabels() {
        switch (loai) {
            case NGAY: 
                return new String[]{"07h", "09h", "11h", "13h", "15h", "17h", "19h", "21h"};
            case THANG:
                return new String[]{"T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"};
            default:
                return new String[]{"2020", "2021", "2022", "2023", "2024", "2025", "2026"};
        }
    }

    private JPanel buildBangPanel() {
        JPanel wrap = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        wrap.setOpaque(false);

        JLabel lblT = new JLabel("  📋  " + getTenBang());
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblT.setForeground(CLR_TEXT);
        lblT.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, CLR_BORDER),
            new EmptyBorder(12, 18, 12, 18)));
        wrap.add(lblT, BorderLayout.NORTH);

        String[] cols = getTableCols();
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        for (Object[] row : getTableData()) tm.addRow(row);

        JTable tbl = buildStyledTable(tm);
        JScrollPane sc = new JScrollPane(tbl);
        sc.setBorder(new EmptyBorder(10, 15, 15, 15));
        sc.getViewport().setBackground(CLR_WHITE);
        wrap.add(sc, BorderLayout.CENTER);

        return wrap;
    }

    private JPanel buildKhachHangContent() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        p.setBackground(BG_PAGE);
        p.setBorder(new EmptyBorder(22, 28, 22, 28));

        p.add(buildKhachHangStats(), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 15)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        center.setOpaque(false);
        
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        searchBar.setOpaque(false);
        searchBar.setBorder(new MatteBorder(0, 0, 1, 0, CLR_BORDER));
        
        // Thêm Icon tìm kiếm bằng hình ảnh thay cho Emoji lỗi
        JLabel lblSearchIco = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("images/icon_thongke.png");
            Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            lblSearchIco.setIcon(new ImageIcon(img));
        } catch(Exception e) {}
        searchBar.add(lblSearchIco);

        JTextField txSearch = new JTextField(20);
        txSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txSearch.setBorder(new CompoundBorder(
            new LineBorder(CLR_BORDER, 1, true),
            new EmptyBorder(5, 10, 5, 10)));
        txSearch.setForeground(CLR_GRAY);

        final String PLACEHOLDER = "Tìm theo tên hoặc số điện thoại...";
        txSearch.setText(PLACEHOLDER);
        txSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txSearch.getText().equals(PLACEHOLDER)) {
                    txSearch.setText(""); txSearch.setForeground(CLR_TEXT);
                }
            }
            public void focusLost(FocusEvent e) {
                if (txSearch.getText().isEmpty()) {
                    txSearch.setText(PLACEHOLDER); txSearch.setForeground(CLR_GRAY);
                }
            }
        });
        searchBar.add(txSearch);
        center.add(searchBar, BorderLayout.NORTH);

        String[] cols = {"STT", "Họ Tên", "SĐT", "Lần Mua", "Tổng Chi Tiêu", "Hạng"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        List<Object[]> allRows = DataManager.getTopKhachHang();
        for (Object[] r : allRows) tm.addRow(r);
        
        JTable tbl = buildStyledTable(tm);
        JScrollPane sc = new JScrollPane(tbl);
        sc.setBorder(new EmptyBorder(5, 15, 15, 15));
        sc.getViewport().setBackground(CLR_WHITE);
        center.add(sc, BorderLayout.CENTER);

        p.add(center, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildKhachHangStats() {
        JPanel row = new JPanel(new GridLayout(1, 4, 18, 0));
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 90));

        java.util.Map<String, String> m = DataManager.getThongKeKhachHang();
        row.add(statCard("👥", "Tổng Khách Hàng",   m.getOrDefault("tongKH",    "0"),  CLR_BLUE,  new Color(219, 234, 254)));
        row.add(statCard("⭐", "Khách Thân Thiết",   m.getOrDefault("thanThiet", "0"),  CLR_AMBER, new Color(254, 243, 199)));
        row.add(statCard("🆕", "Khách Mới (Tháng)",  m.getOrDefault("khachMoi",  "0"),  CLR_GREEN, new Color(220, 252, 231)));
        row.add(statCard("💰", "Chi Tiêu TB",         m.getOrDefault("chiTieuTB", "0đ"),CLR_BROWN, new Color(253, 243, 221)));
        return row;
    }

    private String[] getTableCols() {
        switch (loai) {
            case NGAY:  return new String[]{"Khung Giờ", "Số HĐ", "Hóa Đơn", "Doanh Thu"};
            case THANG: return new String[]{"Thời Gian", "Số HĐ", "Hóa Đơn", "Doanh Thu"};
            default:    return new String[]{"Năm", "Số HĐ", "Hóa Đơn", "Doanh Thu"};
        }
    }

    private List<Object[]> getTableData() {
        switch (loai) {
            case NGAY:  return DataManager.getBangNgay(selectedNgay, selectedThang, selectedNam);
            case THANG: return DataManager.getBangThang(selectedNam);
            default:    return DataManager.getBangNam();
        }
    }

    private String getTenBieuDo() {
        switch (loai) {
            case NGAY:  return "Biểu Đồ Doanh Thu Theo Giờ";
            case THANG: return "Biểu Đồ Doanh Thu Theo Tháng";
            default:    return "Doanh Thu Theo Các Năm (2020-2026)";
        }
    }

    private String getTenBang() {
        switch (loai) {
            case NGAY:  return "Chi Tiết Doanh Thu Trong Ngày";
            case THANG: return "Báo Cáo Doanh Thu Theo Tháng";
            default:    return "Tổng Kết Các Năm";
        }
    }

    private static long roundUpNice(long v) {
        if (v <= 0) return 100000;
        long magnitude = (long) Math.pow(10, (long) Math.floor(Math.log10(v)));
        long[] steps = {1, 2, 5, 10};
        for (long s : steps) {
            long candidate = s * magnitude;
            if (candidate >= v) return candidate;
        }
        return 10 * magnitude;
    }

    private String getIcoPath() {
        switch (loai) {
            case NGAY:       return "images/icon_calendar.png";
            case THANG:      return "images/icon_calendar.png";
            case NAM:        return "images/icon_thongke.png";
            default:         return "images/icon_khachhang.png";
        }
    }

    private String getIco() {
        switch (loai) {
            case NGAY:       return "📅";
            case THANG:      return "📆";
            case NAM:        return "🗓";
            default:         return "👥";
        }
    }
    private String getTieuDe() {
        switch (loai) {
            case NGAY:       return "Thống Kê Theo Ngày";
            case THANG:      return "Thống Kê Theo Tháng";
            case NAM:        return "Thống Kê Theo Năm";
            default:         return "Phân Tích Khách Hàng";
        }
    }
    private String getMoTa() {
        switch (loai) {
            case NGAY:       return "Doanh thu và hóa đơn theo từng khung giờ trong ngày.";
            case THANG:      return "Tổng hợp kết quả kinh doanh chi tiết trong tháng.";
            case NAM:        return "Cái nhìn tổng quát về tăng trưởng doanh thu theo năm.";
            default:         return "Báo cáo thói quen mua sắm và xếp hạng khách hàng.";
        }
    }

    private JTable buildStyledTable(DefaultTableModel tm) {
        JTable tbl = new JTable(tm);
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tbl.setRowHeight(40);
        tbl.setShowHorizontalLines(true);
        tbl.setShowVerticalLines(false);
        tbl.setGridColor(new Color(243, 234, 220));
        tbl.setBackground(CLR_WHITE);
        tbl.setSelectionBackground(CLR_CREAM);
        tbl.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader th = tbl.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 12));
        th.setBackground(new Color(250, 244, 234));
        th.setForeground(CLR_GRAY);
        th.setBorder(new MatteBorder(0, 0, 1, 0, CLR_BORDER));
        th.setPreferredSize(new Dimension(0, 38));
        th.setReorderingAllowed(false);
        return tbl;
    }

    private JPanel statCard(String ico, String label, String val, Color accent, Color bg) {
        JPanel card = new JPanel(new BorderLayout(10, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel lblIco = new JLabel(ico, SwingConstants.CENTER);
        lblIco.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        lblIco.setOpaque(true);
        lblIco.setBackground(bg);
        lblIco.setPreferredSize(new Dimension(52, 52));

        JPanel txt = new JPanel();
        txt.setOpaque(false);
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));

        JLabel lblVal = new JLabel(val);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblVal.setForeground(accent);

        JLabel lblLbl = new JLabel(label);
        lblLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLbl.setForeground(CLR_GRAY);

        txt.add(lblVal);
        txt.add(lblLbl);
        card.add(lblIco, BorderLayout.WEST);
        card.add(txt,    BorderLayout.CENTER);
        return card;
    }

    private JComboBox<String> styledCombo(String[] items, int width) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setPreferredSize(new Dimension(width, 32));
        c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        c.setBackground(CLR_WHITE);
        return c;
    }
}