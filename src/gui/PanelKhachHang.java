package gui;

import connectDB.ConnectDB;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * PanelKhachHang.java — Quản lý Khách Hàng
 * - Thêm / Sửa / Xóa / Làm mới
 */
public class PanelKhachHang extends JPanel {

    private static final Color C_BG       = new Color(245, 246, 250);
    private static final Color C_WHITE     = Color.WHITE;
    private static final Color C_TITLE     = new Color(44, 28, 18);
    private static final Color C_HDR_BG    = new Color(101, 67, 33);
    private static final Color C_HDR_FG    = new Color(62, 39, 35);
    private static final Color C_ROW_ODD   = Color.WHITE;
    private static final Color C_ROW_EVEN  = new Color(245, 240, 235);
    private static final Color C_SELECT    = new Color(210, 180, 140);
    private static final Color C_BTN_ADD   = new Color(46, 139, 87);
    private static final Color C_BTN_EDIT  = new Color(70, 130, 180);
    private static final Color C_BTN_DEL   = new Color(178, 34, 34);
    private static final Color C_BTN_RESET = new Color(120, 120, 120);

    private static final Font F_LABEL = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font F_INPUT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font F_BTN   = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font F_TABLE = new Font("Segoe UI", Font.PLAIN, 13);

    private JTextField txtHoTen, txtSDT, txtDiaChi, txtEmail;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    private String selectedMaKH = null;

    public PanelKhachHang() {
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
        JLabel lbl = new JLabel("Quản Lý Khách Hàng");
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

        txtHoTen  = makeField();
        txtSDT    = makeField();
        txtDiaChi = makeField();
        txtEmail  = makeField();

        addRow(form, gbc, 0, "Họ Tên:",   txtHoTen,  "Số ĐT:",  txtSDT);
        addRow(form, gbc, 1, "Địa Chỉ:",  txtDiaChi, "Email:",   txtEmail);

        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 5; gbc.weightx = 1;
        JLabel note = new JLabel(
            "<html><i>Điểm tích lũy &amp; loại khách chỉ được cập nhật từ <b>Tìm Kiếm → Tìm Khách Hàng</b></i></html>");
        note.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        form.add(note, gbc);
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
        String[] cols = {"Mã KH", "Họ Tên", "Số ĐT", "Địa Chỉ", "Email", "Điểm TL", "Loại Khách"};
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
                if (!sel && col == 6 && v != null) {
                    setForeground(KhachHangHelper.mauLoai(v.toString()));
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                }
                setHorizontalAlignment(col == 0 || col == 5 ? SwingConstants.CENTER : SwingConstants.LEFT);
                setBorder(new EmptyBorder(0, 6, 0, 6));
                return this;
            }
        });

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
        model.setRowCount(0); selectedMaKH = null;
        try (Connection conn = ConnectDB.getConnection()) {
            if (conn == null) return;
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT maKH,hoTen,sdt,diaChi,email,diemTichLuy,loaiKhach FROM KhachHang ORDER BY maKH");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("maKH"), rs.getString("hoTen"), rs.getString("sdt"),
                    nullSafe(rs.getString("diaChi")), nullSafe(rs.getString("email")),
                    rs.getInt("diemTichLuy"), rs.getString("loaiKhach")
                });
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        selectedMaKH = str(model.getValueAt(row, 0));
        txtHoTen .setText(str(model.getValueAt(row, 1)));
        txtSDT   .setText(str(model.getValueAt(row, 2)));
        txtDiaChi.setText(str(model.getValueAt(row, 3)));
        txtEmail .setText(str(model.getValueAt(row, 4)));
    }

    private void them() {
        if (!validate2()) return;
        try (Connection conn = ConnectDB.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO KhachHang(hoTen,sdt,diaChi,diemTichLuy,email,loaiKhach) VALUES(?,?,?,0,?,?)");
            ps.setString(1, txtHoTen.getText().trim());
            ps.setString(2, txtSDT.getText().trim());
            ps.setString(3, txtDiaChi.getText().trim());
            ps.setString(4, txtEmail.getText().trim());
            ps.setString(5, KhachHangHelper.xepLoai(0));
            ps.executeUpdate();
            msg("Thêm khách hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadData(); lamMoi();
        } catch (Exception ex) { msg("Thêm thất bại!\n"+ex.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE); }
    }

    private void sua() {
        if (selectedMaKH == null) { msg("Vui lòng chọn khách hàng!","Cảnh báo",JOptionPane.WARNING_MESSAGE); return; }
        if (!validate2()) return;
        try (Connection conn = ConnectDB.getConnection()) {
            PreparedStatement psD = conn.prepareStatement("SELECT diemTichLuy FROM KhachHang WHERE maKH=?");
            psD.setString(1, selectedMaKH); ResultSet rs = psD.executeQuery();
            int diem = rs.next() ? rs.getInt("diemTichLuy") : 0;

            PreparedStatement ps = conn.prepareStatement(
                "UPDATE KhachHang SET hoTen=?,sdt=?,diaChi=?,email=?,loaiKhach=? WHERE maKH=?");
            ps.setString(1, txtHoTen.getText().trim());
            ps.setString(2, txtSDT.getText().trim());
            ps.setString(3, txtDiaChi.getText().trim());
            ps.setString(4, txtEmail.getText().trim());
            ps.setString(5, KhachHangHelper.xepLoai(diem));
            ps.setString(6, selectedMaKH);
            ps.executeUpdate();
            msg("Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadData(); lamMoi();
        } catch (Exception ex) { msg("Cập nhật thất bại!\n"+ex.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE); }
    }

    private void xoa() {
        if (selectedMaKH == null) { msg("Vui lòng chọn khách hàng!","Cảnh báo",JOptionPane.WARNING_MESSAGE); return; }
        if (JOptionPane.showConfirmDialog(this,"Xác nhận xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try (Connection conn = ConnectDB.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM KhachHang WHERE maKH=?");
            ps.setString(1, selectedMaKH); ps.executeUpdate();
            msg("Xóa thành công!","Thành công",JOptionPane.INFORMATION_MESSAGE);
            loadData(); lamMoi();
        } catch (Exception ex) { msg("Xóa thất bại!\n"+ex.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE); }
    }

    private void lamMoi() {
        txtHoTen.setText(""); txtSDT.setText(""); txtDiaChi.setText(""); txtEmail.setText("");
        selectedMaKH = null; table.clearSelection();
    }

    private boolean validate2() {
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
        btn.addMouseListener(new MouseAdapter() {
            final Color o = bg;
            public void mouseEntered(MouseEvent e) { btn.setBackground(o.darker()); }
            public void mouseExited (MouseEvent e) { btn.setBackground(o); }
        });
        return btn;
    }
    private String str(Object o)  { return o==null?"":o.toString(); }
    private String nullSafe(String s) { return s==null?"":s; }
    private void   msg(String m,String t,int type){ JOptionPane.showMessageDialog(this,m,t,type); }
}
