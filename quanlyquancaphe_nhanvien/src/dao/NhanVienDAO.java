package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.NhanVien;

/**
 * NhanVienDAO - map đúng với schema thực tế:
 * NhanVien(MaNV, hoTen, gioiTinh, sdt, email, diaChi, ngayVaoLam)
 * 
 * Entity NhanVien dùng: maNV, tenNV, gioiTinh, dienThoai, chucVu
 * → Mapping: hoTen → tenNV | sdt → dienThoai | chucVu = "" (không có trong DB)
 */
public class NhanVienDAO {

    public ArrayList<NhanVien> getAllNhanVien() {
        ArrayList<NhanVien> ds = new ArrayList<>();
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return ds;

            String sql = "SELECT MaNV, hoTen, gioiTinh, sdt, chucVu FROM NhanVien";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ds.add(new NhanVien(
                    rs.getString("MaNV"),
                    rs.getString("hoTen"),
                    rs.getString("gioiTinh"),
                    rs.getString("sdt"),
                    rs.getString("chucVu") != null ? rs.getString("chucVu") : ""
                ));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public boolean themNhanVien(NhanVien nv) {
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return false;

            // INSERT bao gồm chucVu
            String sql = "INSERT INTO NhanVien(MaNV, hoTen, gioiTinh, sdt, chucVu) VALUES(?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getTenNV());     // tenNV → hoTen
            ps.setString(3, nv.getGioiTinh());
            ps.setString(4, nv.getDienThoai()); // dienThoai → sdt
            ps.setString(5, nv.getChucVu());    // chucVu → chucVu
            int rows = ps.executeUpdate();
            conn.close();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean suaNhanVien(NhanVien nv) {
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return false;

            String sql = "UPDATE NhanVien SET hoTen=?, gioiTinh=?, sdt=?, chucVu=? WHERE MaNV=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nv.getTenNV());
            ps.setString(2, nv.getGioiTinh());
            ps.setString(3, nv.getDienThoai());
            ps.setString(4, nv.getChucVu());
            ps.setString(5, nv.getMaNV());
            int rows = ps.executeUpdate();
            conn.close();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaNhanVien(String maNV) {
        try {
            Connection conn = ConnectDB.getConnection();
            if (conn == null) return false;

            String sql = "DELETE FROM NhanVien WHERE MaNV=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maNV);
            int rows = ps.executeUpdate();
            conn.close();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}