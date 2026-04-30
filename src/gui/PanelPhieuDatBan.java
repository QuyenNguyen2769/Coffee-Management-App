package gui;

import connectDB.ConnectDB;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * PanelPhieuDatBan.java
 * Tìm kiếm & xem chi tiết Phiếu Đặt Bàn
 */
public class PanelPhieuDatBan extends JPanel {

    private static final Color C_BG       = new Color(245, 246, 250);
    private static final Color C_WHITE     = Color.WHITE;
    private static final Color C_TITLE     = new Color(44, 28, 18);
    private static final Color C_HDR_BG    = new Color(101, 67, 33);
    private static final Color C_HDR_FG    = new Color(62, 39, 35);
    private static final Color C_SELECT    = new Color(210, 180, 140);
    private static final Color C_BTN_FIND  = new Color(70, 130, 180);
    private static final Color C_BTN_RESET = new Color(120, 120, 120);
    private static final Color C_BTN_DEL   = new Color(178, 34, 34);
    private static final Font  F_TITLE     = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font  F_LABEL     = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font  F_INPUT     = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  F_BTN       = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font  F_TABLE     = new Font("Segoe UI", Font.PLAIN, 13);

    private JTextField    txtKeyword;
    private JComboBox<String> cbCriteria, cbTrangThai;
    private JTable        table;
    private DefaultTableModel model;
    private JLabel        lblCount;
    private JButton       btnHuy;

    public PanelPhieuDatBan() {
        setLayout(new BorderLayout(10, 10));
        setBackground(C_BG);
        setBorder(new EmptyBorder(15, 20, 15, 20));
        add(buildNorth(),  BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildSouth(),  BorderLayout.SOUTH);
        search("", "Tất cả", "Tất cả");
    }

    private JPanel buildNorth() {
        JPanel w = new JPanel(new BorderLayout(0, 10)); w.setOpaque(false);
        JLabel lbl = new JLabel("Phiếu Đặt Bàn");
        lbl.setFont(F_TITLE); lbl.setForeground(C_TITLE);
        w.add(lbl, BorderLayout.NORTH);
        w.add(buildSearchBar(), BorderLayout.CENTER);
        return w;
    }

    private JPanel buildSearchBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        bar.setBackground(C_WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 190, 170), 1),
            new EmptyBorder(8, 12, 8, 12)
        ));

        JLabel lblBy = new JLabel("Tìm theo:");
        lblBy.setFont(F_LABEL); lblBy.setForeground(new Color(60, 40, 20));

        cbCriteria = new JComboBox<>(new String[]{"Tất cả", "Mã PĐB", "Mã KH", "Tên Khách"});
        cbCriteria.setFont(F_INPUT); cbCriteria.setPreferredSize(new Dimension(120, 30));

        txtKeyword = new JTextField();
        txtKeyword.setFont(F_INPUT); txtKeyword.setPreferredSize(new Dimension(220, 30));
        txtKeyword.addActionListener(e -> doSearch());

        JLabel lblTT = new JLabel("Trạng thái:");
        lblTT.setFont(F_LABEL); lblTT.setForeground(new Color(60, 40, 20));
        cbTrangThai = new JComboBox<>(new String[]{"Tất cả", "Chờ xác nhận", "Đã xác nhận", "Đã hủy"});
        cbTrangThai.setFont(F_INPUT); cbTrangThai.setPreferredSize(new Dimension(140, 30));

        JButton btnFind  = makeBtn("Tìm", C_BTN_FIND);
        JButton btnReset = makeBtn("Tất Cả", C_BTN_RESET);
        btnFind.setPreferredSize(new Dimension(110, 30));
        btnReset.setPreferredSize(new Dimension(110, 30));
        btnFind.addActionListener(e -> doSearch());
        btnReset.addActionListener(e -> {
            txtKeyword.setText(""); cbCriteria.setSelectedIndex(0); cbTrangThai.setSelectedIndex(0);
            search("", "Tất cả", "Tất cả");
        });

        lblCount = new JLabel("Tổng: 0 phiếu");
        lblCount.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblCount.setForeground(new Color(100, 80, 60));

        bar.add(lblBy); bar.add(cbCriteria);
        bar.add(txtKeyword); bar.add(lblTT); bar.add(cbTrangThai);
        bar.add(btnFind); bar.add(btnReset);
        bar.add(Box.createHorizontalStrut(10)); bar.add(lblCount);
        return bar;
    }

    private JScrollPane buildCenter() {
        String[] cols = {"Mã PĐB", "Mã KH", "Tên Khách", "Ngày Đặt", "Ngày Đến", "Trạng Thái"};
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
                if (!sel) {
                    setBackground(row % 2 == 0 ? C_WHITE : new Color(245, 240, 235));
                    if (col == 5 && v != null) {
                        String tt = v.toString();
                        if (tt.contains("hủy") || tt.contains("Hủy")) setForeground(new Color(178, 34, 34));
                        else if (tt.contains("xác nhận") || tt.contains("Xác nhận")) setForeground(new Color(0, 128, 0));
                        else setForeground(Color.BLACK);
                    } else setForeground(Color.BLACK);
                }
                setHorizontalAlignment(col == 0 || col == 1 ? SwingConstants.CENTER : SwingConstants.LEFT);
                setBorder(new EmptyBorder(0, 6, 0, 6));
                return this;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(210, 190, 170)));
        return sp;
    }

    private JPanel buildSouth() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        p.setOpaque(false);
        btnHuy = makeBtn("Hủy Phiếu Đặt", C_BTN_DEL);
        p.add(btnHuy);
        btnHuy.addActionListener(e -> huyPhieuDat());
        return p;
    }

    private void doSearch() {
        search(txtKeyword.getText().trim(), cbCriteria.getSelectedItem().toString(), cbTrangThai.getSelectedItem().toString());
    }

    private void search(String keyword, String criteria, String trangThai) {
        model.setRowCount(0);
        try (Connection conn = ConnectDB.getConnection()) {
            if (conn == null) return;
            StringBuilder sql = new StringBuilder(
                "SELECT p.maPDB, p.maKH, k.hoTen, p.ngayDat, p.ngayDen, p.trangThai " +
                "FROM PhieuDatBan p LEFT JOIN KhachHang k ON p.maKH = k.maKH WHERE 1=1 "
            );
            if (!trangThai.equals("Tất cả")) sql.append("AND p.trangThai LIKE ? ");
            if (!criteria.equals("Tất cả") && !keyword.isEmpty()) {
                switch (criteria) {
                    case "Mã PĐB":    sql.append("AND CAST(p.maPDB AS NVARCHAR) LIKE ? "); break;
                    case "Mã KH":     sql.append("AND p.maKH LIKE ? ");  break;
                    case "Tên Khách": sql.append("AND k.hoTen LIKE ? ");                   break;
                }
            }
            sql.append("ORDER BY p.maPDB DESC");
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            int idx = 1;
            if (!trangThai.equals("Tất cả")) ps.setString(idx++, "%" + trangThai + "%");
            if (!criteria.equals("Tất cả") && !keyword.isEmpty()) ps.setString(idx, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            int count = 0;
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("maPDB"), rs.getString("maKH"),
                    rs.getString("hoTen"), rs.getString("ngayDat"), rs.getString("ngayDen"),
                    rs.getString("trangThai")
                });
                count++;
            }
            lblCount.setText("Tổng: " + count + " phiếu");
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void huyPhieuDat() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần hủy!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int maPDB = (int) model.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "Hủy phiếu đặt bàn số: " + maPDB + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try (Connection conn = ConnectDB.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE PhieuDatBan SET trangThai=N'Đã hủy' WHERE maPDB=?");
            ps.setInt(1, maPDB);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Hủy phiếu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                doSearch();
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private JButton makeBtn(String txt, Color bg) {
        JButton btn = new JButton(txt);
        btn.setFont(F_BTN); btn.setBackground(bg); btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(170, 36));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }
}
