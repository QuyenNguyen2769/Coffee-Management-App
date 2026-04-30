package dao;

import connectDB.ConnectDB;
import entity.TaiKhoan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TaiKhoanDAO {

    /**
     * Kiểm tra đăng nhập đúng với schema thực tế:
     * TaiKhoan(userName, maNV, password, vaiTro, trangThai)
     */
    public TaiKhoan checkLogin(String username, String password) {
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return null;

            // Tên cột đúng trong DB: userName, password (không phải MatKhau)
            String sql = "SELECT * FROM TaiKhoan WHERE userName = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                TaiKhoan tk = new TaiKhoan(
                    rs.getString("userName"),
                    rs.getString("maNV"),
                    rs.getString("password"),
                    rs.getString("vaiTro") != null ? rs.getString("vaiTro") : "Nhân viên",
                    rs.getString("trangThai")
                );
                conn.close();
                return tk;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
