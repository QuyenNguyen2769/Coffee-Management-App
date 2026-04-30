package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import dao.DataManager;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.SimpleDateFormat;
import java.util.*;

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

    // ── Dữ liệu mẫu ──────────────────────────────────────
    private DefaultTableModel tableModel;
    private JTable            table;
    private int               nextId = 4;

    public PanelKhuyenMai() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);

        // Thêm dữ liệu mẫu
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

        // Trái: tiêu đề
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("🎁  Khuyến Mãi");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(CLR_BROWN);

        JLabel lblSub = new JLabel("Tạo và quản lý các voucher giảm giá.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(CLR_GRAY);

        left.add(lblTitle);
        left.add(Box.createVerticalStrut(3));
        left.add(lblSub);
        hdr.add(left, BorderLayout.WEST);

        // Phải: nút Tạo Voucher
        JButton btnTao = roundBtn("+ Tạo Voucher", CLR_AMBER, CLR_WHITE);
        btnTao.addActionListener(e -> moFormThem(null));
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(btnTao);
        hdr.add(right, BorderLayout.EAST);

        return hdr;
    }

    // ═══════════════════════════════════════════════════
    //  NỘI DUNG CHÍNH: thẻ thống kê + bảng
    // ═══════════════════════════════════════════════════
    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(BG_PAGE);
        content.setBorder(new EmptyBorder(22, 28, 22, 28));

        content.add(buildStatCards(), BorderLayout.NORTH);
        content.add(buildTable(),     BorderLayout.CENTER);

        return content;
    }

    // ── 3 thẻ thống kê nhanh ──────────────────────────
    private JPanel buildStatCards() {
        JPanel row = new JPanel(new GridLayout(1, 3, 16, 0));
        row.setOpaque(false);

        row.add(statCard("🎟", "Tổng Voucher",    "3",    CLR_AMBER,
                         new Color(254, 243, 199)));
        row.add(statCard("✅", "Đang Hoạt Động", "2",    CLR_GREEN,
                         new Color(220, 252, 231)));
        row.add(statCard("⏰", "Sắp Hết Hạn",    "1",    CLR_RED,
                         new Color(254, 226, 226)));

        return row;
    }

    private JPanel statCard(String ico, String label, String val,
                             Color accent, Color bg) {
        JPanel card = new JPanel(new BorderLayout(12, 0)) {
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
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        // Icon tròn
        JLabel lblIco = new JLabel(ico, SwingConstants.CENTER);
        lblIco.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        lblIco.setOpaque(true);
        lblIco.setBackground(bg);
        lblIco.setPreferredSize(new Dimension(56, 56));
        lblIco.setBorder(BorderFactory.createLineBorder(bg, 1, true));

        // Text
        JPanel txt = new JPanel();
        txt.setOpaque(false);
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));

        JLabel lblVal = new JLabel(val);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblVal.setForeground(accent);

        JLabel lblLbl = new JLabel(label);
        lblLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblLbl.setForeground(CLR_GRAY);

        txt.add(lblVal);
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
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0,
                                                     getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        wrap.setOpaque(false);
        wrap.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Thanh tiêu đề bảng
        JPanel tblHdr = new JPanel(new BorderLayout());
        tblHdr.setOpaque(false);
        tblHdr.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, CLR_BORDER),
            new EmptyBorder(14, 18, 14, 18)));

        JLabel lblTblTitle = new JLabel("Danh sách Voucher");
        lblTblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTblTitle.setForeground(CLR_TEXT);
        tblHdr.add(lblTblTitle, BorderLayout.WEST);

        // Ô tìm kiếm nhỏ
        JTextField txSearch = new JTextField(14);
        txSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txSearch.setBorder(new CompoundBorder(
            new LineBorder(CLR_BORDER, 1, true),
            new EmptyBorder(5, 10, 5, 10)));
        txSearch.setText("🔍  Tìm mã voucher...");
        txSearch.setForeground(CLR_GRAY);
        txSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txSearch.getText().startsWith("🔍"))
                    { txSearch.setText(""); txSearch.setForeground(CLR_TEXT); }
            }
            public void focusLost(FocusEvent e) {
                if (txSearch.getText().isEmpty())
                    { txSearch.setText("🔍  Tìm mã voucher..."); txSearch.setForeground(CLR_GRAY); }
            }
        });
        tblHdr.add(txSearch, BorderLayout.EAST);
        wrap.add(tblHdr, BorderLayout.NORTH);

        // Table
        String[] cols = {"Mã Voucher", "Tên Chương Trình", "Giảm Giá",
                          "Loại", "Hạn Sử Dụng", "Trạng Thái", "Thao Tác"};
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
        table.setIntercellSpacing(new Dimension(0, 0));

        // Header style
        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(new Color(250, 244, 234));
        th.setForeground(CLR_GRAY);
        th.setBorder(new MatteBorder(0, 0, 1, 0, CLR_BORDER));
        th.setPreferredSize(new Dimension(0, 42));

        // Cột độ rộng
        int[] widths = {120, 220, 100, 100, 130, 110, 100};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Renderer cột Trạng Thái (badge màu)
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                JLabel lbl = new JLabel(val != null ? val.toString() : "", SwingConstants.CENTER) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                            RenderingHints.VALUE_ANTIALIAS_ON);
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

        // Renderer cột Thao Tác (2 nút)
        table.getColumnModel().getColumn(6).setCellRenderer(new TableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                return buildActPanel();
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row < 0) return;
                if (col == 6) {
                    // Xác định vùng nút
                    Rectangle cellRect = table.getCellRect(row, col, false);
                    int rx = e.getX() - cellRect.x;
                    if (rx < cellRect.width / 2) {
                        // Nút Sửa
                        Object[] rowData = new Object[tableModel.getColumnCount()];
                        for (int i = 0; i < rowData.length - 1; i++)
                            rowData[i] = tableModel.getValueAt(row, i);
                        moFormSua(row, rowData);
                    } else {
                        // Nút Xóa
                        xoaVoucher(row);
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        scroll.getViewport().setBackground(CLR_WHITE);
        wrap.add(scroll, BorderLayout.CENTER);

        return wrap;
    }

    private JPanel buildActPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 8));
        p.setBackground(CLR_WHITE);

        JButton btnSua = miniBtn("✏ Sửa",  new Color(59, 130, 246), CLR_WHITE);
        JButton btnXoa = miniBtn("🗑 Xóa", CLR_RED, CLR_WHITE);
        p.add(btnSua); p.add(btnXoa);
        return p;
    }

    // ═══════════════════════════════════════════════════
    //  FORM THÊM / SỬA (JDialog)
    // ═══════════════════════════════════════════════════
    private void moFormThem(Object[] data) {
        moForm(data, -1);
    }

    private void moFormSua(int row, Object[] data) {
        moForm(data, row);
    }

    private void moForm(Object[] data, int editRow) {
        boolean laSua = (editRow >= 0);
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dlg = new JDialog(owner instanceof Frame ? (Frame) owner : null,
                                   laSua ? "Sửa Voucher" : "Tạo Voucher Mới", true);
        dlg.setSize(480, 520);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());
        dlg.getRootPane().setBorder(null);

        // Header
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(CLR_AMBER);
        hdr.setBorder(new EmptyBorder(16, 22, 16, 22));
        JLabel lblT = new JLabel(laSua ? "✏  Sửa Voucher" : "🎁  Tạo Voucher Mới");
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblT.setForeground(CLR_WHITE);
        JButton btnX = xBtn();
        btnX.addActionListener(e -> dlg.dispose());
        hdr.add(lblT, BorderLayout.WEST);
        hdr.add(btnX, BorderLayout.EAST);
        dlg.add(hdr, BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel();
        body.setBackground(CLR_WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(20, 24, 10, 24));

        JTextField txMa    = frmField(laSua ? str(data[0]) : "");
        JTextField txTen   = frmField(laSua ? str(data[1]) : "");
        JTextField txGiam  = frmField(laSua ? str(data[2]) : "");
        String[] loais = {"Phần Trăm (%)", "Số Tiền (đ)"};
        JComboBox<String> cboLoai = new JComboBox<>(loais);
        cboLoai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboLoai.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        if (laSua && str(data[3]).contains("Tiền")) cboLoai.setSelectedIndex(1);
        JTextField txHan   = frmField(laSua ? str(data[4]) : "");
        txHan.setToolTipText("Định dạng: dd/MM/yyyy");

        String[] ttList = {"Đang Hoạt Động", "Hết Hạn", "Chờ Kích Hoạt"};
        JComboBox<String> cboTT = new JComboBox<>(ttList);
        cboTT.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboTT.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        if (laSua) {
            String tt = str(data[5]);
            for (int i = 0; i < ttList.length; i++)
                if (ttList[i].equals(tt)) { cboTT.setSelectedIndex(i); break; }
        }

        body.add(frmRow("Mã Voucher *",       txMa));   body.add(Box.createVerticalStrut(10));
        body.add(frmRow("Tên Chương Trình *",  txTen));  body.add(Box.createVerticalStrut(10));
        body.add(frmRow("Mức Giảm Giá *",      txGiam)); body.add(Box.createVerticalStrut(10));
        body.add(frmLbl("Loại Giảm Giá"));               body.add(Box.createVerticalStrut(4));
        body.add(cboLoai);                                body.add(Box.createVerticalStrut(10));
        body.add(frmRow("Hạn Sử Dụng",        txHan));  body.add(Box.createVerticalStrut(10));
        body.add(frmLbl("Trạng Thái"));                  body.add(Box.createVerticalStrut(4));
        body.add(cboTT);

        dlg.add(new JScrollPane(body) {{ setBorder(null); }}, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footer.setBackground(new Color(250, 244, 234));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, CLR_BORDER));
        JButton btnHuy = roundBtn("Hủy", new Color(229, 231, 235), new Color(55, 65, 81));
        JButton btnLuu = roundBtn(laSua ? "💾 Lưu" : "✔ Tạo Voucher", CLR_AMBER, CLR_WHITE);

        btnHuy.addActionListener(e -> dlg.dispose());
        btnLuu.addActionListener(e -> {
            String ma   = txMa.getText().trim();
            String ten  = txTen.getText().trim();
            String giam = txGiam.getText().trim();
            String han  = txHan.getText().trim();
            String loai = cboLoai.getSelectedItem().toString().contains("%")
                          ? "Phần Trăm" : "Số Tiền";
            String tt   = cboTT.getSelectedItem().toString();

            if (ma.isEmpty() || ten.isEmpty() || giam.isEmpty()) {
                JOptionPane.showMessageDialog(dlg,
                    "Vui lòng điền đầy đủ các trường bắt buộc (*)",
                    "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (laSua) {
                tableModel.setValueAt(ma,   editRow, 0);
                tableModel.setValueAt(ten,  editRow, 1);
                tableModel.setValueAt(giam, editRow, 2);
                tableModel.setValueAt(loai, editRow, 3);
                tableModel.setValueAt(han,  editRow, 4);
                tableModel.setValueAt(tt,   editRow, 5);
                DataManager.getDsKhuyenMai().set(editRow,
                    new Object[]{ma, ten, giam, loai, han, tt});
            } else {
                tableModel.addRow(new Object[]{ma, ten, giam, loai, han, tt, ""});
                DataManager.getDsKhuyenMai().add(new Object[]{ma, ten, giam, loai, han, tt});
            }
            DataManager.luuKhuyenMai(); // ← LƯU NGAY
            dlg.dispose();
        });

        footer.add(btnHuy); footer.add(btnLuu);
        dlg.add(footer, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void xoaVoucher(int row) {
        String ten = tableModel.getValueAt(row, 1).toString();
        int c = JOptionPane.showConfirmDialog(this,
            "Xóa voucher \"" + ten + "\"?", "Xác nhận",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
            DataManager.getDsKhuyenMai().remove(row); // ← THÊM
            DataManager.luuKhuyenMai();               // ← THÊM
        }
    }

    // ═══════════════════════════════════════════════════
    //  DỮ LIỆU MẪU
    // ═══════════════════════════════════════════════════
    private void themMauDuLieu() {
        tableModel.setRowCount(0);
        for (Object[] row : DataManager.getDsKhuyenMai())
            tableModel.addRow(new Object[]{row[0], row[1], row[2],
                                            row[3], row[4], row[5], ""});
    }
    // ═══════════════════════════════════════════════════
    //  HELPERS UI
    // ═══════════════════════════════════════════════════
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
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
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