package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import connectDB.ConnectDB;
import entity.KhuyenMai;

public class KhuyenMaiDAO {
    
    /**
     * Lấy danh sách khuyến mãi đang có hiệu lực và thỏa mãn điều kiện tối thiểu
     * @param tongTien Tiền hàng tối thiểu để được áp dụng
     * @return Danh sách KhuyenMai
     */
    public ArrayList<KhuyenMai> getKhuyenMaiHopLe(double tongTien) {
        ArrayList<KhuyenMai> list = new ArrayList<>();
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return list;

            String sql = "SELECT * FROM KhuyenMai WHERE ngayKetThuc >= GETDATE() AND ngayBatDau <= GETDATE()";
            PreparedStatement ps = conn.prepareStatement(sql);
            // Bỏ setDouble(1, tongTien) vì ta không cần truyền tham số nữa
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                KhuyenMai km = new KhuyenMai(
                    String.valueOf(rs.getInt("maKM")),
                    rs.getString("tenKM"),
                    rs.getDouble("mucGiamGia"),
                    rs.getDate("ngayBatDau"),
                    rs.getDate("ngayKetThuc"),
                    rs.getDouble("dieuKienToiThieu")
                );
                list.add(km);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
