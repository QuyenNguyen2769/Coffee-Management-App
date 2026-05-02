package gui;

import dao.DataManager;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class PanelThongKe extends JPanel {

    // ── Màu sắc ──────────────────────────────────────────────────
    private static final Color BG_PAGE    = new Color(252, 248, 243);
    private static final Color CLR_BROWN  = new Color(101,  67,  33);
    private static final Color CLR_AMBER  = new Color(217, 119,   6);
    private static final Color CLR_CREAM  = new Color(254, 243, 224);
    private static final Color CLR_GREEN  = new Color( 22, 163,  74);
    private static final Color CLR_RED    = new Color(220,  53,  69);
    private static final Color CLR_BLUE   = new Color( 37,  99, 235);
    private static final Color CLR_GRAY   = new Color(107, 114, 128);
    private static final Color CLR_BORDER = new Color(229, 215, 195);
    private static final Color CLR_TEXT   = new Color( 28,  20,  10);
    private static final Color CLR_WHITE  = Color.WHITE;

    public enum LoaiThongKe { NGAY, THANG, NAM, KHACH_HANG }

    // ── State ────────────────────────────────────────────────────
    private final LoaiThongKe loai;
    private int selectedNgay  = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH);
    private int selectedThang = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1;
    private int selectedNam   = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);

    /** Vùng nội dung chính — được rebuild mỗi lần nhấn "Xem" */
    private JPanel contentPanel;

    // ── Constructor ──────────────────────────────────────────────
    public PanelThongKe(LoaiThongKe loai) {
        this.loai = loai;
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);
        add(buildHeader(), BorderLayout.NORTH);
        contentPanel = buildContent();
        add(contentPanel, BorderLayout.CENTER);
    }

    // ── Factory methods ──────────────────────────────────────────
    public static PanelThongKe theoNgay()  { return new PanelThongKe(LoaiThongKe.NGAY); }
    public static PanelThongKe theoThang() { return new PanelThongKe(LoaiThongKe.THANG); }
    public static PanelThongKe theoNam()   { return new PanelThongKe(LoaiThongKe.NAM); }
    public static PanelThongKe khachHang() { return new PanelThongKe(LoaiThongKe.KHACH_HANG); }

    // ═════════════════════════════════════════════════════════════
    //  REFRESH toàn bộ nội dung (gọi sau khi thay đổi bộ lọc)
    // ═════════════════════════════════════════════════════════════
    private void refreshContent() {
        remove(contentPanel);
        contentPanel = buildContent();
        add(contentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // ═════════════════════════════════════════════════════════════
    //  HEADER + BỘ LỌC
    // ═════════════════════════════════════════════════════════════
    private JPanel buildHeader() {
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(CLR_WHITE);
        hdr.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, CLR_BORDER),
            new EmptyBorder(18, 28, 18, 28)));

        // Tiêu đề bên trái
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel lblT = new JLabel(getIco() + "  " + getTieuDe());
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblT.setForeground(CLR_BROWN);
        JLabel lblS = new JLabel(getMoTa());
        lblS.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblS.setForeground(CLR_GRAY);
        left.add(lblT);
        left.add(Box.createVerticalStrut(3));
        left.add(lblS);
        hdr.add(left, BorderLayout.WEST);

        // Bộ lọc bên phải (trừ tab khách hàng)
        if (loai != LoaiThongKe.KHACH_HANG) {
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            right.setOpaque(false);
            right.add(buildBoLoc());
            hdr.add(right, BorderLayout.EAST);
        }
        return hdr;
    }

    private JPanel buildBoLoc() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setOpaque(false);

        // ── Combo ngày ──
        JComboBox<String> cboNgay = null;
        if (loai == LoaiThongKe.NGAY) {
            String[] ngays = new String[31];
            for (int i = 0; i < 31; i++) ngays[i] = "Ngày " + (i + 1);
            cboNgay = styledCombo(ngays, 110);
            cboNgay.setSelectedIndex(selectedNgay - 1);
            p.add(cboNgay);
        }

        // ── Combo tháng ──
        JComboBox<String> cboThang = null;
        if (loai == LoaiThongKe.NGAY || loai == LoaiThongKe.THANG) {
            String[] thangList = new String[12];
            for (int i = 0; i < 12; i++) thangList[i] = "Tháng " + (i + 1);
            cboThang = styledCombo(thangList, 110);
            cboThang.setSelectedIndex(selectedThang - 1);
            p.add(cboThang);
        }

        // ── Combo năm ──
        String[] namList = {"2024", "2025", "2026"};
        JComboBox<String> cboNam = styledCombo(namList, 90);
        int namIdx = selectedNam - 2024;
        cboNam.setSelectedIndex(namIdx >= 0 && namIdx < namList.length ? namIdx : 2);
        p.add(cboNam);

        // ── Nút Xem ──
        JButton btnLoc = new JButton("🔍 Xem") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? CLR_BROWN.darker() : CLR_BROWN);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 8, 8));
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnLoc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLoc.setForeground(CLR_WHITE);
        btnLoc.setContentAreaFilled(false);
        btnLoc.setBorderPainted(false);
        btnLoc.setFocusPainted(false);
        btnLoc.setBorder(new EmptyBorder(8, 16, 8, 16));
        btnLoc.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Giữ reference final để dùng trong lambda
        final JComboBox<String> finalCboNgay   = cboNgay;
        final JComboBox<String> finalCboThang  = cboThang;
        final JComboBox<String> finalCboNam    = cboNam;
        final String[]          finalNamList   = namList;

        btnLoc.addActionListener(e -> {
            // Cập nhật state từ combo
            if (finalCboNgay != null)
                selectedNgay  = finalCboNgay.getSelectedIndex() + 1;
            if (finalCboThang != null)
                selectedThang = finalCboThang.getSelectedIndex() + 1;
            selectedNam = Integer.parseInt(finalNamList[finalCboNam.getSelectedIndex()]);

            // Rebuild toàn bộ nội dung với dữ liệu mới
            refreshContent();
        });

        p.add(btnLoc);
        return p;
    }

    // ═════════════════════════════════════════════════════════════
    //  NỘI DUNG CHÍNH
    // ═════════════════════════════════════════════════════════════
    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(BG_PAGE);
        content.setBorder(new EmptyBorder(20, 28, 20, 28));

        if (loai == LoaiThongKe.KHACH_HANG) {
            content.add(buildStatCardsKH(),   BorderLayout.NORTH);
            content.add(buildBangKhachHang(), BorderLayout.CENTER);
        } else {
            content.add(buildStatCardsDT(), BorderLayout.NORTH);
            JPanel chartRow = new JPanel(new GridLayout(1, 2, 16, 0));
            chartRow.setOpaque(false);
            chartRow.add(buildBieuDo());
            chartRow.add(buildBangDoanhThu());
            content.add(chartRow, BorderLayout.CENTER);
        }
        return content;
    }

    // ═════════════════════════════════════════════════════════════
    //  THẺ THỐNG KÊ — DOANH THU (lấy từ DB theo bộ lọc hiện tại)
    // ═════════════════════════════════════════════════════════════
    private JPanel buildStatCardsDT() {
        JPanel row = new JPanel(new GridLayout(1, 4, 14, 0));
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 90));

        String[][] data = getDTStatData();
        Color[]    accents = {CLR_BROWN, CLR_GREEN, CLR_BLUE, CLR_RED};
        Color[]    bgs     = {
            new Color(253, 243, 221), new Color(220, 252, 231),
            new Color(219, 234, 254), new Color(254, 226, 226)
        };
        for (int i = 0; i < data.length; i++)
            row.add(statCard(data[i][0], data[i][1], data[i][2], accents[i], bgs[i]));
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

    // ═════════════════════════════════════════════════════════════
    //  BIỂU ĐỒ CỘT — scale động theo dữ liệu thực (VND)
    // ═════════════════════════════════════════════════════════════
    private JPanel buildBieuDo() {
        // Lấy dữ liệu thực từ DB
        final long[] vals    = getValuesLong();
        final String[] labels = getLabels();

        JPanel wrap = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        wrap.setOpaque(false);

        // Tiêu đề biểu đồ
        JLabel lblT = new JLabel("  📊  " + getTenBieuDo());
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblT.setForeground(CLR_TEXT);
        lblT.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, CLR_BORDER),
            new EmptyBorder(14, 18, 14, 18)));
        wrap.add(lblT, BorderLayout.NORTH);

        // Canvas vẽ biểu đồ
        JPanel canvas = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();
                int padL = 55, padR = 20, padT = 35, padB = 50;
                int chartW = w - padL - padR;
                int chartH = h - padT - padB;
                if (chartW <= 0 || chartH <= 0) { g2.dispose(); return; }

                // Tìm max để scale
                long maxV = 1;
                for (long v : vals) if (v > maxV) maxV = v;
                // Làm tròn maxV lên mức đẹp
                maxV = roundUpNice(maxV);

                // ── Grid lines + nhãn trục Y ──
                int gridLines = 5;
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2.setStroke(new BasicStroke(1f));
                for (int i = 0; i <= gridLines; i++) {
                    int y = padT + (int)((double) i / gridLines * chartH);
                    g2.setColor(new Color(243, 234, 220));
                    g2.drawLine(padL, y, padL + chartW, y);

                    // Nhãn Y
                    long yVal = maxV - (long)((double) i / gridLines * maxV);
                    g2.setColor(CLR_GRAY);
                    String yStr = formatVnd(yVal);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(yStr, padL - fm.stringWidth(yStr) - 4,
                                  y + fm.getAscent() / 2 - 1);
                }

                // ── Trục X ──
                g2.setColor(CLR_BORDER);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawLine(padL, padT + chartH, padL + chartW, padT + chartH);

                // ── Các cột ──
                int n    = labels.length;
                int barW = Math.max(6, Math.min(40, chartW / n - 8));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 10));

                for (int i = 0; i < n; i++) {
                    int cx = padL + (int)((i + 0.5) * chartW / n);
                    int x  = cx - barW / 2;

                    int barH = (maxV == 0) ? 0
                             : (int)((double) vals[i] / maxV * chartH);
                    int y = padT + chartH - barH;

                    // Gradient cột
                    if (barH > 0) {
                        GradientPaint gp = new GradientPaint(
                            x, y, CLR_AMBER,
                            x, y + barH, new Color(180, 90, 10));
                        g2.setPaint(gp);
                        g2.fill(new RoundRectangle2D.Double(x, y, barW, barH, 5, 5));
                    }

                    // Giá trị trên cột
                    g2.setColor(CLR_BROWN);
                    String valStr = formatVnd(vals[i]);
                    FontMetrics fm = g2.getFontMetrics();
                    int tx = cx - fm.stringWidth(valStr) / 2;
                    if (barH > 0) g2.drawString(valStr, tx, y - 4);

                    // Nhãn dưới cột
                    g2.setColor(CLR_GRAY);
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    fm = g2.getFontMetrics();
                    g2.drawString(labels[i],
                        cx - fm.stringWidth(labels[i]) / 2,
                        padT + chartH + 18);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                }
                g2.dispose();
            }
        };
        canvas.setOpaque(false);
        canvas.setBackground(CLR_WHITE);
        wrap.add(canvas, BorderLayout.CENTER);
        return wrap;
    }

    // ═════════════════════════════════════════════════════════════
    //  BẢNG CHI TIẾT DOANH THU — lấy từ DB
    // ═════════════════════════════════════════════════════════════
    private JPanel buildBangDoanhThu() {
        JPanel wrap = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
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
            new EmptyBorder(14, 18, 14, 18)));
        wrap.add(lblT, BorderLayout.NORTH);

        String[]    cols = getBangCols();
        List<Object[]> rows = getBangRowsFromDB();

        DefaultTableModel tm = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Object[] row : rows) tm.addRow(row);

        JTable tbl = buildStyledTable(tm);

        // Cột Doanh Thu (cột cuối): căn phải + in đậm màu nâu
        int dtCol = cols.length - 1;
        tbl.getColumnModel().getColumn(dtCol).setCellRenderer(
            new DefaultTableCellRenderer() {
                @Override public Component getTableCellRendererComponent(
                        JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                    JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                            t, val, sel, foc, r, c);
                    lbl.setHorizontalAlignment(SwingConstants.RIGHT);
                    lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    lbl.setForeground(CLR_BROWN);
                    lbl.setBorder(new EmptyBorder(0, 0, 0, 10));
                    return lbl;
                }
            });

        JScrollPane scroll = new JScrollPane(tbl);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CLR_WHITE);
        wrap.add(scroll, BorderLayout.CENTER);
        return wrap;
    }

    // ═════════════════════════════════════════════════════════════
    //  THỐNG KÊ KHÁCH HÀNG
    // ═════════════════════════════════════════════════════════════
    private JPanel buildStatCardsKH() {
        JPanel row = new JPanel(new GridLayout(1, 4, 14, 0));
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 90));

        java.util.Map<String, String> m = DataManager.getThongKeKhachHang();
        row.add(statCard("👥", "Tổng Khách Hàng",   m.getOrDefault("tongKH",    "0"),  CLR_BLUE,  new Color(219, 234, 254)));
        row.add(statCard("⭐", "Khách Thân Thiết",   m.getOrDefault("thanThiet", "0"),  CLR_AMBER, new Color(254, 243, 199)));
        row.add(statCard("🆕", "Khách Mới (Tháng)",  m.getOrDefault("khachMoi",  "0"),  CLR_GREEN, new Color(220, 252, 231)));
        row.add(statCard("💰", "Chi Tiêu TB",         m.getOrDefault("chiTieuTB", "0đ"),CLR_BROWN, new Color(253, 243, 221)));
        return row;
    }

    private JPanel buildBangKhachHang() {
        JPanel wrap = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        wrap.setOpaque(false);

        // ── Header bảng + ô tìm kiếm ──
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);
        hdr.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, CLR_BORDER),
            new EmptyBorder(12, 18, 12, 18)));

        JLabel lblT = new JLabel("  🏆  Top Khách Hàng Mua Nhiều Nhất");
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblT.setForeground(CLR_TEXT);

        JTextField txSearch = new JTextField(16);
        txSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txSearch.setBorder(new CompoundBorder(
            new LineBorder(CLR_BORDER, 1, true),
            new EmptyBorder(5, 10, 5, 10)));
        txSearch.setForeground(CLR_GRAY);

        final String PLACEHOLDER = "🔍 Tìm theo tên / SĐT...";
        txSearch.setText(PLACEHOLDER);
        txSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txSearch.getText().equals(PLACEHOLDER)) {
                    txSearch.setText("");
                    txSearch.setForeground(CLR_TEXT);
                }
            }
            public void focusLost(FocusEvent e) {
                if (txSearch.getText().trim().isEmpty()) {
                    txSearch.setText(PLACEHOLDER);
                    txSearch.setForeground(CLR_GRAY);
                }
            }
        });

        hdr.add(lblT,     BorderLayout.WEST);
        hdr.add(txSearch, BorderLayout.EAST);
        wrap.add(hdr, BorderLayout.NORTH);

        // ── Load dữ liệu ──
        List<Object[]> allRows = DataManager.getTopKhachHang();

        // Fallback nếu DB trống hoàn toàn
        if (allRows.isEmpty()) {
            Object[][] fallback = {
                {1,"Nguyễn Thị Kim Loan","0901000008", 12,"6,000,000đ","Vàng"},
                {2,"Trần Hoàng Long",    "0901000009", 10,"5,000,000đ","Vàng"},
                {3,"Đặng Gia Huy",       "0901000007",  8,"3,250,000đ","Bạc"},
                {4,"Võ Thanh Hà",        "0901000006",  7,"2,500,000đ","Bạc"},
                {5,"Hoàng Ngọc Em",      "0901000005",  5,"750,000đ",  "Đồng"},
                {6,"Phạm Văn Dũng",      "0901000004",  4,"600,000đ",  "Đồng"},
                {7,"Lê Thị Cẩm",         "0901000003",  3,"450,000đ",  "Đồng"},
                {8,"Lý Văn Nam",         "0901000010",  2,"250,000đ",  "Đồng"},
            };
            java.util.Collections.addAll(allRows, fallback);
        }

        // ── Model & Table ──
        String[] cols = {"#", "Tên Khách Hàng", "Số Điện Thoại",
                         "Lần Mua", "Tổng Chi Tiêu", "Hạng"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Object[] row : allRows) tm.addRow(row);

        JTable tbl = buildStyledTable(tm);
        tbl.setRowHeight(46);

        // Độ rộng cột
        int[] widths = {40, 180, 130, 80, 140, 80};
        for (int i = 0; i < widths.length; i++)
            tbl.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Renderer cột # (top 3 có màu đặc biệt)
        tbl.getColumnModel().getColumn(0).setCellRenderer(
            new DefaultTableCellRenderer() {
                @Override public Component getTableCellRendererComponent(
                        JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                    JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                            t, val, sel, foc, r, c);
                    lbl.setHorizontalAlignment(SwingConstants.CENTER);
                    lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    int rank = Integer.parseInt(val.toString());
                    lbl.setForeground(rank == 1 ? new Color(202, 138, 4)
                                    : rank == 2 ? CLR_GRAY
                                    : rank == 3 ? new Color(180, 120, 60)
                                    :             CLR_TEXT);
                    return lbl;
                }
            });

        // Renderer cột Hạng (badge màu)
        tbl.getColumnModel().getColumn(5).setCellRenderer(
            new DefaultTableCellRenderer() {
                @Override public Component getTableCellRendererComponent(
                        JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                    final String s = val.toString();
                    JLabel lbl = new JLabel(s, SwingConstants.CENTER) {
                        @Override protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                RenderingHints.VALUE_ANTIALIAS_ON);
                            Color bg = s.equals("Vàng") ? new Color(254, 243, 199)
                                     : s.equals("Bạc")  ? new Color(241, 245, 249)
                                     :                    new Color(254, 235, 200);
                            Color fg = s.equals("Vàng") ? new Color(161, 98,   7)
                                     : s.equals("Bạc")  ? CLR_GRAY
                                     :                    new Color(154, 75,  20);
                            g2.setColor(bg);
                            g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 12, 18, 18);
                            g2.setColor(fg);
                            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                            FontMetrics fm = g2.getFontMetrics();
                            g2.drawString(s,
                                (getWidth()  - fm.stringWidth(s)) / 2,
                                (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                            g2.dispose();
                        }
                    };
                    lbl.setOpaque(false);
                    return lbl;
                }
            });

        // Renderer cột Chi Tiêu
        tbl.getColumnModel().getColumn(4).setCellRenderer(
            new DefaultTableCellRenderer() {
                @Override public Component getTableCellRendererComponent(
                        JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                    JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                            t, val, sel, foc, r, c);
                    lbl.setHorizontalAlignment(SwingConstants.RIGHT);
                    lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    lbl.setForeground(CLR_BROWN);
                    lbl.setBorder(new EmptyBorder(0, 0, 0, 8));
                    return lbl;
                }
            });

        // ── Tìm kiếm realtime ──
        final List<Object[]> finalAllRows = allRows;
        txSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }

            void filter() {
                String kw = txSearch.getText().trim().toLowerCase();
                tm.setRowCount(0);
                for (Object[] row : finalAllRows) {
                    // Bỏ qua placeholder
                    if (kw.isEmpty() || kw.equals(PLACEHOLDER.toLowerCase())) {
                        tm.addRow(row);
                        continue;
                    }
                    String ten = row[1].toString().toLowerCase();
                    String sdt = row[2].toString().toLowerCase();
                    if (ten.contains(kw) || sdt.contains(kw)) tm.addRow(row);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tbl);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CLR_WHITE);
        wrap.add(scroll, BorderLayout.CENTER);
        return wrap;
    }

    // ═════════════════════════════════════════════════════════════
    //  DATA HELPERS — lấy từ DB theo state hiện tại
    // ═════════════════════════════════════════════════════════════

    /** Giá trị biểu đồ (VND thực) theo bộ lọc hiện tại */
    private long[] getValuesLong() {
        switch (loai) {
            case NGAY:  return DataManager.getBieuDoNgay(selectedNgay, selectedThang, selectedNam);
            case THANG: return DataManager.getBieuDoThang(selectedNam);
            default:    return DataManager.getBieuDoNam();
        }
    }

    /** Nhãn trục X */
    private String[] getLabels() {
        switch (loai) {
            case NGAY:  return new String[]{"7h","9h","11h","13h","15h","17h","19h","21h"};
            case THANG: return new String[]{"T1","T2","T3","T4","T5","T6","T7","T8","T9","T10","T11","T12"};
            default:    return new String[]{"2020","2021","2022","2023","2024","2025","2026"};
        }
    }

    /** Dữ liệu bảng từ DB */
    private List<Object[]> getBangRowsFromDB() {
        switch (loai) {
            case NGAY:  return DataManager.getBangNgay(selectedNgay, selectedThang, selectedNam);
            case THANG: return DataManager.getBangThang(selectedNam);
            default:    return DataManager.getBangNam();
        }
    }

    // ═════════════════════════════════════════════════════════════
    //  FORMAT VND → rút gọn cho hiển thị trên biểu đồ
    // ═════════════════════════════════════════════════════════════
    private static String formatVnd(long v) {
        if (v == 0)           return "0";
        if (v >= 1_000_000_000) return String.format("%.1ftỷ", v / 1_000_000_000.0);
        if (v >= 1_000_000)   return (v / 1_000_000) + "tr";
        if (v >= 1_000)       return (v / 1_000) + "k";
        return v + "đ";
    }

    /** Làm tròn max lên mức "đẹp" để Y-axis trông gọn */
    private static long roundUpNice(long v) {
        if (v <= 0) return 1;
        long magnitude = (long) Math.pow(10, (long) Math.floor(Math.log10(v)));
        long[] steps = {1, 2, 5, 10};
        for (long s : steps) {
            long candidate = s * magnitude;
            if (candidate >= v) return candidate;
        }
        return 10 * magnitude;
    }

    // ═════════════════════════════════════════════════════════════
    //  TEXT HELPERS
    // ═════════════════════════════════════════════════════════════
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
            default:         return "Thống Kê Khách Hàng";
        }
    }
    private String getMoTa() {
        switch (loai) {
            case NGAY:       return "Doanh thu và hóa đơn theo từng khung giờ trong ngày.";
            case THANG:      return "Tổng hợp doanh thu và số hóa đơn theo từng tháng.";
            case NAM:        return "Báo cáo tổng quan doanh thu hàng năm.";
            default:         return "Top khách hàng chi tiêu nhiều nhất để tặng ưu đãi.";
        }
    }
    private String getTenBieuDo() {
        switch (loai) {
            case NGAY:       return "Doanh Thu Theo Giờ — Ngày " + selectedNgay + "/" + selectedThang + "/" + selectedNam;
            case THANG:      return "Doanh Thu 12 Tháng — Năm " + selectedNam;
            default:         return "Doanh Thu Theo Năm (2020 – 2026)";
        }
    }
    private String getTenBang() {
        switch (loai) {
            case NGAY:       return "Chi Tiết Theo Giờ";
            case THANG:      return "Chi Tiết Theo Tháng — Năm " + selectedNam;
            default:         return "Chi Tiết Theo Năm";
        }
    }
    private String[] getBangCols() {
        switch (loai) {
            case NGAY:  return new String[]{"Khung Giờ",  "Số Hóa Đơn", "Lượt Khách", "Doanh Thu"};
            case THANG: return new String[]{"Tháng",      "Số Hóa Đơn", "Lượt Khách", "Doanh Thu"};
            default:    return new String[]{"Năm",        "Số Hóa Đơn", "Lượt Khách", "Doanh Thu"};
        }
    }

    // ═════════════════════════════════════════════════════════════
    //  UI HELPERS
    // ═════════════════════════════════════════════════════════════
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

    private JPanel statCard(String ico, String label, String val,
                             Color accent, Color bg) {
        JPanel card = new JPanel(new BorderLayout(10, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
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
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setPreferredSize(new Dimension(width, 36));
        cb.setBorder(new LineBorder(CLR_BORDER, 1, true));
        return cb;
    }
}