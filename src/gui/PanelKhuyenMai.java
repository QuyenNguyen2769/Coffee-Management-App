package gui;

import dao.DataManager;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.geom.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PanelKhuyenMai extends JPanel {

    private static final Color BG_PAGE    = new Color(252, 248, 243);
    private static final Color CLR_BROWN  = new Color(101, 67,  33);
    private static final Color CLR_AMBER  = new Color(217, 119, 6);
    private static final Color CL_GREEN   = new Color(22,  163, 74);
    private static final Color CLR_RED     = new Color(220, 38,  38);
    private static final Color CLR_GRAY   = new Color(107, 114, 128);
    private static final Color CLR_BORDER = new Color(229, 215, 195);
    private static final Color CLR_TEXT   = new Color(28,  20,  10);
    private static final Color CLR_WHITE  = Color.WHITE;
    private static final Color CLR_CREAM  = new Color(254, 243, 224);

    private DefaultTableModel tableModel;
    private JTable            table;
    private JTextField        txSearch;
    private JLabel            lblTongVoucher, lblDangHoatDong, lblSapHetHan;

    public PanelKhuyenMai() {
        setLayout(new BorderLayout());
        setBackground(BG_PAGE);

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);

        loadData();
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

        JLabel lblTitle = new JLabel("  Chương Trình Khuyến Mãi");
        try {
            ImageIcon icon = new ImageIcon("images/icon_khuyenmai.png");
            Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            lblTitle.setIcon(new ImageIcon(img));
        } catch(Exception e) {}
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(CLR_BROWN);

        JLabel lblSub = new JLabel("Quản lý mã giảm giá, voucher và các chiến dịch ưu đãi.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(CLR_GRAY);

        left.add(lblTitle);
        left.add(Box.createVerticalStrut(3));
        left.add(lblSub);
        hdr.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        JButton btnAdd = roundBtn("+ Tạo Voucher Mới", CLR_BROWN, CLR_WHITE);
        btnAdd.addActionListener(e -> showKMPopup(null));
        right.add(btnAdd);
        hdr.add(right, BorderLayout.EAST);

        return hdr;
    }

    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(BG_PAGE);
        content.setBorder(new EmptyBorder(22, 28, 22, 28));

        // 1. Stat Cards
        content.add(buildStatCards(), BorderLayout.NORTH);

        // 2. Table Panel
        content.add(buildTablePanel(), BorderLayout.CENTER);

        return content;
    }

    private JPanel buildStatCards() {
        JPanel row = new JPanel(new GridLayout(1, 3, 20, 0));
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 90));

        lblTongVoucher   = valLabel("...", CLR_BROWN);
        lblDangHoatDong = valLabel("...", CL_GREEN);
        lblSapHetHan    = valLabel("...", CLR_RED);

        row.add(statCard("🎟", "Tổng Voucher",    lblTongVoucher,  new Color(254, 243, 199)));
        row.add(statCard("✅", "Đang Hoạt Động", lblDangHoatDong, new Color(220, 252, 231)));
        row.add(statCard("⏰", "Sắp Hết Hạn",    lblSapHetHan,    new Color(254, 226, 226)));

        return row;
    }

    private JLabel valLabel(String text, Color c) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 22));
        l.setForeground(c);
        return l;
    }

    private JPanel statCard(String ico, String label, JLabel val, Color bg) {
        JPanel card = new JPanel(new BorderLayout(15, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));
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

        JPanel txt = new JPanel();
        txt.setOpaque(false);
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        JLabel lblLbl = new JLabel(label);
        lblLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLbl.setForeground(CLR_GRAY);
        txt.add(val);
        txt.add(lblLbl);

        card.add(lblIco, BorderLayout.WEST);
        card.add(txt,    BorderLayout.CENTER);
        return card;
    }

    private JPanel buildTablePanel() {
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

        // Filter bar
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, CLR_BORDER),
            new EmptyBorder(12, 18, 12, 18)));

        txSearch = new JTextField(20);
        txSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
                if (txSearch.getText().isEmpty())
                    { txSearch.setText(PLACEHOLDER); txSearch.setForeground(CLR_GRAY); }
            }
        });
        txSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
        });
        bar.add(txSearch, BorderLayout.WEST);

        wrap.add(bar, BorderLayout.NORTH);

        // Table
        String[] cols = {"STT", "Mã KM", "Tên Chương Trình", "Mức Giảm", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Trạng Thái", "Thao Tác"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 7; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(52);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(new Color(243, 234, 220));
        table.setSelectionBackground(CLR_CREAM);
        table.setShowVerticalLines(false);

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(new Color(250, 244, 234));
        th.setPreferredSize(new Dimension(0, 42));

        table.getColumnModel().getColumn(7).setCellRenderer((t,v,s,f,r,c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
            p.setBackground(CLR_WHITE);
            p.add(miniBtn("Sửa", new Color(59, 130, 246)));
            p.add(miniBtn("Xóa", CLR_RED));
            return p;
        });

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int col = table.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 7) {
                    Object[] data = new Object[table.getColumnCount()];
                    for(int i=0; i<data.length; i++) data[i] = tableModel.getValueAt(row, i);
                    
                    Rectangle rect = table.getCellRect(row, col, false);
                    int xInCell = e.getX() - rect.x;
                    
                    if (xInCell < rect.width / 2) {
                         showKMPopup(data);
                    } else {
                         confirmDelete(data[1].toString(), data[2].toString());
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CLR_WHITE);
        wrap.add(scroll, BorderLayout.CENTER);

        return wrap;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Object[]> ds = DataManager.getDsKhuyenMai();
        int stt = 1;
        for (Object[] r : ds) {
            Object[] row = new Object[8];
            System.arraycopy(r, 0, row, 1, 6);
            row[0] = stt++;
            row[3] = r[2] + "%";
            row[7] = "";
            tableModel.addRow(row);
        }
        updateStats();
    }

    private void updateStats() {
        java.util.Map<String, String> m = DataManager.getThongKeKhuyenMai();
        lblTongVoucher.setText(m.getOrDefault("tong", "0"));
        lblDangHoatDong.setText(m.getOrDefault("dangChay", "0"));
        lblSapHetHan.setText(m.getOrDefault("sapHet", "0"));
    }

    private void showKMPopup(Object[] data) {
        boolean isEdit = (data != null);
        JDialog dlg = new JDialog((Frame)null, isEdit ? "Sửa Khuyến Mãi" : "Tạo Mới", true);
        dlg.setLayout(new BorderLayout());
        dlg.setSize(380, 420);
        dlg.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridLayout(5, 1, 10, 15));
        p.setBorder(new EmptyBorder(25, 30, 25, 30));
        p.setBackground(CLR_WHITE);

        JTextField tfTen = new JTextField();
        JTextField tfMuc = new JTextField();
        JTextField tfBatDau = new JTextField(LocalDate.now().toString());
        JTextField tfKetThuc = new JTextField(LocalDate.now().plusMonths(1).toString());

        if (isEdit) {
            tfTen.setText(data[2].toString());
            tfMuc.setText(data[3].toString().replace("%",""));
            tfBatDau.setText(data[4].toString());
            tfKetThuc.setText(data[5].toString());
        }

        p.add(new JLabel("Tên chương trình:")); p.add(tfTen);
        p.add(new JLabel("Mức giảm (%):")); p.add(tfMuc);
        p.add(new JLabel("Ngày bắt đầu (yyyy-mm-dd):")); p.add(tfBatDau);
        p.add(new JLabel("Ngày kết thúc (yyyy-mm-dd):")); p.add(tfKetThuc);

        JButton btnSave = roundBtn(isEdit ? "Cập Nhật" : "Kích Hoạt", CLR_BROWN, CLR_WHITE);
        btnSave.addActionListener(e -> {
            try {
                String ten = tfTen.getText().trim();
                int mucGiam = Integer.parseInt(tfMuc.getText().trim());
                String batDau = tfBatDau.getText().trim();
                String han = tfKetThuc.getText().trim();
                
                boolean ok;
                if (isEdit) {
                    int maKM = (Integer)data[1];
                    ok = DataManager.suaKhuyenMai(maKM, ten, mucGiam, batDau, han);
                } else {
                    ok = DataManager.themKhuyenMaiDayDu(ten, mucGiam, batDau, han);
                }

                if (ok) { dlg.dispose(); loadData(); }
            } catch (Exception ex) { JOptionPane.showMessageDialog(dlg, "Lỗi định dạng dữ liệu!"); }
        });

        dlg.add(p, BorderLayout.CENTER);
        JPanel btm = new JPanel(); btm.setBackground(CLR_WHITE); btm.add(btnSave);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void confirmDelete(String maKM, String ten) {
        int c = JOptionPane.showConfirmDialog(this, "Xóa khuyến mãi '" + ten + "'?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            if (DataManager.xoaKhuyenMai(Integer.parseInt(maKM))) {
                loadData();
            }
        }
    }

    private void applyFilter() {
        String kw = "";
        if (txSearch != null) {
            String txt = txSearch.getText().trim();
            if (!txt.startsWith("🔍")) kw = txt.toLowerCase();
        }

        List<Object[]> ds = DataManager.getDsKhuyenMai();
        tableModel.setRowCount(0);
        int stt = 1;
        for (Object[] r : ds) {
            String ten = r[1].toString().toLowerCase();
            if (kw.isEmpty() || ten.contains(kw)) {
                Object[] row = new Object[8];
                System.arraycopy(r, 0, row, 1, 6);
                row[0] = stt++;
                row[3] = r[2] + "%";
                row[7] = "";
                tableModel.addRow(row);
            }
        }
    }

    private JButton roundBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setPreferredSize(new Dimension(160, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        return btn;
    }

    private JButton miniBtn(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(65, 30));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        return btn;
    }
}