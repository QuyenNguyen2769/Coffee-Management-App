package gui;

import bus.NhanVienBUS;
import entity.NhanVien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class QuanLyNhanVien extends JFrame {

    JTextField txtMaNV, txtTenNV, txtGioiTinh, txtDienThoai, txtChucVu;
    JTable tableNhanVien;
    DefaultTableModel model;

    JButton btnThem, btnSua, btnXoa, btnLamMoi;

    NhanVienBUS bus = new NhanVienBUS();

    public QuanLyNhanVien() {

        setTitle("Quản Lý Nhân Viên");
        setSize(700,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // ===== PANEL FORM =====

        JPanel panelForm = new JPanel(new GridLayout(5,2,5,5));

        panelForm.add(new JLabel("Mã NV"));
        txtMaNV = new JTextField();
        panelForm.add(txtMaNV);

        panelForm.add(new JLabel("Tên NV"));
        txtTenNV = new JTextField();
        panelForm.add(txtTenNV);

        panelForm.add(new JLabel("Giới tính"));
        txtGioiTinh = new JTextField();
        panelForm.add(txtGioiTinh);

        panelForm.add(new JLabel("Điện thoại"));
        txtDienThoai = new JTextField();
        panelForm.add(txtDienThoai);

        panelForm.add(new JLabel("Chức vụ"));
        txtChucVu = new JTextField();
        panelForm.add(txtChucVu);

        add(panelForm, BorderLayout.NORTH);

        // ===== TABLE =====

        String[] column = {"MaNV","TenNV","GioiTinh","DienThoai","ChucVu"};

        model = new DefaultTableModel(column,0);

        tableNhanVien = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(tableNhanVien);

        add(scrollPane, BorderLayout.CENTER);

        // ===== BUTTON =====

        JPanel panelButton = new JPanel();

        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");

        panelButton.add(btnThem);
        panelButton.add(btnSua);
        panelButton.add(btnXoa);
        panelButton.add(btnLamMoi);

        add(panelButton, BorderLayout.SOUTH);

        // ===== LOAD DATA =====

        loadNhanVien();

        // ===== EVENT CLICK TABLE =====

        tableNhanVien.getSelectionModel().addListSelectionListener(e -> {

            int row = tableNhanVien.getSelectedRow();

            if(row >= 0){

                txtMaNV.setText(model.getValueAt(row,0).toString());
                txtTenNV.setText(model.getValueAt(row,1).toString());
                txtGioiTinh.setText(model.getValueAt(row,2).toString());
                txtDienThoai.setText(model.getValueAt(row,3).toString());
                txtChucVu.setText(model.getValueAt(row,4).toString());

            }

        });

    }

    // ===== LOAD DATA FROM DATABASE =====

    public void loadNhanVien(){

        model.setRowCount(0);

        ArrayList<NhanVien> ds = bus.getAllNhanVien();

        for(NhanVien nv : ds){

            Object[] row = {

                    nv.getMaNV(),
                    nv.getTenNV(),
                    nv.getGioiTinh(),
                    nv.getDienThoai(),
                    nv.getChucVu()

            };

            model.addRow(row);

        }

    }

}