package gui;

import dao.DataManager;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.geom.*;

public class PanelDichVu extends JPanel {

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

    private DefaultTableModel tableModel;
    private JTable            table;
    private JTextField        txSearch;
    private JComboBox<String> cbCategory;
    
    // Labels for stats
    private JLabel lblTongMon, lblDangPhucVu, lblBanChay, lblGiaTB;

    public PanelDichVu() {
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

        JLabel lblTitle = new JLabel("  Quản Lý Dịch Vụ");
        try {
            ImageIcon icon = new ImageIcon("images/icon_dichvu.png");
            Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            lblTitle.setIcon(new ImageIcon(img));
        } catch(Exception e) {}
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(CLR_BROWN);

        JLabel lblSub = new JLabel("Quản lý danh sách món ăn, đồ uống và giá bán.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(CLR_GRAY);

        left.add(lblTitle);
        left.add(Box.createVerticalStrut(3));
        left.add(lblSub);
        hdr.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        JButton btnAdd = roundBtn("+ Thêm Món Mới", CLR_BROWN, CLR_WHITE);
        btnAdd.addActionListener(e -> showProductDialog(null));
        right.add(btnAdd);
        hdr.add(right, BorderLayout.EAST);

        return hdr;
    }

    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(BG_PAGE);
        content.setBorder(new EmptyBorder(22, 28, 22, 28));

        // 1. Thống kê nhanh
        content.add(buildStatCards(), BorderLayout.NORTH);

        // 2. Bảng và Bộ lọc
        content.add(buildTablePanel(), BorderLayout.CENTER);

        return content;
    }

    private JPanel buildStatCards() {
        JPanel row = new JPanel(new GridLayout(1, 4, 18, 0));
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 85));

        lblTongMon     = valLabel("...", CLR_BROWN);
        lblDangPhucVu   = valLabel("...", CLR_GREEN);
        lblBanChay    = valLabel("...", CLR_BLUE);
        lblGiaTB      = valLabel("...", CLR_AMBER);

        row.add(statCard("☕", "Tổng Món",       lblTongMon,    new Color(253, 243, 221)));
        row.add(statCard("✅", "Đang Phục Vụ",   lblDangPhucVu, new Color(220, 252, 231)));
        row.add(statCard("⭐", "Bán Chạy Nhất",  lblBanChay,    new Color(219, 234, 254)));
        row.add(statCard("💰", "Giá Trung Bình", lblGiaTB,      new Color(254, 243, 199)));

        return row;
    }

    private JLabel valLabel(String text, Color c) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 20));
        l.setForeground(c);
        return l;
    }

    private JPanel statCard(String ico, String label, JLabel val, Color bg) {
        JPanel card = new JPanel(new BorderLayout(15, 0)) {
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

        // Filter Bar
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, CLR_BORDER),
            new EmptyBorder(12, 18, 12, 18)));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        
        txSearch = new JTextField(18);
        txSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
            public void insertUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
        });

        String[] cats = {"Tất Cả Danh Mục", "Cà Phê", "Trà", "Đồ Đóng Chai", "Bánh Ngọt"};
        cbCategory = new JComboBox<>(cats);
        cbCategory.setPreferredSize(new Dimension(150, 36));
        cbCategory.addActionListener(e -> applyFilter());

        left.add(txSearch);
        left.add(new JLabel("  Phân loại:"));
        left.add(cbCategory);
        bar.add(left, BorderLayout.WEST);

        wrap.add(bar, BorderLayout.NORTH);

        // Table
        String[] cols = {"STT", "Tên Món", "ĐVT", "Giá Bán", "Mô Tả", "Trạng Thái", "Mã SP", "Danh Mục", "Thao Tác"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 8; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(52);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(CLR_CREAM);
        table.setGridColor(new Color(243, 234, 220));
        table.setShowVerticalLines(false);

        // Hide columns: MaSP, DanhMuc
        table.getColumnModel().getColumn(6).setMinWidth(0);
        table.getColumnModel().getColumn(6).setMaxWidth(0);
        table.getColumnModel().getColumn(7).setMinWidth(0);
        table.getColumnModel().getColumn(7).setMaxWidth(0);

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(new Color(250, 244, 234));
        th.setPreferredSize(new Dimension(0, 42));

        // Action Renderer
        table.getColumnModel().getColumn(8).setCellRenderer((t,v,s,f,r,c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
            p.setBackground(CLR_WHITE);
            p.add(miniBtn("Sửa", CLR_BLUE));
            p.add(miniBtn("Xóa", new Color(239, 68, 68)));
            return p;
        });
        
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int col = table.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 8) {
                    Object[] data = new Object[table.getColumnCount()];
                    for(int i=0; i<data.length; i++) data[i] = tableModel.getValueAt(row, i);
                    
                    Rectangle rect = table.getCellRect(row, col, false);
                    int xInCell = e.getX() - rect.x;
                    
                    if (xInCell < rect.width / 2) {
                         showProductDialog(data); // Edit
                    } else {
                         confirmDelete(data[6].toString(), data[1].toString());
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
        List<Object[]> ds = DataManager.getDsDichVu();
        for(Object[] r : ds) {
            Object[] row = new Object[9];
            System.arraycopy(r, 0, row, 0, 8);
            row[3] = String.format("%,.0fđ", (Double)r[3]);
            row[8] = "";
            tableModel.addRow(row);
        }
        updateStats();
    }

    private void updateStats() {
        java.util.Map<String, String> m = DataManager.getThongKeDichVu();
        lblTongMon.setText(m.getOrDefault("tongMon", "0"));
        lblDangPhucVu.setText(m.getOrDefault("dangPhucVu", "0"));
        lblBanChay.setText(m.getOrDefault("banChay", "N/A"));
        lblGiaTB.setText(m.getOrDefault("giaTB", "0đ"));
    }

    private void showProductDialog(Object[] data) {
        boolean isEdit = (data != null);
        JDialog dlg = new JDialog((Frame)null, isEdit ? "Sửa Món" : "Thêm Món Mới", true);
        dlg.setLayout(new BorderLayout());
        dlg.setSize(420, 520);
        dlg.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridLayout(7, 1, 10, 10));
        p.setBorder(new EmptyBorder(20, 30, 20, 30));
        p.setBackground(CLR_WHITE);

        JTextField tfTen = new JTextField();
        JTextField tfDonVi = new JTextField("Ly");
        JTextField tfGia = new JTextField();
        JTextField tfMoTa = new JTextField();
        String[] cats = {"Cà Phê", "Trà", "Đồ Đóng Chai", "Bánh Ngọt"};
        JComboBox<String> cbDM = new JComboBox<>(cats);
        String[] tts = {"Đang Phục Vụ", "Tạm Ngưng"};
        JComboBox<String> cbTT = new JComboBox<>(tts);

        if (isEdit) {
            tfTen.setText(data[1].toString());
            tfDonVi.setText(data[2].toString());
            tfGia.setText(data[3].toString().replaceAll("[^0-9]", ""));
            tfMoTa.setText(data[4].toString());
            cbDM.setSelectedItem(data[7].toString());
            cbTT.setSelectedItem(data[5].toString());
        }

        p.add(new JLabel("Tên món:")); p.add(tfTen);
        p.add(new JLabel("Đơn vị tính:")); p.add(tfDonVi);
        p.add(new JLabel("Giá bán:")); p.add(tfGia);
        p.add(new JLabel("Danh mục:")); p.add(cbDM);
        p.add(new JLabel("Trạng thái:")); p.add(cbTT);
        p.add(new JLabel("Mô tả:")); p.add(tfMoTa);

        JButton btnSave = roundBtn(isEdit ? "Cập Nhật" : "Lưu Lại", CLR_BROWN, CLR_WHITE);
        btnSave.addActionListener(e -> {
            try {
                String ten = tfTen.getText().trim();
                String donVi = tfDonVi.getText().trim();
                double gia = Double.parseDouble(tfGia.getText().trim());
                String moTa = tfMoTa.getText().trim();
                String danhMuc = cbDM.getSelectedItem().toString();
                String tt = cbTT.getSelectedItem().toString();

                if (ten.isEmpty()) throw new Exception("Tên không được để trống");

                boolean ok;
                if (isEdit) {
                    int maSP = (Integer)data[6];
                    ok = DataManager.suaDichVu(maSP, ten, donVi, gia, moTa, tt);
                } else {
                    ok = DataManager.themDichVu(ten, donVi, gia, moTa, danhMuc);
                }

                if (ok) {
                    dlg.dispose();
                    loadData();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Lỗi: " + ex.getMessage());
            }
        });

        dlg.add(p, BorderLayout.CENTER);
        JPanel btm = new JPanel(); btm.setBackground(CLR_WHITE); btm.add(btnSave);
        dlg.add(btm, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void confirmDelete(String maSP, String ten) {
        int c = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa món '" + ten + "'?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            boolean deleted = DataManager.xoaDichVu(Integer.parseInt(maSP));
            if (deleted) loadData();
            else JOptionPane.showMessageDialog(this, "Không thể xóa món này (đã có trong hóa đơn)");
        }
    }

    private void applyFilter() {
        String searchText = "";
        if (txSearch != null) {
            String txt = txSearch.getText().trim();
            if (!txt.startsWith("🔍")) searchText = txt.toLowerCase();
        }

        String cat = cbCategory.getSelectedItem().toString();
        tableModel.setRowCount(0);

        List<Object[]> allData = DataManager.getDsDichVu();
        int stt = 1;
        for (Object[] r : allData) {
            String ten = r[1].toString().toLowerCase();
            String dm  = r[7].toString();

            boolean matchSearch = searchText.isEmpty() || ten.contains(searchText);
            boolean matchCat    = cat.equals("Tất Cả Danh Mục") || dm.equals(cat);

            if (matchSearch && matchCat) {
                Object[] row = new Object[9];
                System.arraycopy(r, 0, row, 0, 8);
                row[0] = stt++;
                row[3] = String.format("%,.0fđ", (Double)r[3]);
                row[8] = "";
                tableModel.addRow(row);
            }
        }
    }

    // ── UI Helpers ──────────────────────────────────────────────
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
        btn.setPreferredSize(new Dimension(150, 40));
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