package dao;

import connectDB.ConnectDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.time.LocalDate;

public class HoaDonDAO {

    // Lấy mã hóa đơn đang phục vụ của một bàn
    public int getHoaDonHienTai(int maBan) {
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return -1;
            String sql = "SELECT maHD FROM HoaDon WHERE maBan = ? AND trangThai = N'Đang phục vụ'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, maBan);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int maHD = rs.getInt("maHD");
                conn.close();
                return maHD;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Tạo hóa đơn mới và trả về maHD
    public int taoHoaDon(int maBan, int maNV) {
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return -1;
            String sql = "INSERT INTO HoaDon (maNV, maBan, ngayLap, tongTien, trangThai, ghiChu) VALUES (?, ?, ?, 0, N'Đang phục vụ', N'')";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, maNV);
            ps.setInt(2, maBan);
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int maHD = rs.getInt(1);
                
                // Cập nhật trạng thái bàn thành 'Đang dùng'
                String updateBan = "UPDATE Ban SET trangThai = N'Đang dùng' WHERE maBan = ?";
                PreparedStatement psBan = conn.prepareStatement(updateBan);
                psBan.setInt(1, maBan);
                psBan.executeUpdate();
                
                conn.close();
                return maHD;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Fix 4: Lưu chi tiết order kèm maKM (Integer null = không có KM)
    public void luuOrderVoiKM(int maHD, DefaultTableModel modelOrder, double tongTien, Integer maKM) {
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return;

            // Cập nhật tổng tiền hóa đơn
            String sqlHD = "UPDATE HoaDon SET tongTien = ? WHERE maHD = ?";
            PreparedStatement psHD = conn.prepareStatement(sqlHD);
            psHD.setDouble(1, tongTien);
            psHD.setInt(2, maHD);
            psHD.executeUpdate();

            // Xóa chi tiết cũ
            String sqlDelete = "DELETE FROM HoaDonChiTiet WHERE maHD = ?";
            PreparedStatement psDelete = conn.prepareStatement(sqlDelete);
            psDelete.setInt(1, maHD);
            psDelete.executeUpdate();

            // Thêm chi tiết mới kèm maKM
            String sqlInsert = "INSERT INTO HoaDonChiTiet (maHD, maSP, maKM, soLuong, donGia, thanhTien, ghiChu) VALUES (?, ?, ?, ?, ?, ?, N'')";
            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);

            for (int i = 0; i < modelOrder.getRowCount(); i++) {
                int maSP = Integer.parseInt(modelOrder.getValueAt(i, 0).toString());
                int soLuong = Integer.parseInt(modelOrder.getValueAt(i, 3).toString());

                String donGiaStr = modelOrder.getValueAt(i, 2).toString().replaceAll("[^0-9]", "");
                double donGia = Double.parseDouble(donGiaStr);

                String thanhTienStr = modelOrder.getValueAt(i, 4).toString().replaceAll("[^0-9]", "");
                double thanhTien = Double.parseDouble(thanhTienStr);

                psInsert.setInt(1, maHD);
                psInsert.setInt(2, maSP);
                if (maKM != null) {
                    psInsert.setInt(3, maKM);
                } else {
                    psInsert.setNull(3, java.sql.Types.INTEGER);
                }
                psInsert.setInt(4, soLuong);
                psInsert.setDouble(5, donGia);
                psInsert.setDouble(6, thanhTien);
                psInsert.executeUpdate();
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lưu chi tiết order từ bảng
    public void luuOrder(int maHD, DefaultTableModel modelOrder, double tongTien) {
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return;
            
            // Cập nhật tổng tiền hóa đơn
            String sqlHD = "UPDATE HoaDon SET tongTien = ? WHERE maHD = ?";
            PreparedStatement psHD = conn.prepareStatement(sqlHD);
            psHD.setDouble(1, tongTien);
            psHD.setInt(2, maHD);
            psHD.executeUpdate();

            // Xóa chi tiết cũ
            String sqlDelete = "DELETE FROM HoaDonChiTiet WHERE maHD = ?";
            PreparedStatement psDelete = conn.prepareStatement(sqlDelete);
            psDelete.setInt(1, maHD);
            psDelete.executeUpdate();

            // Thêm chi tiết mới
            String sqlInsert = "INSERT INTO HoaDonChiTiet (maHD, maSP, soLuong, donGia, thanhTien, ghiChu) VALUES (?, ?, ?, ?, ?, N'')";
            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
            
            for (int i = 0; i < modelOrder.getRowCount(); i++) {
                int maSP = Integer.parseInt(modelOrder.getValueAt(i, 0).toString());
                int soLuong = Integer.parseInt(modelOrder.getValueAt(i, 3).toString());
                
                String donGiaStr = modelOrder.getValueAt(i, 2).toString().replaceAll("[^0-9]", "");
                double donGia = Double.parseDouble(donGiaStr);
                
                String thanhTienStr = modelOrder.getValueAt(i, 4).toString().replaceAll("[^0-9]", "");
                double thanhTien = Double.parseDouble(thanhTienStr);
                
                psInsert.setInt(1, maHD);
                psInsert.setInt(2, maSP);
                psInsert.setInt(3, soLuong);
                psInsert.setDouble(4, donGia);
                psInsert.setDouble(5, thanhTien);
                psInsert.executeUpdate();
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Load chi tiết order vào bảng
    public void loadOrderDetail(int maHD, DefaultTableModel modelOrder, java.text.DecimalFormat df) {
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return;
            String sql = "SELECT c.maSP, s.tenSP, c.donGia, c.soLuong, c.thanhTien " +
                         "FROM HoaDonChiTiet c JOIN SanPham s ON c.maSP = s.maSP " +
                         "WHERE c.maHD = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, maHD);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String maSP = String.valueOf(rs.getInt("maSP"));
                String tenSP = rs.getString("tenSP");
                double donGia = rs.getDouble("donGia");
                int soLuong = rs.getInt("soLuong");
                double thanhTien = rs.getDouble("thanhTien");
                
                modelOrder.addRow(new Object[]{
                    maSP, tenSP, df.format(donGia), soLuong, df.format(thanhTien), "+", "-", "x"
                });
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Chuyển bàn
    public boolean chuyenBan(int maBanCu, int maBanMoi) {
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return false;
            
            // Lấy hóa đơn đang phục vụ của bàn cũ
            String sqlGet = "SELECT maHD FROM HoaDon WHERE maBan = ? AND trangThai = N'Đang phục vụ'";
            PreparedStatement psGet = conn.prepareStatement(sqlGet);
            psGet.setInt(1, maBanCu);
            ResultSet rs = psGet.executeQuery();
            if (rs.next()) {
                int maHD = rs.getInt("maHD");
                
                // Cập nhật maBan trong hóa đơn
                String sqlUpdHD = "UPDATE HoaDon SET maBan = ? WHERE maHD = ?";
                PreparedStatement psUpdHD = conn.prepareStatement(sqlUpdHD);
                psUpdHD.setInt(1, maBanMoi);
                psUpdHD.setInt(2, maHD);
                psUpdHD.executeUpdate();
                
                // Cập nhật trạng thái 2 bàn
                String sqlBanMoi = "UPDATE Ban SET trangThai = N'Đang dùng' WHERE maBan = ?";
                PreparedStatement psBanMoi = conn.prepareStatement(sqlBanMoi);
                psBanMoi.setInt(1, maBanMoi);
                psBanMoi.executeUpdate();
                
                String sqlBanCu = "UPDATE Ban SET trangThai = N'Trống' WHERE maBan = ?";
                PreparedStatement psBanCu = conn.prepareStatement(sqlBanCu);
                psBanCu.setInt(1, maBanCu);
                psBanCu.executeUpdate();
                
                conn.close();
                return true;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Thanh toán hóa đơn
    public boolean thanhToanHoaDon(int maHD) {
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return false;
            String sql = "UPDATE HoaDon SET trangThai = N'Đã thanh toán', phuongThucThanhToan = N'Tiền mặt' WHERE maHD = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, maHD);
            int n = ps.executeUpdate();
            conn.close();
            return n > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Dọn bàn (Kết thúc sử dụng)
    public boolean donBan(int maBan) {
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return false;
            String sql = "UPDATE Ban SET trangThai = N'Trống' WHERE maBan = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, maBan);
            int n = ps.executeUpdate();
            conn.close();
            return n > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
