package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.Ban;

public class BanDAO {
    
    /**
     * Lấy danh sách bàn TRỐNG thông minh:
     * - Bàn đó không phải là 'Đang dùng'.
     * - Bàn đó KHÔNG bị kẹt trong bất kỳ Phiếu Đặt Bàn nào có thời gian đến (ngayDen)
     *   nằm trong khoảng +- 2 tiếng so với thời gian khách yêu cầu (gioDen).
     */
    public ArrayList<Ban> getBanTrong(String khuVuc, java.time.LocalDateTime gioDen) {
        ArrayList<Ban> ds = new ArrayList<>();
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return ds;

            // Câu truy vấn kiểm tra:
            // Lấy tất cả bàn (không phải Đang dùng)
            // Trừ đi (-) những bàn đang có trong ChiTietPhieuDatBan mà thời gian chênh lệch <= 120 phút (2 tiếng)
            String sql = "SELECT * FROM Ban WHERE trangThai != N'Đang dùng' ";
            
            if (khuVuc != null && !khuVuc.trim().isEmpty() && !khuVuc.equals("Tất cả")) {
                sql += " AND khuVuc = ? ";
            }
            
            sql += " AND maBan NOT IN ("
                 + "     SELECT c.maBan FROM ChiTietPhieuDatBan c "
                 + "     JOIN PhieuDatBan p ON c.maPDB = p.maPDB "
                 + "     WHERE p.trangThai = N'Chờ nhận bàn' " // Hoặc trạng thái tương đương
                 + "     AND ABS(DATEDIFF(MINUTE, p.ngayDen, ?)) <= 120"
                 + " ) ORDER BY sucChua DESC";

            PreparedStatement ps = conn.prepareStatement(sql);
            
            int paramIndex = 1;
            if (khuVuc != null && !khuVuc.trim().isEmpty() && !khuVuc.equals("Tất cả")) {
                ps.setString(paramIndex++, khuVuc);
            }
            
            // Chuyển LocalDateTime thành java.sql.Timestamp
            java.sql.Timestamp tsGioDen = java.sql.Timestamp.valueOf(gioDen);
            ps.setTimestamp(paramIndex, tsGioDen);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Ban b = new Ban(
                    rs.getInt("soBan"),
                    rs.getString("trangThai"),
                    rs.getInt("sucChua"),
                    rs.getString("khuVuc")
                );
                ds.add(b);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }
}
