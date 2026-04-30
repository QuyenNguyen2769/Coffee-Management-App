package dao;

import connectDB.ConnectDB;
import entity.SanPham;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SanPhamDAO {

    public ArrayList<SanPham> getAllSanPham() {
        ArrayList<SanPham> ds = new ArrayList<>();
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return ds;

            String sql = "SELECT * FROM SanPham";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SanPham sp = new SanPham(
                    rs.getString("maSP"),
                    rs.getString("maThue"),
                    rs.getString("tenSP"),
                    rs.getDouble("giaBan"),
                    rs.getString("donViTinh"),
                    rs.getString("moTa"),
                    rs.getInt("soLuongTon"),
                    rs.getString("hanSuDung"),
                    rs.getDate("ngayNhap")
                );
                ds.add(sp);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<SanPham> timSanPhamTheoTen(String ten) {
        ArrayList<SanPham> ds = new ArrayList<>();
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return ds;

            String sql = "SELECT * FROM SanPham WHERE tenSP LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + ten + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SanPham sp = new SanPham(
                    rs.getString("maSP"),
                    rs.getString("maThue"),
                    rs.getString("tenSP"),
                    rs.getDouble("giaBan"),
                    rs.getString("donViTinh"),
                    rs.getString("moTa"),
                    rs.getInt("soLuongTon"),
                    rs.getString("hanSuDung"),
                    rs.getDate("ngayNhap")
                );
                ds.add(sp);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }
}
