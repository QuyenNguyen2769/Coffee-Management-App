package gui;

import connectDB.ConnectDB;
import entity.NhanVien;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * PanelNhanVien.java — Quản lý Nhân Viên
 */
public class PanelNhanVien extends JPanel {

    private static final Color C_BG        = new Color(245, 246, 250);
    private static final Color C_WHITE      = Color.WHITE;
    private static final Color C_TITLE      = new Color(44, 28, 18);
    private static final Color C_HDR_BG     = new Color(101, 67, 33);
    private static final Color C_HDR_FG     = new Color(62, 39, 35);
    private static final Color C_ROW_ODD    = Color.WHITE;
    private static final Color C_ROW_EVEN   = new Color(245, 240, 235);
    private static final Color C_SELECT     = new Color(210, 180, 140);
    private static final Color C_BTN_ADD    = new Color(46, 139, 87);
    private static final Color C_BTN_EDIT   = new Color(70, 130, 180);
    private static final Color C_BTN_DEL    = new Color(178, 34, 34);
    private static final Color C_BTN_RESET  = new Color(120, 120, 120);
    private static final Font  F_LABEL      = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font  F_INPUT      = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  F_TITLE      = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font  F_BTN        = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font  F_TABLE      = new Font("Segoe UI", Font.PLAIN, 13);

    private JTextField    txtHoTen, txtSDT, txtChucVu, txtNgayVaoLam;
    private JComboBox<String> cbGioiTinh;
    private JTable        table;
    private DefaultTableModel model;
    private JButton       btnThem, btnSua, btnXoa, btnLamMoi;
    private String        selectedMaNV = null;

    public PanelNhanVien() {
        setLayout(new BorderLayout(10, 10));
        setBackground(C_BG);
        setBorder(new EmptyBorder(15, 20, 15, 20));
        add(buildNorth(),  BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildSouth(),  BorderLayout.SOUTH);
        loadData();
    }

    private JPanel buildNorth() {
        JPanel p = new JPanel(new BorderLayout()); p.setOpaque(false);
        JLabel lbl = new JLabel("Quản Lý Nhân Viên");
        lbl.setFont(F_TITLE); lbl.setForeground(C_TITLE);
        lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
        p.add(lbl, BorderLayout.WEST);
        return p;
    }

    private JPanel buildCenter() {
        JPanel p = new JPanel(new BorderLayout(0, 12)); p.setOpaque(false);
        p.add(buildForm(),  BorderLayout.NORTH);
        p.add(buildTable(), BorderLayout.CENTER);
        return p;
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(C_WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 190, 170), 1),
            new EmptyBorder(15, 20, 15, 20)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtHoTen      = makeField();
        txtSDT        = makeField();
        txtChucVu     = makeField();
        txtNgayVaoLam = makeField();
        txtNgayVaoLam.setToolTipText("Định dạng: YYYY-MM-DD");

        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cbGioiTinh.setFont(F_INPUT);

        addRow(form, gbc, 0, "Họ Tên:",       txtHoTen,      "Số ĐT:",          txtSDT);
        addRow(form, gbc, 1, "Giới Tính:",     cbGioiTinh,    "Ngày Vào Làm:",   txtNgayVaoLam);
        addRow(form, gbc, 2, "Chức Vụ:",       txtChucVu,     "", new JLabel());

        return form;
    }

    private void addRow(JPanel p, GridBagConstraints gbc, int row, String l1, Component c1, String l2, Component c2) {
        gbc.gridy = row; gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.weightx = 0; p.add(makeLabel(l1), gbc);
        gbc.gridx = 1; gbc.weightx = 1; p.add(c1, gbc);
        gbc.gridx = 2; gbc.weightx = 0.05; p.add(new JLabel(), gbc);
        gbc.gridx = 3; gbc.weightx = 0; p.add(makeLabel(l2), gbc);
        gbc.gridx = 4; gbc.weightx = 1; p.add(c2, gbc);
    }

    private JScrollPane buildTable() {
        String[] cols = {"Mã NV", "Họ Tên", "Số ĐT", "Giới Tính", "Ngày Vào Làm", "Chức Vụ"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setFont(F_TABLE); table.setRowHeight(30);
        table.setShowGrid(false);
        table.setSelectionBackground(C_SELECT); table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(C_HDR_BG); header.setForeground(C_HDR_FG);
        header.setPreferredSize(new Dimension(0, 35));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBackground(sel ? C_SELECT : (row % 2 == 0 ? C_ROW_ODD : C_ROW_EVEN));
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

        int[] w = {60, 190, 110, 90, 120, 120};
        for (int i = 0; i < w.length; i++) table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { fillFormFromTable(); }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(210, 190, 170)));
        return sp;
    }

    private JPanel buildSouth() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8)); p.setOpaque(false);
        btnThem   = makeBtn("Thêm",    C_BTN_ADD);
        btnSua    = makeBtn("Sửa",     C_BTN_EDIT);
        btnXoa    = makeBtn("Xóa",    C_BTN_DEL);
        btnLamMoi = makeBtn("Làm Mới", C_BTN_RESET);
        p.add(btnThem); p.add(btnSua); p.add(btnXoa); p.add(btnLamMoi);
        btnThem.addActionListener(e -> them());
        btnSua .addActionListener(e -> sua());
        btnXoa .addActionListener(e -> xoa());
        btnLamMoi.addActionListener(e -> lamMoi());
        return p;
    }

    public void loadData() {
        model.setRowCount(0); selectedMaNV = null;
        try (Connection conn = ConnectDB.getConnection()) {
            if (conn == null) return;
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT maNV, hoTen, sdt, gioiTinh, CONVERT(VARCHAR,ngayVaoLam,23) AS ngayVaoLam, chucVu FROM NhanVien ORDER BY maNV");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("maNV"), rs.getString("hoTen"), rs.getString("sdt"),
                    rs.getString("gioiTinh"), rs.getString("ngayVaoLam"), rs.getString("chucVu")
                });
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        selectedMaNV = str(model.getValueAt(row, 0));
        txtHoTen     .setText(str(model.getValueAt(row, 1)));
        txtSDT       .setText(str(model.getValueAt(row, 2)));
        String gt = str(model.getValueAt(row, 3));
        cbGioiTinh.setSelectedItem(gt.isEmpty() ? "Nam" : gt);
        txtNgayVaoLam.setText(str(model.getValueAt(row, 4)));
        txtChucVu    .setText(str(model.getValueAt(row, 5)));
    }

    private void them() {
        if (!validateForm()) return;
        try (Connection conn = ConnectDB.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO NhanVien(hoTen,sdt,gioiTinh,ngayVaoLam,chucVu) VALUES(?,?,?,?,?)");
            ps.setString(1, txtHoTen.getText().trim());
            ps.setString(2, txtSDT.getText().trim());
            ps.setString(3, cbGioiTinh.getSelectedItem().toString());
            String ngay = txtNgayVaoLam.getText().trim();
            if (ngay.isEmpty()) ps.setNull(4, Types.DATE);
            else                ps.setString(4, ngay);
            ps.setString(5, txtChucVu.getText().trim());
            ps.executeUpdate();
            msg("Thêm nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadData(); lamMoi();
        } catch (Exception ex) { msg("Thêm thất bại!\n"+ex.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE); }
    }

    private void sua() {
        if (selectedMaNV == null) { msg("Vui lòng chọn nhân viên!","Cảnh báo",JOptionPane.WARNING_MESSAGE); return; }
        if (!validateForm()) return;
        try (Connection conn = ConnectDB.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE NhanVien SET hoTen=?,sdt=?,gioiTinh=?,ngayVaoLam=?,chucVu=? WHERE maNV=?");
            ps.setString(1, txtHoTen.getText().trim());
            ps.setString(2, txtSDT.getText().trim());
            ps.setString(3, cbGioiTinh.getSelectedItem().toString());
            String ngay = txtNgayVaoLam.getText().trim();
            if (ngay.isEmpty()) ps.setNull(4, Types.DATE);
            else                ps.setString(4, ngay);
            ps.setString(5, txtChucVu.getText().trim());
            ps.setString(6, selectedMaNV);
            ps.executeUpdate();
            msg("Cập nhật thành công!","Thành công",JOptionPane.INFORMATION_MESSAGE);
            loadData(); lamMoi();
        } catch (Exception ex) { msg("Cập nhật thất bại!\n"+ex.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE); }
    }

    private void xoa() {
        if (selectedMaNV == null) { msg("Vui lòng chọn nhân viên!","Cảnh báo",JOptionPane.WARNING_MESSAGE); return; }
        if (JOptionPane.showConfirmDialog(this,"Xác nhận xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try (Connection conn = ConnectDB.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM NhanVien WHERE maNV=?");
            ps.setString(1, selectedMaNV); ps.executeUpdate();
            msg("Xóa thành công!","Thành công",JOptionPane.INFORMATION_MESSAGE);
            loadData(); lamMoi();
        } catch (Exception ex) { msg("Xóa thất bại!\n"+ex.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE); }
    }

    private void lamMoi() {
        txtHoTen.setText(""); txtSDT.setText(""); txtChucVu.setText(""); txtNgayVaoLam.setText("");
        cbGioiTinh.setSelectedIndex(0); selectedMaNV = null; table.clearSelection();
    }

    private boolean validateForm() {
        if (txtHoTen.getText().trim().isEmpty()) { msg("Vui lòng nhập Họ Tên!","Cảnh báo",JOptionPane.WARNING_MESSAGE); return false; }
        if (txtSDT.getText().trim().isEmpty())   { msg("Vui lòng nhập Số ĐT!","Cảnh báo",JOptionPane.WARNING_MESSAGE); return false; }
        return true;
    }

    private JTextField makeField() {
        JTextField tf = new JTextField(); tf.setFont(F_INPUT);
        tf.setPreferredSize(new Dimension(200, 30)); return tf;
    }
    private JLabel makeLabel(String t) {
        JLabel l = new JLabel(t); l.setFont(F_LABEL); l.setForeground(new Color(60,40,20)); return l;
    }
    private JButton makeBtn(String t, Color bg) {
        JButton btn = new JButton(t);
        btn.setFont(F_BTN); btn.setBackground(bg); btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(140,36));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter(){
            final Color o=bg;
            public void mouseEntered(MouseEvent e){btn.setBackground(o.darker());}
            public void mouseExited (MouseEvent e){btn.setBackground(o);}
        });
        return btn;
    }
    private String str(Object o)  { return o==null?"":o.toString(); }
    private void   msg(String m,String t,int type){ JOptionPane.showMessageDialog(this,m,t,type); }
}
