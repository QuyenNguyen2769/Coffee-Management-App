package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import dao.DataManager;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;

public class PanelKhuyenMai extends JPanel {

    // ── Bảng màu ─────────────────────────────────────────
    private static final Color BG_PAGE    = new Color(252, 248, 243);
    private static final Color CLR_BROWN  = new Color(101, 67,  33);
    private static final Color CLR_AMBER  = new Color(217, 119, 6);
    private static final Color CLR_CREAM  = new Color(254, 243, 224);
    private static final Color CLR_GREEN  = new Color(22,  163, 74);
    private static final Color CLR_RED    = new Color(220, 53,  69);
    private static final Color CLR_GRAY   = new Color(107, 114, 128);
    private static final Color CLR_BORDER = new Color(229, 215, 195);
    private static final Color CLR_TEXT   = new Color(28,  20,  10);
    private static final Color CLR_WHITE  = Color.WHITE;

    private DefaultTableModel tableModel;
    private JTable            table;

    // ── Stat card labels (cập nhật realtime) ──
    private JLabel lblTongVoucher;
    private JLabel lblDangHoatDong;
    private JLabel lblSapHetHan;

    // ── Ô tìm kiếm ──
    private JTextField txSearch;

    public PanelKhuyenMai() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);

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

        JLabel lblTitle = new JLabel("Khuyến Mãi");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(CLR_BROWN);

        JLabel lblSub = new JLabel("Tạo và quản lý các voucher giảm giá.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(CLR_GRAY);

        left.add(lblTitle);
        left.add(Box.createVerticalStrut(3));
        left.add(lblSub);
        hdr.add(left, BorderLayout.WEST);

        JButton btnTao = roundBtn("+ Tạo Voucher", CLR_AMBER, CLR_WHITE);
        btnTao.addActionListener(e -> moForm(null, -1));
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(btnTao);
        hdr.add(right, BorderLayout.EAST);

        return hdr;
    }

    // ═══════════════════════════════════════════════════
    //  NỘI DUNG
    // ═══════════════════════════════════════════════════
    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(BG_PAGE);
        content.setBorder(new EmptyBorder(22, 28, 22, 28));

        content.add(buildStatCards(), BorderLayout.NORTH);
        content.add(buildTable(),     BorderLayout.CENTER);

        return content;
    }

    // ── Thẻ thống kê ──
    private JPanel buildStatCards() {
        JPanel row = new JPanel(new GridLayout(1, 3, 16, 0));
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 90));

        lblTongVoucher  = valLabel("...", CLR_AMBER);
        lblDangHoatDong = valLabel("...", CLR_GREEN);
        lblSapHetHan    = valLabel("...", CLR_RED);

        row.add(statCard("🎟", "Tổng Voucher",    lblTongVoucher,  new Color(254, 243, 199)));
        row.add(statCard("✅", "Đang Hoạt Động", lblDangHoatDong, new Color(220, 252, 231)));
        row.add(statCard("⏰", "Sắp Hết Hạn",    lblSapHetHan,    new Color(254, 226, 226)));

        return row;
    }

    private JLabel valLabel(String text, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lbl.setForeground(color);
        return lbl;
    }

    private void refreshStatCards() {
        SwingWorker<java.util.Map<String, String>, Void> w = new SwingWorker<>() {
            @Override protected java.util.Map<String, String> doInBackground() {
                return DataManager.getThongKeKhuyenMai();
            }
            @Override protected void done() {
                try {
                    java.util.Map<String, String> m = get();
                    if (lblTongVoucher  != null) lblTongVoucher .setText(m.getOrDefault("tongVoucher",  "0"));
                    if (lblDangHoatDong != null) lblDangHoatDong.setText(m.getOrDefault("dangHoatDong", "0"));
                    if (lblSapHetHan    != null) lblSapHetHan   .setText(m.getOrDefault("sapHetHan",    "0"));
                } catch (Exception ignored) {}
            }
        };
        w.execute();
    }

    private JPanel statCard(String ico, String label, JLabel valLbl, Color bg) {
        JPanel card = new JPanel(new BorderLayout(12, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel lblIco = new JLabel(ico, SwingConstants.CENTER);
        lblIco.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        lblIco.setOpaque(true);
        lblIco.setBackground(bg);
        lblIco.setPreferredSize(new Dimension(56, 56));
        lblIco.setBorder(BorderFactory.createLineBorder(bg, 1, true));

        JPanel txt = new JPanel();
        txt.setOpaque(false);
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));

        JLabel lblLbl = new JLabel(label);
        lblLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblLbl.setForeground(CLR_GRAY);

        txt.add(valLbl);
        txt.add(lblLbl);

        card.add(lblIco, BorderLayout.WEST);
        card.add(txt,    BorderLayout.CENTER);
        return card;
    }

    // ── Bảng voucher ─────────────────────────────────
    private JPanel buildTable() {
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

        // ── Thanh tiêu đề + tìm kiếm ──
        JPanel tblHdr = new JPanel(new BorderLayout());
        tblHdr.setOpaque(false);
        tblHdr.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, CLR_BORDER),
            new EmptyBorder(14, 18, 14, 18)));

        JLabel lblTblTitle = new JLabel("Danh sách Voucher");
        lblTblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTblTitle.setForeground(CLR_TEXT);
        tblHdr.add(lblTblTitle, BorderLayout.WEST);

        txSearch = new JTextField(16);
        txSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txSearch.setBorder(new CompoundBorder(
            new LineBorder(CLR_BORDER, 1, true),
            new EmptyBorder(5, 10, 5, 10)));
        final String PLACEHOLDER = "🔍  Tìm mã / tên voucher...";
        txSearch.setText(PLACEHOLDER);
        txSearch.setForeground(CLR_GRAY);
        txSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txSearch.getText().startsWith("🔍"))
                    { txSearch.setText(""); txSearch.setForeground(CLR_TEXT); }
            }
            public void focusLost(FocusEvent e) {
                if (txSearch.getText().trim().isEmpty())
                    { txSearch.setText(PLACEHOLDER); txSearch.setForeground(CLR_GRAY); }
            }
        });
        txSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { applyFilter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { applyFilter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
        });

        tblHdr.add(txSearch, BorderLayout.EAST);
        wrap.add(tblHdr, BorderLayout.NORTH);

        // ── Table ──
        // Cols hiển thị: Mã Voucher | Tên | Giảm Giá | Loại | Hạn SD | Trạng Thái | Thao Tác
        // Col ẩn index 7: maKM (int)
        // Col ẩn index 8: ngayBatDau (String) — FIX: thêm để form sửa đọc lại
        String[] cols = {"Mã Voucher", "Tên Chương Trình", "Giảm Giá",
                          "Loại", "Hạn Sử Dụng", "Trạng Thái", "Thao Tác", "maKM", "ngayBatDau"};
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
        table.setIntercellSpacing(new Dimension(0, 0));

        // Ẩn cột maKM (index 7) và ngayBatDau (index 8)
        hideColumn(table, 7);
        hideColumn(table, 8);

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(new Color(250, 244, 234));
        th.setForeground(CLR_GRAY);
        th.setBorder(new MatteBorder(0, 0, 1, 0, CLR_BORDER));
        th.setPreferredSize(new Dimension(0, 42));

        int[] widths = {110, 210, 90, 100, 120, 120, 100, 0, 0};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Badge trạng thái (cột 5)
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                JLabel lbl = new JLabel(val != null ? val.toString() : "", SwingConstants.CENTER) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        String s = getText();
                        Color bg = s.contains("Đang") ? new Color(220, 252, 231)
                                 : s.contains("Hết")  ? new Color(254, 226, 226)
                                 :                      new Color(254, 243, 199);
                        Color fg = s.contains("Đang") ? CLR_GREEN
                                 : s.contains("Hết")  ? CLR_RED
                                 :                      CLR_AMBER;
                        g2.setColor(bg);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
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

        // Cột thao tác (cột 6)
        table.getColumnModel().getColumn(6).setCellRenderer(
            (t, val, sel, foc, r, c) -> buildActPanel());

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row < 0 || col != 6) return;
                Rectangle rect = table.getCellRect(row, col, false);
                int rx = e.getX() - rect.x;
                if (rx < rect.width / 2) {
                    // FIX: đọc đủ 9 cột kể cả 2 cột ẩn
                    Object[] d = new Object[tableModel.getColumnCount()];
                    for (int i = 0; i < d.length; i++)
                        d[i] = tableModel.getValueAt(row, i);
                    moForm(d, row);
                } else {
                    xoaVoucher(row);
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
    private void hideColumn(JTable tbl, int colIndex) {
        TableColumn col = tbl.getColumnModel().getColumn(colIndex);
        col.setMinWidth(0);
        col.setMaxWidth(0);
        col.setWidth(0);
        col.setPreferredWidth(0);
        col.setResizable(false);
    }

    private JPanel buildActPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 8));
        p.setBackground(CLR_WHITE);
        p.add(miniBtn("Sửa", new Color(59, 130, 246), CLR_WHITE));
        p.add(miniBtn("Xóa", CLR_RED, CLR_WHITE));
        return p;
    }

    // ═══════════════════════════════════════════════════
    //  FORM THÊM / SỬA
    // ═══════════════════════════════════════════════════
    private void moForm(Object[] data, int editRow) {
        boolean laSua = (editRow >= 0);
        Window owner  = SwingUtilities.getWindowAncestor(this);
        JDialog dlg   = new JDialog(owner instanceof Frame ? (Frame) owner : null,
                                    laSua ? "Sửa Voucher" : "Tạo Voucher Mới", true);
        dlg.setSize(480, 560);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        // Header
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(CLR_AMBER);
        hdr.setBorder(new EmptyBorder(16, 22, 16, 22));
        JLabel lblT = new JLabel(laSua ? "Sửa Voucher" : "Tạo Voucher Mới");
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblT.setForeground(CLR_WHITE);
        JButton btnX = xBtn();
        btnX.addActionListener(e -> dlg.dispose());
        hdr.add(lblT, BorderLayout.WEST);
        hdr.add(btnX, BorderLayout.EAST);
        dlg.add(hdr, BorderLayout.NORTH);

        // Body
        // data indices (khi sửa, 9 cột):
        //   0=maKMStr, 1=tenKM, 2=mucGiam%, 3=loai, 4=ngayKetThuc,
        //   5=trangThai, 6=thaoTac, 7=maKM(int), 8=ngayBatDau(String)
        JPanel body = new JPanel();
        body.setBackground(CLR_WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(20, 24, 10, 24));

        JTextField txTen    = frmField(laSua ? str(data[1]) : "");
        JTextField txGiam   = frmField(laSua ? str(data[2]).replace("%", "").trim() : "");

        // FIX: đọc ngayBatDau từ index 8 khi sửa (không để trống → tránh SET NULL)
        String ngayBDGoc = laSua ? str(data[8]) : "";
        JTextField txBatDau = frmField(ngayBDGoc);
        txBatDau.setToolTipText("Định dạng: yyyy-MM-dd  (để trống = giữ nguyên khi sửa / GETDATE() khi tạo mới)");

        JTextField txHan    = frmField(laSua ? str(data[4]) : "");
        txHan.setToolTipText("Để trống = không giới hạn. Định dạng: yyyy-MM-dd");

        String[] loais = {"Phần Trăm (%)", "Số Tiền (đ)"};
        JComboBox<String> cboLoai = styledCombo(loais);
        if (laSua && str(data[3]).contains("Tiền")) cboLoai.setSelectedIndex(1);

        body.add(frmRow("Tên Chương Trình *", txTen));  body.add(Box.createVerticalStrut(10));
        body.add(frmRow("Mức Giảm Giá *",     txGiam)); body.add(Box.createVerticalStrut(10));
        body.add(frmLbl("Loại Giảm Giá"));              body.add(Box.createVerticalStrut(4));
        body.add(cboLoai);                               body.add(Box.createVerticalStrut(10));
        body.add(frmRow("Ngày Bắt Đầu",       txBatDau)); body.add(Box.createVerticalStrut(10));
        body.add(frmRow("Ngày Kết Thúc",       txHan));  body.add(Box.createVerticalStrut(6));

        JLabel lblNote = new JLabel("<html><i style='color:#6b7280'>Trạng thái được tính tự động từ ngày bắt đầu / kết thúc.</i></html>");
        lblNote.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        body.add(lblNote);

        JScrollPane scrollBody = new JScrollPane(body);
        scrollBody.setBorder(null);
        dlg.add(scrollBody, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footer.setBackground(new Color(250, 244, 234));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, CLR_BORDER));
        JButton btnHuy = roundBtn("Hủy", new Color(229, 231, 235), new Color(55, 65, 81));
        JButton btnLuu = roundBtn(laSua ? "Lưu" : "Tạo Voucher", CLR_AMBER, CLR_WHITE);

        btnHuy.addActionListener(e -> dlg.dispose());
        btnLuu.addActionListener(e -> {
            String ten    = txTen.getText().trim();
            String giam   = txGiam.getText().trim().replace("%", "").replace(",", "").trim();
            String batDau = txBatDau.getText().trim();
            String han    = txHan.getText().trim();

            if (ten.isEmpty() || giam.isEmpty()) {
                JOptionPane.showMessageDialog(dlg,
                    "Vui lòng điền đầy đủ các trường bắt buộc (*)",
                    "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double mucGiam;
            try { mucGiam = Double.parseDouble(giam); }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg, "Mức giảm giá không hợp lệ!"); return;
            }

            boolean ok;
            if (laSua) {
                // FIX: data[7] = maKM (cột ẩn index 7)
                int maKM = Integer.parseInt(str(data[7]));
                // FIX: truyền batDau — nếu trống, suaKhuyenMai sẽ giữ nguyên ngayBatDau trong DB
                ok = DataManager.suaKhuyenMai(maKM, ten, mucGiam, batDau, han);
            } else {
                ok = themKhuyenMaiVoiBatDau(ten, mucGiam, batDau, han);
            }

            if (ok) {
                dlg.dispose();
                themMauDuLieu();
                applyFilter();
                refreshStatCards();
            } else {
                JOptionPane.showMessageDialog(dlg, "Lỗi lưu dữ liệu!");
            }
        });

        footer.add(btnHuy); footer.add(btnLuu);
        dlg.add(footer, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private boolean themKhuyenMaiVoiBatDau(String ten, double mucGiam,
                                            String ngayBatDau, String ngayKetThuc) {
        if (ngayBatDau.isEmpty()) {
            return DataManager.themKhuyenMai(ten, mucGiam, ngayKetThuc);
        }
        return DataManager.themKhuyenMaiDayDu(ten, mucGiam, ngayBatDau, ngayKetThuc);
    }

    // ═══════════════════════════════════════════════════
    //  XÓA VOUCHER
    // ═══════════════════════════════════════════════════
    private void xoaVoucher(int row) {
        String ten     = tableModel.getValueAt(row, 1).toString();
        // FIX: maKM vẫn ở index 7
        Object maKMObj = tableModel.getValueAt(row, 7);
        if (maKMObj == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy ID voucher!"); return;
        }
        int maKM = Integer.parseInt(maKMObj.toString());

        int c = JOptionPane.showConfirmDialog(this,
            "Xóa voucher \"" + ten + "\"?", "Xác nhận",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (c == JOptionPane.YES_OPTION) {
            if (DataManager.xoaKhuyenMai(maKM)) {
                themMauDuLieu();
                refreshStatCards();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa voucher này!");
            }
        }
    }

    // ═══════════════════════════════════════════════════
    //  TÌM KIẾM REALTIME
    // ═══════════════════════════════════════════════════
    private void applyFilter() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);

        String kw = "";
        if (txSearch != null) {
            String txt = txSearch.getText().trim();
            if (!txt.startsWith("🔍")) kw = txt.toLowerCase();
        }

        List<Object[]> ds = DataManager.getDsKhuyenMai();
        for (Object[] row : ds) {
            String ma  = str(row[0]).toLowerCase();
            String ten = str(row[1]).toLowerCase();
            boolean match = kw.isEmpty() || ma.contains(kw) || ten.contains(kw);
            if (match) {
                // FIX: 9 cột — index 7=maKM(int), index 8=ngayBatDau(String)
                tableModel.addRow(new Object[]{
                    row[0],  // 0: maKMStr
                    row[1],  // 1: tenKM
                    row[2],  // 2: mucGiam%
                    row[3],  // 3: loai
                    row[4],  // 4: ngayKetThuc
                    row[5],  // 5: trangThai
                    "",      // 6: thaoTac (renderer)
                    row[6],  // 7: maKM (ẩn) — từ getDsKhuyenMai index 6
                    row[7]   // 8: ngayBatDau (ẩn) — từ getDsKhuyenMai index 7
                });
            }
        }
    }

    // ═══════════════════════════════════════════════════
    //  LOAD ĐẦY ĐỦ DỮ LIỆU
    // ═══════════════════════════════════════════════════
    private void themMauDuLieu() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        // FIX: map đúng 9 cột, khớp với applyFilter
        for (Object[] row : DataManager.getDsKhuyenMai())
            tableModel.addRow(new Object[]{
                row[0],  // 0: maKMStr
                row[1],  // 1: tenKM
                row[2],  // 2: mucGiam%
                row[3],  // 3: loai
                row[4],  // 4: ngayKetThuc
                row[5],  // 5: trangThai
                "",      // 6: thaoTac
                row[6],  // 7: maKM (ẩn)
                row[7]   // 8: ngayBatDau (ẩn)
            });
    }

    // ═══════════════════════════════════════════════════
    //  HELPERS UI
    // ═══════════════════════════════════════════════════
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
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed()  ? bg.darker()  :
                             getModel().isRollover() ? bg.brighter(): bg);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 8, 8));
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
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 6, 6));
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

    private JButton xBtn() {
        JButton b = new JButton("✕");
        b.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        b.setForeground(CLR_WHITE);
        b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private String str(Object o) { return o == null ? "" : o.toString(); }
}