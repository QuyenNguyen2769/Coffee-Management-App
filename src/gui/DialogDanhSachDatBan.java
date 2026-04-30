package gui;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import connectDB.ConnectDB;

public class DialogDanhSachDatBan extends JDialog {

    private DefaultTableModel model;
    private JTable table;
    private PanelQuanLyPhongBan parentPanel;

    public DialogDanhSachDatBan(Window owner, PanelQuanLyPhongBan parent) {
        super(owner, "Danh Sách Đặt Trước", ModalityType.APPLICATION_MODAL);
        this.parentPanel = parent;
        setSize(800, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // Header
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        pnlHeader.setBackground(new Color(111, 78, 55));
        JLabel lblTitle = new JLabel("DANH SÁCH BÀN ĐÃ ĐẶT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // Center Table
        String[] cols = {"Mã Phiếu", "Tên Khách Hàng", "Số Điện Thoại", "Giờ Đến", "Bàn Cấp", "Trạng Thái"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp
            }
        };
        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);

        // Footer Buttons
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pnlFooter.setBackground(Color.WHITE);
        
        JButton btnNhanBan = new JButton("Nhận Bàn Ngay");
        btnNhanBan.setBackground(new Color(34, 139, 34));
        btnNhanBan.setForeground(Color.WHITE);
        btnNhanBan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNhanBan.setFocusPainted(false);
        
        JButton btnDong = new JButton("Đóng");
        btnDong.setBackground(new Color(220, 53, 69));
        btnDong.setForeground(Color.WHITE);
        btnDong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDong.setFocusPainted(false);

        pnlFooter.add(btnNhanBan);
        pnlFooter.add(btnDong);
        add(pnlFooter, BorderLayout.SOUTH);

        // Actions
        btnDong.addActionListener(e -> dispose());
        
        btnNhanBan.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu đặt bàn để nhận!");
                return;
            }
            String trangThai = (String) model.getValueAt(row, 5);
            if (!trangThai.equalsIgnoreCase("Đã đặt")) {
                JOptionPane.showMessageDialog(this, "Chỉ có thể nhận bàn với các phiếu đang ở trạng thái 'Đã đặt'!");
                return;
            }
            
            // Xử lý nhận bàn
            String idPhieu = model.getValueAt(row, 0).toString();
            String banCap = model.getValueAt(row, 4).toString();
            
            int opt = JOptionPane.showConfirmDialog(this, "Xác nhận nhận bàn " + banCap + " cho phiếu " + idPhieu + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = ConnectDB.getConnection();
                    if (conn != null) {
                        conn.setAutoCommit(false);
                        
                        // Update PhieuDatBan
                        String sql1 = "UPDATE PhieuDatBan SET trangThai = N'Đã đến' WHERE maPDB = ?";
                        PreparedStatement ps1 = conn.prepareStatement(sql1);
                        ps1.setInt(1, Integer.parseInt(idPhieu));
                        ps1.executeUpdate();
                        
                        // Update Ban
                        String sqlGetMaBan = "SELECT maBan FROM ChiTietPhieuDatBan WHERE maPDB = ?";
                        PreparedStatement psGet = conn.prepareStatement(sqlGetMaBan);
                        psGet.setInt(1, Integer.parseInt(idPhieu));
                        ResultSet rsGet = psGet.executeQuery();
                        if (rsGet.next()) {
                            int maBan = rsGet.getInt("maBan");
                            new dao.BanDAO().capNhatTrangThaiBan(maBan, "Đang dùng");
                        }
                        
                        conn.commit();
                        conn.close();
                        
                        JOptionPane.showMessageDialog(this, "Nhận bàn thành công! Hệ thống sẽ mở màn hình gọi món.");
                        this.dispose();
                        
                        // Mở DialogGoiMon
                        DialogGoiMon dialogGoiMon = new DialogGoiMon(SwingUtilities.getWindowAncestor(parentPanel), banCap);
                        dialogGoiMon.setVisible(true);
                        
                        // Refresh bảng
                        if (parentPanel != null) {
                            parentPanel.loadTables("Tất cả", "Tất cả", "Tất cả", "");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi nhận bàn!");
                }
            }
        });

        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return;
            
            String sql = "SELECT p.maPDB, k.hoTen, k.sdt, p.ngayDen, b.soBan, p.trangThai " +
                         "FROM PhieuDatBan p " +
                         "JOIN KhachHang k ON p.maKH = k.maKH " +
                         "JOIN ChiTietPhieuDatBan c ON p.maPDB = c.maPDB " +
                         "JOIN Ban b ON c.maBan = b.maBan " +
                         "ORDER BY p.ngayDen ASC";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            while (rs.next()) {
                String maPDB = rs.getString("maPDB");
                String tenKH = rs.getString("hoTen");
                String sdt = rs.getString("sdt");
                String ngayDen = sdf.format(rs.getTimestamp("ngayDen"));
                String banCap = String.format("%03d", rs.getInt("soBan"));
                String trangThai = rs.getString("trangThai");
                
                model.addRow(new Object[]{maPDB, tenKH, sdt, ngayDen, banCap, trangThai});
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
