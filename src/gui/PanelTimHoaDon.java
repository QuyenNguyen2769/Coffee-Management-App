package gui;

import connectDB.ConnectDB;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * PanelTimHoaDon.java
 * Tìm kiếm hóa đơn theo: Mã HD / Mã Nhân Viên / Mã Bàn / Trạng Thái
 */
public class PanelTimHoaDon extends JPanel {

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

    private JTextField    txtKeyword;
    private JComboBox<String> cbCriteria;
    private JTable        table;
    private DefaultTableModel model;
    private JLabel        lblCount;

    public PanelTimHoaDon() {
        setLayout(new BorderLayout(10, 10));
        setBackground(C_BG);
        setBorder(new EmptyBorder(15, 20, 15, 20));
        add(buildNorth(),  BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        search("", "Tất cả");
    }

    private JPanel buildNorth() {
        JPanel w = new JPanel(new BorderLayout(0, 10)); w.setOpaque(false);
        JLabel lbl = new JLabel("Tìm Hóa Đơn");
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
            new EmptyBorder(8, 12, 8, 12)
        ));

        JLabel lblBy = new JLabel("Tìm theo:");
        lblBy.setFont(F_LABEL); lblBy.setForeground(new Color(60, 40, 20));

        cbCriteria = new JComboBox<>(new String[]{"Tất cả", "Mã HD", "Mã NV", "Mã Bàn", "Trạng Thái"});
        cbCriteria.setFont(F_INPUT);
        cbCriteria.setPreferredSize(new Dimension(130, 32));

        txtKeyword = new JTextField();
        txtKeyword.setFont(F_INPUT);
        txtKeyword.setPreferredSize(new Dimension(260, 32));
        txtKeyword.addActionListener(e -> doSearch());

        JButton btnFind  = makeBtn("Tìm Kiếm",        C_BTN_FIND);
        JButton btnReset = makeBtn("Hiển Thị Tất Cả", C_BTN_RESET);
        btnFind.addActionListener(e -> doSearch());
        btnReset.addActionListener(e -> { txtKeyword.setText(""); cbCriteria.setSelectedIndex(0); search("", "Tất cả"); });

        lblCount = new JLabel("Tổng: 0 hóa đơn");
        lblCount.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblCount.setForeground(new Color(100, 80, 60));

        bar.add(lblBy); bar.add(cbCriteria);
        bar.add(new JLabel("Từ khóa:")); bar.add(txtKeyword);
        bar.add(btnFind); bar.add(btnReset);
        bar.add(Box.createHorizontalStrut(20)); bar.add(lblCount);
        return bar;
    }

    private JScrollPane buildCenter() {
        String[] cols = {"Mã HD", "Mã NV", "Mã Bàn", "Ngày Lập", "Tổng Tiền", "Trạng Thái"};
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
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) setBackground(row % 2 == 0 ? C_WHITE : new Color(245, 240, 235));
                setHorizontalAlignment(col <= 1 || col == 2 ? SwingConstants.CENTER : SwingConstants.LEFT);
                if (col == 4) setHorizontalAlignment(SwingConstants.RIGHT);
                setBorder(new EmptyBorder(0, 6, 0, 6));
                return this;
            }
        });

        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(160);
        table.getColumnModel().getColumn(4).setPreferredWidth(140);
        table.getColumnModel().getColumn(5).setPreferredWidth(130);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(210, 190, 170)));
        return sp;
    }

    private void doSearch() {
        search(txtKeyword.getText().trim(), cbCriteria.getSelectedItem().toString());
    }

    private void search(String keyword, String criteria) {
        model.setRowCount(0);
        try (Connection conn = ConnectDB.getConnection()) {
            if (conn == null) return;

            PreparedStatement ps;
            String base = "SELECT maHD, maNV, maBan, ngayLap, tongTien, trangThai FROM HoaDon ";
            if (criteria.equals("Tất cả") || keyword.isEmpty()) {
                ps = conn.prepareStatement(base + "ORDER BY maHD DESC");
            } else {
                String col;
                switch (criteria) {
                    case "Mã HD":       col = "CAST(maHD AS NVARCHAR)"; break;
                    case "Mã NV":       col = "maNV"; break;
                    case "Mã Bàn":      col = "CAST(maBan AS NVARCHAR)"; break;
                    case "Trạng Thái":  col = "trangThai";               break;
                    default:            col = "CAST(maHD AS NVARCHAR)";
                }
                ps = conn.prepareStatement(base + "WHERE " + col + " LIKE ? ORDER BY maHD DESC");
                ps.setString(1, "%" + keyword + "%");
            }

            ResultSet rs = ps.executeQuery();
            int count = 0;
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("maHD"), rs.getString("maNV"), rs.getInt("maBan"),
                    rs.getString("ngayLap"),
                    String.format("%,.0f đ", rs.getDouble("tongTien")),
                    rs.getString("trangThai")
                });
                count++;
            }
            lblCount.setText("Tổng: " + count + " hóa đơn");
        } catch (Exception ex) {
            ex.printStackTrace(); lblCount.setText("Lỗi kết nối DB");
        }
    }

    private JButton makeBtn(String txt, Color bg) {
        JButton btn = new JButton(txt);
        btn.setFont(F_BTN); btn.setBackground(bg); btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(160, 32));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            Color orig = bg;
            public void mouseEntered(MouseEvent e) { btn.setBackground(orig.darker()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(orig); }
        });
        return btn;
    }
}
