package gui;

import connectDB.ConnectDB;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * PanelTimNhanVien.java — Tìm kiếm nhân viên
 * - Tìm theo: Mã NV / Họ Tên / SĐT / Chức Vụ
 * - Cột Chức Vụ: màu chữ theo chức vụ (dùng NhanVienHelper)
 * - Có phần chú thích màu sắc chức vụ bên dưới
 */
public class PanelTimNhanVien extends JPanel {

    private static final Color C_BG       = new Color(245, 246, 250);
    private static final Color C_WHITE     = Color.WHITE;
    private static final Color C_TITLE     = new Color(44, 28, 18);
    private static final Color C_HDR_BG    = new Color(101, 67, 33);
    private static final Color C_HDR_FG    = new Color(62, 39, 35);
    private static final Color C_SELECT    = new Color(210, 180, 140);
    private static final Color C_BTN_FIND  = new Color(70, 130, 180);
    private static final Color C_BTN_RESET = new Color(120, 120, 120);
    private static final Font  F_TITLE     = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font  F_LABEL     = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font  F_INPUT     = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  F_BTN       = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font  F_TABLE     = new Font("Segoe UI", Font.PLAIN, 13);

    private JTextField       txtKeyword;
    private JComboBox<String> cbCriteria;
    private JTable           table;
    private DefaultTableModel model;
    private JLabel           lblCount;

    public PanelTimNhanVien() {
        setLayout(new BorderLayout(10, 10));
        setBackground(C_BG);
        setBorder(new EmptyBorder(15, 20, 15, 20));
        add(buildNorth(),  BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildLegend(), BorderLayout.SOUTH);
        search("", "Tất cả");
    }

    private JPanel buildNorth() {
        JPanel w = new JPanel(new BorderLayout(0, 10)); w.setOpaque(false);
        
        JLabel lbl = new JLabel("  Tìm Nhân Viên");
        try {
            ImageIcon icon = new ImageIcon("images/icon_nhanvien.png");
            Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(img));
        } catch(Exception e) {}
        
        lbl.setFont(F_TITLE); lbl.setForeground(C_TITLE);
        w.add(lbl, BorderLayout.NORTH);
        w.add(buildSearchBar(), BorderLayout.CENTER);
        return w;
    }

    private JPanel buildSearchBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        bar.setBackground(C_WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 190, 170), 1),
            new EmptyBorder(8, 12, 8, 12)));

        cbCriteria = new JComboBox<>(new String[]{"Tất cả", "Mã NV", "Họ Tên", "Số ĐT", "Chức Vụ"});
        cbCriteria.setFont(F_INPUT); cbCriteria.setPreferredSize(new Dimension(130, 32));

        txtKeyword = new JTextField();
        txtKeyword.setFont(F_INPUT); txtKeyword.setPreferredSize(new Dimension(280, 32));
        txtKeyword.addActionListener(e -> doSearch());

        JButton btnFind  = makeBtn("Tìm Kiếm",        C_BTN_FIND);
        JButton btnReset = makeBtn("Hiển Thị Tất Cả", C_BTN_RESET);
        btnFind .addActionListener(e -> doSearch());
        btnReset.addActionListener(e -> { txtKeyword.setText(""); cbCriteria.setSelectedIndex(0); search("","Tất cả"); });

        lblCount = new JLabel("Tổng: 0 nhân viên");
        lblCount.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblCount.setForeground(new Color(100, 80, 60));

        bar.add(new JLabel("Tìm theo:") {{ setFont(F_LABEL); }});
        bar.add(cbCriteria);
        bar.add(new JLabel("Từ khóa:") {{ setFont(F_LABEL); }});
        bar.add(txtKeyword);
        bar.add(btnFind); bar.add(btnReset);
        bar.add(Box.createHorizontalStrut(20)); bar.add(lblCount);
        return bar;
    }

    private JScrollPane buildCenter() {
        String[] cols = {"Mã NV", "Họ Tên", "Số ĐT", "Giới Tính", "Ngày Vào Làm", "Chức Vụ"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setFont(F_TABLE); table.setRowHeight(30);
        table.setShowGrid(false); table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(C_SELECT); table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(C_HDR_BG); header.setForeground(C_HDR_FG);
        header.setPreferredSize(new Dimension(0, 35));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBackground(sel ? C_SELECT : (row%2==0 ? C_WHITE : new Color(245,240,235)));
                setForeground(Color.BLACK);
                if (!sel && col == 5 && v != null) {
                    setForeground(NhanVienHelper.mauChucVu(v.toString()));
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                }
                setHorizontalAlignment(col == 0 ? SwingConstants.CENTER : SwingConstants.LEFT);
                setBorder(new EmptyBorder(0, 6, 0, 6));
                return this;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(210, 190, 170)));
        return sp;
    }

    private JPanel buildLegend() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 6));
        p.setBackground(C_WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210,190,170),1),
            new EmptyBorder(4,10,4,10)));

        String[][] items = {
            {"Quản lý",   "#B22222"}, {"Thu ngân",  "#4682B4"},
            {"Pha chế",   "#228B22"}, {"Phục vụ",   "#D2691E"},
            {"Part-time", "#800080"}
        };
        p.add(new JLabel("Chú thích: ") {{ setFont(new Font("Segoe UI", Font.BOLD, 11)); }});
        for (String[] item : items) {
            JLabel dot = new JLabel("● " + item[0]);
            dot.setFont(new Font("Segoe UI", Font.BOLD, 12));
            dot.setForeground(Color.decode(item[1]));
            p.add(dot);
        }
        return p;
    }

    private void doSearch() { search(txtKeyword.getText().trim(), cbCriteria.getSelectedItem().toString()); }

    private void search(String kw, String cr) {
        model.setRowCount(0);
        try (Connection conn = ConnectDB.getConnection()) {
            if (conn == null) return;
            PreparedStatement ps;
            String base = "SELECT maNV, hoTen, sdt, gioiTinh, CONVERT(VARCHAR,ngayVaoLam,103) AS ngayVaoLam, chucVu FROM NhanVien ";
            if (cr.equals("Tất cả") || kw.isEmpty()) {
                ps = conn.prepareStatement(base + "ORDER BY maNV");
            } else {
                String col = cr.equals("Mã NV") ? "maNV" : (cr.equals("Họ Tên") ? "hoTen" : (cr.equals("Số ĐT") ? "sdt" : "chucVu"));
                ps = conn.prepareStatement(base + "WHERE " + col + " LIKE ? ORDER BY maNV");
                ps.setString(1, "%" + kw + "%");
            }
            ResultSet rs = ps.executeQuery();
            int count = 0;
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("maNV"), rs.getString("hoTen"), rs.getString("sdt"),
                    rs.getString("gioiTinh"), rs.getString("ngayVaoLam"), rs.getString("chucVu")
                });
                count++;
            }
            lblCount.setText("Tổng: " + count + " nhân viên");
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private JButton makeBtn(String txt, Color bg) {
        JButton btn = new JButton(txt);
        btn.setFont(F_BTN); btn.setBackground(bg); btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(170, 32));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
