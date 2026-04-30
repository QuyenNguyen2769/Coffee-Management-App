package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

public class PanelThongKe extends JPanel {

    // ── Màu sắc ──────────────────────────────────────
    private static final Color BG_PAGE    = new Color(252, 248, 243);
    private static final Color CLR_BROWN  = new Color(101, 67,  33);
    private static final Color CLR_AMBER  = new Color(217, 119, 6);
    private static final Color CLR_CREAM  = new Color(254, 243, 224);
    private static final Color CLR_GREEN  = new Color(22,  163, 74);
    private static final Color CLR_RED    = new Color(220, 53,  69);
    private static final Color CLR_BLUE   = new Color(37,  99,  235);
    private static final Color CLR_GRAY   = new Color(107, 114, 128);
    private static final Color CLR_BORDER = new Color(229, 215, 195);
    private static final Color CLR_TEXT   = new Color(28,  20,  10);
    private static final Color CLR_WHITE  = Color.WHITE;

    public enum LoaiThongKe { NGAY, THANG, NAM, KHACH_HANG }

    private final LoaiThongKe loai;

    public PanelThongKe(LoaiThongKe loai) {
        this.loai = loai;
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);
        add(buildHeader(),  BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
    }

    // ── Tiện dụng ─────────────────────────────────────
    public static PanelThongKe theoNgay()      { return new PanelThongKe(LoaiThongKe.NGAY); }
    public static PanelThongKe theoThang()     { return new PanelThongKe(LoaiThongKe.THANG); }
    public static PanelThongKe theoNam()       { return new PanelThongKe(LoaiThongKe.NAM); }
    public static PanelThongKe khachHang()     { return new PanelThongKe(LoaiThongKe.KHACH_HANG); }

    // ═══════════════════════════════════════════════════
    //  HEADER
    // ═══════════════════════════════════════════════════
    private JPanel buildHeader() {
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(CLR_WHITE);
        hdr.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, CLR_BORDER),
            new EmptyBorder(18, 28, 18, 28)));

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

        // Bộ lọc thời gian bên phải
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

        if (loai == LoaiThongKe.NGAY) {
            String[] ngays = new String[30];
            for (int i = 0; i < 30; i++) ngays[i] = "Ngày " + (i + 1);
            p.add(styledCombo(ngays, 110));
            String[] thangList = new String[12];
            for (int i = 0; i < 12; i++) thangList[i] = "Tháng " + (i + 1);
            p.add(styledCombo(thangList, 110));
        } else if (loai == LoaiThongKe.THANG) {
            String[] thangList = new String[12];
            for (int i = 0; i < 12; i++) thangList[i] = "Tháng " + (i + 1);
            p.add(styledCombo(thangList, 110));
        }

        String[] namList = {"2024", "2025", "2026"};
        p.add(styledCombo(namList, 90));

        JButton btnLoc = new JButton("🔍 Xem") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? CLR_BROWN.darker() : CLR_BROWN);
                g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),8,8));
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
        p.add(btnLoc);

        return p;
    }

    // ═══════════════════════════════════════════════════
    //  NỘI DUNG CHÍNH
    // ═══════════════════════════════════════════════════
    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(BG_PAGE);
        content.setBorder(new EmptyBorder(20, 28, 20, 28));

        if (loai == LoaiThongKe.KHACH_HANG) {
            content.add(buildStatCardsKH(),  BorderLayout.NORTH);
            content.add(buildBangKhachHang(), BorderLayout.CENTER);
        } else {
            content.add(buildStatCardsDT(),  BorderLayout.NORTH);
            JPanel lower = new JPanel(new GridLayout(1, 2, 16, 0));
            lower.setOpaque(false);
            lower.add(buildBieuDo());
            lower.add(buildBangDoanhThu());
            content.add(lower, BorderLayout.CENTER);
        }

        return content;
    }

    // ═══════════════════════════════════════════════════
    //  THẺ THỐNG KÊ — DOANH THU
    // ═══════════════════════════════════════════════════
    private JPanel buildStatCardsDT() {
        JPanel row = new JPanel(new GridLayout(1, 4, 14, 0));
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 90));

        String[][] data = getDTStatData();
        Color[]  accents = {CLR_BROWN, CLR_GREEN, CLR_BLUE, CLR_RED};
        Color[]  bgs     = {
            new Color(253,243,221), new Color(220,252,231),
            new Color(219,234,254), new Color(254,226,226)
        };

        for (int i = 0; i < data.length; i++)
            row.add(statCard(data[i][0], data[i][1], data[i][2], accents[i], bgs[i]));

        return row;
    }

    private String[][] getDTStatData() {
        switch (loai) {
            case NGAY: return new String[][]{
                {"💰", "Doanh Thu Hôm Nay",  "2,450,000đ"},
                {"🧾", "Số Hóa Đơn",          "47"},
                {"👤", "Khách Hàng",           "38"},
                {"📉", "So Hôm Qua",           "-5%"},
            };
            case THANG: return new String[][]{
                {"💰", "Doanh Thu Tháng",     "68,200,000đ"},
                {"🧾", "Tổng Hóa Đơn",        "1,240"},
                {"👤", "Lượt Khách",           "980"},
                {"📈", "So Tháng Trước",       "+12%"},
            };
            default: return new String[][]{
                {"💰", "Doanh Thu Năm",       "820,000,000đ"},
                {"🧾", "Tổng Hóa Đơn",        "14,800"},
                {"👤", "Lượt Khách",           "11,200"},
                {"📈", "So Năm Trước",         "+18%"},
            };
        }
    }

    // ═══════════════════════════════════════════════════
    //  BIỂU ĐỒ CỘT (vẽ tay bằng Graphics2D)
    // ═══════════════════════════════════════════════════
    private JPanel buildBieuDo() {
        JPanel wrap = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),14,14));
                g2.dispose();
            }
        };
        wrap.setOpaque(false);

        // Tiêu đề
        JLabel lblT = new JLabel("  📊  " + getTenBieuDo());
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblT.setForeground(CLR_TEXT);
        lblT.setBorder(new CompoundBorder(
            new MatteBorder(0,0,1,0,CLR_BORDER),
            new EmptyBorder(14,18,14,18)));
        wrap.add(lblT, BorderLayout.NORTH);

        // Canvas vẽ biểu đồ
        JPanel canvas = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();
                int pad = 40, botPad = 50;
                int chartW = w - pad * 2;
                int chartH = h - pad - botPad;

                // Grid lines
                g2.setColor(new Color(243, 234, 220));
                g2.setStroke(new BasicStroke(1f));
                for (int i = 0; i <= 4; i++) {
                    int y = pad + (chartH / 4) * i;
                    g2.drawLine(pad, y, w - pad, y);
                }

                // Dữ liệu & nhãn
                String[] labels = getLabels();
                int[]    vals   = getValues();
                int n = labels.length;
                int barW = Math.min(40, chartW / n - 10);
                int maxV = 0;
                for (int v : vals) maxV = Math.max(maxV, v);

                for (int i = 0; i < n; i++) {
                    int x = pad + (int)((i + 0.5) * chartW / n) - barW / 2;
                    int barH = (int)((double) vals[i] / maxV * chartH);
                    int y = pad + chartH - barH;

                    // Cột gradient
                    GradientPaint gp = new GradientPaint(
                        x, y, CLR_AMBER,
                        x, y + barH, new Color(180, 90, 10));
                    g2.setPaint(gp);
                    g2.fill(new RoundRectangle2D.Double(x, y, barW, barH, 6, 6));

                    // Giá trị trên cột
                    g2.setColor(CLR_BROWN);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                    String valStr = formatVal(vals[i]);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(valStr, x + (barW - fm.stringWidth(valStr)) / 2, y - 4);

                    // Nhãn dưới
                    g2.setColor(CLR_GRAY);
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                    fm = g2.getFontMetrics();
                    g2.drawString(labels[i],
                        x + (barW - fm.stringWidth(labels[i])) / 2,
                        pad + chartH + 20);
                }

                // Trục X
                g2.setColor(CLR_BORDER);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawLine(pad, pad + chartH, w - pad, pad + chartH);

                g2.dispose();
            }
        };
        canvas.setOpaque(false);
        canvas.setBackground(CLR_WHITE);
        wrap.add(canvas, BorderLayout.CENTER);
        return wrap;
    }

    // ═══════════════════════════════════════════════════
    //  BẢNG DOANH THU CHI TIẾT
    // ═══════════════════════════════════════════════════
    private JPanel buildBangDoanhThu() {
        JPanel wrap = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),14,14));
                g2.dispose();
            }
        };
        wrap.setOpaque(false);

        JLabel lblT = new JLabel("  📋  " + getTenBang());
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblT.setForeground(CLR_TEXT);
        lblT.setBorder(new CompoundBorder(
            new MatteBorder(0,0,1,0,CLR_BORDER),
            new EmptyBorder(14,18,14,18)));
        wrap.add(lblT, BorderLayout.NORTH);

        String[] cols = getBangCols();
        Object[][] rows = getBangRows();

        DefaultTableModel tm = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Object[] row : rows) tm.addRow(row);

        JTable tbl = new JTable(tm);
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tbl.setRowHeight(40);
        tbl.setShowHorizontalLines(true);
        tbl.setShowVerticalLines(false);
        tbl.setGridColor(new Color(243, 234, 220));
        tbl.setBackground(CLR_WHITE);
        tbl.setSelectionBackground(CLR_CREAM);

        JTableHeader th = tbl.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 12));
        th.setBackground(new Color(250, 244, 234));
        th.setForeground(CLR_GRAY);
        th.setBorder(new MatteBorder(0,0,1,0,CLR_BORDER));
        th.setPreferredSize(new Dimension(0, 38));

        // Cột doanh thu: căn phải + màu nâu
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
                    return lbl;
                }
            });

        JScrollPane scroll = new JScrollPane(tbl);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CLR_WHITE);
        wrap.add(scroll, BorderLayout.CENTER);
        return wrap;
    }

    // ═══════════════════════════════════════════════════
    //  THỐNG KÊ KHÁCH HÀNG
    // ═══════════════════════════════════════════════════
    private JPanel buildStatCardsKH() {
        JPanel row = new JPanel(new GridLayout(1, 4, 14, 0));
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 90));

        row.add(statCard("👥", "Tổng Khách Hàng", "248",     CLR_BLUE,  new Color(219,234,254)));
        row.add(statCard("⭐", "Khách Thân Thiết","52",      CLR_AMBER, new Color(254,243,199)));
        row.add(statCard("🆕", "Khách Mới Tháng", "18",      CLR_GREEN, new Color(220,252,231)));
        row.add(statCard("💰", "Chi Tiêu TB",     "125,000đ",CLR_BROWN, new Color(253,243,221)));

        return row;
    }

    private JPanel buildBangKhachHang() {
        JPanel wrap = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),14,14));
                g2.dispose();
            }
        };
        wrap.setOpaque(false);

        // Header bảng
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setOpaque(false);
        hdr.setBorder(new CompoundBorder(
            new MatteBorder(0,0,1,0,CLR_BORDER),
            new EmptyBorder(12,18,12,18)));

        JLabel lblT = new JLabel("🏆  Top Khách Hàng Mua Nhiều Nhất");
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblT.setForeground(CLR_TEXT);

        JTextField txS = new JTextField(14);
        txS.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txS.setBorder(new CompoundBorder(
            new LineBorder(CLR_BORDER, 1, true),
            new EmptyBorder(5,10,5,10)));
        txS.setText("🔍  Tìm khách hàng...");
        txS.setForeground(CLR_GRAY);
        txS.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txS.getText().startsWith("🔍"))
                    { txS.setText(""); txS.setForeground(CLR_TEXT); }
            }
            public void focusLost(FocusEvent e) {
                if (txS.getText().isEmpty())
                    { txS.setText("🔍  Tìm khách hàng..."); txS.setForeground(CLR_GRAY); }
            }
        });

        hdr.add(lblT, BorderLayout.WEST);
        hdr.add(txS,  BorderLayout.EAST);
        wrap.add(hdr, BorderLayout.NORTH);

        // Bảng
        String[] cols = {"#", "Tên Khách Hàng", "Số Điện Thoại",
                          "Tổng Lần Mua", "Tổng Chi Tiêu", "Hạng"};
        Object[][] rows = {
            {1, "Ngô Bá Khá",    "0901 234 567", 48, "5,760,000đ", "Vàng"},
            {2, "Tuấn Khỉ",    "0912 345 678", 42, "4,830,000đ", "Vàng"},
            {3, "Lê Minh Châu",     "0923 456 789", 35, "3,920,000đ", "Bạc"},
            {4, "Phạm Thị Dung",    "0934 567 890", 30, "3,400,000đ", "Bạc"},
            {5, "Hoàng Văn Em",     "0945 678 901", 28, "2,980,000đ", "Bạc"},
            {6, "Đặng Thị Fương",   "0956 789 012", 22, "2,310,000đ", "Đồng"},
            {7, "Vũ Minh Giang",    "0967 890 123", 18, "1,890,000đ", "Đồng"},
            {8, "Bùi Thị Hoa",      "0978 901 234", 15, "1,620,000đ", "Đồng"},
        };

        DefaultTableModel tm = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Object[] row : rows) tm.addRow(row);

        JTable tbl = new JTable(tm);
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tbl.setRowHeight(46);
        tbl.setShowHorizontalLines(true);
        tbl.setShowVerticalLines(false);
        tbl.setGridColor(new Color(243, 234, 220));
        tbl.setBackground(CLR_WHITE);
        tbl.setSelectionBackground(CLR_CREAM);

        JTableHeader th = tbl.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(new Color(250, 244, 234));
        th.setForeground(CLR_GRAY);
        th.setBorder(new MatteBorder(0,0,1,0,CLR_BORDER));
        th.setPreferredSize(new Dimension(0, 42));

        int[] widths = {40, 180, 130, 110, 130, 90};
        for (int i = 0; i < widths.length; i++)
            tbl.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Cột # (thứ hạng có màu vàng cho top 3)
        tbl.getColumnModel().getColumn(0).setCellRenderer(
            new DefaultTableCellRenderer() {
                @Override public Component getTableCellRendererComponent(
                        JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                    JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                            t, val, sel, foc, r, c);
                    lbl.setHorizontalAlignment(SwingConstants.CENTER);
                    lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    int rank = Integer.parseInt(val.toString());
                    lbl.setForeground(rank == 1 ? new Color(202,138,4)
                                    : rank == 2 ? CLR_GRAY
                                    : rank == 3 ? new Color(180,120,60)
                                    :             CLR_TEXT);
                    lbl.setText(rank == 1 ? "🥇" : rank == 2 ? "🥈" : rank == 3 ? "🥉" : val.toString());
                    return lbl;
                }
            });

        // Cột Hạng (badge)
        tbl.getColumnModel().getColumn(5).setCellRenderer(
            new DefaultTableCellRenderer() {
                @Override public Component getTableCellRendererComponent(
                        JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                    JLabel lbl = new JLabel(val.toString(), SwingConstants.CENTER) {
                        @Override protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                RenderingHints.VALUE_ANTIALIAS_ON);
                            String s = getText();
                            Color bg = s.equals("Vàng")  ? new Color(254,243,199)
                                     : s.equals("Bạc")   ? new Color(241,245,249)
                                     :                     new Color(254,235,200);
                            Color fg = s.equals("Vàng")  ? new Color(161,98,7)
                                     : s.equals("Bạc")   ? CLR_GRAY
                                     :                     new Color(154,75,20);
                            g2.setColor(bg);
                            g2.fillRoundRect(2, 4, getWidth()-4, getHeight()-8, 20, 20);
                            g2.setColor(fg);
                            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                            FontMetrics fm = g2.getFontMetrics();
                            int x = (getWidth() - fm.stringWidth(s)) / 2;
                            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                            g2.drawString(s, x, y);
                            g2.dispose();
                        }
                    };
                    lbl.setOpaque(false);
                    return lbl;
                }
            });

        // Cột chi tiêu
        tbl.getColumnModel().getColumn(4).setCellRenderer(
            new DefaultTableCellRenderer() {
                @Override public Component getTableCellRendererComponent(
                        JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                    JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                            t, val, sel, foc, r, c);
                    lbl.setHorizontalAlignment(SwingConstants.RIGHT);
                    lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    lbl.setForeground(CLR_BROWN);
                    return lbl;
                }
            });

        JScrollPane scroll = new JScrollPane(tbl);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CLR_WHITE);
        wrap.add(scroll, BorderLayout.CENTER);
        return wrap;
    }

    // ═══════════════════════════════════════════════════
    //  DỮ LIỆU THEO LOẠI
    // ═══════════════════════════════════════════════════
    private String[] getLabels() {
        switch (loai) {
            case NGAY:  return new String[]{"7h","9h","11h","13h","15h","17h","19h","21h"};
            case THANG: return new String[]{"T1","T2","T3","T4","T5","T6","T7","T8","T9","T10","T11","T12"};
            default:    return new String[]{"2020","2021","2022","2023","2024","2025","2026"};
        }
    }

    private int[] getValues() {
        switch (loai) {
            case NGAY:  return new int[]{120,380,290,450,310,520,480,200};
            case THANG: return new int[]{52,48,65,70,58,80,75,68,90,85,72,95};
            default:    return new int[]{420,510,580,650,720,810,680};
        }
    }

    private String formatVal(int v) {
        if (loai == LoaiThongKe.NGAY) return v + "k";
        return v + "tr";
    }

    private String getTenBieuDo() {
        switch (loai) {
            case NGAY:  return "Doanh Thu Theo Giờ";
            case THANG: return "Doanh Thu Theo Tháng";
            default:    return "Doanh Thu Theo Năm";
        }
    }

    private String getTenBang() {
        switch (loai) {
            case NGAY:  return "Chi Tiết Theo Giờ";
            case THANG: return "Chi Tiết Theo Tháng";
            default:    return "Chi Tiết Theo Năm";
        }
    }

    private String[] getBangCols() {
        switch (loai) {
            case NGAY:  return new String[]{"Khung Giờ","Số Hóa Đơn","Lượt Khách","Doanh Thu"};
            case THANG: return new String[]{"Tháng","Số Hóa Đơn","Lượt Khách","Doanh Thu"};
            default:    return new String[]{"Năm","Số Hóa Đơn","Lượt Khách","Doanh Thu"};
        }
    }

    private Object[][] getBangRows() {
        switch (loai) {
            case NGAY: return new Object[][]{
                {"07:00 - 09:00","24","20","480,000đ"},
                {"09:00 - 11:00","38","35","760,000đ"},
                {"11:00 - 13:00","52","48","1,040,000đ"},
                {"13:00 - 15:00","41","38","820,000đ"},
                {"15:00 - 17:00","35","32","700,000đ"},
                {"17:00 - 19:00","48","45","960,000đ"},
                {"19:00 - 21:00","32","30","640,000đ"},
            };
            case THANG: return new Object[][]{
                {"Tháng 1","980","850","52,000,000đ"},
                {"Tháng 2","870","760","46,500,000đ"},
                {"Tháng 3","1,120","980","59,800,000đ"},
                {"Tháng 4","1,050","920","56,200,000đ"},
                {"Tháng 5","990","870","53,100,000đ"},
                {"Tháng 6","1,240","1,080","66,400,000đ"},
            };
            default: return new Object[][]{
                {"2021","10,200","8,900","510,000,000đ"},
                {"2022","11,600","10,100","580,000,000đ"},
                {"2023","13,000","11,400","650,000,000đ"},
                {"2024","14,400","12,600","720,000,000đ"},
                {"2025","16,200","14,100","810,000,000đ"},
                {"2026","13,600","11,800","680,000,000đ"},
            };
        }
    }

    // ═══════════════════════════════════════════════════
    //  HELPERS TEXT
    // ═══════════════════════════════════════════════════
    private String getIco() {
        switch (loai) {
            case NGAY:        return "📅";
            case THANG:       return "📆";
            case NAM:         return "🗓";
            default:          return "👥";
        }
    }

    private String getTieuDe() {
        switch (loai) {
            case NGAY:        return "Thống Kê Theo Ngày";
            case THANG:       return "Thống Kê Theo Tháng";
            case NAM:         return "Thống Kê Theo Năm";
            default:          return "Thống Kê Khách Hàng";
        }
    }

    private String getMoTa() {
        switch (loai) {
            case NGAY:        return "Xem doanh thu và số lượng hóa đơn trong ngày.";
            case THANG:       return "Tổng hợp doanh thu và khách hàng theo từng tháng.";
            case NAM:         return "Báo cáo tổng quan doanh thu hàng năm.";
            default:          return "Xem top khách hàng mua nhiều để tặng ưu đãi.";
        }
    }

    // ═══════════════════════════════════════════════════
    //  HELPERS UI
    // ═══════════════════════════════════════════════════
    private JPanel statCard(String ico, String label, String val,
                             Color accent, Color bg) {
        JPanel card = new JPanel(new BorderLayout(10, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),14,14));
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