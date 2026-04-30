package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import connectDB.ConnectDB;
import entity.KhachHang;

public class KhachHangDAO {
    
    /**
     * Thêm khách hàng mới vào DB và trả về mã Khách hàng (Identity) vừa được tạo.
     * @return maKH nếu thành công, -1 nếu thất bại
     */
    public int themKhachHang(KhachHang kh) {
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return -1;

            String sql = "INSERT INTO KhachHang(hoTen, sdt, diaChi, diemTichLuy, email, loaiKhach) VALUES(?,?,?,?,?,?)";
            // Lấy ra ID tự tăng (IDENTITY)
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getSdt());
            ps.setString(3, kh.getDiaChi());
            ps.setInt(4, kh.getDiemTichLuy());
            ps.setString(5, kh.getEmail());
            
            int rows = ps.executeUpdate();
            int newMaKH = -1;
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    newMaKH = rs.getInt(1); // Lấy maKH vừa được tự sinh
                }
            }
            conn.close();
            return newMaKH;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Tìm kiếm thông tin khách hàng dựa vào số điện thoại.
     * @return Đối tượng KhachHang nếu tìm thấy, null nếu không tìm thấy
     */
    public KhachHang timKhachHangTheoSDT(String sdt) {
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return null;

            String sql = "SELECT * FROM KhachHang WHERE sdt = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sdt);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                KhachHang kh = new KhachHang(
                    rs.getString("maKH"),
                    rs.getString("hoTen"),
                    rs.getString("sdt"),
                    rs.getString("diaChi"),
                    rs.getInt("diemTichLuy"),
                    rs.getString("email"),
                    rs.getString("loaiKhach")  // Fix 2: Đọc loaiKhach từ DB
                );
                conn.close();
                return kh;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Lấy danh sách Top khách hàng chi tiêu nhiều nhất.
     */
    public java.util.ArrayList<Object[]> getTopKhachHang() {
        java.util.ArrayList<Object[]> list = new java.util.ArrayList<>();
        try {
            Connection conn = ConnectDB.getConnection();
            String sql = "SELECT TOP 10 k.maKH, k.hoTen, k.sdt, COUNT(h.maHD) as soLan, SUM(h.tongTien) as tongChi, k.loaiKhach " +
                         "FROM KhachHang k JOIN HoaDon h ON k.maKH = h.maKH " +
                         "WHERE h.trangThai = N'Đã thanh toán' " +
                         "GROUP BY k.maKH, k.hoTen, k.sdt, k.loaiKhach " +
                         "ORDER BY SUM(h.tongTien) DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int rank = 1;
            while(rs.next()) {
                list.add(new Object[]{
                    rank++, rs.getString("hoTen"), rs.getString("sdt"), 
                    rs.getInt("soLan"), String.format("%,.0fđ", rs.getDouble("tongChi")), 
                    rs.getString("loaiKhach")
                });
            }
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
