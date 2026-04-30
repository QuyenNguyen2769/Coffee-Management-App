package gui;

import bus.NhanVienBUS;
import entity.NhanVien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel Quản Lý Nhân Viên — nhúng bên trong TrangChu (không phải JFrame riêng)
 */
public class PanelQuanLyNhanVien extends JPanel {

    private JTextField txtMaNV, txtTenNV, txtGioiTinh, txtDienThoai, txtChucVu;
    private JTable     tableNhanVien;
    private DefaultTableModel model;
    private JButton    btnThem, btnSua, btnXoa, btnLamMoi;

    private NhanVienBUS bus = new NhanVienBUS();

    public PanelQuanLyNhanVien() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 246, 250));
        setBorder(new EmptyBorder(15, 20, 15, 20));

        // ── Tiêu đề ───────────────────────────────────────────────────────
        JLabel lblTitle = new JLabel("👥  Quản Lý Nhân Viên");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(44, 28, 18));
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // ── Panel Form nhập liệu ──────────────────────────────────────────
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 190, 170), 1),
            new EmptyBorder(15, 20, 15, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Mã NV:", "Tên NV:", "Giới Tính:", "Điện Thoại:", "Chức Vụ:"};
        JTextField[] fields = new JTextField[5];
        for (int i = 0; i < labels.length; i++) {
            // Cột label
            gbc.gridx = (i % 2 == 0) ? 0 : 3;
            gbc.gridy = i / 2;
            gbc.weightx = 0;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lbl.setForeground(new Color(60, 40, 20));
            panelForm.add(lbl, gbc);

            // Cột textfield
            gbc.gridx = (i % 2 == 0) ? 1 : 4;
            gbc.weightx = 1;
            fields[i] = new JTextField();
            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            fields[i].setPreferredSize(new Dimension(180, 30));
            panelForm.add(fields[i], gbc);

            // Khoảng cách giữa 2 cột
            if (i % 2 == 0) {
                gbc.gridx = 2;
                gbc.weightx = 0.1;
                panelForm.add(new JLabel(), gbc);
            }
        }

        txtMaNV     = fields[0];
        txtTenNV    = fields[1];
        txtGioiTinh = fields[2];
        txtDienThoai= fields[3];
        txtChucVu   = fields[4];

        // ── Panel Nút ─────────────────────────────────────────────────────
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBtn.setBackground(Color.WHITE);

        btnThem   = makeButton("Thêm",    new Color(39, 174, 96));
        btnSua    = makeButton("Sửa",     new Color(41, 128, 185));
        btnXoa    = makeButton("Xóa",     new Color(192, 57, 43));
        btnLamMoi = makeButton("Làm Mới", new Color(120, 120, 120));

        panelBtn.add(btnThem);
        panelBtn.add(btnSua);
        panelBtn.add(btnXoa);
        panelBtn.add(btnLamMoi);

        // Ghép form + nút vào panel bắc
        JPanel panelNorth = new JPanel(new BorderLayout(0, 10));
        panelNorth.setBackground(new Color(245, 246, 250));
        panelNorth.add(panelForm, BorderLayout.CENTER);
        panelNorth.add(panelBtn,  BorderLayout.SOUTH);
        add(panelNorth, BorderLayout.NORTH);

        // ── Bảng dữ liệu ─────────────────────────────────────────────────
        String[] cols = {"Mã NV", "Tên Nhân Viên", "Giới Tính", "Điện Thoại", "Chức Vụ"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableNhanVien = new JTable(model);
        tableNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableNhanVien.setRowHeight(30);
        tableNhanVien.setSelectionBackground(new Color(101, 67, 33));
        tableNhanVien.setSelectionForeground(Color.WHITE);
        tableNhanVien.setGridColor(new Color(230, 220, 210));
        tableNhanVien.setIntercellSpacing(new Dimension(0, 1));

        // Header table
        JTableHeader header = tableNhanVien.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(44, 28, 18));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        // Căn giữa các ô
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++)
            tableNhanVien.getColumnModel().getColumn(i).setCellRenderer(center);

        JScrollPane scroll = new JScrollPane(tableNhanVien);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(210, 190, 170), 1));
        add(scroll, BorderLayout.CENTER);

        // ── Load dữ liệu ─────────────────────────────────────────────────
        loadNhanVien();

        // ── Sự kiện click bảng → điền vào form ───────────────────────────
        tableNhanVien.getSelectionModel().addListSelectionListener(e -> {
            int row = tableNhanVien.getSelectedRow();
            if (row >= 0) {
                txtMaNV     .setText(model.getValueAt(row, 0).toString());
                txtTenNV    .setText(model.getValueAt(row, 1).toString());
                txtGioiTinh .setText(model.getValueAt(row, 2).toString());
                txtDienThoai.setText(model.getValueAt(row, 3).toString());
                txtChucVu   .setText(model.getValueAt(row, 4).toString());
            }
        });

        // ── Sự kiện nút ──────────────────────────────────────────────────
        btnLamMoi.addActionListener(e -> {
            txtMaNV.setText(""); txtTenNV.setText(""); txtGioiTinh.setText("");
            txtDienThoai.setText(""); txtChucVu.setText("");
            tableNhanVien.clearSelection();
        });

        btnThem.addActionListener(e -> {
            NhanVien nv = getFormData();
            if (nv == null) return;
            if (bus.themNhanVien(nv)) {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                loadNhanVien();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSua.addActionListener(e -> {
            NhanVien nv = getFormData();
            if (nv == null) return;
            if (bus.suaNhanVien(nv)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadNhanVien();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnXoa.addActionListener(e -> {
            String maNV = txtMaNV.getText().trim();
            if (maNV.isEmpty()) { JOptionPane.showMessageDialog(this, "Chọn nhân viên cần xóa!"); return; }
            int opt = JOptionPane.showConfirmDialog(this, "Xác nhận xóa nhân viên " + maNV + "?", "Xóa", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                if (bus.xoaNhanVien(maNV)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadNhanVien();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void loadNhanVien() {
        model.setRowCount(0);
        try {
            ArrayList<NhanVien> ds = bus.getAllNhanVien();
            for (NhanVien nv : ds) {
                model.addRow(new Object[]{
                    nv.getMaNV(), nv.getTenNV(), nv.getGioiTinh(),
                    nv.getDienThoai(), nv.getChucVu()
                });
            }
        } catch (Exception e) {
            // DB chưa kết nối — bỏ qua
        }
    }

    private NhanVien getFormData() {
        String maNV = txtMaNV.getText().trim();
        String tenNV = txtTenNV.getText().trim();
        if (maNV.isEmpty() || tenNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ Mã NV và Tên NV!");
            return null;
        }
        return new NhanVien(maNV, tenNV, txtGioiTinh.getText().trim(),
                            txtDienThoai.getText().trim(), txtChucVu.getText().trim());
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(110, 34));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            Color orig = bg;
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(bg.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(orig);
            }
        });
        return btn;
    }
}
