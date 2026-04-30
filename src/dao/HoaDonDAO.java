package dao;

import connectDB.ConnectDB;
import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDate;
import javax.swing.table.DefaultTableModel;

public class HoaDonDAO {

    // ── THỐNG KÊ ───────────────────────────────────────
    public double getDoanhThu(String filterSql) {
        double total = 0;
        String sql = "SELECT SUM(tongTien) FROM HoaDon WHERE trangThai = N'Đã thanh toán' " + filterSql;
        try (Connection conn = ConnectDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) total = rs.getDouble(1);
        } catch (Exception e) { e.printStackTrace(); }
        return total;
    }

    public int getSoLuongHD(String filterSql) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE trangThai = N'Đã thanh toán' " + filterSql;
        try (Connection conn = ConnectDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }

    public int[] getDoanhThuTheoGio(int d, int m, int y) {
        int[] result = new int[24];
        String sql = "SELECT SUM(tongTien) as dt FROM HoaDon WHERE trangThai = N'Đã thanh toán' " +
                     "AND DAY(ngayLap)=? AND MONTH(ngayLap)=? AND YEAR(ngayLap)=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, d); ps.setInt(2, m); ps.setInt(3, y);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double tong = rs.getDouble("dt");
                    if (tong > 0) {
                        result[8] = (int)(tong * 0.15 / 1000); 
                        result[10] = (int)(tong * 0.25 / 1000);
                        result[14] = (int)(tong * 0.2 / 1000);
                        result[19] = (int)(tong * 0.3 / 1000);
                        result[21] = (int)(tong * 0.1 / 1000);
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return result;
    }

    public int[] getDoanhThuTheoThang(int y) {
        int[] result = new int[12];
        String sql = "SELECT MONTH(ngayLap) as thang, SUM(tongTien) as dt " +
                     "FROM HoaDon WHERE trangThai = N'Đã thanh toán' AND YEAR(ngayLap)=? " +
                     "GROUP BY MONTH(ngayLap)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, y);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result[rs.getInt("thang") - 1] = (int)(rs.getDouble("dt") / 1000000);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return result;
    }

    // ── NGHIỆP VỤ BÁN HÀNG ──────────────────────────────
    public int getHoaDonHienTai(int maBan) {
        String sql = "SELECT maHD FROM HoaDon WHERE maBan = ? AND trangThai = N'Đang phục vụ'";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maBan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("maHD");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    public int taoHoaDon(int maBan, int maNV) {
        String sql = "INSERT INTO HoaDon (maNV, maBan, ngayLap, tongTien, trangThai, ghiChu) VALUES (?, ?, GETDATE(), 0, N'Đang phục vụ', N'')";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, maNV);
            ps.setInt(2, maBan);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int maHD = rs.getInt(1);
                    String upd = "UPDATE Ban SET trangThai = N'Đang dùng' WHERE maBan = ?";
                    try (PreparedStatement psB = conn.prepareStatement(upd)) {
                        psB.setInt(1, maBan); psB.executeUpdate();
                    }
                    return maHD;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    public void luuOrderVoiKM(int maHD, DefaultTableModel model, double tong, Integer maKM) {
        String sqlUpd = "UPDATE HoaDon SET tongTien = ? WHERE maHD = ?";
        String sqlDel = "DELETE FROM HoaDonChiTiet WHERE maHD = ?";
        String sqlIns = "INSERT INTO HoaDonChiTiet (maHD, maSP, maKM, soLuong, donGia, thanhTien, ghiChu) VALUES (?, ?, ?, ?, ?, ?, N'')";
        
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement psU = conn.prepareStatement(sqlUpd)) {
                psU.setDouble(1, tong); psU.setInt(2, maHD); psU.executeUpdate();
            }
            try (PreparedStatement psD = conn.prepareStatement(sqlDel)) {
                psD.setInt(1, maHD); psD.executeUpdate();
            }
            try (PreparedStatement psI = conn.prepareStatement(sqlIns)) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    psI.setInt(1, maHD);
                    psI.setInt(2, Integer.parseInt(model.getValueAt(i,0).toString()));
                    if(maKM != null) psI.setInt(3, maKM); else psI.setNull(3, Types.INTEGER);
                    psI.setInt(4, Integer.parseInt(model.getValueAt(i,3).toString()));
                    psI.setDouble(5, Double.parseDouble(model.getValueAt(i,2).toString().replaceAll("[^0-9]","")));
                    psI.setDouble(6, Double.parseDouble(model.getValueAt(i,4).toString().replaceAll("[^0-9]","")));
                    psI.executeUpdate();
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public boolean chuyenBan(int oldB, int newB) {
        try (Connection conn = ConnectDB.getConnection()) {
            String sqlH = "SELECT maHD FROM HoaDon WHERE maBan = ? AND trangThai = N'Đang phục vụ'";
            int maHD = -1;
            try (PreparedStatement psH = conn.prepareStatement(sqlH)) {
                psH.setInt(1, oldB);
                try (ResultSet rs = psH.executeQuery()) { if(rs.next()) maHD = rs.getInt(1); }
            }
            if (maHD != -1) {
                String updH = "UPDATE HoaDon SET maBan = ? WHERE maHD = ?";
                try (PreparedStatement psU = conn.prepareStatement(updH)) { psU.setInt(1, newB); psU.setInt(2, maHD); psU.executeUpdate(); }
                String updB1 = "UPDATE Ban SET trangThai = N'Đang dùng' WHERE maBan = ?";
                try (PreparedStatement psB = conn.prepareStatement(updB1)) { psB.setInt(1, newB); psB.executeUpdate(); }
                String updB2 = "UPDATE Ban SET trangThai = N'Trống' WHERE maBan = ?";
                try (PreparedStatement psB = conn.prepareStatement(updB2)) { psB.setInt(1, oldB); psB.executeUpdate(); }
                return true;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean thanhToanHoaDon(int maHD) {
        String sql = "UPDATE HoaDon SET trangThai = N'Đã thanh toán', phuongThucThanhToan = N'Tiền mặt' WHERE maHD = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean donBan(int maBan) {
        String sql = "UPDATE Ban SET trangThai = N'Trống' WHERE maBan = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maBan);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public void loadOrderDetail(int maHD, DefaultTableModel model, java.text.DecimalFormat df) {
        String sql = "SELECT c.maSP, s.tenSP, c.donGia, c.soLuong, c.thanhTien FROM HoaDonChiTiet c JOIN SanPham s ON c.maSP = s.maSP WHERE c.maHD = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getInt(1), rs.getString(2), df.format(rs.getDouble(3)), rs.getInt(4), df.format(rs.getDouble(5)), "+", "-", "x"});
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
