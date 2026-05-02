package gui;

import dao.DataManager;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class PanelDichVu extends JPanel {

    // ── Bảng màu ─────────────────────────────────────────
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

    // ── Danh mục — phải khớp với giá trị cột danhMuc trong DB ──
    private static final String[] DANH_MUC = {
        "Tất Cả", "Cà Phê", "Trà", "Bánh & Ăn Vặt", "Nước Ép", "Khác"
    };

    private DefaultTableModel tableModel;
    private JTable            table;
    private JPanel            gridPanel;
    private boolean           xemDanhSach = true;
    private JButton           btnToggle;

    // ── Thẻ stat — lưu label để cập nhật từ DB ──
    private JLabel lblTongMon;
    private JLabel lblDangPhucVu;
    private JLabel lblBanChay;
    private JLabel lblGiaTB;

    public PanelDichVu() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);

        // Load dữ liệu từ DB
        refreshStatCards();
        themMauDuLieu();
    }

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

        JLabel lblTitle = new JLabel("Dịch Vụ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(CLR_BROWN);

        JLabel lblSub = new JLabel("Quản lý món ăn, đồ uống và giá tiền.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(CLR_GRAY);

        left.add(lblTitle);
        left.add(Box.createVerticalStrut(3));
        left.add(lblSub);
        hdr.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        btnToggle = roundBtn("Dạng Lưới", new Color(229, 231, 235), CLR_TEXT);
        btnToggle.addActionListener(e -> toggleView());

        JButton btnThem = roundBtn("+ Thêm Món", CLR_BROWN, CLR_WHITE);
        btnThem.addActionListener(e -> moForm(null, -1));

        right.add(btnToggle);
        right.add(btnThem);
        hdr.add(right, BorderLayout.EAST);

        return hdr;
    }

    // ═══════════════════════════════════════════════════
    //  NỘI DUNG
    // ═══════════════════════════════════════════════════
    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(BG_PAGE);
        content.setBorder(new EmptyBorder(20, 28, 20, 28));

        content.add(buildStatCards(), BorderLayout.NORTH);
        content.add(buildFilterBar(), BorderLayout.CENTER);
        return content;
    }

    // ── 4 thẻ thống kê — lấy từ DB ──────────────────
    private JPanel buildStatCards() {
        JPanel row = new JPanel(new GridLayout(1, 4, 14, 0));
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 90));

        // Tạo label có thể cập nhật sau
        lblTongMon    = valLabel("...", CLR_BROWN);
        lblDangPhucVu = valLabel("...", CLR_GREEN);
        lblBanChay    = valLabel("...", CLR_BLUE);
        lblGiaTB      = valLabel("...", CLR_AMBER);

        row.add(statCard("☕", "Tổng Món",       lblTongMon,    new Color(253, 243, 221)));
        row.add(statCard("✅", "Đang Phục Vụ",   lblDangPhucVu, new Color(220, 252, 231)));
        row.add(statCard("⭐", "Bán Chạy Nhất",  lblBanChay,    new Color(219, 234, 254)));
        row.add(statCard("💰", "Giá Trung Bình", lblGiaTB,      new Color(254, 243, 199)));

        return row;
    }

    /** Cập nhật thẻ thống kê từ DB (gọi sau khi thêm/sửa/xóa) */
    private void refreshStatCards() {
        SwingWorker<Map<String, String>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, String> doInBackground() {
                return DataManager.getThongKeDichVu();
            }
            @Override
            protected void done() {
                try {
                    Map<String, String> m = get();
                    if (lblTongMon    != null) lblTongMon   .setText(m.getOrDefault("tongMon",      "0"));
                    if (lblDangPhucVu != null) lblDangPhucVu.setText(m.getOrDefault("dangPhucVu",   "0"));
                    if (lblBanChay    != null) lblBanChay   .setText(m.getOrDefault("banChayNhat",  "N/A"));
                    if (lblGiaTB      != null) lblGiaTB     .setText(m.getOrDefault("giaTrungBinh", "0đ"));
                } catch (Exception ignored) {}
            }
        };
        worker.execute();
    }

    private JLabel valLabel(String text, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setForeground(color);
        return lbl;
    }

    private JPanel statCard(String ico, String label, JLabel valLabel, Color bg) {
        JPanel card = new JPanel(new BorderLayout(10, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0,
                        getWidth(), getHeight(), 14, 14));
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

        JLabel lblLbl = new JLabel(label);
        lblLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLbl.setForeground(CLR_GRAY);

        txt.add(valLabel);
        txt.add(lblLbl);

        card.add(lblIco, BorderLayout.WEST);
        card.add(txt,    BorderLayout.CENTER);
        return card;
    }

    // ── Filter + View ─────────────────────────────────
    private JPanel viewWrapper;
    private String selectedCategory = "Tất Cả";
    private List<JButton> filterButtons = new ArrayList<>();
    private JTextField txSearch;

    private JPanel buildFilterBar() {
        JPanel outer = new JPanel(new BorderLayout(0, 12));
        outer.setOpaque(false);

        // Thanh filter danh mục
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filterBar.setOpaque(false);

        for (String dm : DANH_MUC) {
            boolean isFirst = dm.equals("Tất Cả");
            JButton btnDM = new JButton(dm) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                        RenderingHints.VALUE_ANTIALIAS_ON);
                    boolean active = dm.equals(selectedCategory);
                    Color bgColor = active ? CLR_BROWN : CLR_WHITE;
                    g2.setColor(bgColor);
                    g2.fill(new RoundRectangle2D.Double(0, 0,
                            getWidth(), getHeight(), 20, 20));
                    if (!active) {
                        g2.setColor(CLR_BORDER);
                        g2.setStroke(new BasicStroke(1f));
                        g2.draw(new RoundRectangle2D.Double(0, 0,
                                getWidth()-1, getHeight()-1, 20, 20));
                    }
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            btnDM.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnDM.setForeground(isFirst ? CLR_WHITE : CLR_GRAY);
            btnDM.setContentAreaFilled(false);
            btnDM.setBorderPainted(false);
            btnDM.setFocusPainted(false);
            btnDM.setBorder(new EmptyBorder(6, 16, 6, 16));
            btnDM.setCursor(new Cursor(Cursor.HAND_CURSOR));

            filterButtons.add(btnDM);
            btnDM.addActionListener(e -> {
                selectedCategory = dm;
                // Cập nhật màu chữ tất cả nút
                for (JButton b : filterButtons) {
                    b.setForeground(b.getText().equals(selectedCategory) ? CLR_WHITE : CLR_GRAY);
                    b.repaint();
                }
                applyFilter();
            });

            filterBar.add(btnDM);
        }

        outer.add(filterBar, BorderLayout.NORTH);

        viewWrapper = new JPanel(new BorderLayout());
        viewWrapper.setOpaque(false);
        viewWrapper.add(buildTableView(), BorderLayout.CENTER);
        outer.add(viewWrapper, BorderLayout.CENTER);

        return outer;
    }

    // ── Bảng danh sách ───────────────────────────────
    private JPanel buildTableView() {
        JPanel wrap = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0,
                        getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        wrap.setOpaque(false);

        // Thanh trên bảng
        JPanel tblHdr = new JPanel(new BorderLayout());
        tblHdr.setOpaque(false);
        tblHdr.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, CLR_BORDER),
            new EmptyBorder(12, 18, 12, 18)));

        JLabel lblT = new JLabel("Danh Sách Món");
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblT.setForeground(CLR_TEXT);

        txSearch = new JTextField(14);
        txSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txSearch.setBorder(new CompoundBorder(
            new LineBorder(CLR_BORDER, 1, true),
            new EmptyBorder(5, 10, 5, 10)));
        txSearch.setText("🔍  Tìm tên món...");
        txSearch.setForeground(CLR_GRAY);
        txSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txSearch.getText().startsWith("🔍"))
                    { txSearch.setText(""); txSearch.setForeground(CLR_TEXT); }
            }
            public void focusLost(FocusEvent e) {
                if (txSearch.getText().isEmpty())
                    { txSearch.setText("🔍  Tìm tên món..."); txSearch.setForeground(CLR_GRAY); }
            }
        });
        txSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { applyFilter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { applyFilter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
        });

        tblHdr.add(lblT,     BorderLayout.WEST);
        tblHdr.add(txSearch, BorderLayout.EAST);
        wrap.add(tblHdr, BorderLayout.NORTH);

        // Bảng — 9 cột (thêm cột danhMuc ẩn ở index 7)
        // STT | Tên Món | Đơn Vị | Giá Bán | Mô Tả | Trạng Thái | maSP(ẩn) | danhMuc(ẩn) | Thao Tác
        String[] cols = {"STT","Tên Món","Đơn Vị","Giá Bán","Mô Tả","Trạng Thái","maSP","danhMuc","Thao Tác"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(48);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(243, 234, 220));
        table.setBackground(CLR_WHITE);
        table.setSelectionBackground(CLR_CREAM);
        table.setSelectionForeground(CLR_TEXT);

        // Ẩn cột maSP (6) và danhMuc (7)
        hideColumn(6);
        hideColumn(7);

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(new Color(250, 244, 234));
        th.setForeground(CLR_GRAY);
        th.setBorder(new MatteBorder(0, 0, 1, 0, CLR_BORDER));
        th.setPreferredSize(new Dimension(0, 42));

        // Chiều rộng cột hiển thị (0,1,2,3,4,5,8)
        int[] widths = {50, 200, 80, 100, 150, 110, 0, 0, 110};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Badge trạng thái (cột 5)
        table.getColumnModel().getColumn(5).setCellRenderer(
            new DefaultTableCellRenderer() {
                @Override public Component getTableCellRendererComponent(
                        JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                    JLabel lbl = new JLabel(val != null ? val.toString() : "",
                                            SwingConstants.CENTER) {
                        @Override protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                RenderingHints.VALUE_ANTIALIAS_ON);
                            String s = getText();
                            Color bg = s.contains("Phục") ?
                                        new Color(220, 252, 231) : new Color(254, 226, 226);
                            Color fg = s.contains("Phục") ? CLR_GREEN : CLR_RED;
                            g2.setColor(bg);
                            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                            g2.setColor(fg);
                            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                            FontMetrics fm = g2.getFontMetrics();
                            int x = (getWidth()  - fm.stringWidth(s)) / 2;
                            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                            g2.drawString(s, x, y);
                            g2.dispose();
                        }
                    };
                    lbl.setOpaque(false);
                    return lbl;
                }
            });

        // Cột thao tác (cột 8)
        table.getColumnModel().getColumn(8).setCellRenderer(
            (t, val, sel, foc, r, c) -> buildActPanel());

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row < 0 || col != 8) return;
                Rectangle rect = table.getCellRect(row, col, false);
                int rx = e.getX() - rect.x;
                if (rx < rect.width / 2) {
                    // Sửa — lấy đủ 9 cột kể cả ẩn
                    Object[] d = new Object[tableModel.getColumnCount()];
                    for (int i = 0; i < d.length; i++)
                        d[i] = tableModel.getValueAt(row, i);
                    moForm(d, row);
                } else {
                    xoaMon(row);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CLR_WHITE);
        wrap.add(scroll, BorderLayout.CENTER);
        return wrap;
    }

    /** Ẩn hoàn toàn một cột trong JTable */
    private void hideColumn(int colIndex) {
        if (table == null) return;
        TableColumn col = table.getColumnModel().getColumn(colIndex);
        col.setMinWidth(0); col.setMaxWidth(0); col.setWidth(0);
    }

    // ── Toggle view (bảng ↔ lưới) ────────────────────
    private void toggleView() {
        xemDanhSach = !xemDanhSach;
        viewWrapper.removeAll();
        if (xemDanhSach) {
            viewWrapper.add(buildTableView(), BorderLayout.CENTER);
            themMauDuLieu();
            btnToggle.setText("Dạng Lưới");
        } else {
            viewWrapper.add(buildGridView(), BorderLayout.CENTER);
            btnToggle.setText("Dạng Bảng");
        }
        viewWrapper.revalidate();
        viewWrapper.repaint();
    }

    // ── Dạng lưới card ───────────────────────────────
    private JScrollPane buildGridView() {
        JPanel grid = new JPanel(new WrapLayout(FlowLayout.LEFT, 14, 14));
        grid.setBackground(BG_PAGE);

        List<Object[]> ds = DataManager.getDsDichVu();
        for (Object[] row : ds) {
            String[] d = {
                str(row[1]),                             // tên
                str(row[7]),                             // danh mục
                String.format("%,.0fđ", toDouble(row[3])), // giá
                str(row[5]),                             // trạng thái
                ""                                       // ảnh (chưa có)
            };
            grid.add(buildMiniCard(d));
        }

        JScrollPane sp = new JScrollPane(grid);
        sp.setBorder(null);
        sp.getViewport().setBackground(BG_PAGE);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    private double toDouble(Object o) {
        if (o == null) return 0;
        try { return Double.parseDouble(o.toString().replaceAll("[^\\d.]", "")); }
        catch (Exception e) { return 0; }
    }

    private JPanel buildMiniCard(String[] d) {
        JPanel card = new JPanel(new BorderLayout(0, 6)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0,
                        getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(175, 240));
        card.setBorder(new EmptyBorder(10, 10, 10, 10));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblAnh = new JLabel();
        lblAnh.setPreferredSize(new Dimension(155, 120));
        lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
        lblAnh.setOpaque(true);
        lblAnh.setBackground(new Color(243, 234, 220));
        lblAnh.setText("☕");
        lblAnh.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));

        card.add(lblAnh, BorderLayout.NORTH);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel lblTen = new JLabel(d[0]);
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTen.setForeground(CLR_TEXT);

        JLabel lblGia = new JLabel(d[2]);
        lblGia.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblGia.setForeground(CLR_AMBER);

        JLabel lblTT = new JLabel(d[3]);
        lblTT.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTT.setForeground(d[3].contains("Phục") ? CLR_GREEN : CLR_RED);

        info.add(lblTen);
        info.add(Box.createVerticalStrut(3));
        info.add(lblGia);
        info.add(Box.createVerticalStrut(2));
        info.add(lblTT);
        card.add(info, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                card.setBorder(new CompoundBorder(
                    new LineBorder(CLR_BROWN, 2, true),
                    new EmptyBorder(8, 8, 8, 8)));
                card.repaint();
            }
            @Override public void mouseExited(MouseEvent e) {
                card.setBorder(new EmptyBorder(10, 10, 10, 10));
                card.repaint();
            }
        });

        return card;
    }

    // ═══════════════════════════════════════════════════
    //  FORM THÊM / SỬA
    // ═══════════════════════════════════════════════════
    private void moForm(Object[] data, int editRow) {
        boolean laSua = (editRow >= 0);
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dlg = new JDialog(
            owner instanceof Frame ? (Frame) owner : null,
            laSua ? "Sửa Món" : "Thêm Món Mới", true);
        dlg.setSize(460, 590);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        // Header
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(CLR_BROWN);
        hdr.setBorder(new EmptyBorder(16, 22, 16, 22));
        JLabel lblT = new JLabel(laSua ? "Sửa Món" : "Thêm Món Mới");
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblT.setForeground(CLR_WHITE);
        JButton btnX = xBtn(CLR_WHITE);
        btnX.addActionListener(e -> dlg.dispose());
        hdr.add(lblT, BorderLayout.WEST);
        hdr.add(btnX, BorderLayout.EAST);
        dlg.add(hdr, BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel();
        body.setBackground(CLR_WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(20, 24, 10, 24));

        // data indices: 0=STT, 1=tenMon, 2=donVi, 3=giaBan, 4=moTa, 5=trangThai, 6=maSP, 7=danhMuc
        JTextField txTen   = frmField(laSua ? str(data[1]) : "");
        JTextField txDonVi = frmField(laSua ? str(data[2]) : "ly");
        JTextField txGia   = frmField(laSua ? str(data[3]).replaceAll("[^\\d.]", "") : "");
        JTextField txMoTa  = frmField(laSua ? str(data[4]) : "");

        // Combo danh mục (bỏ "Tất Cả")
        String[] danhMucList = {"Cà Phê", "Trà", "Bánh & Ăn Vặt", "Nước Ép", "Khác"};
        JComboBox<String> cboDM = styledCombo(danhMucList);
        if (laSua) {
            String dm = str(data[7]);
            for (int i = 0; i < danhMucList.length; i++)
                if (danhMucList[i].equals(dm)) { cboDM.setSelectedIndex(i); break; }
        }

        String[] ttList = {"Đang Phục Vụ", "Tạm Ngưng"};
        JComboBox<String> cboTT = styledCombo(ttList);
        if (laSua && str(data[5]).contains("Ngưng")) cboTT.setSelectedIndex(1);

        body.add(frmRow("Tên Món *",     txTen));   body.add(Box.createVerticalStrut(10));
        body.add(frmRow("Đơn Vị *",      txDonVi)); body.add(Box.createVerticalStrut(10));
        body.add(frmRow("Giá Bán (đ) *", txGia));   body.add(Box.createVerticalStrut(10));
        body.add(frmRow("Mô Tả",         txMoTa));  body.add(Box.createVerticalStrut(10));
        body.add(frmLbl("Danh Mục"));                body.add(Box.createVerticalStrut(4));
        body.add(cboDM);                             body.add(Box.createVerticalStrut(10));
        body.add(frmLbl("Trạng Thái"));              body.add(Box.createVerticalStrut(4));
        body.add(cboTT);

        JScrollPane scrollBody = new JScrollPane(body);
        scrollBody.setBorder(null);
        dlg.add(scrollBody, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footer.setBackground(new Color(250, 244, 234));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, CLR_BORDER));

        JButton btnHuy = roundBtn("Hủy", new Color(229, 231, 235), new Color(55, 65, 81));
        JButton btnLuu = roundBtn(laSua ? "Lưu" : "Thêm Món", CLR_BROWN, CLR_WHITE);

        btnHuy.addActionListener(e -> dlg.dispose());
        btnLuu.addActionListener(e -> {
            String ten    = txTen.getText().trim();
            String donVi  = txDonVi.getText().trim();
            String giaStr = txGia.getText().trim()
                    .replace(",", "").replace("đ", "").replace(" ", "");
            String tt     = cboTT.getSelectedItem().toString();
            String moTa   = txMoTa.getText().trim();
            String danhMuc = cboDM.getSelectedItem().toString();

            // Validation
            if (ten.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Vui lòng nhập tên món!"); return;
            }
            if (donVi.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Vui lòng nhập đơn vị!"); return;
            }
            if (giaStr.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Vui lòng nhập giá bán!"); return;
            }

            double gia;
            try {
                gia = Double.parseDouble(giaStr);
                if (gia <= 0) {
                    JOptionPane.showMessageDialog(dlg, "Giá bán phải lớn hơn 0!"); return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg, "Giá không hợp lệ (chỉ nhập số)!"); return;
            }

            boolean ok;
            if (laSua) {
                int maSP = Integer.parseInt(str(data[6]));
                ok = DataManager.suaDichVu(maSP, ten, donVi, gia, moTa, tt);
            } else {
                // FIX: Truyền đủ 5 tham số bao gồm danhMuc
                ok = DataManager.themDichVu(ten, donVi, gia, moTa, danhMuc);
            }

            if (ok) {
                dlg.dispose();
                themMauDuLieu();   // reload bảng
                applyFilter();     // áp lại filter hiện tại
                refreshStatCards();// cập nhật thẻ thống kê
            } else {
                JOptionPane.showMessageDialog(dlg, "Lỗi lưu dữ liệu!");
            }
        });

        footer.add(btnHuy); footer.add(btnLuu);
        dlg.add(footer, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ═══════════════════════════════════════════════════
    //  XÓA MÓN
    // ═══════════════════════════════════════════════════
    private void xoaMon(int row) {
        String ten        = tableModel.getValueAt(row, 1).toString();
        String giaHienThi = tableModel.getValueAt(row, 3).toString();

        Object maSPObj = tableModel.getValueAt(row, 6);
        if (maSPObj == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy ID món này!"); return;
        }
        int maSP = Integer.parseInt(maSPObj.toString());

        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xóa món \"" + ten + "\" (" + giaHienThi + ") không?",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = DataManager.xoaDichVu(maSP);
            if (deleted) {
                tableModel.removeRow(row);
                refreshStatCards();
                JOptionPane.showMessageDialog(this,
                    "Đã xóa món \"" + ten + "\" thành công!",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int opt = JOptionPane.showConfirmDialog(this,
                    "Món \"" + ten + "\" đang có trong dữ liệu hóa đơn\n"
                    + "nên không thể xóa hoàn toàn.\n\n"
                    + "Bạn có muốn chuyển sang trạng thái \"Tạm Ngưng\" không?",
                    "Không thể xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (opt == JOptionPane.YES_OPTION) {
                    String tenHT   = tableModel.getValueAt(row, 1).toString();
                    String donViHT = tableModel.getValueAt(row, 2).toString();
                    double giaHT;
                    try { giaHT = Double.parseDouble(
                            tableModel.getValueAt(row, 3).toString()
                                .replaceAll("[^\\d.]", ""));
                    } catch (Exception ex) { giaHT = 0; }
                    String moTaHT = tableModel.getValueAt(row, 4).toString();

                    boolean updated = DataManager.suaDichVu(
                        maSP, tenHT, donViHT, giaHT, moTaHT, "Tạm Ngưng");

                    if (updated) {
                        tableModel.setValueAt("Tạm Ngưng", row, 5);
                        refreshStatCards();
                        JOptionPane.showMessageDialog(this,
                            "Đã chuyển món \"" + ten + "\" sang trạng thái Tạm Ngưng.",
                            "Cập nhật thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Cập nhật trạng thái thất bại. Vui lòng thử lại!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    // ═══════════════════════════════════════════════════
    //  LỌC VÀ TÌM KIẾM
    // ═══════════════════════════════════════════════════
    private void applyFilter() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);

        String searchText = "";
        if (txSearch != null) {
            String txt = txSearch.getText().trim();
            if (!txt.startsWith("🔍")) searchText = txt.toLowerCase();
        }

        List<Object[]> allData = DataManager.getDsDichVu();
        int stt = 1;

        for (Object[] row : allData) {
            String tenMon  = str(row[1]).toLowerCase();
            String danhMuc = str(row[7]); // cột danhMuc từ DB

            // FIX: Lọc theo danh mục — so sánh với selectedCategory
            boolean matchCat = "Tất Cả".equals(selectedCategory)
                             || selectedCategory.equalsIgnoreCase(danhMuc);
            boolean matchSearch = searchText.isEmpty() || tenMon.contains(searchText);

            if (matchCat && matchSearch) {
                tableModel.addRow(new Object[]{
                    stt++,
                    row[1], // tenMon
                    row[2], // donVi
                    row[3], // gia
                    row[4], // moTa
                    row[5], // trangThai
                    row[6], // maSP (ẩn)
                    row[7], // danhMuc (ẩn)
                    ""      // thao tác
                });
            }
        }
    }

    // ═══════════════════════════════════════════════════
    //  LOAD DỮ LIỆU ĐẦY ĐỦ
    // ═══════════════════════════════════════════════════
    private void themMauDuLieu() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        List<Object[]> ds = DataManager.getDsDichVu();
        for (Object[] row : ds)
            tableModel.addRow(new Object[]{
                row[0], row[1], row[2], row[3], row[4], row[5],
                row[6], row[7], ""});
    }

    // ═══════════════════════════════════════════════════
    //  HELPERS UI
    // ═══════════════════════════════════════════════════
    private JPanel buildActPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 8));
        p.setBackground(CLR_WHITE);
        p.add(miniBtn("Sửa", new Color(59, 130, 246), CLR_WHITE));
        p.add(miniBtn("Xóa", CLR_RED, CLR_WHITE));
        return p;
    }

    private JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cb.setBorder(new LineBorder(CLR_BORDER, 1, true));
        return cb;
    }

    private JPanel frmRow(String label, JTextField field) {
        JPanel row = new JPanel();
        row.setOpaque(false);
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.add(frmLbl(label));
        row.add(Box.createVerticalStrut(4));
        row.add(field);
        return row;
    }

    private JLabel frmLbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(CLR_TEXT);
        return l;
    }

    private JTextField frmField(String val) {
        JTextField tf = new JTextField(val);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tf.setBorder(new CompoundBorder(
            new LineBorder(CLR_BORDER, 1, true),
            new EmptyBorder(4, 10, 4, 10)));
        return tf;
    }

    private JButton roundBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed()  ? bg.darker()   :
                             getModel().isRollover() ? bg.brighter() : bg);
                g2.fill(new RoundRectangle2D.Double(0, 0,
                        getWidth(), getHeight(), 8, 8));
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg); btn.setForeground(fg);
        btn.setFocusPainted(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(9, 20, 9, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton miniBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
                g2.fill(new RoundRectangle2D.Double(0, 0,
                        getWidth(), getHeight(), 6, 6));
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setBackground(bg); btn.setForeground(fg);
        btn.setFocusPainted(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(4, 10, 4, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(75, 30));
        return btn;
    }

    private JButton xBtn(Color fg) {
        JButton b = new JButton("X");
        b.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        b.setForeground(fg);
        b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private String str(Object o) { return o == null ? "" : o.toString(); }

    // ── WrapLayout ────────────────────────────────────
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
                return new Dimension(maxW + ins.left + ins.right,
                                     y + rowH + getVgap() + ins.top + ins.bottom);
            }
        }
    }
}