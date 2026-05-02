package bus;

import dao.NhanVienDAO;
import entity.NhanVien;
import java.util.ArrayList;

public class NhanVienBUS {

    NhanVienDAO dao = new NhanVienDAO();

    public ArrayList<NhanVien> getAllNhanVien() {
        return dao.getAllNhanVien();
    }

    public boolean themNhanVien(NhanVien nv) {
        return dao.themNhanVien(nv);
    }

    public boolean suaNhanVien(NhanVien nv) {
        return dao.suaNhanVien(nv);
    }

    public boolean xoaNhanVien(String maNV) {
        return dao.xoaNhanVien(maNV);
    }
}