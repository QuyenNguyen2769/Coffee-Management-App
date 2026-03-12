package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.NhanVien;

public class NhanVienDAO {

    public ArrayList<NhanVien> getAllNhanVien() {

        ArrayList<NhanVien> ds = new ArrayList<>();

        try {

            Connection conn = ConnectDB.getConnection();

            String sql = "SELECT * FROM NhanVien";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String ma = rs.getString("MaNV");
                String ten = rs.getString("TenNV");
                String gt = rs.getString("GioiTinh");
                String dt = rs.getString("DienThoai");
                String cv = rs.getString("ChucVu");

                NhanVien nv = new NhanVien(ma, ten, gt, dt, cv);

                ds.add(nv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }
}